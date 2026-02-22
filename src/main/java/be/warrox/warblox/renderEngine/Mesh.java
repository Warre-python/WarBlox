package be.warrox.warblox.renderEngine;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {
    public Vertex[] vertices;
    public int[] indices;
    public Texture[] textures;

    private int vbo, vao, ebo;

    private final int POS_SIZE = 3;
    //private final int COLOR_SIZE = 3;
    private final int TEXCOORDS_SIZE = 2;
    private final int NORMAL_SIZE = 3;

    private final int POS_OFFSET = 0;
    //private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEXCOORDS_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int NORMAL_OFFSET = TEXCOORDS_OFFSET + TEXCOORDS_SIZE * Float.BYTES;

    private final int VERTEX_SIZE = 8;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    public Mesh(Vertex[] vertices, int[] indices, Texture[] textures) {
        this.vertices = vertices;
        this.indices = indices;
        this.textures = textures;

        setupMesh();

    }

    public void draw(Shader shader) {

    }

    private void setupMesh() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, TEXCOORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXCOORDS_OFFSET);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(2, NORMAL_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, NORMAL_OFFSET);
        glEnableVertexAttribArray(2);
    }
}
