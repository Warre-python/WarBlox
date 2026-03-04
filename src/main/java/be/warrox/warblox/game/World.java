package be.warrox.warblox.game;

import be.warrox.warblox.game.objects.Cube;
import be.warrox.warblox.game.objects.LightSource;
import be.warrox.warblox.game.objects.MyModel;
import be.warrox.warblox.game.objects.entities.Player;
import be.warrox.warblox.renderEngine.*;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class World {
    private RenderBatch rb;
    private Camera camera;
    private Player player;
    public LightSource lightSource;


    public World(RenderBatch rb) {
        this.rb = rb;
    }

    public void initWorld() {
        // 1. Camera setup
        this.camera = new Camera(new Vector3f(0, 6, 12));

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                for (int z = 0; z < 10; z++) {
                    addBlock(new Cube(new Vector3f(x, y, z), new Color("blue")));
                }
            }
        }


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
