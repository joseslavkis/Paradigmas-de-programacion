package logic.blocks;

import logic.Laser;
import logic.Pair;
import logic.Position;

public class CrystalBlock implements Block {

    @Override
    public Pair applyEffect(Laser laser, Position position, Side side) {
        return side.getUpdatedPair(laser, position);
    }

    @Override
    public BlockType getType() {
        return BlockType.CRYSTAL;
    }
}