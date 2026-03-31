package be.warrox.game;

import be.warrox.engine.core.Window;
import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.Renderer;
import be.warrox.engine.gui.Element;
import be.warrox.engine.scene.Camera;
import be.warrox.engine.scene.Scene;
import be.warrox.game.world.BlockType;
import be.warrox.game.world.Chunk;
import be.warrox.game.world.Player;
import be.warrox.game.world.World;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class MyGame implements IGame {
    private World world; // Your Voxel World
    private Player player;
    private Element element;


    @Override
    public void init(Window window, Scene scene) {
        scene.addCamera(new Camera(new Vector3f(0, 0, 3)));
        this.element = new Element(10, 10, 50, 50, "diamond_block");
        this.world = new World();
        this.player = new Player(new Vector3f(0, 0, 0));


        this.world.addBlock(0, 30, 3, BlockType.STONE);


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
        player.update(dt, scene.getCamera(), world);
        world.update(player);

        System.out.println(player.getPos().x + " " + player.getPos().y + " " + player.getPos().z);

    }

    @Override
    public void render(Renderer renderer, Scene scene) {

        for (Chunk chunk : world.getChunks().values()) {
            for (Mesh mesh : chunk.getSubMeshes().values()) {
                mesh.draw(renderer.getShader(), scene.getCamera());
            }
        }

        element.draw(renderer);
    }
}
