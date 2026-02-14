package be.warrox.warblox.game;

import be.warrox.warblox.game.objects.Cube;
import be.warrox.warblox.game.objects.LightSource;
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

        // 1. Camera iets hoger voor overzicht
        this.camera = new Camera(new Vector3f(0, 5, 15));

        this.player = new Player(new Transform(new Vector3f(0, 10, 4), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1)), new Vector4f(0, 1, 0, 1), rb);
        rb.addGameObject(player);

        // 2. Voeg een grote vloer toe (zonder vloer zie je geen lichtval!)
        // We schalen een cube heel plat en breed
        addBlock(new Cube(
                new Transform(new Vector3f(0, -1, 0), new Vector3f(0), new Vector3f(20, 0.5f, 20)),
                new Vector4f(0.2f, 0.2f, 0.2f, 1.0f), // Donkergrijze vloer reflecteert mooi
                rb
        ));

        // 3. Plaats objecten in het midden van de lichten
        addBlock(new Cube(new Transform(new Vector3f(0, 0.5f, 0), new Vector3f(0), new Vector3f(1)), "assets/textures/block/diamond_block.png", rb));
        addBlock(new Cube(new Transform(new Vector3f(-2, 0.5f, 2), new Vector3f(0), new Vector3f(1)), new Vector4f(1, 0, 0, 1), rb));

        // 4. Goede Lichtposities (Drie-punts belichting + sfeer)
        Vector3f[] lightPositions = {
                new Vector3f(5, 5, 5),    // Rechtsvoor hoog (Key light)
                new Vector3f(-5, 3, 2),   // Linksvoor (Fill light)
                new Vector3f(0, 2, -5),   // Achterkant (Backlight voor randjes)
                new Vector3f(0, 8, 0)     // Top light
        };

        Vector4f[] lightColors = {
                new Vector4f(1.0f, 0.9f, 0.8f, 1.0f), // Warm wit
                new Vector4f(0.8f, 0.8f, 1.0f, 1.0f), // Koel blauw
                new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), // Helder wit
                new Vector4f(1.0f, 0.5f, 0.0f, 1.0f)  // Oranje gloed
        };

        for (int i = 0; i < lightPositions.length; i++) {
            LightSource ls = new LightSource(
                    new Transform(lightPositions[i], new Vector3f(0), new Vector3f(0.3f)),
                    lightColors[i],
                    rb
            );
            rb.addGameObject(ls);
        }
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

}

