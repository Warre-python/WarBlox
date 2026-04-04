package be.warrox.game.world;

import be.warrox.engine.core.MouseListener;
import be.warrox.engine.scene.Camera;
import be.warrox.engine.util.Raycast;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
    private Vector3f pos;
    private float cooldown = 0.0f;
    private final float REACH = 5.0f;
    private final float CLICK_DELAY = 0.2f; // 200ms between actions

    public Player(Vector3f pos) {
        this.pos = pos;
    }

    public void update(float dt, Camera camera, World world) {
        this.pos = camera.position;

        if (cooldown > 0) {
            cooldown -= dt;
        }

        // Only process if cooldown is finished
        if (cooldown <= 0) {
            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                handleRaycast(camera, world, true);
                cooldown = CLICK_DELAY;
            } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {
                handleRaycast(camera, world, false);
                cooldown = CLICK_DELAY;
            }
        }
    }

    private void handleRaycast(Camera camera, World world, boolean isLeftClick) {
        // Use the new helper to ensure alignment with the screen center (crosshair)
        Raycast ray = Raycast.fromScreenCenter(camera, REACH);
        Raycast.RaycastResult result = ray.cast(world);

        if (result != null) {
            if (isLeftClick) {
                // Remove the block we hit
                world.addBlock((int)result.blockPos.x, (int)result.blockPos.y, (int)result.blockPos.z, BlockType.AIR);
            } else {
                // Place a block by adding the face normal to the hit position
                int placeX = (int)(result.blockPos.x + result.faceNormal.x);
                int placeY = (int)(result.blockPos.y + result.faceNormal.y);
                int placeZ = (int)(result.blockPos.z + result.faceNormal.z);

                // Basic collision check: Don't place a block where the player is standing
                if (!isPositionInsidePlayer(placeX, placeY, placeZ)) {
                    world.addBlock(placeX, placeY, placeZ, BlockType.GRASS);
                }
            }
        }
    }

    private boolean isPositionInsidePlayer(int x, int y, int z) {
        // Simplistic check: is the targeted block the same as the player's head or feet?
        int px = (int) Math.floor(pos.x);
        int py = (int) Math.floor(pos.y);
        int pz = (int) Math.floor(pos.z);

        // Check feet and one block up (head)
        return (x == px && z == pz) && (y == py || y == py - 1);
    }

    public Vector3f getPos() {
        return pos;
    }
}
