package be.warrox.warblox.renderEngine;

import org.joml.Matrix4f;
import org.joml.Vector3f;


import static org.joml.Math.cos;
import static org.joml.Math.sin;
import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private Vector3f position;
    private Vector3f up;
    private Vector3f front;

    private Vector3f direction = new Vector3f();

    private final Vector3f temp = new Vector3f();
    private final Vector3f right = new Vector3f();
    private final Matrix4f viewMatrix = new Matrix4f();

    public Camera(Vector3f pos) {
        this.position = pos;
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.front = new Vector3f(0.0f, 0.0f, -1.0f);
    }

    public void update(float dt) {
        processInput(dt);


        // Calculate target: Position + Front (without modifying either)
        Vector3f target = position.add(front, new Vector3f());
        viewMatrix.identity().lookAt(position, target, up);
    }

    public void processInput(float dt) {
        float cameraSpeed = 2.5f * dt;

        // Forward: position += front * speed
        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            position.add(front.mul(cameraSpeed, temp));
        }
        // Backward: position -= front * speed
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            position.sub(front.mul(cameraSpeed, temp));
        }
        // Strafe Left: position -= (front x up) * speed
        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            front.cross(up, right).normalize();
            position.sub(right.mul(cameraSpeed, temp));
        }
        // Strafe Right: position += (front x up) * speed
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            front.cross(up, right).normalize();
            position.add(right.mul(cameraSpeed, temp));
        }
    }


    public Matrix4f getViewMatrix() { return viewMatrix; }
}

