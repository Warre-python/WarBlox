package be.warrox.engine.objects;

import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.Shader;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Object {
    private Mesh mesh;
    private Matrix4f transform;

    public Object(Mesh mesh) {
        this.mesh = mesh;
        init();
    }

    public void init() {
        transform = new Matrix4f();
        transform.rotate(Math.toRadians(90.0f), new Vector3f(0.0f, 0.0f, 1.0f));
        transform.scale(new Vector3f(0.5f, 0.5f, 0.5f));
    }

    public void update() {
        transform.translate(new Vector3f(0.0f, 0.0f, 0.0f));
        transform.rotate(0.01f, new Vector3f(0.0f, 0.0f, 1.0f));
    }

    public void draw(Shader shader) {
        mesh.draw(shader, transform);
    }
}
