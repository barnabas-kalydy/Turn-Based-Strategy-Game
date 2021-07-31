package com.codecool.dungeoncrawl.logic.drawable.troops;

import com.codecool.dungeoncrawl.logic.Player;
import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;

public class Swordsman extends TroopImpl {
    public Swordsman(Cell cell, Player player) {
        super(cell, player);
        setHealth(15);
        setDmg(2);
    }

    public String getTileName() {
        return "swordsman";
    }
}
