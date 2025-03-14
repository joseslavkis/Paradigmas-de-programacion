package logic;
import java.util.Map;
import java.util.Objects;

import logic.blocks.Side;
import logic.blocks.SideType;

public class Position implements Comparable<Position> {
    private final int row;
    private final int column;

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

    public boolean checkEven(String rowEven, String columnEven) {
        return checkEven(rowEven, row) && checkEven(columnEven, column);
    }

    private boolean checkEven(String even, int factor) {
        if (even.equalsIgnoreCase("even")) return factor % 2 == 0;
        else return factor % 2 != 0;
    }

    public Position getBorder(Position currentPos, Side border) {
        return border.updatePosition(currentPos);
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

    @Override
    public int compareTo(Position o) {
        int rowCompare = Integer.compare(this.row, o.row);
        if (rowCompare != 0) {
            return rowCompare;
        }
        return Integer.compare(this.column, o.column);
    }

    @Override
    public String toString() {
        return "Position{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }

}