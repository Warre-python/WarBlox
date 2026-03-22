package be.warrox.engine.core;

import be.warrox.game.IGame;

public class Engine implements Runnable {
    private final Window window;
    private final IGame gameLogic;

    public Engine(String title, int w, int h, IGame gameLogic) {
        this.window = new Window(title, w, h);
        this.gameLogic = gameLogic;
    }

    @Override
    public void run() {
        init();
        gameLoop();
    }

    private void init() {
        window.init(); // Creates the GLFW window
        gameLogic.init(window); // Tells the game to load textures/models
    }

    private void gameLoop() {
        while (!window.windowShouldClose()) {
            // Update logic (Physics, Input)
            gameLogic.update(getDeltaTime());

            // Render logic
            gameLogic.render(window);

            window.update(); // Swap buffers and poll events
        }
    }
}
