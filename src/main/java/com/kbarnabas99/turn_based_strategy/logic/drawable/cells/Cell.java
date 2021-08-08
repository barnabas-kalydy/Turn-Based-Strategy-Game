package com.kbarnabas99.turn_based_strategy.logic.drawable.cells;


import com.kbarnabas99.turn_based_strategy.logic.drawable.Drawable;
import com.kbarnabas99.turn_based_strategy.logic.drawable.cities.City;
import com.kbarnabas99.turn_based_strategy.logic.drawable.troops.TroopImpl;
import com.kbarnabas99.turn_based_strategy.logic.maps.GameMap;

public class Cell implements Drawable {
    private final GameMap gameMap;
    private final int x, y;
    private CellType type;
    private TroopImpl troopImpl;
    private City city;

    public Cell(GameMap gameMap, int x, int y, CellType type) {
        this.gameMap = gameMap;
        this.x = x;
        this.y = y;
        this.type = type;
        city = null;
    }

    public CellType getCellType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public TroopImpl getTroop() {
        return troopImpl;
    }

    public void setTroop(TroopImpl troopImpl) {
        this.troopImpl = troopImpl;
    }

    public Cell getNeighbor(int upperDistanceFromCell, int rightDistanceFromCell) {
        return gameMap.getCell(x + upperDistanceFromCell, y + rightDistanceFromCell);
    }

    @Override
    public String getTileName() {
        return type.getTileName();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
