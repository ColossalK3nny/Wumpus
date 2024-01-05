package wumpus;

import wumpus.menu.MainMenu;
import wumpus.map.Room;
import wumpus.Wumpus;
import java.util.Scanner;
import java.util.List;
import wumpus.Database.Database;

public class Main {
    public static String playerName;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printFrame();
        System.out.print("Please enter your name: ");
        playerName = scanner.nextLine();
        System.out.println("Hi " + playerName + "! Welcome to the game!");

        Room[][] rooms = initializeRooms(10, 10);
        Wumpus wumpus = new Wumpus(0, 0, 'N');

        MainMenu mainMenu = new MainMenu(playerName, wumpus, rooms);

        boolean isRunning = true;
        while (isRunning) {
            mainMenu.show();
            int option = mainMenu.getOption();
            switch (option) {
                case 1:
                    mainMenu.loadFromFile();
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
                    System.out.println("Exiting the game. Goodbye " + playerName + "!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        }
    }
    private static void printFrame() {
        String border = "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww";
        System.out.println(border);
        System.out.println("w                        Welcome to the Wumpus game                    w");
        System.out.println("w                 This is where you can feel the real fear             w");
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
}
