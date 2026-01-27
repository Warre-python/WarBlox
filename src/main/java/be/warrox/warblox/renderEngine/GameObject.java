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

    public void render(Shader shader, Camera camera, Vector3f lightPos) {
        Matrix4f model = this.transform.getModelMatrix();

        shader.uploadMat4f("view", camera.getViewMatrix());
        shader.uploadMat4f("model", model);
        shader.uploadMat4f("projection", Transform.getProjectionMatrix(Window.width, Window.height, camera));

        shader.uploadVec3f("lightPos", lightPos);

        shader.uploadFloat("ambientStrength", 0.2f);


        shader.uploadVec3f("objectColor", new Vector3f(color.x, color.y, color.z));
        shader.uploadVec3f("lightColor",  new Vector3f(1.0f, 1.0f, 1.0f));

        if (texture != null) {
            shader.uploadBool("useTexture", true);
        } else {
            shader.uploadBool("useTexture", false);
        }


        glBindVertexArray(vao);
        if (this.texture != null) this.texture.bind();

        // Use the specific element count for this object
        glDrawElements(GL_TRIANGLES, elementCount, GL_UNSIGNED_INT, 0);

        if (this.texture != null) this.texture.unbind();
        glBindVertexArray(0);
    }

    public void update() {

    }

    public Transform getTransform() {
        return this.transform;
    }
}
