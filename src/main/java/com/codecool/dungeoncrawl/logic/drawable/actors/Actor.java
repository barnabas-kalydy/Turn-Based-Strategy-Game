package com.codecool.dungeoncrawl.logic.drawable.actors;

import com.codecool.dungeoncrawl.logic.drawable.Drawable;
import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;

public abstract class Actor implements Drawable {
    private Cell cell;
    private int health = 10;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        cell.setActor(null);
        nextCell.setActor(this);
        cell = nextCell;
    }

    public int getHealth() {
        return health;
    }

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
    public String toString() {
        return "Actor{" +
                "cell=" + cell +
                ", health=" + health +
                '}';
    }
}
