package com.kbarnabas99.turn_based_strategy.logic.drawable.troops;


import com.kbarnabas99.turn_based_strategy.logic.Player;
import com.kbarnabas99.turn_based_strategy.logic.drawable.Drawable;
import com.kbarnabas99.turn_based_strategy.logic.drawable.cells.Cell;

public abstract class TroopImpl implements Drawable, Troop {
    private final Player player;
    private final float maxHealth;
    private Cell cell;
    private float health = 10;
    private float dmg = 3;

    public TroopImpl(Cell cell, Player player, float maxHealth) {
        this.cell = cell;
        this.cell.setTroop(this);
        this.player = player;
        this.maxHealth = maxHealth;
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
        if (troopToAttack.getHealth() < 1) {
            troopToAttack.getCell().setTroop(null);
            troopToAttack.getPlayer().removeTroop(troopToAttack);
            return;
        }
        this.loseHealth(troopToAttack.getDmg());
    }


    @Override
    public float getHealth() {
        return health;
    }

    @Override
    public void setHealth(float health) {
        this.health = health;
    }

    @Override
    public float getMaxHealth() {
        return this.maxHealth;
    }

    @Override
    public void loseHealth(float healthToLose) {
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
    public float getDmg() {
        return dmg;
    }

    @Override
    public void setDmg(float dmg) {
        this.dmg = dmg;
    }
}
