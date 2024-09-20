package logic;

import logic.blocks.Side;

import java.util.Map;

public class DisplacementApplier {
    private Map<Direction, Position> mapDisplacement;

    public DisplacementApplier(Map<Direction, Position> map) {
        mapDisplacement = map;
    }

    public Position getDisplacement(Direction direction) {
        return mapDisplacement.get(direction);
    }

    public Position applyDisplacement(Position from, Position delta) {
        return new Position(from.getRow() + delta.getRow(), from.getColumn() + delta.getColumn());
    }

    public Direction getCrystalDirection(Side side) {
        Map<Side, Direction> positionMap = Map.of(
                Side.LEFT, Direction.E,
                Side.LOWER, Direction.N,
                Side.RIGHT, Direction.W,
                Side.UPPER, Direction.S
        );
        return positionMap.get(side);
    }
}

