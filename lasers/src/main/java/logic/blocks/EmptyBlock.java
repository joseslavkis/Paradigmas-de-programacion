package logic.blocks;
import logic.Direction;
import logic.DisplacementApplier;
import logic.Laser;
import logic.Position;

import java.util.Map;

public class EmptyBlock implements Block {
    @Override
    public Laser applyEffect(Laser laser, Position position, Side side) {
        return laser;
    }

    @Override
    public BlockType getType() {
        return BlockType.EMPTY_BLOCK;
    }

 
}