package logic;

import logic.blocks.SideType;
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

    public Direction getCrystalDirection(SideType sideType) {
        Map<SideType, Direction> positionMap = Map.of(
                SideType.LEFT, Direction.E,
                SideType.LOWER, Direction.N,
                SideType.RIGHT, Direction.W,
                SideType.UPPER, Direction.S
        );
        return positionMap.get(sideType);
    }
}

