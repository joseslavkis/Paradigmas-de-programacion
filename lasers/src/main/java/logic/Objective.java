package logic;

import java.util.Map;

public class Objective {
    private final Position position;

    public Objective(Position position) {
        this.position = position;
    }
    public Position getPosition() {
        return position;
    }
    public boolean isCrossed(Map<Pair, Laser> lasers) {
        return lasers.keySet().stream().anyMatch(pair -> pair.getPosition().equals(position));
    }
}
