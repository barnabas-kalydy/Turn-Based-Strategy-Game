package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;
import com.codecool.dungeoncrawl.logic.maps.GameMap;
import com.codecool.dungeoncrawl.logic.maps.MapLoader;
import com.codecool.dungeoncrawl.logic.tiles.Tiles;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {

    GameMap map = MapLoader.loadMap();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
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
        String actor;
        if(map.getCell((int) x / 32, (int) y / 32).getActor() != null) {
            actor = map.getCell((int) x / 32, (int) y / 32).getActor().toString();
        } else {
            actor = null;
        }
        String tileName = map.getCell((int) x / 32, (int) y / 32).getTileName();

        // adding modal window with details of the place where mouse click occured
        VBox vBoxPane = new VBox();
        vBoxPane.getChildren().add(new Hyperlink("Click X coordinate: " + x));
        vBoxPane.getChildren().add(new Hyperlink("Click Y coordinate: " + y));
        vBoxPane.getChildren().add(new Hyperlink("cell.getActor(): " + actor));
        vBoxPane.getChildren().add(new Hyperlink("cell.getTileName(): " + tileName));

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setWidth(600);
        dialog.setHeight(500);
        dialog.setScene(new Scene(vBoxPane));
        dialog.showAndWait();
    }

    private void setKeyEvents(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case W -> {
                map.getPlayer().move(0, -1);
                refresh();
            }
            case S -> {
                map.getPlayer().move(0, 1);
                refresh();
            }
            case A -> {
                map.getPlayer().move(-1, 0);
                refresh();
            }
            case D -> {
                map.getPlayer().move(1, 0);
                refresh();
            }
        }
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
