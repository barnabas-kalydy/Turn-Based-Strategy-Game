package com.codecool.dungeoncrawl.logic.drawable.troops;

import com.codecool.dungeoncrawl.logic.Player;
import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;

public interface Troop {
    String getId();

    void move(int dx, int dy);

    Player getPlayer();

    Cell getCell();
}
