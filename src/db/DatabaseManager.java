package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/battlegame";
    private static final String USER = "postgres";
    private static final String PASSWORD = "kometa0707";

    public static void initDatabase() {
        String createTable = """
            CREATE TABLE IF NOT EXISTS battles (
                id SERIAL PRIMARY KEY,
                hero1 VARCHAR(50),
                hero2 VARCHAR(50),
                winner VARCHAR(50),
                hero1_health INT,
                hero2_health INT,
                battle_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """;


        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(createTable)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveBattle(String hero1, String hero2, String winner,
                                  int hero1Health, int hero2Health) {
        String insert = "INSERT INTO battles(hero1, hero2, winner, hero1_health, hero2_health) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insert)) {

            stmt.setString(1, hero1);
            stmt.setString(2, hero2);
            stmt.setString(3, winner);
            stmt.setInt(4, hero1Health);
            stmt.setInt(5, hero2Health);

            int rows = stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
