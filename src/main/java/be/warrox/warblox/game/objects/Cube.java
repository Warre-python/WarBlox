package be.warrox.warblox.game.objects;

import be.warrox.warblox.renderEngine.*;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Cube extends GameObject {
    public Cube(Transform transform, String path, RenderBatch rb) {
        super(transform, path, rb, Primitives.cubeVertices, Primitives.cubeIndices);
    }

    public Cube(Vector3f pos, String path, RenderBatch rb) {
        super(new Transform(pos, new Vector3f(0, 0,0), new Vector3f(1, 1, 1)), path, rb, Primitives.cubeVertices, Primitives.cubeIndices);
    }

    public Cube(Transform transform, Vector4f color, RenderBatch rb) {
        super(transform, color, rb, Primitives.cubeVertices, Primitives.cubeIndices);
    }

    public void update(Camera camera) {

    }
}
