package be.warrox.warblox.renderEngine;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.joml.Math.cos;
import static org.joml.Math.sin;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Camera {
    private Vector3f position;
    private Vector3f target;
    private Vector3f up;

    private Matrix4f viewMatrix;

    public Camera(Vector3f pos) {
        this.position = pos;
        this.target = new Vector3f(0.0f, 0.0f, 0.0f);
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix = new Matrix4f();
    }

    public void update() {
        float radius = 10.0f;
        double time = glfwGetTime();

        // Calculate new orbital position
        position.x = (float) sin(time) * radius;
        position.z = (float) cos(time) * radius;
        position.y = 0.0f;

        // Identity is important to clear previous state before lookAt
        viewMatrix.identity().lookAt(position, target, up);
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }
}

