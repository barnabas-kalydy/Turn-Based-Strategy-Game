package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.drawable.troops.Troop;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private final Map<String, Troop> troops;

    public Player() {
        troops = new HashMap<>();
    }

    public Map<String, Troop> getTroops() {
        return troops;
    }

    public Troop getTroop(String troopId) {
        return troops.get(troopId);
    }

    public void addTroop(Troop troop) {
        troops.put(troop.getId(), troop);
    }
}
