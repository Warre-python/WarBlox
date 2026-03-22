package be.warrox.engine.gfx;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Vertex {
    // 1. Make fields final or provide sensible defaults to prevent NullPointerExceptions
    public final Vector3f position;
    public final Vector3f normal;
    public final Vector2f texCoords;
    public final Vector4f color;

    public static final int POS_SIZE = 3;
    public static final int NORMAL_SIZE = 3;
    public static final int TEXCOORDS_SIZE = 2;
    public static final int COLOR_SIZE = 4;

    public static final int VERTEX_SIZE_FLOATS = POS_SIZE + NORMAL_SIZE + TEXCOORDS_SIZE + COLOR_SIZE;
    public static final int VERTEX_SIZE_BYTES = VERTEX_SIZE_FLOATS * Float.BYTES;

    public static final int POS_OFFSET = 0;
    public static final int NORMAL_OFFSET = POS_OFFSET + (POS_SIZE * Float.BYTES);
    public static final int TEXCOORDS_OFFSET = NORMAL_OFFSET + (NORMAL_SIZE * Float.BYTES);
    public static final int COLOR_OFFSET = TEXCOORDS_OFFSET + (TEXCOORDS_SIZE * Float.BYTES);

    // 2. Full Constructor
    public Vertex(Vector3f position, Vector3f normal, Vector2f texCoords, Vector4f color) {
        this.position = position;
        this.normal = (normal == null) ? new Vector3f(0, 1, 0) : normal;
        this.texCoords = (texCoords == null) ? new Vector2f(0, 0) : texCoords;
        this.color = (color == null) ? new Vector4f(1, 1, 1, 1) : color;
    }

    // 3. Convenience Constructor for simple shapes (like your Triangle)
    public Vertex(Vector3f position, Vector4f color) {
        this(position, new Vector3f(0, 0, 1), new Vector2f(0, 0), color);
    }

    // 4. Convenience Constructor for textured models
    public Vertex(Vector3f position, Vector2f texCoords) {
        this(position, new Vector3f(0, 0, 1), texCoords, new Vector4f(1, 1, 1, 1));
    }

    // 5. Default constructor for "Empty" vertex
    public Vertex() {
        this(new Vector3f(0), new Vector3f(0, 1, 0), new Vector2f(0), new Vector4f(1));
    }
}