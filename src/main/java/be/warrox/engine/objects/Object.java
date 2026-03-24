package be.warrox.engine.objects;

import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.Shader;
import be.warrox.engine.scene.Transform;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Object {
    private Mesh mesh;
    private Transform transform;

    public Object(Mesh mesh) {
        this.mesh = mesh;
        init();
    }

    public void init() {

    }

    public void update() {

    }

    public void draw(Shader shader) {
        mesh.draw(shader);
    }
}
