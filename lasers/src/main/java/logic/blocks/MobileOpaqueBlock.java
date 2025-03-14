package logic.blocks;
import logic.Laser;
import logic.Pair;
import logic.Position;

public class MobileOpaqueBlock implements Block {
    @Override
    public Pair applyEffect(Laser laser, Position position, Side side) {
        return new FixedOpaqueBlock().applyEffect(laser, position, side);
    }

    @Override
    public BlockType getType() {
        return BlockType.MOBILE_OPAQUE;
    }
}
