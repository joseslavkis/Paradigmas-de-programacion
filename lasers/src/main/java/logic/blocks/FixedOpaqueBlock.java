package logic.blocks;

import logic.Direction;
import logic.Laser;
import logic.Pair;
import logic.Position;

public class FixedOpaqueBlock implements Block {

    @Override
    public Pair applyEffect(Laser laser, Position position, Side side) {
        return new Pair(position, Direction.STATIC);
    }

    @Override
    public BlockType getType() {
        return BlockType.FIXED_OPAQUE;
    }
}
