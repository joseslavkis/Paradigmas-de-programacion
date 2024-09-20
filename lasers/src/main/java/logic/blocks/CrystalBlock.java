package logic.blocks;

import logic.Board;
import logic.Laser;
import logic.Position;
import static logic.Direction.NE;
import static logic.Direction.SE;

public class CrystalBlock implements Block {
//    private final Board board;
//    public CrystalBlock(Board board) {
//        this.board = board;
//    }

    @Override
    public Laser applyEffect(Laser laser, Position position) {
        //        Laser continuedLaser = new EmptyBlock().applyEffect(laser, position);
//        board.addLasers(reflectedLaser);
//        board.addLasers(continuedLaser);



        return new MirrorBlock().applyEffect(laser, position);
    }




/*
    public Laser applyEffect(Laser laser, Position position) {
        if (laser.getDirection() == NORTH_EAST || laser.getDirection() == SOUTH_EAST) {
            Position newPosition = new Position(position.getRow(), position.getColumn()+2);
            return new Laser(laser.getDirection(), newPosition);

        } else {
            Position newPosition = new Position(position.getRow(), position.getColumn()-2);
            return new Laser(laser.getDirection(), newPosition);

        }
//        return new MirrorBlock().applyEffect(laser, position);
    }
*/
    @Override
    public BlockType getType() {
        return BlockType.CRYSTAL;
    }
}