package com.kbarnabas99.turn_based_strategy.logic.tiles;

import java.util.ArrayList;
import java.util.List;

import static com.kbarnabas99.turn_based_strategy.logic.tiles.Tiles.TILE_WIDTH;


public class Tile {

    private final int x, y, width, height;

    Tile(int coordinateNumber) {
        List<Integer> coordinates = calcCoordinates(coordinateNumber);
        int tileXPosition = coordinates.get(0);
        int tileYPosition = coordinates.get(1);
        // idk what is that 2 but it is needed
        x = tileXPosition * (TILE_WIDTH + 2);
        y = tileYPosition * (TILE_WIDTH + 2);
        width = TILE_WIDTH;
        height = TILE_WIDTH;
    }

    private List<Integer> calcCoordinates(Integer coordinateInTiled) {
        List<Integer> coordinates = new ArrayList<>();
        coordinates.add(coordinateInTiled % 32);
        coordinates.add(coordinateInTiled / 32);
        return coordinates;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}