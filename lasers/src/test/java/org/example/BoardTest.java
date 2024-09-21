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
    public void testMoveLaserOpaqueBlocks() throws Exception {
        Map<Position, Block> blocks = new HashMap<>();
        blocks.put(new Position(1, 3), new MobileOpaqueBlock());

        Map<Position, Laser> lasers = new HashMap<>();
        lasers.put(new Position(0, 1), new Laser(Direction.SE));

        Board board = new Board(5, 5, blocks, new HashMap<>(), lasers);
        board.moveAllLaser();

        // Verificar que el láser se haya movido correctamente
        Assert.assertFalse(board.getLasers().containsKey(new Position(1, 2)));
        Assert.assertEquals(1, board.getLasers().size());
    }

    public void testMoveLaser() throws Exception {
        Map<Position, Block> blocks = new HashMap<>();
        blocks.put(new Position(1, 3), new EmptyBlock());
        blocks.put(new Position(3, 3), new CrystalBlock());
        blocks.put(new Position(5, 5), new MirrorBlock());

        Map<Position, Laser> lasers = new HashMap<>();
        lasers.put(new Position(0, 1), new Laser(Direction.SE));

        Board board = new Board(5, 5, blocks, new HashMap<>(), lasers);
        board.moveAllLaser();
        board.moveAllLaser();
        board.moveAllLaser();

        // Verificar que el láser se haya movido correctamente
        Assert.assertTrue(board.getLasers().containsKey(new Position(1, 2)));
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Position(1, 2)).getDirection());
        Assert.assertEquals(Direction.S, board.getLasers().get(new Position(2, 3)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Position(4, 3)).getDirection());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Position(5, 4)).getDirection());

    }

    @Test
    public void testLoadBoardLevel5() throws IOException {
        FileLoader fileLoader = new FileLoader();
        Map<Position, Block> blocks = fileLoader.loadBlocks("src/test/resources/level5.dat");
        Map<Position, Laser> lasers = fileLoader.loadLasers("src/test/resources/level5.dat");
        Map<Position, Objective> objectives = fileLoader.loadObjectives("src/test/resources/level5.dat");

        Board board = new Board(9, 9, blocks, objectives, lasers);

        // Verificar que los bloques, láseres y objetivos se cargaron correctamente.
        Assert.assertTrue(board.getBlocks().get(new Position(1, 1)) instanceof NotBlock);
        Assert.assertNotNull(board.getLasers().get(new Position(2, 5)));
        Assert.assertNotNull(board.getObjectives().get(new Position(6, 3)));
        Assert.assertNotNull(board.getObjectives().get(new Position(8, 7)));

        board.moveAllLaser();

        Assert.assertEquals(Direction.SW, board.getLasers().get(new Position(2, 5)).getDirection());
        Assert.assertEquals(Direction.W, board.getLasers().get(new Position(3, 4)).getDirection());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Position(3, 2)).getDirection());


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