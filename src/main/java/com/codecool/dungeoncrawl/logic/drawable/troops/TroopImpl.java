package com.codecool.dungeoncrawl.logic.drawable.troops;

import com.codecool.dungeoncrawl.logic.Player;
import com.codecool.dungeoncrawl.logic.drawable.Drawable;
import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;

public abstract class TroopImpl implements Drawable, Troop {
    private Cell cell;
    private int health = 10;
    private String troopId;
    private Player player;

    public TroopImpl(Cell cell, Player player) {
        this.cell = cell;
        this.cell.setTroop(this);
        troopId = getNewTroopId();
        this.player = player;
    }

    public static String getNewTroopId() {
        return String.valueOf(System.nanoTime());
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        cell.setTroop(null);
        nextCell.setTroop(this);
        cell = nextCell;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    @Override
    public String getId() {
        return troopId;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void interactWith(Cell neighbor) {
        // TODO continue here

        // make interaction with city -> conquer the city

        // make interaction with other players troop -> attack the troop

        // todo implement new building method -> like build a shelter or something
    }
}
