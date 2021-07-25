package com.codecool.dungeoncrawl.logic.drawable.actors;

import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;

public class Player extends Actor {
    public Player(Cell cell) {
        super(cell);
    }

    public String getTileName() {
        return "player";
    }
}
