package logic.blocks;
import logic.Laser;
import logic.Position;

public class MobileOpaqueBlock implements Block {
    @Override
    public Laser applyEffect(Laser laser, Position position) {
        return laser;
    }

    @Override
    public BlockType getType() {
        return BlockType.MOBILE_OPAQUE;
    }
}
