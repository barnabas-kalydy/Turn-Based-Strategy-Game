package com.codecool.dungeoncrawl.logic.maps;

import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;
import com.codecool.dungeoncrawl.logic.drawable.cells.CellType;
import com.codecool.dungeoncrawl.logic.drawable.troops.Skeleton;
import com.codecool.dungeoncrawl.logic.drawable.troops.Swordsman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MapLoader {

    public static GameMap loadMapFromCsv(int mapNumber) {

        // todo replace this with live width and height reading from csv file
        int mapWidth = 30;
        int mapHeight = 20;

        GameMap map = new GameMap(mapWidth, mapHeight, CellType.EMPTY);
        try {

            Scanner sc = new Scanner(new File(
                    String.format("src/main/resources/maps/%d/map_%d.csv", mapNumber, mapNumber)));
            for (int y = 0; y < mapHeight; y++) {
                String[] line = sc.nextLine().split(",");
                for (int x = 0; x < mapWidth; x++) {
                    Cell cell = map.getCell(x, y);
                    switch (line[x]) {
                        case "ground_1" -> {
                            cell.setType(CellType.GROUND_1);
                        }
                        case "pine_tree_1" -> {
                            cell.setType(CellType.PINE_TREE_1);
                        }
                        case "pine_tree_2" -> {
                            cell.setType(CellType.PINE_TREE_2);
                        }
                        case "house_1" -> {
                            cell.setType(CellType.HOUSE_1);
                        }
                        case "house_2" -> {
                            cell.setType(CellType.HOUSE_2);
                        }
                        case "bridge_1" -> {
                            cell.setType(CellType.BRIDGE_1);
                        }
                        case "bridge_2" -> {
                            cell.setType(CellType.BRIDGE_2);
                        }
                        case "water_full" -> {
                            cell.setType(CellType.WATER_FULL);
                        }
                        case "water_corner" -> {
                            cell.setType(CellType.WATER_CORNER);
                        }
                        case "boat_1" -> {
                            cell.setType(CellType.BOAT_1);
                        }
                        case "torch_1" -> {
                            cell.setType(CellType.TORCH_1);
                        }
                        case "swordsman" -> {
                            cell.setType(CellType.GROUND_1);
                            new Swordsman(cell, map.getPlayer(0));
                        }
                        case "skeleton" -> {
                            cell.setType(CellType.GROUND_1);
                            new Skeleton(cell, map.getPlayer(1));
                        }
                    }
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

}
