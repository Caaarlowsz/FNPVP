package com.zalpha.FantasyNetwork.PvP.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import com.zalpha.FantasyNetwork.PvP.util.player.PlayerData;

public class Enums
{
    public enum Stats
    {
        CLAN("CLAN", 0, 0), 
        DINHEIRO("DINHEIRO", 1, 1), 
        KILLS("KILLS", 2, 2), 
        KILLSTREAK("KILLSTREAK", 3, 3), 
        MORTES("MORTES", 4, 4), 
        SCORE("SCORE", 5, 5), 
        CAIXAS("CAIXAS", 6, 6), 
        CHAVES("CHAVES", 7, 7);
        
        private int id;
        
        public int getId() {
            return this.id;
        }
        
        private Stats(final String s, final int n, final int index) {
            this.id = index;
        }
        
        public long getLong(final String[] split) {
            return Long.valueOf(split[this.id]);
        }
        
        public int getInt(final String[] split) {
            return Integer.parseInt(split[this.id]);
        }
        
        public String getString(final String[] split) {
            return String.valueOf(split[this.id]).toString();
        }
        
        public static String parseFrom(final PlayerData pd) {
            return String.valueOf(pd.getClan()) + " : " + pd.getMoney() + " : " + pd.getKills() + " : " + pd.getKillStreak() + " : " + pd.getDeaths() + " : " + pd.getScore() + " : " + pd.getCaixas() + " : " + pd.getChaves();
        }
    }
    
    public enum Warps
    {
        Fps("Fps", 0, Manager.criarItem2(Material.GLASS, "§7\u279c §bWarp FPS", (short)0, 1, new String[] { "§7Nesta warp voc\u00ea pode", "§7tirar um pvp sem travamentos", "§7por tratar-se de uma arena bastante", "§7leve.", "", "§e\u25ba Clique para teleportar." }), "Fps"), 
        Lv1("Lv1", 1, Manager.criarItem2(Material.BLAZE_ROD, "§7\u279c §bWarp 1v1", (short)0, 1, new String[] { "§7Nesta warp voc\u00ea pode", "§7tirar duelos customizados ou normais.", "", "§e\u25ba Clique para teleportar." }), "1v1"), 
        Lava("Lava", 2, Manager.criarItem2(Material.LAVA_BUCKET, "§7\u279c §bWarp Lava Challenge", (short)0, 1, new String[] { "§7Nesta warp voc\u00ea pode", "§7treinar refil na lava.", "", "§e\u25ba Clique para teleportar." }), "Lava"), 
        Main("Main", 3, Manager.criarItem2(Material.DIAMOND_SWORD, "§7\u279c §bWarp Main", (short)0, 1, new String[] { "§7Nesta warp voc\u00ea pode", "§7tirar duelos com itens mais", "§7fortes.", "", "§e\u25ba Clique para teleportar." }), "Main"), 
        Potion("Potion", 4, Manager.criarItem2(Material.POTION, "§7\u279c §bWarp Potion", (short)9, 1, new String[] { "§7Nesta warp voc\u00ea pode", "§7tirar duelos com po\u00e7\u00f5es", "", "§e\u25ba Clique para teleportar." }), "Potion");
        
        private ItemStack stack;
        private String name;
        
        private Warps(final String s, final int n, final ItemStack stack, final String display) {
            this.stack = stack;
            this.name = display;
        }
        
        public ItemStack getItem() {
            return this.stack;
        }
        
        public String getName() {
            return this.name;
        }
    }
}
