package logic.blocks;

import logic.Laser;
import logic.Pair;
import logic.Position;

public class GlassBlock implements Block {
    @Override
    public Pair applyEffect(Laser laser, Position position, Side side) {
        return new MirrorBlock().applyEffect(laser, position, side);
    }

    @Override
    public BlockType getType() {
        return BlockType.GLASS;
    }
}
