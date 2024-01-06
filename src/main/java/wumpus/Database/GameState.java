package wumpus.Database;

import com.google.gson.Gson;
import wumpus.map.Room;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class GameState {
    private Room[][] rooms;
    private int heroRow;
    private int heroColumn;
    private char heroDirection;
    private int wumpusCount;
    private int heroArrows;
    private boolean heroHasGold;
    public GameState(Room[][] rooms, int heroRow, int heroColumn, char heroDirection, int wumpusCount, int heroArrows, boolean heroHasGold) {
        this.rooms = rooms;
        this.heroRow = heroRow;
        this.heroColumn = heroColumn;
        this.heroDirection = heroDirection;
        this.wumpusCount = wumpusCount;
        this.heroArrows = heroArrows;
        this.heroHasGold = heroHasGold;
    }

    public int getHeroArrows() {
        return heroArrows;
    }

    public void setHeroArrows(int heroArrows) {
        this.heroArrows = heroArrows;
    }

    public boolean isHeroHasGold() {
        return heroHasGold;
    }

    public void setHeroHasGold(boolean heroHasGold) {
        this.heroHasGold = heroHasGold;
    }

    public Room[][] getRooms() {
        return rooms;
    }

    public void setRooms(Room[][] rooms) {
        this.rooms = rooms;
    }

    public int getHeroRow() {
        return heroRow;
    }

    public void setHeroRow(int heroRow) {
        this.heroRow = heroRow;
    }

    public int getHeroColumn() {
        return heroColumn;
    }

    public void setHeroColumn(int heroColumn) {
        this.heroColumn = heroColumn;
    }

    public char getHeroDirection() {
        return heroDirection;
    }

    public void setHeroDirection(char heroDirection) {
        this.heroDirection = heroDirection;
    }

    public int getWumpusCount() {
        return wumpusCount;
    }

    public void setWumpusCount(int wumpusCount) {
        this.wumpusCount = wumpusCount;
    }

    // JSON mentése
    public void save(String filePath) {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            System.out.println("Hiba történt a játékállapot mentése közben: " + e.getMessage());
        }
    }

    // JSON betöltése
    public static GameState loadFromFile(String filePath) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, GameState.class);
        } catch (IOException e) {
            System.out.println("Hiba történt a játékállapot betöltése közben: " + e.getMessage());
            return null;
        }
    }
}
