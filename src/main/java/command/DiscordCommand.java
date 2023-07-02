package command;

import data.PlayerStats;
import storage.SQLiteStorage;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.UUID;

public class DiscordCommand implements CommandExecutor {
    private final SQLiteStorage storage;
    private final TextChannel destinationChannel;

    public DiscordCommand(SQLiteStorage storage, JavaPlugin plugin) {
        this.storage = storage;

        // Load the Discord channel ID from config.yml
        FileConfiguration config = plugin.getConfig();
        String channelId = config.getString("discord.channel-id");
        this.destinationChannel = DiscordSRV.getPlugin().getJda().getTextChannelById(channelId);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerId = player.getUniqueId();

            try {
                PlayerStats.StatsData stats = storage.loadPlayerStats(playerId);

                if (stats != null) {
                    // Send the player's stats to Discord
                    if (destinationChannel != null) {
                        String message = "Player Stats for " + player.getName() + ":\n" +
                                "Kills: " + stats.getKills() + "\n" +
                                "Deaths: " + stats.getDeaths() + "\n" +
                                "Level: " + stats.getLevel() + "\n" +
                                "Total Time Played: " + stats.getTotalTimePlayed() + "ms";

                        destinationChannel.sendMessage(message).queue();
                        player.sendMessage("Player stats sent to Discord.");
                    } else {
                        player.sendMessage("Unable to send player stats to Discord. Destination channel not found.");
                    }
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
