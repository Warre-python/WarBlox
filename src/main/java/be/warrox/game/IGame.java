package be.warrox.game;

public interface IGame {
    void init(Window window);
    void input(Window window, Scene scene);
    void update(float interval, Scene scene);
    void render(Renderer renderer, Scene scene);
}
