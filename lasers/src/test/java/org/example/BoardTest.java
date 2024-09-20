package org.example;

import logic.*;
import logic.blocks.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BoardTest {

    @Test
    public void testMoveBlock() {
        Map<Position, Block> blocks = new HashMap<>();
        blocks.put(new Position(1, 1), new FixedOpaqueBlock());
        blocks.put(new Position(1, 3), new MobileOpaqueBlock());

        Board board = new Board(5, 5, blocks, new HashMap<>(), new HashMap<>());
        board.moveBlock(new Position(1, 1), new Position(1, 3));

        Assert.assertTrue(board.getBlocks().get(new Position(1, 1)) instanceof MobileOpaqueBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(1, 3)) instanceof FixedOpaqueBlock);
    }

    @Test
    public void testMoveLaser() {
        Map<Position, Block> blocks = new HashMap<>();
        blocks.put(new Position(1, 3), new MirrorBlock());

        Map<Position, Laser> lasers = new HashMap<>();
        lasers.put(new Position(0, 1), new Laser(Direction.SE, new Position(0, 1)));

        Board board = new Board(5, 5, blocks, new HashMap<>(), lasers);
        board.moveLaser();

        // Verificar que el láser se haya movido correctamente
        Assert.assertTrue(board.getLasers().containsKey(new Position(1, 3)));
    }

    @Test
    public void testLoadBoardLevel5() throws IOException {
        FileLoader fileLoader = new FileLoader();
        Map<Position, Block> blocks = fileLoader.loadBlocks("src/test/resources/level5.dat");
        Map<Position, Laser> lasers = fileLoader.loadLasers("src/test/resources/level5.dat");
        Map<Position, Objective> objectives = fileLoader.loadObjectives("src/test/resources/level5.dat");

        Board board = new Board(9, 9, blocks, objectives, lasers);

        // Verificar que los bloques, láseres y objetivos se cargaron correctamente
        Assert.assertTrue(board.getBlocks().get(new Position(1, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getLasers().get(new Position(5, 2)) != null);
        Assert.assertTrue(board.getObjectives().get(new Position(3, 6)) != null);
    }

    @Test
    public void testLoadBoardLevel6() throws IOException {
        FileLoader fileLoader = new FileLoader();
        Map<Position, Block> blocks = fileLoader.loadBlocks("src/test/resources/level6.dat");
        Map<Position, Laser> lasers = fileLoader.loadLasers("src/test/resources/level6.dat");
        Map<Position, Objective> objectives = fileLoader.loadObjectives("src/test/resources/level6.dat");

        Board board = new Board(9, 9, blocks, objectives, lasers);

        // Verificar que los bloques, láseres y objetivos se cargaron correctamente
        Assert.assertTrue(board.getBlocks().get(new Position(1, 1)) instanceof MirrorBlock);
        Assert.assertTrue(board.getLasers().get(new Position(0, 3)) != null);
        Assert.assertTrue(board.getObjectives().get(new Position(1, 6)) != null);
    }
}