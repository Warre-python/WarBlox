package be.warrox.warblox.game;

import be.warrox.warblox.renderEngine.Camera;
import be.warrox.warblox.renderEngine.RenderBatch;
import be.warrox.warblox.renderEngine.Shader;
import org.joml.Vector3f;

public class Game {
    private World world;
    private Camera camera;
    private RenderBatch rb;
    private Shader shader;

    public void init() {
        this.rb = new RenderBatch();
        this.shader = this.rb.start();
        this.world = new World(rb);
        this.camera = new Camera(new Vector3f());
    }

    public void update(float deltaTime) {
        camera.update(deltaTime);
        rb.render(shader, camera);

    }

    public World getWorld() {
        return world;
    }
}
