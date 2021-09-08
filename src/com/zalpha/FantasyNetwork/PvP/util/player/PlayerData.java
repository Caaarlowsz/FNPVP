package com.zalpha.FantasyNetwork.PvP.util.player;

import java.util.Collection;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import com.zalpha.FantasyNetwork.PvP.util.Enums;
import com.zalpha.FantasyNetwork.PvP.util.ScoreBoard;
import com.zalpha.FantasyNetwork.PvP.util.ConfigAPI;
import com.zalpha.FantasyNetwork.PvP.util.Manager;
import java.util.Iterator;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import com.zalpha.FantasyNetwork.PvP.util.storage.SQLDatabase;
import com.zalpha.FantasyNetwork.PvP.FantasyKits;
import org.bukkit.entity.Player;
import java.util.Map;

public class PlayerData
{
    private boolean admin;
    private boolean scoreb;
    private String clan;
    private int dinheiro;
    private int kills;
    private int killstreak;
    private int mortes;
    private int score;
    private int caixas;
    private int chaves;
    private static Map<Player, PlayerData> datas;
    private FantasyKits plugin;
    private Player player;
    private SQLDatabase data;
    
    static {
        PlayerData.datas = new HashMap<Player, PlayerData>();
    }
    
    public FantasyKits getPlugin() {
        return this.plugin;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public SQLDatabase getData() {
        return this.data;
    }
    
    public boolean isAdmin() {
        return this.admin;
    }
    
    public void setAdmin(final boolean admin) {
        this.admin = admin;
    }
    
    public static List<Player> getPlayersFrom(final List<String> names) {
        final List<Player> players = new ArrayList<Player>();
        for (final String name : names) {
            if (Bukkit.getPlayer(name) != null && !players.contains(Bukkit.getPlayer(name))) {
                players.add(Bukkit.getPlayer(name));
            }
        }
        return players;
    }
    
    public boolean getScoreB() {
        return this.scoreb;
    }
    
    public void setScore(final boolean b) {
        if (!b) {
            for (final String s : this.player.getScoreboard().getEntries()) {
                this.player.getScoreboard().resetScores(s);
            }
            this.scoreb = false;
        }
        else {
            this.scoreb = true;
            ScoreBoard.unrankedSidebarDisplay(this.player, new String[] { "§5§lFANTASY §e§lKITPVP", "§1", "§fGrupo: " + Manager.pegarRank(this.player), "§fLiga: " + this.getRank(), "§fDinheiro: " + Manager.modificarCoins(this.getMoney()) + "$", "§4", "§fKills: §a" + this.getKills(), "§fMortes: §c" + this.getDeaths(), "§0", "§fKS: §9" + this.getKillStreak(), "§fK.D: §e" + Manager.getKDR(this.player) + "%", "§fPontua\u00e7\u00e3o: §5" + Manager.modificarCoins(this.getScore()), "§2", "§ewww." + ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Site") });
        }
    }
    
    public PlayerData(final FantasyKits plugin, final Player p) {
        this.admin = false;
        this.scoreb = true;
        this.clan = "Nenhum";
        this.dinheiro = 0;
        this.kills = 0;
        this.killstreak = 0;
        this.mortes = 0;
        this.score = 0;
        this.caixas = 0;
        this.chaves = 0;
        this.player = p;
        this.plugin = plugin;
        this.data = new SQLDatabase(plugin, p);
        final String[] stats = this.data.getString("stats").split(" : ");
        this.clan = Enums.Stats.CLAN.getString(stats);
        this.dinheiro = Enums.Stats.DINHEIRO.getInt(stats);
        this.kills = Enums.Stats.KILLS.getInt(stats);
        this.killstreak = Enums.Stats.KILLSTREAK.getInt(stats);
        this.mortes = Enums.Stats.MORTES.getInt(stats);
        this.score = Enums.Stats.SCORE.getInt(stats);
        this.caixas = Enums.Stats.CAIXAS.getInt(stats);
        this.chaves = Enums.Stats.CHAVES.getInt(stats);
    }
    
    public String getClan() {
        return this.clan;
    }
    
    public int getMoney() {
        return this.dinheiro;
    }
    
    public int getKills() {
        return this.kills;
    }
    
    public int getKillStreak() {
        return this.killstreak;
    }
    
    public int getDeaths() {
        return this.mortes;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public int getCaixas() {
        return this.caixas;
    }
    
    public int getChaves() {
        return this.chaves;
    }
    
    public String getRank() {
        return Ranks.getRank(this.getScore()).getRankDisplay();
    }
    
    public String getRankSymbol() {
        return Ranks.getRank(this.getScore()).getSymbol();
    }
    
    public void setClan(final String clan) {
        this.clan = clan;
    }
    
    public void setMoney(final int money) {
        this.dinheiro = money;
    }
    
    public void addMoney(final int money) {
        this.dinheiro += money;
    }
    
    public void removeMoney(final int money) {
        if (this.dinheiro >= 0) {
            this.dinheiro -= money;
        }
        if (this.dinheiro < 0) {
            this.dinheiro = 0;
        }
    }
    
    public void setKills(final int Kills) {
        this.kills = Kills;
    }
    
    public void addKills(final int Kills) {
        this.kills += Kills;
    }
    
    public void removeKills(final int Kills) {
        if (this.kills >= 0) {
            this.kills -= Kills;
        }
        if (this.kills < 0) {
            this.kills = 0;
        }
    }
    
    public void setKillStreak(final int KillStreak) {
        this.killstreak = KillStreak;
    }
    
    public void addKillStreak(final int KillStreak) {
        this.killstreak += KillStreak;
    }
    
    public void removeKillStreak(final int KillStreak) {
        if (this.killstreak >= 0) {
            this.killstreak -= KillStreak;
        }
        if (this.killstreak < 0) {
            this.killstreak = 0;
        }
    }
    
    public void setDeaths(final int Deaths) {
        this.mortes = Deaths;
    }
    
    public void addDeaths(final int Deaths) {
        this.mortes += Deaths;
    }
    
    public void removeDeaths(final int Deaths) {
        if (this.mortes >= 0) {
            this.mortes -= Deaths;
        }
        if (this.mortes < 0) {
            this.mortes = 0;
        }
    }
    
    public void setScore(final int Score) {
        this.score = Score;
    }
    
    public void addScore(final int Score) {
        this.score += Score;
    }
    
    public void removeScore(final int Score) {
        if (this.score >= 0) {
            this.score -= Score;
        }
        if (this.score < 0) {
            this.score = 0;
        }
    }
    
    public void setCaixas(final int Caixas) {
        this.caixas = Caixas;
    }
    
    public void addCaixas(final int Caixas) {
        this.caixas += Caixas;
    }
    
    public void removeCaixas(final int Caixas) {
        if (this.caixas >= 0) {
            this.caixas -= Caixas;
        }
        if (this.caixas < 0) {
            this.caixas = 0;
        }
    }
    
    public void setChaves(final int Chaves) {
        this.chaves = Chaves;
    }
    
    public void addChaves(final int KillStreak) {
        this.chaves += KillStreak;
    }
    
    public void removeChaves(final int KillStreak) {
        if (this.chaves >= 0) {
            this.chaves -= KillStreak;
        }
        if (this.chaves < 0) {
            this.chaves = 0;
        }
    }
    
    public static void setRank(final String rank) {
    }
    
    public void save() {
        this.plugin.getStorage().execute("UPDATE kitpvp SET stats=? WHERE uuid=?", false, Enums.Stats.parseFrom(this), this.player.getUniqueId().toString());
    }
    
    public void saveAsync() {
        this.plugin.getStorage().execute("UPDATE kitpvp SET stats=? WHERE uuid=?", true, Enums.Stats.parseFrom(this), this.player.getUniqueId().toString());
    }
    
    public void updateInventory() {
        this.player.updateInventory();
    }
    
    public String getDisplayName() {
        return this.player.getDisplayName();
    }
    
    public PlayerInventory getInventory() {
        return this.player.getInventory();
    }
    
    public ItemStack getItemInHand() {
        return this.player.getItemInHand();
    }
    
    public Location getLocation() {
        return this.player.getLocation();
    }
    
    public World getWorld() {
        return this.player.getWorld();
    }
    
    public GameMode getGameMode() {
        return this.player.getGameMode();
    }
    
    public boolean teleport(final Location location) {
        return this.player.teleport(location);
    }
    
    public boolean hasPermission(final String permission) {
        return this.player.hasPermission(permission);
    }
    
    public static PlayerData create(final Player p) {
        if (PlayerData.datas.containsKey(p)) {
            return get(p);
        }
        PlayerData.datas.put(p, new PlayerData(FantasyKits.getMain(), p));
        return get(p);
    }
    
    public static void remove(final Player p) {
        if (PlayerData.datas.containsKey(p)) {
            PlayerData.datas.remove(p);
        }
    }
    
    public static PlayerData get(final Player p) {
        if (!PlayerData.datas.containsKey(p)) {
            return null;
        }
        return PlayerData.datas.get(p);
    }
    
    public static List<PlayerData> getDatas() {
        return new ArrayList<PlayerData>(PlayerData.datas.values());
    }
    
    public enum Ranks
    {
        UNRANKED("UNRANKED", 0, "§fNOT RANKED", "§f-", 400), 
        SILVER("SILVER", 1, "§7SILVER", "§7=", 800), 
        GOLD("GOLD", 2, "§6GOLD", "§6\u2630", 1400), 
        CRYSTAL("CRYSTAL", 3, "§dCRYSTAL", "§d\u2637", 2000), 
        MASTER("MASTER", 4, "§8MASTER", "§8\u2736", 2600), 
        CHAMPION("CHAMPION", 5, "§CCHAMPION", "§c\u2737", 3200), 
        TITAN("TITAN", 6, "§eTITAN", "§e\u2737", 4100), 
        LEGENDARY1("LEGENDARY1", 7, "§4LEGENDARY", "§4\u2742", 5000), 
        LEGENDARY("LEGENDARY", 8, "§4LEGENDARY", "§4\u2742", Integer.MAX_VALUE);
        
        private String rankdisplay;
        private String symbol;
        private int cost;
        
        public int getCost() {
            return this.cost;
        }
        
        public String getSymbol() {
            return this.symbol;
        }
        
        public String getRankDisplay() {
            return this.rankdisplay;
        }
        
        public int getMin() {
            int min = 0;
            if (this.ordinal() > 0) {
                min = values()[this.ordinal() - 1].getCost();
            }
            return min;
        }
        
        private Ranks(final String s, final int n, final String rankdisplay, final String symbol, final int cost) {
            this.cost = cost;
            this.symbol = symbol;
            this.rankdisplay = rankdisplay;
        }
        
        public static Ranks getRank(final int xp) {
            Ranks liga = Ranks.UNRANKED;
            Ranks[] arrayOfLiga;
            for (int j = (arrayOfLiga = values()).length, i = 0; i < j; ++i) {
                final Ranks rank = arrayOfLiga[i];
                if (xp <= rank.getCost() && xp >= rank.getMin()) {
                    liga = rank;
                }
            }
            return liga;
        }
    }
}
