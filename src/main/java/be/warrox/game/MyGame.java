package be.warrox.game;

import be.warrox.engine.core.Window;
import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.Renderer;
import be.warrox.engine.gfx.Vertex;
import be.warrox.engine.scene.Entity;
import be.warrox.engine.scene.Scene;
import be.warrox.game.world.World;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MyGame implements IGame {
    private World world; // Your Voxel World

    private Mesh rectangle;

    @Override
    public void init(Window window) {
        // Inside MyGame.init()
        Vertex[] vertices = new Vertex[] {
                new Vertex(new Vector3f(-0.5f,  0.5f, 0.0f), new Vector4f(1, 0, 0, 1)), // Top Left (Red)
                new Vertex(new Vector3f( 0.5f,  0.5f, 0.0f), new Vector4f(0, 1, 0, 1)), // Top Right (Green)
                new Vertex(new Vector3f( 0.5f, -0.5f, 0.0f), new Vector4f(0, 0, 1, 1)), // Bottom Right (Blue)
                new Vertex(new Vector3f(-0.5f, -0.5f, 0.0f), new Vector4f(1, 1, 1, 1))  // Bottom Left (White)
        };

        int[] indices = new int[] {
                0, 1, 2,
                2, 3, 0
        };

        rectangle = new Mesh(vertices, indices);
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
        //renderer.renderWorld(world, scene);

        // 2. Render the Entities (Assimp models) stored in the scene
        //renderer.renderEntities(scene);

        renderer.renderMesh(rectangle);

    }
}
