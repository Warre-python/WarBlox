package be.warrox.engine.gfx;

import be.warrox.engine.scene.Transform;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private int vaoId;
    private int vboId;
    private int eboId; // Index Buffer
    private int vertexCount;

    private Texture texture;
    private Vector4f meshColor;

    // Constructor for Color-based Mesh
    public Mesh(Vertex[] vertices, int[] indices, Vector4f color) {
        this(vertices, indices, (Texture) null); // Call the main setup logic
        this.meshColor = color;
    }

    // Constructor for Texture-based Mesh
    public Mesh(Vertex[] vertices, int[] indices, Texture texture) {
        this.vertexCount = indices.length;
        this.texture = texture;
        this.meshColor = new Vector4f(1, 1, 1, 1); // Default to white (no tint)

        setupMesh(vertices, indices);
    }

    private void setupMesh(Vertex[] vertices, int[] indices) {
        this.vertexCount = indices.length;
        FloatBuffer vertexBuffer = null;
        IntBuffer indexBuffer = null;

        if (texture != null) {
            this.texture = texture;
        }

        try {
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // 1. Pack Vertex objects into a flat FloatBuffer
            vertexBuffer = MemoryUtil.memAllocFloat(vertices.length * Vertex.VERTEX_SIZE_FLOATS);
            for (Vertex v : vertices) {
                vertexBuffer.put(v.position.x).put(v.position.y).put(v.position.z);
                vertexBuffer.put(v.normal.x).put(v.normal.y).put(v.normal.z);
                vertexBuffer.put(v.texCoords.x).put(v.texCoords.y);
                vertexBuffer.put(v.color.x).put(v.color.y).put(v.color.z).put(v.color.w);
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
            int stride = Vertex.VERTEX_SIZE_BYTES;

            // Position
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, Vertex.POS_SIZE, GL_FLOAT, false, stride, Vertex.POS_OFFSET);

            // Normals
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, Vertex.NORMAL_SIZE, GL_FLOAT, false, stride, Vertex.NORMAL_OFFSET);

            // Texture Coords
            glEnableVertexAttribArray(2);
            glVertexAttribPointer(2, Vertex.TEXCOORDS_SIZE, GL_FLOAT, false, stride, Vertex.TEXCOORDS_OFFSET);

            // Color
            glEnableVertexAttribArray(3);
            glVertexAttribPointer(3, Vertex.COLOR_SIZE, GL_FLOAT, false, stride, Vertex.COLOR_OFFSET);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);

        } finally {
            if (vertexBuffer != null) MemoryUtil.memFree(vertexBuffer);
            if (indexBuffer != null) MemoryUtil.memFree(indexBuffer);
        }
    }




    public void draw(Shader shader) {

        glBindVertexArray(vaoId);
        if (this.texture != null) {
            this.texture.bind();
            shader.uploadBool("useTexture", true);

        }

        Matrix4f model = new Matrix4f();
        model.rotate(Math.toRadians(-55.0f), new Vector3f(1.0f, 0.0f, 0.0f));

        Matrix4f view = new Matrix4f();
        view.translate(new Vector3f(0.0f, 0.0f, -3.0f));

        Matrix4f projection = new Matrix4f();
        projection.perspective(Math.toRadians(45.0f), 800.0f / 600.0f, 0.1f, 100.0f);

        shader.uploadMat4f("model", model);
        shader.uploadMat4f("view", view);
        shader.uploadMat4f("projection", projection);


        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        if (this.texture != null) {
            this.texture.unbind();
        }
        glBindVertexArray(0);


    }


}