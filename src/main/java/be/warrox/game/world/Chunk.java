package be.warrox.game.world;

import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.Vertex;
import be.warrox.engine.scene.Transform;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.*;

public class Chunk {
    public static final int HEIGHT = 32;
    public static final int SIZE = 16;
    private final byte[][][] blocks;
    private final Vector3f worldPosition;

    // We bewaren een map van Texture-naam -> Mesh
    private final Map<String, Mesh> subMeshes;

    private final World world;

    public Chunk(Vector3f worldPosition, World world) {
        this.worldPosition = worldPosition;
        this.blocks = new byte[SIZE][HEIGHT][SIZE];
        this.subMeshes = new HashMap<>();
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
    }

    public void generateMesh() {
        // Cleanup oude meshes indien nodig
        subMeshes.clear();

        // Tijdelijke opslag voor vertices en indices per texture
        Map<String, List<Vertex>> vertexBatches = new HashMap<>();
        Map<String, List<Integer>> indexBatches = new HashMap<>();

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int z = 0; z < SIZE; z++) {
                    byte blockId = blocks[x][y][z];
                    if (blockId == BlockType.AIR.getId()) continue;

                    BlockType type = BlockType.fromId(blockId);

                    // Check alle 6 kanten
                    for (int side = 0; side < 6; side++) {
                        if (isSideVisible(x, y, z, side)) {
                            String texName = type.getTextureForSide(side);

                            // Zorg dat er een lijst bestaat voor deze specifieke texture
                            vertexBatches.putIfAbsent(texName, new ArrayList<>());
                            indexBatches.putIfAbsent(texName, new ArrayList<>());

                            Vector4f faceColor = new Vector4f(1, 1, 1, 1); // Standaard wit (geen aanpassing)

                            // Check of het GRASS is en of het de BOVENKANT (side 2) is
                            if (type == BlockType.GRASS && side == 2) {
                                faceColor.set(0.48f, 0.95f, 0.35f, 1.0f); // Minecraft Gras-groen
                            } else if (type == BlockType.WATER) {
                                faceColor.set(0, 0, 1, 1);
                            }else {
                                faceColor.set(1, 1, 1, 1);
                            }

                            addFace(x, y, z, side, vertexBatches.get(texName), indexBatches.get(texName), faceColor);
                        }
                    }
                }
            }
        }

        // Maak van elke batch een echte Mesh
        for (String texName : vertexBatches.keySet()) {
            Vertex[] verts = vertexBatches.get(texName).toArray(new Vertex[0]);
            int[] inds = indexBatches.get(texName).stream().mapToInt(i -> i).toArray();

            // We gebruiken de worldPosition in de Transform, dus x,y,z in addFace blijven lokaal (0-16)
            Mesh mesh = new Mesh(verts, inds, texName, new Transform(worldPosition, new Vector3f(), new Vector3f(1)));
            subMeshes.put(texName, mesh);
        }
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
        return blocks[lx][ly][lz];

    }
    public void cleanup() {
        for (Mesh mesh : subMeshes.values()) {
            // Zorg dat je Mesh klasse een methode heeft om glDeleteBuffers aan te roepen
            mesh.cleanup();
        }
        subMeshes.clear();
    }

    public Vector3f getWorldPosition() {
        return worldPosition;
    }


    public boolean isMeshGenerated() {
        return !subMeshes.isEmpty();
    }
}