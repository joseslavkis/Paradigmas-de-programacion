package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.*;
import logic.blocks.Block;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class App extends Application {

    private VBox mainArea;
    private Board board;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Game Levels");

        VBox levelsBox = createLevelsBox();
        mainArea = new VBox();
        mainArea.setSpacing(5);

        HBox root = new HBox();
        root.setSpacing(20);
        root.getChildren().addAll(levelsBox, mainArea);

        Scene scene = new Scene(root, 500, 450);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createLevelsBox() {
        VBox levelsBox = new VBox();
        for (int i = 1; i <= 6; i++) {
            Button levelButton = new Button("Level " + i);
            levelButton.setPrefWidth(100);
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
            Map<Pair, Laser> lasers = fileLoader.loadLasers(filePath);
            Map<Position, Objective> objectives = fileLoader.loadObjectives(filePath);
            Map<Pair, Laser> primitiveLasers = new HashMap<>(lasers);
            int row = fileLoader.getRowCount(filePath);
            int col = fileLoader.getColumnCount(filePath);

            Board board = new Board(row, col, blocks, objectives, lasers, primitiveLasers);
            updateMainArea(blocks, row, col);
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

    private void updateMainArea(Map<Position, Block> blocks, int row, int col) {
        mainArea.getChildren().clear();
        TilePane tilePane1 = new TilePane();
        tilePane1.setPrefColumns(col);
        tilePane1.setPrefRows(row);
        tilePane1.setTileAlignment( Pos.CENTER );

        mainArea.getChildren().clear();
        TilePane tilePane2 = new TilePane();
        tilePane2.setPrefColumns(col);
        tilePane2.setPrefRows(row);
        tilePane2.setTileAlignment( Pos.CENTER );

        Set<Map.Entry<Position, Block>> entrySet = blocks.entrySet();
        List<Map.Entry<Position, Block>> sortedList = entrySet.stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());

        for (Map.Entry<Position, Block> positionBlockEntry : sortedList) {
            Image blockImage = getBlockImage(positionBlockEntry.getValue());
            if (blockImage != null) {
                ImageView imageView = new ImageView(blockImage);
                tilePane1.getChildren().add(imageView);
            }
        }

//        Set<Map.Entry<Position, Block>> entrySetObjectives = blocks.entrySet();
//        List<Map.Entry<Position, Block>> sortedListObjectives = entrySet.stream()
//                .sorted(Map.Entry.comparingByKey())
//                .collect(Collectors.toList());
//
//        for (Map.Entry<Position, Block> positionObjectiveEntry : sortedListObjectives) {
//            Image blockImage = getBlockImage(positionObjectiveEntry.getValue());
//            if (blockImage != null) {
//                ImageView imageView = new ImageView(blockImage);
//                tilePane2.getChildren().add(imageView);
//            }
//        }

        mainArea.getChildren().add(tilePane1);
//        mainArea.getChildren().add(tilePane2);

    }


    private Image getBlockImage(Block block) {
        String imageName = block.getType().name().toLowerCase() + "_block.png";
        try (FileInputStream input = new FileInputStream("src/main/java/org/example/pngs/" + imageName)) {
            return new Image(input);
        } catch (FileNotFoundException e) {
            showError("Unexpected error: " + e.getMessage());
            return null;
        } catch (IOException e) {
            showError("Unexpected error: " + e.getMessage());
            return null;
        }
    }

}