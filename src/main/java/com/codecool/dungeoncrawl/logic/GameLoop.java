package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;
import com.codecool.dungeoncrawl.logic.drawable.other_entities.Building;
import com.codecool.dungeoncrawl.logic.drawable.troops.Troop;
import com.codecool.dungeoncrawl.logic.maps.GameMap;
import com.codecool.dungeoncrawl.logic.maps.MapLoader;
import com.codecool.dungeoncrawl.logic.tiles.Tiles;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.maven.model.Build;

public class GameLoop {

    Player actualTurnPlayer;

    GameMap map = MapLoader.loadMap();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    VBox rightLogBox;

    public void start(Stage primaryStage) {

        canvas.setOnMouseClicked(this::setOnMouseClicked);

        Button passTurnButton = new Button("Current");
        passTurnButton.setPrefSize(100, 20);
        passTurnButton.setOnMouseClicked(this::passTurn);

        HBox hbox = new HBox();
        hbox.getChildren().add(passTurnButton);

        rightLogBox = new VBox();
        rightLogBox.getChildren().add(new Hyperlink("Log output: "));

        ScrollPane scroll = new ScrollPane();
        scroll.setMinWidth(500);
        scroll.setContent(rightLogBox);

        BorderPane borderPane = new BorderPane();
        borderPane.setOnKeyPressed(this::setKeyEvents);
        borderPane.setCenter(canvas);
        borderPane.setBottom(hbox);
        borderPane.setRight(scroll);

        Scene scene = new Scene(borderPane);

        primaryStage.setScene(scene);
        primaryStage.show();

        actualTurnPlayer = map.getPlayer(0);

        refresh();
    }

    private void passTurn(MouseEvent mouseEvent) {
        if (actualTurnPlayer.equals(map.getPlayer(0)))
            actualTurnPlayer = map.getPlayer(1);
        else
            actualTurnPlayer = map.getPlayer(0);
        map.setSelectedTroopToNull();
    }


    private void setOnMouseClicked(MouseEvent mouseEvent) {
        // getting data about the place where mouse click occured
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        Cell cell = map.getCell((int) x / 32, (int) y / 32);
        Troop actor = cell.getTroop();
        String tileName = cell.getTileName();


        map.setSelectedTroop(actor, actualTurnPlayer);

        logToVBox(String.valueOf(x));
        logToVBox(String.valueOf(y));
        logToVBox(actor.toString());

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
                    else if(canInteractWith(selectedTroop, 0, -1))
                        selectedTroop.interactWith(selectedTroop
                                .getCell()
                                .getNeighbor(0, -1));
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

    private boolean canInteractWith(Troop selectedTroop, int xDirection, int yDirection) {
        Building building = selectedTroop.getCell().getNeighbor(xDirection, yDirection).getBuilding();
        if(building != null) {
            return true;
        }
        Troop troop = selectedTroop.getCell().getNeighbor(xDirection, yDirection).getTroop();
        if(troop != null) {
            if(!troop.getPlayer().equals(selectedTroop.getPlayer())) {
                return true;
            }
        }
        return false;
    }

    private boolean freeToMove(Troop selectedTroop, int xDirection, int yDirection) {
        return selectedTroop.getCell().getNeighbor(xDirection, yDirection).getTroop() == null
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
                if (cell.getTroop() != null) {
                    Tiles.drawTile(context, cell.getTroop(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
    }

    public void logToVBox(String log) {
        rightLogBox.getChildren().add(new Hyperlink(log));
    }
}
