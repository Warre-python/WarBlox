package be.warrox.warblox.renderEngine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.*;

public class GameObject {
    private Vector4f color;
    private Texture texture;
    private Transform transform;

    public GameObject(Transform transform, Vector4f color) {
        this.transform = transform;
        this.color = color;
    }

    public GameObject(Transform transform, String path) {
        this.transform = transform;
        this.texture = new Texture(path);
        this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void render(Shader shader) {

        shader.uploadMat4f("view", new Matrix4f());
        shader.uploadMat4f("model", this.transform.getModelMatrix());
        shader.uploadMat4f("projection", Transform.getProjectionMatrix(Window.width, Window.height));

        this.texture.bind();
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
        this.texture.unbind();
    }

    public void update() {
        this.transform.rotate(new Vector3f(0.25f, 0.5f, 0.0f));
    }
}
