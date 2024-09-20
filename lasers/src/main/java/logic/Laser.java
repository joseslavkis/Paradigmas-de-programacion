package logic;

import java.util.Map;
import logic.blocks.Side;

public class Laser {
    private Direction direction;
    private final Position position;

    public Laser(Direction direction, Position position) {
        this.direction = direction;
        this.position = position;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Side defineBlock() throws Exception {
        if (position.isEvenAndOdd()) {
            Map<Direction, Side> evenAndOdd = Map.of(
                    Direction.SE, Side.UPPER,
                    Direction.SW, Side.UPPER,
                    Direction.NE, Side.LOWER,
                    Direction.NW, Side.LOWER
            );
            return evenAndOdd.get(direction);

        } else if (!position.isEvenAndOdd()) {
            Map<Direction, Side> oddAndEven = Map.of(
                    Direction.SE, Side.LEFT,
                    Direction.SW, Side.RIGHT,
                    Direction.NE, Side.LEFT,
                    Direction.NW, Side.RIGHT
            );
            return oddAndEven.get(direction);

        }
        return null;
    }

    public Direction getDirection() {
        return direction;
    }

    public Position getPosition() {
        return position;
    }

}