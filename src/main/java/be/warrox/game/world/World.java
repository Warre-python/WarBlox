package be.warrox.game.world;

import org.joml.Vector3f;
import java.util.HashMap;
import java.util.Map;

public class World {
    private final Map<String, Chunk> chunks;
    // Instelling voor hoe groot de wereld moet zijn (bijv. 4x4 chunks)
    private final int worldSize = 1;

    public World() {
        this.chunks = new HashMap<>();
        generateWorld();
    }

    private void generateWorld() {
        // Loop door de X en Z as om een plat vlak van chunks te maken
        for (int x = 0; x < worldSize; x++) {
            for (int z = 0; z < worldSize; z++) {
                // We houden Y op 0 voor een platte wereld,
                // maar je kunt hier ook een lus voor Y toevoegen voor hoogte
                addChunk(x, 0, z);
            }
        }
    }

    public void addChunk(int x, int y, int z) {
        String key = x + "," + y + "," + z;
        if (!chunks.containsKey(key)) {
            // Belangrijk: we geven de grid-coördinaten door aan de Chunk
            chunks.put(key, new Chunk(new Vector3f(x * Chunk.SIZE, y * Chunk.SIZE, z * Chunk.SIZE)));
        }
    }

    public Map<String, Chunk> getChunks() {
        return chunks;
    }
}