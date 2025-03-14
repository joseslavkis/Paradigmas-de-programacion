package logic.effect;

import logic.Laser;
import logic.Pair;
import logic.Position;
import logic.blocks.Block;
import logic.blocks.Side;
import logic.blocks.SideType;

import java.util.Map;

public class GenericEffectApplier implements EffectApplier{

    @Override
    public void applyEffect(Block currentBlock, Laser newLaser, Position newPosition, SideType blockSideType, Map<Pair, Laser> auxMap) {
        Pair pair = currentBlock.applyEffect(newLaser, newPosition, new Side(blockSideType));
        Laser laser = new Laser(pair.getDirection());
        auxMap.put(pair, laser);
    }
}
