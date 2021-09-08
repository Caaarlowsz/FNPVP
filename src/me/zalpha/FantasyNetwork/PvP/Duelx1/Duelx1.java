package me.zalpha.FantasyNetwork.PvP.Duelx1;

import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import java.text.DecimalFormat;
import me.zalpha.FantasyNetwork.PvP.CustomEvent.PlayerDeathInWarpEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import java.util.Iterator;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;
import com.zalpha.FantasyNetwork.PvP.util.ConfigAPI;
import com.zalpha.FantasyNetwork.PvP.util.Name;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import com.zalpha.FantasyNetwork.PvP.util.Manager;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.ArrayList;
import com.zalpha.FantasyNetwork.PvP.FantasyKits;
import org.bukkit.Location;
import java.util.HashMap;
import org.bukkit.entity.Player;
import java.util.List;
import org.bukkit.event.Listener;

public class Duelx1 implements Listener
{
    public static List<Player> playersIn1v1;
    private HashMap<String, HashMap<ChanllengeType, HashMap<String, Desafio>>> playerDesafios;
    public static List<Player> playersInQueue;
    private Location firstLoction;
    private Location secondLoction;
    
    public Duelx1(final FantasyKits yBattleCraft) {
        Duelx1.playersIn1v1 = new ArrayList<Player>();
        this.playerDesafios = new HashMap<String, HashMap<ChanllengeType, HashMap<String, Desafio>>>();
        Duelx1.playersInQueue = new ArrayList<Player>();
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        final ItemStack item = event.getItem();
        if (!Manager.pegarKit(p).equalsIgnoreCase("1v1")) {
            return;
        }
        if (item == null) {
            return;
        }
        if (!event.getAction().toString().contains("RIGHT")) {
            return;
        }
        if (item.getType() != Material.INK_SACK) {
            return;
        }
        if (item.getDurability() == 8) {
            if (Duelx1.playersInQueue.contains(p)) {
                item.setDurability((short)10);
                p.sendMessage(String.valueOf(Manager.prefix) + " §cVoc\u00ea j\u00e1 esta na lista de espera, aguarde...");
                return;
            }
            if (Duelx1.playersInQueue.size() > 0) {
                final Player player = Duelx1.playersInQueue.get(0);
                if (player != null) {
                    player.sendMessage(String.valueOf(Manager.prefix) + " §a" + p.getName() + " §7foi escolhido para ser seu oponente.");
                    p.sendMessage(String.valueOf(Manager.prefix) + " §a" + player.getName() + " §7foi escolhido para ser seu oponente.");
                    this.setIn1v1(new Desafio(player, p));
                    return;
                }
            }
            item.setDurability((short)10);
            item.getItemMeta().setDisplayName("§a\u25ba 1v1 r\u00e1pido (Procurando...)");
            p.updateInventory();
            p.sendMessage(String.valueOf(Manager.prefix) + " §eVoc\u00ea entrou na fila do 1v1 r\u00e1pido, procurando um oponente...");
            Duelx1.playersInQueue.add(p);
        }
        else {
            if (!Duelx1.playersInQueue.contains(p)) {
                item.getItemMeta().setDisplayName("§7\u25ba 1v1 r\u00e1pido");
                p.updateInventory();
                p.sendMessage(String.valueOf(Manager.prefix) + " §cVoc\u00ea n\u00e3o esta na fila do 1v1 r\u00e1pido");
                item.setDurability((short)8);
                return;
            }
            item.setDurability((short)8);
            item.getItemMeta().setDisplayName("§7\u25ba 1v1 r\u00e1pido");
            p.updateInventory();
            p.sendMessage(String.valueOf(Manager.prefix) + " §cVoc\u00ea saiu da fila do 1v1 r\u00e1pido");
            Duelx1.playersInQueue.remove(p);
        }
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEntityEvent event) {
        final Player p = event.getPlayer();
        final Entity e = event.getRightClicked();
        final ItemStack item = p.getItemInHand();
        if (!Manager.pegarKit(p).equalsIgnoreCase("1v1")) {
            return;
        }
        if (!(e instanceof Player)) {
            return;
        }
        if (item == null) {
            return;
        }
        if (item.getType() != Material.BLAZE_ROD && item.getType() != Material.ANVIL) {
            return;
        }
        final Player clicado = (Player)e;
        if (isIn1v1(clicado)) {
            p.sendMessage(String.valueOf(Manager.prefix) + " §cEste jogador j\u00e1 encontra-se em uma batalha.");
            return;
        }
        if (Duelx1.playersInQueue.contains(p)) {
            p.sendMessage(String.valueOf(Manager.prefix) + " §cVoc\u00ea esta na fila do 1v1 rapido, por isso n\u00e3o pode desafiar um jogador.");
            return;
        }
        ChanllengeType type = null;
        switch (item.getType()) {
            case ANVIL: {
                type = ChanllengeType.CUSTOM;
                break;
            }
            default: {
                type = ChanllengeType.DEFAULT;
                break;
            }
        }
        if (this.hasDesafio(p, clicado, type)) {
            final Desafio desafio = this.getDesafio(p, clicado, type);
            if (type == ChanllengeType.CUSTOM) {
                if (!desafio.hasExpire()) {
                    openAccept(p, desafio);
                    return;
                }
            }
            else if (!desafio.hasExpire()) {
                p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea aceitou o desafio de §a" + clicado.getName() + "§7.");
                clicado.sendMessage(String.valueOf(Manager.prefix) + " §a" + p.getName() + " §7aceitou seu desafio.");
                this.setIn1v1(desafio);
                return;
            }
        }
        if (this.hasDesafio(clicado, p, type) && !this.getDesafio(clicado, p, type).hasExpire()) {
            p.sendMessage(String.valueOf(Manager.prefix) + " §cAguarde, pois voc\u00ea desafiou este player recentemente.");
            return;
        }
        if (type == ChanllengeType.DEFAULT) {
            HashMap<ChanllengeType, HashMap<String, Desafio>> hash;
            if (this.playerDesafios.containsKey(clicado.getName())) {
                hash = this.playerDesafios.get(clicado.getName());
            }
            else {
                hash = new HashMap<ChanllengeType, HashMap<String, Desafio>>();
            }
            HashMap<String, Desafio> hashDesa;
            if (hash.containsKey(type)) {
                hashDesa = hash.get(type);
            }
            else {
                hashDesa = new HashMap<String, Desafio>();
            }
            hashDesa.put(p.getName(), new Desafio(p, clicado));
            hash.put(type, hashDesa);
            this.playerDesafios.put(clicado.getName(), hash);
            p.sendMessage(String.valueOf(Manager.prefix) + " §aVoc\u00ea desafiou §f" + clicado.getName() + "§a para um 1v1 Normal.");
            clicado.sendMessage(String.valueOf(Manager.prefix) + " §eVoc\u00ea recebeu um desafio de §f" + p.getName() + "§e para um 1v1 Normal.");
        }
        else {
            openChallange(p, clicado);
        }
    }
    
