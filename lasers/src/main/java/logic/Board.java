package logic;

import logic.blocks.Block;
import java.util.Map;

public class Board {
    private final Map<Position, Block> blocks;
    private final Map<Position, Objective> objectives;
    private final Map<Position, Laser> lasers;
    private final int row;
    private final int column;

    public Board(int row, int column, Map<Position, Block> blocks, Map<Position, Objective> objectives, Map<Position, Laser> laser) {
        this.blocks = blocks;
        this.objectives = objectives;
        this.row = row;
        this.column = column;
        this.lasers = laser;
    }

    private void moveBlock(Position from, Position to) {
        Block block1 = blocks.get(from);
        Block block2 = blocks.get(to);
        blocks.put(from, block2);
        blocks.put(to, block1);
    }

    private void moveLaser(Position from, Position to) {
        lasers.put(to, lasers.get(from));
    }



}
