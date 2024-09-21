package logic;

import java.util.Map;
import logic.blocks.Side;

public class Laser {
    private Direction direction;

    public Laser(Direction direction) {
        this.direction = direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Side getBlockSide(Position position) {
        if (position.isEvenAndOdd()) {
            Map<Direction, Side> evenAndOdd = Map.of(
                    Direction.SE, Side.LEFT,
                    Direction.SW, Side.RIGHT,
                    Direction.NE, Side.LEFT,
                    Direction.NW, Side.RIGHT,
                    Direction.S, Side.LOWER,
                    Direction.N, Side.UPPER,
                    Direction.E, Side.RIGHT,
                    Direction.W, Side.LEFT

            );
            return evenAndOdd.get(direction);

        } else if (!position.isEvenAndOdd()) {
            Map<Direction, Side> OddAndEven = Map.of(
                    Direction.SE, Side.UPPER,
                    Direction.SW, Side.UPPER,
                    Direction.NE, Side.LOWER,
                    Direction.NW, Side.LOWER,
                    Direction.S, Side.LOWER,
                    Direction.N, Side.UPPER,
                    Direction.E, Side.RIGHT,
                    Direction.W, Side.LEFT
            );
            return OddAndEven.get(direction);
        }
        return null;
    }

    public Direction getDirection() {
        return direction;
    }

}