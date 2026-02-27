package be.warrox.warblox.renderEngine;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.assimp.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;

public class Model {
    private List<Mesh> meshes;
    private String directory;

    public Model(String path) {
        meshes = new ArrayList<>();
        loadModel(path);
    }

    public void draw(Shader shader) {
        for(Mesh mesh : meshes){
            mesh.draw(shader);
        }
    }

    private void loadModel(String path) {
        // Only load the OBJ file, ignore materials
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
        List<Texture> textures = new ArrayList<>(); // Empty list, no textures

        AIVector3D.Buffer aiVertices = mesh.mVertices();
        AIVector3D.Buffer aiNormals = mesh.mNormals();
        // We can ignore texture coords since we are not using textures
        
        for (int i = 0; i < mesh.mNumVertices(); i++) {
            AIVector3D aiVertex = aiVertices.get(i);
            Vector3f vector = new Vector3f(aiVertex.x(), aiVertex.y(), aiVertex.z());
            
            Vector3f normal = new Vector3f();
            if (aiNormals != null) {
                AIVector3D aiNormal = aiNormals.get(i);
                normal.set(aiNormal.x(), aiNormal.y(), aiNormal.z());
            }

            // No texture coords needed
            Vector2f texCoords = new Vector2f(0, 0);
            
            // Create vertex with black color (handled by Vertex constructor or explicitly here)
            Vertex vertex = new Vertex(vector, normal, texCoords);
            // Explicitly set color to black just to be sure, though constructor does it
            // vertex.setColor(new Vector3f(0.0f, 0.0f, 0.0f)); 
            
            vertices.add(vertex);
        }

        for (int i = 0; i < mesh.mNumFaces(); i++) {
            AIFace face = mesh.mFaces().get(i);
            for (int j = 0; j < face.mNumIndices(); j++) {
                indices.add(face.mIndices().get(j));
            }
        }

        // Skip material loading entirely

        return new Mesh(
            vertices.toArray(new Vertex[0]),
            indices.stream().mapToInt(i -> i).toArray(),
            textures.toArray(new Texture[0])
        );
    }
}
