package logic.blocks;

import logic.Direction;
import logic.Laser;
import logic.Position;

public interface Block {
    Laser applyEffect(Laser laser, Position position, Side side);
    BlockType getType();
}
