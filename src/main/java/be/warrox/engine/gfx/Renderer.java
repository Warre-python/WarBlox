package be.warrox.engine.gfx;

import be.warrox.engine.scene.Entity;
import be.warrox.engine.scene.Scene;
import be.warrox.engine.world.World;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    private Shader sceneShader;

    public void init() {
        // Basic OpenGL state setup
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE); // Don't render backs of triangles
        glCullFace(GL_BACK);

        // Setup Alpha Blending for 2D/Translucency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Initialize Shaders (You'll need to create this class next)
        sceneShader = new Shader("assets/shaders/scene.vert", "assets/shaders/scene.frag");
    }

    public void clear() {
        // Clears the color and the depth buffer so previous frames don't "smear"
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void renderWorld(World world, Scene scene) {
        sceneShader.bind();

        // Pass Projection and View matrices from the Camera to the Shader
        sceneShader.setUniform("projectionMatrix", scene.getCamera().getProjectionMatrix());
        sceneShader.setUniform("viewMatrix", scene.getCamera().getViewMatrix());

        // Render each chunk in the world
        world.getChunks().values().forEach(chunk -> {
            // Only draw chunks that have a mesh
            if (chunk.getMesh() != null) {
                chunk.getMesh().draw();
            }
        });

        sceneShader.unbind();
    }

    public void renderEntities(Scene scene) {
        sceneShader.bind();

        // Render Assimp models/entities
        for (Entity entity : scene.getEntities()) {
            sceneShader.setUniform("modelMatrix", entity.getTransform().getMatrix());
            entity.getModel().draw();
        }

        sceneShader.unbind();
    }
}