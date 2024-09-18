package logic.blocks;


import logic.Direction;
import logic.DisplacementApplier;
import logic.Laser;
import logic.Position;

import java.util.Map;

public class MirrorBlock implements Block {
    @Override
    public Laser applyEffect(Laser laser, Position position) {
        Map<Direction, Direction> directionMap = Map.of(
                Direction.SOUTH_EAST, Direction.NORTH_WEST,
                Direction.SOUTH_WEST, Direction.NORTH_EAST,
                Direction.NORTH_EAST, Direction.SOUTH_WEST,
                Direction.NORTH_WEST, Direction.SOUTH_EAST
        );
        Direction newDirection = directionMap.get(laser.getDirection());
        Map<Direction, Position> positionMap = Map.of(
                Direction.SOUTH_EAST, new Position(1, -1),
                Direction.SOUTH_WEST, new Position(1, 1),
                Direction.NORTH_EAST, new Position(-1, -1),
                Direction.NORTH_WEST, new Position(-1, 1)
        );
        DisplacementApplier applier = new DisplacementApplier(positionMap);
        Position delta = applier.getDisplacement(newDirection);
        Position newPosition = applier.applyDisplacement(position, delta);
        return new Laser(newDirection, newPosition);
    }

    @Override
    public BlockType getType() {
        return BlockType.MIRROR;
    }
}
