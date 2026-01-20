package be.warrox.warblox.game;

import be.warrox.warblox.game.objects.Cube;
import be.warrox.warblox.game.objects.Rectangle;
import be.warrox.warblox.renderEngine.RenderBatch;
import be.warrox.warblox.renderEngine.Transform;
import org.joml.Vector3f;

public class World {
    private RenderBatch rb;
    public World(RenderBatch rb) {
        this.rb = rb;

    }

    public void initBlocks() {
        rb.addGameObject(new Cube(new Transform(new Vector3f(0, 0, 0f), new Vector3f(1, 1, 1), new Vector3f(1.0f, 2.0f, 1.0f)), "assets/textures/block/oak_planks.png", this));
        rb.addGameObject(new Rectangle(new Transform(new Vector3f(-1.5f, -1.5f, 0f), new Vector3f(1, 1, 1), new Vector3f(2.0f, 1.0f, 1.0f)), "assets/textures/block/deepslate_diamond_ore.png", this));
    }

    public void update() {

    }
}

