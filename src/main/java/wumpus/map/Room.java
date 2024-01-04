package wumpus.map;

import java.util.Set;

public class Room {
    private final Set<Character> VALID_SYMBOLS = Set.of('W', 'H', 'U', 'P', 'G', '_');
    private char symbol;
    private boolean hasGoldOriginally; //arany kezdeti jelenlétének tárolása

    public Room(char symbol) {
        this.hasGoldOriginally = (symbol == 'G');
        setSymbol(symbol);
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        if (!VALID_SYMBOLS.contains(symbol)) {
            throw new IllegalArgumentException("A szoba nem tartalmazhatja ezt a karaktert: " + symbol);
        }
        this.symbol = symbol;
    }

    public boolean hasGoldOriginally() {
        return hasGoldOriginally;
    }
}
