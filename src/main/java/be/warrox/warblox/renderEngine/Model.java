package be.warrox.warblox.renderEngine;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.assimp.*;
import org.lwjgl.PointerBuffer;

import java.io.File;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

public class Model {
    private List<Mesh> meshes;
    private String directory;
    private List<Texture> textures_loaded;

    public Model(String path) {
        meshes = new ArrayList<>();
        textures_loaded = new ArrayList<>();
        loadModel(path);
    }

    public void draw(Shader shader) {
        for(Mesh mesh : meshes){
            mesh.draw(shader);
        }
    }

    private void loadModel(String path) {
        AIScene scene = aiImportFile(path, aiProcess_Triangulate | aiProcess_FlipUVs);

        if (scene == null || scene.mRootNode() == null || (scene.mFlags() & AI_SCENE_FLAGS_INCOMPLETE) != 0) {
            System.err.println("ERROR::ASSIMP:: " + aiGetErrorString());
            return;
        }

        int lastSlashIndex = path.lastIndexOf(File.separator);
        if (lastSlashIndex == -1) {
            lastSlashIndex = path.lastIndexOf('/');
        }

        if (lastSlashIndex != -1) {
            directory = path.substring(0, lastSlashIndex);
        } else {
            directory = "";
        }

        processNode(scene.mRootNode(), scene);
    }

    private void processNode(AINode node, AIScene scene) {
        for (int i = 0; i < node.mNumMeshes(); i++) {
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(node.mMeshes().get(i)));
            meshes.add(processMesh(mesh, scene));
        }

        for (int i = 0; i < node.mNumChildren(); i++) {
            processNode(AINode.create(node.mChildren().get(i)), scene);
        }
    }

    private Mesh processMesh(AIMesh mesh, AIScene scene) {
        List<Vertex> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        List<Texture> textures = new ArrayList<>();

        AIVector3D.Buffer aiVertices = mesh.mVertices();
        AIVector3D.Buffer aiNormals = mesh.mNormals();
        AIVector3D.Buffer aiTexCoords = mesh.mTextureCoords(0);

        for (int i = 0; i < mesh.mNumVertices(); i++) {
            AIVector3D aiVertex = aiVertices.get(i);
            Vector3f vector = new Vector3f(aiVertex.x(), aiVertex.y(), aiVertex.z());
            
            Vector3f normal = new Vector3f();
            if (aiNormals != null) {
                AIVector3D aiNormal = aiNormals.get(i);
                normal.set(aiNormal.x(), aiNormal.y(), aiNormal.z());
            }

            Vector2f texCoords = new Vector2f(0, 0);
            if (aiTexCoords != null) {
                AIVector3D aiTexCoord = aiTexCoords.get(i);
                texCoords.set(aiTexCoord.x(), aiTexCoord.y());
            }
            
            vertices.add(new Vertex(vector, normal, texCoords));
        }

        for (int i = 0; i < mesh.mNumFaces(); i++) {
            AIFace face = mesh.mFaces().get(i);
            for (int j = 0; j < face.mNumIndices(); j++) {
                indices.add(face.mIndices().get(j));
            }
        }

        if (mesh.mMaterialIndex() >= 0) {
            AIMaterial material = AIMaterial.create(scene.mMaterials().get(mesh.mMaterialIndex()));
            List<Texture> diffuseMaps = loadMaterialTextures(material, aiTextureType_DIFFUSE, "texture_diffuse");
            textures.addAll(diffuseMaps);
            List<Texture> specularMaps = loadMaterialTextures(material, aiTextureType_SPECULAR, "texture_specular");
            textures.addAll(specularMaps);
        }

        return new Mesh(
            vertices.toArray(new Vertex[0]),
            indices.stream().mapToInt(i -> i).toArray(),
            textures.toArray(new Texture[0])
        );
    }

    private List<Texture> loadMaterialTextures(AIMaterial material, int type, String typeName) {
        List<Texture> textures = new ArrayList<>();
        AIString path = AIString.calloc();

        for (int i = 0; i < aiGetMaterialTextureCount(material, type); i++) {
            aiGetMaterialTexture(material, type, i, path, (IntBuffer) null, null, null, null, null, null);
            String textPath = path.dataString();
            boolean skip = false;
            
            for(Texture loadedTex : textures_loaded) {
                if(loadedTex.getType().equals(typeName)) { 
                     // Simple check, ideally check path too
                }
            }

            String fullPath;
            if (directory.isEmpty()) {
                fullPath = textPath;
            } else {
                fullPath = directory + "/" + textPath;
            }

            Texture texture = new Texture(fullPath, typeName);
            textures.add(texture);
            textures_loaded.add(texture);
        }
        path.free();
        return textures;
    }
}
