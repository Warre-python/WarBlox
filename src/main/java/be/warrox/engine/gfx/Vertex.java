package be.warrox.engine.gfx;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Vertex {
    public Vector3f position;
    public Vector3f normal;
    public Vector2f texCoords;
    public Vector4f color;

    // Look at these sizes carefully
    public static final int POS_SIZE = 3;
    public static final int NORMAL_SIZE = 3;
    public static final int TEXCOORDS_SIZE = 2;
    public static final int COLOR_SIZE = 4; // Changed to 4 to match Vector4f

    // Offsets in BYTES (Required for glVertexAttribPointer)
    public static final int POS_OFFSET = 0;
    public static final int NORMAL_OFFSET = POS_OFFSET + (POS_SIZE * Float.BYTES);
    public static final int TEXCOORDS_OFFSET = NORMAL_OFFSET + (NORMAL_SIZE * Float.BYTES);
    public static final int COLOR_OFFSET = TEXCOORDS_OFFSET + (TEXCOORDS_SIZE * Float.BYTES);

    // Total size of ONE vertex
    public static final int VERTEX_SIZE_FLOATS = POS_SIZE + NORMAL_SIZE + TEXCOORDS_SIZE + COLOR_SIZE; // 12
    public static final int VERTEX_SIZE_BYTES = VERTEX_SIZE_FLOATS * Float.BYTES; // 48 bytes
}
