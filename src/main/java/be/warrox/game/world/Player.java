package be.warrox.game.world;

import be.warrox.engine.core.KeyListener;
import be.warrox.engine.scene.Camera;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Player {

    private Vector3f pos;

    public Player(Vector3f pos) {
        this.pos = pos;

    }

    public void update(float dt, Camera camera, World world) {
        this.pos = camera.getPos();
        if (KeyListener.isKeyPressed(GLFW_KEY_E)) {
            world.addBlock((int) this.pos.x, (int)this.pos.y, (int)this.pos.z, BlockType.AIR);
        }
    }

    public Vector3f getPos() {
        return pos;
    }
}
