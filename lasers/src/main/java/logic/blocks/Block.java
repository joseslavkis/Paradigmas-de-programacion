package logic.blocks;

import logic.Laser;
import logic.Pair;
import logic.Position;

public interface Block {
    Pair applyEffect(Laser laser, Position position, Side side);
    BlockType getType();
}
