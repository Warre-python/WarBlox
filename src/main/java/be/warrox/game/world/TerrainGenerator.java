package be.warrox.game.world;

import org.joml.SimplexNoise;

public class TerrainGenerator {
    // Laag 1: De grote vormen (Grote heuvels)
    private static final float BASE_SCALE = 0.005f;
    private static final float BASE_AMPLITUDE = 25.0f;

    // Laag 2: De details (Kleine hobbels en ruwheid)
    private static final float DETAIL_SCALE = 0.02f;
    private static final float DETAIL_AMPLITUDE = 5.0f;

    private static final int WATER_LEVEL = 8;
    private static final int MIN_HEIGHT = 4; // Zorg dat de wereld nooit onder dit niveau komt

    public static byte getBlockAt(int worldX, int worldY, int worldZ) {
        // 1. Bereken de basis hoogte (Lage frequentie, hoge amplitude)
        float baseNoise = SimplexNoise.noise(worldX * BASE_SCALE, worldZ * BASE_SCALE);
        float baseHeight = (baseNoise + 1.0f) * 0.5f * BASE_AMPLITUDE;

        // 2. Bereken de detail hoogte (Hoge frequentie, lage amplitude)
        float detailNoise = SimplexNoise.noise(worldX * DETAIL_SCALE, worldZ * DETAIL_SCALE);
        float detailHeight = (detailNoise + 1.0f) * 0.5f * DETAIL_AMPLITUDE;

        // 3. Combineer ze en voeg de minimum hoogte toe
        int finalHeight = (int) (baseHeight + detailHeight) + MIN_HEIGHT;

        // --- Blok Selectie Logica ---
        if (worldY > finalHeight) {
            // Alles boven de grond is Lucht of Water
            return (worldY <= WATER_LEVEL) ? BlockType.WATER.getId() : BlockType.AIR.getId();
        }

        if (worldY == finalHeight) {
            // De toplaag: Gras of Zand bij water
            if (worldY <= WATER_LEVEL + 1) {
                return BlockType.DIRT.getId(); // Of voeg een SAND type toe!
            }
            return BlockType.GRASS.getId();
        }

        if (worldY > finalHeight - 4) {
            // Onder het gras zit aarde
            return BlockType.DIRT.getId();
        }

        // De rest is steen
        return BlockType.STONE.getId();
    }
}