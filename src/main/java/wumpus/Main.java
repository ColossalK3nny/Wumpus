package wumpus;

import wumpus.map.Room;
import wumpus.map.MapLoader;
import wumpus.menu.MainMenu;
import wumpus.menu.GameMenu;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import wumpus.map.Clear;
import wumpus.Database.Database;
import java.util.List;
public class Main {
    public static String playerName;
    public static void main(String[] args) {
        Database.initialize();
        Scanner scanner = new Scanner(System.in);
        printFrame();
        System.out.print("Please enter your name: ");
        playerName = scanner.nextLine(); // Itt már beállítjuk az osztályszintű változót
        System.out.println("Hi " + playerName + "! Welcome to the game!");
        System.out.println("Choose an option");
        Room[][] rooms = initializeRooms(10, 10);
        Wumpus wumpus = new Wumpus(0, 0, 'N');
        MainMenu mainMenu = new MainMenu(playerName, wumpus, rooms);
        mainMenu.show();
        do {
            int option = mainMenu.getOption();
            switch (option) {
                case 1:
                    try {
                        MapLoader mapLoader = new MapLoader();
                        rooms = mapLoader.loadMap("wumpuszinput.txt");
                        System.out.println("Room sucessfully loaded.");
                        printBoard(rooms);
                    } catch (IOException e) {
                        System.out.println("An error occurred while loading the room: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 2:

                    break;
                case 3:
                    List<String> highScores = Database.getHighScores();
                    for (String score : highScores) {
                        System.out.println(score);
                    }

                    break;
                case 4:
                    System.out.println("See you later " + playerName + "!");
                    System.exit(0);
                    break;
            }
        } while (true);
    }
    private static void printFrame() {
        String border = "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww";
        System.out.println(border);
        System.out.println("w                        Welcome to Wumpus game                        w");
        System.out.println("w                                                                      w");
        System.out.println(border);
    }
    private static Room[][] initializeRooms(int width, int height) {
        Room[][] rooms = new Room[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rooms[i][j] = new Room('_'); // Tételezzük fel, hogy minden szoba kezdetben üres
            }
        }
        return rooms;
    }
    private static void printBoard(Room[][] rooms) {
        System.out.println("   " + IntStream.range('A', 'A' + rooms[0].length)
                .mapToObj(i -> Character.toString((char) i))
                .collect(Collectors.joining(" ")));
        for (int i = 0; i < rooms.length; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < rooms[i].length; j++) {
                System.out.print(rooms[i][j].getSymbol() + " ");
            }
            System.out.println();
        }
    }
}
