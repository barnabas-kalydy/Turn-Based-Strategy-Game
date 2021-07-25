package com.codecool.dungeoncrawl.logic.drawable.other_entities;

import com.codecool.dungeoncrawl.logic.Player;
import com.codecool.dungeoncrawl.logic.drawable.Drawable;
import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;

public class ConqueredCity implements Drawable, Building {
    private Cell cell;
    private String id;
    private Player player;

    public ConqueredCity(Cell cell, Player player) {
        id = String.valueOf(System.nanoTime());
        this.cell = cell;
        this.cell.setBuilding(this);
        this.player = player;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTileName() {
        return null;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
