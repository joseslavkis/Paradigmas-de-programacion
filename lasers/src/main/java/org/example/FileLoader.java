package org.example;

import logic.*;
import logic.blocks.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileLoader {
    public static final char SPACE = ' ';
    public Map<Position, Block> loadBlocks(String filePath) throws IOException {
        Map<Position, Block> blocks = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int row = 1;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                int col = 1;
                for (int i = 0; i < line.length(); i++) {
                    char blockChar = line.charAt(i);
                    Block block;
                    switch (blockChar) {
                        case SPACE:
                            block = new NotBlock();
                            break;
                        case '.':
                            block = new EmptyBlock();
                            break;
                        case 'F':
                            block = new FixedOpaqueBlock();
                            break;
                        case 'B':
                            block = new MobileOpaqueBlock();
                            break;
                        case 'R':
                            block = new MirrorBlock();
                            break;
                        case 'G':
                            block = new GlassBlock();
                            break;
                        case 'C':
                            block = new CrystalBlock();
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid block character: " + blockChar);
                    }
                    Position center = new Position(row, col);
                    blocks.put(center, block);
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
                        try {
                            int row = Integer.parseInt(parts[2]);
                            int col = Integer.parseInt(parts[1]);
                            Direction direction = Direction.valueOf(parts[3]);
                            Pair center = new Pair(new Position(row, col), direction);
                            Laser laser = new Laser(direction);
                            lasers.put(center, laser);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid laser line: " + line);
                        }
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
                        try {
                            int row = Integer.parseInt(parts[2]);
                            int col = Integer.parseInt(parts[1]);
                            Position position = new Position(row, col);
                            Objective objective = new Objective(position);
                            objectives.put(position, objective);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid objective line: " + line);
                        }
                    }
                }
            }
        }
        return objectives;
    }
}