package org.example;

import com.sun.source.tree.AssertTree;
import logic.*;
import logic.blocks.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BoardTest {

    @Test
    public void testMoveBlock() {
        Map<Position, Block> blocks = new HashMap<>();
        blocks.put(new Position(1, 1), new FixedOpaqueBlock());
        blocks.put(new Position(1, 3), new MobileOpaqueBlock());

        Board board = new Board(5, 5, blocks, new HashMap<>(), new HashMap<>(), new HashMap<>());
        board.moveBlock(new Position(1, 1), new Position(1, 3));

        Assert.assertTrue(board.getBlocks().get(new Position(1, 1)) instanceof FixedOpaqueBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(1, 3)) instanceof MobileOpaqueBlock);
    }

    @Test
    public void testMoveLaserOpaqueBlocks() throws Exception {
        Map<Position, Block> blocks = new HashMap<>();
        blocks.put(new Position(1, 3), new MobileOpaqueBlock());

        Map<Pair, Laser> lasers = new HashMap<>();
        lasers.put(new Pair(new Position(0, 1), Direction.SE), new Laser(Direction.SE));

        Board board = new Board(5, 5, blocks, new HashMap<>(), lasers, lasers);
        board.moveAllLaser();

        // Verificar que el láser se haya movido correctamente
        Assert.assertEquals(Direction.STATIC, board.getLasers().get(new Pair(new Position(1, 2), Direction.STATIC)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(0, 1), Direction.SE)).getDirection());
        Assert.assertEquals(2, board.getLasers().size());
    }

    @Test
    public void testMoveLaser() throws Exception {
        Map<Position, Block> blocks = new HashMap<>();
        blocks.put(new Position(1, 3), new EmptyBlock());
        blocks.put(new Position(3, 3), new CrystalBlock());
        blocks.put(new Position(5, 5), new MirrorBlock());

        Map<Pair, Laser> lasers = new HashMap<>();
        lasers.put(new Pair(new Position(0, 1), Direction.SE), new Laser(Direction.SE));

        Board board = new Board(5, 5, blocks, new HashMap<>(), lasers, lasers);
        int i = 0;
        while(i < board.getRow()*3) {
            board.moveAllLaser();
            i++;
        }

        // Verificar que el láser se haya movido correctamente
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(1, 2), Direction.SE)).getDirection());
        Assert.assertEquals(Direction.S, board.getLasers().get(new Pair(new Position(2, 3), Direction.S)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(4, 3), Direction.SE)).getDirection());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(5, 4), Direction.SW)).getDirection());
    }
    @Test
    public void testLoadBoardLevel4() throws IOException {
        FileLoader fileLoader = new FileLoader();
        Map<Position, Block> blocks = fileLoader.loadBlocks("src/test/resources/level4.dat");
        Map<Pair, Laser> lasers = fileLoader.loadLasers("src/test/resources/level4.dat");
        Map<Position, Objective> objectives = fileLoader.loadObjectives("src/test/resources/level4.dat");
        Map<Pair, Laser> primitiveLasers = new HashMap<>(lasers);


        Board board = new Board(4, 4, blocks, objectives, lasers, primitiveLasers);

        //Blocks
        Assert.assertTrue(board.getBlocks().get(new Position(1, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(1, 3)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(1, 5)) instanceof MirrorBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(1, 7)) instanceof GlassBlock);

        Assert.assertTrue(board.getBlocks().get(new Position(3, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(3, 3)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(3, 5)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(3, 7)) instanceof MirrorBlock);

        Assert.assertTrue(board.getBlocks().get(new Position(5, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(5, 3)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(5, 5)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(5, 7)) instanceof EmptyBlock);

        Assert.assertTrue(board.getBlocks().get(new Position(7, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(7, 3)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(7, 5)) instanceof FixedOpaqueBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(7, 7)) instanceof EmptyBlock);
        //Objectives
        Assert.assertNotNull(board.getObjectives().get(new Position(3, 2)));
        Assert.assertNotNull(board.getObjectives().get(new Position(4, 3)));
        Assert.assertNotNull(board.getObjectives().get(new Position(5, 4)));
        Assert.assertNotNull(board.getObjectives().get(new Position(6, 5)));
        Assert.assertNotNull(board.getObjectives().get(new Position(7, 6)));
        //Lasers
        int i = 0;
        while(i < board.getRow()*4) {
            board.moveAllLaser();
            i++;
        }
        //Assert.assertEquals(9, board.getLasers().size());
        // Origenes
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(7, 8), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(6, 7), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(5, 6), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(4, 5), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(3, 4), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(2, 3), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(1, 2), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(0, 1), Direction.NW)).getDirection());

        board.moveBlock(new Position(1, 5), new Position(1, 3));
        board.resetLasers();
        //chequear que se movio el bloque
        Assert.assertTrue(board.getBlocks().get(new Position(1, 5)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(1, 3)) instanceof MirrorBlock);
        //chequear los otros blques
        Assert.assertTrue(board.getBlocks().get(new Position(1, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(1, 7)) instanceof GlassBlock);

        Assert.assertTrue(board.getBlocks().get(new Position(3, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(3, 3)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(3, 5)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(3, 7)) instanceof MirrorBlock);

        Assert.assertTrue(board.getBlocks().get(new Position(5, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(5, 3)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(5, 5)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(5, 7)) instanceof EmptyBlock);

        Assert.assertTrue(board.getBlocks().get(new Position(7, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(7, 3)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(7, 5)) instanceof FixedOpaqueBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(7, 7)) instanceof EmptyBlock);
        int j = 0;
        while(j < board.getRow()*3) {
            board.moveAllLaser();
            j++;
        }
        //chequear que se resetearon los lasers
        Assert.assertEquals(9, board.getLasers().size());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(5, 0), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(4, 1), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(3, 2), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(2, 3), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(3, 4), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(4, 5), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(5, 6), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(6, 7), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(7, 8), Direction.NW)).getDirection());

        board.moveBlock(new Position(3, 7), new Position(3, 1));
        board.resetLasers();

        int k = 0;
        while(k < board.getRow()*3) {
            board.moveAllLaser();
            k++;
        }

        //chequear que se movio el bloque
        Assert.assertTrue(board.getBlocks().get(new Position(3, 7)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(3, 1)) instanceof MirrorBlock);

        //chequear demas bloques
        Assert.assertTrue(board.getBlocks().get(new Position(1, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(1, 3)) instanceof MirrorBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(1, 5)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(1, 7)) instanceof GlassBlock);

        Assert.assertTrue(board.getBlocks().get(new Position(3, 3)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(3, 5)) instanceof EmptyBlock);

        Assert.assertTrue(board.getBlocks().get(new Position(5, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(5, 3)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(5, 5)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(5, 7)) instanceof EmptyBlock);

        Assert.assertTrue(board.getBlocks().get(new Position(7, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(7, 3)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(7, 5)) instanceof FixedOpaqueBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(7, 7)) instanceof EmptyBlock);

        //chequear que se resetearon los lasers
        Assert.assertEquals(10, board.getLasers().size());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(2, 3), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(3, 4), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(4, 5), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(5, 6), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(6, 7), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(7, 8), Direction.NW)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(3, 2), Direction.SE)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(4, 3), Direction.SE)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(5, 4), Direction.SE)).getDirection());
        Assert.assertEquals(Direction.STATIC, board.getLasers().get(new Pair(new Position(6, 5), Direction.STATIC)).getDirection());
        Assert.assertNull(board.getLasers().get(new Pair(new Position(7, 6), Direction.SE)));

        board.moveBlock(new Position(1, 7), new Position(5, 7));
        board.resetLasers();

        int w = 0;
        while(w < board.getRow()*3) {
            board.moveAllLaser();
            w++;
        }

        //chequear demas bloques
        Assert.assertTrue(board.getBlocks().get(new Position(1, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(1, 3)) instanceof MirrorBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(1, 5)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(1, 7)) instanceof EmptyBlock);

        Assert.assertTrue(board.getBlocks().get(new Position(3, 3)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(3, 5)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(3, 7)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(3, 1)) instanceof MirrorBlock);

        Assert.assertTrue(board.getBlocks().get(new Position(5, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(5, 3)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(5, 5)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(5, 7)) instanceof GlassBlock);

        Assert.assertTrue(board.getBlocks().get(new Position(7, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(7, 3)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(7, 5)) instanceof FixedOpaqueBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(7, 7)) instanceof EmptyBlock);

        Assert.assertEquals(12, board.getLasers().size());
        Assert.assertEquals(Direction.NW, board.getLasers().get(new Pair(new Position(6, 7), Direction.NW)).getDirection());

        Assert.assertTrue(board.isWin());
    }

    @Test
    public void testLoadBoardLevel5() throws IOException {
        FileLoader fileLoader = new FileLoader();
        Map<Position, Block> blocks = fileLoader.loadBlocks("src/test/resources/level5.dat");
        Map<Pair, Laser> lasers = fileLoader.loadLasers("src/test/resources/level5.dat");
        Map<Pair, Laser> primitiveLasers = new HashMap<>(lasers);
        Map<Position, Objective> objectives = fileLoader.loadObjectives("src/test/resources/level5.dat");

        Board board = new Board(4, 4, blocks, objectives, lasers, primitiveLasers);
        // Verificar que los bloques, láseres y objetivos se cargaron correctamente
        Assert.assertTrue(board.getBlocks().get(new Position(1, 1)) instanceof NotBlock);
        Assert.assertNotNull(board.getLasers().get(new Pair(new Position(2, 5), Direction.SW)));
        Assert.assertNotNull(board.getObjectives().get(new Position(6, 3)));
        Assert.assertNotNull(board.getObjectives().get(new Position(8, 7)));
        int j = 0;
        while(j < 6) {
            board.moveAllLaser();
            j++;
        }
        Assert.assertEquals(6, board.getLasers().size());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(2, 5), Direction.SW)).getDirection());
        // Crystal Block
        Assert.assertEquals(Direction.W, board.getLasers().get(new Pair(new Position(3, 4), Direction.W)).getDirection());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(3, 2), Direction.SW)).getDirection());

        //Assert.assertEquals(Direction.E, board.getLasers().get(new Pair(new Position(3, 2), Direction.E)).getDirection());
        // Glass Block
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(4, 1), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(5, 0), Direction.SW)).getDirection());

        board.moveBlock(new Position(5, 1), new Position(5, 3));
        board.resetLasers();

        Assert.assertTrue(board.getBlocks().get(new Position(5, 1)) instanceof EmptyBlock);
        Assert.assertTrue(board.getBlocks().get(new Position(3, 3)) instanceof CrystalBlock);


        int i = 0;
        while(i < 3) {
            board.moveAllLaser();
            i++;
        }

        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(2, 5), Direction.SW)).getDirection());
        // Crystal Block
        Assert.assertEquals(Direction.W, board.getLasers().get(new Pair(new Position(3, 4), Direction.W)).getDirection());
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(3, 2), Direction.SW)).getDirection());
        // Empty Block
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(5, 0), Direction.SW)).getDirection());
        Assert.assertNull(board.getLasers().get(new Pair(new Position(3, 0), Direction.NW)));
    }

    @Test
    public void testLoadBoardLevel6() throws IOException {
        FileLoader fileLoader = new FileLoader();
        Map<Position, Block> blocks = fileLoader.loadBlocks("src/test/resources/level6.dat");
        Map<Pair, Laser> lasers = fileLoader.loadLasers("src/test/resources/level6.dat");
        Map<Position, Objective> objectives = fileLoader.loadObjectives("src/test/resources/level6.dat");

        Board board = new Board(4, 4, blocks, objectives, lasers, lasers);
        // Verificar que los bloques, láseres y objetivos se cargaron correctamente
        Assert.assertTrue(board.getBlocks().get(new Position(1, 1)) instanceof MirrorBlock);
        int i = 0;
        while(i < board.getRow()*3) {
            board.moveAllLaser();
            i++;
        }

        // Origenes
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(2, 7), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(3, 0), Direction.SE)).getDirection());
        // Primer move lasers
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(3, 6), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(4, 1), Direction.SE)).getDirection());
        // Segundo move lasers
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(4, 5), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(5, 2), Direction.SE)).getDirection());
        // Tercer move lasers
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(5, 4), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(6, 3), Direction.SE)).getDirection());
        // Caso de colision con anterior y cuarto move lasers
        Assert.assertEquals(Direction.SW, board.getLasers().get(new Pair(new Position(6, 3), Direction.SW)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(7, 4), Direction.SE)).getDirection());
        // Quintos lasers bro
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(7, 2), Direction.SE)).getDirection());
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(8, 5), Direction.SE)).getDirection());
        // Sexto y ultimo move lasers
        Assert.assertEquals(Direction.SE, board.getLasers().get(new Pair(new Position(8, 3), Direction.SE)).getDirection());
        Assert.assertEquals(13, board.getLasers().size());
        Assert.assertTrue(board.getObjectives().get(new Position(6, 1)) != null);
    }
}