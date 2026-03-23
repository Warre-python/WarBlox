package be.warrox.engine.scene;

import be.warrox.engine.gfx.Mesh;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Mesh> objects = new ArrayList<Mesh>();

    public void addObject(Mesh object) {
        objects.add(object);
    }

    public List<Mesh> getObjects() {
        return objects;
    }

    public void updateObjects() {

    }

    public void renderObjects() {
        for (Mesh object : objects) {
            object.draw();
        }
    }
}
