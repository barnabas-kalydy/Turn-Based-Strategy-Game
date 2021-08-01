package com.kbarnabas99.turn_based_strategy.logic.maps;

import com.kbarnabas99.turn_based_strategy.logic.Player;
import com.kbarnabas99.turn_based_strategy.logic.drawable.cells.Cell;
import com.kbarnabas99.turn_based_strategy.logic.drawable.cells.CellType;
import com.kbarnabas99.turn_based_strategy.logic.drawable.troops.Troop;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private final int width;
    private final int height;
    private Cell[][] cells;

    private List<Player> players;

    private Troop selectedTroop;

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;

        initCells(defaultCellType);

        initPlayers();
    }

    private void initCells(CellType defaultCellType) {
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public void initPlayers() {
        players = new ArrayList<>();
        players.add(new Player(Paint.valueOf("0000ff")));
        players.add(new Player(Paint.valueOf("ff0000")));
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Troop getSelectedTroop() {
        return selectedTroop;
    }

    public void setSelectedTroop(Troop selectedTroop, Player actualPlayerTurn) {
        if (selectedTroop != null) {
            if (selectedTroop.getPlayer().equals(actualPlayerTurn))
                this.selectedTroop = selectedTroop;
        }
    }

    public void setSelectedTroopToNull() {
        selectedTroop = null;
    }

    public Player getPlayer(int playerNumber) {
        return players.get(playerNumber);
    }
}