package logic.blocks;

import logic.Direction;
import logic.Laser;
import logic.Pair;
import logic.Position;

import java.util.Map;

public class Side {
    private SideType sideType;

    public Side(SideType sideType) {
        this.sideType = sideType;
    }

    public Map<Direction, Direction> getPosibleDirections() {
        Map<Direction, Direction> directionMap;
        if (sideType == SideType.LEFT) {
            directionMap = Map.of(
                    Direction.SE, Direction.SW,
                    Direction.NE, Direction.NW
            );
        } else if (sideType == SideType.LOWER) {
            directionMap = Map.of(
                    Direction.NE, Direction.SE,
                    Direction.NW, Direction.SW
            );
        } else if (sideType == SideType.RIGHT) {
            directionMap = Map.of(
                    Direction.SW, Direction.SE,
                    Direction.NW, Direction.NE
            );
        } else {
            directionMap = Map.of(
                    Direction.SE, Direction.NE,
                    Direction.SW, Direction.NW
            );
        }
        return directionMap;
    }

    public Pair getUpdatedPair(Laser laser, Position position) {
        Map<SideType, Pair> posiblePairs = Map.of(
                SideType.LEFT, new Pair(new Position(position.getRow(), position.getColumn()+2), laser.getDirection()),
                SideType.LOWER, new Pair(new Position(position.getRow() - 2, position.getColumn()), laser.getDirection()),
                SideType.RIGHT, new Pair(new Position(position.getRow(), position.getColumn()-2), laser.getDirection()),
                SideType.UPPER, new Pair(new Position(position.getRow() + 2, position.getColumn()), laser.getDirection())
        );
        return posiblePairs.get(sideType);
    }

}
