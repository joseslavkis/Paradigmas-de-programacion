package org.example;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.DragEvent;
import logic.Position;
import logic.blocks.Block;

public class NodeBlock extends Pane {
    private final Block block;
    private Position position;
    private final ImageView blockImageView;

    public NodeBlock(Block block, Position position, Image image) {
        this.block = block;
        this.position = position;
        this.blockImageView = new ImageView(image);
        getChildren().add(blockImageView);
        setupDragAndDrop();
        setPosition(blockImageView, position);
    }

    private void setupDragAndDrop() {
        setOnDragDetected((MouseEvent event) -> {
            Dragboard db = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            System.out.println(event.getX());
            System.out.println(event.getY());
            content.putString(position.getRow() + "," + position.getColumn());
            db.setContent(content);
            event.consume();
        });

        setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String[] positions = db.getString().split(",");
                Position newPos = new Position(Integer.parseInt(positions[0]), Integer.parseInt(positions[1]));
                success = handleMove(newPos);
                if (success) {
                    position = newPos;
                    setPosition(blockImageView, position);
                }
                event.setDropCompleted(success);
            }
            event.consume();
        });
    }

    public boolean handleMove(Position newPos) {
        return true;
    }

    private void setPosition(ImageView imageView, Position position) {
        imageView.setLayoutX(position.getColumn() * 58);
        imageView.setLayoutY(position.getRow() * 58);
    }

    public Position getPosition() {
        return position;
    }

    public Block getBlock() {
        return block;
    }
}
