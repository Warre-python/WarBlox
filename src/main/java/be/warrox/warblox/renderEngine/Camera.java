package be.warrox.warblox.renderEngine;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;


import static org.joml.Math.cos;
import static org.joml.Math.sin;
import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    public Vector3f position;
    public Vector3f up;
    public Vector3f front;

    private Vector3f direction = new Vector3f();
    private float yaw;
    private float pitch;
    private float roll;

    private float sensitivity = 0.1f;
    private float fov = 45.0f;

    private final Vector3f temp = new Vector3f();
    private final Vector3f right = new Vector3f();
    private final Matrix4f viewMatrix = new Matrix4f();

    public Camera(Vector3f pos) {
        this.position = pos;
        this.direction = new Vector3f();
        this.yaw = -90.0f;
        this.pitch = 0.0f;

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

        // Movement (Correct)
        if (KeyListener.isKeyPressed(GLFW_KEY_W)) position.add(front.mul(cameraSpeed, temp));
        if (KeyListener.isKeyPressed(GLFW_KEY_S)) position.sub(front.mul(cameraSpeed, temp));

        // Strafe (Optimized: use the pre-calculated 'right' vector)
        front.cross(up, right).normalize();
        if (KeyListener.isKeyPressed(GLFW_KEY_A)) position.sub(right.mul(cameraSpeed, temp));
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) position.add(right.mul(cameraSpeed, temp));

        if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)) position.add(up.mul(cameraSpeed, temp));
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) position.sub(up.mul(cameraSpeed, temp));

        // Turning Logic (Fixed)
        // getDx() -> Yaw (Horizontal), getDy() -> Pitch (Vertical)
        yaw   += MouseListener.getDx() * sensitivity;
        pitch -= MouseListener.getDy() * sensitivity; // Subtraction prevents inverted look

        if(pitch > 89.0f) pitch = 89.0f;
        if(pitch < -89.0f) pitch = -89.0f;

        // Convert Euler angles to Direction Vector
        direction.x = cos(Math.toRadians(yaw)) * cos(Math.toRadians(pitch));
        direction.y = sin(Math.toRadians(pitch));
        direction.z = sin(Math.toRadians(yaw)) * cos(Math.toRadians(pitch));

        // Fix: Normalize 'direction' and store the result into 'front'
        direction.normalize(front);

        fov -= (float) MouseListener.getScrollY() * sensitivity;
        if (fov < 1.0f)
            fov = 1.0f;
        if (fov > 45.0f)
            fov = 45.0f;

    }

    public float getFov() {
        return fov;
    }


    public Matrix4f getViewMatrix() { return viewMatrix; }
}

