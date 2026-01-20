package be.warrox.warblox.game;

import be.warrox.warblox.game.objects.Cube;
import be.warrox.warblox.game.objects.Player;
import be.warrox.warblox.game.objects.Rectangle;
import be.warrox.warblox.renderEngine.Camera;
import be.warrox.warblox.renderEngine.GameObject;
import be.warrox.warblox.renderEngine.RenderBatch;
import be.warrox.warblox.renderEngine.Transform;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class World {
    private RenderBatch rb;
    private Camera camera;
    private Player player;
    public World(RenderBatch rb) {
        this.rb = rb;
    }

    public void initWorld() {
        this.camera = new Camera(new Vector3f(0, 10, 5));

        this.player = new Player(new Transform(new Vector3f(0, 10, 4), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)), new Vector4f(0, 1, 0, 1), rb);
        rb.addGameObject(player);


        addBlock(new Cube(new Transform(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(64, 64, 64)), "assets/textures/block/redstone_block.png", rb));


        //addBlock(new Cube(new Transform(new Vector3f(0, 10, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)), new Vector4f(1, 1, 1, 1), rb));

        for (int x = 0; x<10; x++) {
            for (int z = 0; z<10; z++) {
                addBlock(new Cube(new Vector3f(x, 0,z), "assets/textures/block/oak_log.png", rb));
            }
        }

    }

    public void addBlock(GameObject object) {
        rb.addGameObject(object);
    }

    public void update(float deltaTime) {
        camera.update(deltaTime);
        player.update(camera);
        for (GameObject go : rb.getGameObjects()) {
            go.update();
        }
    }

    public Camera getCamera() {
        return camera;
    }
}

