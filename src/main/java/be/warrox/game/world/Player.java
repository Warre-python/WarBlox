package be.warrox.game.world;

import be.warrox.engine.scene.Camera;
import org.joml.Vector3f;

public class Player {

    private Vector3f pos;

    public Player(Vector3f pos) {
        this.pos = pos;

    }

    public void update(float dt, Camera camera) {
        this.pos = camera.getPos();
    }

    public Vector3f getPos() {
        return pos;
    }
}
