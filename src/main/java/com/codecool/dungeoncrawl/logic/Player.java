package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.drawable.troops.Troop;
import javafx.scene.paint.Paint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private final List<Troop> troops;

    private javafx.scene.paint.Paint playerColor;

    public Player(Paint playerColor) {
        troops = new ArrayList<>();
        this.playerColor = playerColor;
    }

    public List<Troop> getTroops() {
        return troops;
    }

    public void addTroop(Troop troop) {
        troops.add(troop);
    }

    public void removeTroop(Troop troopToRemove) {
        troops.remove(troopToRemove);
    }

    public javafx.scene.paint.Paint getColor() {
        return playerColor;
    }
}
