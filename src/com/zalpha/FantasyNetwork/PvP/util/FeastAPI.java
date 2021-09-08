package com.zalpha.FantasyNetwork.PvP.util;

import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;
import java.util.Random;
import com.zalpha.FantasyNetwork.PvP.FantasyKits;
import org.bukkit.Location;
import java.util.Iterator;
import org.bukkit.block.Chest;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import com.google.common.collect.Lists;
import org.bukkit.block.Block;
import java.util.List;

public class FeastAPI
{
    public static int ContagemFeast;
    private static List<Block> chests;
    
    static {
        FeastAPI.ContagemFeast = 600;
        FeastAPI.chests = (List<Block>)Lists.newArrayList();
    }
    
    public static void Feast() {
        new BukkitRunnable() {
            public void run() {
                switch (FeastAPI.ContagemFeast) {
                    case 600: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §a§7O feast sera recarregado com §aitens §7em §a10 Minutos§7.");
                        break;
                    }
                    case 300: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §a§7O feast sera recarregado com §aitens §7em §a5 Minutos§7.");
                        break;
                    }
                    case 240: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §a§7O feast sera recarregado com §aitens §7em §a4 Minutos§7.");
                        break;
                    }
                    case 180: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §a§7O feast sera recarregado com §aitens §7em §a3 Minutos§7.");
                        break;
                    }
                    case 120: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §a§7O feast sera recarregado com §aitens §7em §a2 Minutos§7.");
                        break;
                    }
                    case 60: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §a§7O feast sera recarregado com §aitens §7em §a1 Minuto§7.");
                        break;
                    }
                    case 50: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §a§7O feast sera recarregado com §aitens §7em §a50 segundos§7.");
                        break;
                    }
                    case 15: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §a§7O feast sera recarregado com §aitens §7em §a15 segundos§7.");
                        break;
                    }
                    case 10: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §a§7O feast sera recarregado com §aitens §7em §a10 segundos§7.");
                        break;
                    }
                    case 5: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §a§7O feast sera recarregado com §aitens §7em §a5 segundos§7.");
                        break;
                    }
                    case 4: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §a§7O feast sera recarregado com §aitens §7em §a4 segundos§7.");
                        break;
                    }
                    case 3: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §a§7O feast sera recarregado com §aitens §7em §a3 segundos§7.");
                        break;
                    }
                    case 2: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §a§7O feast sera recarregado com §aitens §7em §a2 segundos§7.");
                        break;
                    }
                    case 1: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §a§7O feast sera recarregado com §aitens §7em §a1 segundo§7.");
                        break;
                    }
                    case 0: {
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §aO feast foi recarregado com itens§a.");
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §aEm §f10 minutos §aele sera recarregado novamente§7.");
                        FeastAPI.ContagemFeast = 660;
                        for (final Block chest : FeastAPI.chests) {
                            chest.setType(Material.CHEST);
                            chest.getState().update();
                        }
                        if (ConfigAPI.pegarConfig().pegarconfigCords().getString("Coordenadas.Feast") != null) {
                            final Location lobby = Manager.unserializeLocation(ConfigAPI.pegarConfig().pegarconfigCords().getString("Coordenadas.Feast"));
                            for (final Block blocos : FeastAPI.pegarBlocosPorPerto(lobby, 10)) {
                                if (blocos.getType() == Material.CHEST) {
                                    final Chest chest2 = (Chest)blocos.getState();
                                    EncherFeast(chest2);
                                }
                            }
                            break;
                        }
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §aO feast n\u00e3o Foi spawnado pois n\u00e3o foi setado§a.");
                        Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §aEm §f10 minutos §aele tentar\u00e1 spawnar novamente.");
                        break;
                    }
                }
                if (FeastAPI.ContagemFeast > 0) {
                    --FeastAPI.ContagemFeast;
                }
            }
        }.runTaskTimer(FantasyKits.getPlugin(), 0L, 20L);
    }
    
    private static void EncherFeast(final Chest chest) {
        final Manager.RandomCollection<ItemStack> ItemAleatorio = pegarItensFeast();
        final Random random = new Random();
        if (random.nextInt(100) < 65) {
            chest.getInventory().clear();
            chest.getInventory().addItem(new ItemStack[] { ItemAleatorio.next() });
            chest.getInventory().addItem(new ItemStack[] { ItemAleatorio.next() });
            chest.getInventory().addItem(new ItemStack[] { ItemAleatorio.next() });
            chest.getInventory().addItem(new ItemStack[] { ItemAleatorio.next() });
            chest.getInventory().addItem(new ItemStack[] { ItemAleatorio.next() });
            chest.getInventory().addItem(new ItemStack[] { ItemAleatorio.next() });
            chest.getInventory().addItem(new ItemStack[] { ItemAleatorio.next() });
            chest.getInventory().addItem(new ItemStack[] { ItemAleatorio.next() });
            chest.update();
        }
    }
    
    public static List<Block> pegarBlocosPorPerto(final Location location, final int radius) {
        final List<Block> blocks = new ArrayList<Block>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; ++x) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; ++y) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; ++z) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
    
    public static Manager.RandomCollection<ItemStack> pegarItensFeast() {
        try {
            final Manager.RandomCollection<ItemStack> ItemAleatorio = new Manager.RandomCollection<ItemStack>();
            ItemAleatorio.add(15.0, Manager.criarItem2(Material.LEATHER_HELMET, "§eCapacete", (short)0, 1, new String[0]));
            ItemAleatorio.add(10.0, Manager.criarItem2(Material.CHAINMAIL_CHESTPLATE, "§ePeitoral", (short)0, 1, new String[0]));
            ItemAleatorio.add(11.0, Manager.criarItem2(Material.CHAINMAIL_LEGGINGS, "§eCal\u00e7a", (short)0, 1, new String[0]));
            ItemAleatorio.add(10.0, Manager.criarItem2(Material.LEATHER_BOOTS, "§eBota", (short)0, 1, new String[0]));
            ItemAleatorio.add(8.0, Manager.criarItem2(Material.IRON_SWORD, "§eEspada", (short)0, 1, new String[0]));
            ItemAleatorio.add(13.0, Manager.criarItem2(Material.BOW, "§eArco", (short)0, 1, new String[0]));
            ItemAleatorio.add(12.0, Manager.criarItem2(Material.GOLDEN_APPLE, "§eMa\u00e7a", (short)0, 5, new String[0]));
            ItemAleatorio.add(14.0, Manager.criarItem2(Material.ARROW, "§eFlecha", (short)0, 4, new String[0]));
            ItemAleatorio.add(18.0, Manager.criarItem2(Material.STONE_SWORD, "§eEspada", (short)0, 1, new String[0]));
            ItemAleatorio.add(11.0, Manager.criarItem2(Material.MUSHROOM_SOUP, "§eSopa", (short)0, 4, new String[0]));
            ItemAleatorio.add(6.0, Manager.criarItem2(Material.POTION, "§ePo\u00e7\u00e3o", (short)16460, 1, new String[0]));
            ItemAleatorio.add(8.0, Manager.criarItem2(Material.POTION, "§ePo\u00e7\u00e3o", (short)16425, 1, new String[0]));
            ItemAleatorio.add(5.0, Manager.criarItem2(Material.POTION, "§ePo\u00e7\u00e3o", (short)16449, 1, new String[0]));
            ItemAleatorio.add(9.0, Manager.criarItem2(Material.POTION, "§ePo\u00e7\u00e3o", (short)16420, 1, new String[0]));
            ItemAleatorio.add(6.0, Manager.criarItem2(Material.POTION, "§ePo\u00e7\u00e3o", (short)16418, 1, new String[0]));
            ItemAleatorio.add(8.0, Manager.criarItem2(Material.ENDER_PEARL, "§eEnderPearl", (short)0, 1, new String[0]));
            ItemAleatorio.add(13.0, Manager.criarItem2(Material.EXP_BOTTLE, "§ePo\u00e7\u00e3o De Xp", (short)0, 1, new String[0]));
            return ItemAleatorio;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
