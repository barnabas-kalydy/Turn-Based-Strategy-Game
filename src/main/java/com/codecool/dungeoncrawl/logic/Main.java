package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;
import com.codecool.dungeoncrawl.logic.maps.GameMap;
import com.codecool.dungeoncrawl.logic.maps.MapLoader;
import com.codecool.dungeoncrawl.logic.tiles.Tiles;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
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

        primaryStage.setScene(scene);
        primaryStage.show();

        refresh();

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
