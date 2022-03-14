package com.zalpha.FantasyNetwork.PvP.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.caaarlowsz.fantasymc.kitpvp.FantasyPvP;

import me.zalpha.FantasyNetwork.PvP.Duelx1.Duelx1;

public class Vanish {
	public static void updateVanished(final Player player) {
		for (final Player p : Bukkit.getOnlinePlayers()) {
			if (!player.getName().equals(p.getName())) {
				if (!player.hasPermission("fantasy.staff")) {
					if (!player.canSee(p)) {
						continue;
					}
					player.hidePlayer(p);
				} else {
					if (Duelx1.isIn1v1(player) || player.canSee(p)
							|| FantasyPvP.getMain().getPlayerHideManager().isHiding(player.getUniqueId())
							|| FantasyPvP.getMain().getPlayerHideManager().hideForAll(player.getUniqueId())) {
						continue;
					}
					player.showPlayer(p);
				}
			}
		}
	}
}