    @EventHandler
    public void onTeleport(final PlayerTeleportEvent event) {
        if (this.playerDesafios.containsKey(event.getPlayer().getName())) {
            this.playerDesafios.remove(event.getPlayer().getName());
        }
        if (Duelx1.playersInQueue.contains(event.getPlayer())) {
            Duelx1.playersInQueue.remove(event.getPlayer());
        }
    }
    
    public boolean hasDesafio(final Player desafiado, final Player desafiante, final ChanllengeType type) {
        return this.playerDesafios.containsKey(desafiado.getName()) && this.playerDesafios.get(desafiado.getName()).containsKey(type) && this.playerDesafios.get(desafiado.getName()).get(type).containsKey(desafiante.getName());
    }
    
    public Desafio getDesafio(final Player desafiado, final Player desafiante, final ChanllengeType type) {
        return this.playerDesafios.get(desafiado.getName()).get(type).get(desafiante.getName());
    }
    
    public static boolean isIn1v1(final Player player) {
        return Duelx1.playersIn1v1.contains(player);
    }
    
    public static void openChallange(final Player p, final Player desafiador) {
        final ItemStack enviar = getItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)5), "§a\u25ba Enviar desafio para " + desafiador.getName(), new String[0]);
        final ItemStack espada = Manager.criarItem2(Material.WOOD_SWORD, "§6\u279c Espada de madeira", (short)0, 1, new String[] { "", "§e\u25ba Clique para mudar o tipo da espada." });
        final ItemStack armor = getItem(Material.BARRIER, ChatColor.RED + "Sem Armadura", "", "§e\u25ba Clique para mudar o tipo da armadura.");
        final ItemStack speed = getItem(Material.GLASS_BOTTLE, "§c\u279c Sem efeito de velocidade", "", "§e\u25ba Clique para ativar o efeito de velocidade.");
        final ItemStack forca = getItem(Material.GLASS_BOTTLE, "§c\u279c Sem efeito de for\u00e7a", "", "§e\u25ba Clique para ativar o efeito de for\u00e7a.");
        final ItemStack sopa = getItem(Material.BOWL, "§a\u279c Uma HotBar de sopa", "", "§e\u25ba Clique para mudar o tipo.");
        final ItemStack sharp = getItem(Material.BOOK, "§a\u279c Sem encantamento", "", "§e\u25ba Clique para mudar o encantamento. ");
        final Inventory inventoty = Bukkit.createInventory((InventoryHolder)null, 54, "§7Duelo contra " + Manager.pegarNomePequeno(desafiador.getName()));
        inventoty.setItem(4, Manager.criarItemSkull("§b\u25ba Duelo contra:", new String[] { "", "§b\u279c " + desafiador.getName() }, desafiador.getName(), 1));
        inventoty.setItem(20, espada);
        inventoty.setItem(22, armor);
        inventoty.setItem(24, speed);
        inventoty.setItem(33, forca);
        inventoty.setItem(29, sharp);
        inventoty.setItem(31, sopa);
        inventoty.setItem(9, enviar);
        inventoty.setItem(17, enviar);
        inventoty.setItem(18, enviar);
        inventoty.setItem(26, enviar);
        inventoty.setItem(27, enviar);
        inventoty.setItem(35, enviar);
        inventoty.setItem(36, enviar);
        inventoty.setItem(44, enviar);
        p.openInventory(inventoty);
    }
    
    public static void openAccept(final Player p, final Desafio desafio) {
        final ItemStack enviar = getItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)5), "§a\u25ba Aceitar desafio " + desafio.getDesafiante().getName(), new String[0]);
        final ItemStack recusar = getItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)14), "§c\u25ba Recusar desafio " + desafio.getDesafiante().getName(), new String[0]);
        final ItemStack espada = Manager.criarItem2(Material.valueOf(desafio.getSwordType().toUpperCase()), new Name().getName(desafio.getSwordType().toUpperCase()), (short)0, 1, new String[] { "", "§e\u25ba Esta \u00e9, a espada que voc\u00ea vai lutar. " });
        final ItemStack armor = getItem(Material.valueOf(desafio.getArmorType().toUpperCase()), new Name().getName(desafio.getArmorType().toUpperCase()), "", "§e\u25ba Esta \u00e9, \u00e1 armadura que voc\u00ea vai lutar. ");
        ItemStack speed = null;
        ItemStack forca = null;
        if (desafio.isSpeed()) {
            speed = getItem(new ItemStack(Material.POTION, 1, (short)2), "§a\u279c Com efeito de velocidade", "", "§e\u25ba Efeito de velocidade ativo. ");
        }
        else {
            speed = getItem(Material.GLASS_BOTTLE, "§c\u279c Sem efeito de velocidade", "", "§e\u25ba Efeito de velocidade desativado. ");
        }
        if (desafio.isStreght()) {
            forca = getItem(new ItemStack(Material.POTION, 1, (short)9), "§a\u279c Com efeito de forca", "", "§e\u25ba Efeito de forca ativo. ");
        }
        else {
            forca = getItem(Material.GLASS_BOTTLE, "§c\u279c Sem efeito de forca", "", "§e\u25ba Efeito de for\u00e7a desativado. ");
        }
        ItemStack sharp;
        if (!desafio.hasSharp()) {
            sharp = getItem(Material.BOOK, "§a\u279c Sem encantamento", "", "§e\u25ba Este \u00e9 o encantamento", "§eque estara na sua espada.");
        }
        else {
            sharp = (ItemStack)Manager.addGlow(getItem(Material.BOOK, "§b\u279c Afiada " + String.valueOf(desafio.getLevel()).replace("5", "V").replace("4", "IV").replace("3", "III").replace("2", "II").replace("1", "I"), "", "§e\u25ba Este \u00e9 o encantamento", "§eque estara na sua espada."));
        }
        ItemStack sopa = null;
        if (desafio.getRefil().equalsIgnoreCase("Uma HotBar de sopa")) {
            sopa = getItem(Material.BOWL, "§a\u279c Uma HotBar de sopa", "", "§e\u25ba Clique para mudar o tipo.");
        }
        else if (desafio.getRefil().equalsIgnoreCase("Full Sopa")) {
            sopa = getItem(Material.MUSHROOM_SOUP, "§a\u279c  Full Sopa", "", "§e\u25ba Clique para mudar o tipo.");
        }
        else if (desafio.getRefil().equalsIgnoreCase("Full Sopa com refil")) {
            sopa = getItem(Material.RED_MUSHROOM, "§a\u279c Full Sopa com refil", "", "§e\u25ba Clique para mudar o tipo.");
        }
        final Inventory inventoty = Bukkit.createInventory((InventoryHolder)null, 54, "§7Info do duelo " + Manager.pegarNomePequeno(desafio.getDesafiante().getName()));
        inventoty.setItem(4, Manager.criarItemSkull("§b\u25ba Info do duelo:", new String[] { "", "§b\u279c " + desafio.getDesafiante() }, desafio.getDesafiante().getName(), 1));
        inventoty.setItem(20, espada);
        inventoty.setItem(22, armor);
        inventoty.setItem(24, speed);
        inventoty.setItem(33, forca);
        inventoty.setItem(29, sharp);
        inventoty.setItem(31, sopa);
        inventoty.setItem(9, recusar);
        inventoty.setItem(17, enviar);
        inventoty.setItem(18, recusar);
        inventoty.setItem(26, enviar);
        inventoty.setItem(27, recusar);
        inventoty.setItem(35, enviar);
        inventoty.setItem(36, recusar);
        inventoty.setItem(44, enviar);
        p.openInventory(inventoty);
    }
    
    public void setIn1v1(final Desafio desafio) {
        if (desafio == null) {
            return;
        }
        final Player p = desafio.getDesafiante();
        final Player desafiado = desafio.getDesafiado();
        if (Duelx1.playersInQueue.contains(p)) {
            Duelx1.playersInQueue.remove(p);
        }
        if (Duelx1.playersInQueue.contains(desafiado)) {
            Duelx1.playersInQueue.remove(desafiado);
        }
        p.getInventory().setArmorContents(new ItemStack[0]);
        p.getInventory().setContents(new ItemStack[0]);
        desafiado.getInventory().setArmorContents(new ItemStack[0]);
        desafiado.getInventory().setContents(new ItemStack[0]);
        final String espada = desafio.getSwordType();
        final String armadura = desafio.getArmorType();
        final String sopa = desafio.getRefil();
        final boolean forca = desafio.isStreght();
        final boolean speed = desafio.isSpeed();
        final boolean sharp = desafio.hasSharp();
        final int level = desafio.getLevel();
        setSword(p, espada, sharp, level);
        setArmor(p, armadura);
        setArmor(desafiado, armadura);
        setSoup(p, sopa);
        addSpeed(p, speed);
        setSoup(desafiado, sopa);
        addStrengh(p, forca);
        setSword(desafiado, espada, sharp, level);
        addSpeed(desafiado, speed);
        addStrengh(desafiado, forca);
        Duelx1.playersIn1v1.add(p);
        Duelx1.playersIn1v1.add(desafiado);
        FantasyKits.getMain().getPlayerHideManager().hideAllPlayers(p);
        FantasyKits.getMain().getPlayerHideManager().hideAllPlayers(desafiado);
        p.showPlayer(desafiado);
        desafiado.showPlayer(p);
        if (this.firstLoction == null) {
            this.firstLoction = Manager.unserializeLocation(ConfigAPI.pegarConfig().pegarconfigCords().getString("Coordenadas.1v1_Position1"));
        }
        p.teleport(this.firstLoction);
        if (this.secondLoction == null) {
            this.secondLoction = Manager.unserializeLocation(ConfigAPI.pegarConfig().pegarconfigCords().getString("Coordenadas.1v1_Position2"));
        }
        desafiado.teleport(this.secondLoction);
        p.showPlayer(desafiado);
        desafiado.showPlayer(p);
        new Fight(this, p, desafiado);
    }
    
    private static ItemStack getItem(final Material mat, final String name, final String... desc) {
        final ItemStack item = new ItemStack(mat);
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore((List)Arrays.asList(desc));
        item.setItemMeta(itemMeta);
        return item;
    }
    
    private static ItemStack getItem(final ItemStack item, final String name, final String... desc) {
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore((List)Arrays.asList(desc));
        item.setItemMeta(itemMeta);
        return item;
    }
    
    public static void setSword(final Player p, final String material, final boolean sharpness, final int level) {
        final ItemStack item = new ItemStack(Material.valueOf(material));
        if (sharpness) {
            item.addEnchantment(Enchantment.DAMAGE_ALL, level);
        }
        p.getInventory().setItem(0, item);
    }
    
    public static void setSoup(final Player p, final String full) {
        if (full.equalsIgnoreCase("Uma HotBar de sopa")) {
            Manager.setarItemInv(Material.MUSHROOM_SOUP, "§a\u25ba Sopa", "§7Tome Para Regenerar Sua Vida.", (short)0, 1, 1, p);
            Manager.setarItemInv(Material.MUSHROOM_SOUP, "§a\u25ba Sopa", "§7Tome Para Regenerar Sua Vida.", (short)0, 2, 1, p);
            Manager.setarItemInv(Material.MUSHROOM_SOUP, "§a\u25ba Sopa", "§7Tome Para Regenerar Sua Vida.", (short)0, 3, 1, p);
            Manager.setarItemInv(Material.MUSHROOM_SOUP, "§a\u25ba Sopa", "§7Tome Para Regenerar Sua Vida.", (short)0, 4, 1, p);
            Manager.setarItemInv(Material.MUSHROOM_SOUP, "§a\u25ba Sopa", "§7Tome Para Regenerar Sua Vida.", (short)0, 5, 1, p);
            Manager.setarItemInv(Material.MUSHROOM_SOUP, "§a\u25ba Sopa", "§7Tome Para Regenerar Sua Vida.", (short)0, 6, 1, p);
            Manager.setarItemInv(Material.MUSHROOM_SOUP, "§a\u25ba Sopa", "§7Tome Para Regenerar Sua Vida.", (short)0, 7, 1, p);
            Manager.setarItemInv(Material.MUSHROOM_SOUP, "§a\u25ba Sopa", "§7Tome Para Regenerar Sua Vida.", (short)0, 8, 1, p);
        }
        else if (full.equalsIgnoreCase("Full Sopa")) {
            for (int sopas = 0; sopas < p.getInventory().getSize(); ++sopas) {
                while (p.getInventory().getItem(sopas) == null) {
                    Manager.setarItemInv(Material.MUSHROOM_SOUP, "§a\u25ba Sopa", "§7Tome Para Regenerar Sua Vida.", (short)0, sopas, 1, p);
                }
            }
        }
        else if (full.equalsIgnoreCase("Full Sopa com refil")) {
            for (int sopas = 0; sopas < p.getInventory().getSize(); ++sopas) {
                while (p.getInventory().getItem(sopas) == null) {
                    Manager.setarItemInv(Material.MUSHROOM_SOUP, "§a\u25ba Sopa", "§7Tome Para Regenerar Sua Vida.", (short)0, sopas, 1, p);
                }
            }
            Manager.setarItemInv(Material.BOWL, "§b\u25ba Tigela", "", (short)0, 13, 64, p);
            Manager.setarItemInv(Material.RED_MUSHROOM, "§c\u25ba Cogumelo", "", (short)0, 14, 64, p);
            Manager.setarItemInv(Material.BROWN_MUSHROOM, "§6\u25ba Cogumelo", "", (short)0, 15, 64, p);
        }
    }
    
    public static void addStrengh(final Player p, final boolean forca) {
        if (forca) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2000000, 0));
        }
    }
    
    public static void addSpeed(final Player p, final boolean speed) {
        if (speed) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2000000, 0));
        }
    }
    
    public static void setArmor(final Player p, String material) {
        if (material.contains("BARRIER")) {
            return;
        }
        material = material.replace("_CHESTPLATE", "");
        p.getInventory().setHelmet(new ItemStack(Material.valueOf(String.valueOf(material) + "_HELMET")));
        p.getInventory().setChestplate(new ItemStack(Material.valueOf(String.valueOf(material) + "_CHESTPLATE")));
        p.getInventory().setLeggings(new ItemStack(Material.valueOf(String.valueOf(material) + "_LEGGINGS")));
        p.getInventory().setBoots(new ItemStack(Material.valueOf(String.valueOf(material) + "_BOOTS")));
        p.updateInventory();
    }
    
    @EventHandler
    public void on1v1(final InventoryClickEvent event) {
        final ItemStack item = event.getCurrentItem();
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        if (item == null) {
            return;
        }
        if (!item.hasItemMeta()) {
            return;
        }
        if (!item.getItemMeta().hasDisplayName()) {
            return;
        }
        final Player p = (Player)event.getWhoClicked();
        final String inv = p.getOpenInventory().getTitle();
        if (!Manager.pegarKit(p).equalsIgnoreCase("1v1")) {
            return;
        }
        if (!inv.contains("§7Duelo contra ")) {
            return;
        }
        event.setCancelled(true);
        p.setItemOnCursor(new ItemStack(0));
        p.updateInventory();
        if (event.isShiftClick() || event.isRightClick()) {
            return;
        }
        if (item.getItemMeta().getDisplayName().contains("§a\u25ba Enviar desafio para ")) {
            final String sword = p.getOpenInventory().getItem(20).getType().toString();
            final String armor = p.getOpenInventory().getItem(22).getType().toString();
            final String b = p.getOpenInventory().getItem(24).getType().toString();
            final String c = p.getOpenInventory().getItem(34).getType().toString();
            final String e = p.getOpenInventory().getItem(29).getType().toString();
            final String refil = p.getOpenInventory().getItem(31).getItemMeta().getDisplayName().replace("§a\u279c ", "");
            boolean speed = true;
            boolean forca = true;
            boolean sharpness = true;
            int level = 0;
            if (b.equalsIgnoreCase(Material.GLASS_BOTTLE.toString())) {
                speed = false;
            }
            if (c.equalsIgnoreCase(Material.GLASS_BOTTLE.toString())) {
                forca = false;
            }
            if (e.equalsIgnoreCase(Material.BOOK.toString()) && p.getOpenInventory().getItem(29).getItemMeta().getDisplayName().equalsIgnoreCase("§a\u279c Sem encantamento")) {
                sharpness = false;
            }
            else {
                sharpness = true;
                final Integer l = Integer.valueOf(p.getOpenInventory().getItem(29).getItemMeta().getDisplayName().replace("§b\u279c Afiada ", "").replace("III", "3").replace("II", "2").replace("IV", "4").replace("I", "1").replace("V", "5"));
                level = l;
            }
            final Player desafiado = Bukkit.getPlayerExact(event.getInventory().getItem(4).getItemMeta().getLore().get(1).replace("§b\u279c ", ""));
            if (desafiado == null) {
                p.sendMessage(Manager.JOGADORFF);
                p.closeInventory();
                return;
            }
            if (isIn1v1(desafiado)) {
                p.sendMessage(String.valueOf(Manager.prefix) + " §cEste jogador j\u00e1 encontra-se em uma batalha.");
                p.closeInventory();
                return;
            }
            final Desafio desafio = new Desafio(p, desafiado, sword, armor, refil, speed, forca, sharpness, level);
            HashMap<ChanllengeType, HashMap<String, Desafio>> hash;
            if (this.playerDesafios.containsKey(desafiado.getName())) {
                hash = this.playerDesafios.get(desafiado.getName());
            }
            else {
                hash = new HashMap<ChanllengeType, HashMap<String, Desafio>>();
            }
            HashMap<String, Desafio> hashDesa;
            if (hash.containsKey(ChanllengeType.CUSTOM)) {
                hashDesa = hash.get(ChanllengeType.CUSTOM);
            }
            else {
                hashDesa = new HashMap<String, Desafio>();
            }
            hashDesa.put(p.getName(), desafio);
            hash.put(ChanllengeType.CUSTOM, hashDesa);
            this.playerDesafios.put(desafiado.getName(), hash);
            p.sendMessage(String.valueOf(Manager.prefix) + " §aVoc\u00ea desafiou §f" + desafiado.getName() + "§a para um 1v1 Custom.");
            desafiado.sendMessage(String.valueOf(Manager.prefix) + " §eVoc\u00ea recebeu um desafio de §f" + p.getName() + "§e para um 1v1 Custom.");
            p.closeInventory();
        }
        else if (item.getType().toString().contains("_SWORD")) {
            final String swordName = String.valueOf(this.getNextSwordLevel(item.getType().toString().replace("_SWORD", ""))) + "_SWORD";
            final ItemStack sword2 = getItem(Material.valueOf(swordName), new Name().getName(swordName), "", "§e\u25ba Clique para mudar o tipo da espada. ");
            p.getOpenInventory().setItem(event.getSlot(), sword2);
            p.playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
        }
        else if (item.getType().toString().equalsIgnoreCase("BARRIER") || item.getType().toString().contains("_CHESTPLATE")) {
            String materialName;
            if (item.getType().toString().equalsIgnoreCase("DIAMOND_CHESTPLATE")) {
                materialName = this.getNextArmorLevel(item.getType().toString().replace("_CHESTPLATE", ""));
            }
            else {
                materialName = String.valueOf(this.getNextArmorLevel(item.getType().toString().replace("_CHESTPLATE", ""))) + "_CHESTPLATE";
            }
            final ItemStack armor2 = getItem(Material.valueOf(materialName), new Name().getName(materialName), "§e\u25ba Clique para mudar o tipo da armadura. ");
            p.getOpenInventory().setItem(event.getSlot(), armor2);
            p.playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
        }
        else if (event.getSlot() == 24 && item.getType().toString().equalsIgnoreCase("GLASS_BOTTLE")) {
            final ItemStack speed2 = getItem(new ItemStack(Material.POTION, 1, (short)2), "§a\u279c Com efeito de velocidade", "", "§e\u25ba Clique para desativarr o efeito de velocidade. ");
            p.getOpenInventory().setItem(event.getSlot(), speed2);
            p.playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
        }
        else if (event.getSlot() == 24 && item.getType().toString().equalsIgnoreCase("POTION")) {
            final ItemStack speed2 = getItem(Material.GLASS_BOTTLE, "§c\u279c Sem efeito de velocidade", "", "§e\u25ba Clique para ativar o efeito de velocidade. ");
            p.getOpenInventory().setItem(event.getSlot(), speed2);
            p.playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
        }
        else if (event.getSlot() == 33 && item.getType().toString().equalsIgnoreCase("GLASS_BOTTLE")) {
            final ItemStack speed2 = getItem(new ItemStack(Material.POTION, 1, (short)9), "§a\u279c Com efeito de for\u00e7a", "", "§e\u25ba Clique para desativar o efeito de velocidade. ");
            p.getOpenInventory().setItem(event.getSlot(), speed2);
            p.playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
        }
        else if (event.getSlot() == 33 && item.getType().toString().equalsIgnoreCase("POTION")) {
            final ItemStack speed2 = getItem(Material.GLASS_BOTTLE, "§c\u279c Sem efeito de velocidade", "", "§e\u25ba Clique para ativar o efeito de velocidade. ");
            p.getOpenInventory().setItem(event.getSlot(), speed2);
            p.playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
        }
        else if (item.getType().toString().equalsIgnoreCase("BOWL")) {
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§a\u279c Uma HotBar de sopa")) {
                p.getOpenInventory().setItem(event.getSlot(), Manager.criarItem2(Material.MUSHROOM_SOUP, "§a\u279c Full Sopa", (short)0, 1, new String[] { "", "§e\u25ba Clique para mudar o tipo." }));
                p.playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
            }
        }
        else if (item.getType().toString().equalsIgnoreCase("MUSHROOM_SOUP")) {
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§a\u279c Full Sopa")) {
                p.getOpenInventory().setItem(event.getSlot(), Manager.criarItem2(Material.RED_MUSHROOM, "§a\u279c Full Sopa com refil", (short)0, 1, new String[] { "", "§e\u25ba Clique para mudar o tipo." }));
                p.playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
            }
        }
        else if (item.getType().toString().equalsIgnoreCase("RED_MUSHROOM")) {
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§a\u279c Full Sopa com refil")) {
                final ItemStack sopa = getItem(Material.BOWL, "§a\u279c Uma HotBar de sopa", "", "§e\u25ba Clique para mudar o tipo.");
                p.getOpenInventory().setItem(event.getSlot(), sopa);
                p.playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
            }
        }
        else if (item.getType().toString().equalsIgnoreCase("BOOK")) {
            if (item.getItemMeta().getDisplayName().startsWith("§b\u279c Afiada")) {
                final Integer i = Integer.valueOf(p.getOpenInventory().getItem(29).getItemMeta().getDisplayName().replace("§b\u279c Afiada ", "").replace("III", "3").replace("II", "2").replace("IV", "4").replace("I", "1").replace("V", "5"));
                String materialName;
                if (item.getType().toString().equalsIgnoreCase("BOOK") && item.getItemMeta().getDisplayName().startsWith("§b\u279c")) {
                    materialName = this.getNextEnchatementLevel(i);
                }
                else {
                    materialName = this.getNextEnchatementLevel(4);
                }
                if (materialName.equalsIgnoreCase("0")) {
                    final ItemStack sharp = getItem(Material.BOOK, "§a\u279c Sem encantamento", "", "§e\u25ba Clique para mudar o encantamento. ");
                    p.getOpenInventory().setItem(event.getSlot(), sharp);
                    p.playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
                }
                else {
                    final ItemStack sharp = (ItemStack)Manager.addGlow(getItem(Material.BOOK, "§b\u279c Afiada " + materialName, "", "§e\u25ba Clique para mudar o encantamento. "));
                    p.getOpenInventory().setItem(event.getSlot(), sharp);
                    p.playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
                }
            }
            else {
                final ItemStack sharp2 = (ItemStack)Manager.addGlow(getItem(Material.BOOK, "§b\u279c Afiada I", "", "§e\u25ba Clique para mudar o encantamento. "));
                p.getOpenInventory().setItem(event.getSlot(), sharp2);
                p.playSound(p.getLocation(), Sound.CLICK, 2.0f, 2.0f);
            }
        }
    }
    
    private String getNextSwordLevel(final String str) {
        switch (str) {
            case "DIAMOND": {
                return "WOOD";
            }
            case "IRON": {
                return "DIAMOND";
            }
            case "WOOD": {
                return "STONE";
            }
            case "STONE": {
                return "IRON";
            }
            default:
                break;
        }
        return "";
    }
    
    private String getNextEnchatementLevel(final int lvel) {
        switch (lvel) {
            case 0: {
                return "I";
            }
            case 1: {
                return "II";
            }
            case 2: {
                return "III";
            }
            case 3: {
                return "IV";
            }
            case 4: {
                return "V";
            }
            case 5: {
                return "0";
            }
            default: {
                return "0";
            }
        }
    }
    
    private String getNextArmorLevel(final String str) {
        switch (str) {
            case "DIAMOND": {
                return "BARRIER";
            }
            case "CHAINMAIL": {
                return "IRON";
            }
            case "IRON": {
                return "DIAMOND";
            }
            case "BARRIER": {
                return "LEATHER";
            }
            case "LEATHER": {
                return "CHAINMAIL";
            }
            default:
                break;
        }
        return "";
    }
    
    @EventHandler
    public void on1v1Accept(final InventoryClickEvent event) {
        final ItemStack item = event.getCurrentItem();
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        if (item == null) {
            return;
        }
        if (!item.hasItemMeta()) {
            return;
        }
        if (!item.getItemMeta().hasDisplayName()) {
            return;
        }
        final Player p = (Player)event.getWhoClicked();
        final String inv = p.getOpenInventory().getTitle();
        if (!Manager.pegarKit(p).equalsIgnoreCase("1v1")) {
            return;
        }
        if (!inv.contains("§7Info do duelo")) {
            return;
        }
        event.setCancelled(true);
        p.setItemOnCursor(new ItemStack(0));
        p.updateInventory();
        if (event.isShiftClick() || event.isRightClick()) {
            return;
        }
        final Player desafiante = Bukkit.getPlayerExact(event.getCurrentItem().getItemMeta().getDisplayName().replace("§a\u25ba Aceitar desafio ", "").replace("§c\u25ba Recusar desafio", ""));
        final Player desafiado = p;
        if (item.getItemMeta().getDisplayName().contains("§a\u25ba Aceitar desafio ")) {
            if (desafiante == null) {
                p.sendMessage(Manager.JOGADORFF);
                p.closeInventory();
                return;
            }
            if (isIn1v1(desafiante)) {
                p.sendMessage(String.valueOf(Manager.prefix) + " §cEsta jogador encontra-se em uma batalha.");
                p.closeInventory();
                return;
            }
            if (this.playerDesafios.containsKey(desafiado.getName())) {
                final HashMap<ChanllengeType, HashMap<String, Desafio>> hash = this.playerDesafios.get(desafiado.getName());
                if (hash.containsKey(ChanllengeType.CUSTOM)) {
                    final HashMap<String, Desafio> hashDesa = hash.get(ChanllengeType.CUSTOM);
                    if (hashDesa.containsKey(desafiante.getName())) {
                        final Desafio desafio = hashDesa.get(desafiante.getName());
                        this.setIn1v1(desafio);
                        return;
                    }
                }
            }
            p.sendMessage(String.valueOf(Manager.prefix) + " §cErro, tente novamente.");
            p.closeInventory();
        }
        else if (item.getItemMeta().getDisplayName().contains("§c\u25ba Recusar desafio")) {
            if (desafiante == null) {
                p.sendMessage(Manager.JOGADORFF);
                p.closeInventory();
                return;
            }
            if (isIn1v1(desafiante)) {
                p.sendMessage(String.valueOf(Manager.prefix) + " §cEste jogador j\u00e1 encontra-se em uma batalha.");
                p.closeInventory();
                return;
            }
            if (this.playerDesafios.containsKey(desafiado.getName())) {
                final HashMap<ChanllengeType, HashMap<String, Desafio>> hash = this.playerDesafios.get(desafiado.getName());
                if (hash.containsKey(ChanllengeType.CUSTOM)) {
                    final HashMap<String, Desafio> hashDesa = hash.get(ChanllengeType.CUSTOM);
                    if (hashDesa.containsKey(desafiante.getName())) {
                        hashDesa.remove(desafiante.getName());
                    }
                }
            }
            desafiante.sendMessage(String.valueOf(Manager.prefix) + " §c" + desafiado.getName() + " §7recusou seu desafio");
            desafiado.sendMessage(String.valueOf(Manager.prefix) + " §aVoc\u00ea recusou o desafio de §f" + desafiante.getName());
            p.closeInventory();
        }
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        this.handleQuit(event.getPlayer());
    }
    
    @EventHandler
    public void onKick(final PlayerKickEvent event) {
        this.handleQuit(event.getPlayer());
    }
    
    public void handleQuit(final Player p) {
        if (this.playerDesafios.containsKey(p.getName())) {
            this.playerDesafios.remove(p.getName());
        }
        if (Duelx1.playersInQueue.contains(p)) {
            Duelx1.playersInQueue.remove(p);
        }
    }
    
    public static void teleport1v1(final Player p) {
        if (p.getGameMode() == GameMode.CREATIVE) {
            p.sendMessage(String.valueOf(Manager.prefix) + " §cVoc\u00ea n\u00e3o pode entrar na arena 1v1 no modo criativo");
            return;
        }
        for (final PotionEffect potion : p.getActivePotionEffects()) {
            p.removePotionEffect(potion.getType());
        }
        p.setAllowFlight(false);
        Manager.setarKit(p, "1v1");
        Manager.teleportarWarp(p, "1v1");
        Manager.darItens1v1(p);
    }
    
    @EventHandler
    void dano(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player jogador = (Player)e.getEntity();
            if (Manager.pegarKit(jogador).equalsIgnoreCase("1v1")) {
                if (!isIn1v1(jogador)) {
                    e.setCancelled(true);
                }
                else if (isIn1v1(jogador)) {
                    e.setCancelled(false);
                }
            }
        }
    }
    
    @EventHandler
    void pegar(final PlayerPickupItemEvent e) {
        if (Manager.pegarKit(e.getPlayer()).equalsIgnoreCase("1v1")) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    private void semcomandos(final PlayerCommandPreprocessEvent e) {
        if (Manager.pegarKit(e.getPlayer()).equalsIgnoreCase("1v1") && !e.getMessage().startsWith("/1v1")) {
            e.getPlayer().sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas Voce N\u00e3o Pode Usar Comandos Na 1v1, Use /1v1 Para Sair.");
            e.setCancelled(true);
        }
        if (Manager.pegarKit(e.getPlayer()).equalsIgnoreCase("1v1") && Manager.pegarKit(e.getPlayer()).equalsIgnoreCase("1v1") && isIn1v1(e.getPlayer())) {
            e.getPlayer().sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas Voce N\u00e3o Pode Usar Comandos Em Batalha.");
            e.setCancelled(true);
        }
        if (Manager.pegarKit(e.getPlayer()).equalsIgnoreCase("1v1") && e.getMessage().startsWith("/1v1") && !isIn1v1(e.getPlayer())) {
            e.setCancelled(false);
        }
    }
    
    public enum ChanllengeType
    {
        DEFAULT("DEFAULT", 0), 
        CUSTOM("CUSTOM", 1), 
        FAST("FAST", 2);
        
        private ChanllengeType(final String s, final int n) {
        }
    }
    
    private class Fight
    {
        private Listener listener;
        
        public Fight(final Duelx1 l, final Player player1, final Player player2) {
            this.listener = (Listener)new Listener() {
                @EventHandler(priority = EventPriority.LOWEST)
                public void onDeathStatus(final PlayerDeathInWarpEvent e) {
                    final Player p = e.getPlayer();
                    if (!this.isInPvP(p)) {
                        return;
                    }
                    Player killer = null;
                    if (p == player1) {
                        killer = player2;
                    }
                    if (p == player2) {
                        killer = player1;
                    }
                    final DecimalFormat dm = new DecimalFormat("##.#");
                    p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea perdeu a batalha contra §c" + killer.getName() + " §7que ficou com §c" + dm.format(killer.getHealth() / 2.0) + " cora\u00e7\u00f5es §7e §a" + Manager.pegarQuantidade(killer, Material.MUSHROOM_SOUP) + " sopas§7.");
                    killer.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea ganhou a batalha contra §a" + p.getName() + " §7que ficou com §a" + Manager.pegarQuantidade(p, Material.MUSHROOM_SOUP) + " sopas restantes§7.");
                    p.getInventory().clear();
                    p.getInventory().setArmorContents((ItemStack[])null);
                    killer.getInventory().clear();
                    killer.getInventory().setArmorContents((ItemStack[])null);
                    FantasyKits.getMain().getPlayerHideManager().showAllPlayers(killer);
                    FantasyKits.getMain().getPlayerHideManager().showAllPlayers(p);
                    for (final PotionEffect potion : p.getActivePotionEffects()) {
                        p.removePotionEffect(potion.getType());
                    }
                    for (final PotionEffect potion : killer.getActivePotionEffects()) {
                        killer.removePotionEffect(potion.getType());
                    }
                    Manager.setarKit(p, "1v1");
                    Manager.setarKit(killer, "1v1");
                    Manager.teleportarWarp(p, "1v1");
                    Manager.teleportarWarp(killer, "1v1");
                    final Firework fire = (Firework)killer.getWorld().spawnEntity(killer.getLocation(), EntityType.FIREWORK);
                    final FireworkMeta firemeta = fire.getFireworkMeta();
                    firemeta.setPower(0);
                    firemeta.addEffects(new FireworkEffect[] { FireworkEffect.builder().trail(false).flicker(true).with(FireworkEffect.Type.BALL_LARGE).withColor(Color.ORANGE).build() });
                    Manager.darItens1v1(killer);
                    killer.setHealth(20.0);
                    killer.updateInventory();
                    killer.updateInventory();
                    Duelx1.playersIn1v1.remove(killer);
                    Fight.this.destroy();
                }
                
                @EventHandler
                public void onEntityDamage(final EntityDamageByEntityEvent event) {
                    if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
                        final Player recebe = (Player)event.getEntity();
                        final Player faz = (Player)event.getDamager();
                        if (this.isInPvP(faz) && this.isInPvP(recebe)) {
                            return;
                        }
                        if (this.isInPvP(faz) && !this.isInPvP(recebe)) {
                            event.setCancelled(true);
                        }
                        else if (!this.isInPvP(faz) && this.isInPvP(recebe)) {
                            event.setCancelled(true);
                        }
                    }
                }
                
                @EventHandler
                public void onQuit(final PlayerQuitEvent event) {
                    this.handleQuit(event.getPlayer());
                }
                
                @EventHandler
                public void onKick(final PlayerKickEvent event) {
                    this.handleQuit(event.getPlayer());
                }
                
                public void handleQuit(final Player p) {
                    if (!this.isInPvP(p)) {
                        return;
                    }
                    Player killer = null;
                    if (p == player1) {
                        killer = player2;
                    }
                    if (p == player2) {
                        killer = player1;
                    }
                    killer.sendMessage(String.valueOf(Manager.prefix) + " §c" + p.getName() + " durante a batalha.");
                    Duelx1.teleport1v1(killer);
                    killer.setHealth(20.0);
                    killer.updateInventory();
                    if (Duelx1.isIn1v1(p)) {
                        Duelx1.playersIn1v1.remove(p);
                    }
                    if (Duelx1.isIn1v1(killer)) {
                        Duelx1.playersIn1v1.remove(killer);
                    }
                    Manager.removerArrays(p);
                    FantasyKits.getMain().getPlayerHideManager().showAllPlayers(killer);
                    FantasyKits.getMain().getPlayerHideManager().showAllPlayers(p);
                    p.damage(2000.0, (Entity)killer);
                    final Firework fire = (Firework)killer.getWorld().spawnEntity(killer.getLocation(), EntityType.FIREWORK);
                    final FireworkMeta firemeta = fire.getFireworkMeta();
                    firemeta.setPower(0);
                    firemeta.addEffects(new FireworkEffect[] { FireworkEffect.builder().trail(false).flicker(true).with(FireworkEffect.Type.BALL_LARGE).withColor(Color.ORANGE).build() });
                    Fight.this.destroy();
                }
                
                public boolean isInPvP(final Player player) {
                    return player == player1 || player == player2;
                }
            };
            Bukkit.getPluginManager().registerEvents(this.listener, FantasyKits.getPlugin());
        }
        
        public void destroy() {
            HandlerList.unregisterAll(this.listener);
        }
    }
}
