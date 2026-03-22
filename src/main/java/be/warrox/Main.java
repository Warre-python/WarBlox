package be.warrox;

public class Main {
    public static void main(String[] args) {
        // 1. Create the game logic
        IGame myGame = new MyGame();

        // 2. Create the engine and pass the game into it
        Engine engine = new Engine("Voxel Engine v1.0", 1280, 720, myGame);

        // 3. Start the thread
        engine.run();
    }
}