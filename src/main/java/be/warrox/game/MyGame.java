package be.warrox.game;

import be.warrox.engine.core.Window;
import be.warrox.engine.gfx.*;
import be.warrox.engine.objects.Object;
import be.warrox.engine.objects.Primitives;
import be.warrox.engine.scene.Entity;
import be.warrox.engine.scene.Scene;
import be.warrox.engine.scene.Transform;
import be.warrox.game.world.World;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class MyGame implements IGame {
    private World world; // Your Voxel World

    @Override
    public void init(Window window, Scene scene) {
        scene.addObject(new Object(new Mesh(Primitives.verticesRectangleText, Primitives.indicesRectangleText, new Texture("assets/textures/block/grass_block_side.png"))));
    }

    @Override
    public void input(Window window, Scene scene) {

    }

    @Override
    public void update(float interval, Scene scene) {
       scene.update();
    }

    @Override
    public void render(Renderer renderer, Scene scene) {

        renderer.renderObjects(renderer.getShader(), scene.getObjects());

    }
}
