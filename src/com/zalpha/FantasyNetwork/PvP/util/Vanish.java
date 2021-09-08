package com.zalpha.FantasyNetwork.PvP.util;

import java.util.Iterator;
import com.zalpha.FantasyNetwork.PvP.FantasyKits;
import me.zalpha.FantasyNetwork.PvP.Duelx1.Duelx1;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Vanish
{
    public static void updateVanished(final Player player) {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            if (!player.getName().equals(p.getName())) {
                if (!player.hasPermission("fantasy.staff")) {
                    if (!player.canSee(p)) {
                        continue;
                    }
                    player.hidePlayer(p);
                }
                else {
                    if (Duelx1.isIn1v1(player) || player.canSee(p) || FantasyKits.getMain().getPlayerHideManager().isHiding(player.getUniqueId()) || FantasyKits.getMain().getPlayerHideManager().hideForAll(player.getUniqueId())) {
                        continue;
                    }
                    player.showPlayer(p);
                }
            }
        }
    }
}
