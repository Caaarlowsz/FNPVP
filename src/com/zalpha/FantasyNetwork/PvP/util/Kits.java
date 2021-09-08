package com.zalpha.FantasyNetwork.PvP.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Kits
{
    PVP("PVP", 0, Manager.criarItem2(Material.STONE_SWORD, "§7\u279c §aKit PvP", (short)0, 1, new String[] { "§7Pegue o kit b\u00e1sico contendo", "§7nenhuma habilidade.", "", "§e\u25ba Clique para pegar." })), 
    KANGAROO("KANGAROO", 1, Manager.criarItem2(Material.FIREWORK, "§7\u279c §aKit Kangaroo", (short)0, 1, new String[] { "§7Com este kit voc\u00ea", "§7ir\u00e1 locomover-se mais", "§7r\u00e1pido como um canguru.", "", "§e\u25ba Clique para pegar." })), 
    ANCHOR("ANCHOR", 2, Manager.criarItem2(Material.ANVIL, "§7\u279c §aKit Anchor", (short)0, 1, new String[] { "§7Ultilizando este kit", "§7voc\u00ea n\u00e3o ir\u00e1 mais tomar e", "§7nem darrepuls\u00e3o.", "", "§e\u25ba Clique para pegar." })), 
    FISHERMAN("FISHERMAN", 3, Manager.criarItem2(Material.FISHING_ROD, "§7\u279c §aKit Fisherman", (short)0, 1, new String[] { "§7Puxe seus inimigos para voc\u00ea.", "", "§e\u25ba Clique para pegar." })), 
    ARCHER("ARCHER", 4, Manager.criarItem2(Material.BOW, "§7\u279c §aKit Archer", (short)0, 1, new String[] { "§7Tenha um arco que elimina inimigo com um tiro.", "", "§e\u25ba Clique para pegar." })), 
    SWITCHER("SWITCHER", 5, Manager.criarItem2(Material.SNOW_BALL, "§7\u279c §aKit Switcher", (short)0, 1, new String[] { "§7Troque De Posi\u00e7\u00e3o Com Seu Inimigo.", "", "§e\u25ba Clique para pegar." })), 
    THOR("THOR", 6, Manager.criarItem2(Material.GOLD_AXE, "§7\u279c §aKit Thor", (short)0, 1, new String[] { "§7Solte Raios E Destrua Seus Inimigos.", "", "§e\u25ba Clique para pegar." })), 
    NINJA("NINJA", 7, Manager.criarItem2(Material.COMPASS, "§7\u279c §aKit Ninja", (short)0, 1, new String[] { "§7Teleporte Para O Ultimo Jogador Hitado Por Voc\u00ea.", "", "§e\u25ba Clique para pegar." })), 
    GLADIATOR("GLADIATOR", 8, Manager.criarItem2(Material.IRON_FENCE, "§7\u279c §aKit Gladiator", (short)0, 1, new String[] { "§7Puxe Inimigos Para Uma Jogo Sombrio.", "", "§e\u25ba Clique para pegar." })), 
    MAGMA("MAGMA", 9, Manager.criarItem2(Material.MAGMA_CREAM, "§7\u279c §aKit Magma", (short)0, 1, new String[] { "§7Tenha 33% De Chance De Atear Fogo Em Seus Inimigos.", "", "§e\u25ba Clique para pegar." })), 
    VIPER("VIPER", 10, Manager.criarItem2(Material.SPIDER_EYE, "§7\u279c §aKit Viper", (short)0, 1, new String[] { "§7Tenha 33% De Chance De Envenenar Seu Inimigo.", "", "§e\u25ba Clique para pegar." })), 
    SNAIL("SNAIL", 11, Manager.criarItem2(Material.SOUL_SAND, "§7\u279c §aKit Snail", (short)0, 1, new String[] { "§7Tenha 33% De Chance De Aplicar Lentidao Em Seu Inimigo.", "", "§e\u25ba Clique para pegar." })), 
    PHANTOM("PHANTOM", 12, Manager.criarItem2(Material.FEATHER, "§7\u279c §aKit Phantom", (short)0, 1, new String[] { "§7Transforme-se Em Um Fantasma E Voe Por 10 Segundos.", "", "§e\u25ba Clique para pegar." })), 
    FIREMAN("FIREMAN", 13, Manager.criarItem2(Material.LAVA_BUCKET, "§7\u279c §aKit Fireman", (short)0, 1, new String[] { "§7Solte Raios E Destrua Seus Inimigos.", "", "§e\u25ba Clique para pegar." })), 
    SNIPER("SNIPER", 14, Manager.criarItem2(Material.BLAZE_ROD, "§7\u279c §aKit Sniper", (short)0, 1, new String[] { "§7N\u00e3o Leve Dano Para Fogo Ou Derivados.", "", "§e\u25ba Clique para pegar." })), 
    POSEIDON("POSEIDON", 15, Manager.criarItem2(Material.WATER_BUCKET, "§7\u279c §aKit Poseidon", (short)0, 1, new String[] { "§7Ao Entrar Na Agua Fique Mais Forte.", "", "§e\u25ba Clique para pegar." })), 
    DESHFIRE("DESHFIRE", 16, Manager.criarItem2(Material.REDSTONE_BLOCK, "§7\u279c §aKit Deshfire", (short)0, 1, new String[] { "§7D\u00ea Um Boost E Taque Fogo", "§7Em Seus Inimigos.", "", "§e\u25ba Clique para pegar." })), 
    SONIC("SONIC", 17, Manager.criarItem2(Material.LAPIS_BLOCK, "§7\u279c §aKit Sonic", (short)0, 1, new String[] { "§7D\u00ea Um Boost E Envenene Seus Inimigos.", "", "§e\u25ba Clique para pegar." })), 
    TURTLE("TURTLE", 18, Manager.criarItem2(Material.DIAMOND_CHESTPLATE, "§7\u279c §aKit Turtle", (short)0, 1, new String[] { "§7Enquanto Estiver No Shift N\u00e3o Tome ", "§7Nenhum Dano E Tambem N\u00e3o D\u00ea.", "", "§e\u25ba Clique para pegar." })), 
    STOMPER("STOMPER", 19, Manager.criarItem2(Material.IRON_BOOTS, "§7\u279c §aKit Stomper", (short)0, 1, new String[] { "§7Ao Pular Em Inimigos Quando Eles N\u00e3o Estiverem ", "§7No Shift De Dano Ou Ate Mesmo Mate-o.", "", "§e\u25ba Clique para pegar." })), 
    HEADSHOT("HEADSHOT", 20, Manager.criarItem2(Material.SKULL_ITEM, "§7\u279c §aKit Headshot", (short)0, 1, new String[] { "§7Ao Bater Na Cabe\u00e7a(A De Cima) De Seu ", "§7Inimigo De Mais Dano Nele.", "", "§e\u25ba Clique para pegar." })), 
    FLASH("FLASH", 21, Manager.criarItem2(Material.REDSTONE_BLOCK, "§7\u279c §aKit Flash", (short)0, 1, new String[] { "§7Ao Entrar Na Agua Fique Mais Forte.", "", "§e\u25ba Clique para pegar." })), 
    DRAIG("DRAIG", 22, Manager.criarItem2(Material.BLAZE_POWDER, "§7\u279c §aKit Draig", (short)0, 1, new String[] { "§7Ao Clicar Na Item Do Kit Voce Ira Ganhar ", "§7Velocidade,E Ao Bater No ", "§7Inimigo Bagun\u00e7e Seu Inventario.", "", "§e\u25ba Clique para pegar." })), 
    THRESH("THRESH", 23, Manager.criarItem2(Material.LEASH, "§7\u279c §aKit Thresh", (short)0, 1, new String[] { "§7Puxe Os Jogadores Com Sua Corda At\u00e9 Voce.", "", "§e\u25ba Clique para pegar." })), 
    BOXER("BOXER", 24, Manager.criarItem2(Material.QUARTZ, "§7\u279c §aKit Boxer", (short)0, 1, new String[] { "§7Ao hitar um jogador voce", "§7vai dar mais dano e levar menos dano.", "", "§e\u25ba Clique para pegar." })), 
    MADMAN("MADMAN", 25, Manager.criarItem2(Material.POTION, "§7\u279c §aKit Madman", (short)0, 1, new String[] { "§7Cause fraqueza ao jogador", "§7que voce atingir!", "", "§e\u25ba Clique para pegar." }));
    
    private ItemStack item;
    
    private Kits(final String s, final int n, final ItemStack stack) {
        this.item = stack;
    }
    
    public ItemStack getItem() {
        return this.item;
    }
}
