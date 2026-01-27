package be.warrox.warblox.game;

import be.warrox.warblox.game.objects.Cube;
import be.warrox.warblox.game.objects.entities.Player;
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
    private Vector3f lightPos = new Vector3f(0, 20, 20);


    public World(RenderBatch rb) {
        this.rb = rb;
    }

    public void initWorld() {
        this.camera = new Camera(new Vector3f(0, 10, 5));

        this.player = new Player(new Transform(new Vector3f(0, 10, 4), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)), new Vector4f(0, 1, 0, 1), rb);
        rb.addGameObject(player);


        addBlock(new Cube(new Transform(new Vector3f(0, 10, 4), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)), new Vector4f(0, 1, 0, 1), rb));

        addBlock(new Cube(new Transform(new Vector3f(0, 10, 1), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)), new Vector4f(0, 0, 1, 1), rb));

        addBlock(new Cube(new Transform(lightPos, new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)), new Vector4f(1, 1, 1, 1), rb));
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

    public Vector3f getLightPos() {
        return lightPos;
    }
}

