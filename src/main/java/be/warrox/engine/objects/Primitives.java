package be.warrox.engine.objects;

import be.warrox.engine.gfx.Vertex;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Primitives {
    public static Vertex[] verticesRectangle = new Vertex[] {
            new Vertex(new Vector3f(-0.5f,  0.5f, 0.0f), new Vector4f(1, 0, 0, 1)), // Top Left (Red)
            new Vertex(new Vector3f( 0.5f,  0.5f, 0.0f), new Vector4f(0, 1, 0, 1)), // Top Right (Green)
            new Vertex(new Vector3f( 0.5f, -0.5f, 0.0f), new Vector4f(0, 0, 1, 1)), // Bottom Right (Blue)
            new Vertex(new Vector3f(-0.5f, -0.5f, 0.0f), new Vector4f(1, 1, 1, 1))  // Bottom Left (White)
    };

    public static int[] indicesRectangle = new int[] {
            0, 1, 2,
            2, 3, 0
    };

    public static Vertex[] verticesRectangleText = new Vertex[] {
            new Vertex(new Vector3f(-0.5f,  0.5f, 0.0f), new Vector2f(0.0f, 0.0f)),
            new Vertex(new Vector3f( 0.5f,  0.5f, 0.0f), new Vector2f(1.0f, 0.0f)),
            new Vertex(new Vector3f( 0.5f, -0.5f, 0.0f), new Vector2f(1.0f, 1.0f)),
            new Vertex(new Vector3f(-0.5f, -0.5f, 0.0f), new Vector2f(0.0f, 1.0f))
    };

    public static int[] indicesRectangleText = new int[] {
            0, 1, 2, // First Triangle
            2, 3, 0  // Second Triangle
    };


}
