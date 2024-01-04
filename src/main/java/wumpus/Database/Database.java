package wumpus.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Database {
    private static final String DB_URL = "jdbc:sqlite:src/main/resources/Database/wumpusdb.db";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    public static void initialize() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // Pontszámok táblája
            stmt.execute("CREATE TABLE IF NOT EXISTS scores (" +
                    "id INTEGER PRIMARY KEY," +
                    "player_name TEXT NOT NULL," +
                    "score INTEGER NOT NULL)");
            // Játékállapotok táblája
            stmt.execute("CREATE TABLE IF NOT EXISTS game_states (" +
                    "id INTEGER PRIMARY KEY," +
                    "player_name TEXT NOT NULL," +
                    "state TEXT NOT NULL," + // JSON formátumú játékállapot
                    "save_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        } catch (SQLException e) {
            System.out.println("Hiba az adatbázis inicializálásakor: " + e.getMessage());
        }
    }
    public static void insertScore(String playerName, int score) {
        String sql = "INSERT INTO scores(player_name, score) VALUES(?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Hiba a pontszám beillesztésekor: " + e.getMessage());
        }
    }
    public static void saveGameState(String playerName, String gameState) {
        String sql = "INSERT INTO game_states(player_name, state) VALUES(?,?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.setString(2, gameState);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Hiba a játékállapot mentésekor: " + e.getMessage());
        }
    }
    public static String loadLastGameState(String playerName) {
        String sql = "SELECT state FROM game_states WHERE player_name = ? ORDER BY save_time DESC LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("state");
                }
            }
        } catch (SQLException e) {
            System.out.println("Hiba a játékállapot betöltésekor: " + e.getMessage());
        }
        return null;
    }
    public static List<String> getHighScores() {
        List<String> highScores = new ArrayList<>();
        String sql = "SELECT player_name, score FROM scores ORDER BY score DESC";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String playerName = rs.getString("player_name");
                int score = rs.getInt("score");
                highScores.add(playerName + ": " + score);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving high scores: " + e.getMessage());
        }
        return highScores;
    }
}

