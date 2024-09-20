package logic;
import java.util.Objects;

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
        return column % 2 == 0 && row % 2 != 0;
    }

    public Position getBorder(Position currentPos, String border) {
        switch (border) {
            case "upper":
                return new Position(currentPos.getRow() - 1, currentPos.getColumn());
            case "lower":
                return new Position(currentPos.getRow() + 1, currentPos.getColumn());
            case "left":
                return new Position(currentPos.getRow(), currentPos.getColumn() + 1);
            case "right":
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