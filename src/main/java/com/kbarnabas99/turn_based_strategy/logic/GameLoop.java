package com.kbarnabas99.turn_based_strategy.logic;

import com.kbarnabas99.turn_based_strategy.logic.drawable.cells.Cell;
import com.kbarnabas99.turn_based_strategy.logic.drawable.cities.City;
import com.kbarnabas99.turn_based_strategy.logic.drawable.troops.Troop;
import com.kbarnabas99.turn_based_strategy.logic.drawable.troops.TroopImpl;
import com.kbarnabas99.turn_based_strategy.logic.maps.GameMap;
import com.kbarnabas99.turn_based_strategy.logic.maps.MapLoader;
import com.kbarnabas99.turn_based_strategy.logic.tiles.Tiles;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class GameLoop {

    // todo export this somewhere else
    final List<String> movableCellTypes = Arrays.asList("ground_1", "ground_2", "ground_3", "bridge_1", "bridge_2", "road_1");

    Player actualTurnPlayer;
    GameMap map = MapLoader.loadMapFromCsv(1);

    // Variables needed for graphical things
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    HBox downButtonsBox;
    ScrollPane scroll;
    BorderPane borderPane;
    Scene scene;


    public void start(Stage primaryStage) {
        // Setup graphical user interface
        setupDownButtonsBox();
        setupBorderPane();
        setScene(primaryStage);

        setupEventListeners();

        // Set first turn player
        actualTurnPlayer = map.getPlayer(0);

        refreshScreen();
    }

    private void setupEventListeners() {
        canvas.setOnMouseClicked(this::setMouseClickEventOnMainScreen);
        borderPane.setOnKeyPressed(this::setKeyEvents);
    }

    private void setupDownButtonsBox() {
        Button passTurnButton = new Button("Pass Turn!");
        passTurnButton.setPrefSize(100, 20);
        passTurnButton.setOnMouseClicked(this::passTurn);

        downButtonsBox = new HBox();
        downButtonsBox.getChildren().add(passTurnButton);
    }

    private void setupBorderPane() {
        borderPane = new BorderPane();
        borderPane.setCenter(canvas);
        borderPane.setBottom(downButtonsBox);
        borderPane.setRight(scroll);
    }

    private void setScene(Stage primaryStage) {
        scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void passTurn(MouseEvent mouseEvent) {
        if (actualTurnPlayer.equals(map.getPlayer(0)))
            actualTurnPlayer = map.getPlayer(1);
        else
            actualTurnPlayer = map.getPlayer(0);
        map.setSelectedTroopToNull();
        refreshScreen();
    }


    private void setMouseClickEventOnMainScreen(MouseEvent mouseEvent) {
        // getting data about the place where mouse click occurred
        double x = mouseEvent.getX(), y = mouseEvent.getY();
        Cell cell = map.getCell((int) x / 32, (int) y / 32);
        TroopImpl troop = cell.getTroop();
        City city = cell.getCity();

        // Set selected troop if possible
        if (troop != null) {
            map.setSelectedTroop(troop, actualTurnPlayer);
        } else
            map.setSelectedTroopToNull();

        // todo Open city menu
        if (city != null) {

        }

        refreshScreen();
    }

    private void setKeyEvents(KeyEvent keyEvent) {
        TroopImpl selectedTroop = map.getSelectedTroop();
        if (selectedTroop != null) {
            int xDirection = 0, yDirection = 0;
            // set direction
            switch (keyEvent.getCode()) {
                case W -> {
                    xDirection = 0;
                    yDirection = -1;
                }
                case S -> {
                    xDirection = 0;
                    yDirection = 1;
                }
                case A -> {
                    xDirection = -1;
                    yDirection = 0;
                }
                case D -> {
                    xDirection = 1;
                    yDirection = 0;
                }
            }
            if (freeToMove(selectedTroop, xDirection, yDirection))
                selectedTroop.move(xDirection, yDirection);
            else if (canAttack(selectedTroop, xDirection, yDirection)) {
                selectedTroop.attack(xDirection, yDirection);
                // If selected troop dies
                if (selectedTroop.getHealth() < 1) {
                    selectedTroop.removeTroop();
                    map.setSelectedTroopToNull();
                }
            } else if (canConquer(selectedTroop, xDirection, yDirection)) {
                selectedTroop.conquerCity(xDirection, yDirection, selectedTroop.getPlayer());
            }
            refreshScreen();
        }
    }

    private boolean freeToMove(TroopImpl selectedTroop, int xDirection, int yDirection) {
        return selectedTroop.getCell().getNeighbor(xDirection, yDirection).getTroop() == null
                && movableCellTypes.contains(selectedTroop.getCell().getNeighbor(xDirection, yDirection).getTileName());
    }

    private boolean canConquer(TroopImpl selectedTroop, int xDirection, int yDirection) {
        City cityToConquer = selectedTroop.getCell().getNeighbor(xDirection, yDirection).getCity();
        return cityToConquer != null;
    }

    private boolean canAttack(TroopImpl selectedTroop, int xDirection, int yDirection) {
        Troop troopToAttack = selectedTroop.getCell().getNeighbor(xDirection, yDirection).getTroop();
        if (troopToAttack != null) {
            if (!troopToAttack.getPlayer().equals(selectedTroop.getPlayer()))
                return true;
        }
        return false;
    }

    private void refreshScreen() {
        // filling screen with black
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Redraw ground, cities and troops
        drawTiles();
        drawCities();
        drawTroops();

    }

    private void drawTiles() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                Tiles.drawTile(context, cell, x, y);
            }
        }
    }

    private void drawCities() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                City city = cell.getCity();
                if (city != null) {
                    // Draw city to map
                    Tiles.drawTile(context, city, x, y);

                    if (city.getPlayer() != null) {
                        // Draw player color around city
                        context.setStroke(city.getPlayer().getColor());
                        context.setLineWidth(2);
                        context.strokeOval(cell.getX() * 32 - 4, cell.getY() * 32 - 4, 40, 40);
                    }
                }
            }
        }
    }

    private void drawTroops() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getTroop() != null) {
                    // Draw troop to map
                    Tiles.drawTile(context, cell.getTroop(), x, y);

                    // Draw health bar under the troop
                    Troop troop = cell.getTroop();
                    context.setFill(Color.GREEN);
                    context.fillRect(cell.getX() * 32,
                            cell.getY() * 32 + 29,
                            32 * (troop.getHealth() / troop.getMaxHealth()),
                            3);

                    // Draw white circle around selected troop
                    if (cell.getTroop().equals(map.getSelectedTroop())) {
                        context.setStroke(Color.WHITE);
                        context.setLineWidth(3);
                        context.strokeOval(cell.getX() * 32 - 3, cell.getY() * 32 - 3, 38, 38);
                    }

                    // Draw player color circle around troop
                    context.setStroke(troop.getPlayer().getColor());
                    context.setLineWidth(2);
                    context.strokeOval(cell.getX() * 32 - 4, cell.getY() * 32 - 4, 40, 40);
                }
            }
        }
    }
}
