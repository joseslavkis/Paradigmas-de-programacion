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

        Map<Pair, Laser> lasers = new HashMap<>();
        lasers.put(new Pair(new Position(0, 1), Direction.SE), new Laser(Direction.SE));

        Board board = new Board(5, 5, blocks, new HashMap<>(), lasers);
        board.moveAllLaser();

        // Verificar que el láser se haya movido correctamente
        Assert.assertFalse(board.getLasers().containsKey(new Pair(new Position(1, 2), Direction.SE)));
        Assert.assertEquals(1, board.getLasers().size());
    }

    @Test
    public void testMoveLaser() throws Exception {
        Map<Position, Block> blocks = new HashMap<>();
        blocks.put(new Position(1, 3), new EmptyBlock());
        blocks.put(new Position(3, 3), new CrystalBlock());
        blocks.put(new Position(5, 5), new MirrorBlock());

        Map<Pair, Laser> lasers = new HashMap<>();
        lasers.put(new Pair(new Position(0, 1), Direction.SE), new Laser(Direction.SE));

        Board board = new Board(5, 5, blocks, new HashMap<>(), lasers);
        board.moveAllLaser();
        board.moveAllLaser();
        board.moveAllLaser();

        // Verificar que el láser se haya movido correctamente
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(1, 2), Direction.SE)).getDirection());
        Assert.assertEquals(Direction.S, board.getLasers().get(new Pair(new Position(2, 3), Direction.S)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(4, 3), Direction.SE)).getDirection());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(5, 4), Direction.SW)).getDirection());
    }

    @Test
    public void testLoadBoardLevel5() throws IOException {
        FileLoader fileLoader = new FileLoader();
        Map<Position, Block> blocks = fileLoader.loadBlocks("src/test/resources/level5.dat");
        Map<Pair, Laser> lasers = fileLoader.loadLasers("src/test/resources/level5.dat");
        Map<Position, Objective> objectives = fileLoader.loadObjectives("src/test/resources/level5.dat");

        Board board = new Board(4, 4, blocks, objectives, lasers);

        // Verificar que los bloques, láseres y objetivos se cargaron correctamente.
        Assert.assertTrue(board.getBlocks().get(new Position(1, 1)) instanceof NotBlock);
        Assert.assertNotNull(board.getLasers().get(new Pair(new Position(2, 5), Direction.SW)));
        Assert.assertNotNull(board.getObjectives().get(new Position(6, 3)));
        Assert.assertNotNull(board.getObjectives().get(new Position(8, 7)));

        board.moveAllLaser();
        board.moveAllLaser();
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(2, 5), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.W, board.getLasers().get(new Pair(new Position(3, 4), Direction.W)).getDirection());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(3, 2), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(4, 1), Direction.SW)).getDirection());
    }

    @Test
    public void testLoadBoardLevel6() throws IOException {
        FileLoader fileLoader = new FileLoader();
        Map<Position, Block> blocks = fileLoader.loadBlocks("src/test/resources/level6.dat");
        Map<Pair, Laser> lasers = fileLoader.loadLasers("src/test/resources/level6.dat");
        Map<Position, Objective> objectives = fileLoader.loadObjectives("src/test/resources/level6.dat");

        Board board = new Board(4, 4, blocks, objectives, lasers);
        // Verificar que los bloques, láseres y objetivos se cargaron correctamente
        Assert.assertTrue(board.getBlocks().get(new Position(1, 1)) instanceof MirrorBlock);
        // Origenes
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(2, 7), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(3, 0), Direction.SE)).getDirection());
        // Primer move lasers
        board.moveAllLaser();
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(3, 6), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(4, 1), Direction.SE)).getDirection());
        // Segundo move lasers
        board.moveAllLaser();
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(4, 5), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(5, 2), Direction.SE)).getDirection());
        // Tercer move lasers
        board.moveAllLaser();
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(5, 4), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(6, 3), Direction.SE)).getDirection());
        // Caso de colision con anterior y cuarto move lasers
        board.moveAllLaser();
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(6, 3), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(7, 4), Direction.SE)).getDirection());
        // Quintos lasers bro
        board.moveAllLaser();
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(7, 2), Direction.SE)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(8, 5), Direction.SE)).getDirection());

        // Sexto y ultimo move lasers
        board.moveAllLaser();
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(8, 3), Direction.SE)).getDirection());
        Assert.assertEquals(13, board.getLasers().size());
        Assert.assertTrue(board.getObjectives().get(new Position(6, 1)) != null);
    }
}