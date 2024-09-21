package logic.blocks;

import logic.Direction;
import logic.Laser;
import logic.Pair;
import logic.Position;
import java.util.Map;

public class MirrorBlock implements Block {
    @Override
    public Pair applyEffect(Laser laser, Position position, Side side) {
        Map<Direction, Direction> directionMap;
        if (side == Side.LEFT) {
            directionMap = Map.of(
                    Direction.SE, Direction.SW,
                    Direction.NE, Direction.NW
                    );
        } else if (side == Side.LOWER) {
            directionMap = Map.of(
                   Direction.NE, Direction.SE,
                   Direction.NW, Direction.SW
                   );
        } else if(side == Side.RIGHT) {
            directionMap = Map.of(
                   Direction.SW, Direction.SE,
                   Direction.NW, Direction.NE
                   );
        } else {
            directionMap = Map.of(
                   Direction.SE, Direction.NE,
                   Direction.SW, Direction.NW
                   );
        }

        Direction newDirection = directionMap.get(laser.getDirection());
        return new Pair(position, newDirection);
    }

    @Override
    public BlockType getType() {
        return BlockType.MIRROR;
    }
}