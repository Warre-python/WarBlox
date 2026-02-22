package be.warrox.warblox.renderEngine;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vertex {
    private Vector3f position;
    private Vector3f normal;
    private Vector2f texCoords;
    private Vector3f color;

    public Vertex(Vector3f position, Vector3f normal, Vector3f color) {
        this.position = position;
        this.normal = normal;
        this.color = color;

    }

    public Vertex(Vector3f position, Vector3f normal, Vector2f texCoords) {
        this.position = position;
        this.normal = normal;
        this.texCoords = texCoords;

    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getNormal() {
        return normal;
    }

    public Vector2f getTexCoords() {
        return texCoords;
    }
}
