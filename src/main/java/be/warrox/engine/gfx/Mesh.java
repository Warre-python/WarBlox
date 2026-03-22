package be.warrox.engine.gfx;

import org.lwjgl.system.MemoryUtil;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private int vaoId;
    private int vboId;
    private int eboId; // Index Buffer
    private int vertexCount;

    public Mesh(Vertex[] vertices, int[] indices) {
        this.vertexCount = indices.length;
        FloatBuffer vertexBuffer = null;
        IntBuffer indexBuffer = null;

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

    public void draw() {
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }
}