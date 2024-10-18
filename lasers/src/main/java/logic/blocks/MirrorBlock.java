package logic.blocks;

import logic.Direction;
import logic.Laser;
import logic.Pair;
import logic.Position;
import java.util.Map;

public class MirrorBlock implements Block {
    @Override
    public Pair applyEffect(Laser laser, Position position, Side side) {
        Map<Direction, Direction> directionMap = side.getPossibleDirections();
        Direction newDirection = directionMap.get(laser.getDirection());
        return new Pair(position, newDirection);
    }

    @Override
    public BlockType getType() {
        return BlockType.MIRROR;
    }
}