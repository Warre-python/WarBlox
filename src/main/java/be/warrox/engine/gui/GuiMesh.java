package be.warrox.engine.gui;

import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.Shader;
import be.warrox.engine.gfx.Texture;
import be.warrox.engine.gfx.Vertex;
import be.warrox.engine.scene.Camera;
import be.warrox.engine.scene.Transform;
import be.warrox.engine.util.AssetManager;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GuiMesh extends Mesh {

    public GuiMesh(Vertex[] vertices, int[] indices, Vector4f color, Transform transform) {
        super(vertices, indices, color, transform);
    }

    public GuiMesh(Vertex[] vertices, int[] indices, String texturePath, Transform transform) {
        super(vertices, indices, texturePath, transform);
    }

    @Override
    public void draw(Shader shader, Camera camera) {
        // We pass null for camera because GuiRenderer handles the Projection matrix,
        // and GUI elements typically do not use a View matrix.
        super.draw(shader, camera);
    }
}