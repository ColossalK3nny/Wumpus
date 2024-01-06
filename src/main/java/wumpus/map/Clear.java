package wumpus.map;

import java.io.IOException;

public class Clear {
    public static void clearScreen() {
        try {
            new ProcessBuilder("cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
