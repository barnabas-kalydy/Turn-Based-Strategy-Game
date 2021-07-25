package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;
import com.codecool.dungeoncrawl.logic.drawable.troops.Troop;
import com.codecool.dungeoncrawl.logic.maps.GameMap;
import com.codecool.dungeoncrawl.logic.maps.MapLoader;
import com.codecool.dungeoncrawl.logic.tiles.Tiles;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameLoop {

    Player actualTurnPlayer;

    GameMap map = MapLoader.loadMap();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();


    public void start(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(canvas);

        Scene scene = new Scene(borderPane);
        scene.setOnKeyPressed(this::setKeyEvents);
        scene.setOnMouseClicked(this::setOnMouseClicked);

        primaryStage.setScene(scene);
        primaryStage.show();

        refresh();
    }


    private void setOnMouseClicked(MouseEvent mouseEvent) {
        // getting data about the place where mouse click occured
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        Troop actor = map.getCell((int) x / 32, (int) y / 32).getActor();
        String tileName = map.getCell((int) x / 32, (int) y / 32).getTileName();


        actualTurnPlayer = map.getPlayer(0);
        map.setSelectedTroop(actor, actualTurnPlayer);

        // adding modal window with details of the place where mouse click occured
//        VBox vBoxPane = new VBox();
//        vBoxPane.getChildren().add(new Hyperlink("Click X coordinate: " + x));
//        vBoxPane.getChildren().add(new Hyperlink("Click Y coordinate: " + y));
//        vBoxPane.getChildren().add(new Hyperlink("cell.getActor(): " + actor));
//        vBoxPane.getChildren().add(new Hyperlink("cell.getTileName(): " + tileName));
//
//        Stage dialog = new Stage();
//        dialog.initModality(Modality.APPLICATION_MODAL);
//        dialog.setWidth(600);
//        dialog.setHeight(500);
//        dialog.setScene(new Scene(vBoxPane));
//        dialog.showAndWait();
    }

    private void setKeyEvents(KeyEvent keyEvent) {
        Troop selectedTroop = map.getSelectedTroop();
        if (selectedTroop != null) {
            switch (keyEvent.getCode()) {
                case W -> {
                    if (freeToMove(selectedTroop, 0, -1))
                        selectedTroop.move(0, -1);
                    refresh();
                }
                case S -> {
                    if (freeToMove(selectedTroop, 0, 1))
                        selectedTroop.move(0, 1);
                    refresh();
                }
                case A -> {
                    if (freeToMove(selectedTroop, -1, 0))
                        selectedTroop.move(-1, 0);
                    refresh();
                }
                case D -> {
                    if (freeToMove(selectedTroop, 1, 0))
                        selectedTroop.move(1, 0);
                    refresh();
                }
            }
        }
    }

    private boolean freeToMove(Troop selectedTroop, int xDirection, int yDirection) {
        return selectedTroop.getCell().getNeighbor(xDirection, yDirection).getActor() == null
                && selectedTroop.getCell().getNeighbor(xDirection, yDirection).getTileName() == "floor";
    }

    private void refresh() {
        // filling screen with black
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // redrawing every Tile
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
    }
}
