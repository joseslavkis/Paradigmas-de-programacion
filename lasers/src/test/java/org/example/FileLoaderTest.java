package org.example;

import logic.*;
import logic.blocks.*;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.util.Map;

public class FileLoaderTest {
    private final FileLoader fileLoader = new FileLoader();

    @Test
    public void testLoadBlocks() throws IOException {
        Map<Position, Block> blocks1 = fileLoader.loadBlocks("src/test/resources/test_level.dat");

        // Verificar que los bloques se cargaron correctamente
        Assert.assertTrue(blocks1.get(new Position(1, 1)) instanceof FixedOpaqueBlock);
        Assert.assertTrue(blocks1.get(new Position(1, 7)) instanceof MobileOpaqueBlock);
        Assert.assertTrue(blocks1.get(new Position(7, 5)) instanceof NotBlock);
        Assert.assertTrue(blocks1.get(new Position(5, 1)) instanceof EmptyBlock);
        Assert.assertTrue(blocks1.get(new Position(7, 1)) instanceof CrystalBlock);

        FileLoader fileLoader1 = new FileLoader();
        Map<Position, Block> blocks2 = fileLoader1.loadBlocks("src/test/resources/level6.dat");

        Assert.assertTrue(blocks2.get(new Position(1, 1)) instanceof MirrorBlock);
        Assert.assertFalse(blocks2.get(new Position(1, 7)) instanceof GlassBlock);
    }

    @Test
    public void testGetRowCount() throws IOException {
        String filePath = "src/test/resources/level1.dat";

        int expectedRowCount = 6;
        int actualRowCount = fileLoader.getRowCount(filePath);

        Assert.assertEquals(expectedRowCount, actualRowCount);
    }

    @Test
    public void testGetColumnCount() throws IOException {
        String filePath = "src/test/resources/level1.dat";

        int expectedColumnCount = 4;
        int actualColumnCount = fileLoader.getColumnCount(filePath);

        Assert.assertEquals(expectedColumnCount, actualColumnCount);
    }

    @Test
    public void testLoadLasers() throws IOException {
        Map<Pair, Laser> lasers = fileLoader.loadLasers("src/test/resources/level6.dat");

        // Verificar que los láseres se cargaron correctamente
        Assert.assertEquals(2, lasers.size());
        Assert.assertEquals(Direction.SE, lasers.get(new Pair(new Position(3, 0), Direction.SE)).getDirection());
    }

    @Test
    public void testLoadObjectives() throws IOException {
        Map<Position, Objective> objectives = fileLoader.loadObjectives("src/test/resources/test_level.dat");

        // Verificar que los láseres se cargaron correctamente
        Assert.assertEquals(1, objectives.size());
        Assert.assertNotNull(objectives.get(new Position(3, 4)));

        Map<Position, Objective> objectives2 = fileLoader.loadObjectives("src/test/resources/level6.dat");
        Assert.assertEquals(4, objectives2.size());
        Assert.assertNotNull(objectives2.get(new Position(6, 1)));
        Assert.assertNotNull(objectives2.get(new Position(4, 3)));
        Assert.assertNotNull(objectives2.get(new Position(4, 5)));
        Assert.assertNotNull(objectives2.get(new Position(5, 8)));
    }
}
