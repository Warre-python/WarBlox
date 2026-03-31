package be.warrox.game.world;

import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.Map;

public class World {
    private final Map<String, Chunk> chunks = new HashMap<>();;

    private final int renderDistance = 5; // Aantal chunks in elke richting

    public void update(Player player) {
        int playerChunkX = Math.floorDiv((int)player.getPos().x, Chunk.SIZE);
        int playerChunkZ = Math.floorDiv((int)player.getPos().z, Chunk.SIZE);
        // --- STAP 1: CHUNKS LADEN ---
        for (int x = -renderDistance; x <= renderDistance; x++) {
            for (int z = -renderDistance; z <= renderDistance; z++) {
                int targetX = playerChunkX + x;
                int targetZ = playerChunkZ + z;
                // Voor nu laden we alleen de 'grond' chunks op y=0
                if (getChunk(targetX, 0, targetZ) == null) {
                    addChunk(targetX, 0, targetZ);
                    Chunk newChunk = getChunk(targetX, 0, targetZ);
                    newChunk.generateTerrain();
                    newChunk.generateMesh();
                    updateNeighbors(targetX, 0, targetZ);
                }
            }
        }

        // --- STAP 2: CHUNKS VERWIJDEREN (UNLOAD) ---
        var iterator = chunks.entrySet().iterator();
        int unloadDistance = renderDistance + 2;

        while (iterator.hasNext()) {
            var entry = iterator.next();
            Chunk chunk = entry.getValue();
            int cx = Math.floorDiv((int)chunk.getWorldPosition().x, Chunk.SIZE);
            int cz = Math.floorDiv((int)chunk.getWorldPosition().z, Chunk.SIZE);
            int distLineX = Math.abs(cx - playerChunkX);
            int distLineZ = Math.abs(cz - playerChunkZ);
            if (distLineX > unloadDistance || distLineZ > unloadDistance) {
                chunk.cleanup();
                iterator.remove();
            }
        }
    }



    private void updateNeighbors(int x, int y, int z) {
        int[][] neighbors = {{1,0,0}, {-1,0,0}, {0,0,1}, {0,0,-1}, {0,1,0}, {0,-1,0}};
        for (int[] offset : neighbors) {
            Chunk neighbor = getChunk(x + offset[0], y + offset[1], z + offset[2]);
            if (neighbor != null) {
                neighbor.generateMesh();
            }
        }
    }


    public void addChunk(int x, int y, int z) {
        String key = x + "," + y + "," + z;
        if (!chunks.containsKey(key)) {
            chunks.put(key, new Chunk(new Vector3f(x * Chunk.SIZE, y * Chunk.HEIGHT, z * Chunk.SIZE), this));
        }
    }


    public Map<String, Chunk> getChunks() {
        return chunks;
    }

    public Chunk getChunk(int x, int y, int z) {
        String key = x + "," + y + "," + z;
        return chunks.get(key);
    }



    public byte getBlock(int x, int y, int z) {
        int cx = Math.floorDiv(x, Chunk.SIZE);
        int cy = Math.floorDiv(y, Chunk.HEIGHT);
        int cz = Math.floorDiv(z, Chunk.SIZE);

        Chunk chunk = getChunk(cx, cy, cz);

        if (chunk != null) {
            int lx = x - (cx * Chunk.SIZE);
            int ly = y - (cy * Chunk.HEIGHT);
            int lz = z - (cz * Chunk.SIZE);
            return chunk.getBlock(lx, ly, lz);
        }

        return BlockType.AIR.getId();
    }

    public void addBlock(int x, int y, int z, BlockType type) {
        int cx = Math.floorDiv(x, Chunk.SIZE);
        int cy = Math.floorDiv(y, Chunk.HEIGHT);
        int cz = Math.floorDiv(z, Chunk.SIZE);

        Chunk chunk = getChunk(cx, cy, cz);
        if (chunk == null) {
            addChunk(cx, cy, cz);
            chunk = getChunk(cx, cy, cz);
            // Zorg dat het terrein eerst gegenereerd wordt, anders is de rest van de chunk leeg!
            chunk.generateTerrain();
        }

        int lx = x - (cx * Chunk.SIZE);
        int ly = y - (cy * Chunk.HEIGHT);
        int lz = z - (cz * Chunk.SIZE);
        chunk.setBlock(lx, ly, lz, type.getId());
        
        // Update de mesh van de chunk zelf
        chunk.generateMesh();

        // Update ook buur-meshes als we aan de rand van een chunk zitten
        if (lx == 0) updateNeighborMesh(cx - 1, cy, cz);
        if (lx == Chunk.SIZE - 1) updateNeighborMesh(cx + 1, cy, cz);
        if (ly == 0) updateNeighborMesh(cx, cy - 1, cz);
        if (ly == Chunk.HEIGHT - 1) updateNeighborMesh(cx, cy + 1, cz);
        if (lz == 0) updateNeighborMesh(cx, cy, cz - 1);
        if (lz == Chunk.SIZE - 1) updateNeighborMesh(cx, cy, cz + 1);
    }

    private void updateNeighborMesh(int cx, int cy, int cz) {
        Chunk neighbor = getChunk(cx, cy, cz);
        if (neighbor != null) {
            neighbor.generateMesh();
        }
    }
}