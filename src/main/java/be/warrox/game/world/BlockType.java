package be.warrox.game.world;

public enum BlockType {
    AIR((byte)0, null, null, null, null, null, null, false),

    // Minecraft style: Define all 6 sides
    GRASS((byte)1,
            "block/grass_block_top",    // Top
            "block/dirt",               // Bottom
            "block/grass_block_side",   // Front
            "block/grass_block_side",   // Back
            "block/grass_block_side",   // Left
            "block/grass_block_side"    // Right
            , true
    ),

    STONE((byte)2, "block/stone", "block/stone", "block/stone", "block/stone", "block/stone", "block/stone", true),

    WATER((byte)3, "block/grass_block_top", "block/grass_block_top", "block/grass_block_top", "block/grass_block_top", "block/grass_block_top", "block/grass_block_top", true),
    DIRT((byte)4, "block/dirt", "block/dirt", "block/dirt", "block/dirt", "block/dirt", "block/dirt", true);


    private final byte id;
    private final String[] sideTextures = new String[6];
    private final boolean solid;


    BlockType(byte id, String top, String bottom, String front, String back, String left, String right, boolean solid) {
        this.id = id;
        this.sideTextures[0] = front;
        this.sideTextures[1] = back;
        this.sideTextures[2] = top;
        this.sideTextures[3] = bottom;
        this.sideTextures[4] = left;
        this.sideTextures[5] = right;
        this.solid = solid;
    }

    public String getTextureForSide(int side) {
        return sideTextures[side];
    }

    public static BlockType fromId(byte id) {
        for (BlockType type : values()) if (type.id == id) return type;
        return AIR;
    }

    public byte getId() {
        return id;
    }

    public boolean isSolid() {
        return solid;
    }
}