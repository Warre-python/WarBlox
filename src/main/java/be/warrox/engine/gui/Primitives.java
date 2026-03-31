package be.warrox.engine.gui;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Primitives {
    public static GuiVertex[] rectangleVertices = {
            // Linksboven (0,0)
            new GuiVertex(new Vector3f(0.0f, 0.0f, 0.0f), new Vector2f(0.0f, 0.0f)),
            // Rechtsboven (1,0)
            new GuiVertex(new Vector3f(1.0f, 0.0f, 0.0f), new Vector2f(1.0f, 0.0f)),
            // Rechtsonder (1,1)
            new GuiVertex(new Vector3f(1.0f, 1.0f, 0.0f), new Vector2f(1.0f, 1.0f)),
            // Linksonder (0,1)
            new GuiVertex(new Vector3f(0.0f, 1.0f, 0.0f), new Vector2f(0.0f, 1.0f))
    };

    public static int[] rectangleIndices = {
            0, 1, 2, // Driehoek 1
            2, 3, 0  // Driehoek 2
    };
}
