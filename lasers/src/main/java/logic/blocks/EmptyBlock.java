package logic.blocks;
import logic.Direction;
import logic.DisplacementApplier;
import logic.Laser;
import logic.Position;

import java.util.Map;

public class EmptyBlock implements Block{
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
                Direction.SE, new Position(1, -1),
                Direction.SW, new Position(1, 1),
                Direction.NE, new Position(-1, -1),
                Direction.NW, new Position(-1, 1)
        );
        DisplacementApplier applier = new DisplacementApplier(positionMap);
        Position displacement = applier.getDisplacement(newDirection);
        if (displacement == null) return laser;
        return new Laser(laser.getDirection(), applier.applyDisplacement(position, displacement));
    }

    @Override
    public BlockType getType() {
        return BlockType.EMPTY_BLOCK;
    }

 
}