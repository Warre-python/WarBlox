package be.warrox.warblox.renderEngine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix;
    public Vector3f position;

    // Camera options
    private float movementSpeed = 5.0f;
    private float mouseSensitivity = 0.1f;

    // Euler Angles
    public float yaw = 0.0f;
    public float pitch = 0.0f;

    // Camera vectors
    private Vector3f front;
    private Vector3f up;
    private Vector3f right;
    private Vector3f worldUp;

    private boolean firstMouse = true;


    public Camera(Vector3f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.worldUp = new Vector3f(0, 1, 0);
        this.front = new Vector3f(0, 0, -1);
        updateCameraVectors();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.perspective((float)Math.toRadians(45.0f), (float) Window.get().getWidth() / Window.get().getHeight(), 0.1f, 100.0f);
    }

    public Matrix4f getViewMatrix() {
        this.viewMatrix.identity().lookAt(position, new Vector3f(position).add(front), up);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public void processInput(float dt) {
        processKeyboard(dt);
        processMouse();
    }

    private void processKeyboard(float dt) {
        float velocity = movementSpeed * dt;
        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            position.add(new Vector3f(front).mul(velocity));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            position.sub(new Vector3f(front).mul(velocity));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            position.sub(new Vector3f(right).mul(velocity));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            position.add(new Vector3f(right).mul(velocity));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) {
            position.add(new Vector3f(worldUp).mul(velocity));
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            position.sub(new Vector3f(worldUp).mul(velocity));
        }
    }

    private void processMouse() {
        if (firstMouse) {
            MouseListener.getX(); // consume lastX
            MouseListener.getY(); // consume lastY
            firstMouse = false;
        }

        float xOffset = MouseListener.getDx() * mouseSensitivity;
        float yOffset = MouseListener.getDy() * mouseSensitivity;

        yaw -= xOffset;
        pitch += yOffset;

        // Clamp pitch
        if (pitch > 89.0f) {
            pitch = 89.0f;
        }
        if (pitch < -89.0f) {
            pitch = -89.0f;
        }

        updateCameraVectors();
    }


    public Vector3f getFront() {
        return this.front;
    }

    private void updateCameraVectors() {
        // Calculate the new Front vector
        Vector3f newFront = new Vector3f();
        newFront.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        newFront.y = (float) Math.sin(Math.toRadians(pitch));
        newFront.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        this.front = newFront.normalize();

        // Also re-calculate the Right and Up vector
        this.right = new Vector3f(this.front).cross(this.worldUp).normalize();
        this.up = new Vector3f(this.right).cross(this.front).normalize();
    }
}
