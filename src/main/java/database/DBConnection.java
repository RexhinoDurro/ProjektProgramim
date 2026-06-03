package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String DB_URL = "jdbc:sqlite:kafe.db";

    private DBConnection() {}

    /**
     * Kthen një lidhje aktive me SQLite.
     * SQLite JDBC krijon skedarin automatikisht nëse nuk ekziston.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Thirret një herë gjatë nisjes së aplikacionit.
     * Krijon tabelën "porosite" nëse nuk ekziston tashmë.
     */
    public static void krijoTabelen() {
        String sql = """
                CREATE TABLE IF NOT EXISTS porosite (
                    id       INTEGER PRIMARY KEY AUTOINCREMENT,
                    tavolina INTEGER NOT NULL,
                    artikulli TEXT   NOT NULL,
                    sasia    INTEGER NOT NULL,
                    statusi  TEXT    NOT NULL
                );
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("[DB] Tabela 'porosite' u verifikua/krijua me sukses.");

        } catch (SQLException e) {
            System.err.println("[DB ERROR] Gabim gjatë krijimit të tabelës: " + e.getMessage());
        }
    }
}
