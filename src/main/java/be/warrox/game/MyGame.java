package be.warrox.game;

import be.warrox.engine.core.Window;
import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.Renderer;
import be.warrox.engine.scene.Camera;
import be.warrox.engine.scene.Scene;
import be.warrox.game.world.Chunk;
import be.warrox.game.world.Player;
import be.warrox.game.world.World;
import org.joml.Vector3f;

public class MyGame implements IGame {
    private World world; // Your Voxel World
    private Player player;
    @Override
    public void init(Window window, Scene scene) {
        scene.addCamera(new Camera(new Vector3f(0, 0, 3)));

        this.world = new World();
        this.player = new Player(new Vector3f(0, 0, 0));







    }

    @Override
    public void input(Window window, Scene scene, float delta) {
        if (scene.getCamera() != null) {
            scene.getCamera().processInput(delta);
        }
    }

    @Override
    public void update(float dt, Scene scene) {
        //scene.update();
        player.update(dt, scene.getCamera());
        world.update(player);

    }

    @Override
    public void render(Renderer renderer, Scene scene) {
        //scene.getObjects().forEach(object -> object.draw(renderer.getShader(), scene.getCamere()));


        for (Chunk chunk : world.getChunks().values()) {
            for (Mesh mesh : chunk.getSubMeshes().values()) {
                mesh.draw(renderer.getShader(), scene.getCamera());
            }
        }
    }
}
