package data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class PlayerStats {
    private final Map<UUID, StatsData> playerStats;

    public PlayerStats() {
        this.playerStats = new HashMap<>();
    }

    public void setKillCount(UUID playerId, int kills) {
        StatsData stats = getPlayerStats(playerId);
        stats.setKills(kills);
    }

    public void setDeathCount(UUID playerId, int deaths) {
        StatsData stats = getPlayerStats(playerId);
        stats.setDeaths(deaths);
    }

    public void setLevel(UUID playerId, int level) {
        StatsData stats = getPlayerStats(playerId);
        stats.setLevel(level);
    }

    public void setTotalTimePlayed(UUID playerId, int totalTimePlayed) {
        StatsData stats = getPlayerStats(playerId);
        stats.setTotalTimePlayed(totalTimePlayed);
    }

    public StatsData getPlayerStats(UUID playerId) {
        return playerStats.computeIfAbsent(playerId, id -> new StatsData());
    }

    public void clearPlayerStats(UUID playerId) {
        playerStats.remove(playerId);
    }

    public static class StatsData {
        private int kills;
        private int deaths;
        private int level;
        private long totalTimePlayed;

        public StatsData() {
            this.kills = 0;
            this.deaths = 0;
            this.level = 0;
            this.totalTimePlayed = 0;
        }

        public StatsData(int kills, int deaths, int level, long totalTimePlayed) {
            this.kills = kills;
            this.deaths = deaths;
            this.level = level;
            this.totalTimePlayed = totalTimePlayed;
        }

        public int getKills() {
            return kills;
        }

        public void setKills(int kills) {
            this.kills = kills;
        }

        public int getDeaths() {
            return deaths;
        }

        public void setDeaths(int deaths) {
            this.deaths = deaths;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public long getTotalTimePlayed() {
            return totalTimePlayed;
        }

        public void setTotalTimePlayed(long totalTimePlayed) {
            this.totalTimePlayed = totalTimePlayed;
        }
    }

}
