package com.zalpha.FantasyNetwork.PvP.util;

import java.util.TreeMap;
import java.util.NavigableMap;
import org.bukkit.inventory.PlayerInventory;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.bukkit.potion.PotionEffect;
import java.util.Random;
import org.bukkit.potion.PotionEffectType;
import java.util.concurrent.TimeUnit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import java.text.DecimalFormat;
import org.bukkit.inventory.meta.SkullMeta;
import me.zalpha.FantasyNetwork.PvP.Duelx1.Duelx1;
import org.bukkit.Sound;
import java.lang.reflect.Field;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.Color;
import org.bukkit.enchantments.Enchantment;
import java.util.Iterator;
import org.bukkit.scheduler.BukkitRunnable;
import java.text.NumberFormat;
import java.util.Locale;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.Inventory;
import java.sql.Date;
import java.text.DateFormat;
import org.bukkit.ChatColor;
import me.zalpha.FantasyNetwork.PvP.Util.JSONChatHoverEventType;
import me.zalpha.FantasyNetwork.PvP.Util.JSONChatExtra;
import me.zalpha.FantasyNetwork.PvP.Util.JSONChatFormat;
import java.util.List;
import me.zalpha.FantasyNetwork.PvP.Util.JSONChatMessage;
import me.zalpha.FantasyNetwork.PvP.Util.JSONChatColor;
import com.zalpha.FantasyNetwork.PvP.util.player.PlayerData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.apache.commons.lang.StringUtils;
import java.sql.ResultSet;
import java.sql.Statement;
import org.bukkit.Bukkit;
import java.sql.SQLException;
import com.zalpha.FantasyNetwork.PvP.FantasyKits;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.UUID;
import java.util.ArrayList;
import org.bukkit.entity.Player;
import java.util.Map;
import java.util.HashMap;

public class Manager
{
    public static String prefix;
    public static String use;
    public static String CONSOLE;
    public static String SEMPERM;
    public static String JOGADORFF;
    public static String UMKITPORVIDA;
    public static String PEGOUKIT;
    public static HashMap<String, String> kit;
    public static HashMap<String, String> Rank;
    public static HashMap<String, Tags> Tag;
    public static Map<Player, ScoreBoard> Scoreboard;
    public static ArrayList<Player> construir;
    public static ArrayList<String> StaffChat;
    public static HashMap<String, Long> Cooldown;
    public static boolean Chat;
    public static ArrayList<Player> Admin;
    public static ArrayList<Player> teleportnaomover;
    public static ArrayList<Player> CooldownReport;
    public static HashMap<String, Boolean> Report;
    public static HashMap<String, Boolean> Tell;
    public static HashMap<Integer, String> Top_Kills;
    public static HashMap<Integer, String> Top_Wins;
    public static HashMap<UUID, Integer> Tops;
    public static ArrayList<Player> Combate;
    private static char[] chars;
    public static String nmsver;
    
    static {
        Manager.prefix = "§5§lFANTASY §e\u279c";
        Manager.use = String.valueOf(Manager.prefix) + " §cUse: /";
        Manager.CONSOLE = String.valueOf(Manager.prefix) + " §cDesculpe, mas voc\u00ea n\u00e3o \u00e9 um jogador.";
        Manager.SEMPERM = String.valueOf(Manager.prefix) + " §cDesculpe, mas voc\u00ea n\u00e3o tem permiss\u00e3o para usar isto.";
        Manager.JOGADORFF = String.valueOf(Manager.prefix) + " §cDesculpe, mas este jogador esta offline.";
        Manager.UMKITPORVIDA = String.valueOf(Manager.prefix) + " §cVoc\u00ea esta com um kit ativado,morra ou de /spawn para desativa-lo.";
        Manager.PEGOUKIT = String.valueOf(Manager.prefix) + " §aVoc\u00ea pegou o kit: §f";
        Manager.kit = new HashMap<String, String>();
        Manager.Rank = new HashMap<String, String>();
        Manager.Tag = new HashMap<String, Tags>();
        Manager.Scoreboard = (Map<Player, ScoreBoard>)Maps.newHashMap();
        Manager.construir = (ArrayList<Player>)Lists.newArrayList();
        Manager.StaffChat = (ArrayList<String>)Lists.newArrayList();
        Manager.Cooldown = new HashMap<String, Long>();
        Manager.Chat = true;
        Manager.Admin = new ArrayList<Player>();
        Manager.teleportnaomover = new ArrayList<Player>();
        Manager.CooldownReport = new ArrayList<Player>();
        Manager.Report = new HashMap<String, Boolean>();
        Manager.Tell = new HashMap<String, Boolean>();
        Manager.Top_Kills = new HashMap<Integer, String>();
        Manager.Top_Wins = new HashMap<Integer, String>();
        Manager.Tops = new HashMap<UUID, Integer>();
        Manager.Combate = new ArrayList<Player>();
        Manager.chars = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
    }
    
    public static void createTop() {
        try {
            int counter = 0;
            final Statement st = FantasyKits.getMain().storage.getConnection().createStatement();
            final ResultSet r = st.executeQuery("SELECT UUID FROM kitpvp ORDER BY SUBSTRING_INDEX(stats, ' : ', 1) DESC LIMIT 5");
            while (r.next()) {
                ++counter;
                Manager.Top_Kills.put(counter, r.getString("name"));
            }
        }
        catch (SQLException ex) {}
        for (int x = 0; x < 5; ++x) {
            final int id = x + 1;
            if (Manager.Top_Kills.get(id) != null) {
                Bukkit.broadcastMessage("§a\u279c " + id + "º " + Manager.Top_Kills.get(id));
            }
        }
    }
    
    public static String Time2(final Integer i) {
        final int a = i / 60;
        final int b = i % 60;
        String c = null;
        String d = null;
        if (a > 1000) {
            c = new StringBuilder(String.valueOf(a)).toString();
        }
        else if (a == 1) {
            c = String.valueOf(a) + " minuto";
        }
        else {
            c = String.valueOf(a) + " minutos";
        }
        if (a == 0) {
            c = "";
        }
        if (a == 0) {
            c = "";
        }
        if (b > 60) {
            d = "";
        }
        else if (b == 1) {
            d = String.valueOf(b) + " segundo";
        }
        else {
            d = String.valueOf(b) + " segundos";
        }
        if (b == 0) {
            d = "";
        }
        return String.valueOf(c) + d;
    }
    
    public static void sendAccept(final Player t, final String clan) {
    }
    
    public static boolean comStaffAtivado(final Player jogador) {
        return Manager.StaffChat.contains(jogador.getName());
    }
    
