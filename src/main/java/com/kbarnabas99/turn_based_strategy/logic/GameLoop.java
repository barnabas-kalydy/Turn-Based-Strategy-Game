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

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class GameLoop {

    // todo export this somewhere else
    private final List<String> MOVABLE_CELL_TYPES = Arrays.asList("ground_1", "ground_2", "ground_3", "bridge_1", "bridge_2", "road_1");

    private final GameMap GAMEMAP = MapLoader.loadMapFromCsv(2);
    private final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private final int CELLS_NUMBER_HORIZONTALLY = (int) (SCREEN_WIDTH / 32);
    private final int CELLS_NUMBER_VERTICALLY = (int) (SCREEN_HEIGHT / 32);

    // Variables needed for graphical things
    Canvas canvas = new Canvas(SCREEN_WIDTH,
            SCREEN_HEIGHT - 95);
    GraphicsContext context = canvas.getGraphicsContext2D();
    HBox downButtonsBox;
    ScrollPane scroll;
    BorderPane borderPane;
    Scene scene;
    private Player actualTurnPlayer;

    public void start(Stage primaryStage) {
        // Setup graphical user interface
        setupDownButtonsBox();
        setupBorderPane();
        setScene(primaryStage);

        setupEventListeners();

        // Set first turn player
        actualTurnPlayer = GAMEMAP.getPlayer(0);

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
        if (actualTurnPlayer.equals(GAMEMAP.getPlayer(0)))
            actualTurnPlayer = GAMEMAP.getPlayer(1);
        else
            actualTurnPlayer = GAMEMAP.getPlayer(0);
        GAMEMAP.setSelectedTroopToNull();
        refreshScreen();
    }


    private void setMouseClickEventOnMainScreen(MouseEvent mouseEvent) {
        // getting data about the place where mouse click occurred
        double x = mouseEvent.getX(), y = mouseEvent.getY();
        Cell cell = GAMEMAP.getCell((int) x / 32, (int) y / 32);
        TroopImpl troop = cell.getTroop();
        City city = cell.getCity();

        // Set selected troop if possible
        if (troop != null) {
            selectTroop(troop);
        } else
            GAMEMAP.setSelectedTroopToNull();

        // todo Open city menu
        if (city != null) {

        }

        refreshScreen();
    }

    private void selectTroop(TroopImpl troop) {
        GAMEMAP.setSelectedTroop(troop, actualTurnPlayer);
    }

    private void setKeyEvents(KeyEvent keyEvent) {
        TroopImpl selectedTroop = GAMEMAP.getSelectedTroop();
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
                    GAMEMAP.setSelectedTroopToNull();
                }
            } else if (canConquer(selectedTroop, xDirection, yDirection)) {
                selectedTroop.conquerCity(xDirection, yDirection, selectedTroop.getPlayer());
            }
            refreshScreen();
        }
    }

    private boolean freeToMove(TroopImpl selectedTroop, int xDirection, int yDirection) {
        return selectedTroop.getCell().getNeighbor(xDirection, yDirection).getTroop() == null
                && MOVABLE_CELL_TYPES.contains(selectedTroop.getCell().getNeighbor(xDirection, yDirection).getTileName());
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
        context.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // Redraw ground, cities and troops
        drawTiles();
        drawCities();
        drawTroops();

    }

    private void drawTiles() {
        for (int x = 0; x < (0 + CELLS_NUMBER_HORIZONTALLY); x++) {
            for (int y = 0; y < (0 + CELLS_NUMBER_VERTICALLY); y++) {
                Cell cell = GAMEMAP.getCell(x, y);
                Tiles.drawTile(context, cell, x, y);
            }
        }
    }

    private void drawCities() {
        for (int x = 0; x < (0 + CELLS_NUMBER_HORIZONTALLY); x++) {
            for (int y = 0; y < (0 + CELLS_NUMBER_VERTICALLY); y++) {
                Cell cell = GAMEMAP.getCell(x, y);
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
        for (int x = 0; x < (0 + CELLS_NUMBER_HORIZONTALLY); x++) {
            for (int y = 0; y < (0 + CELLS_NUMBER_VERTICALLY); y++) {
                Cell cell = GAMEMAP.getCell(x, y);
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
                    if (cell.getTroop().equals(GAMEMAP.getSelectedTroop())) {
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
