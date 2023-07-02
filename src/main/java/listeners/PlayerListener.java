package listeners;

import data.PlayerStats;
import storage.SQLiteStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.UUID;
public class PlayerListener implements Listener {
    private final SQLiteStorage storage;

    public PlayerListener(SQLiteStorage storage) {
        this.storage = storage;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        try {
            PlayerStats.StatsData stats = storage.loadPlayerStats(playerId);

            if (stats == null) {
                // Create a new stats data object for the player
                stats = new PlayerStats.StatsData();
                storage.savePlayerStats(playerId, stats);
            }

            // Perform any necessary actions with the player stats
            // For example, you can increment the total time played or assign a starting level

        } catch (SQLException e) {
            player.sendMessage("An error occurred while loading your stats.");
            e.printStackTrace();
        }
    }
}
