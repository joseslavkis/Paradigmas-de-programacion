package logic.blocks;

import logic.Direction;
import logic.Laser;
import logic.Pair;
import logic.Position;

import java.util.Map;

public class Side {
    private final SideType sideType;

    public Side(SideType sideType) {
        this.sideType = sideType;
    }

    public Map<Direction, Direction> getPossibleDirections() {
        Map<SideType, Map<Direction, Direction>> directionMap = Map.of(
                SideType.LEFT, Map.of(
                        Direction.SE, Direction.SW,
                        Direction.NE, Direction.NW
                ),
                SideType.LOWER, Map.of(
                        Direction.NE, Direction.SE,
                        Direction.NW, Direction.SW
                ),
                SideType.RIGHT, Map.of(
                        Direction.SW, Direction.SE,
                        Direction.NW, Direction.NE
                ),
                SideType.UPPER, Map.of(
                        Direction.SE, Direction.NE,
                        Direction.SW, Direction.NW
                )
        );
        return directionMap.get(sideType);
    }

    public Pair getUpdatedPair(Laser laser, Position position) {
        Map<SideType, Pair> possiblePairs = Map.of(
                SideType.LEFT, new Pair(new Position(position.getRow(), position.getColumn()+2), laser.getDirection()),
                SideType.LOWER, new Pair(new Position(position.getRow() - 2, position.getColumn()), laser.getDirection()),
                SideType.RIGHT, new Pair(new Position(position.getRow(), position.getColumn()-2), laser.getDirection()),
                SideType.UPPER, new Pair(new Position(position.getRow() + 2, position.getColumn()), laser.getDirection())
        );
        return possiblePairs.get(sideType);
    }

}
