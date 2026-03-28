package be.warrox.game.world;

import be.warrox.engine.gfx.Mesh;
import be.warrox.engine.gfx.Vertex;
import be.warrox.engine.scene.Transform;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.*;

public class Chunk {
    public static final int SIZE = 100;
    private final byte[][][] blocks;
    private final Vector3f worldPosition;

    // We bewaren een map van Texture-naam -> Mesh
    private final Map<String, Mesh> subMeshes;

    public Chunk(Vector3f worldPosition) {
        this.worldPosition = worldPosition;
        this.blocks = new byte[SIZE][SIZE][SIZE];
        this.subMeshes = new HashMap<>();

        generateTerrain();
        generateMesh();
    }

    private void generateTerrain() {
        for (int x = 0; x < SIZE; x++) {
            for (int z = 0; z < SIZE; z++) {
                // Maak een simpele ondergrond van steen en een laagje gras
                for (int y = 0; y < 5; y++) {
                    blocks[x][y][z] = BlockType.STONE.getId();
                }
                blocks[x][5][z] = BlockType.GRASS.getId();

            }
        }

        blocks[8][6][8] = BlockType.GRASS.getId();
    }

    public void generateMesh() {
        // Cleanup oude meshes indien nodig
        subMeshes.clear();

        // Tijdelijke opslag voor vertices en indices per texture
        Map<String, List<Vertex>> vertexBatches = new HashMap<>();
        Map<String, List<Integer>> indexBatches = new HashMap<>();

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
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
                            } else {
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
        int nx = x, ny = y, nz = z;
        switch (side) {
            case 0 -> nz++; case 1 -> nz--;
            case 2 -> ny++; case 3 -> ny--;
            case 4 -> nx--; case 5 -> nx++;
        }

        // Als het blokje BINNEN deze chunk valt:
        if (nx >= 0 && nx < SIZE && ny >= 0 && ny < SIZE && nz >= 0 && nz < SIZE) {
            return BlockType.fromId(blocks[nx][ny][nz]) == BlockType.AIR;
        }

        // Als het blokje BUITEN deze chunk valt, moeten we de World checken:
        // Dit is een geavanceerde stap. Voor nu kun je het op 'true' laten,
        // maar dan zie je dus muren tussen de chunks.
        return true;
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
}