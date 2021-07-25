package com.codecool.dungeoncrawl.logic.tiles;

import static com.codecool.dungeoncrawl.logic.tiles.Tiles.TILE_WIDTH;

public class Tile {

    public final int x, y, width, height;

    Tile(int tileXPosition, int tileYPosition) {
        // idk what is that 2 but it is needed
        x = tileXPosition * (TILE_WIDTH + 2);
        y = tileYPosition * (TILE_WIDTH + 2);
        width = TILE_WIDTH;
        height = TILE_WIDTH;
    }
}