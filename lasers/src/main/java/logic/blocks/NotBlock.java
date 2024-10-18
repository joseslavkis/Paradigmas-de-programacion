package logic.blocks;

import logic.Laser;
import logic.Pair;
import logic.Position;

public class NotBlock implements Block {
    @Override
    public Pair applyEffect(Laser laser, Position position, Side side) {
        return new EmptyBlock().applyEffect(laser, position, side);
    }

    @Override
    public BlockType getType() {
        return BlockType.NOT;
    }
}