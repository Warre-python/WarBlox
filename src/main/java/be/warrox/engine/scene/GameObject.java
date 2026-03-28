package be.warrox.engine.scene;

import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.Shader;

public class GameObject {
    private Mesh mesh;

    public GameObject(Mesh mesh) {
        this.mesh = mesh;
        init();
    }

    public void init() {

    }

    public void update() {

    }

    public void draw(Shader shader, Camera camera) {
        mesh.draw(shader, camera);
    }
}
