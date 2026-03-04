package be.warrox.warblox.game.objects;

public enum BlockType {
    DIRT("dirt.png"),
    STONE("stone.png"),
    GRASS("grass_block_side.png");

    private final String texturePath;

    // Constructor om de bestandsnaam op te slaan
    BlockType(String texturePath) {
        this.texturePath =  "assets/textures/block/"+texturePath;
    }

    public String getTexturePath() {
        return texturePath;
    }
}
