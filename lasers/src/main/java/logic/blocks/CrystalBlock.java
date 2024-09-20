package logic.blocks;

import logic.Laser;
import logic.Position;

public class CrystalBlock implements Block {

    @Override
    public Laser applyEffect(Laser laser, Position position, Side side) {
        if (side == Side.LEFT) {
            return new Laser(laser.getDirection(), new Position(position.getRow(), position.getColumn()+2));

        } else if (side == Side.LOWER) {
            return new Laser(laser.getDirection(), new Position(position.getRow() - 2, position.getColumn()));

        } else if (side == Side.RIGHT) {
            return new Laser(laser.getDirection(), new Position(position.getRow(), position.getColumn()-2));

        } else {
            return new Laser(laser.getDirection(), new Position(position.getRow() + 2, position.getColumn()));
        }
    }

    @Override
    public BlockType getType() {
        return BlockType.CRYSTAL;
    }
}