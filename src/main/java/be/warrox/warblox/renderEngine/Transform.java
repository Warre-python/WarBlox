package be.warrox.warblox.renderEngine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;

    public Transform() {
        this.position = new Vector3f();
        this.rotation = new Vector3f();
        this.scale = new Vector3f(1.0f, 1.0f, 1.0f);
    }

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Matrix4f getModelMatrix() {
        return new Matrix4f()
                .translation(position)
                // Use rotateXYZ for cleaner 3D rotations (Euler angles)
                .rotateXYZ((float) Math.toRadians(rotation.x),
                        (float) Math.toRadians(rotation.y),
                        (float) Math.toRadians(rotation.z))
                .scale(scale);
    }

    public static Matrix4f getProjectionMatrix(float width, float height, Camera camera) {
        float fov = camera.getFov();
        float aspectRatio = width / height;
        float nearPlane = 0.1f;
        float farPlane = 1000.0f;

        return new Matrix4f().perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void rotate(Vector3f rotation) {
        this.rotation.add(rotation);
    }

    public void move(Vector3f position) {
        this.position.add(position);
    }

    public void changeScale(Vector3f scale) {
        this.scale.add(scale);
    }
}

