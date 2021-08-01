package com.kbarnabas99.turn_based_strategy.logic.drawable.cells;

public enum CellType {

    EMPTY("empty"),
    GROUND_1("ground_1"),
    PINE_TREE_1("pine_tree_1"),
    PINE_TREE_2("pine_tree_2"),
    HOUSE_1("house_1"),
    HOUSE_2("house_2"),
    BRIDGE_1("bridge_1"),
    BRIDGE_2("bridge_2"),
    WATER_FULL("water_full"),
    WATER_CORNER("water_corner"),
    BOAT_1("boat_1"),
    TORCH_1("torch_1"),
    CAT("cat");

    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
