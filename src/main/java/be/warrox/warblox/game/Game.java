package be.warrox.warblox.game;

public class Game {
    private World world;
    public void init() {
        world = new World();
    }

    public void update() {

    }

    public World getWorld() {
        return world;
    }
}
