package com.zalpha.FantasyNetwork.PvP.util;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Name {
	private HashMap<String, String> NAMES;

	public Name() {
		(this.NAMES = new HashMap<String, String>()).put("WOOD_SWORD", "§6\u279c Espada de madeira");
		this.NAMES.put("STONE_SWORD", "§7\u279c Espada de pedra");
		this.NAMES.put("IRON_SWORD", "§f\u279c Espada de ferro");
		this.NAMES.put("DIAMOND_SWORD", "§b\u279c Espada de diamante");
		this.NAMES.put("BARRIER", ChatColor.RED + "Sem Armadura");
		this.NAMES.put("LEATHER_CHESTPLATE", ChatColor.GOLD + "Armadura de Couro");
		this.NAMES.put("CHAINMAIL_CHESTPLATE", ChatColor.GRAY + "Armadura de Cota de Malha");
		this.NAMES.put("IRON_CHESTPLATE", ChatColor.WHITE + "Armadura de Ferro");
		this.NAMES.put("DIAMOND_CHESTPLATE", ChatColor.AQUA + "Armadura de Diamante");
	}

	public String getEnchantName(final Enchantment enchant) {
		return this.getName(enchant.getName());
	}

	public String getItemName(ItemStack item) {
		if (item == null) {
			item = new ItemStack(0);
		}
		if (this.NAMES.containsKey(item.getType().name())) {
			return this.NAMES.get(item.getType().name());
		}
		return this.getName(item.getType().name());
	}

	public String getName(final String string) {
		if (this.NAMES.containsKey(string)) {
			return this.NAMES.get(string);
		}
		return this.toReadable(string);
	}

	public String toReadable(final String string) {
		final String[] names = string.split("_");
		for (int i = 0; i < names.length; ++i) {
			names[i] = String.valueOf(names[i].substring(0, 1)) + names[i].substring(1).toLowerCase();
		}
		return StringUtils.join(names, " ");
	}
}
