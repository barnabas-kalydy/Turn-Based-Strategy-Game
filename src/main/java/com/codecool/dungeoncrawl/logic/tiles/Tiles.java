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
        //////// map 2 tiles /////////////
        tileMap.put("empty", new Tile(0));
        tileMap.put("ground_1", new Tile(2));
        tileMap.put("pine_tree_1", new Tile(32));
        tileMap.put("pine_tree_2", new Tile(67));
        tileMap.put("house_1", new Tile(644));
        tileMap.put("house_2", new Tile(672));
        tileMap.put("horse", new Tile(252));
        tileMap.put("cow", new Tile(251));
        tileMap.put("bridge_1", new Tile(176));
        tileMap.put("bridge_2", new Tile(175));
        tileMap.put("water_full", new Tile(168));
        tileMap.put("water_corner", new Tile(170));
        tileMap.put("boat_1", new Tile(619));
        tileMap.put("torch_1", new Tile(484));

        tileMap.put("swordsman", new Tile(28));
        tileMap.put("skeleton", new Tile(26));

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
