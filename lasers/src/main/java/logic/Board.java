package logic;

import logic.blocks.*;
import logic.effect.CrystalEffectApplier;
import logic.effect.EffectApplier;
import logic.effect.GenericEffectApplier;
import logic.effect.GlassEffectApplier;

import java.util.*;

public class Board {
    private final Map<Position, Block> blocks;
    private final Map<Position, Objective> objectives;
    private Map<Pair, Laser> lasers;
    private final Map<Pair, Laser> primitiveLasers;
    private final int row;
    private final int column;

    public Board(int row, int column, Map<Position, Block> blocks, Map<Position, Objective> objectives, Map<Pair, Laser> laser, Map<Pair, Laser> primitiveLasers) {
        this.blocks = blocks;
        this.objectives = objectives;
        this.row = row;
        this.column = column;
        this.lasers = laser;
        this.primitiveLasers = primitiveLasers;
    }

    public Map<Position, Objective> getObjectives() {
        return objectives;
    }

    public Map<Pair, Laser> getLasers() {
        return lasers;
    }

    public Map<Pair, Laser> getPrimitiveLasers() {
        return primitiveLasers;
    }

    public void resetLasers() {
        this.lasers = new HashMap<>(primitiveLasers);
    }

    public void moveBlock(Position from, Position to) {
        Block block1 = blocks.get(from);
        Block block2 = blocks.get(to);

        if (block1.getType() == BlockType.NOT || block2.getType() == BlockType.NOT) return;
        if (block1.getType() == BlockType.FIXED_OPAQUE || block2.getType() == BlockType.FIXED_OPAQUE) return;
        if (block1.getType() == BlockType.EMPTY) return;

        blocks.put(from, block2);
        blocks.put(to, block1);
    }

    public void moveAllLaser() {
        Map<Pair, Laser> auxMap = new HashMap<>();

        for (Map.Entry<Pair, Laser> positionLaserEntry : lasers.entrySet()) {
            Laser currentLaser = positionLaserEntry.getValue();
            if (currentLaser.getDirection() == Direction.STATIC) continue;
            Position currentLaserPosition = positionLaserEntry.getKey().getPosition();

            moveLaser(currentLaser, currentLaserPosition, auxMap);
        }
        lasers.putAll(auxMap);
    }

    public boolean isWin() {
        int objectivesCrossed = objectives.size();
        Map<Position, Laser> laserPositions = new HashMap<>();

        for (Map.Entry<Pair, Laser> entry : lasers.entrySet()) {
            laserPositions.put(entry.getKey().getPosition(), entry.getValue());
        }

        for (Map.Entry<Position, Objective> currentElement : objectives.entrySet()) {
            Position currentObjectivePosition = currentElement.getKey();
            if (laserPositions.containsKey(currentObjectivePosition)) {
                objectivesCrossed -= 1;
            }
        }

        return objectivesCrossed == 0;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Map<Position, Block> getBlocks() {
        return blocks;
    }

     private boolean isCurrentBlockNull(Block currentBlock, Position newPosition, Laser newLaser, Map<Pair, Laser> auxMap) {
        if (currentBlock == null) {
            Pair finalPair = new Pair(newPosition, newLaser.getDirection());
            Laser finalLaser = new Laser(finalPair.getDirection());
            auxMap.put(finalPair, finalLaser);
            return true;
        }
        return false;
    }

    private void moveLaser(Laser currentLaser, Position currentLaserPosition, Map<Pair, Laser> auxMap) {
        Map<Direction, Position> movementMap = currentLaserPosition.getMovementMap();
        DisplacementApplier applier = new DisplacementApplier(movementMap);

        Position newPosition = currentLaserPosition.rePosition(currentLaser, currentLaserPosition, applier, row, column);
        if (newPosition == null) return;

        Laser newLaser = new Laser(currentLaser.getDirection());

        SideType blockSideType = currentLaser.getBlockSide(currentLaserPosition);
        if (blockSideType == null) return;

        Position currentBlockPosition = currentLaserPosition.getBorder(newPosition, new Side(blockSideType));
        Block currentBlock = blocks.get(currentBlockPosition);

        if (isCurrentBlockNull(currentBlock, newPosition, newLaser, auxMap)) return;

        applyBlockEffect(currentBlock, newLaser, newPosition, blockSideType, auxMap, applier);
    }

    private void applyBlockEffect(Block currentBlock, Laser newLaser, Position newPosition, SideType blockSideType, Map<Pair, Laser> auxMap, DisplacementApplier displacementApplier) {
        Map<BlockType, EffectApplier> applierMap = new HashMap<>(Map.of(
                BlockType.CRYSTAL, new CrystalEffectApplier(displacementApplier),
                BlockType.GLASS, new GlassEffectApplier())
        );

        EffectApplier effectApplier = Optional.ofNullable(applierMap.get(currentBlock.getType())).orElse(new GenericEffectApplier());
        effectApplier.applyEffect(currentBlock, newLaser, newPosition, blockSideType, auxMap);
    }

}
