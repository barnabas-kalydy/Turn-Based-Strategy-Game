package com.kbarnabas99.turn_based_strategy.logic.tiles;

import com.kbarnabas99.turn_based_strategy.logic.drawable.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    private static final Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static final Map<String, Tile> tileMap = new HashMap<>();
    // TODO EXPORT THIS
    public static int TILE_WIDTH = 32; // in pixels

    // initialize Tile types with names
    static {
        // Map elements
        tileMap.put("empty", new Tile(0));
        tileMap.put("ground_1", new Tile(2));
        tileMap.put("ground_2", new Tile(3));
        tileMap.put("ground_3", new Tile(4));
        tileMap.put("road_1", new Tile(16));
        tileMap.put("pine_tree_1", new Tile(32));
        tileMap.put("pine_tree_2", new Tile(67));
        tileMap.put("bridge_1", new Tile(176));
        tileMap.put("bridge_2", new Tile(175));
        tileMap.put("water_full", new Tile(168));
        tileMap.put("water_corner", new Tile(170));
        tileMap.put("boat_1", new Tile(619));
        tileMap.put("torch_1", new Tile(484));

        // CITIES
        tileMap.put("city_1", new Tile(644));
        tileMap.put("city_2", new Tile(672));

        // troops/entities/animals
        tileMap.put("swordsman", new Tile(28));
        tileMap.put("skeleton", new Tile(26));
        tileMap.put("horse", new Tile(252));
        tileMap.put("cat", new Tile(254));
        tileMap.put("cow", new Tile(251));
    }

    /**
     * Gets the tile picture of a drawable and draws on the context.
     *
     * @param context  to draw the tile on
     * @param drawable the Drawable object to draw to the GraphicsContext
     * @param x        the x coordinate of the GraphicsContext to start the tile from
     * @param y        the y coordinate of the GraphicsContext to start the tile from
     */
    public static void drawTile(GraphicsContext context, Drawable drawable, int x, int y) {
        Tile tile = tileMap.get(drawable.getTileName());
        context.drawImage(tileset,
                tile.getX(),
                tile.getY(),
                tile.getWidth(),
                tile.getHeight(),
                x * TILE_WIDTH,
                y * TILE_WIDTH,
                TILE_WIDTH,
                TILE_WIDTH);
    }
}
