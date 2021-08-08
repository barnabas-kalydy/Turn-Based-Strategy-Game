package com.kbarnabas99.turn_based_strategy.logic.maps;

import com.kbarnabas99.turn_based_strategy.logic.drawable.cells.Cell;
import com.kbarnabas99.turn_based_strategy.logic.drawable.cells.CellType;
import com.kbarnabas99.turn_based_strategy.logic.drawable.cities.City;
import com.kbarnabas99.turn_based_strategy.logic.drawable.troops.Skeleton;
import com.kbarnabas99.turn_based_strategy.logic.drawable.troops.Swordsman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Logger;

public class MapLoader {

    private final static Logger LOGGER = Logger.getLogger(MapLoader.class.getName());

    static int mapWidth = 0, mapHeight = 0;

    public static GameMap loadMapFromCsv(int mapNumber) {
        GameMap map;
        try {
            readMapWidthAndHeightFromCsv(mapNumber);
            map = new GameMap(mapWidth, mapHeight, CellType.EMPTY);
            Scanner sc = new Scanner(new File(
                    String.format("src/main/resources/maps/%d/map_%d.csv", mapNumber, mapNumber)));
            for (int y = 0; y < mapHeight; y++) {
                String[] line = sc.nextLine().split(",");
                for (int x = 0; x < mapWidth; x++) {
                    Cell cell = map.getCell(x, y);
                    switch (line[x]) {
                        // MAP ELEMENTS
                        case "ground_1" -> cell.setType(CellType.GROUND_1);
                        case "ground_2" -> cell.setType(CellType.GROUND_2);
                        case "ground_3" -> cell.setType(CellType.GROUND_3);
                        case "road_1" -> cell.setType(CellType.ROAD_1);
                        case "pine_tree_1" -> cell.setType(CellType.PINE_TREE_1);
                        case "pine_tree_2" -> cell.setType(CellType.PINE_TREE_2);
                        case "bridge_1" -> cell.setType(CellType.BRIDGE_1);
                        case "bridge_2" -> cell.setType(CellType.BRIDGE_2);
                        case "water_full" -> cell.setType(CellType.WATER_FULL);
                        case "water_corner" -> cell.setType(CellType.WATER_CORNER);
                        case "boat_1" -> cell.setType(CellType.BOAT_1);
                        case "torch_1" -> cell.setType(CellType.TORCH_1);
                        case "cat" -> cell.setType(CellType.CAT);

                        // TROOPS
                        case "swordsman" -> {
                            cell.setType(CellType.GROUND_1);
                            new Swordsman(cell, map.getPlayer(0));
                        }
                        case "skeleton" -> {
                            cell.setType(CellType.GROUND_1);
                            new Skeleton(cell, map.getPlayer(1));
                        }

                        // CITIES
                        case "city_1" -> new City(cell);

                        // NOT FOUND ELEMENT
                        default -> LOGGER.warning("Cell type " + line[x] + " not found!");
                    }
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            LOGGER.warning("File not found!");
            map = new GameMap(-1, -1, CellType.EMPTY);
        }
        return map;
    }

    private static void readMapWidthAndHeightFromCsv(int mapNumber) throws FileNotFoundException {
        String file = String.format("src/main/resources/maps/%d/map_%d.csv", mapNumber, mapNumber);
        String line = "";
        try (Scanner s =
                     new Scanner(new File(file))) {
            while (s.hasNext()) {
                line = s.next();
                mapHeight++;
            }
            mapWidth = line.split(",").length;
        } catch (Exception e) {
            LOGGER.warning("Problem with reading width and height from map: " + file);
        }
    }
}
