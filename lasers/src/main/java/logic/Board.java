package logic;

import logic.blocks.Block;
import logic.blocks.BlockType;
import logic.blocks.EmptyBlock;
import logic.blocks.Side;
import org.example.Listener;
import org.example.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Board implements Observable<String> {
    private final Map<Position, Block> blocks;
    private final Map<Position, Objective> objectives;
    private final Map<Position, Laser> lasers;
    private final int row;
    private final int column;
    private final List<Listener<String>> listeners = new ArrayList<>();

    public Board(int row, int column, Map<Position, Block> blocks, Map<Position, Objective> objectives, Map<Position, Laser> laser) {
        this.blocks = blocks;
        this.objectives = objectives;
        this.row = row;
        this.column = column;
        this.lasers = laser;
    }
    public Map<Position, Objective> getObjectives() {
        return objectives;
    }
    public Map<Position, Laser> getLasers() {
        return lasers;
    }
    @Override
    public void addListener(Listener<String> listener){
        listeners.add(listener);
    };


    public void moveBlock(Position from, Position to) {
        Block block1 = blocks.get(from);
        Block block2 = blocks.get(to);

        if (block1.getType() == BlockType.NOT_BLOCK || block2.getType() == BlockType.NOT_BLOCK) return;

        blocks.put(from, block2);
        blocks.put(to, block1);

        String event = "Moved block from " + from.toString() + " to " + to.toString();

        listeners.forEach(listener -> listener.notifyChanges(event));

    }

    public void moveLaser() {
        for (Map.Entry<Position, Laser> positionLaserEntry : lasers.entrySet()) {
            Laser currentLaser = positionLaserEntry.getValue();
            Position currentLaserPosition = positionLaserEntry.getKey();

            Map<Direction, Position> positionMap = Map.of(
                    Direction.SE, new Position(1, 1),
                    Direction.SW, new Position(1, -1),
                    Direction.NE, new Position(-1, 1),
                    Direction.NW, new Position(-1, -1)
            );


            DisplacementApplier applier = new DisplacementApplier(positionMap);
            Position delta = applier.getDisplacement(currentLaser.getDirection());
            Position newPosition = applier.applyDisplacement(currentLaserPosition, delta);

            Laser newLaser = new Laser(currentLaser.getDirection(), newPosition);

            // This works with the current address and position determines which block side the laser hits.
            Side blockSide = currentLaser.defineBlock();
            if (blockSide == null) {
                return;
            }
            // This function with the current position and the side of the block determines what type
            // of block the laser hits.
            Position currentBlockPosition = currentLaserPosition.getBorder(newLaser.getPosition(), blockSide);
            Block currentBlock = blocks.get(currentBlockPosition);

            switch (currentBlock.getType()) {
                case CRYSTAL:
                    Block emptyBlock = new EmptyBlock();
                    Laser laser1 = emptyBlock.applyEffect(newLaser, newLaser.getPosition());
                    Laser laser2 = currentBlock.applyEffect(newLaser, newLaser.getPosition());
                    lasers.put(laser1.getPosition(), laser1);
                    lasers.put(laser2.getPosition(), laser2);
                    break;
                default:
                    Laser finalLaser = currentBlock.applyEffect(newLaser, newLaser.getPosition());
                    lasers.put(finalLaser.getPosition(), finalLaser);
                    break;
            }
        }
    }

    public Map<Position, Block> getBlocks() {
        return blocks;
    }
}
