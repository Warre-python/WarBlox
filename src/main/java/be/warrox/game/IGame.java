package be.warrox.game;

import be.warrox.engine.core.Window;
import be.warrox.engine.gfx.Renderer;
import be.warrox.engine.scene.Scene;

public interface IGame {
    void init(Window window);
    void input(Window window, Scene scene);
    void update(float interval, Scene scene);
    void render(Renderer renderer, Scene scene);
}
