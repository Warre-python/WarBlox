package be.warrox.warblox.game.objects;

import be.warrox.warblox.renderEngine.*;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Cube extends GameObject {
    public Cube(Transform transform, String path) {
        super(transform, path, Primitives.cubeVertices, Primitives.cubeIndices);
    }

    public Cube(Vector3f pos, String path) {
        super(new Transform(pos, new Vector3f(0, 0,0), new Vector3f(1, 1, 1)), path, Primitives.cubeVertices, Primitives.cubeIndices);
    }

    public Cube(Transform transform, Vector4f color) {
        super(transform, color, Primitives.cubeVertices, Primitives.cubeIndices);
    }

    public void update(Camera camera) {

    }

}
