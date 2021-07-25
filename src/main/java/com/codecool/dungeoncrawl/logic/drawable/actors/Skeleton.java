package com.codecool.dungeoncrawl.logic.drawable.actors;

import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;

public class Skeleton extends Actor {
    public Skeleton(Cell cell) {
        super(cell);
    }

    public String getTileName() {
        return "skeleton";
    }
}
