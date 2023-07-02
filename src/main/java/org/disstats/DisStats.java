package org.disstats;

import command.StatsCommand;
import data.PlayerStats;
import listeners.PlayerStatsListener;
import storage.SQLiteStorage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.UUID;

public class DisStats extends JavaPlugin {
    private SQLiteStorage storage;
    private Economy economy;

    @Override
    public void onEnable() {
        // Create plugin folder
        File pluginFolder = getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }

        // Create config.yml if it doesn't exist
        File configFile = new File(pluginFolder, "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }

        // Initialize SQLite storage
        try {
            File databaseFile = new File(pluginFolder, "stats.db");
            storage = new SQLiteStorage(databaseFile);
            storage.createTables();
        } catch (SQLException e) {
            getLogger().severe("Failed to initialize SQLite storage.");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Hook into Vault for economy

        // Register commands
        getCommand("statspl").setExecutor(new StatsCommand(storage, this));

        // Register events
        getServer().getPluginManager().registerEvents(new PlayerStatsListener(storage), this);

        getLogger().info("DisStats has been enabled.");
    }

    @Override
    public void onDisable() {
        if (storage != null) {
            try {
                storage.close();
            } catch (SQLException e) {
                getLogger().severe("Failed to close SQLite storage.");
                e.printStackTrace();
            }
        }

        getLogger().info("DisStats has been disabled.");
    }

}

