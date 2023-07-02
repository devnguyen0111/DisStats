package utils;

import data.PlayerStats;
import org.bukkit.ChatColor;
public class Utils {
    public static String formatStatsMessage(PlayerStats.StatsData stats) {
        StringBuilder message = new StringBuilder();
        message.append(ChatColor.BOLD).append("=== Stats ===").append(ChatColor.RESET).append("\n");
        message.append("Kills: ").append(stats.getKills()).append("\n");
        message.append("Deaths: ").append(stats.getDeaths()).append("\n");
        message.append("Level: ").append(stats.getLevel()).append("\n");
        message.append("Total Time Played: ").append(stats.getTotalTimePlayed()).append("ms");
        return message.toString();
    }
}
