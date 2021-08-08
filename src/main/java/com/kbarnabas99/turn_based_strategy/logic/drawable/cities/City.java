package com.kbarnabas99.turn_based_strategy.logic.drawable.cities;

import com.kbarnabas99.turn_based_strategy.logic.Player;
import com.kbarnabas99.turn_based_strategy.logic.drawable.Drawable;
import com.kbarnabas99.turn_based_strategy.logic.drawable.cells.Cell;

public class City implements Drawable {

    private final Cell cell;
    private Player owner;

    public City(Cell cell) {
        this.cell = cell;
        cell.setCity(this);
    }

    @Override
    public String getTileName() {
        return "city_1";
    }

    public Player getPlayer() {
        return owner;
    }

    public void setPlayer(Player owner) {
        this.owner = owner;
        owner.addCity(this);
    }

    public Cell getCell() {
        return cell;
    }
}
