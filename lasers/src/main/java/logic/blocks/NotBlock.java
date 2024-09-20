package logic.blocks;

import logic.Laser;
import logic.Position;

public class NotBlock implements Block {
    @Override
    public Laser applyEffect(Laser laser, Position position, Side side) {
        // Devuelve el láser siguiendo su camino (puedes ajustar esto según la lógica de tu aplicación)
        return new EmptyBlock().applyEffect(laser, position, side);
    }

    @Override
    public BlockType getType() {
        return BlockType.NOT_BLOCK;
    }
}