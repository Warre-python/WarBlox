package be.warrox.game.world;

import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.MeshData;
import be.warrox.engine.gfx.Vertex;
import be.warrox.engine.scene.Transform;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Chunk {
    public static final int HEIGHT = 32;
    public static final int SIZE = 16;
    private final byte[][][] blocks;
    private final Vector3f worldPosition;

    // We bewaren een map van Texture-naam -> Mesh
    private final Map<String, Mesh> subMeshes = new ConcurrentHashMap<>();

    private final World world;
    private volatile boolean terrainGenerated = false;
    private volatile boolean meshGenerated = false;

    public Chunk(Vector3f worldPosition, World world) {
        this.worldPosition = worldPosition;
        this.blocks = new byte[SIZE][HEIGHT][SIZE];
        this.world = world;

    }

    public void generateTerrain() {
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                for (int y = 0; y < HEIGHT; y++) {
                    // Bereken de absolute positie in de wereld
                    int worldX = (int)worldPosition.x + x;
                    int worldY = (int)worldPosition.y + y;
                    int worldZ = (int)worldPosition.z + z;

                    // Vraag de generator welk blok hier moet staan
                    blocks[x][y][z] = TerrainGenerator.getBlockAt(worldX, worldY, worldZ);
                }
            }
        }
        terrainGenerated = true;
    }

    public void setBlock(int x, int y, int z, byte blockId) {
        if (x >= 0 && x < SIZE && y >= 0 && y < HEIGHT && z >= 0 && z < SIZE) {
            blocks[x][y][z] = blockId;
        }
    }

    public MeshData generateMeshData() {
        Map<String, List<Vertex>> vertexBatches = new HashMap<>();
        Map<String, List<Integer>> indexBatches = new HashMap<>();

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int z = 0; z < SIZE; z++) {
                    byte blockId = blocks[x][y][z];
                    if (blockId == BlockType.AIR.getId()) continue;

                    BlockType type = BlockType.fromId(blockId);

                    for (int side = 0; side < 6; side++) {
                        if (isSideVisible(x, y, z, side)) {
                            String texName = type.getTextureForSide(side);

                            vertexBatches.putIfAbsent(texName, new ArrayList<>());
                            indexBatches.putIfAbsent(texName, new ArrayList<>());

                            Vector4f color = new Vector4f(1,1,1,1);

                            if (type == BlockType.GRASS && side == 2) {
                                color.set(0.48f, 0.95f, 0.35f, 1);
                            } else if (type == BlockType.WATER) {
                                color.set(0,0,1,1);
                            }

                            addFace(x, y, z, side,
                                    vertexBatches.get(texName),
                                    indexBatches.get(texName),
                                    color);
                        }
                    }
                }
            }
        }

        MeshData data = new MeshData();

        for (String tex : vertexBatches.keySet()) {
            data.vertices.put(tex, vertexBatches.get(tex).toArray(new Vertex[0]));
            data.indices.put(tex, indexBatches.get(tex).stream().mapToInt(i -> i).toArray());
        }

        return data;
    }

    public void uploadMesh(MeshData data) {
        cleanup(); // remove old meshes safely

        for (String tex : data.vertices.keySet()) {
            Mesh mesh = new Mesh(
                    data.vertices.get(tex),
                    data.indices.get(tex),
                    tex,
                    new Transform(worldPosition, new Vector3f(), new Vector3f(1))
            );
            subMeshes.put(tex, mesh);
        }
        meshGenerated = true;
    }


    private boolean isSideVisible(int x, int y, int z, int side) {
        // Wereldcoördinaten van het buurblokje berekenen
        int wx = (int)worldPosition.x + x;
        int wy = (int)worldPosition.y + y;
        int wz = (int)worldPosition.z + z;

        switch (side) {
            case 0 -> wz++; // Front
            case 1 -> wz--; // Back
            case 2 -> wy++; // Top
            case 3 -> wy--; // Bottom
            case 4 -> wx--; // Left
            case 5 -> wx++; // Right
        }

        // Vraag aan de wereld welk blokje op die plek staat
        byte neighborId = world.getBlock(wx, wy, wz);
        BlockType neighborType = BlockType.fromId(neighborId);

        // Teken de face alleen als de buur NIET solide is (dus lucht of water)
        return !neighborType.isSolid();
    }

    private void addFace(int x, int y, int z, int side, List<Vertex> vertices, List<Integer> indices, Vector4f color) {
        int startIdx = vertices.size();
        float s = 0.5f;


        // Let op: we gebruiken x, y, z als LOKALE coördinaten (0-15)
        // De Transform in de Mesh regelt de wereldpositie.
        Vector3f normal = new Vector3f();
        switch (side) {
            case 0 -> { // FRONT (Z+) - Deze werkt nu!
                normal.set(0, 0, 1);
                vertices.add(new Vertex(new Vector3f(x-s, y+s, z+s), normal, new Vector2f(0, 1), color));
                vertices.add(new Vertex(new Vector3f(x+s, y+s, z+s), normal, new Vector2f(1, 1), color));
                vertices.add(new Vertex(new Vector3f(x+s, y-s, z+s), normal, new Vector2f(1, 0), color));
                vertices.add(new Vertex(new Vector3f(x-s, y-s, z+s), normal, new Vector2f(0, 0), color));
            }
            case 1 -> { // BACK (Z-)
                normal.set(0, 0, -1);
                vertices.add(new Vertex(new Vector3f(x+s, y+s, z-s), normal, new Vector2f(0, 1), color));
                vertices.add(new Vertex(new Vector3f(x-s, y+s, z-s), normal, new Vector2f(1, 1), color));
                vertices.add(new Vertex(new Vector3f(x-s, y-s, z-s), normal, new Vector2f(1, 0), color));
                vertices.add(new Vertex(new Vector3f(x+s, y-s, z-s), normal, new Vector2f(0, 0), color));
            }
            case 2 -> { // TOP (Y+)
                normal.set(0, 1, 0);
                vertices.add(new Vertex(new Vector3f(x-s, y+s, z-s), normal, new Vector2f(0, 1), color));
                vertices.add(new Vertex(new Vector3f(x+s, y+s, z-s), normal, new Vector2f(1, 1), color));
                vertices.add(new Vertex(new Vector3f(x+s, y+s, z+s), normal, new Vector2f(1, 0), color));
                vertices.add(new Vertex(new Vector3f(x-s, y+s, z+s), normal, new Vector2f(0, 0), color));
            }
            case 3 -> { // BOTTOM (Y-)
                normal.set(0, -1, 0);
                vertices.add(new Vertex(new Vector3f(x-s, y-s, z+s), normal, new Vector2f(0, 1), color));
                vertices.add(new Vertex(new Vector3f(x+s, y-s, z+s), normal, new Vector2f(1, 1), color));
                vertices.add(new Vertex(new Vector3f(x+s, y-s, z-s), normal, new Vector2f(1, 0), color));
                vertices.add(new Vertex(new Vector3f(x-s, y-s, z-s), normal, new Vector2f(0, 0), color));
            }
            case 4 -> { // LEFT (X-)
                normal.set(-1, 0, 0);
                vertices.add(new Vertex(new Vector3f(x-s, y+s, z-s), normal, new Vector2f(0, 1), color));
                vertices.add(new Vertex(new Vector3f(x-s, y+s, z+s), normal, new Vector2f(1, 1), color));
                vertices.add(new Vertex(new Vector3f(x-s, y-s, z+s), normal, new Vector2f(1, 0), color));
                vertices.add(new Vertex(new Vector3f(x-s, y-s, z-s), normal, new Vector2f(0, 0), color));
            }
            case 5 -> { // RIGHT (X+)
                normal.set(1, 0, 0);
                vertices.add(new Vertex(new Vector3f(x+s, y+s, z+s), normal, new Vector2f(0, 1), color));
                vertices.add(new Vertex(new Vector3f(x+s, y+s, z-s), normal, new Vector2f(1, 1), color));
                vertices.add(new Vertex(new Vector3f(x+s, y-s, z-s), normal, new Vector2f(1, 0), color));
                vertices.add(new Vertex(new Vector3f(x+s, y-s, z+s), normal, new Vector2f(0, 0), color));
            }
        }

        indices.add(startIdx);     indices.add(startIdx + 1); indices.add(startIdx + 2);
        indices.add(startIdx + 2); indices.add(startIdx + 3); indices.add(startIdx);
    }

    public Map<String, Mesh> getSubMeshes() {
        return subMeshes;
    }

    public byte getBlock(int lx, int ly, int lz) {
        if (!terrainGenerated) return BlockType.AIR.getId();
        if (lx < 0 || lx >= SIZE || ly < 0 || ly >= HEIGHT || lz < 0 || lz >= SIZE) {
            return BlockType.AIR.getId();
        }
        return blocks[lx][ly][lz];

    }
    public void cleanup() {
        for (Mesh mesh : subMeshes.values()) {
            mesh.cleanup();
        }
        subMeshes.clear();
        meshGenerated = false;
    }

    public Vector3f getWorldPosition() {
        return worldPosition;
    }


    public boolean isMeshGenerated() {
        return meshGenerated;
    }

    public boolean isTerrainGenerated() {
        return terrainGenerated;
    }

    public void setMeshGenerated(boolean meshGenerated) {
        this.meshGenerated = meshGenerated;
    }
}