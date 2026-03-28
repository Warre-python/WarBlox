package be.warrox.game.world;

public enum BlockType {
    AIR((byte)0, null, null, null, null, null, null),

    // Minecraft style: Define all 6 sides
    GRASS((byte)1,
            "grass_block_top",    // Top
            "dirt",               // Bottom
            "grass_block_side",   // Front
            "grass_block_side",   // Back
            "grass_block_side",   // Left
            "grass_block_side"    // Right
    ),

    STONE((byte)2, "stone", "stone", "stone", "stone", "stone", "stone");

    private final byte id;
    private final String[] sideTextures = new String[6];

    BlockType(byte id, String top, String bottom, String front, String back, String left, String right) {
        this.id = id;
        this.sideTextures[0] = front;
        this.sideTextures[1] = back;
        this.sideTextures[2] = top;
        this.sideTextures[3] = bottom;
        this.sideTextures[4] = left;
        this.sideTextures[5] = right;
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
}