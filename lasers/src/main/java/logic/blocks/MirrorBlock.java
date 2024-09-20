package logic.blocks;

import logic.Direction;
import logic.Laser;
import logic.Position;
import java.util.Map;

public class MirrorBlock implements Block {
    @Override
    public Laser applyEffect(Laser laser, Position position, Side side) {

       if (side == Side.LEFT) {
           Map<Direction, Direction> positionMap = Map.of(
                   Direction.SE, Direction.SW,
                   Direction.NE, Direction.NW,
                   );
       } else if (side == Side.LOWER) {
           Map<Direction, Direction> positionMap = Map.of(
                   Direction.NE, Direction.SE,
                   Direction.NW, Direction.SW,
                   );
       } else if(side == Side.RIGHT) {
           Map<Direction, Direction> positionMap = Map.of(
                   Direction.SW, Direction.SE,
                   Direction.NW, Direction.NE,
                   );
       } else {
           Map<Direction, Direction> positionMap = Map.of(
                   Direction.SE, Direction.NE,
                   Direction.SW, Direction.NW,
                   );
       }

        Direction newDirection = directionMap.get(laser.getDirection());
        return new Laser(newDirection, laser.getPosition());
    }

    @Override
    public BlockType getType() {
        return BlockType.MIRROR;
    }
}