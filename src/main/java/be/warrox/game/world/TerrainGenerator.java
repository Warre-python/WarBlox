package be.warrox.game.world;

import org.joml.SimplexNoise;

public class TerrainGenerator {
    private static final float SCALE = 0.02f; // Hoe "steil" de heuvels zijn
    private static final int MAX_HEIGHT = 12; // Maximale hoogte van de heuvels
    private static final int WATER_LEVEL = 4;

    public static byte getBlockAt(int worldX, int worldY, int worldZ) {
        // Genereer een waarde tussen -1 en 1
        float noise = SimplexNoise.noise(worldX * SCALE, worldZ * SCALE);

        // Zet noise om naar een hoogte (0 tot MAX_HEIGHT)
        int height = (int)((noise + 1) * 0.5f * MAX_HEIGHT) + 2;

        if (worldY > height) {
            return worldY <= WATER_LEVEL ? BlockType.WATER.getId() : BlockType.AIR.getId();
        } else if (worldY == height) {
            return BlockType.GRASS.getId();
        } else if (worldY > height - 3) {
            return BlockType.DIRT.getId();
        } else {
            return BlockType.STONE.getId();
        }
    }
}