package logic.blocks;

import logic.Laser;
import logic.Position;
import static logic.Direction.NORTH_EAST;
import static logic.Direction.SOUTH_EAST;

public class CrystalBlock implements Block {
    @Override
    public Laser applyEffect(Laser laser, Position position) {
        if (laser.getDirection() == NORTH_EAST || laser.getDirection() == SOUTH_EAST) {
            Position newPosition = new Position(position.getRow(), position.getColumn()+2);
            return new Laser(laser.getDirection(), newPosition);

        } else {
            Position newPosition = new Position(position.getRow(), position.getColumn()-2);
            return new Laser(laser.getDirection(), newPosition);

        }

    }

    @Override
    public BlockType getType() {
        return BlockType.CRYSTAL;
    }
}