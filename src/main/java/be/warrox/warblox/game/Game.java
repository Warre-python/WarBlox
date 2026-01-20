package be.warrox.warblox.game;

import be.warrox.warblox.renderEngine.RenderBatch;

public class Game {
    private World world;
    private RenderBatch rb;
    public void init() {
        world = new World();
    }

    public void update() {

    }

    public World getWorld() {
        return world;
    }
}
