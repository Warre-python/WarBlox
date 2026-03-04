package be.warrox.warblox.game;

import be.warrox.warblox.game.objects.*;
import be.warrox.warblox.game.objects.entities.Player;
import be.warrox.warblox.renderEngine.*;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;

import java.util.Map;

public class World {
    private RenderBatch rb;
    private Camera camera;
    private Player player;
    public LightSource lightSource;
    private Map<Position, Block> blocks;


    public World(RenderBatch rb) {
        this.rb = rb;
    }

    public void initWorld() {
        // 1. Camera setup
        this.camera = new Camera(new Vector3f(0, 6, 12));

        addBlock(new Block(new Position(0, 0, 0), BlockType.GRASS));


    }




    public void addBlock(GameObject object) {
        rb.addGameObject(object);
    }

    public void update(float deltaTime) {
        camera.update(deltaTime); // Update eerst de camera (input)

        // Update de player op basis van de nieuwe camera positie
        //if (this.player != null) {
        //    this.player.update(camera);
        //}

        // Update alle andere objecten
        for (GameObject go : rb.getGameObjects()) {
            go.update();
        }
    }


    public Camera getCamera() {
        return camera;
    }

}
