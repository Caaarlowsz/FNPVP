package com.zalpha.FantasyNetwork.PvP.util;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.UUID;
import org.bukkit.ChatColor;
import java.text.DecimalFormat;
import com.zalpha.FantasyNetwork.PvP.util.player.PlayerData;
import org.bukkit.inventory.Inventory;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Inventarios
{
    public static void loadToggle(final Player jogador) {
        if (!Manager.Tell.containsKey(jogador.getName())) {
            Manager.Tell.put(jogador.getName(), true);
        }
        if (!Manager.Report.containsKey(jogador.getName())) {
            Manager.Report.put(jogador.getName(), true);
        }
    }
    
    public static void inventariotoggle(final Player jogador, final MenuPaged.Type t) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 54, "§7Op\u00e7\u00f5es \u25ba ");
        loadToggle(jogador);
        inv.setItem(4, Manager.criarItem(Material.NETHER_STAR, "§b\u25ba Op\u00e7\u00f5es", (short)0, new String[] { "", "§7Escolha As Op\u00e7\u00f5es." }));
        if (jogador.hasPermission("fantasy.veravisos")) {
            inv.setItem(20, Manager.criarItem(Material.BOOK, "§b\u25ba Report", (short)0, new String[] { "", "§7Clique No Icone Abaixo." }));
        }
        else {
            inv.setItem(20, Manager.criarItem(Material.BOOK, "§c\u25ba Report", (short)0, new String[] { "" }));
        }
        inv.setItem(24, Manager.criarItem(Material.PAPER, "§b\u25ba Tell", (short)0, new String[] { "", "§7Clique No Icone Abaixo." }));
        if (Manager.comReportAtivado(jogador)) {
            inv.setItem(29, Manager.criarItem(Material.INK_SACK, "§a\u25ba Receber Report - Ativado", (short)10, new String[] { "", "§7Clique Para Alterar." }));
        }
        else {
            inv.setItem(29, Manager.criarItem(Material.INK_SACK, "§c\u25ba Receber Report - Desativado", (short)8, new String[] { "", "§7Clique Para Alterar." }));
        }
        if (!jogador.hasPermission("fantasy.veravisos")) {
            inv.setItem(29, Manager.criarItem(Material.INK_SACK, "§c\u25ba Voce N\u00e3o Tem Esta Op\u00e7\u00e3o", (short)1, new String[] { "" }));
        }
        if (Manager.comTellAtivado(jogador)) {
            inv.setItem(33, Manager.criarItem(Material.INK_SACK, "§a\u25ba Receber Tell - Ativado", (short)10, new String[] { "", "§7Clique Para Alterar." }));
        }
        else {
            inv.setItem(33, Manager.criarItem(Material.INK_SACK, "§c\u25ba Receber Tell - Desativado", (short)8, new String[] { "", "§7Clique Para Alterar." }));
        }
        if (t == MenuPaged.Type.PERFIL) {
            inv.setItem(49, Manager.criarItem2(Material.ARROW, "§bVoltar", (short)0, 1, new String[] { "", "§7Clique para voltar." }));
        }
        jogador.openInventory(inv);
    }
    
    public static void inventarioestatisticas(final Player jogador) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 54, "§7Estat\u00edsticas \u25ba ");
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
        final DecimalFormat df = new DecimalFormat("#.##");
        inv.setItem(4, Manager.criarItem2(Material.BOOK, "§b\u25ba Estat\u00edsticas ", (short)0, 1, new String[] { "", "§7Aqui est\u00e3o todas as suas estat\u00edsticas", "§7no kitpvp." }));
        inv.setItem(20, Manager.criarItem2(Material.SKULL_ITEM, "§a\u25ba Kills ", (short)0, 1, new String[] { "", "§7Voc\u00ea tem \u279c §a" + pd.getKills() + " kills" }));
        inv.setItem(21, Manager.criarItem2(Material.DEAD_BUSH, "§c\u25ba Mortes ", (short)0, 1, new String[] { "", "§7Voc\u00ea tem \u279c §c" + pd.getDeaths() + " mortes" }));
        inv.setItem(22, Manager.criarItem2(Material.NETHER_STAR, "§" + pd.getRank().split("")[1] + "\u25ba Rank ", (short)0, 1, new String[] { "", "§7Seu rank \u00e9 §" + pd.getRank().split("")[1] + "\u279c " + ChatColor.stripColor(pd.getRankSymbol()) + " " + ChatColor.stripColor(pd.getRank()) }));
        inv.setItem(23, Manager.criarItem2(Material.GOLD_NUGGET, "§e\u25ba Dinheiro ", (short)0, 1, new String[] { "", "§7Voc\u00ea tem \u279c §e" + Manager.modificarCoins(pd.getMoney()) + "$" }));
        inv.setItem(24, Manager.criarItem2(Material.PAPER, "§e\u25ba K.D  ", (short)0, 1, new String[] { "", "§7Seu K.D em % \u279c §a" + Manager.getProgressExact(total, value) + "%", "§7Seu K.D em divis\u00e3o \u279c §a" + String.valueOf(df.format(total)) }));
        inv.setItem(31, Manager.criarItem2(Material.EYE_OF_ENDER, "§a\u25ba Score ", (short)0, 1, new String[] { "", "§7Voc\u00ea tem \u279c §a" + Manager.modificarCoins(pd.getScore()) + " score" }));
        inv.setItem(49, Manager.criarItem2(Material.ARROW, "§bVoltar", (short)0, 1, new String[] { "", "§7Clique para voltar." }));
        jogador.openInventory(inv);
    }
    
    public static void inventarioestatisticassecond(final Player view, final UUID target, final String name) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 54, "§7Estat\u00edsticas \u25ba ");
        final PlayerData pd = PlayerData.get(Bukkit.getPlayer(target));
        if (pd == null) {
            view.sendMessage(String.valueOf(Manager.prefix) + " §CEste jogador n\u00e3o possui uma conta registrada.");
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
        final DecimalFormat df = new DecimalFormat("#.##");
        inv.setItem(4, Manager.criarItem2(Material.BOOK, "§b\u25ba Estat\u00edsticas ", (short)0, 1, new String[] { "", "§7Aqui est\u00e3o todas as suas estat\u00edsticas", "§7no kitpvp." }));
        inv.setItem(20, Manager.criarItem2(Material.SKULL_ITEM, "§a\u25ba Kills ", (short)0, 1, new String[] { "", "§7Voc\u00ea tem \u279c §a" + pd.getKills() + " kills" }));
        inv.setItem(21, Manager.criarItem2(Material.DEAD_BUSH, "§c\u25ba Mortes ", (short)0, 1, new String[] { "", "§7Voc\u00ea tem \u279c §c" + pd.getDeaths() + " mortes" }));
        inv.setItem(22, Manager.criarItem2(Material.NETHER_STAR, "§" + PlayerData.Ranks.getRank(pd.getScore()).getRankDisplay().split("")[1] + "\u25ba Rank ", (short)0, 1, new String[] { "", "§7Seu rank \u00e9 §" + PlayerData.Ranks.getRank(pd.getScore()).getRankDisplay().split("")[1] + "\u279c " + ChatColor.stripColor(PlayerData.Ranks.getRank(pd.getScore()).getSymbol()) + " " + ChatColor.stripColor(PlayerData.Ranks.getRank(pd.getScore()).getRankDisplay()) }));
        inv.setItem(23, Manager.criarItem2(Material.GOLD_NUGGET, "§e\u25ba Dinheiro ", (short)0, 1, new String[] { "", "§7Voc\u00ea tem \u279c §e" + Manager.modificarCoins(pd.getMoney()) + "$" }));
        inv.setItem(24, Manager.criarItem2(Material.PAPER, "§e\u25ba K.D  ", (short)0, 1, new String[] { "", "§7Seu K.D em % \u279c §a" + Manager.getProgressExact(total, value) + "%", "§7Seu K.D em divis\u00e3o \u279c §a" + String.valueOf(df.format(total)) }));
        inv.setItem(31, Manager.criarItem2(Material.EYE_OF_ENDER, "§a\u25ba Score ", (short)0, 1, new String[] { "", "§7Voc\u00ea tem \u279c §a" + Manager.modificarCoins(pd.getScore()) + " score" }));
        inv.setItem(49, Manager.criarItem2(Material.ARROW, "§bVoltar", (short)0, 1, new String[] { "", "§7Clique para voltar." }));
        view.openInventory(inv);
    }
    
    public static void openMenu(final Player p) {
    }
    
    public static void inventarioperfil(final Player jogador) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)jogador, 27, "§7Perfil \u25ba");
        inv.setItem(11, Manager.criarItem2(Material.BOOK, "§a\u25ba Estat\u00edsticas", (short)0, 1, new String[] { "", "§7Clique para ver seu perfil no kitpvp." }));
        inv.setItem(13, Manager.criarItemSkull("§b\u25ba Detalhes da conta", new String[] { "", "§7Rank §" + Manager.pegarRank(jogador).split("")[1] + "\u279c " + ChatColor.stripColor(Manager.pegarRank(jogador)), "§7Primeiro login §a\u279c " + new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(new Date(jogador.getFirstPlayed())), "§7Ultimo login §e\u279c " + new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(new Date(jogador.getLastPlayed())) }, jogador.getName(), 1));
        inv.setItem(15, Manager.criarItem2(Material.PAINTING, "§e\u25ba Prefer\u00eancias", (short)0, 1, new String[] { "", "§7Clique para ver." }));
        jogador.openInventory(inv);
    }
}
