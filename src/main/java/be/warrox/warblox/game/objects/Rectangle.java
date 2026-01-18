package be.warrox.warblox.game.objects;

import be.warrox.warblox.renderEngine.GameObject;
import be.warrox.warblox.renderEngine.Primitives;
import be.warrox.warblox.renderEngine.RenderBatch;
import be.warrox.warblox.renderEngine.Transform;

public class Rectangle extends GameObject {
    public Rectangle(Transform transform, String path, RenderBatch rb) {
        super(transform, path, rb, Primitives.rectangleVertices, Primitives.rectangleIndices);
    }
}
