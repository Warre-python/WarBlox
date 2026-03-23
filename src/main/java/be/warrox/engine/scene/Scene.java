package be.warrox.engine.scene;

import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.Shader;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Mesh> objects = new ArrayList<Mesh>();

    public Scene(Shader shader) {
    }

    public void addObject(Mesh object) {
        objects.add(object);
    }

    public List<Mesh> getObjects() {
        return objects;
    }

    public void updateObjects() {

    }

    public void renderObjects(Shader shader) {
        for (Mesh object : objects) {
            object.draw(shader);
        }
    }
}
