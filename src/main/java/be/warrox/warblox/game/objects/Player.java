package be.warrox.warblox.game.objects;

import be.warrox.warblox.renderEngine.Camera;
import be.warrox.warblox.renderEngine.RenderBatch;
import be.warrox.warblox.renderEngine.Transform;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Player extends Cube{
    private Transform transform;
    public Player(Transform transform, Vector4f color, RenderBatch rb) {
        super(transform, color, rb);
        this.transform = transform;
    }

    @Override
    public void update(Camera camera) {
        Vector3f newPos = new Vector3f(camera.position);
        transform.setPosition(newPos.sub(camera.front));
    }
}
