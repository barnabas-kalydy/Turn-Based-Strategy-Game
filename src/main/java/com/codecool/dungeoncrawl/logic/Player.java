package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.drawable.troops.Troop;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final List<Troop> troops;

    public Player() {
        troops = new ArrayList<>();
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
}
