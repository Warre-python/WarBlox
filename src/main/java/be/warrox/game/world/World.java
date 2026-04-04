package be.warrox.game.world;

import be.warrox.engine.gfx.MeshData;
import org.joml.Vector3f;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class World {
    private final Map<String, Chunk> chunks = new ConcurrentHashMap<>();
    private final Queue<Runnable> mainThreadQueue = new ConcurrentLinkedQueue<>();
    private final int renderDistance = 5;



    private final ExecutorService chunkExecutor = Executors.newFixedThreadPool(
            Math.max(1, Runtime.getRuntime().availableProcessors() - 1)
    );

    public void processMainThreadQueue() {
        Runnable task;
        while ((task = mainThreadQueue.poll()) != null) {
            task.run();
        }
    }

    public void update(Player player) {
        int playerChunkX = Math.floorDiv((int) player.getPos().x, Chunk.SIZE);
        int playerChunkZ = Math.floorDiv((int) player.getPos().z, Chunk.SIZE);

        // 1. CHUNKS GENEREREN (Terrain)
        for (int x = -renderDistance; x <= renderDistance; x++) {
            for (int z = -renderDistance; z <= renderDistance; z++) {
                int targetX = playerChunkX + x;
                int targetZ = playerChunkZ + z;

                if (getChunk(targetX, 0, targetZ) == null) {
                    addChunk(targetX, 0, targetZ);
                    Chunk newChunk = getChunk(targetX, 0, targetZ);
                    chunkExecutor.submit(newChunk::generateTerrain);
                }
            }
        }

        // 2. CHUNKS MESHEN (Pas als buren ook terrain hebben)
        for (int x = -renderDistance; x <= renderDistance; x++) {
            for (int z = -renderDistance; z <= renderDistance; z++) {
                int targetX = playerChunkX + x;
                int targetZ = playerChunkZ + z;
                Chunk chunk = getChunk(targetX, 0, targetZ);

                if (chunk != null && chunk.isTerrainGenerated() && !chunk.isMeshGenerated()) {
                    if (hasAllNeighborsTerrain(targetX, 0, targetZ)) {
                        chunkExecutor.submit(() -> {
                            MeshData data = chunk.generateMeshData();
                            mainThreadQueue.add(() -> chunk.uploadMesh(data));
                        });
                    }
                }
            }
        }

        // 3. CHUNKS VERWIJDEREN
        var iterator = chunks.entrySet().iterator();
        int unloadDistance = renderDistance + 2;

        while (iterator.hasNext()) {
            var entry = iterator.next();
            Chunk chunk = entry.getValue();
            int cx = Math.floorDiv((int) chunk.getWorldPosition().x, Chunk.SIZE);
            int cz = Math.floorDiv((int) chunk.getWorldPosition().z, Chunk.SIZE);
            int distLineX = Math.abs(cx - playerChunkX);
            int distLineZ = Math.abs(cz - playerChunkZ);
            if (distLineX > unloadDistance || distLineZ > unloadDistance) {
                chunk.cleanup();
                iterator.remove();
            }
        }
    }

    private boolean hasAllNeighborsTerrain(int x, int y, int z) {
        int[][] neighbors = {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}, {0, 1, 0}, {0, -1, 0}};
        for (int[] offset : neighbors) {
            Chunk neighbor = getChunk(x + offset[0], y + offset[1], z + offset[2]);
            if (neighbor == null || !neighbor.isTerrainGenerated()) {
                if (offset[1] != 0) continue; 
                return false;
            }
        }
        return true;
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
        return chunks.get(x + "," + y + "," + z);
    }

    public byte getBlock(int x, int y, int z) {
        int cx = Math.floorDiv(x, Chunk.SIZE);
        int cy = Math.floorDiv(y, Chunk.HEIGHT);
        int cz = Math.floorDiv(z, Chunk.SIZE);

        Chunk chunk = getChunk(cx, cy, cz);
        if (chunk != null) {
            return chunk.getBlock(x - (cx * Chunk.SIZE), y - (cy * Chunk.HEIGHT), z - (cz * Chunk.SIZE));
        }
        return BlockType.AIR.getId();
    }

    public void addBlock(int x, int y, int z, BlockType type) {
        // 1. Boundary Check: Prevent ArrayIndexOutOfBounds if clicking outside world height
        if (y < 0 || y >= Chunk.HEIGHT) return;

        int cx = Math.floorDiv(x, Chunk.SIZE);
        int cy = Math.floorDiv(y, Chunk.HEIGHT); // Usually 0 in your setup
        int cz = Math.floorDiv(z, Chunk.SIZE);

        Chunk chunk = getChunk(cx, cy, cz);

        // 2. Ensure the chunk exists before modifying
        if (chunk == null) {
            addChunk(cx, cy, cz);
            chunk = getChunk(cx, cy, cz);
            // If it's a brand new chunk, we generate terrain immediately
            chunk.generateTerrain();
        }

        int lx = x - (cx * Chunk.SIZE);
        int ly = y - (cy * Chunk.HEIGHT);
        int lz = z - (cz * Chunk.SIZE);

        // 3. Set the block
        chunk.setBlock(lx, ly, lz, type.getId());

        // 4. Update meshes (Self + Neighbors if on edge)
        updateChunkMesh(chunk);

        // Neighbor updates: Only update if the neighbor actually exists
        if (lx == 0) updateChunkMesh(getChunk(cx - 1, cy, cz));
        if (lx == Chunk.SIZE - 1) updateChunkMesh(getChunk(cx + 1, cy, cz));

        // Vertical neighbors (if you decide to support multiple chunk layers later)
        if (ly == 0) updateChunkMesh(getChunk(cx, cy - 1, cz));
        if (ly == Chunk.HEIGHT - 1) updateChunkMesh(getChunk(cx, cy + 1, cz));

        if (lz == 0) updateChunkMesh(getChunk(cx, cy, cz - 1));
        if (lz == Chunk.SIZE - 1) updateChunkMesh(getChunk(cx, cy, cz + 1));
    }

    private void updateChunkMesh(Chunk chunk) {
        if (chunk != null) {
            chunkExecutor.submit(() -> {
                MeshData data = chunk.generateMeshData();
                mainThreadQueue.add(() -> chunk.uploadMesh(data));
            });
        }
    }

    public void cleanup() {
        chunkExecutor.shutdown();
        try {
            if (!chunkExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
                chunkExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            chunkExecutor.shutdownNow();
        }
        
        for (Chunk chunk : chunks.values()) {
            chunk.cleanup();
        }
        chunks.clear();
        mainThreadQueue.clear();
    }
}