package org.example;

import logic.*;
import logic.blocks.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class FileLoader {
    private final Map<Character, Block> converter = Map.of(
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

    private Position generateCurrentPosition(String row, String column) {
        return new Position(Integer.parseInt(row), Integer.parseInt(column));
    }

    private void addBlock(int row, int column, Character blockChar, Map<Position, Block> blocks) {
        try {
            Block block = converter.get(blockChar);
            Position center = new Position(row, column);
            blocks.put(center, block);
        } catch (InvalidParameterException e) {
            System.err.println("Invalid block character: " + blockChar);
        }
    }

    private void addLaser(String row, String column, String direction, Map<Pair, Laser> lasers, String line) {
        try {
            Position currentPosition = generateCurrentPosition(row, column);
            Direction currentDirection = Direction.valueOf(direction);
            Pair center = new Pair(currentPosition, currentDirection);
            Laser laser = new Laser(currentDirection);
            lasers.put(center, laser);
        } catch (NumberFormatException e) {
            System.err.println("Invalid laser line: " + line);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid direction in laser line: " + line);
        }
    }

    private void addObjective(String row, String column, Map<Position, Objective> objectives, String line) {
        try {
            Position currentPosition = generateCurrentPosition(row, column);
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
                    addBlock(row, col, line.charAt(i), blocks);
                    col += 2;
                }
                row += 2;
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
                        addLaser(parts[2], parts[1], parts[3], lasers, line);
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
                    String[] separatedParts = line.split(" ");
                    if (separatedParts.length == 3) {
                        addObjective(separatedParts[2], separatedParts[1], objectives, line);
                    }
                }
            }
        }
        return objectives;
    }

}