package logic;

import java.util.Map;
import java.util.Objects;

import logic.blocks.SideType;

public class Laser {
    private Direction direction;

    public Laser(Direction direction) {
        this.direction = direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public SideType getBlockSide(Position position) {
        if (position.checkEven("even", "odd")) {
            Map<Direction, SideType> evenAndOdd = Map.of(
                    Direction.SE, SideType.LEFT,
                    Direction.SW, SideType.RIGHT,
                    Direction.NE, SideType.LEFT,
                    Direction.NW, SideType.RIGHT,
                    Direction.S, SideType.LOWER,
                    Direction.N, SideType.UPPER,
                    Direction.E, SideType.RIGHT,
                    Direction.W, SideType.LEFT

            );
            return evenAndOdd.get(direction);

        } else if (position.checkEven("odd", "even")) {
            Map<Direction, SideType> oddAndEven = Map.of(
                    Direction.SE, SideType.UPPER,
                    Direction.SW, SideType.UPPER,
                    Direction.NE, SideType.LOWER,
                    Direction.NW, SideType.LOWER,
                    Direction.S, SideType.LOWER,
                    Direction.N, SideType.UPPER,
                    Direction.E, SideType.RIGHT,
                    Direction.W, SideType.LEFT
            );
            return oddAndEven.get(direction);
        }
        return null;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Laser laser = (Laser) o;
        return direction == laser.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(direction);
    }

}