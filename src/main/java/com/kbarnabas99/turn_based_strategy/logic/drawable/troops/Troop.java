package com.kbarnabas99.turn_based_strategy.logic.drawable.troops;

import com.kbarnabas99.turn_based_strategy.logic.Player;
import com.kbarnabas99.turn_based_strategy.logic.drawable.cells.Cell;

public interface Troop {
    Player getPlayer();

    Cell getCell();

    void move(int dx, int dy);

    void attack(int xDirection, int yDirection);

    float getDmg();

    void setDmg(float dmg);

    float getHealth();

    void setHealth(float health);

    float getMaxHealth();

    void loseHealth(float healthToLose);
}
