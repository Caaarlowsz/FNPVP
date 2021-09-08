package com.zalpha.FantasyNetwork.PvP;

import me.zalpha.FantasyNetwork.PvP.Duelx1.Duelx1;
import com.zalpha.FantasyNetwork.PvP.abilities.Habilidades;
import me.zalpha.FantasyNetwork.PvP.Cmds.Admin;
import me.zalpha.FantasyNetwork.PvP.Cmds.ComandosUteis;
import me.zalpha.FantasyNetwork.PvP.Cmds.ComandosTP;
import org.bukkit.command.CommandExecutor;
import me.zalpha.FantasyNetwork.PvP.Cmds.ComandKit;
import java.util.List;
import com.zalpha.FantasyNetwork.PvP.util.TopKills;
import com.zalpha.FantasyNetwork.PvP.util.player.PlayerData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import com.zalpha.FantasyNetwork.PvP.util.storage.MySQL;
import com.zalpha.FantasyNetwork.PvP.util.storage.SQLite;
import java.io.File;
import com.zalpha.FantasyNetwork.PvP.util.ConfigAPI;
import org.bukkit.World;
import com.zalpha.FantasyNetwork.PvP.util.FeastAPI;
import com.zalpha.FantasyNetwork.PvP.util.Manager;
import java.util.HashMap;
import org.bukkit.entity.Player;
import java.util.Map;
import com.zalpha.FantasyNetwork.PvP.util.player.PlayerHide;
import org.bukkit.inventory.EnchantingInventory;
import java.util.ArrayList;
import com.zalpha.FantasyNetwork.PvP.util.storage.Sql;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class FantasyKits extends JavaPlugin implements Listener
{
    private PluginManager pm;
    private static Plugin plugin;
    private static FantasyKits main;
    public static FantasyKits instance;
    public Sql storage;
    public int time;
    public ArrayList<EnchantingInventory> inventories;
    private PlayerHide playerHideManager;
    public Map<Player, Long> times;
    
    public FantasyKits() {
        this.pm = this.getServer().getPluginManager();
        this.time = 7200;
        this.times = new HashMap<Player, Long>();
    }
    
    public static FantasyKits getInstace() {
        return FantasyKits.instance;
    }
    
    public void onLoad() {
        this.getServer().getConsoleSender().sendMessage("   ");
        this.getServer().getConsoleSender().sendMessage("FantasyKits - Carregando Arquivos...");
        this.getServer().getConsoleSender().sendMessage("FantasyKits - Criado Por nukopon   ");
        this.getServer().getConsoleSender().sendMessage("   ");
        this.getServer().getConsoleSender().sendMessage("   ");
        this.getServer().getConsoleSender().sendMessage("FantasyKits - Carregando Plugin...");
        this.getServer().getConsoleSender().sendMessage("FantasyKits - Criado Por nukopon   ");
        this.getServer().getConsoleSender().sendMessage("   ");
    }
    
    public void onEnable() {
        FantasyKits.plugin = (Plugin)this;
        (FantasyKits.main = this).InicarConexaoBungee();
        this.inventories = new ArrayList<EnchantingInventory>();
        this.pm.registerEvents((Listener)this, (Plugin)this);
        this.playerHideManager = new PlayerHide(this);
        this.registrarComandos();
        this.registrarEventos();
        this.registrarHabilidades();
        Manager.atualizar();
        FeastAPI.Feast();
        for (final World w : this.getServer().getWorlds()) {
            w.setPVP(true);
        }
        ConfigAPI.pegarConfig().carregarMensagens();
        ConfigAPI.pegarConfig().carregarCords();
        final boolean sql = ConfigAPI.pegarConfig().pegarconfigMensagens().getBoolean("Mysql.use");
        if (!sql) {
            this.storage = new SQLite(new File("plugins/ma_FNPVP/database.db"));
        }
        else {
            this.storage = new MySQL("jdbc:mysql://", ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Mysql.Host"), ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Mysql.DB"), ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Mysql.User"), ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Mysql.Senha"));
        }
        Bukkit.getScheduler().runTaskTimer((Plugin)this, (Runnable)new Runnable() {
            @Override
            public void run() {
                final ArrayList<Player> p = new ArrayList<Player>();
                for (final Map.Entry<Player, Long> ks : FantasyKits.this.times.entrySet()) {
                    if (!ks.getKey().isOnline()) {
                        p.add(ks.getKey());
                    }
                    else {
                        final double time = (System.currentTimeMillis() - ks.getValue()) / 60000.0;
                        if (time < 0.15) {
                            continue;
                        }
                        FantasyKits.this.getServer().dispatchCommand((CommandSender)FantasyKits.this.getServer().getConsoleSender(), "kick " + ks.getKey().getName() + " Kickado por esta AFK");
                    }
                }
                for (final Player p2 : p) {
                    FantasyKits.this.times.remove(p2);
                }
            }
        }, 0L, 20L);
        for (final Entity e : Bukkit.getWorlds().get(0).getEntities()) {
            if (e instanceof ArmorStand) {
                e.remove();
            }
        }
        this.pm.registerEvents((Listener)this, (Plugin)this);
        for (final Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer(String.valueOf(Manager.prefix) + "\n   \n       §7O Servidor Foi Atualizado(Reload).\n   §7Voce Foi Kickado Para Evitar Bugs.");
        }
        this.getServer().getConsoleSender().sendMessage("   ");
        this.getServer().getConsoleSender().sendMessage("FantasyKits - Iniciado");
        this.getServer().getConsoleSender().sendMessage("FantasyKits - Criado Por zAlpha_   ");
        this.getServer().getConsoleSender().sendMessage("   ");
    }
    
    public void onDisable() {
        final CommandSender sender = (CommandSender)Bukkit.getConsoleSender();
        final boolean save = PlayerData.getDatas().size() > 0;
        if (save) {
            sender.sendMessage("FANTASY KITPVP - SAVES ");
        }
        final List<String> saveds = new ArrayList<String>();
        saveds.add("§eJogadores salvados:");
        saveds.add(" ");
        for (final PlayerData pd : PlayerData.getDatas()) {
            pd.save();
            saveds.add("§a" + pd.getPlayer().getName());
        }
        if (save) {
            saveds.add(" ");
            for (final String string : saveds) {
                sender.sendMessage(string);
            }
        }
        TopKills.destroyStands();
        this.getServer().getConsoleSender().sendMessage("   ");
        this.getServer().getConsoleSender().sendMessage("FantasyKits - Desativado");
        this.getServer().getConsoleSender().sendMessage("FantasyKits - Criado Por nukopon   ");
        this.getServer().getConsoleSender().sendMessage("   ");
        this.getServer().getScheduler().cancelAllTasks();
    }
    
    private void registrarComandos() {
        this.getServer().getConsoleSender().sendMessage("   ");
        this.getServer().getConsoleSender().sendMessage("FantasyKits - Registrando Comandos...");
        this.getServer().getConsoleSender().sendMessage("FantasyKits - Criado Por nukopon   ");
        this.getServer().getConsoleSender().sendMessage("   ");
        try {
            this.getCommand("kit").setExecutor((CommandExecutor)new ComandKit());
            this.getCommand("tp").setExecutor((CommandExecutor)new ComandosTP());
            this.getCommand("construir").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("setwarp").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("spawn").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("tag").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("toggle").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("tag").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("inv").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("tell").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("gm").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("gamemode").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("tell").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("fly").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("chaves").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("caixas").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("staffchat").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("sc").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("dinheiro").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("report").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("admin").setExecutor((CommandExecutor)new Admin());
            this.getCommand("youtuber").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("bc").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("chat").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("status").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("1v1").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("skit").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("tpall").setExecutor((CommandExecutor)new ComandosTP());
            this.getCommand("limpar").setExecutor((CommandExecutor)new ComandosUteis());
            this.getCommand("score").setExecutor((CommandExecutor)new ComandosUteis());
        }
        catch (Exception e) {
            this.getServer().getConsoleSender().sendMessage("   ");
            this.getServer().getConsoleSender().sendMessage("FantasyKits - Desculpe Ouve Um Erro Ao Registrar Um Ou Mais Comandos");
            this.getServer().getConsoleSender().sendMessage("FantasyKits - Criado Por nukopon   ");
            this.getServer().getConsoleSender().sendMessage("   ");
        }
    }
    
    public void InicarConexaoBungee() {
        this.getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
    }
    
    private void registrarHabilidades() {
        this.getServer().getConsoleSender().sendMessage("   ");
        this.getServer().getConsoleSender().sendMessage("FantasyKits - Registrando Habilidades...");
        this.getServer().getConsoleSender().sendMessage("FantasyKits - Criado Por nukopon   ");
        this.getServer().getConsoleSender().sendMessage("   ");
        this.pm.registerEvents((Listener)new Habilidades(), (Plugin)this);
    }
    
    private void registrarEventos() {
        this.getServer().getConsoleSender().sendMessage("   ");
        this.getServer().getConsoleSender().sendMessage("FantasyKits - Registrando Eventos...");
        this.getServer().getConsoleSender().sendMessage("FantasyKits - Criado Por nukopon   ");
        this.getServer().getConsoleSender().sendMessage("   ");
        this.pm.registerEvents((Listener)new Eventos(), (Plugin)this);
        this.pm.registerEvents((Listener)new Admin(), (Plugin)this);
        this.pm.registerEvents((Listener)new Duelx1(this), (Plugin)this);
    }
    
    public static Plugin getPlugin() {
        return FantasyKits.plugin;
    }
    
    public static FantasyKits getMain() {
        return FantasyKits.main;
    }
    
    public Sql getStorage() {
        return this.storage;
    }
    
    public PlayerHide getPlayerHideManager() {
        return this.playerHideManager;
    }
}
