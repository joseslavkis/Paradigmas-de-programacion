package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class App extends Application {
    private Scene scene;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Game Levels");
        Scene scene = new Adapter(new VBox(), 58).setGUI();

        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Lasers");

        stage.show();
    }

}