package command;

import data.PlayerStats;
import storage.SQLiteStorage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.sql.SQLException;
import java.util.UUID;
public class StatsCommand implements CommandExecutor {
    private final SQLiteStorage storage;

    public StatsCommand(SQLiteStorage storage, Plugin plugin) {
        this.storage = storage;

        // Initialize Vault economy
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerId = player.getUniqueId();

            try {
                PlayerStats.StatsData stats = storage.loadPlayerStats(playerId);

                if (stats != null) {

                    player.sendMessage("Player Stats for " + player.getName() + ":\n" +
                            "Kills: " + stats.getKills() + "\n" +
                            "Deaths: " + stats.getDeaths() + "\n" +
                            "Level: " + stats.getLevel() + "\n" +
                            "Total Time Played: " + stats.getTotalTimePlayed() + "ms");
                } else {
                    player.sendMessage("No stats found for your player.");
                }
            } catch (SQLException e) {
                player.sendMessage("An error occurred while fetching your stats.");
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }
}
