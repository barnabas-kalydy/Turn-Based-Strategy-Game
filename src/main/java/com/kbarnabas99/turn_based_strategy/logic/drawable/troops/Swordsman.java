package com.kbarnabas99.turn_based_strategy.logic.drawable.troops;

import com.kbarnabas99.turn_based_strategy.logic.Player;
import com.kbarnabas99.turn_based_strategy.logic.drawable.cells.Cell;

public class Swordsman extends TroopImpl {

    static final float MAX_HEALTH = 15;
    static final float BASE_DMG = 2;

    public Swordsman(Cell cell, Player player) {
        super(cell, player, Swordsman.MAX_HEALTH);
        setHealth(Swordsman.MAX_HEALTH);
        setDmg(Swordsman.BASE_DMG);
    }

    public String getTileName() {
        return "swordsman";
    }
}
