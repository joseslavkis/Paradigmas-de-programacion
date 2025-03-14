package logic.effect;

import logic.*;
import logic.blocks.Block;
import logic.blocks.Side;
import logic.blocks.SideType;

import java.util.Map;

public class CrystalEffectApplier implements EffectApplier {
    private final DisplacementApplier applier;

    public CrystalEffectApplier(DisplacementApplier applier) {
        this.applier = applier;
    }

    @Override
    public void applyEffect(Block currentBlock, Laser newLaser, Position newPosition, SideType blockSideType, Map<Pair, Laser> auxMap) {
        Pair pairExited = currentBlock.applyEffect(newLaser, newPosition, new Side(blockSideType));
        Laser laserExited = new Laser(pairExited.getDirection());

        Direction newDirection = applier.getCrystalDirection(blockSideType);
        newLaser.setDirection(newDirection);

        auxMap.put(pairExited, laserExited);
        Pair newPair = new Pair(newPosition, newDirection);

        if (!auxMap.containsKey(newPair)) {
            auxMap.put(newPair, newLaser);
        }
    }
}
