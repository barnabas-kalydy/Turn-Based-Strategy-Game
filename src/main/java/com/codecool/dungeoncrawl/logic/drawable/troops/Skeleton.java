package com.codecool.dungeoncrawl.logic.drawable.troops;

import com.codecool.dungeoncrawl.logic.Player;
import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;

public class Skeleton extends TroopImpl {
    public Skeleton(Cell cell, Player player) {
        super(cell, player);
        setHealth(8);
        setDmg(3);
    }

    public String getTileName() {
        return "skeleton";
    }
}
