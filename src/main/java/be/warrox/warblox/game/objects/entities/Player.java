package be.warrox.warblox.game.objects.entities;

import be.warrox.warblox.game.objects.Cube;
import be.warrox.warblox.renderEngine.Camera;
import be.warrox.warblox.renderEngine.RenderBatch;
import be.warrox.warblox.renderEngine.Transform;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Player extends Cube {

    public Player(Transform transform, Vector4f color) {
        // We roepen de Cube constructor aan die nu een Model genereert
        super(transform, color);
    }

    // Gebruik de update(camera) om de player 'vast' te maken aan de camera (First Person)
    // Of laat de player vrij bewegen en laat de camera volgen.
    public void update(Camera camera) {
        // Voor een First-Person view: Plaats het 'lichaam' van de speler
        // een klein stukje onder en achter de camera zodat je jezelf niet ziet,
        // maar wel schaduw vangt.
        Vector3f cameraPos = camera.position;
        Vector3f offset = new Vector3f(camera.front).mul(-0.5f); // Klein beetje naar achter

        this.transform.position.set(cameraPos.x + offset.x, cameraPos.y - 1.5f, cameraPos.z + offset.z);

        // Zorg dat het model van de speler dezelfde kant op kijkt als de camera
        // (Optioneel: handig als je later een echt character model gebruikt)
        this.transform.rotation.y = (float) Math.toDegrees(Math.atan2(camera.front.x, camera.front.z));
    }
}

