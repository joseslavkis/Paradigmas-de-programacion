package logic.blocks;

import logic.Laser;
import logic.Position;

public class FixedOpaqueBlock implements Block {

    @Override
    public Laser applyEffect(Laser laser, Position position, Side side) {
        return null;
    }

    @Override
    public BlockType getType() {
        return BlockType.FIXED_OPAQUE;
    }
}
