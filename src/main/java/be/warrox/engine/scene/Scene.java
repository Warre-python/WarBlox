package be.warrox.engine.scene;

import be.warrox.engine.gfx.Shader;
import be.warrox.game.world.Chunk;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<GameObject> objects = new ArrayList<>();
    private Camera camera;
    private List<Chunk> chunks = new ArrayList<>();


    public Scene(Shader shader) {
    }
    public void update() {
        for (GameObject object : objects) {
            object.update();
        }
    }
    public void addObject(GameObject object) {
        objects.add(object);
    }

    public List<GameObject> getObjects() {
        return objects;
    }


    public void addCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    public void addChunk(Chunk chunk) {
        chunks.add(chunk);
    }

    public List<Chunk> getChunks() {
        return chunks;
    }


}
