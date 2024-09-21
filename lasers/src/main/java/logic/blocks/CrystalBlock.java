package logic.blocks;

import logic.Laser;
import logic.Pair;
import logic.Position;

public class CrystalBlock implements Block {

    @Override
    public Pair applyEffect(Laser laser, Position position, Side side) {
        if (side == Side.LEFT) {
            return new Pair(new Position(position.getRow(), position.getColumn()+2), laser.getDirection());

        } else if (side == Side.LOWER) {
            return new Pair(new Position(position.getRow() - 2, position.getColumn()), laser.getDirection());

        } else if (side == Side.RIGHT) {
            return new Pair(new Position(position.getRow(), position.getColumn()-2), laser.getDirection());

        } else {
            return new Pair(new Position(position.getRow() + 2, position.getColumn()), laser.getDirection());
        }
    }

    @Override
    public BlockType getType() {
        return BlockType.CRYSTAL;
    }
}