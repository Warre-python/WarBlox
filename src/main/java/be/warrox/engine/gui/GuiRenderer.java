package be.warrox.engine.gui;

import be.warrox.engine.core.Window;
import be.warrox.engine.gfx.Shader;
import org.joml.Matrix4f;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class GuiRenderer {
    private Shader guiShader;
    private Matrix4f projectionMatrix;

    public void init() {
        guiShader = new Shader("assets/shaders/2d.glsl");
        guiShader.compile();
        // Create an orthographic projection matrix for UI
        // This maps pixel coordinates (0 to width, 0 to height) to (-1 to 1)
        projectionMatrix = new Matrix4f().ortho(0, Window.width, Window.height, 0, -1, 1);
    }

    public void render(List<Element> elements) {
        guiShader.use();
        
        // Update projection if window resized (optional for now)
        projectionMatrix.identity().ortho(0, Window.width, Window.height, 0, -1, 1);
        guiShader.uploadMat4f("uProjection", projectionMatrix);

        // Disable depth testing so GUI is always on top
        glDisable(GL_DEPTH_TEST);
        // Enable blending for transparent GUI elements
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        for (Element element : elements) {
            if (element.visible) {
                element.draw(getShader());
            }
        }

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        guiShader.detach();
    }

    public Shader getShader() {
        return guiShader;
    }

}