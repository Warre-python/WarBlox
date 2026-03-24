package be.warrox.engine.objects;

import be.warrox.engine.gfx.Vertex;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Primitives {
    public static Vertex[] verticesRectangle = new Vertex[] {
            new Vertex(new Vector3f(0.5f,  0.5f, 0.0f), new Vector4f(1, 0, 0, 1)),
            new Vertex(new Vector3f( 0.5f, -0.5f, 0.0f), new Vector4f(0, 1, 0, 1)),
            new Vertex(new Vector3f( -0.5f, -0.5f, 0.0f), new Vector4f(0, 0, 1, 1)),
            new Vertex(new Vector3f(-0.5f,  0.5f, 0.0f), new Vector4f(1, 1, 1, 1))
    };

    public static int[] indicesRectangle = new int[] {
            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle
    };

    public static Vertex[] verticesRectangleText = new Vertex[] {
            new Vertex(new Vector3f(0.5f,  0.5f, 0.0f), new Vector2f(1.0f, 1.0f)),
            new Vertex(new Vector3f( 0.5f, -0.5f, 0.0f), new Vector2f(1.0f, 0.0f)),
            new Vertex(new Vector3f( -0.5f, -0.5f, 0.0f), new Vector2f(0.0f, 0.0f)),
            new Vertex(new Vector3f(-0.5f,  0.5f, 0.0f), new Vector2f(0.0f, 1.0f))
    };

    public static int[] indicesRectangleText = new int[] {
            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle
    };


    public static Vertex[] verticesCube = new Vertex[] {
            // Front Face
            new Vertex(new Vector3f(-0.5f,  0.5f,  0.5f), new Vector2f(0.0f, 1.0f)),
            new Vertex(new Vector3f( 0.5f,  0.5f,  0.5f), new Vector2f(1.0f, 1.0f)),
            new Vertex(new Vector3f( 0.5f, -0.5f,  0.5f), new Vector2f(1.0f, 0.0f)),
            new Vertex(new Vector3f(-0.5f, -0.5f,  0.5f), new Vector2f(0.0f, 0.0f)),

            // Back Face
            new Vertex(new Vector3f( 0.5f,  0.5f, -0.5f), new Vector2f(0.0f, 1.0f)),
            new Vertex(new Vector3f(-0.5f,  0.5f, -0.5f), new Vector2f(1.0f, 1.0f)),
            new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector2f(1.0f, 0.0f)),
            new Vertex(new Vector3f( 0.5f, -0.5f, -0.5f), new Vector2f(0.0f, 0.0f)),

            // Top Face
            new Vertex(new Vector3f(-0.5f,  0.5f, -0.5f), new Vector2f(0.0f, 1.0f)),
            new Vertex(new Vector3f( 0.5f,  0.5f, -0.5f), new Vector2f(1.0f, 1.0f)),
            new Vertex(new Vector3f( 0.5f,  0.5f,  0.5f), new Vector2f(1.0f, 0.0f)),
            new Vertex(new Vector3f(-0.5f,  0.5f,  0.5f), new Vector2f(0.0f, 0.0f)),

            // Bottom Face
            new Vertex(new Vector3f(-0.5f, -0.5f,  0.5f), new Vector2f(0.0f, 1.0f)),
            new Vertex(new Vector3f( 0.5f, -0.5f,  0.5f), new Vector2f(1.0f, 1.0f)),
            new Vertex(new Vector3f( 0.5f, -0.5f, -0.5f), new Vector2f(1.0f, 0.0f)),
            new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector2f(0.0f, 0.0f)),

            // Left Face
            new Vertex(new Vector3f(-0.5f,  0.5f, -0.5f), new Vector2f(0.0f, 1.0f)),
            new Vertex(new Vector3f(-0.5f,  0.5f,  0.5f), new Vector2f(1.0f, 1.0f)),
            new Vertex(new Vector3f(-0.5f, -0.5f,  0.5f), new Vector2f(1.0f, 0.0f)),
            new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector2f(0.0f, 0.0f)),

            // Right Face
            new Vertex(new Vector3f( 0.5f,  0.5f,  0.5f), new Vector2f(0.0f, 1.0f)),
            new Vertex(new Vector3f( 0.5f,  0.5f, -0.5f), new Vector2f(1.0f, 1.0f)),
            new Vertex(new Vector3f( 0.5f, -0.5f, -0.5f), new Vector2f(1.0f, 0.0f)),
            new Vertex(new Vector3f( 0.5f, -0.5f,  0.5f), new Vector2f(0.0f, 0.0f)),
    };

    public static int[] indicesCube = new int[] {
            0, 1, 2, 2, 3, 0,       // Front
            4, 5, 6, 6, 7, 4,       // Back
            8, 9, 10, 10, 11, 8,    // Top
            12, 13, 14, 14, 15, 12, // Bottom
            16, 17, 18, 18, 19, 16, // Left
            20, 21, 22, 22, 23, 20  // Right
    };



}
