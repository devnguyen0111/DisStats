package storage;

import data.PlayerStats;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
public class SQLiteStorage {
    private final File databaseFile;
    private Connection connection;

    public SQLiteStorage(File databaseFile) throws SQLException {
        this.databaseFile = databaseFile;
        connect();
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getPath());
    }

    public void createTables() throws SQLException {
        String createStatsTable = "CREATE TABLE IF NOT EXISTS player_stats (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "player_uuid TEXT UNIQUE NOT NULL," +
                "kills INTEGER DEFAULT 0," +
                "deaths INTEGER DEFAULT 0," +
                "level INTEGER DEFAULT 0," +
                "total_time_played BIGINT DEFAULT 0" +
                ");";

        try (PreparedStatement stmt = connection.prepareStatement(createStatsTable)) {
            stmt.execute();
        }
    }

    public PlayerStats.StatsData loadPlayerStats(UUID playerId) throws SQLException {
        String query = "SELECT * FROM player_stats WHERE player_uuid = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerId.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int kills = rs.getInt("kills");
                    int deaths = rs.getInt("deaths");
                    int level = rs.getInt("level");
                    long totalTimePlayed = rs.getLong("total_time_played");
                    return new PlayerStats.StatsData(kills, deaths, level, totalTimePlayed);
                }
            }
        }

        return null;
    }

    public void savePlayerStats(UUID playerId, PlayerStats.StatsData statsData) throws SQLException {
        String query = "INSERT OR REPLACE INTO player_stats (player_uuid, kills, deaths, level, balance, total_time_played) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerId.toString());
            stmt.setInt(2, statsData.getKills());
            stmt.setInt(3, statsData.getDeaths());
            stmt.setInt(4, statsData.getLevel());
            stmt.setLong(6, statsData.getTotalTimePlayed());
            stmt.executeUpdate();
        }
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
