package be.warrox.warblox.game;

import be.warrox.warblox.game.objects.Cube;
import be.warrox.warblox.game.objects.LightSource;
import be.warrox.warblox.game.objects.MyModel;
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
    public LightSource lightSource;


    public World(RenderBatch rb) {
        this.rb = rb;
    }

    public void initWorld() {
        // 1. Camera setup
        this.camera = new Camera(new Vector3f(0, 6, 12));

        // 2. The Floor (A nice grassy plane)
        addBlock(new Cube(
                new Transform(new Vector3f(0, -0.5f, 0), new Vector3f(0), new Vector3f(40, 1f, 40)),
                "assets/textures/block/grass_block_top.png", rb
        ));

        // 3. A showcase of various blocks
        // Diamond Block
        addBlock(new Cube(
                new Transform(new Vector3f(3, 0.5f, 0), new Vector3f(0, 45, 0), new Vector3f(1)),
                "assets/textures/block/diamond_block.png", rb
        ));
        // Gold Block
        addBlock(new Cube(
                new Transform(new Vector3f(5, 0.5f, 0), new Vector3f(0), new Vector3f(1)),
                "assets/textures/block/gold_block.png", rb
        ));
        // Emerald Block
        addBlock(new Cube(
                new Transform(new Vector3f(7, 0.5f, 0), new Vector3f(0), new Vector3f(1)),
                "assets/textures/block/emerald_block.png", rb
        ));

        // 4. A small cobblestone wall
        for (int i = 0; i < 8; i++) {
            addBlock(new Cube(
                    new Transform(new Vector3f(-6 + i, 0.5f, -4), new Vector3f(0), new Vector3f(1)),
                    "assets/textures/block/cobblestone.png", rb
            ));
            if (i % 2 != 0) {
                addBlock(new Cube(
                        new Transform(new Vector3f(-6 + i, 1.5f, -4), new Vector3f(0), new Vector3f(1)),
                        "assets/textures/block/cobblestone.png", rb
                ));
            }
        }

        // 5. A simple tree
        // Trunk
        for (int i = 0; i < 4; i++) {
            addBlock(new Cube(
                    new Transform(new Vector3f(-8, 0.5f + i, 4), new Vector3f(0), new Vector3f(1)),
                    "assets/textures/block/oak_log.png", rb
            ));
        }
        // Leaves canopy
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                // Layer 1
                addBlock(new Cube(
                        new Transform(new Vector3f(-8 + x, 3.5f, 4 + z), new Vector3f(0), new Vector3f(1)),
                        "assets/textures/block/oak_leaves.png", rb
                ));
                // Layer 2
                if (Math.abs(x) + Math.abs(z) < 2) { // create a + shape on top
                    addBlock(new Cube(
                            new Transform(new Vector3f(-8 + x, 4.5f, 4 + z), new Vector3f(0), new Vector3f(1)),
                            "assets/textures/block/oak_leaves.png", rb
                    ));
                }
            }
        }

        // 6. Light sources (Atmospheric lighting)
        // We'll use a more natural and cohesive lighting setup
        Vector3f[] lightPositions = {
                new Vector3f(5, 5, 5),    // Right (Key)
                new Vector3f(-5, 5, 5),   // Left (Fill)
                new Vector3f(0, 10, -2),  // Top
                new Vector3f(0, 2, 4)     // Front (Mood)
        };

        Vector4f[] lightColors = {
                new Vector4f(1.0f, 0.9f, 0.7f, 1.0f), // Warm sun-like
                new Vector4f(0.7f, 0.8f, 1.0f, 1.0f), // Cool sky-like
                new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), // Bright White
                new Vector4f(1.0f, 0.6f, 0.2f, 1.0f)  // Warm torch-like
        };

        for (int i = 0; i < lightPositions.length; i++) {
            LightSource ls = new LightSource(
                    new Transform(lightPositions[i], new Vector3f(0), new Vector3f(0.2f)),
                    lightColors[i],
                    rb
            );
            rb.addGameObject(ls);
        }

        // 7. Player
        this.player = new Player(new Transform(new Vector3f(0, 2, 10), new Vector3f(0, 0, 0), new Vector3f(1)), new Vector4f(0, 1, 0, 1), rb);
        rb.addGameObject(player);

        //8. Model
        addBlock(new MyModel(new Transform(new Vector3f(0, 2, 10), new Vector3f(0, 0, 0), new Vector3f(1)), "assets/models/backpack/backpack.obj", rb));
    }




    public void addBlock(GameObject object) {
        rb.addGameObject(object);
    }

    public void update(float deltaTime) {
        camera.update(deltaTime); // Update eerst de camera (input)

        // Update de player op basis van de nieuwe camera positie
        if (this.player != null) {
            this.player.update(camera);
        }

        // Update alle andere objecten
        for (GameObject go : rb.getGameObjects()) {
            go.update();
        }
    }


    public Camera getCamera() {
        return camera;
    }

}
