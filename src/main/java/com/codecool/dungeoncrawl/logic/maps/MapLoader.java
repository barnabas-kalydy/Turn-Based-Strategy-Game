package com.codecool.dungeoncrawl.logic.maps;

import com.codecool.dungeoncrawl.logic.drawable.actors.Player;
import com.codecool.dungeoncrawl.logic.drawable.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.drawable.cells.Cell;
import com.codecool.dungeoncrawl.logic.drawable.cells.CellType;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {

    public static GameMap loadMap() {
        InputStream is = MapLoader.class.getResourceAsStream("/map.txt");
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    // set cell types on every cell
                    switch (line.charAt(x)) {
                        //#####   MAP ELEMENTS    ########
                        case ' ':
                            // empty
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            // wall
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            // floor
                            cell.setType(CellType.FLOOR);
                            break;

                        //#####   ENTITIES    ########
                        case 's':
                            // skeleton
                            cell.setType(CellType.FLOOR);
                            new Skeleton(cell);
                            break;
                        case '@':
                            // player
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

}
