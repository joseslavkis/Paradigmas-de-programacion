package logic.blocks;

import logic.Laser;
import logic.Position;

public interface Block{
    Laser applyEffect(Laser laser, Position position);
    BlockType getType();
}
