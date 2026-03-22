package be.warrox.game;

import be.warrox.engine.core.Window;
import be.warrox.engine.gfx.Renderer;
import be.warrox.engine.scene.Entity;
import be.warrox.engine.scene.Scene;
import be.warrox.game.world.World;
import org.joml.Vector3f;

public class MyGame implements IGame {
    private World world; // Your Voxel World

    @Override
    public void init(Window window) {
        // We don't necessarily need to 'new' the scene here
        // if the Engine already created one and passed it,
        // but usually, the Game defines the world data.
        world = new World();
        world.generate();
    }

    @Override
    public void input(Window window, Scene scene) {
        // Example: if (window.isKeyPressed(GLFW_KEY_W)) scene.getCamera().moveForward();
    }

    @Override
    public void update(float interval, Scene scene) {
        // Update entity positions or block animations
    }

    @Override
    public void render(Renderer renderer, Scene scene) {
        // 1. Render the Voxel World first
        renderer.renderWorld(world, scene);

        // 2. Render the Entities (Assimp models) stored in the scene
        renderer.renderEntities(scene);
    }
}
