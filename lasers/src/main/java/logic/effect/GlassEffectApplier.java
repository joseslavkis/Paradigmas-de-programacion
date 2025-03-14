package logic.effect;

import logic.Laser;
import logic.Pair;
import logic.Position;
import logic.blocks.Block;
import logic.blocks.EmptyBlock;
import logic.blocks.Side;
import logic.blocks.SideType;

import java.util.Map;

public class GlassEffectApplier implements EffectApplier{
    @Override
    public void applyEffect(Block currentBlock, Laser newLaser, Position newPosition, SideType blockSideType, Map<Pair, Laser> auxMap) {
        Block emptyBlock = new EmptyBlock();
        Pair pair1 = emptyBlock.applyEffect(newLaser, newPosition, new Side(blockSideType));
        Pair pair2 = currentBlock.applyEffect(newLaser, newPosition, new Side(blockSideType));
        Laser laser1 = new Laser(pair1.getDirection());
        Laser laser2 = new Laser(pair2.getDirection());

        auxMap.put(pair1, laser1);
        if (auxMap.containsKey(pair2)) return;
        auxMap.put(pair2, laser2);
    }
}
