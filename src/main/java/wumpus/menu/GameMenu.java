package wumpus.menu;

import wumpus.Main;
import wumpus.map.Room;
import wumpus.Player.Hero;
import java.util.Scanner;
import wumpus.Database.Database;
public class GameMenu {
    private Scanner scanner;
    private Room[][] rooms;
    private Hero hero;
    private int heroRow;
    private int heroColumn;
    private int initialHeroRow;  // Initial row of the hero
    private int initialHeroColumn;  // Initial column of the hero
    private boolean isGameRunning;
    private boolean isGameInitialized;
    private int stepsTaken;  // Steps taken by the hero
    public GameMenu(Room[][] rooms, int heroRow, int heroColumn, char heroDirection, int wumpusCount) {
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
            System.out.println("6 - Quit");
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
                    System.out.println("See you later " + Main.playerName + "!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
            if (hero.hasGold() && heroRow == initialHeroRow && heroColumn == initialHeroColumn) {
                System.out.println("Congratulations, you have completed the mission! It took you " + stepsTaken + " steps.");
                Database.insertScore(Main.playerName, stepsTaken);
                isGameRunning = false;
            }
        }
    }
    private void initializeGame() {
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[i].length; j++) {
                if (rooms[i][j].getSymbol() == 'H') {
                    heroRow = i;
                    heroColumn = j;
                    initialHeroRow = i; // Set initial row
                    initialHeroColumn = j; // Set initial column
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
    }
    public void shootArrow() {
        if (hero.getArrows() > 0) {
            boolean hitWall = false;
            boolean hitWumpus = false;
            int row = heroRow;
            int col = heroColumn;

            while (!hitWall && !hitWumpus) {
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
                if (row < 0 || row >= rooms.length || col < 0 || col >= rooms[0].length) {
                    hitWall = true;
                } else if (rooms[row][col].getSymbol() == 'W') {
                    hitWall = true;
                } else if (rooms[row][col].getSymbol() == 'U') {
                    rooms[row][col].setSymbol('_');
                    hitWumpus = true;
                }
            }
            if (hitWumpus) {
                System.out.println("You have killed a Wumpus!");
            } else {
                System.out.println("Your arrow hit a wall or disappeared.");
            }
            hero.shootArrow();
        } else {
            System.out.println("You have no more arrows.");
        }
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
