package com.zalpha.FantasyNetwork.PvP.util;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import me.zalpha.FantasyNetwork.PvP.Duelx1.Duelx1;
import org.bukkit.Sound;
import java.util.List;
import org.bukkit.Material;
import java.util.ArrayList;
import org.bukkit.inventory.ItemStack;
import com.sun.xml.internal.ws.util.StringUtils;
import org.bukkit.entity.Player;

public class InventarioKits extends MenuPaged
{
    public InventarioKits(final Player p, final Type t) {
        super(p, "§7" + StringUtils.capitalize(t.toString().toLowerCase()) + " (Pagina <pag>) \u25ba", t.getSize());
        if (t == Type.KITS) {
            this.addNoItems(null, 9, 17, 18, 27, 35, 26, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
            final List<ItemStack> all = new ArrayList<ItemStack>();
            for (int x = 0; x < Kits.values().length; ++x) {
                if (p.hasPermission("kit." + Kits.values()[x].toString())) {
                    all.add(Kits.values()[x].getItem());
                }
            }
            this.addItems(all, 19, 44, Type.KITS);
            this.openPage(p, 1);
        }
        if (t == Type.WARPS) {
            this.addNoItems(null, 9, 17, 18, 27, 35, 26, 36);
            final List<ItemStack> all = new ArrayList<ItemStack>();
            for (int x = 0; x < Enums.Warps.values().length; ++x) {
                if (ConfigAPI.pegarConfig().pegarconfigCords().getString("Coordenadas." + Enums.Warps.values()[x].getName()) != null) {
                    all.add(Enums.Warps.values()[x].getItem());
                }
            }
            if (all.isEmpty()) {
                all.add(Manager.criarItem(Material.STAINED_GLASS_PANE, "§cErro", (short)0, new String[] { "§7Nenhuma warp foi setada." }));
                this.addItems(all, 22, 22, Type.WARPS);
            }
            else {
                this.addItems(all, 10, 34, Type.WARPS);
                this.openPage(p, 1);
            }
            this.openPage(p, 1);
        }
    }
    
    public void onClick(final Player p, final ItemStack item) {
        if (item.equals((Object)InventarioKits.previous)) {
            this.openPage(p, this.currentPage - 1);
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 2.0f, 2.0f);
        }
        else if (item.equals((Object)InventarioKits.next)) {
            this.openPage(p, this.currentPage + 1);
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 2.0f, 2.0f);
        }
        else if (item.getItemMeta().getDisplayName().startsWith("§7\u279c §aKit ")) {
            if (p.hasPermission("kit." + item.getItemMeta().getDisplayName().replace("§7\u279c §aKit ", "").toUpperCase())) {
                if (!Manager.comKit(p)) {
                    final Kits kit = Kits.valueOf(item.getItemMeta().getDisplayName().replace("§7\u279c §aKit ", "").toUpperCase());
                    Manager.darKit(p, kit);
                    p.sendMessage(String.valueOf(Manager.PEGOUKIT) + item.getItemMeta().getDisplayName().replace("§7\u279c §aKit ", ""));
                    p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                    p.closeInventory();
                }
                else {
                    p.sendMessage(Manager.UMKITPORVIDA);
                }
            }
            else {
                new InventarioKits(p, Type.KITS);
            }
        }
        else if (item.getItemMeta().getDisplayName().startsWith("§7\u279c §bWarp")) {
            if (item.getItemMeta().getDisplayName().replace("§7\u279c §bWarp ", "").replace("FPS", "Fps").replace("Lava Challenge", "Lava").equalsIgnoreCase("1v1")) {
                Duelx1.teleport1v1(p);
            }
            else {
                Manager.teleportarWarp(p, item.getItemMeta().getDisplayName().replace("§7\u279c §bWarp ", "").replace("FPS", "Fps").replace("Lava Challenge", "Lava"));
                Manager.setarKit(p, item.getItemMeta().getDisplayName().replace("§7\u279c §bWarp ", "").replace("FPS", "Fps").replace("Lava Challenge", "Lava"));
            }
            p.closeInventory();
        }
    }
    
    @EventHandler
    private void onClick(final InventoryClickEvent e) {
        if (this.inventoryEquals(e.getInventory())) {
            e.setCancelled(true);
            final ItemStack item = e.getCurrentItem();
            final Player p = (Player)e.getWhoClicked();
            if (item != null && item.hasItemMeta() && p == this.getPlayer()) {
                if (e.getInventory().getTitle().startsWith("§7Kits ")) {
                    this.onClick(p, item);
                }
                if (e.getInventory().getTitle().startsWith("§7Warps ")) {
                    this.onClick(p, item);
                }
            }
        }
    }
}
