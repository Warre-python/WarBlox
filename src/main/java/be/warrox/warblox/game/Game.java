package be.warrox.warblox.game;


import be.warrox.warblox.renderEngine.RenderBatch;
import be.warrox.warblox.renderEngine.Shader;


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
        rb.render(shader, world.getCamera(), world.getLightPos());

    }

    public World getWorld() {
        return world;
    }
}