    public static int pegarNumeroDeKits(final Player jogador) {
        int numerodekits = 0;
        for (int x = 0; x < Kits.values().length; ++x) {
            if (jogador.hasPermission("kit." + StringUtils.capitalise(Kits.values()[x].toString()))) {
                ++numerodekits;
            }
            else if (jogador.hasPermission("kit.*")) {
                numerodekits = Kits.values().length;
            }
        }
        return numerodekits;
    }
    
    public static void darItens(final Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents((ItemStack[])null);
        p.getInventory().setItem(1, criarItem2(Material.EMERALD, "§a\u279c §7Loja", (short)0, 1, new String[] { "§7", "§7Aqui est\u00e3o as lojas tanto in-game ou on-line." }));
        p.getInventory().setItem(2, criarItem2(Material.CHEST, "§e\u279c §7Kits", (short)0, 1, new String[] { "§7", "§7Aqui todos os kits que voc\u00ea possui.", "", "§e§lINFORMA\u00c7\u00d5ES:", "", "§7Voce possui: §a" + pegarNumeroDeKits(p) + "§7/§c" + Kits.values().length }));
        p.getInventory().setItem(4, criarItemSkull("§b\u279c §7Seu perfil", new String[] { "§7Aqui esta seu perfil do kitpvp" }, p.getName(), 1));
        p.getInventory().setItem(7, criarItem2(Material.BOOK, "§b\u279c §7Warps", (short)0, 1, new String[] { "§7", "§7Aqui est\u00e3o todas as warps do servidor", "§7que SETADAS." }));
    }
    
    public static String pegarRank(final Player p) {
        if (p.hasPermission("tag.dono")) {
            return "§4Dono";
        }
        if (p.hasPermission("tag.dev")) {
            return "§3Dev";
        }
        if (p.hasPermission("tag.coord")) {
            return "§4Coordenador";
        }
        if (p.hasPermission("tag.admin")) {
            return "§cAdmin";
        }
        if (p.hasPermission("tag.mod+")) {
            return "§5Mod+";
        }
        if (p.hasPermission("tag.mod")) {
            return "§5Mod";
        }
        if (p.hasPermission("tag.ajudante")) {
            return "§2Ajudante";
        }
        if (p.hasPermission("tag.youtuber+")) {
            return "§3Youtuber+";
        }
        if (p.hasPermission("tag.construtor")) {
            return "§eConstrutor";
        }
        if (p.hasPermission("tag.youtuber")) {
            return "§bYoutuber";
        }
        if (p.hasPermission("tag.gold")) {
            return "§eGold";
        }
        if (p.hasPermission("tag.gold+")) {
            return "§eGold+";
        }
        if (p.hasPermission("tag.diamond")) {
            return "§bDiamond";
        }
        if (p.hasPermission("tag.diamond+")) {
            return "§bDiamond+";
        }
        if (p.hasPermission("tag.emerald")) {
            return "§aEmerald";
        }
        if (p.hasPermission("tag.emerald+")) {
            return "§aEmerald+";
        }
        if (p.hasPermission("tag.global")) {
            return "§6Global";
        }
        if (p.hasPermission("tag.global+")) {
            return "§6Global+";
        }
        if (p.hasPermission("tag.youtuber.mirim")) {
            return "§BMini YT";
        }
        if (p.hasPermission("tag.normal")) {
            return "§7Normal";
        }
        return "§cNenhum";
    }
    
    public static void sendMessageChat(final Player jogador, final Player senders, final String messagechat) {
        final PlayerData pd = PlayerData.get(jogador);
        if (pd == null) {
            jogador.sendMessage(String.valueOf(Manager.prefix) + " §CSua conta n\u00e3o esta registrada, relogue para resolver.");
            return;
        }
        double kill = pd.getKills();
        double death = pd.getDeaths();
        if (kill == 0.0) {
            kill = 1.0;
        }
        if (death == 0.0) {
            death = 1.0;
        }
        final double total = kill / death;
        final double value = (kill + death) / 2.0;
        String kd;
        if (pd.getKills() == 0 && pd.getDeaths() == 0) {
            kd = "§cNenhum";
        }
        else {
            kd = String.valueOf(getProgressExact(total, value)) + "%";
        }
        final JSONChatMessage message = new JSONChatMessage("§7\u276a ", JSONChatColor.GRAY, null);
        final JSONChatExtra rank = new JSONChatExtra(pd.getRankSymbol());
        rank.setHoverEvent(JSONChatHoverEventType.SHOW_TEXT, String.valueOf(pd.getRankSymbol()) + " " + ChatColor.stripColor(pd.getRank()));
        final JSONChatExtra nome = new JSONChatExtra(jogador.getDisplayName());
        nome.setHoverEvent(JSONChatHoverEventType.SHOW_TEXT, "§eInforma\u00e7\u00f5es do §f" + jogador.getName() + "\n§7\u279c Rank: " + pegarRank(jogador) + "\n§7\u279c K.D: " + kd);
        message.addExtra(rank);
        message.addExtra(new JSONChatExtra(" §7| "));
        message.addExtra(nome);
        message.addExtra(new JSONChatExtra(" §7\u276b " + messagechat));
        message.sendToPlayer(senders);
    }
    
    public static String getTime(final long ms) {
        return DateFormat.getInstance().format(new Date(ms));
    }
    
    public static List<String> lore(String string) {
        final String[] split = string.split("");
        string = "";
        final ChatColor color = ChatColor.GRAY;
        final ArrayList<String> newString = new ArrayList<String>();
        for (int i = 0; i < split.length; ++i) {
            if (string.length() > 30 || string.endsWith(".")) {
                newString.add(color + string);
                if (string.endsWith(".")) {
                    newString.add("");
                }
                string = "";
            }
            string = String.valueOf(string) + ((string.length() == 0) ? "" : "") + split[i];
        }
        newString.add(color + string);
        return newString;
    }
    
    public static void adicionarItemPermissao(final Material material, final String nome, final String permissao, final String desc, final short durabilidade, final Inventory inventario, final Player jogador) {
        final ItemStack Material1 = new ItemStack(material);
        final ItemMeta MaterialMeta = Material1.getItemMeta();
        MaterialMeta.setDisplayName(nome);
        MaterialMeta.setLore((List)lore(desc));
        Material1.setDurability(durabilidade);
        Material1.setItemMeta(MaterialMeta);
        if (jogador.hasPermission(permissao)) {
            inventario.addItem(new ItemStack[] { Material1 });
        }
    }
    
    public static String format(final double paramDouble) {
        final NumberFormat localNumberFormat = NumberFormat.getInstance(Locale.ENGLISH);
        localNumberFormat.setMaximumFractionDigits(2);
        localNumberFormat.setMinimumFractionDigits(0);
        return localNumberFormat.format(paramDouble);
    }
    
