package com.codecool.dungeoncrawl.logic.drawable.troops;

import com.codecool.dungeoncrawl.logic.Player;
import com.codecool.dungeoncrawl.logic.drawable.Drawable;
import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;

public abstract class TroopImpl implements Drawable, Troop {
    private Cell cell;
    private int health = 10;
    private int dmg = 3;
    private Player player;

    public TroopImpl(Cell cell, Player player) {
        this.cell = cell;
        this.cell.setTroop(this);
        this.player = player;
        player.addTroop(this);
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        cell.setTroop(null);
        nextCell.setTroop(this);
        cell = nextCell;
    }

    @Override
    public void attack(int xDirection, int yDirection) {
        Troop troopToAttack = cell.getNeighbor(xDirection, yDirection).getTroop();
        troopToAttack.loseHealth(this.getDmg());
        if(troopToAttack.getHealth() < 1) {
            troopToAttack.getCell().setTroop(null);
            troopToAttack.getPlayer().removeTroop(troopToAttack);
            return;
        }
        this.loseHealth(troopToAttack.getDmg());
    }


    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public void loseHealth(int healthToLose) {
        this.health -= healthToLose;
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
    public Player getPlayer() {
        return player;
    }

    @Override
    public int getDmg() {
        return dmg;
    }

    @Override
    public void setDmg(int dmg) {
        this.dmg = dmg;
    }
}
