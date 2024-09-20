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
                Direction.SE, Direction.NW,
                Direction.SW, Direction.NE,
                Direction.NE, Direction.SW,
                Direction.NW, Direction.SE
        );
        Direction newDirection = directionMap.get(laser.getDirection());
        Map<Direction, Position> positionMap = Map.of(
                Direction.SE, new Position(1, 1),
                Direction.SW, new Position(1, -1),
                Direction.NE, new Position(-1, 1),
                Direction.NW, new Position(-1, -1)
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