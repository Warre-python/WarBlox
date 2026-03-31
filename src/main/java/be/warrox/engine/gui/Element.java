package be.warrox.engine.gui;

import be.warrox.engine.gfx.Renderer;
import be.warrox.engine.scene.Transform;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Element {
    private int x, y, width, height;

    private Vector4f color;
    private String path;

    private GuiMesh mesh;

    public Element(int x, int y, int width, int height, Vector4f color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;

        this.mesh = new GuiMesh(Primitives.rectangleVertices, Primitives.rectangleIndices, color);

    }

    public Element(int x, int y, int width, int height, String path) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.path = path;

        this.mesh = new GuiMesh(Primitives.rectangleVertices, Primitives.rectangleIndices, path);


    }

    public void draw(Renderer Renderer) {
        mesh.draw(Renderer.getGuiShader(), x, y, width, height);
    }
}
