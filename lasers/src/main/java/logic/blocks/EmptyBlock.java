package logic.blocks;

public class EmptyBlock implements Block{
    @Override
    public void applyEffect() {

    }

    @Override
    public BlockType getType() {
        return BlockType.EMPTY_BLOCK;
    }

}