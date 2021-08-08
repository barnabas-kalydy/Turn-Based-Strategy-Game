package com.kbarnabas99.turn_based_strategy.logic;

import com.kbarnabas99.turn_based_strategy.logic.drawable.cities.City;
import com.kbarnabas99.turn_based_strategy.logic.drawable.troops.TroopImpl;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final List<TroopImpl> troops;
    private final List<City> cities;

    private final javafx.scene.paint.Paint playerColor;

    public Player(Paint playerColor) {
        troops = new ArrayList<>();
        cities = new ArrayList<>();
        this.playerColor = playerColor;
    }

    public List<TroopImpl> getTroops() {
        return troops;
    }

    public void addTroop(TroopImpl troop) {
        troops.add(troop);
    }

    public void removeTroop(TroopImpl troopToRemove) {
        troops.remove(troopToRemove);
    }

    public void addCity(City city) {cities.add(city); }

    public void removeCity(City city) {cities.remove(city); }

    public javafx.scene.paint.Paint getColor() {
        return playerColor;
    }
}
