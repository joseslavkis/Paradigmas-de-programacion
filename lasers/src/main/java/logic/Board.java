package logic;

import logic.blocks.Block;
import logic.blocks.BlockType;
import logic.blocks.EmptyBlock;
import logic.blocks.Side;
import org.example.Listener;
import org.example.Observable;

import java.util.ArrayList;
import java.util.HashMap;
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
    public void addListener(Listener<String> listener) {
        listeners.add(listener);
    }

    public void moveBlock(Position from, Position to) {
        Block block1 = blocks.get(from);
        Block block2 = blocks.get(to);

        if (block1.getType() == BlockType.NOT_BLOCK || block2.getType() == BlockType.NOT_BLOCK) return;

        blocks.put(from, block2);
        blocks.put(to, block1);

        String event = "Moved block from " + from.toString() + " to " + to.toString();

        listeners.forEach(listener -> listener.notifyChanges(event));

    }

    public void moveAllLaser() {
        Map<Position, Laser> auxMap = new HashMap<>();

        for (Map.Entry<Position, Laser> positionLaserEntry : lasers.entrySet()) {
            Laser currentLaser = positionLaserEntry.getValue();
            Position currentLaserPosition = positionLaserEntry.getKey();

            Map<Direction, Position> positionMap = Map.of(
                    Direction.SE, new Position(1, 1),
                    Direction.SW, new Position(1, -1),
                    Direction.NE, new Position(-1, 1),
                    Direction.NW, new Position(-1, -1),
                    Direction.S, new Position(2, 0),
                    Direction.N, new Position(-2, 0),
                    Direction.E, new Position(0, 2),
                    Direction.W, new Position(0, -2)
            );

            DisplacementApplier applier = new DisplacementApplier(positionMap);
            Position delta = applier.getDisplacement(currentLaser.getDirection());
            Position newPosition = applier.applyDisplacement(currentLaserPosition, delta);

            Laser newLaser = new Laser(currentLaser.getDirection());

            // This works with the current address and position determines which block side the laser hits.
            Side blockSide = currentLaser.getBlockSide(currentLaserPosition);
            if (blockSide == null) {
                return;
            }
            // This function with the current position and the side of the block determines what type
            // of block the laser hits.
            Position currentBlockPosition = currentLaserPosition.getBorder(newPosition, blockSide);
            Block currentBlock = blocks.get(currentBlockPosition);

            switch (currentBlock.getType()) {
                case GLASS:
                    Block emptyBlock = new EmptyBlock();
                    Pair pair1 = emptyBlock.applyEffect(newLaser, newPosition, blockSide);
                    Pair pair2 = currentBlock.applyEffect(newLaser, newPosition, blockSide);
                    Laser laser1 = new Laser(pair1.getDirection());
                    Laser laser2 = new Laser(pair2.getDirection());

                    auxMap.put(pair1.getPosition(), laser1);
                    if (auxMap.containsKey(pair2.getPosition())) continue;
                    auxMap.put(pair2.getPosition(), laser2);
                    break;

                case CRYSTAL:
                    Pair PairExited = currentBlock.applyEffect(newLaser, newPosition, blockSide);
                    Laser laserExited = new Laser(PairExited.getDirection());
                    Direction directionExited = laserExited.getDirection();

                    Direction newDirection = applier.getCrystalDirection(blockSide);
                    newLaser.setDirection(newDirection);

                    auxMap.put(PairExited.getPosition(), laserExited);
                    if (auxMap.containsKey(newPosition)) continue; // Verifico si ya esta puesto este lado ( puede ser que al hacer el recorrido repita este punto pq es un "punto doble" )
                    auxMap.put(newPosition, newLaser);
                    break;

                default:
                    Pair finalPair = currentBlock.applyEffect(newLaser, newPosition, blockSide);
                    if (finalPair == null) return;
                    Laser finalLaser = new Laser(finalPair.getDirection());
                    auxMap.put(finalPair.getPosition(), finalLaser);
                    break;
            }
        }

        lasers.putAll(auxMap);

    }

    public Map<Position, Block> getBlocks() {
        return blocks;
    }
}
