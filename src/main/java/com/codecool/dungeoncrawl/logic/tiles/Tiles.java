package com.codecool.dungeoncrawl.logic.tiles;

import com.codecool.dungeoncrawl.logic.drawable.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    private static final Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);

    public static int TILE_WIDTH = 32; // in pixels
    private static Map<String, Tile> tileMap = new HashMap<>();

    // initialize Tile types with names
    static {
        // map elements
        tileMap.put("empty", new Tile(0, 0));
        tileMap.put("wall", new Tile(10, 17));
        tileMap.put("floor", new Tile(2, 0));

        // entities
        tileMap.put("swordsman", new Tile(27, 0));
        tileMap.put("skeleton", new Tile(29, 6));
    }

    /**
     * Gets the tile picture of a drawable and draws on the context.
     *
     * @param context  to draw the tile on
     * @param drawable the Drawable object to draw to the GraphicsContext
     * @param x        the x coordinate of the GraphicsContext
     * @param y        the y coordinate of the GraphicsContext
     */
    public static void drawTile(GraphicsContext context, Drawable drawable, int x, int y) {
        Tile tile = tileMap.get(drawable.getTileName());
        context.drawImage(tileset,
                tile.x,
                tile.y,
                tile.width,
                tile.height,
                x * TILE_WIDTH,
                y * TILE_WIDTH,
                TILE_WIDTH,
                TILE_WIDTH);
    }
}
