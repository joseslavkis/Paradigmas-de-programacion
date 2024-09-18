package logic.blocks;

import logic.Laser;
import logic.Position;

public class GlassBlock implements Block {
    @Override
    public Laser applyEffect(Laser laser, Position position) {
        return laser;
    }

    @Override
    public BlockType getType() {
        return BlockType.GLASS;
    }
}
