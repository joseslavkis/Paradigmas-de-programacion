package logic.blocks;
import logic.DisplacementApplier;
import logic.Laser;
import logic.Position;

public class EmptyBlock implements Block{
    @Override
    public Laser applyEffect(Laser laser, Position position) {
        DisplacementApplier applier = new DisplacementApplier();
        
        Position displacement = applier.getDisplacement(laser.getDirection());
        if(displacement == null) return laser;

        return new Laser(laser.getDirection(), applier.applyDisplacement(position, displacement));
    }

    @Override
    public BlockType getType() {
        return BlockType.EMPTY_BLOCK;
    }


 
}