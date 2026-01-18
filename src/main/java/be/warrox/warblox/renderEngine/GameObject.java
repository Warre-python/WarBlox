package be.warrox.warblox.renderEngine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public abstract class GameObject {
    private Vector4f color;
    private Texture texture;
    private Transform transform;

    protected int elementCount;
    private float[] vertices;
    private int[] indices;
    private int vao;

    public GameObject(Transform transform, Vector4f color, RenderBatch rb, float[] vertices, int[] indices) {
        this.transform = transform;
        this.color = color;
        this.elementCount = indices.length;
        this.vertices = vertices;
        this.indices = indices;
        init(rb);
    }

    public GameObject(Transform transform, String path, RenderBatch rb, float[] vertices, int[] indices) {
        this.transform = transform;
        this.texture = new Texture(path);
        this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.elementCount = indices.length;
        this.vertices = vertices;
        this.indices = indices;
        init(rb);
    }

    public void init(RenderBatch rb) {
        vao = rb.setupMesh(vertices, indices);
    }

    public void render(Shader shader) {
        //shader.uploadMat4f("view", new Matrix4f());
        shader.uploadMat4f("model", this.transform.getModelMatrix());
        //shader.uploadMat4f("projection", Transform.getProjectionMatrix(Window.width, Window.height));

        glBindVertexArray(vao);
        if (this.texture != null) this.texture.bind();

        // Use the specific element count for this object
        glDrawElements(GL_TRIANGLES, elementCount, GL_UNSIGNED_INT, 0);

        if (this.texture != null) this.texture.unbind();
        glBindVertexArray(0);
    }

    public void update() {
        this.transform.rotate(new Vector3f(0.5f, 0.5f, 0.5f));
    }
}
