package be.warrox.warblox.renderEngine;

public class Primitives {
    public static float[] rectangleVertices = {
            // positions         // texture coords
            0.5f,  0.5f, 0.0f,   1.0f, 1.0f,   // top right
            0.5f, -0.5f, 0.0f,   1.0f, 0.0f,   // bottom right
            -0.5f, -0.5f, 0.0f,  0.0f, 0.0f,   // bottom left
            -0.5f,  0.5f, 0.0f,  0.0f, 1.0f    // top left

    };

    public static int[] rectangleIndices = {
            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle
    };

    public static float[] cubeVertices = {
            // Front face (Z = 0.5)
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, // Top Left     (0)
            -0.5f, -0.5f,  0.5f,  0.0f, 1.0f, // Bottom Left  (1)
            0.5f, -0.5f,  0.5f,  1.0f, 1.0f, // Bottom Right (2)
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f, // Top Right    (3)

            // Back face (Z = -0.5)
            -0.5f,  0.5f, -0.5f,  1.0f, 0.0f, // Top Left     (4)
            -0.5f, -0.5f, -0.5f,  1.0f, 1.0f, // Bottom Left  (5)
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f, // Bottom Right (6)
            0.5f,  0.5f, -0.5f,  0.0f, 0.0f, // Top Right    (7)

            // Top face (Y = 0.5)
            -0.5f,  0.5f, -0.5f,  0.0f, 0.0f, // (8)
            -0.5f,  0.5f,  0.5f,  0.0f, 1.0f, // (9)
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f, // (10)
            0.5f,  0.5f, -0.5f,  1.0f, 0.0f, // (11)

            // Bottom face (Y = -0.5)
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, // (12)
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, // (13)
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f, // (14)
            0.5f, -0.5f, -0.5f,  1.0f, 1.0f, // (15)

            // Right face (X = 0.5)
            0.5f,  0.5f,  0.5f,  0.0f, 0.0f, // (16)
            0.5f, -0.5f,  0.5f,  0.0f, 1.0f, // (17)
            0.5f, -0.5f, -0.5f,  1.0f, 1.0f, // (18)
            0.5f,  0.5f, -0.5f,  1.0f, 0.0f, // (19)

            // Left face (X = -0.5)
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f, // (20)
            -0.5f, -0.5f,  0.5f,  1.0f, 1.0f, // (21)
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, // (22)
            -0.5f,  0.5f, -0.5f,  0.0f, 0.0f  // (23)
    };

    public static int[] cubeIndices = {
            0, 1, 3, 3, 1, 2,       // Front
            4, 5, 7, 7, 5, 6,       // Back
            8, 9, 11, 11, 9, 10,    // Top
            12, 13, 15, 15, 13, 14, // Bottom
            16, 17, 19, 19, 17, 18, // Right
            20, 21, 23, 23, 21, 22  // Left
    };
}
