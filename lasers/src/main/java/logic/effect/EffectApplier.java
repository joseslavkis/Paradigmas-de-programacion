package logic.effect;

import logic.DisplacementApplier;
import logic.Laser;
import logic.Pair;
import logic.Position;
import logic.blocks.Block;
import logic.blocks.SideType;

import java.util.Map;

public interface EffectApplier {
    void applyEffect(Block currentBlock, Laser newLaser, Position newPosition, SideType blockSideType, Map<Pair, Laser> auxMap);
}
