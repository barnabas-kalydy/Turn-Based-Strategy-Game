package com.kbarnabas99.turn_based_strategy.logic.drawable.troops;

import com.kbarnabas99.turn_based_strategy.logic.Player;
import com.kbarnabas99.turn_based_strategy.logic.drawable.cells.Cell;

public class Skeleton extends TroopImpl {

    static final float MAX_HEALTH = 10;
    static final float BASE_DMG = 3;

    public Skeleton(Cell cell, Player player) {
        super(cell, player, Skeleton.MAX_HEALTH);
        setHealth(Skeleton.MAX_HEALTH);
        setDmg(Skeleton.BASE_DMG);
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
