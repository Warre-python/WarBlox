package be.warrox.engine.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class GuiVertex {

    public final Vector3f position;
    public final Vector2f texCoords;

    public static final int POS_SIZE = 3;
    public static final int TEXCOORDS_SIZE = 2;

    public static final int VERTEX_SIZE_FLOATS = POS_SIZE + TEXCOORDS_SIZE;
    public static final int VERTEX_SIZE_BYTES = VERTEX_SIZE_FLOATS * Float.BYTES;

    public static final int POS_OFFSET = 0;
    public static final int TEXCOORDS_OFFSET = POS_OFFSET + (POS_SIZE * Float.BYTES);

    // 2. Full Constructor
    public GuiVertex(Vector3f position, Vector2f texCoords) {
        this.position = position;
        this.texCoords = (texCoords == null) ? new Vector2f(0, 0) : texCoords;
    }

    // 3. Convenience Constructor for simple shapes (like your Triangle)
    public GuiVertex(Vector3f position) {
        this(position, new Vector2f(0, 0));
    }

    // 4. Default constructor for "Empty" vertex
    public GuiVertex() {
        this(new Vector3f(0), new Vector2f(0));
    }
}