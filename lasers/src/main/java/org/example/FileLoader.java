package org.example;

import javafx.geometry.Pos;
import logic.*;
import logic.blocks.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileLoader {
    private Map<Character, Block> converter = Map.of(
        ' ', new NotBlock(),
        '.', new EmptyBlock(),
        'F', new FixedOpaqueBlock(),
        'B', new MobileOpaqueBlock(),
        'R', new MirrorBlock(),
        'G', new GlassBlock(),
        'C', new CrystalBlock()
    );

    public int getRowCount(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int rowCount = 0;
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                rowCount++;
            }
            return rowCount;
        }
    }

    public int getColumnCount(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line != null) {
                return line.length();
            }
        }
        return 0;
    }

    private Block generateBlock(int row, int column, Character blockChar, Map<Position, Block> blocks) {
        try {
            Block block = converter.get(blockChar);
            Position center = new Position(row, column);
            blocks.put(center, block);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid block character: " + blockChar);
        }
    }

    private Position generateCurrentPosition(String row, String column) {
        return new Position(Integer.parseInt(row), Integer.parseInt(column));
    }

    private void generateLaser(String row, String column, String direction, Map<Pair, Laser> lasers, String line) {
        try {
            Position currentPosition = generateCurrentPosition(row, column);
            Direction currentDirection = Direction.valueOf(direction);
            Pair center = new Pair(currentPosition, currentDirection);
            Laser laser = new Laser(currentDirection);
            lasers.put(center, laser);
        } catch (NumberFormatException e) {
            System.err.println("Invalid laser line: " + line);
        }
    }

    private void generateObjective(String row, String column, String direction, Map<Position, Objective> objectives, String line) {
        try {
            Position currentPosition = generateCurrentPosition(row, column);
            Direction currentDirection = Direction.valueOf(direction);
            Objective currentObjective = new Objective(currentPosition);
            objectives.put(currentPosition, currentObjective);
        } catch (NumberFormatException e) {
            System.err.println("Invalid objective line: " + line);
        }
    }

    public Map<Position, Block> loadBlocks(String filePath) throws IOException {
        Map<Position, Block> blocks = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 1;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                int col = 1;
                for (int i = 0; i < line.length(); i++) {
                    generateBlock(row, col, line.charAt(i), blocks);
                    col += 2; // Incrementar en 2 para mantener col impar
                }
                row += 2; // Incrementar en 2 para mantener row impar
            }
        }
        return blocks;
    }

    public Map<Pair, Laser> loadLasers(String filePath) throws IOException {
        Map<Pair, Laser> lasers = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("E")) {
                    String[] parts = line.split(" ");
                    if (parts.length == 4) {
                        generateLaser(parts[2], parts[1], parts[3], lasers, line);
                    } else {
                        System.err.println("Incorrect number of parts in laser line: " + line);
                    }
                }
            }
        }
        return lasers;
    }

    public Map<Position, Objective> loadObjectives(String filePath) throws IOException {
        Map<Position, Objective> objectives = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("G")) {
                    String[] parts = line.split(" ");
                    if (parts.length == 3) {
                        generateObjective(parts[2], parts[1], parts[3], objectives, line);
                    }
                }
            }
        }
        return objectives;
    }

}