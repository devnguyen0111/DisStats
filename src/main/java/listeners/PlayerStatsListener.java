package listeners;

import org.bukkit.Statistic;
import org.disstats.DisStats;
import data.PlayerStats;
import storage.SQLiteStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.sql.SQLException;
import java.util.UUID;
public class PlayerStatsListener implements Listener {
    private final SQLiteStorage storage;

    public PlayerStatsListener(SQLiteStorage storage) {
        this.storage = storage;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        try {
            PlayerStats.StatsData statsData = storage.loadPlayerStats(playerId);

            if (statsData == null) {
                // Create new stats data for the player if it doesn't exist
                statsData = new PlayerStats.StatsData(0, 0, 0, 0L);
                storage.savePlayerStats(playerId, statsData);
            }
        } catch (SQLException e) {
            // Handle database error
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Update and save the player's stats
        PlayerStats.StatsData statsData = new PlayerStats.StatsData(
                player.getStatistic(org.bukkit.Statistic.PLAYER_KILLS),
                player.getStatistic(org.bukkit.Statistic.DEATHS),
                player.getLevel(),
                player.getStatistic(Statistic.PLAY_ONE_MINUTE)
        );

        try {
            storage.savePlayerStats(playerId, statsData);
        } catch (SQLException e) {
            // Handle database error
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerId = player.getUniqueId();

        // Increment death count
        try {
            PlayerStats.StatsData statsData = storage.loadPlayerStats(playerId);

            if (statsData != null) {
                statsData.setDeaths(statsData.getDeaths() + 1);
                storage.savePlayerStats(playerId, statsData);
            }
        } catch (SQLException e) {
            // Handle database error
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Update player's level
        try {
            PlayerStats.StatsData statsData = storage.loadPlayerStats(playerId);

            if (statsData != null) {
                statsData.setLevel(player.getLevel());
                storage.savePlayerStats(playerId, statsData);
            }
        } catch (SQLException e) {
            // Handle database error
            e.printStackTrace();
        }
    }
}