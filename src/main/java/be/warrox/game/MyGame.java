package be.warrox.game;

import be.warrox.game.world.World;

public class MyGame implements IGame {
    private Scene scene; // The container provided by the Engine
    private World world; // The voxel data (Game-specific)

    @Override
    public void init(Window window) {
        scene = new Scene(); // Engine-level class
        world = new World(); // Game-level class

        // Game logic adds a light to the engine's scene
        scene.addLight(new DirectionalLight(new Vector3f(1, 1, 1), 1.0f));

        // Game logic adds the player to the engine's scene
        Entity player = new Entity(modelLoader.load("player.obj"));
        scene.addEntity(player);
    }

    @Override
    public void render(Window window) {
        // You pass the Scene (the container) to the Renderer
        renderer.render(window, scene, world);
    }
}
