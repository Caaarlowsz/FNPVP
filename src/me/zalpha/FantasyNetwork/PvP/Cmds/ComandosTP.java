package me.zalpha.FantasyNetwork.PvP.Cmds;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.zalpha.FantasyNetwork.PvP.util.Manager;

public class ComandosTP implements CommandExecutor {
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Manager.CONSOLE);
			return true;
		}
		final Player p = (Player) sender;
		if (label.equalsIgnoreCase("tp")) {
			if (!p.hasPermission("fantasy.tp")) {
				p.sendMessage(Manager.SEMPERM);
				return true;
			}
			if (args.length == 0) {
				p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tp <player> ou Use: /tp <x> <y> <z>");
				return true;
			}
			if (args.length > 3) {
				p.sendMessage(String.valueOf(Manager.prefix) + " §cUse: /tp <player> ou Use: /tp <x> <y> <z>");
				return true;
			}
			if (args.length == 1) {
				final Player jogadorencontrado = Bukkit.getPlayer(args[0]);
				if (jogadorencontrado != null) {
					if (jogadorencontrado != p) {
						p.teleport(jogadorencontrado.getLocation());
						p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Teleportou-se Para §f"
								+ jogadorencontrado.getName() + "§a.");
						Manager.mandarMensagemComPermissao(
								"§7[§a" + p.getName() + " §7teleportou para " + jogadorencontrado.getName() + "]",
								"veravisos", false);
					} else {
						p.sendMessage(String.valueOf(Manager.prefix)
								+ " §cDesculpe, Mas Voce N\u00e3o Pode Teleporta-se Para Voce Mesmo.");
					}
				} else {
					p.sendMessage(Manager.JOGADORFF);
				}
				return true;
			}
			if (args.length == 2) {
				final Player jogadorencontrado = Bukkit.getPlayer(args[0]);
				final Player jogadorencontrado2 = Bukkit.getPlayer(args[1]);
				if (jogadorencontrado != null && jogadorencontrado2 != null) {
					if (jogadorencontrado != jogadorencontrado2) {
						jogadorencontrado.teleport(jogadorencontrado2.getLocation());
						p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Teleportou §f"
								+ jogadorencontrado.getName() + " §aPara §f" + jogadorencontrado2.getName() + "§a.");
						Manager.mandarMensagemComPermissao("§7[§a" + jogadorencontrado.getName() + " §7teleportou para "
								+ jogadorencontrado2.getName() + "]", "veravisos", false);
					} else {
						p.sendMessage(String.valueOf(Manager.prefix)
								+ " §cDesculpe, Mas Voce N\u00e3o Pode Teleporta-se Para Voce Mesmo.");
					}
				} else {
					p.sendMessage(Manager.JOGADORFF);
				}
				return true;
			}
			if (args.length == 3) {
				final DecimalFormat df = new DecimalFormat("##.#");
				if (Manager.argumentoNumero(args[0].replace("~", ""))
						&& Manager.argumentoNumero(args[1].replace("~", ""))
						&& Manager.argumentoNumero(args[2].replace("~", ""))) {
					double x;
					double y;
					double z;
					if (args[0].startsWith("~") && args[1].startsWith("~") && args[2].startsWith("~")) {
						x = p.getLocation().getX() + Double.valueOf(args[0].replace("~", ""));
						y = p.getLocation().getY() + Double.valueOf(args[1].replace("~", ""));
						z = p.getLocation().getZ() + Double.valueOf(args[2].replace("~", ""));
					} else {
						x = Double.valueOf(args[0]);
						y = Double.valueOf(args[1]);
						z = Double.valueOf(args[2]);
					}
					p.teleport(new Location(p.getWorld(), x, y, z));
					p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Teleportou Para §a[§fX: §a"
							+ df.format(x).replace(".", ",") + " §fY: §a" + df.format(y).replace(".", ",") + " §fZ: §a"
							+ df.format(z).replace(".", ",") + "§a]§a.");
					Manager.mandarMensagemComPermissao(
							"§7[§a" + p.getName() + " §7teleportou para §f" + df.format(x).replace(".", ",") + ","
									+ df.format(y).replace(".", ",") + "," + df.format(z).replace(".", ",") + "§7]",
							"veravisos", false);
				} else {
					p.sendMessage(String.valueOf(Manager.prefix) + " §cDesculpe, Mas Isto N\u00e3o E Uma Numero.");
				}
			}
		}
		if (label.equalsIgnoreCase("tpall")) {
			if (!p.hasPermission("fantasy.tpall")) {
				p.sendMessage(Manager.SEMPERM);
				return true;
			}
			int size = 0;
			for (final Player jogadores : Bukkit.getOnlinePlayers()) {
				if (jogadores != p && !Manager.pegarKit(jogadores).equalsIgnoreCase("1v1")
						&& !Manager.pegarKit(jogadores).equalsIgnoreCase("None")) {
					++size;
					jogadores.teleport(p.getLocation());
				}
			}
			if (size > 0) {
				p.sendMessage(String.valueOf(Manager.prefix) + " §7Voc\u00ea puxou §a" + size + " "
						+ ((size == 1) ? "jogador" : "jogadores") + " §7para sua localiza\u00e7\u00e3o.");
				Manager.mandarMensagem(String.valueOf(Manager.prefix) + " §f" + p.getName()
						+ " §7puxou todos os jogadores para sua localiza\u00e7\u00e3o.");
			} else {
				p.sendMessage(
						String.valueOf(Manager.prefix) + " §7N\u00e3o existe nenhum jogador para voc\u00ea puxar.");
			}
		}
		return false;
	}
}
