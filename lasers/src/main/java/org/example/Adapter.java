package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import logic.*;
import logic.blocks.Block;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Adapter {
    private final int multiplier;
    private final VBox mainArea;
    private Board board;
    private final HashMap<String, String> imagePathMap = new HashMap<>();

    {
        imagePathMap.put("objective", "objectives/objective.png");
        imagePathMap.put("impacted", "objectives/impacted_objective.png");
        imagePathMap.put("crystal", "blocks/crystal_block.png");
        imagePathMap.put("empty", "blocks/empty_block.png");
        imagePathMap.put("mirror", "blocks/mirror_block.png");
        imagePathMap.put("glass", "blocks/glass_block.png");
        imagePathMap.put("not", "blocks/not_block.png");
        imagePathMap.put("fixed_opaque", "blocks/fixed_opaque_block.png");
        imagePathMap.put("mobile_opaque", "blocks/mobile_opaque_block.png");
        imagePathMap.put("n", "lasers/laser_n.png");
        imagePathMap.put("e", "lasers/laser_e.png");
        imagePathMap.put("w", "lasers/laser_w.png");
        imagePathMap.put("s", "lasers/laser_s.png");
        imagePathMap.put("ne", "lasers/laser_ne.png");
        imagePathMap.put("nw", "lasers/laser_nw.png");
        imagePathMap.put("se", "lasers/laser_se.png");
        imagePathMap.put("sw", "lasers/laser_sw.png");
    }

    public Adapter(VBox mainArea, int multiplier) {
        this.mainArea = mainArea;
        this.multiplier = multiplier;
    }

    public Scene setGUI() {
        VBox levelsBox = createLevelsBox();
        mainArea.setSpacing(5);
        HBox root = new HBox();
        root.setSpacing(20);
        root.getChildren().addAll(levelsBox, mainArea);
        return new Scene(root, 750, 800);
    }

    private VBox createLevelsBox() {
        VBox levelsBox = new VBox();
        for (int i = 1; i <= 6; i++) {
            Button levelButton = new Button("Level " + i);
            levelButton.setPrefWidth(150);
            levelButton.setPrefHeight(150);
            int level = i;
            levelButton.setOnAction(e -> loadLevel(level));
            levelsBox.getChildren().add(levelButton);
        }
        return levelsBox;
    }

    public Board getBoard() {
        return board;
    }

    private void loadLevel(int level) {
        try {
            String filePath = getLevelFilePath(level);
            FileLoader fileLoader = new FileLoader();
            Map<Position, Block> blocks = fileLoader.loadBlocks(filePath);
            Map<Position, Objective> objectives = fileLoader.loadObjectives(filePath);
            Map<Pair, Laser> lasers = fileLoader.loadLasers(filePath);
            Map<Pair, Laser> primitiveLasers = new HashMap<>(lasers);
            int row = fileLoader.getRowCount(filePath);
            int col = fileLoader.getColumnCount(filePath);

            this.board = new Board(row, col, blocks, objectives, lasers, primitiveLasers);
            updateMainArea();
            moveLasers();
        } catch (FileNotFoundException e) {
            showError("Unexpected error: " + e.getMessage());
        } catch (IOException e) {
            showError("Error loading level: " + e.getMessage());
        } catch (Exception e) {
            showError("Unexpected error: " + e.getMessage());
        }
    }

    private String getLevelFilePath(int level) {
        return "src/main/java/org/example/levels/level" + level + ".dat";
    }

    private void showError(String message) {
        System.err.println(message);
    }

    private void updateMainArea() {
        mainArea.getChildren().clear();
        mainArea.setAlignment(Pos.TOP_CENTER);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        Pane overlayPane = new Pane();
        overlayPane.setPickOnBounds(false);

        updateBlocks(gridPane);
        updateObjectives(overlayPane);
        updateLasers(overlayPane);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(gridPane, overlayPane);

        mainArea.getChildren().add(stackPane);
    }

    private void updateBlocks(GridPane gridPane) {
        board.getBlocks().forEach((position, block) -> {
            Image image = getElementImage(block.getType().name().toLowerCase());
            NodeBlock currentBlock = new NodeBlock(position, image, this);
            gridPane.add(currentBlock, position.getColumn(), position.getRow());
        });
    }

    private void updateObjectives(Pane overlayPane) {
        board.getObjectives().forEach((position, objective) -> {
            ImageView objectiveImageView = new ImageView(getElementImage("objective"));
            setPosition(objectiveImageView, position);
            overlayPane.getChildren().add(objectiveImageView);
        });
    }

    private void updateLasers(Pane overlayPane) {
        board.getLasers().forEach((pair, laser) -> {
            ImageView laserImageView = new ImageView(getElementImage(laser.getDirection().name().toLowerCase()));
            setPosition(laserImageView, pair.getPosition());
            overlayPane.getChildren().add(laserImageView);
        });
    }

    private void setPosition(ImageView imageView, Position position) {
        int x = position.getColumn() * multiplier;
        int y = position.getRow() * multiplier;
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
    }

    private Image getElementImage(String request) {
        if (Objects.equals(request, "static")) return null;
        String currentPathImage = imagePathMap.get(request);
        try (FileInputStream input = new FileInputStream("src/main/java/org/example/pngs/" + currentPathImage)) {
            return new Image(input);
        } catch (IOException e) {
            showError("Unexpected error: " + e.getMessage());
            return null;
        }
    }

    public void moveLasers() {
        if (board != null) {
            int i = 0;
            while (i < board.getRow() * 3) {
                board.moveAllLaser();
                i++;
            }
            updateMainArea();
        }
    }
}