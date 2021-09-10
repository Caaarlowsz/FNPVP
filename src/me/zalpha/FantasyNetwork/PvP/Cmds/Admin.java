package me.zalpha.FantasyNetwork.PvP.Cmds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.zalpha.FantasyNetwork.PvP.FantasyKits;
import com.zalpha.FantasyNetwork.PvP.util.Manager;

public class Admin implements Listener, CommandExecutor {
	public static HashMap<String, ItemStack[]> SalvarInventario;
	public static HashMap<String, ItemStack[]> SalvarArmadura;
	public static HashMap<String, ItemStack[]> SalvarInventarioAutoSoup;
	public static HashMap<String, ItemStack[]> SalvarArmaduraAutoSoup;
	public static ArrayList<Player> Testando;
	public static ArrayList<Player> Bateu;
	public static ArrayList<Player> TrocaRapida;

	static {
		Admin.SalvarInventario = new HashMap<String, ItemStack[]>();
		Admin.SalvarArmadura = new HashMap<String, ItemStack[]>();
		Admin.SalvarInventarioAutoSoup = new HashMap<String, ItemStack[]>();
		Admin.SalvarArmaduraAutoSoup = new HashMap<String, ItemStack[]>();
		Admin.Testando = new ArrayList<Player>();
		Admin.Bateu = new ArrayList<Player>();
		Admin.TrocaRapida = new ArrayList<Player>();
	}

	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Manager.CONSOLE);
			return true;
		}
		final Player p = (Player) sender;
		if (label.equalsIgnoreCase("admin")) {
			if (p.hasPermission("fantasy.admin")) {
				if (Manager.Admin.contains(p)) {
					Manager.Admin.remove(p);
					p.getInventory().setArmorContents((ItemStack[]) null);
					p.setGameMode(GameMode.SURVIVAL);
					p.getInventory().setContents(Admin.SalvarInventario.get(p.getName()));
					p.getInventory().setArmorContents(Admin.SalvarArmadura.get(p.getName()));
					Admin.SalvarInventario.remove(p.getName());
					Admin.SalvarArmadura.remove(p.getName());
					p.sendMessage(String.valueOf(Manager.prefix) + " §cVoce Saiu Do Modo Admin.");
					Manager.mandarMensagemComPermissao("§7[§a" + p.getName() + " §7saiu do modo ADMIN]", "veravisos",
							false);
					for (final Player p2 : Bukkit.getOnlinePlayers()) {
						p2.showPlayer(p);
					}
				} else if (!Manager.Admin.contains(p)) {
					Manager.Admin.add(p);
					p.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Entrou No Modo Admin.");
					Manager.mandarMensagemComPermissao("§7[§a" + p.getName() + " §7entrou do modo ADMIN]", "veravisos",
							false);
					Admin.SalvarArmadura.put(p.getName(), p.getInventory().getArmorContents());
					Admin.SalvarInventario.put(p.getName(), p.getInventory().getContents());
					p.getInventory().setArmorContents((ItemStack[]) null);
					p.setGameMode(GameMode.CREATIVE);
					p.getInventory().clear();
					p.getInventory().setItem(1, Manager.criarItem2(Material.DIAMOND_SWORD, "§6\u25ba Players Em PvP",
							(short) 0, 1, new String[] { "§7Clique Para Usar." }));
					p.getInventory().setItem(2,
							Manager.criarItem2(Material.BONE, "§b\u25ba Teste De KnockBack (Player)", (short) 0, 1,
									new String[] { "§7Clique Para Usar." }));
					p.getInventory().setItem(3, Manager.criarItem2(Material.IRON_FENCE,
							"§b\u25ba Criar Uma Arena (Player)", (short) 0, 1, new String[] { "§7Clique Para Usar." }));
					p.getInventory().setItem(4,
							Manager.criarItem2(Material.INK_SACK, "§b\u25ba ACABA COM O PC Do Jogador (Player)",
									(short) 15, 1, new String[] { "§7Clique Para Usar." }));
					p.getInventory().setItem(5,
							Manager.criarItem2(Material.MUSHROOM_SOUP, "§b\u25ba Checar Auto-Soup (Player)", (short) 0,
									1, new String[] { "§7Clique Para Usar." }));
					p.getInventory().setItem(6,
							Manager.criarItem2(Material.PAPER, "§b\u25ba Teste Force-Field/Kill-Aura (Player)",
									(short) 0, 1, new String[] { "§7Clique Para Usar." }));
					p.getInventory().setItem(7, Manager.criarItem2(Material.MAGMA_CREAM, "§b\u25ba Troca R\u00e1pida",
							(short) 0, 1, new String[] { "§7Clique Para Usar." }));
					for (final Player p2 : Bukkit.getOnlinePlayers()) {
						if (!p2.hasPermission("fantasy.admin")) {
							p2.hidePlayer(p);
						} else {
							p2.showPlayer(p);
						}
					}
				}
			} else {
				p.sendMessage(Manager.SEMPERM);
			}
		}
		return false;
	}

	public void sumiradmins(final Player player) {
		for (final Player p : Manager.Admin) {
			if (!player.hasPermission("fantasy.admin")) {
				player.hidePlayer(p);
			}
		}
	}

	@EventHandler
	private void AdminJoin(final PlayerJoinEvent ev) {
		final Player p = ev.getPlayer();
		this.sumiradmins(p);
	}

	@EventHandler
	private void AdminItens(final PlayerInteractEntityEvent e) {
		final Player jogador = e.getPlayer();
		if (e.getRightClicked() instanceof Player) {
			if (!Manager.Admin.contains(jogador)) {
				return;
			}
			final Player clicado = (Player) e.getRightClicked();
			if (jogador.getItemInHand().getType() == Material.BONE) {
				if (Admin.Testando.contains(clicado)) {
					jogador.sendMessage(
							String.valueOf(Manager.prefix) + " §cDesculpe, Mas Ja Tem Alguem Testando Este Jogador.");
					return;
				}
				final double localTestado = clicado.getLocation().getY();
				Admin.Testando.add(clicado);
				clicado.setVelocity(new Vector(0, 3, 0));
				Bukkit.getScheduler().scheduleSyncDelayedTask(FantasyKits.getPlugin(), new Runnable() {
					@Override
					public void run() {
						if (clicado.isOnline()) {
							clicado.setVelocity(new Vector(0, -1, 0));
						}
					}
				}, 5L);
				Bukkit.getScheduler().scheduleSyncDelayedTask(FantasyKits.getPlugin(), new Runnable() {
					@Override
					public void run() {
						if (clicado.isOnline()) {
							Admin.Testando.remove(clicado);
							if (clicado.getLocation().getY() == localTestado) {
								jogador.sendMessage(String.valueOf(Manager.prefix)
										+ " §aEste Jogador §fProvavelmente §aPode Estar De Anti-KnockBack.");
							} else {
								jogador.sendMessage(String.valueOf(Manager.prefix)
										+ " §cEste Jogador §cN\u00e3o Esta De Anti-KnockBack.");
							}
						} else {
							jogador.sendMessage(String.valueOf(Manager.prefix)
									+ " §aEste Jogador Deslogou Enquanto O Teste Estava Sendo Feito.");
							Admin.Testando.remove(clicado);
						}
					}
				}, 8L);
			}
			if (jogador.getItemInHand().getType() == Material.IRON_FENCE) {
				clicado.getLocation().add(0.0, 13.0, 0.0).getBlock().setType(Material.BEDROCK);
				clicado.getLocation().add(0.0, 11.0, 1.0).getBlock().setType(Material.BEDROCK);
				clicado.getLocation().add(1.0, 11.0, 0.0).getBlock().setType(Material.BEDROCK);
				clicado.getLocation().add(0.0, 11.0, -1.0).getBlock().setType(Material.BEDROCK);
				clicado.getLocation().add(-1.0, 11.0, 0.0).getBlock().setType(Material.BEDROCK);
				clicado.getLocation().add(0.0, 10.0, 0.0).getBlock().setType(Material.BEDROCK);
				clicado.teleport(clicado.getLocation().add(0.0, 11.0, -0.05));
				jogador.sendMessage(String.valueOf(Manager.prefix) + " §aVoc\u00ea Criou Uma Arena Para §f"
						+ clicado.getName() + "§a.");
				Manager.mandarMensagemComPermissao(
						"§7[§a" + jogador.getName() + " §7criou uma arena para " + clicado.getName() + "§7]",
						"veravisos", false);
			}
			if (jogador.getItemInHand().getType() == Material.INK_SACK) {
				clicado.openInventory(Bukkit.createInventory((InventoryHolder) null, 8100));
				jogador.sendMessage(
						String.valueOf(Manager.prefix) + " §aVoc\u00ea Crashou §f" + clicado.getName() + "§a.");
			}
			if (jogador.getItemInHand().getType().equals(Material.AIR)) {
				jogador.sendMessage(String.valueOf(Manager.prefix) + " §aVoce Esta Vendo O Inventario De §f"
						+ clicado.getName() + "§a.");
				Manager.mandarMensagemComPermissao(
						"§7[§a" + jogador.getName() + " §7esta vendo o inventario de " + clicado.getName() + "§7]",
						"veravisos", false);
				jogador.openInventory(clicado.getInventory());
			}
			if (jogador.getItemInHand().getType() == Material.MUSHROOM_SOUP) {
				if (Admin.Testando.contains(clicado)) {
					jogador.sendMessage(
							String.valueOf(Manager.prefix) + " §cDesculpe, Mas Ja Tem Alguem Testando Este Jogador.");
					return;
				}
				Admin.SalvarInventarioAutoSoup.put(clicado.getName(), clicado.getInventory().getContents());
				Admin.SalvarArmaduraAutoSoup.put(clicado.getName(), clicado.getInventory().getArmorContents());
				Admin.Testando.add(clicado);
				clicado.setHealth(10.0);
				clicado.getInventory().clear();
				clicado.getInventory().setArmorContents((ItemStack[]) null);
				final ItemStack Sopa = new ItemStack(Material.MUSHROOM_SOUP);
				clicado.getInventory().setItem(10, Sopa);
				Bukkit.getScheduler().scheduleSyncDelayedTask(FantasyKits.getPlugin(), new Runnable() {
					@Override
					public void run() {
						if (clicado.isOnline()) {
							Admin.Testando.remove(clicado);
							if (clicado.getInventory().contains(Material.BOWL)) {
								clicado.getInventory().setContents(
										Admin.SalvarInventarioAutoSoup.get(clicado.getName()));
								clicado.getInventory().setArmorContents(
										Admin.SalvarArmaduraAutoSoup.get(clicado.getName()));
								clicado.updateInventory();
								jogador.sendMessage(String.valueOf(Manager.prefix)
										+ " §aEste Jogador §fDefinitivamente §aEsta De Auto-Soup.");
								clicado.setHealth(20.0);
							} else {
								clicado.getInventory().setContents(
										Admin.SalvarInventarioAutoSoup.get(clicado.getName()));
								clicado.getInventory().setArmorContents(
										Admin.SalvarArmaduraAutoSoup.get(clicado.getName()));
								clicado.updateInventory();
								jogador.sendMessage(String.valueOf(Manager.prefix)
										+ " §cEste Jogador §fProvavelmente §cN\u00e3o Pode Estar De Auto-Soup.");
								clicado.setHealth(20.0);
							}
						} else {
							jogador.sendMessage(String.valueOf(Manager.prefix)
									+ " §aEste Jogador Deslogou Enquanto O Teste Estava Sendo Feito.");
							Admin.Testando.remove(clicado);
						}
					}
				}, 10L);
			}
			if (jogador.getItemInHand().getType() == Material.PAPER && jogador.getItemInHand().getItemMeta()
					.getDisplayName().equalsIgnoreCase("§b\u25ba Teste Force-Field/Kill-Aura (Player)")) {
				if (Admin.Testando.contains(clicado)) {
					jogador.sendMessage(
							String.valueOf(Manager.prefix) + " §cDesculpe, Mas Ja Tem Alguem Testando Este Jogador.");
					return;
				}
				final int LocalX = clicado.getLocation().getBlockX();
				final int LocalY = clicado.getLocation().getBlockY() + 1;
				final int LocalZ = clicado.getLocation().getBlockZ();
				Admin.Testando.add(clicado);
				final Location Local = new Location(clicado.getWorld(), LocalX, LocalY,
						LocalZ);
				final Bat Morcego = (Bat) clicado.getWorld().spawnEntity(Local, EntityType.BAT);
				Morcego.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 99999999));
				Bukkit.getScheduler().scheduleSyncDelayedTask(FantasyKits.getPlugin(), new Runnable() {
					@Override
					public void run() {
						Morcego.remove();
						Admin.Testando.remove(clicado);
					}
				}, 11L);
				Bukkit.getScheduler().scheduleSyncDelayedTask(FantasyKits.getPlugin(), new Runnable() {
					@Override
					public void run() {
						if (clicado.isOnline()) {
							if (Admin.Bateu.contains(clicado)) {
								Admin.Bateu.remove(clicado);
								Admin.Testando.remove(clicado);
								jogador.sendMessage(String.valueOf(Manager.prefix)
										+ " §aEste Jogador §fDefinitivamente §aEsta De Forcefield.");
							} else {
								jogador.sendMessage(String.valueOf(Manager.prefix)
										+ " §cEste Jogador §fProvavelmente §cN\u00e3o Pode Estar De Forcefield.");
								Admin.Bateu.remove(clicado);
							}
						} else {
							jogador.sendMessage(String.valueOf(Manager.prefix)
									+ " §aEste Jogador Deslogou Enquanto O Teste Estava Sendo Feito.");
							Admin.Testando.remove(clicado);
						}
					}
				}, 12L);
			}
		}
	}

	@EventHandler
	private void semPegarItemNoTeste(final PlayerPickupItemEvent evento) {
		final Player jogador = evento.getPlayer();
		if (Admin.Testando.contains(jogador)) {
			evento.setCancelled(true);
			jogador.updateInventory();
		}
		if (Manager.Admin.contains(jogador)) {
			evento.setCancelled(true);
			jogador.updateInventory();
		}
	}

	@EventHandler
	private void naodroparItensAdmin(final PlayerDropItemEvent e) {
		final Player jogador = e.getPlayer();
		if (Manager.Admin.contains(jogador)) {
			if (e.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			}
			if (e.getItemDrop().getItemStack().getType() == Material.BONE) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			}
			if (e.getItemDrop().getItemStack().getType() == Material.IRON_FENCE) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			}
			if (e.getItemDrop().getItemStack().getType() == Material.INK_SACK) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			}
			if (e.getItemDrop().getItemStack().getType() == Material.MUSHROOM_SOUP) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			}
			if (e.getItemDrop().getItemStack().getType() == Material.SKULL_ITEM) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			}
			if (e.getItemDrop().getItemStack().getType() == Material.MAGMA_CREAM) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			}
		}
	}

	@EventHandler
	private void semDanoTesteAuto(final EntityDamageEvent evento) {
		if (evento.getEntity() instanceof Player) {
			final Player jogador = (Player) evento.getEntity();
			if (Admin.Testando.contains(jogador)) {
				evento.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void modoRapido(final PlayerInteractEvent evento) {
		final Player jogador = evento.getPlayer();
		if (Manager.Admin.contains(jogador)) {
			if (jogador.getItemInHand().getType().equals(Material.MAGMA_CREAM)
					&& !Admin.TrocaRapida.contains(jogador)) {
				Admin.TrocaRapida.add(jogador);
				jogador.setGameMode(GameMode.SURVIVAL);
				for (final Player jogadores : Bukkit.getOnlinePlayers()) {
					jogadores.showPlayer(jogador);
				}
				Bukkit.getScheduler().scheduleSyncDelayedTask(FantasyKits.getPlugin(), new Runnable() {
					@Override
					public void run() {
						jogador.sendMessage(String.valueOf(Manager.prefix) + " §aTroca Rapida Efeuada Com Sucesso.");
						Admin.TrocaRapida.remove(jogador);
						jogador.setGameMode(GameMode.CREATIVE);
						for (final Player p2 : Bukkit.getOnlinePlayers()) {
							if (!p2.hasPermission("fantasy.admin")) {
								p2.hidePlayer(jogador);
							} else {
								p2.showPlayer(jogador);
							}
						}
					}
				}, 15L);
			}
			if (jogador.getItemInHand().getType().equals(Material.DIAMOND_SWORD)) {
				if (Manager.Combate.size() >= 1) {
					final int random = new Random().nextInt(Manager.Combate.size());
					if (Manager.Combate.get(random).isOnline()) {
						jogador.teleport(Manager.Combate.get(random));
					}
				} else {
					jogador.sendMessage(
							String.valueOf(Manager.prefix) + " §cDesculpe, Mas N\u00e3o Existe Um Player Em Combate.");
				}
			}
		}
	}

	@EventHandler
	private void verificarForcefield(final EntityDamageByEntityEvent evento) {
		if (evento.getDamager() instanceof Player && evento.getEntity() instanceof Bat) {
			final Player jogador = (Player) evento.getDamager();
			if (Admin.Testando.contains(jogador)) {
				evento.setCancelled(false);
				Admin.Bateu.add(jogador);
			}
		}
	}
}
