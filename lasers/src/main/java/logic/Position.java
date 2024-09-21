package logic;
import java.util.Objects;
import logic.blocks.Side;

public class Position {
    private final int row;
    private final int column;

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
}