package be.warrox.game.world;

import org.joml.Vector3f;
import java.util.HashMap;
import java.util.Map;

public class World {
    private final Map<String, Chunk> chunks;
    private final int renderDistance = 7; // Aantal chunks in elke richting

    public World() {
        this.chunks = new HashMap<>();
        // De constructor is nu leeg, alles gebeurt in update()
    }

    public void update(Player player) {
        int playerChunkX = Math.floorDiv((int)player.getPos().x, Chunk.SIZE);
        int playerChunkZ = Math.floorDiv((int)player.getPos().z, Chunk.SIZE);

        // --- STAP 1: CHUNKS LADEN ---
        for (int x = -renderDistance; x <= renderDistance; x++) {
            for (int z = -renderDistance; z <= renderDistance; z++) {
                int targetX = playerChunkX + x;
                int targetZ = playerChunkZ + z;

                if (getChunk(targetX, 0, targetZ) == null) {
                    addChunk(targetX, 0, targetZ);
                    Chunk newChunk = getChunk(targetX, 0, targetZ);
                    newChunk.generateMesh();
                    updateNeighbors(targetX, 0, targetZ);
                }
            }
        }

        // --- STAP 2: CHUNKS VERWIJDEREN (UNLOAD) ---
        // We gebruiken een Iterator om veilig items uit een Map te verwijderen tijdens het loopen
        var iterator = chunks.entrySet().iterator();
        int unloadDistance = renderDistance + 2; // Buffer van 2 chunks om herhaaldelijk laden/lossen te voorkomen

        while (iterator.hasNext()) {
            var entry = iterator.next();
            Chunk chunk = entry.getValue();

            // Bereken afstand in chunks (we negeren Y voor nu)
            int cx = Math.floorDiv((int)chunk.getWorldPosition().x, Chunk.SIZE);
            int cz = Math.floorDiv((int)chunk.getWorldPosition().z, Chunk.SIZE);

            int distLineX = Math.abs(cx - playerChunkX);
            int distLineZ = Math.abs(cz - playerChunkZ);

            if (distLineX > unloadDistance || distLineZ > unloadDistance) {
                // BELANGRIJK: Ruim eerst de Mesh op van de GPU!
                chunk.cleanup();
                iterator.remove(); // Verwijder uit de HashMap
            }
        }
    }

    private void updateNeighbors(int x, int y, int z) {
        int[][] neighbors = {{1,0,0}, {-1,0,0}, {0,0,1}, {0,0,-1}};
        for (int[] offset : neighbors) {
            Chunk neighbor = getChunk(x + offset[0], y + offset[1], z + offset[2]);
            if (neighbor != null) {
                neighbor.generateMesh(); // Herbereken de mesh nu er een nieuwe buur is
            }
        }
    }


    public void addChunk(int x, int y, int z) {
        String key = x + "," + y + "," + z;
        if (!chunks.containsKey(key)) {
            // Belangrijk: we geven de grid-coördinaten door aan de Chunk
            chunks.put(key, new Chunk(new Vector3f(x * Chunk.SIZE, y * Chunk.SIZE, z * Chunk.SIZE), this));
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
        // Bereken in welke chunk dit punt valt
        int cx = Math.floorDiv(x, Chunk.SIZE);
        int cy = Math.floorDiv(y, Chunk.SIZE);
        int cz = Math.floorDiv(z, Chunk.SIZE);

        String key = cx + "," + cy + "," + cz;
        Chunk chunk = chunks.get(key);

        if (chunk != null) {
            // Bereken de lokale positie binnen die chunk
            int lx = x - (cx * Chunk.SIZE);
            int ly = y - (cy * Chunk.SIZE);
            int lz = z - (cz * Chunk.SIZE);
            return chunk.getBlock(lx, ly, lz);
        }

        return BlockType.AIR.getId(); // Als de chunk niet bestaat, is het lucht
    }
}