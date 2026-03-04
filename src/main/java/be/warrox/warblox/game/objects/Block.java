package be.warrox.warblox.game.objects;

import be.warrox.warblox.game.Position;
import be.warrox.warblox.renderEngine.GameObject;
import be.warrox.warblox.renderEngine.Primitives;
import be.warrox.warblox.renderEngine.Transform;
import org.joml.Vector3f;

public class Block extends GameObject {
    public Block(Position pos, BlockType type) {
        super(new Transform(pos.getVec(), new Vector3f(0, 0,0), new Vector3f(1, 1, 1)), type.getTexturePath(), Primitives.cubeVertices, Primitives.cubeIndices);
    }
}
