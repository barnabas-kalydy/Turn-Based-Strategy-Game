package com.codecool.dungeoncrawl.logic.drawable.troops;

import com.codecool.dungeoncrawl.logic.Player;
import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;

public class Skeleton extends TroopImpl {

    static final float MAX_HEALTH = 10;
    static final float BASE_DMG = 3;

    public Skeleton(Cell cell, Player player) {
        super(cell, player, Skeleton.MAX_HEALTH);
        setHealth(Skeleton.MAX_HEALTH);
        setDmg(Skeleton.BASE_DMG);
    }

    public String getTileName() {
        return "skeleton";
    }
}
