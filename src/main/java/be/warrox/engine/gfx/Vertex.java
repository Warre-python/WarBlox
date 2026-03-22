package be.warrox.engine.gfx;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Vertex {
    public Vector3f position;
    public Vector3f normal;
    public Vector2f texCoords;
    public Vector4f color; // Useful for 2D sprites or vertex painting

    // Size of this vertex in floats: 3 + 3 + 2 + 4 = 12 floats
    public static final int SIZE = 12;
    public static final int BYTES = SIZE * Float.BYTES;
}
