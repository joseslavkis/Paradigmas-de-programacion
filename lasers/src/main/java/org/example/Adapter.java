package org.example;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import logic.*;
import logic.blocks.Block;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
            updateMainArea(blocks, objectives, primitiveLasers, lasers, row, col);
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

    private void updateMainArea(Map<Position, Block> blocks, Map<Position, Objective> objectives,
                                Map<Pair, Laser> primitive, Map<Pair, Laser> lasers, int row, int col) {
        mainArea.getChildren().clear();
        mainArea.setAlignment(Pos.TOP_CENTER);

        Pane pane = new Pane();
        pane.setPrefSize(col * multiplier, row * multiplier);
        pane.setPadding(new Insets(10));

        updateBlocks(pane, blocks);
        updateObjectives(pane, objectives);
        updateLasers(pane, primitive, lasers);

        mainArea.getChildren().add(pane);
    }


    private void updateBlocks(Pane pane, Map<Position, Block> blocks) {
        pane.getChildren().clear();

        blocks.forEach((position, block) -> {
            Image image = getElementImage(block.getType().name().toLowerCase());
            NodeBlock currentBlock = new NodeBlock(block, position, image);

            System.out.println("Adding block at: " + position.getRow() + ", " + position.getColumn());

            pane.getChildren().add(currentBlock);
        });
    }



    private void updateLasers(Pane pane, Map<Pair, Laser> primitive, Map<Pair, Laser> lasers) {
        primitive.forEach((pair, laser) -> {
            ImageView primitiveImageView = new ImageView(getElementImage("impacted"));
            setPosition(primitiveImageView, pair.getPosition());
            pane.getChildren().add(primitiveImageView);
        });

        lasers.forEach((pair, laser) -> {
            ImageView laserImageView = new ImageView(getElementImage(laser.getDirection().name().toLowerCase()));
            setPosition(laserImageView, pair.getPosition());
            pane.getChildren().add(laserImageView);
        });
    }
    private void updateObjectives(Pane pane, Map<Position, Objective> objectives) {
        objectives.forEach((position, objective) -> {
            ImageView objectiveImageView = new ImageView(getElementImage("objective"));
            setPosition(objectiveImageView, position);
            pane.getChildren().add(objectiveImageView);
        });
    }

    private void setPosition(ImageView imageView, Position position) {
        int x = position.getColumn() * multiplier;
        int y = position.getRow() * multiplier;
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
    }

    private Image getElementImage(String request) {
        String currentPathImage = imagePathMap.get(request);
        try (FileInputStream input = new FileInputStream("src/main/java/org/example/pngs/" + currentPathImage)) {
            return new Image(input);
        } catch (FileNotFoundException e) {
            showError("Unexpected error: " + e.getMessage());
            return null;
        } catch (IOException e) {
            showError("Unexpected error: " + e.getMessage());
            return null;
        }
    }

    private void moveLasers() {
        if (board != null) {
            int i = 0;
            while (i < board.getRow() * 3) {
                board.moveAllLaser();
                i++;
            }
            updateMainArea(board.getBlocks(), board.getObjectives(), board.getPrimitiveLasers(), board.getLasers(), board.getRow(), board.getColumn());
        }
    }

    public Pane getCurrentBoard() {
        for (Node node : mainArea.getChildren()) {
            if (node instanceof Pane) {
                return (Pane) node;
            }
        }
        return null;
    }

    public void makeMove() {
        Pane currentBoard = getCurrentBoard();
        currentBoard.setOnDragDetected((MouseEvent dragEvent) -> {
            Dragboard db = currentBoard.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            db.setContent(content);
            dragEvent.consume();
        });
    }
}
