package org.example;

import logic.Laser;
import logic.Objective;
import logic.Position;
import logic.blocks.*;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.util.Map;

public class FileLoaderTest {

    @Test
    public void testLoadBlocks() throws IOException {
        FileLoader fileLoader = new FileLoader();
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
    public void testLoadLasers() throws IOException {
        FileLoader fileLoader = new FileLoader();
        Map<Position, Laser> lasers = fileLoader.loadLasers("src/test/resources/test_level.dat");

        // Verificar que los láseres se cargaron correctamente
        Assert.assertEquals(1, lasers.size());
        Assert.assertNotNull(lasers.get(new Position(0, 5)));
    }

    @Test
    public void testLoadObjetives() throws IOException {
        FileLoader fileLoader = new FileLoader();
        Map<Position, Objective> objetives = fileLoader.loadObjectives("src/test/resources/test_level.dat");

        // Verificar que los láseres se cargaron correctamente
        Assert.assertEquals(1, objetives.size());
        Assert.assertNotNull(objetives.get(new Position(4, 3)));

        Map<Position, Objective> objetives2 = fileLoader.loadObjectives("src/test/resources/level6.dat");

        Assert.assertEquals(4, objetives2.size());
        Assert.assertNotNull(objetives2.get(new Position(1, 6)));
        Assert.assertNotNull(objetives2.get(new Position(3, 4)));
        Assert.assertNotNull(objetives2.get(new Position(5, 4)));
        Assert.assertNotNull(objetives2.get(new Position(8, 5)));
    }

}
