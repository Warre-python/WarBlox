package be.warrox.warblox.game;

import be.warrox.warblox.renderEngine.Camera;
import be.warrox.warblox.renderEngine.RenderBatch;
import be.warrox.warblox.renderEngine.Shader;
import org.joml.Vector3f;

public class Game {
    private World world;
    private RenderBatch rb;
    private Shader shader;

    public void init() {
        this.rb = new RenderBatch();
        this.shader = this.rb.start();
        this.world = new World(rb);

        this.world.initWorld();

    }

    public void update(float deltaTime) {
        world.update(deltaTime);
        rb.render(shader, world.getCamera());

    }

    public World getWorld() {
        return world;
    }
}
