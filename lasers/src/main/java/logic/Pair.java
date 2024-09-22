package logic;

import java.util.Objects;

public class Pair {
    Position position;
    Direction direction;

    public Pair(Position position, Direction direction) {
        this.position = position;
        this.direction = direction;
    }

    public Position getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(position, pair.position) && direction == pair.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, direction);
    }
}
