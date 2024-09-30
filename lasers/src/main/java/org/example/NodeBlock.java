package org.example;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.DragEvent;
import logic.Board;
import logic.Position;
import logic.blocks.Block;

public class NodeBlock extends Pane {
    private final Block block;
    private Position position;
    private final ImageView blockImageView;
    private final Adapter adapter;

    public NodeBlock(Block block, Position position, Image image, Adapter adapter) {
        this.block = block;
        this.position = position;
        this.blockImageView = new ImageView(image);
        this.adapter = adapter;
        getChildren().add(blockImageView);
        setupDragAndDrop();
        setPosition(blockImageView, position);
    }

    private void setupDragAndDrop() {
        setOnDragDetected((MouseEvent event) -> {
            Dragboard db = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(position.getRow() + "," + position.getColumn());
            db.setContent(content);
            event.consume();

            System.out.println("Dragging from: " + position.getRow() + ", " + position.getColumn());
        });

        setOnDragOver((DragEvent event) -> {
            if (event.getGestureSource() != this && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                String[] positions = db.getString().split(",");
                Position originalPosition = new Position(
                        Integer.parseInt(positions[0]),
                        Integer.parseInt(positions[1])
                );

                Position newPosition = new Position((int)(blockImageView.getLayoutY() / 58), (int)(blockImageView.getLayoutX() / 58)); // TODO: get multiplier from constructor

                Board currentBoard = adapter.getBoard();
                currentBoard.moveBlock(originalPosition, newPosition);


                this.position = newPosition;
                setPosition(this.blockImageView, this.position);

                NodeBlock targetBlock = (NodeBlock) event.getGestureSource();
                if (targetBlock != null) {
                    targetBlock.position = originalPosition;
                    setPosition(targetBlock.blockImageView, targetBlock.position);
                }

                success = true;
                currentBoard.resetLasers();
            }

            event.setDropCompleted(success);
            event.consume();

            System.out.println("Dropped to: " + this.position.getRow() + ", " + this.position.getColumn());
            adapter.moveLasers();

        });


    }

    private void setPosition(ImageView imageView, Position position) {
        imageView.setLayoutX(position.getColumn()*58);
        imageView.setLayoutY(position.getRow()*58);
    }

    public Position getPosition() {
        return position;
    }

    public Block getBlock() {
        return block;
    }
}