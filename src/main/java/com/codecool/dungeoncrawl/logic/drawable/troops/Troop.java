package com.codecool.dungeoncrawl.logic.drawable.troops;

import com.codecool.dungeoncrawl.logic.Player;
import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;

public interface Troop {
    Player getPlayer();

    Cell getCell();

    void move(int dx, int dy);
    void attack(int xDirection, int yDirection);

    int getDmg();
    void setDmg(int dmg);

    int getHealth();
    void setHealth(int health);
    void loseHealth(int healthToLose);
}
