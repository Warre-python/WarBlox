package be.warrox.warblox.renderEngine;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {
    private int vbo, vao, ebo;
    private List<GameObject> gameObjects = new ArrayList<>();
    private Texture texture = new Texture("assets/textures/block/deepslate_diamond_ore.png");

    private final int POS_SIZE = 3;
    //private final int COLOR_SIZE = 3;
    private final int TEXCOORDS_SIZE = 2;

    private final int POS_OFFSET = 0;
    //private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEXCOORDS_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 5;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    public Shader start() {
        float[] vertices = Primitives.cubeVertices;
        int[] indices = Primitives.cubeIndices;
        // Generate and bind a Vertex Array Object
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Allocate space for vertices
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);

        ebo = glGenBuffers();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);


        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, TEXCOORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXCOORDS_OFFSET);
        glEnableVertexAttribArray(1);


        Shader shader = new Shader("assets/shaders/default.glsl");
        shader.compile();

        return shader;

    }

    public void render(Shader shader, Camera camera) {
        shader.use();



        for (GameObject go : gameObjects) {
            go.render(shader, camera);
        }


        shader.detach();

    }

    public void addGameObject(GameObject go) {
        this.gameObjects.add(go);
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public int setupMesh(float[] vertices, int[] indices) {
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, TEXCOORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXCOORDS_OFFSET);
        glEnableVertexAttribArray(1);
        return vao;
    }

}
