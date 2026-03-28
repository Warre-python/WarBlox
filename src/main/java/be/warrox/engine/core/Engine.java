package be.warrox.engine.core;

import be.warrox.engine.gfx.Renderer;
import be.warrox.engine.scene.Scene;
import be.warrox.engine.util.AssetManager;
import be.warrox.game.IGame;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Engine implements Runnable {

    private final Window window;
    private final IGame gameLogic;
    private final Renderer renderer;
    private final Scene scene;

    // Time management variables
    private double lastTime;

    public Engine(String title, int w, int h, IGame gameLogic, boolean vsync) {
        this.window = new Window(title, w, h, vsync);
        this.gameLogic = gameLogic;

        // Initialize Engine-level components
        this.renderer = new Renderer();
        this.scene = new Scene(this.renderer.getShader());

    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } finally {
            cleanup();
        }
    }

    private void init() {
        window.init(); // Setup GLFW and OpenGL context
        renderer.init(); // Setup shaders and global GL states



        // The game's init now has access to the window to load assets
        gameLogic.init(window, scene);

        lastTime = glfwGetTime();
    }

    private void gameLoop() {
        float delta;
        while (!window.windowShouldClose()) {
            delta = getDeltaTime();

            // 1. Handle Input
            gameLogic.input(window, scene, delta);

            // 2. Update Physics/Logic
            gameLogic.update(delta, scene);

            // 3. Render
            renderer.clear(); // Clears color and depth buffers

            // The game decides WHAT to render using the engine's tools
            gameLogic.render(renderer, scene);

            MouseListener.endFrame();

            // 4. Swap buffers & poll events
            window.update();

            if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
                cleanup();
                break;
            }

            System.out.println("Fps: " + (1.0f / delta));
        }
    }

    private float getDeltaTime() {
        double currentTime = glfwGetTime();
        float delta = (float) (currentTime - lastTime);
        lastTime = currentTime;
        return delta;
    }


    private void cleanup() {
        window.cleanup();
        // Add renderer or game cleanup if necessary
    }
}