    public static String getProgressExact(final double paramDouble1, final double paramDouble2) {
        final double d = 100.0 * paramDouble1 / paramDouble2;
        if (d >= 100.0) {
            return "100";
        }
        final String str = format(d);
        return str.replace(".", ",");
    }
    
    public static String getKDR(final Player p) {
        final PlayerData pd = PlayerData.get(p);
        if (pd == null) {
            return "Nenhum";
        }
        double kill = pd.getKills();
        double death = pd.getDeaths();
        if (kill == 0.0) {
            kill = 1.0;
        }
        if (death == 0.0) {
            death = 1.0;
        }
        final double total = kill / death;
        final double value = (kill + death) / 2.0;
        return getProgressExact(total, value);
    }
    
    public static void atualizar() {
        new BukkitRunnable() {
            public void run() {
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    final PlayerData pd = PlayerData.get(p);
                    if (pd.getScoreB()) {
                        ScoreBoard.unrankedSidebarDisplay(p, new String[] { "§5§lFANTASY §e§lKITPVP", "§1", "§fGrupo: " + Manager.pegarRank(p), "§fLiga: " + pd.getRank(), "§fDinheiro: §a" + Manager.modificarCoins(pd.getMoney()) + "$", "§4", "§fKills: §a" + pd.getKills(), "§fMortes: §c" + pd.getDeaths(), "§0", "§fKS: §9" + pd.getKillStreak(), "§fK.D: §e" + Manager.getKDR(p) + "%", "§fPontua\u00e7\u00e3o: §5" + Manager.modificarCoins(pd.getScore()), "§2", "§ewww." + ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Site") });
                    }
                }
            }
        }.runTaskTimer(FantasyKits.getPlugin(), 6L, 60L);
    }
    
    public static void adicionarSemItemPermissao(final Material material, final String nome, final String permissao, final String[] descricao, final short durabilidade, final Inventory inventario, final Player jogador) {
        final ItemStack Material1 = new ItemStack(material);
        final ItemMeta MaterialMeta = Material1.getItemMeta();
        MaterialMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        for (final String descri : descricao) {
            Descricao.add(descri);
        }
        MaterialMeta.setLore((List)Descricao);
        Material1.setDurability(durabilidade);
        Material1.setItemMeta(MaterialMeta);
        if (!jogador.hasPermission(permissao)) {
            inventario.addItem(new ItemStack[] { Material1 });
        }
    }
    
    public static void setarItemEchantInv(final Material material, final String nome, final String desc, final short durabilidade, final int slot, final Enchantment encantamento, final int level, final Enchantment encantamento2, final int level2, final Player jogador) {
        final ItemStack Material = new ItemStack(material);
        final ItemMeta MaterialMeta = Material.getItemMeta();
        MaterialMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        Descricao.add(desc);
        MaterialMeta.setLore((List)Descricao);
        MaterialMeta.addEnchant(encantamento, level, true);
        MaterialMeta.addEnchant(encantamento2, level2, true);
        Material.setDurability(durabilidade);
        Material.setItemMeta(MaterialMeta);
        jogador.getInventory().setItem(slot, Material);
    }
    
    public static void adicionarItemInv(final Material material, final String nome, final String desc, final short durabilidade, final Player jogador) {
        final ItemStack Material = new ItemStack(material);
        final ItemMeta MaterialMeta = Material.getItemMeta();
        MaterialMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        Descricao.add(desc);
        MaterialMeta.setLore((List)Descricao);
        Material.setDurability(durabilidade);
        Material.setItemMeta(MaterialMeta);
        jogador.getInventory().addItem(new ItemStack[] { Material });
    }
    
    public static void criarItemGUI(final Material material, final String nome, final String desc, final short durabilidade, final int slot, final int quantidade, final Inventory inventario) {
        final ItemStack Material = new ItemStack(material, quantidade);
        final ItemMeta MaterialMeta = Material.getItemMeta();
        MaterialMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        Descricao.add(desc);
        MaterialMeta.setLore((List)Descricao);
        Material.setDurability(durabilidade);
        Material.setItemMeta(MaterialMeta);
        inventario.setItem(slot, Material);
    }
    
    public static void adicionarItemGUI(final Material material, final String nome, final String desc, final String desc2, final String desc3, final short durabilidade, final Inventory inventario) {
        final ItemStack Material = new ItemStack(material);
        final ItemMeta MaterialMeta = Material.getItemMeta();
        MaterialMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        Descricao.add(desc);
        Descricao.add(desc2);
        Descricao.add(desc3);
        MaterialMeta.setLore((List)Descricao);
        Material.setDurability(durabilidade);
        Material.setItemMeta(MaterialMeta);
        inventario.addItem(new ItemStack[] { Material });
    }
    
    public static void adicionarItemEnchantGUI(final Material material, final String nome, final String desc, final String desc2, final String desc3, final short durabilidade, final Enchantment encantamento, final int level, final Enchantment encantamento2, final int level2, final Inventory inventario) {
        final ItemStack Material = new ItemStack(material);
        final ItemMeta MaterialMeta = Material.getItemMeta();
        MaterialMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        Descricao.add(desc);
        MaterialMeta.setLore((List)Descricao);
        MaterialMeta.addEnchant(encantamento, level, true);
        MaterialMeta.addEnchant(encantamento2, level2, true);
        Material.setDurability(durabilidade);
        Material.setItemMeta(MaterialMeta);
        inventario.addItem(new ItemStack[] { Material });
    }
    
    public static ItemStack setarCor(final Material Material, final Color cor, final String nome) {
        final ItemStack Armadura = new ItemStack(Material);
        final LeatherArmorMeta ArmaduraMeta = (LeatherArmorMeta)Armadura.getItemMeta();
        ArmaduraMeta.setColor(cor);
        ArmaduraMeta.setDisplayName(nome);
        Armadura.setItemMeta((ItemMeta)ArmaduraMeta);
        return Armadura;
    }
    
    public static ItemStack criarItem(final Material Material, final String nome, final short durabilidade, final String[] descricao) {
        final ItemStack item = new ItemStack(Material, 1, durabilidade);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        for (final String descri : descricao) {
            Descricao.add(descri);
        }
        itemMeta.setLore((List)Descricao);
        item.setItemMeta(itemMeta);
        return item;
    }
    
    public static ItemStack criarItemGlow(final Material Material, final String nome, final short durabilidade, final String[] descricao) {
        final ItemStack item = new ItemStack(Material, 1, durabilidade);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        for (final String descri : descricao) {
            Descricao.add(descri);
        }
        itemMeta.setLore((List)Descricao);
        item.setItemMeta(itemMeta);
        return (ItemStack)addGlow(item);
    }
    
    public static ItemStack criarItemEnchantamento(final Material Material, final String nome, final Enchantment enchant, final int lv, final short durabilidade, final String[] descricao) {
        final ItemStack item = new ItemStack(Material, 1, durabilidade);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(nome);
        itemMeta.addEnchant(enchant, lv, true);
        final ArrayList<String> Descricao = new ArrayList<String>();
        for (final String descri : descricao) {
            Descricao.add(descri);
        }
        itemMeta.setLore((List)Descricao);
        item.setItemMeta(itemMeta);
        return item;
    }
    
    public static ItemStack criarItem2(final Material Material, final String nome, final short durabilidade, final int quantidade, final String[] descricao) {
        final ItemStack item = new ItemStack(Material, quantidade, durabilidade);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        for (final String descri : descricao) {
            Descricao.add(descri);
        }
        itemMeta.setLore((List)Descricao);
        item.setItemMeta(itemMeta);
        return item;
    }
    
    public static void sendTabColor(final Player player, final String cima, final String baixo) {
        final CraftPlayer craftplayer = (CraftPlayer)player;
        final PlayerConnection connection = craftplayer.getHandle().playerConnection;
        final IChatBaseComponent headerJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + cima + "\"}");
        final IChatBaseComponent footerJSON = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + baixo + "\"}");
        final PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            final Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, headerJSON);
            headerField.setAccessible(!headerField.isAccessible());
            final Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, footerJSON);
            footerField.setAccessible(!footerField.isAccessible());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        connection.sendPacket((Packet)packet);
    }
    
    public static void mandarMensagemComPermissao(final String mensagem, final String permissao, final boolean essecao) {
        for (final Player jogadores : Bukkit.getOnlinePlayers()) {
            if (essecao) {
                if (!comReportAtivado(jogadores) || !jogadores.hasPermission("fantasy." + permissao)) {
                    continue;
                }
                jogadores.sendMessage(mensagem);
                jogadores.playSound(jogadores.getLocation(), Sound.EXPLODE, 2.0f, 2.0f);
            }
            else {
                if (!jogadores.hasPermission("fantasy." + permissao)) {
                    continue;
                }
                jogadores.sendMessage(mensagem);
            }
        }
    }
    
    public static void mandarTituloBaixoComPermissao(final String mensagem, final String permissao) {
        for (final Player jogadores : Bukkit.getOnlinePlayers()) {
            if (jogadores.hasPermission("fantasy." + permissao)) {
                mandarTituloBaixo(jogadores, mensagem);
            }
        }
    }
    
    public static void mandarTituloCimaComPermissao(final String mensagem, final String permissao) {
        for (final Player jogadores : Bukkit.getOnlinePlayers()) {
            if (jogadores.hasPermission("fantasy." + permissao)) {
                mandarTituloCima(jogadores, mensagem);
            }
        }
    }
    
    public static void setarItemInv(final Material material, final String nome, final String desc, final short durabilidade, final int slot, final int quantidade, final Player jogador) {
        final ItemStack Material = new ItemStack(material, quantidade);
        final ItemMeta MaterialMeta = Material.getItemMeta();
        MaterialMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        Descricao.add(desc);
        MaterialMeta.setLore((List)Descricao);
        Material.setDurability(durabilidade);
        Material.setItemMeta(MaterialMeta);
        jogador.getInventory().setItem(slot, Material);
    }
    
    public static void removerArrays(final Player p) {
        if (Manager.construir.contains(p)) {
            Manager.construir.remove(p);
        }
        if (Manager.Combate.contains(p)) {
            p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea §csaiu §7de combate.");
            Manager.Combate.remove(p);
        }
        if (Duelx1.playersInQueue.contains(p)) {
            Duelx1.playersInQueue.remove(p);
        }
        if (comKit(p)) {
            Manager.kit.remove(p.getName());
        }
    }
    
    public static int numeroaleatorio(final int min, final int max) {
        final int range = max - min;
        return min + (int)(Math.random() * range);
    }
    
    public static void adicionarItemInv(final Material material, final String nome, final String[] desc, final short durabilidade, final Player jogador) {
        final ItemStack Material = new ItemStack(material);
        final ItemMeta MaterialMeta = Material.getItemMeta();
        MaterialMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        for (final String descri : desc) {
            Descricao.add(descri);
        }
        MaterialMeta.setLore((List)Descricao);
        Material.setDurability(durabilidade);
        Material.setItemMeta(MaterialMeta);
        jogador.getInventory().addItem(new ItemStack[] { Material });
    }
    
    public static void adicionarItemEnchantGUI(final Material material, final String nome, final String desc, final String desc2, final short durabilidade, final Enchantment encantamento, final int level, final Enchantment encantamento2, final int level2, final Inventory inventario) {
        final ItemStack Material = new ItemStack(material);
        final ItemMeta MaterialMeta = Material.getItemMeta();
        MaterialMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        Descricao.add(desc);
        Descricao.add(desc2);
        MaterialMeta.setLore((List)Descricao);
        MaterialMeta.addEnchant(encantamento, level, true);
        MaterialMeta.addEnchant(encantamento2, level2, true);
        Material.setDurability(durabilidade);
        Material.setItemMeta(MaterialMeta);
        inventario.addItem(new ItemStack[] { Material });
    }
    
    public static ItemStack setarCor2(final Material Material, final Color cor, final String nome, final String[] desc) {
        final ItemStack Armadura = new ItemStack(Material);
        final LeatherArmorMeta ArmaduraMeta = (LeatherArmorMeta)Armadura.getItemMeta();
        ArmaduraMeta.setColor(cor);
        ArmaduraMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        for (final String descri : desc) {
            Descricao.add(descri);
        }
        ArmaduraMeta.setLore((List)Descricao);
        Armadura.setItemMeta((ItemMeta)ArmaduraMeta);
        return Armadura;
    }
    
    public static ItemStack setarCorGlow(final Material Material, final Color cor, final String nome, final String[] desc) {
        final ItemStack Armadura = new ItemStack(Material);
        final LeatherArmorMeta ArmaduraMeta = (LeatherArmorMeta)Armadura.getItemMeta();
        ArmaduraMeta.setColor(cor);
        ArmaduraMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        for (final String descri : desc) {
            Descricao.add(descri);
        }
        ArmaduraMeta.setLore((List)Descricao);
        Armadura.setItemMeta((ItemMeta)ArmaduraMeta);
        return (ItemStack)addGlow(Armadura);
    }
    
    public static ItemStack criarItemSkull(final String nome, final String[] descricao, final String owner, final int size) {
        final ItemStack item = new ItemStack(Material.SKULL_ITEM);
        final SkullMeta itemMeta = (SkullMeta)item.getItemMeta();
        item.setDurability((short)3);
        itemMeta.setDisplayName(nome);
        itemMeta.setOwner(owner);
        item.setAmount(size);
        final ArrayList<String> Descricao = new ArrayList<String>();
        for (final String descri : descricao) {
            Descricao.add(descri);
        }
        itemMeta.setLore((List)Descricao);
        item.setItemMeta((ItemMeta)itemMeta);
        return item;
    }
    
    public static ItemStack criarItem3(final Material Material, final String nome, final short durabilidade, final int quantidade, final Enchantment encantamento, final int level, final Enchantment encantamento2, final int level2, final String[] descricao) {
        final ItemStack item = new ItemStack(Material, quantidade, durabilidade);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(nome);
        final ArrayList<String> Descricao = new ArrayList<String>();
        for (final String descri : descricao) {
            Descricao.add(descri);
        }
        itemMeta.addEnchant(encantamento, level, true);
        itemMeta.addEnchant(encantamento2, level2, true);
        itemMeta.setLore((List)Descricao);
        item.setItemMeta(itemMeta);
        return item;
    }
    
    public static String modificarCoins(final Integer i) {
        String s = null;
        final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,###,###,###,###,###,###,###,###,###,###,###,###");
        s = df.format(i).replace(".", ",");
        return s;
    }
    
    public static void mandarTituloCima(final Player jogador, final String mensagem) {
        final PacketPlayOutTitle packet1 = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}").a(mensagem), 10, 100, 100);
        ((CraftPlayer)jogador).getHandle().playerConnection.sendPacket((Packet)packet1);
    }
    
    public static void mandarTituloBaixo(final Player jogador, final String mensagem) {
        final PacketPlayOutTitle packet1 = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{\"text\": \"\"}").a(mensagem), 10, 100, 100);
        ((CraftPlayer)jogador).getHandle().playerConnection.sendPacket((Packet)packet1);
    }
    
    public static CraftItemStack addGlow(final ItemStack item) {
        final net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) {
            tag = nmsStack.getTag();
        }
        final NBTTagList ench = new NBTTagList();
        tag.set("ench", (NBTBase)ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }
    
    public static String getTeamName(final TagsTeams tag, final PlayerData.Ranks liga) {
        return String.valueOf(Manager.chars[tag.ordinal()]) + "-" + Manager.chars[PlayerData.Ranks.values().length - liga.ordinal()];
    }
    
    public static void iniciarTags() {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            final PlayerData pd = PlayerData.get(p);
            if (pd == null) {
                return;
            }
            String finaltag = pegarTagDisplay(p);
            if (finaltag.length() <= 16) {
                continue;
            }
            finaltag = finaltag.substring(0, 16);
        }
    }
    
    public static Tags pegarTag(final Player jogador) {
        if (!Manager.Tag.containsKey(jogador.getName())) {
            return Tags.NORMAL;
        }
        return Manager.Tag.get(jogador.getName());
    }
    
    public static String pegarTagDisplay(final Player jogador) {
        return pegarTag(jogador).getMsg();
    }
    
    public static void setarTag(final Player jogador, final Tags tag) {
        Manager.Tag.put(jogador.getName(), tag);
    }
    
    public static void setarKit(final Player jogador, final String Kit) {
        Manager.kit.put(jogador.getName(), Kit);
    }
    
    public static boolean argumentoNumero(final String argumento) {
        try {
            Integer.valueOf(argumento);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public static void mandarMensagem(final String mensagem) {
        for (final Player jogadores : Bukkit.getOnlinePlayers()) {
            jogadores.sendMessage(mensagem);
        }
    }
    
    public static void darKit(final Player p, final Kits kit) {
        p.getInventory().setArmorContents((ItemStack[])null);
        p.getInventory().clear();
        p.setAllowFlight(false);
        setarKit(p, kit.getItem().getItemMeta().getDisplayName().replace("§7\u279c §aKit ", ""));
        for (final Player p2 : Bukkit.getOnlinePlayers()) {
            p2.showPlayer(p);
        }
        if (kit == Kits.KANGAROO) {
            setarItemInv(Material.FIREWORK, "§a\u25ba Kangaroo Firework", null, (short)0, 1, 1, p);
        }
        if (kit == Kits.FISHERMAN) {
            setarItemInv(Material.FISHING_ROD, "§a\u25ba Fisherman Rod", null, (short)0, 1, 1, p);
        }
        if (kit == Kits.ARCHER) {
            setarItemEchantInv(Material.BOW, "§a\u25ba Arco", null, (short)0, 1, Enchantment.ARROW_DAMAGE, 1, Enchantment.ARROW_INFINITE, 1, p);
            setarItemInv(Material.ARROW, "§a\u25ba Flecha", null, (short)0, 2, 1, p);
        }
        if (kit == Kits.THOR) {
            setarItemInv(Material.STONE_AXE, "§a\u25ba Machado Do Thor", null, (short)0, 1, 1, p);
        }
        if (kit == Kits.GLADIATOR) {
            setarItemInv(Material.IRON_FENCE, "§a\u25ba Jogo Sombrio", null, (short)0, 1, 1, p);
        }
        if (kit == Kits.SWITCHER) {
            setarItemInv(Material.SNOW_BALL, "§a\u25ba Bolas De Neve", null, (short)0, 1, 16, p);
        }
        if (kit == Kits.PHANTOM) {
            setarItemInv(Material.FEATHER, "§a\u25ba Pena Do Phantom", null, (short)0, 1, 1, p);
        }
        if (kit == Kits.SNIPER) {
            setarItemInv(Material.BLAZE_ROD, "§a\u25ba Sniper", null, (short)0, 1, 1, p);
        }
        if (kit == Kits.DESHFIRE) {
            setarItemInv(Material.REDSTONE_BLOCK, "§a\u25ba Deshfire", null, (short)0, 1, 1, p);
        }
        if (kit == Kits.SONIC) {
            setarItemInv(Material.LAPIS_BLOCK, "§a\u25ba Sonic", null, (short)0, 1, 1, p);
        }
        if (kit == Kits.FLASH) {
            setarItemInv(Material.REDSTONE_BLOCK, "§a\u25ba Flash", null, (short)0, 1, 1, p);
        }
        if (kit == Kits.DRAIG) {
            setarItemInv(Material.BLAZE_POWDER, "§a\u25ba Draig", null, (short)0, 1, 1, p);
        }
        if (kit == Kits.THRESH) {
            setarItemInv(Material.LEASH, "§a\u25ba Corda Thresh", null, (short)0, 1, 1, p);
        }
        if (kit == Kits.THRESH) {
            setarItemInv(Material.LEASH, "§a\u25ba Corda Thresh", null, (short)0, 1, 1, p);
        }
        removerCooldown(p);
        final int x = numeroaleatorio(ConfigAPI.pegarConfig().pegarconfigCords().getInt("Coordenadas.Arena.Min.x"), ConfigAPI.pegarConfig().pegarconfigCords().getInt("Coordenadas.Arena.Max.x"));
        final int z = numeroaleatorio(ConfigAPI.pegarConfig().pegarconfigCords().getInt("Coordenadas.Arena.Min.z"), ConfigAPI.pegarConfig().pegarconfigCords().getInt("Coordenadas.Arena.Max.z"));
        final int y = p.getWorld().getHighestBlockYAt(x, z);
        final Location Local = new Location(p.getWorld(), (double)x, (double)y, (double)z);
        p.getInventory().setHeldItemSlot(1);
        p.setGameMode(GameMode.SURVIVAL);
        p.getInventory().setArmorContents((ItemStack[])null);
        p.teleport(Local);
        adicionarItemInv(Material.STONE_SWORD, "§e" + pegarKit(p), "", (short)0, p);
        setarItemInv(Material.COMPASS, "§a\u25ba Bussola", " ", (short)0, 8, 1, p);
        adicionarFullSopa(p);
    }
    
    public static void adicionarCooldown(final Player jogador, final int Tempo) {
        Manager.Cooldown.put(jogador.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(Tempo));
    }
    
    public static void removerCooldown(final Player jogador) {
        Manager.Cooldown.remove(jogador.getName());
    }
    
    public static boolean naoTemCooldown(final Player jogador) {
        return !Manager.Cooldown.containsKey(jogador.getName()) || Manager.Cooldown.get(jogador.getName()) <= System.currentTimeMillis();
    }
    
    public static long pegarCooldown(final Player jogador) {
        final long tempo = TimeUnit.MILLISECONDS.toSeconds(Manager.Cooldown.get(jogador.getName()) - System.currentTimeMillis());
        if (Manager.Cooldown.containsKey(jogador.getName()) || Manager.Cooldown.get(jogador.getName()) > System.currentTimeMillis()) {
            return tempo;
        }
        return 0L;
    }
    
    public static void mensagemCooldown(final Player jogador) {
        if (pegarCooldown(jogador) == 1L) {
            jogador.sendMessage(String.valueOf(Manager.prefix) + " §7Seu kit §c" + pegarKit(jogador) + " §7esta em um cooldown de §f" + pegarCooldown(jogador) + " segundo§7.");
        }
        if (pegarCooldown(jogador) > 1L) {
            jogador.sendMessage(String.valueOf(Manager.prefix) + " §7Seu kit §c" + pegarKit(jogador) + " §7esta em um cooldown de §f" + pegarCooldown(jogador) + " segundos§7.");
        }
    }
    
    public static String pegarKit(final Player p) {
        if (Manager.kit.containsKey(p.getName())) {
            return Manager.kit.get(p.getName());
        }
        return "None";
    }
    
    public static void adicionarFullSopa(final Player p) {
        for (int sopas = 0; sopas < p.getInventory().getSize(); ++sopas) {
            setarItemInv(Material.BOWL, "§b\u25ba Tigela", "", (short)0, 13, 64, p);
            setarItemInv(Material.RED_MUSHROOM, "§c\u25ba Cogumelo", "", (short)0, 14, 64, p);
            setarItemInv(Material.BROWN_MUSHROOM, "§6\u25ba Cogumelo", "", (short)0, 15, 64, p);
            while (p.getInventory().getItem(sopas) == null) {
                setarItemInv(Material.MUSHROOM_SOUP, "§a\u25ba Sopa", "§7Tome Para Regenerar Sua Vida.", (short)0, sopas, 1, p);
            }
        }
    }
    
    public static boolean comKit(final Player p) {
        return Manager.kit.containsKey(p.getName());
    }
    
    public static void chanceefeito(final Player jogador, final int tempo, final int escala, final int chance, final PotionEffectType efeito, final boolean Magma) {
        final Random r = new Random();
        final int aleatorio = r.nextInt();
        if (aleatorio <= chance) {
            if (!Magma) {
                if (efeito == PotionEffectType.POISON) {
                    jogador.sendMessage(String.valueOf(Manager.prefix) + " §aUm Viper Te Envenenou.");
                }
                if (efeito == PotionEffectType.SLOW) {
                    jogador.sendMessage(String.valueOf(Manager.prefix) + " §aUm Snail Te Bateu E Voce Ficou Mais Lento.");
                }
                jogador.addPotionEffect(new PotionEffect(efeito, tempo, escala));
            }
            else {
                jogador.setFireTicks(100);
            }
        }
    }
    
    public static void adicionarEfeito(final Player jogador, final int tempo, final int escala, final PotionEffectType efeito) {
        jogador.addPotionEffect(new PotionEffect(efeito, tempo, escala));
    }
    
    public static void removerEfeito(final Player jogador, final PotionEffectType efeito) {
        jogador.removePotionEffect(efeito);
    }
    
    public static Location getTargetBlock(final Player paramPlayer, final int paramInt) {
        final Location localLocation = paramPlayer.getEyeLocation();
        final Vector localVector = localLocation.getDirection().normalize();
        Block localBlock = null;
        for (int i = 0; i <= paramInt; ++i) {
            localLocation.add(localVector);
            localBlock = localLocation.getBlock();
            if (localBlock.getType() != Material.AIR) {
                return localLocation;
            }
        }
        return localLocation;
    }
    
    public static boolean comTellAtivado(final Player jogador) {
        return Manager.Tell.get(jogador.getName()) && Manager.Tell.get(jogador.getName());
    }
    
    public static boolean comReportAtivado(final Player jogador) {
        return Manager.Report.get(jogador.getName()) && Manager.Report.get(jogador.getName());
    }
    
    public static boolean pegarCombat(final Player jogador) {
        return Manager.Combate.contains(jogador);
    }
    
    public static void mandamensagemKillStreak(final PlayerData pd) {
        if (pd.getKillStreak() % 5 == 0) {
            mandarMensagem(String.valueOf(Manager.prefix) + " §f" + pd.getPlayer().getName() + " §aconseguiu um killstreak de §f" + pd.getKillStreak() + "§a.");
        }
        if (pd.getKillStreak() % 100 == 0) {
            mandarMensagem(String.valueOf(Manager.prefix) + " §f" + pd.getPlayer().getName() + " §aconseguiu um killstreak de §f100§a.");
            for (final Player online : Bukkit.getOnlinePlayers()) {
                mandarTituloCima(online, "§7O jogador §a" + pd.getPlayer().getName());
                mandarTituloBaixo(online, "§eEst\u00e1 imbativel!!");
                mandarMensagem(String.valueOf(Manager.prefix) + " §f" + pd.getPlayer().getName() + " §aEst\u00e1 imbativel!");
            }
        }
    }
    
    public static String serializeLocation(final Location loc) {
        final String toString = String.valueOf(loc.getWorld().getName()) + " : " + loc.getX() + " : " + loc.getY() + " : " + loc.getZ() + " : " + loc.getPitch() + " : " + loc.getYaw();
        return toString;
    }
    
    public static Location unserializeLocation(final String path) {
        final String[] sp = path.split(" : ");
        final Location loc = new Location(Bukkit.getWorld(sp[0]), Double.parseDouble(sp[1]), Double.parseDouble(sp[2]), Double.parseDouble(sp[3]));
        loc.setPitch(Float.parseFloat(sp[4]));
        loc.setYaw(Float.parseFloat(sp[5]));
        return loc;
    }
    
    public static void sendActionBar(final Player player, final String message) {
        final CraftPlayer p = (CraftPlayer)player;
        final IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        final PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
        p.getHandle().playerConnection.sendPacket((Packet)ppoc);
    }
    
    public static void teleportarWarp(final Player jogador, final String warp) {
        if (ConfigAPI.pegarConfig().pegarconfigCords().getString("Coordenadas." + warp) == null) {
            jogador.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas A Warp §f" + warp + "§c N\u00e3o Foi Setada.");
            return;
        }
        if (warp.equalsIgnoreCase("fps")) {
            jogador.getInventory().clear();
            setarKit(jogador, "Fps");
            jogador.getInventory().setItem(0, criarItem2(Material.DIAMOND_SWORD, "§eFps", (short)0, 1, new String[0]));
            adicionarFullSopa(jogador);
            jogador.getInventory().setHelmet(criarItem(Material.IRON_HELMET, "§eFps", (short)0, new String[0]));
            jogador.getInventory().setChestplate(criarItem(Material.IRON_CHESTPLATE, "§eFps", (short)0, new String[0]));
            jogador.getInventory().setLeggings(criarItem(Material.IRON_LEGGINGS, "§eFps", (short)0, new String[0]));
            jogador.getInventory().setBoots(criarItem(Material.IRON_BOOTS, "§eFps", (short)0, new String[0]));
        }
        else if (warp.equalsIgnoreCase("lava")) {
            jogador.getInventory().clear();
            jogador.getInventory().setItem(0, (ItemStack)null);
            setarKit(jogador, "Lava");
            adicionarFullSopa(jogador);
        }
        else if (warp.equalsIgnoreCase("Mdr")) {
            jogador.getInventory().clear();
            jogador.getInventory().setItem(0, criarItem2(Material.STONE_SWORD, "§eMdr", (short)0, 1, new String[0]));
            adicionarFullSopa(jogador);
            setarKit(jogador, "Mdr");
        }
        else if (warp.equalsIgnoreCase("main")) {
            jogador.getInventory().clear();
            jogador.getInventory().setItem(0, (ItemStack)null);
            setarKit(jogador, "Main");
            adicionarItemEnchantGUI(Material.DIAMOND_SWORD, "§eMain", null, null, null, (short)0, Enchantment.DAMAGE_ALL, 1, Enchantment.KNOCKBACK, 1, (Inventory)jogador.getInventory());
            adicionarEfeito(jogador, 1000000, 0, PotionEffectType.SPEED);
            adicionarEfeito(jogador, 1000000, 0, PotionEffectType.INCREASE_DAMAGE);
            adicionarFullSopa(jogador);
            jogador.getInventory().setHelmet(criarItem(Material.IRON_HELMET, "§eMain", (short)0, new String[0]));
            jogador.getInventory().setChestplate(criarItem(Material.IRON_CHESTPLATE, "§eMain", (short)0, new String[0]));
            jogador.getInventory().setLeggings(criarItem(Material.IRON_LEGGINGS, "§eMain", (short)0, new String[0]));
            jogador.getInventory().setBoots(criarItem(Material.IRON_BOOTS, "§eMain", (short)0, new String[0]));
        }
        else if (warp.equalsIgnoreCase("potion")) {
            jogador.getInventory().clear();
            jogador.getInventory().setItem(0, (ItemStack)null);
            setarKit(jogador, "Potion");
            adicionarItemEnchantGUI(Material.DIAMOND_SWORD, "§ePotion", null, null, null, (short)0, Enchantment.DAMAGE_ALL, 1, Enchantment.FIRE_ASPECT, 1, (Inventory)jogador.getInventory());
            jogador.getInventory().setItem(1, criarItem2(Material.GOLDEN_APPLE, "§6\u25ba Ma\u00e7a", (short)1, 10, new String[0]));
            jogador.getInventory().setItem(2, criarItem(Material.POTION, "§e\u25ba Po\u00e7\u00e3o", (short)16424, new String[0]));
            jogador.getInventory().setItem(3, criarItem(Material.POTION, "§e\u25ba Po\u00e7\u00e3o", (short)16419, new String[0]));
            jogador.getInventory().setItem(4, criarItem(Material.POTION, "§e\u25ba Po\u00e7\u00e3o", (short)8226, new String[0]));
            jogador.getInventory().setItem(5, criarItem(Material.POTION, "§e\u25ba Po\u00e7\u00e3o", (short)8226, new String[0]));
            jogador.getInventory().setItem(6, criarItem(Material.POTION, "§e\u25ba Po\u00e7\u00e3o", (short)8233, new String[0]));
            jogador.getInventory().setItem(7, criarItem(Material.POTION, "§e\u25ba Po\u00e7\u00e3o", (short)8233, new String[0]));
            jogador.getInventory().setItem(8, criarItem2(Material.ENDER_PEARL, "§c\u25ba EnderPearl", (short)0, 16, new String[0]));
            jogador.getInventory().setItem(9, criarItem2(Material.COOKED_BEEF, "§a\u25ba Bife", (short)0, 64, new String[0]));
            jogador.getInventory().setHelmet(criarItemEnchantamento(Material.DIAMOND_HELMET, "Capacete", Enchantment.PROTECTION_ENVIRONMENTAL, 1, (short)0, new String[0]));
            jogador.getInventory().setChestplate(criarItemEnchantamento(Material.DIAMOND_CHESTPLATE, "Peitoral", Enchantment.PROTECTION_ENVIRONMENTAL, 1, (short)0, new String[0]));
            jogador.getInventory().setLeggings(criarItemEnchantamento(Material.DIAMOND_LEGGINGS, "Cal\u00e7a", Enchantment.PROTECTION_ENVIRONMENTAL, 1, (short)0, new String[0]));
            jogador.getInventory().setBoots(criarItemEnchantamento(Material.DIAMOND_BOOTS, "Bota", Enchantment.PROTECTION_ENVIRONMENTAL, 1, (short)0, new String[0]));
            adicionarFullPotion(jogador);
        }
        jogador.playSound(jogador.getLocation(), Sound.LEVEL_UP, 2.0f, 2.0f);
        final Location lobby = unserializeLocation(ConfigAPI.pegarConfig().pegarconfigCords().getString("Coordenadas." + warp));
        lobby.setPitch(0.0f);
        lobby.setYaw(0.0f);
        if (!warp.contains("1v1")) {
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage("");
            jogador.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea teleportou para o(a): §a" + warp);
        }
        jogador.teleport(lobby);
    }
    
    public static void adicionarFullPotion(final Player p) {
        for (int sopas = 0; sopas < p.getInventory().getSize(); ++sopas) {
            while (p.getInventory().getItem(sopas) == null) {
                setarItemInv(Material.POTION, "§a\u25ba Po\u00e7\u00e3o", "§7Jogue No Ch\u00e3o Para Regenerar Sua Vida.", (short)16453, sopas, 1, p);
            }
        }
    }
    
    public static void darItens1v1(final Player p) {
        final PlayerInventory i = p.getInventory();
        final ItemStack bastao = new ItemStack(Material.BLAZE_ROD);
        final ItemMeta nomekits = bastao.getItemMeta();
        nomekits.setDisplayName("§a\u25ba 1v1 Normal");
        bastao.setItemMeta(nomekits);
        final ItemStack grade = new ItemStack(Material.ANVIL);
        final ItemMeta nomevips = grade.getItemMeta();
        nomevips.setDisplayName("§b\u25ba 1v1 Custom");
        grade.setItemMeta(nomevips);
        final ItemStack fast = new ItemStack(Material.INK_SACK, 1, (short)8);
        final ItemMeta nomefast = fast.getItemMeta();
        nomefast.setDisplayName("§e\u25ba 1v1 Rapido");
        fast.setItemMeta(nomefast);
        i.clear();
        p.getInventory().setHeldItemSlot(0);
        i.setItem(2, bastao);
        i.setItem(4, grade);
        i.setItem(6, fast);
    }
    
    public static String pegarNomePequeno(final String s) {
        if (s.length() == 16) {
            final String shorts = s.substring(0, s.length() - 6);
            return shorts;
        }
        if (s.length() == 15) {
            final String shorts = s.substring(0, s.length() - 5);
            return shorts;
        }
        if (s.length() == 14) {
            final String shorts = s.substring(0, s.length() - 4);
            return shorts;
        }
        if (s.length() == 13) {
            final String shorts = s.substring(0, s.length() - 3);
            return shorts;
        }
        if (s.length() == 12) {
            final String shorts = s.substring(0, s.length() - 2);
            return shorts;
        }
        if (s.length() == 11) {
            final String shorts = s.substring(0, s.length() - 1);
            return shorts;
        }
        return s;
    }
    
    public static int pegarQuantidade(final Player p, final Material m) {
        int amount = 0;
        ItemStack[] arrayOfItemStack;
        for (int j = (arrayOfItemStack = p.getInventory().getContents()).length, i = 0; i < j; ++i) {
            final ItemStack item = arrayOfItemStack[i];
            if (item != null && item.getType() == m && item.getAmount() > 0) {
                amount += item.getAmount();
            }
        }
        return amount;
    }
    
    public static int pegarChaves(final Player p) {
        return 0;
    }
    
    public static int pegarCaixas(final Player p) {
        return 0;
    }
    
    public static void adicionarCaixas(final Player target, final Integer numero) {
    }
    
    public static void adicionarChaves(final Player target, final Integer numero) {
    }
    
    public enum Tags
    {
        NORMAL("NORMAL", 0, "§7"), 
        GOLD("GOLD", 1, "§e§lGOLD "), 
        DIAMOND("DIAMOND", 2, "§b§lDIAMOND "), 
        EMERALD("EMERALD", 3, "§a§lEMERALD "), 
        YOUTUBER("YOUTUBER", 4, "§c§lY§f§lT "), 
        YOUTUBERMIRIM("YOUTUBERMIRIM", 5, "§b§lYTMIRIM "), 
        BUILDER("BUILDER", 6, "§6§lBUILDER "), 
        TRIAL("TRIAL", 7, "§d§lTRIAL "), 
        AJUDANTE("AJUDANTE", 8, "§2§lAJUDANTE "), 
        YOUTUBERPLUS("YOUTUBERPLUS", 9, "§c§lY§f§lT+ "), 
        MOD("MOD", 10, "§5§lMOD "), 
        MODPLUS("MODPLUS", 11, "§5§lMOD+ "), 
        ADMIN("ADMIN", 12, "§c§lADMIN "), 
        COORD("COORD", 13, "§4§lCOORD "), 
        DEV("DEV", 14, "§3§lDEV "), 
        DONO("DONO", 15, "§4§lDONO ");
        
        private String msg;
        
        private Tags(final String s, final int n, final String msg) {
            this.msg = msg;
        }
        
        public String getMsg() {
            return this.msg;
        }
    }
    
    public enum TagsTeams
    {
        DIRETOR("DIRETOR", 0, "b§4§lDIRETOR "), 
        ADMINPLUS("ADMINPLUS", 1, "c§c§lADMIN+ "), 
        ADMIN("ADMIN", 2, "d§c§lADMIN "), 
        MODPLUS("MODPLUS", 3, "e§5§lMOD+ "), 
        MOD("MOD", 4, "f§5§lMOD "), 
        AJUDANTE("AJUDANTE", 5, "g§2§lAJUDANTE "), 
        BUILDER("BUILDER", 6, "h§6§lBUILDER "), 
        YOUTUBER("YOUTUBER", 7, "i§c§lY§f§lT "), 
        DEV("DEV", 8, "a§3§lDEV "), 
        GOLD("GOLD", 9, "m§e§lGOLD "), 
        DIAMOND("DIAMOND", 10, "k§b§lDIAMOND "), 
        EMERALD("EMERALD", 11, "j§a§lEMERALD "), 
        NORMAL("NORMAL", 12, "n§7");
        
        private String msg;
        
        private TagsTeams(final String s, final int n, final String msg) {
            this.msg = msg;
        }
        
        public String getMsg() {
            return this.msg;
        }
    }
    
    public static class RandomCollection<E>
    {
        private final NavigableMap<Double, E> map;
        private final Random random;
        private double total;
        
        public RandomCollection() {
            this(new Random());
        }
        
        public RandomCollection(final Random paramRandom) {
            this.map = new TreeMap<Double, E>();
            this.total = 0.0;
            this.random = paramRandom;
        }
        
        public void add(final double Chance, final E Item) {
            if (Chance <= 0.0) {
                return;
            }
            this.total += Chance;
            this.map.put(this.total, Item);
        }
        
        public E next() {
            final double d = this.random.nextDouble() * this.total;
            return this.map.ceilingEntry(d).getValue();
        }
    }
}
