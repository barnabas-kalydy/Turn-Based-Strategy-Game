package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.drawable.other_entities.ConqueredCity;
import com.codecool.dungeoncrawl.logic.drawable.troops.Troop;

import java.util.HashMap;
import java.util.Map;

public class Player {

    private final Map<String, Troop> troops;
    private final Map<String, ConqueredCity> cities;
    private String playerId;

    public Player(String playerId) {
        this.playerId = playerId;
        troops = new HashMap<>();
        cities = new HashMap<>();
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

    public Map<String, ConqueredCity> getCities() {
        return cities;
    }

    public ConqueredCity getCity(String cityId) {
        return cities.get(cityId);
    }

    public void addCity(ConqueredCity city) {
        cities.put(city.getId(), city);
    }
}
