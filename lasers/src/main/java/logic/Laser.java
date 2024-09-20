package logic;

import java.util.Map;


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

    // This method defines the block in which the laser collides by positioning and addressing.
    public String defineBlock() {
        if (position.isEvenAndOdd()) {
            Map<Direction, String> evenAndOdd = Map.of(
                    Direction.SE, "upper",
                    Direction.SW, "upper",
                    Direction.NE, "lower",
                    Direction.NW, "lower"
            );
            return evenAndOdd.get(direction);

        } else if (!position.isEvenAndOdd()) {
            Map<Direction, String> oddAndEven = Map.of(
                    Direction.SE, "left",
                    Direction.SW, "right",
                    Direction.NE, "left",
                    Direction.NW, "right"
            );
            return oddAndEven.get(direction);

        }
        return "Error";
    }

    public Direction getDirection() {
        return direction;
    }

    public Position getPosition() {
        return position;
    }

}