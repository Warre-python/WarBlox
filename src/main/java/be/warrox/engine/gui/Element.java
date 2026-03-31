package be.warrox.engine.gui;

import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.Shader;
import be.warrox.engine.scene.Transform;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Element {
    protected float x, y, width, height;
    protected boolean visible = true;
    private GuiMesh mesh;
    private Vector4f color;
    private boolean textured;

    // Color-only element
    public Element(float x, float y, float width, float height, Vector4f color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.textured = false;
        this.mesh = new GuiMesh(Primitives.verRectangle, Primitives.indRectangle, color, new Transform(new Vector3f(x, y, 0), new Vector3f(), new Vector3f(width, height, 1)));
    }

    // Texture-based element
    public Element(float x, float y, float width, float height, String texturePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textured = true;
        this.mesh = new GuiMesh(Primitives.verRectangle, Primitives.indRectangle, texturePath, new Transform(new Vector3f(x, y, 0), new Vector3f(), new Vector3f(width, height, 1)));
    }

    public void draw(Shader shader) {
        shader.uploadBool("useTexture", textured);
        mesh.draw(shader, null);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.mesh.getTransform().getPosition().set(x, y, 0);
    }
}