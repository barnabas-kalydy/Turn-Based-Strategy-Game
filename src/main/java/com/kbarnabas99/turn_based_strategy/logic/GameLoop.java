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
    BorderPane borderPane;
    Scene scene;


    int modifiedX, modifiedY;
    private int centerX = CELLS_NUMBER_HORIZONTALLY / 2;
    private int centerY = CELLS_NUMBER_VERTICALLY / 2;
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

        int cellX = (int) x / 32, cellY = (int) y / 32;
        Cell cell;
        if (GAMEMAP.getSelectedTroop() == null) {
            cell = GAMEMAP.getCell(cellX, cellY);
        } else {
            int emptyXCellsBeforeSelectedCell = -(((int) (SCREEN_WIDTH / 2)) / 32 - GAMEMAP.getSelectedTroop().getX());
            int emptyYCellsBeforeSelectedCell = -(((int) (SCREEN_HEIGHT / 2)) / 32 - GAMEMAP.getSelectedTroop().getY());
            cell = GAMEMAP.getCell(cellX + emptyXCellsBeforeSelectedCell,
                    cellY + emptyYCellsBeforeSelectedCell);
        }

        // Set selected troop if possible
        TroopImpl troop = cell.getTroop();
        if (troop != null) {
            selectTroop(troop);
        } else
            GAMEMAP.setSelectedTroopToNull();

        // todo Open city menu
        City city = cell.getCity();
        if (city != null) {

        }
        refreshScreen();
    }

    private void selectTroop(TroopImpl troop) {
        GAMEMAP.setSelectedTroop(troop, actualTurnPlayer);

        setCenterXAndYofSelectedTroop();
    }

    private void setKeyEvents(KeyEvent keyEvent) {
        TroopImpl selectedTroop = GAMEMAP.getSelectedTroop();
        if (selectedTroop != null) {
            int xDirection, yDirection;
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
                default -> {
                    xDirection = 0;
                    yDirection = 0;
                }
            }
            // TODO move map when no troop is selected with WASD
            if (freeToMove(selectedTroop, xDirection, yDirection)) {
                selectedTroop.move(xDirection, yDirection);
                setCenterXAndYofSelectedTroop();
            } else if (canAttack(selectedTroop, xDirection, yDirection)) {
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

    private void setCenterXAndYofSelectedTroop() {
        if (GAMEMAP.getSelectedTroop() != null) {
            centerX = GAMEMAP.getSelectedTroop().getX();
            centerY = GAMEMAP.getSelectedTroop().getY();
        } else {
            centerX = CELLS_NUMBER_HORIZONTALLY / 2;
            centerY = CELLS_NUMBER_VERTICALLY / 2;
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
        setCenterXAndYofSelectedTroop();

        // filling screen with black
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        // TODO draw empty tiles out of the map
        // Redraw ground, cities and troops
        for (int x = centerX - (CELLS_NUMBER_HORIZONTALLY / 2); x < centerX + (CELLS_NUMBER_HORIZONTALLY / 2); x++) {
            for (int y = centerY - (CELLS_NUMBER_VERTICALLY / 2); y < centerY + (CELLS_NUMBER_VERTICALLY / 2); y++) {
                modifiedX = x - centerX + (CELLS_NUMBER_HORIZONTALLY / 2);
                modifiedY = y - centerY + (CELLS_NUMBER_VERTICALLY / 2);
                if (x > -1 && y > -1) {
                    drawCellType(x, y, modifiedX, modifiedY);
                    drawCity(x, y, modifiedX, modifiedY);
                    drawTroop(x, y, modifiedX, modifiedY);
                }
            }
        }
        canvas.setOnMouseClicked(this::setMouseClickEventOnMainScreen);
    }

    private void drawCellType(int x, int y, int modifiedX, int modifiedY) {
        Cell cell = GAMEMAP.getCell(x, y);
        Tiles.drawTile(context, cell, modifiedX, modifiedY);
    }

    private void drawCity(int x, int y, int modifiedX, int modifiedY) {
        Cell cell = GAMEMAP.getCell(x, y);
        City city = cell.getCity();
        if (city != null) {
            // Draw city to map
            Tiles.drawTile(context, city, modifiedX, modifiedY);

            if (city.getPlayer() != null) {
                // Draw player color around city
                context.setStroke(city.getPlayer().getColor());
                context.setLineWidth(2);
                context.strokeOval(modifiedX * 32 - 4, modifiedY * 32 - 4, 40, 40);
            }
        }
    }

    private void drawTroop(int x, int y, int modifiedX, int modifiedY) {
        Cell cell = GAMEMAP.getCell(x, y);
        if (cell.getTroop() != null) {
            // Draw troop to map
            Tiles.drawTile(context, cell.getTroop(), modifiedX, modifiedY);

            // Draw health bar under the troop
            Troop troop = cell.getTroop();
            context.setFill(Color.GREEN);
            context.fillRect(modifiedX * 32,
                    modifiedY * 32 + 29,
                    32 * (troop.getHealth() / troop.getMaxHealth()),
                    3);

            // Draw white circle around selected troop
            if (cell.getTroop().equals(GAMEMAP.getSelectedTroop())) {
                context.setStroke(Color.WHITE);
                context.setLineWidth(3);
                context.strokeOval(modifiedX * 32 - 3, modifiedY * 32 - 3, 38, 38);
            }

            // Draw player color circle around troop
            context.setStroke(troop.getPlayer().getColor());
            context.setLineWidth(2);
            context.strokeOval(modifiedX * 32 - 4, modifiedY * 32 - 4, 40, 40);
        }
    }
}
