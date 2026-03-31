package be.warrox.engine.gui;

import be.warrox.engine.gfx.Vertex;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Primitives {
    // A standard unit rectangle (0,0 to 1,1) with both texture coordinates and white vertex colors.
    // This allows both textures and per-vertex coloring to work in the same primitive.
    public static Vertex[] verRectangle = new Vertex[] {
            new Vertex(new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(0, 0, 1), new Vector2f(1.0f, 1.0f), new Vector4f(1, 1, 1, 1)),
            new Vertex(new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0, 0, 1), new Vector2f(1.0f, 0.0f), new Vector4f(1, 1, 1, 1)),
            new Vertex(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0, 0, 1), new Vector2f(0.0f, 0.0f), new Vector4f(1, 1, 1, 1)),
            new Vertex(new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0, 0, 1), new Vector2f(0.0f, 1.0f), new Vector4f(1, 1, 1, 1))
    };

    public static int[] indRectangle = new int[] {
            0, 1, 3,
            1, 2, 3
    };

    // Legacy multicolored rectangle - kept for backwards compatibility if needed, 
    // but using the full constructor now to match Vertex.java
    public static Vertex[] verRectangleCol = new Vertex[] {
            new Vertex(new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(0, 0, 1), new Vector2f(1, 1), new Vector4f(1, 0, 0, 1)),
            new Vertex(new Vector3f(1.0f, 0.0f, 0.0f), new Vector3f(0, 0, 1), new Vector2f(1, 0), new Vector4f(0, 1, 0, 1)),
            new Vertex(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0, 0, 1), new Vector2f(0, 0), new Vector4f(0, 0, 1, 1)),
            new Vertex(new Vector3f(0.0f, 1.0f, 0.0f), new Vector3f(0, 0, 1), new Vector2f(0, 1), new Vector4f(1, 1, 1, 1))
    };

    public static int[] indRectangleCol = new int[] {
            0, 1, 3,
            1, 2, 3
    };
}