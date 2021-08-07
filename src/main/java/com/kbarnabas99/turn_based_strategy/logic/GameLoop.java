package com.kbarnabas99.turn_based_strategy.logic;

import com.kbarnabas99.turn_based_strategy.logic.drawable.cells.Cell;
import com.kbarnabas99.turn_based_strategy.logic.drawable.troops.Troop;
import com.kbarnabas99.turn_based_strategy.logic.drawable.troops.TroopImpl;
import com.kbarnabas99.turn_based_strategy.logic.maps.GameMap;
import com.kbarnabas99.turn_based_strategy.logic.maps.MapLoader;
import com.kbarnabas99.turn_based_strategy.logic.tiles.Tiles;
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

import java.util.Arrays;
import java.util.List;

public class GameLoop {

    // todo export this somewhere else
    final List<String> movableCellTypes = Arrays.asList("ground_1", "ground_2", "ground_3", "bridge_1", "bridge_2", "road_1");

    Player actualTurnPlayer;
    GameMap map = MapLoader.loadMapFromCsv(1);

    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    VBox rightLogBox;

    public void start(Stage primaryStage) {

        canvas.setOnMouseClicked(this::setMouseClickEventOnMainScreen);

        Button passTurnButton = new Button("Pass Turn!");
        passTurnButton.setPrefSize(100, 20);
        passTurnButton.setOnMouseClicked(this::passTurn);

        Button clearLogBoxButton = new Button("Clear Right Log Box!");
        clearLogBoxButton.setPrefSize(200, 20);
        clearLogBoxButton.setOnMouseClicked(this::clearVBoxEvent);

        HBox hbox = new HBox();
        hbox.getChildren().add(passTurnButton);
        hbox.getChildren().add(clearLogBoxButton);

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


    private void setMouseClickEventOnMainScreen(MouseEvent mouseEvent) {
        // getting data about the place where mouse click occured
        double x = mouseEvent.getX(), y = mouseEvent.getY();

        Cell cell = map.getCell((int) x / 32, (int) y / 32);
        TroopImpl troop = cell.getTroop();

        if (troop != null)
            map.setSelectedTroop(troop, actualTurnPlayer);
        else
            map.setSelectedTroopToNull();

        TroopImpl selectedTroop = map.getSelectedTroop();
        String tileName = cell.getTileName();

        // logging to vbox
        clearVBox();
        logToVBox("\n");
        logToVBox("Troop: " + (troop == null ? "null" : troop.toString()));
        logToVBox("Tile name: " + tileName);
        logToVBox("Actual round player: " + actualTurnPlayer);
        logToVBox("Selected troop: " + (selectedTroop == null ? "null" : selectedTroop.toString()));
    }

    private void setKeyEvents(KeyEvent keyEvent) {
        TroopImpl selectedTroop = map.getSelectedTroop();
        if (selectedTroop != null) {
            // 0, 0 -> not move
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
                if (selectedTroop.getHealth() < 1) {
                    logToVBox("Selected troop died!");
                    selectedTroop.getCell().setTroop(null);
                    selectedTroop.getPlayer().removeTroop(selectedTroop);
                    map.setSelectedTroopToNull();
                }
            }
            refresh();
        }
    }

    private boolean freeToMove(Troop selectedTroop, int xDirection, int yDirection) {
        return selectedTroop.getCell().getNeighbor(xDirection, yDirection).getTroop() == null
                && movableCellTypes.contains(selectedTroop.getCell().getNeighbor(xDirection, yDirection).getTileName());
    }

    private boolean canAttack(Troop selectedTroop, int xDirection, int yDirection) {
        Troop troopToAttack = selectedTroop.getCell().getNeighbor(xDirection, yDirection).getTroop();
        if (troopToAttack != null) {
            if (!troopToAttack.getPlayer().equals(selectedTroop.getPlayer()))
                return true;
        }
        return false;
    }

    private void refresh() {
        // filling screen with black
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawTiles();
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

    private void drawTroops() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getTroop() != null) {
                    // draw troop to map
                    Tiles.drawTile(context, cell.getTroop(), x, y);

                    // draw health bar under the troop
                    Troop troop = cell.getTroop();
                    context.setFill(Color.GREEN);
                    context.fillRect(cell.getX() * 32,
                            cell.getY() * 32 + 29,
                            32 * (troop.getHealth() / troop.getMaxHealth()),
                            3);

                    // player color circle
                    context.setStroke(troop.getPlayer().getColor());
                    context.strokeOval(cell.getX() * 32 - 4, cell.getY() * 32 - 4, 40, 40);
                }
            }
        }
    }

    public void logToVBox(String log) {
        rightLogBox.getChildren().add(new Hyperlink(log));
    }

    public void clearVBoxEvent(MouseEvent event) {
        clearVBox();
    }

    public void clearVBox() {
        rightLogBox.getChildren().clear();
    }


}
