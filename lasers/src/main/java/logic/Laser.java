package logic;

public class Laser {
    private Direction direction;
    private Position position;

    public Laser(Direction direction, Position position) {
        this.direction = direction;
        this.position = position;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public Position getPosition() {
        return position;
    }
}