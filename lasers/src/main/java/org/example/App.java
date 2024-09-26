package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.*;
import logic.blocks.Block;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    private VBox mainArea;
    private Board board;

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

            Board board = new Board(4, 4, blocks, objectives, lasers, primitiveLasers);
            updateMainArea(blocks);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading level: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unexpected error: " + e.getMessage());
        }
    }

    private String getLevelFilePath(int level) {
        return "src/main/java/org/example/levels/level" + level + ".dat";
    }

    private void showError(String message) {
        System.err.println(message);
    }

    private void updateMainArea(Map<Position, Block> blocks) {
        mainArea.getChildren().clear();
        for (Map.Entry<Position, Block> entry : blocks.entrySet()) {
            Image blockImage = getBlockImage(entry.getValue());
            if (blockImage != null) {
                ImageView imageView = new ImageView(blockImage);
                mainArea.getChildren().add(imageView);
            }
        }
    }

    private Image getBlockImage(Block block) {
        String imageName = block.getType().name().toLowerCase() + "_block.png";
        try (FileInputStream input = new FileInputStream("src/main/java/org/example/pngs/" + imageName)) {
            return new Image(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}