package logic;
import java.util.Map;
import java.util.Objects;

import javafx.geometry.Pos;
import logic.blocks.Side;

public class Position {
    private int row;
    private int column;
    private static final Map<Direction, Position> movementMap = Map.of(
            Direction.SE, new Position(1, 1),
            Direction.SW, new Position(1, -1),
            Direction.NE, new Position(-1, 1),
            Direction.NW, new Position(-1, -1),
            Direction.S, new Position(2, 0),
            Direction.N, new Position(-2, 0),
            Direction.E, new Position(0, 2),
            Direction.W, new Position(0, -2)
    );

    public Map<Direction, Position> getMovementMap() {
        return movementMap;
    }

    public Position(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isEvenAndOdd() {
        return row % 2 == 0 && column % 2 != 0;
    }

    public Position getBorder(Position currentPos, Side border) {
        switch (border) {
            case UPPER:
                return new Position(currentPos.getRow() + 1, currentPos.getColumn());
            case LOWER:
                return new Position(currentPos.getRow() - 1, currentPos.getColumn());
            case LEFT:
                return new Position(currentPos.getRow(), currentPos.getColumn() + 1);
            case RIGHT:
                return new Position(currentPos.getRow(), currentPos.getColumn() - 1);
            default:
                throw new RuntimeException("Unknown border");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    public Position rePosition(Laser currentLaser, Position currentLaserPosition, DisplacementApplier applier, int row, int column) {
        Position delta = applier.getDisplacement(currentLaser.getDirection());
        Position newPosition = applier.applyDisplacement(currentLaserPosition, delta);

        if (newPosition.getRow() < 0 || newPosition.getRow() > 2*row || newPosition.getColumn() < 0 || newPosition.getColumn() > 2*column) {
            return null;
        }
        return newPosition;
    }
}