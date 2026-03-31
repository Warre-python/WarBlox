package be.warrox.engine.gui;

import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.Shader;
import be.warrox.engine.gfx.Texture;
import be.warrox.engine.core.Window;
import be.warrox.engine.gfx.Vertex;
import be.warrox.engine.scene.Camera;
import be.warrox.engine.scene.Transform;
import be.warrox.engine.util.AssetManager;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class GuiMesh {

    protected int vaoId;
    protected int vboId;
    protected int eboId; // Index Buffer
    protected int vertexCount;

    protected Vector4f meshColor;
    protected String path;


    public GuiMesh(GuiVertex[] vertices, int[] indices, String path) {
        this.vertexCount = indices.length;
        this.meshColor =  new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);;
        this.path = path;

        setupMesh(vertices, indices);

    }

    public GuiMesh(GuiVertex[] vertices, int[] indices, Vector4f color) {
        this.vertexCount = indices.length;
        this.meshColor = color;
        this.path = null;

        setupMesh(vertices, indices);

    }

    protected void setupMesh(GuiVertex[] vertices, int[] indices) {
        this.vertexCount = indices.length;
        FloatBuffer vertexBuffer = null;
        IntBuffer indexBuffer = null;




        try {
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // 1. Pack Vertex objects into a flat FloatBuffer
            vertexBuffer = MemoryUtil.memAllocFloat(vertices.length * GuiVertex.VERTEX_SIZE_FLOATS);
            for (GuiVertex v : vertices) {
                vertexBuffer.put(v.position.x).put(v.position.y).put(v.position.z);
                vertexBuffer.put(v.texCoords.x).put(v.texCoords.y);
            }

            vertexBuffer.flip();

            // 2. VBO (Data)
            vboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

            // 3. EBO (Indices)
            indexBuffer = MemoryUtil.memAllocInt(indices.length);
            indexBuffer.put(indices).flip();
            eboId = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

            // 4. Link Attributes (Using your Vertex constants!)
            int stride = GuiVertex.VERTEX_SIZE_BYTES;

            // Position
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, GuiVertex.POS_SIZE, GL_FLOAT, false, stride, GuiVertex.POS_OFFSET);


            // Texture Coords
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, GuiVertex.TEXCOORDS_SIZE, GL_FLOAT, false, stride, GuiVertex.TEXCOORDS_OFFSET);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);

        } finally {
            if (vertexBuffer != null) MemoryUtil.memFree(vertexBuffer);
            if (indexBuffer != null) MemoryUtil.memFree(indexBuffer);
        }
    }


    public void draw(Shader shader, int x, int y, int width, int height) {
        shader.use();
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_CULL_FACE);

        glBindVertexArray(vaoId);

        Matrix4f projection = new Matrix4f().ortho(0.0f, (float)Window.width, (float)Window.height, 0.0f, -1.0f, 1.0f);
        Matrix4f model = new Matrix4f().translate(x, y, 0.0f).scale(width, height, 1.0f);

        if (path != null) {
            Texture texture = AssetManager.getTexture(path);
            if (texture != null) {
                texture.bind();
                shader.uploadBool("useTexture", true);
            }
        } else {
            // Als er geen texture is, vertel de shader dat hij de Vertex Kleur moet gebruiken
            shader.uploadBool("useTexture", false);
        }

        shader.uploadVec4f("uColor", meshColor);
        shader.uploadMat4f("projection", projection);
        shader.uploadMat4f("model", model);
        shader.uploadInt("ourTexture", 0);

        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        if (path != null) {
            Texture texture = AssetManager.getTexture(path);
            if (texture != null) texture.unbind();
        }
        shader.detach();

        glBindVertexArray(0);

    }

}
