package be.warrox.engine.scene;

import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.Shader;
import be.warrox.engine.objects.Object;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Object> objects = new ArrayList<>();

    public Scene(Shader shader) {
    }
    public void update() {
        for (Object object : objects) {
            object.update();
        }
    }
    public void addObject(Object object) {
        objects.add(object);
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void updateObjects() {

    }


}
