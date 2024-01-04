package wumpus.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import wumpus.menu.GameMenu;

public class MapLoader {
    private Room[][] rooms;

    public Room[][] loadMap(String fileName) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is == null) {
            throw new IOException("File not found: " + fileName);
        }

        int wumpusCount = 0; // Count the number of Wumpuses

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String firstLine = reader.readLine();
            String[] parts = firstLine.split(" ");
            int size = Integer.parseInt(parts[0]);
            rooms = new Room[size][size];

            char heroColumn = parts[1].charAt(0);
            int heroRow = Integer.parseInt(parts[2]);
            char heroDirection = parts[3].charAt(0);

            for (int i = 0; i < size; i++) {
                String line = reader.readLine();
                for (int j = 0; j < size; j++) {
                    char symbol = line.charAt(j);
                    rooms[i][j] = new Room(symbol);
                    if (symbol == 'U') {
                        wumpusCount++;
                    }
                }
            }
            if (rooms[heroRow][heroColumn - 'A'].getSymbol() == '_') {
                rooms[heroRow][heroColumn - 'A'].setSymbol('H');
            } else {
                throw new IllegalArgumentException("Invalid hero starting position");
            }
            GameMenu gameMenu = new GameMenu(rooms, heroRow, heroColumn - 'A', heroDirection, wumpusCount);
            gameMenu.show();
        }

        return rooms;
    }
}
