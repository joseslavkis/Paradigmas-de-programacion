package org.example;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import logic.Board;
import logic.Position;

public class NodeBlock extends Pane {
    private Position position;
    private final ImageView blockImageView;
    private final Adapter adapter;

    public NodeBlock(Position position, Image image, Adapter adapter) {
        this.position = position;
        this.blockImageView = new ImageView(image);
        this.adapter = adapter;
        getChildren().add(blockImageView);
        setupDragAndDrop();
    }

    private void setupDragAndDrop() {
        setOnDragDetected(this::handleDragDetected);
        setOnDragOver(this::handleDragOver);
        setOnDragDropped(this::handleDragDropped);
    }

    private void handleDragDetected(MouseEvent event) {
        Dragboard db = startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(position.getRow() + "," + position.getColumn());
        db.setContent(content);
        event.consume();
    }

    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != this && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    private void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            String[] positions = db.getString().split(",");
            Position originalPosition = new Position(
                    Integer.parseInt(positions[0]),
                    Integer.parseInt(positions[1])
            );

            Position newPosition = new Position(
                    (int) (getLayoutY() / adapter.getMultiplier()),
                    (int) (getLayoutX() / adapter.getMultiplier())
            );

            Board currentBoard = adapter.getBoard();
            currentBoard.moveBlock(originalPosition, newPosition);

            this.position = newPosition;
            success = true;
            currentBoard.resetLasers();
        }

        event.setDropCompleted(success);
        adapter.moveLasers();
        event.consume();
    }

    public Position getPosition() {
        return position;
    }
}