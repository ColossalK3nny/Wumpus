package wumpus.menu;

import wumpus.Main;
import wumpus.map.Room;
import wumpus.Player.Hero;
import java.util.Scanner;
import wumpus.Database.Database;
import wumpus.Database.GameState;
public class GameMenu {
    private Scanner scanner;
    private Room[][] rooms;
    private Hero hero;
    private int heroRow;
    private int heroColumn;
    private int initialHeroRow;
    private int initialHeroColumn;
    private boolean isGameRunning;
    private boolean isGameInitialized;
    private int stepsTaken;
    private MainMenu mainMenu;
    private int wumpusCount;
    public GameMenu(Room[][] rooms, int heroRow, int heroColumn, char heroDirection, int wumpusCount, MainMenu mainMenu) {
        this.scanner = new Scanner(System.in);
        this.rooms = rooms;
        this.heroRow = heroRow;
        this.heroColumn = heroColumn;
        this.initialHeroRow = heroRow;
        this.initialHeroColumn = heroColumn;
        this.hero = new Hero(heroDirection, wumpusCount);
        this.isGameInitialized = false;
        this.isGameRunning = true;
        this.stepsTaken = 0;
        this.mainMenu = mainMenu;
        this.wumpusCount = wumpusCount;
    }
    public void showHeroArrows() {
        System.out.println("The hero has " + hero.getArrows() + " arrows.");
    }
    public void show() {
        while (isGameRunning) {
            if (!isGameInitialized) {
                initializeGame();
                isGameInitialized = true;
            }
            updateAndDrawBoard();
            showHeroArrows();
            System.out.println("Choose an action:");
            System.out.println("1 - Step");
            System.out.println("2 - Turn left");
            System.out.println("3 - Turn right");
            System.out.println("4 - Shoot");
            System.out.println("5 - Pick gold");
            System.out.println("6 - Save State");
            System.out.println("7 - Quit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    moveHero();
                    stepsTaken++;
                    break;
                case 2:
                    hero.turnLeft();
                    stepsTaken++;
                    break;
                case 3:
                    hero.turnRight();
                    stepsTaken++;
                    break;
                case 4:
                    shootArrow();
                    break;
                case 5:
                    pickGold();
                    break;
                case 6:
                    GameState gameState = new GameState(
                            rooms, heroRow, heroColumn, hero.getHeroDirection(),
                            wumpusCount, hero.getArrows(), hero.hasGold());
                    gameState.save("src/main/resources/Database/gamestate.json");
                    System.out.println("Game state saved successfully.");
                    break;
                case 7:
                    System.out.println("See you later " + Main.playerName + "!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
            if (hero.hasGold() && heroRow == initialHeroRow && heroColumn == initialHeroColumn) {
                System.out.println("Congratulations, you have completed the mission!");
                System.out.println("Your steps number is: " + stepsTaken);
                Database.insertOrUpdateScore(Main.playerName); // Frissítjük a nyert meccsek számát
                isGameRunning = false;
                mainMenu.show();
                int option = mainMenu.getOption();
            }
        }
    }
    private void initializeGame() {
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[i].length; j++) {
                if (rooms[i][j].getSymbol() == 'H') {
                    heroRow = i;
                    heroColumn = j;
                    initialHeroRow = i;
                    initialHeroColumn = j;
                    rooms[i][j].setSymbol('_');
                    return;
                }
            }
        }
    }
    private void moveHero() {
        int newRow = heroRow;
        int newColumn = heroColumn;
        switch (hero.getHeroDirection()) {
            case 'N':
                newRow = Math.max(heroRow - 1, 0);
                break;
            case 'E':
                newColumn = Math.min(heroColumn + 1, rooms[0].length - 1);
                break;
            case 'S':
                newRow = Math.min(heroRow + 1, rooms.length - 1);
                break;
            case 'W':
                newColumn = Math.max(heroColumn - 1, 0);
                break;
        }
        char newSymbol = rooms[newRow][newColumn].getSymbol();
        if (newSymbol == 'W') {
            System.out.println("You bumped into a wall!");
        } else if (newSymbol == 'U') {
            System.out.println("You have been killed by a Wumpus! Game over.");
            System.exit(0);
        } else if (newSymbol == 'P') {
            hero.fallIntoPit();
        } else {
            rooms[heroRow][heroColumn].setSymbol('_');
            heroRow = newRow;
            heroColumn = newColumn;
            rooms[heroRow][heroColumn].setSymbol('H');
        }
    }
    private void updateAndDrawBoard() {
        System.out.print("   ");
        for (int i = 0; i < rooms[0].length; i++) {
            System.out.print((char) ('A' + i) + " ");
        }
        System.out.println();
        for (int i = 0; i < rooms.length; i++) {
            System.out.print((i < 10 ? " " : "") + i + " ");
            for (int j = 0; j < rooms[i].length; j++) {
                char displayChar = (i == heroRow && j == heroColumn) ? 'H' : rooms[i][j].getSymbol();
                System.out.print(displayChar + " ");
            }
            System.out.println();
        }
        hero.printHeroDirection();
        if (hero.hasGold()) {
            System.out.println("You are very very rich!");
        }
    }
    public void shootArrow() {
        if (hero.getArrows() > 0) {
            boolean hitWall = false;
            boolean hitWumpus = false;
            int row = heroRow;
            int col = heroColumn;

            while (!hitWall && !hitWumpus) {
                // Mozgatja a nyilat az adott irányban
                switch (hero.getHeroDirection()) {
                    case 'N':
                        row--;
                        break;
                    case 'E':
                        col++;
                        break;
                    case 'S':
                        row++;
                        break;
                    case 'W':
                        col--;
                        break;
                }
                if (row < 0 || row >= rooms.length || col < 0 || col >= rooms[0].length || rooms[row][col].getSymbol() == 'W') {
                    hitWall = true;
                    System.out.println("Your arrow hit a wall and destroyed.");
                } else {
                    // Ellenőrzi a szomszédos cellákat Wumpus jelenlétére
                    hitWumpus = checkForWumpusAround(row, col);
                    if (hitWumpus) {
                        System.out.println("You have killed a Wumpus!");
                    }
                }
            }
            hero.shootArrow(); // Csökkenti a nyilak számát
        } else {
            System.out.println("You have no more arrows.");
        }
    }
    private boolean checkForWumpusAround(int row, int col) {
        // Ellenőrzi a szomszédos cellákat
        for (int i = Math.max(row - 1, 0); i <= Math.min(row + 1, rooms.length - 1); i++) {
            for (int j = Math.max(col - 1, 0); j <= Math.min(col + 1, rooms[0].length - 1); j++) {
                if (rooms[i][j].getSymbol() == 'U') {
                    rooms[i][j].setSymbol('_'); // Eltávolítja a Wumpust
                    return true; // Megtalálta a Wumpust
                }
            }
        }
        return false; // Nem talált Wumpust
    }
    private void pickGold() {
        if (rooms[heroRow][heroColumn].hasGoldOriginally()) {
            hero.pickUpGold();
            rooms[heroRow][heroColumn].setSymbol('_');
            System.out.println("You picked up the gold.");
        } else {
            System.out.println("There is no gold on this spot.");
        }
    }
}
