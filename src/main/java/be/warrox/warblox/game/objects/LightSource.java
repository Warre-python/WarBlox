package be.warrox.warblox.game.objects;

import be.warrox.warblox.renderEngine.RenderBatch;
import be.warrox.warblox.renderEngine.Transform;
import org.joml.Vector4f;

public class LightSource extends Cube{
    public LightSource(Transform transform, Vector4f color, RenderBatch rb) {
        super(transform, color, rb);
    }
}
