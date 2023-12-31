package wumpus.menu;

import wumpus.map.MapLoader;
import wumpus.map.Room;
import wumpus.Wumpus;
import java.util.Scanner;
import java.io.IOException;
import wumpus.Database.GameState;
import wumpus.Player.Hero;
public class MainMenu {

    private String playerName;
    private Wumpus wumpus;
    private Room[][] rooms;
    public MainMenu(String playerName, Wumpus wumpus, Room[][] rooms) {
        this.playerName = playerName;
        this.wumpus = wumpus;
        this.rooms = rooms;
    }
    public void show() {
        System.out.println("1-> Room load from file");
        System.out.println("2-> Game load");
        System.out.println("3-> High Score");
        System.out.println("4-> Quit");
    }

    public int getOption() {
        int option = -1;
        Scanner scanner = new Scanner(System.in);

        while (option < 1 || option > 5) {
            System.out.print("Choice: ");
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please enter another number!");
            }
        }
        return option;
    }
    public void startGame() {
        // Játék indítása
        MapLoader mapLoader = new MapLoader();
        try {
            // Átadjuk a this referenciát, ami a MainMenu példány
            Room[][] rooms = mapLoader.loadMap("wumpuszinput.txt", this);
            // ... további logika ...
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadFromFile() {
        try {
            MapLoader mapLoader = new MapLoader();
            this.rooms = mapLoader.loadMap("wumpuszinput.txt", this); // Frissítse a rooms tömböt a betöltött pályával
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void load() {
        GameState gameState = GameState.loadFromFile("Wumpus/gamestate.json");
        if (gameState != null) {
            this.rooms = gameState.getRooms();
            int heroRow = gameState.getHeroRow();
            int heroColumn = gameState.getHeroColumn();
            char heroDirection = gameState.getHeroDirection();
            int wumpusCount = gameState.getWumpusCount();
            Hero hero = new Hero(heroDirection, wumpusCount);
            hero.setArrows(gameState.getHeroArrows());
            hero.setHasGold(gameState.isHeroHasGold());

            GameMenu gameMenu = new GameMenu(rooms, heroRow, heroColumn, heroDirection, wumpusCount, this);

            gameMenu.show();
        } else {
            System.out.println("Nem sikerült betölteni a mentett játékot.");
        }
    }
}
