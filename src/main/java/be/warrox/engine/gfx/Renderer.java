package be.warrox.engine.gfx;

import be.warrox.engine.objects.Object;
import be.warrox.engine.scene.Entity;
import be.warrox.engine.scene.Scene;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private Shader sceneShader;

    public void init() {
        // Basic OpenGL state setup
        glEnable(GL_DEPTH_TEST);
        //glEnable(GL_CULL_FACE); // Don't render backs of triangles
        //glCullFace(GL_BACK);

        // Setup Alpha Blending for 2D/Translucency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Initialize Shaders (You'll need to create this class next)
        sceneShader = new Shader("assets/shaders/3d.glsl");
        sceneShader.compile();
    }

    public void clear() {
        // Clears the color and the depth buffer so previous frames don't "smear"
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void renderObjects(Shader shader, List<Object> objects) {
        for (Object object : objects) {
            object.draw(shader);
        }
    }

    public Shader getShader() {
        return sceneShader;
    }




}