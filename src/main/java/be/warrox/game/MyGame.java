package be.warrox.game;

import be.warrox.engine.core.Window;
import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.Renderer;
import be.warrox.engine.gfx.Vertex;
import be.warrox.engine.objects.Primitives;
import be.warrox.engine.scene.Entity;
import be.warrox.engine.scene.Scene;
import be.warrox.game.world.World;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MyGame implements IGame {
    private World world; // Your Voxel World

    @Override
    public void init(Window window, Scene scene) {
        scene.addObject(new Mesh(Primitives.verticesRectangle, Primitives.indicesRectangle));
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
        scene.renderObjects();

    }
}
