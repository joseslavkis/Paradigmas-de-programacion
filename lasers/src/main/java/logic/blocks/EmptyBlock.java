package logic.blocks;
import logic.*;

public class EmptyBlock implements Block {
    @Override
    public Pair applyEffect(Laser laser, Position position, Side side) {
        return new Pair(position, laser.getDirection());
    }

    @Override
    public BlockType getType() {
        return BlockType.EMPTY;
    }

 
}