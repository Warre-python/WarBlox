package be.warrox;

import be.warrox.engine.core.Engine;
import be.warrox.game.IGame;
import be.warrox.game.MyGame;

public class Main {
    public static void main(String[] args) {
        // 1. Create the game logic
        IGame myGame = new MyGame();

        // 2. Create the engine and pass the game into it
        Engine engine = new Engine("Voxel Engine v1.0", 1280, 720, myGame, true);

        // 3. Start the thread
        engine.run();
    }
}