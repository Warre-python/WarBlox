package be.warrox.warblox.renderEngine;

public class Primitives {
    public static float[] rectangleVertices = {
            // positions          // texture coords
            0.5f, 0.5f, 0.0f,     1.0f, 1.0f,
            0.5f, -0.5f, 0.0f,    1.0f, 0.0f,
            -0.5f, -0.5f, 0.0f,   0.0f, 0.0f,
            -0.5f, 0.5f, 0.0f,    0.0f, 1.0f
    };

    public static int[] rectangleIndices = {
            0, 1, 3,
            1, 2, 3
    };

    public static float[] cubeVertices = {
            // positions           // tex coords  // normals
            // Front face
            -0.5f, -0.5f, 0.5f,    0.0f, 1.0f,    0.0f, 0.0f, 1.0f,
            0.5f, -0.5f, 0.5f,     1.0f, 1.0f,    0.0f, 0.0f, 1.0f,
            0.5f, 0.5f, 0.5f,      1.0f, 0.0f,    0.0f, 0.0f, 1.0f,
            -0.5f, 0.5f, 0.5f,     0.0f, 0.0f,    0.0f, 0.0f, 1.0f,

            // Back face
            -0.5f, -0.5f, -0.5f,   1.0f, 1.0f,    0.0f, 0.0f, -1.0f,
            -0.5f, 0.5f, -0.5f,    1.0f, 0.0f,    0.0f, 0.0f, -1.0f,
            0.5f, 0.5f, -0.5f,     0.0f, 0.0f,    0.0f, 0.0f, -1.0f,
            0.5f, -0.5f, -0.5f,    0.0f, 1.0f,    0.0f, 0.0f, -1.0f,

            // Top face
            -0.5f, 0.5f, -0.5f,    0.0f, 0.0f,    0.0f, 1.0f, 0.0f,
            -0.5f, 0.5f, 0.5f,     0.0f, 1.0f,    0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.5f,      1.0f, 1.0f,    0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, -0.5f,     1.0f, 0.0f,    0.0f, 1.0f, 0.0f,

            // Bottom face
            -0.5f, -0.5f, -0.5f,   0.0f, 1.0f,    0.0f, -1.0f, 0.0f,
            0.5f, -0.5f, -0.5f,    1.0f, 1.0f,    0.0f, -1.0f, 0.0f,
            0.5f, -0.5f, 0.5f,     1.0f, 0.0f,    0.0f, -1.0f, 0.0f,
            -0.5f, -0.5f, 0.5f,    0.0f, 0.0f,    0.0f, -1.0f, 0.0f,

            // Right face
            0.5f, -0.5f, -0.5f,    1.0f, 1.0f,    1.0f, 0.0f, 0.0f,
            0.5f, 0.5f, -0.5f,     1.0f, 0.0f,    1.0f, 0.0f, 0.0f,
            0.5f, 0.5f, 0.5f,      0.0f, 0.0f,    1.0f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.5f,     0.0f, 1.0f,    1.0f, 0.0f, 0.0f,

            // Left face
            -0.5f, -0.5f, -0.5f,   0.0f, 1.0f,    -1.0f, 0.0f, 0.0f,
            -0.5f, -0.5f, 0.5f,    1.0f, 1.0f,    -1.0f, 0.0f, 0.0f,
            -0.5f, 0.5f, 0.5f,     1.0f, 0.0f,    -1.0f, 0.0f, 0.0f,
            -0.5f, 0.5f, -0.5f,    0.0f, 0.0f,    -1.0f, 0.0f, 0.0f
    };

    public static int[] cubeIndices = {
            // Front face
            0, 1, 2, 0, 2, 3,
            // Back face
            4, 5, 6, 4, 6, 7,
            // Top face
            8, 9, 10, 8, 10, 11,
            // Bottom face
            12, 13, 14, 12, 14, 15,
            // Right face
            16, 17, 18, 16, 18, 19,
            // Left face
            20, 21, 22, 20, 22, 23
    };
}
