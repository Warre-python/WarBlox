package be.warrox.warblox.renderEngine;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.lwjgl.BufferUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import java.nio.FloatBuffer;

public class Mesh {
    public Vertex[] vertices;
    public int[] indices;
    public Texture[] textures;

    private int vbo, vao, ebo;

    private final int POS_SIZE = 3;
    private final int COLOR_SIZE = 3;
    private final int TEXCOORDS_SIZE = 2;
    private final int NORMAL_SIZE = 3;

    private final int POS_OFFSET = 0;
    private final int NORMAL_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEXCOORDS_OFFSET = NORMAL_OFFSET + NORMAL_SIZE * Float.BYTES;
    private final int COLOR_OFFSET = TEXCOORDS_OFFSET + TEXCOORDS_SIZE * Float.BYTES;

    private final int VERTEX_SIZE = POS_SIZE + NORMAL_SIZE + TEXCOORDS_SIZE + COLOR_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    public Mesh(Vertex[] vertices, int[] indices, Texture[] textures) {
        this.vertices = vertices;
        this.indices = indices;
        this.textures = textures;

        setupMesh();

    }

    public void draw(Shader shader) {
        int diffuseNr = 1;
        int specularNr = 1;

        for(int i = 0; i < textures.length; i++) {
            glActiveTexture(GL_TEXTURE0 + i);

            String number = "";
            String name = textures[i].getType();

            if(name.equals("texture_diffuse")) {
                number = String.valueOf(diffuseNr++);

            } else if(name.equals("texture_specular")) {
                number = String.valueOf(specularNr++);
            }

            shader.uploadInt(("material." + name + number), i);
            glBindTexture(GL_TEXTURE_2D, textures[i].getTexID());
        }
        glActiveTexture(GL_TEXTURE0);

        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    private void setupMesh() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        // Create a FloatBuffer to send data to OpenGL
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length * VERTEX_SIZE);
        for (Vertex vertex : vertices) {
            // Position
            vertexBuffer.put(vertex.getPosition().x);
            vertexBuffer.put(vertex.getPosition().y);
            vertexBuffer.put(vertex.getPosition().z);

            // Normal
            Vector3f normal = vertex.getNormal();
            vertexBuffer.put(normal.x).put(normal.y).put(normal.z);

            // TexCoords
            Vector2f texCoords = vertex.getTexCoords();
            if (texCoords != null) {
                vertexBuffer.put(texCoords.x).put(texCoords.y);
            } else {
                vertexBuffer.put(0.0f).put(0.0f);
            }

            // Color
            Vector3f color = vertex.getColor();
            if (color != null) {
                vertexBuffer.put(color.x).put(color.y).put(color.z);
            } else {
                vertexBuffer.put(1.0f).put(1.0f).put(1.0f);
            }
        }
        vertexBuffer.flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, NORMAL_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, NORMAL_OFFSET);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, TEXCOORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXCOORDS_OFFSET);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(3, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(3);

        glBindVertexArray(0);
    }
}
