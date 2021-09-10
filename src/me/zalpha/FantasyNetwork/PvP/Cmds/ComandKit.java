package me.zalpha.FantasyNetwork.PvP.Cmds;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zalpha.FantasyNetwork.PvP.util.Kits;
import com.zalpha.FantasyNetwork.PvP.util.Manager;

public class ComandKit implements CommandExecutor {
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Manager.CONSOLE);
			return true;
		}
		boolean find = false;
		final Player p = (Player) sender;
		if (label.equalsIgnoreCase("kit")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Manager.CONSOLE);
				return true;
			}
			if (args.length == 0) {
				p.sendMessage(String.valueOf(Manager.use) + "kit <nome>.");
				return true;
			}
			if (args.length > 1) {
				p.sendMessage(String.valueOf(Manager.use) + "kit <nome>.");
				return true;
			}
			if (Manager.comKit(p)) {
				p.sendMessage(Manager.UMKITPORVIDA);
				return true;
			}
			int x = 0;
			while (x < Kits.values().length) {
				if (args[0].equalsIgnoreCase(Kits.values()[x].toString())) {
					if (!p.hasPermission("kit." + StringUtils.capitalise(Kits.values()[x].toString()))) {
						p.sendMessage(String.valueOf(Manager.prefix) + " §cVoc\u00ea n\u00e3o possui este kit.");
						return true;
					}
					if (Kits.values()[x].toString().equalsIgnoreCase("pvp")) {
						p.sendMessage(String.valueOf(Manager.PEGOUKIT) + "PvP" + "§a.");
						Manager.setarKit(p, "PvP");
					} else {
						p.sendMessage(String.valueOf(Manager.PEGOUKIT)
								+ StringUtils.capitalise(Kits.values()[x].toString().toLowerCase()) + "§a.");
						Manager.setarKit(p, StringUtils.capitalise(Kits.values()[x].toString().toLowerCase()));
					}
					Manager.darKit(p, Kits.values()[x]);
					p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
					find = true;
					break;
				} else {
					++x;
				}
			}
			if (!find) {
				p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas Este Kit N\u00e3o Existe.");
				return true;
			}
		}
		return false;
	}
}
