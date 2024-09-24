package logic;

public class Objective {
    private final Position position;

    public Objective(Position position) {
        this.position = position;
    }
    public Position getPosition() {
        return position;
    }
    public boolean isCrossed(Position positionLaser) {
        return positionLaser == position;
    }
}
