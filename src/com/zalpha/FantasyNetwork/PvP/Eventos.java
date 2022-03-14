package com.zalpha.FantasyNetwork.PvP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.caaarlowsz.fantasymc.kitpvp.FantasyPvP;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.zalpha.FantasyNetwork.PvP.util.ConfigAPI;
import com.zalpha.FantasyNetwork.PvP.util.Holograms_Fixed;
import com.zalpha.FantasyNetwork.PvP.util.InventarioKits;
import com.zalpha.FantasyNetwork.PvP.util.Inventarios;
import com.zalpha.FantasyNetwork.PvP.util.Manager;
import com.zalpha.FantasyNetwork.PvP.util.MenuPaged;
import com.zalpha.FantasyNetwork.PvP.util.TopKills;
import com.zalpha.FantasyNetwork.PvP.util.player.PlayerData;

import me.zalpha.FantasyNetwork.PvP.CustomEvent.PlayerDeathInWarpEvent;
import me.zalpha.FantasyNetwork.PvP.Duelx1.Duelx1;
import net.minecraft.server.v1_8_R3.EntityPlayer;

public class Eventos implements Listener {
	Map<String, Long> cooldownchat;
	Map<String, Long> cooldowncomando;
	public static ArrayList<String> jump;
	ArrayList<String> nofalldamagec;
	ArrayList<String> nofalldamage;

	static {
		Eventos.jump = new ArrayList<String>();
	}

	public Eventos() {
		this.cooldownchat = new HashMap<String, Long>();
		this.cooldowncomando = new HashMap<String, Long>();
		this.nofalldamagec = new ArrayList<String>();
		this.nofalldamage = new ArrayList<String>();
	}

	@EventHandler
	public void staffchat(final AsyncPlayerChatEvent e) {
		final Player p = e.getPlayer();
		for (final Player online : Bukkit.getOnlinePlayers()) {
			if (Manager.comStaffAtivado(p)) {
				e.setCancelled(true);
				if (!online.hasPermission("fantasy.veravisos") && !Manager.comStaffAtivado(online)) {
					continue;
				}
				online.sendMessage("�7\u276a �c�lSTAFFCHAT �7| " + p.getDisplayName() + " �7\u276b "
						+ e.getMessage().replace("&", "�"));
			}
		}
	}

	@EventHandler
	public static void armorstand(final PlayerArmorStandManipulateEvent e) {
		if (e.getRightClicked().getCustomName().contains("�")) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public static void armorstand(final EntityDamageEvent e) {
		if (e.getEntity() instanceof ArmorStand && e.getEntity().getCustomName().contains("�")) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerMove(final PlayerMoveEvent e) {
		if (!e.getPlayer().hasPermission("fantasy.afk")) {
			final float yawDif = Math.abs(e.getFrom().getYaw() - e.getTo().getYaw());
			final float pitchDif = Math.abs(e.getFrom().getPitch() - e.getTo().getPitch());
			if (yawDif != 0.0f || pitchDif != 0.0f) {
				FantasyPvP.getMain().times.remove(e.getPlayer());
				FantasyPvP.getMain().times.put(e.getPlayer(), System.currentTimeMillis());
			}
		}
	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {
		if (!e.getPlayer().hasPermission("fantasy.afk")) {
			FantasyPvP.getMain().times.put(e.getPlayer(), System.currentTimeMillis());
		}
	}

	@EventHandler
	public static void spawnmobs(final CreatureSpawnEvent e) {
		if (e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM) {
			e.setCancelled(true);
		} else if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
			e.setCancelled(false);
		}
	}

	@EventHandler
	public void onTeleport(final PlayerTeleportEvent event) {
		if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)
				&& !Manager.comKit(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	private void nearby(final EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (!(e.getDamager() instanceof Player)) {
			return;
		}
		final Player p = (Player) e.getDamager();
		final Player t = (Player) e.getEntity();
		if (Manager.comKit(t) && !Manager.pegarKit(t).equalsIgnoreCase("1v1")) {
			Manager.sendActionBar(p, "�b" + t.getName() + " - " + Manager.pegarKit(t));
		}
	}

	@EventHandler
	private void entrar(final PlayerJoinEvent e) {
		final PlayerData pd = PlayerData.create(e.getPlayer());
		final Player p = pd.getPlayer();
		for (final Holograms_Fixed h : TopKills.holograms) {
			h.showPlayer(p);
		}
		for (final String names : ConfigAPI.pegarConfig().pegarconfigMensagens().getStringList("BlackList")) {
			final Player jogador2 = Bukkit.getPlayer(names);
			if (jogador2 == p) {
				p.kickPlayer(String.valueOf(Manager.prefix)
						+ "\n   \n       �cOcorreu um erro ao entrar no servidor\n   �cPossivel motivo: �fVoc\u00ea encontra-se na lista-negra do servidor.\n    \n�cPara mais informa\u00e7\u00f5es:\n \n�cSite: �f"
						+ ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Site") + "\n �cTwitter: �f"
						+ ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Twitter")
						+ "\n �cTeamSpeak: �fts.fantasynetwork.com.br");
			}
		}
		Manager.mandarTituloCima(p, "�5�lFANTASY �7- �e�lKITPVP");
		Manager.mandarTituloBaixo(p, "�7Escolha um kit");
		new BukkitRunnable() {
			public void run() {
				if (p.isOnline()) {
					Manager.mandarTituloCima(p, "�5�lFANTASY �7- �e�lKITPVP");
					Manager.mandarTituloBaixo(p, "�7Busque um oponente");
				}
			}
		}.runTaskLater(FantasyPvP.getPlugin(), 40L);
		new BukkitRunnable() {
			public void run() {
				if (p.isOnline()) {
					Manager.mandarTituloCima(p, "�5�lFANTASY �7- �e�lKITPVP");
					Manager.mandarTituloBaixo(p, "�7Lute e mate-o");
				}
			}
		}.runTaskLater(FantasyPvP.getPlugin(), 60L);
		new BukkitRunnable() {
			public void run() {
				if (p.isOnline()) {
					Manager.mandarTituloCima(p, "�5�lFANTASY �7- �e�lKITPVP");
					Manager.mandarTituloBaixo(p, "�7E seja o melhor");
				}
			}
		}.runTaskLater(FantasyPvP.getPlugin(), 80L);
		new BukkitRunnable() {
			public void run() {
				if (p.isOnline()) {
					Manager.mandarTituloCima(p, "�5�lFANTASY �7- �e�lKITPVP");
					Manager.mandarTituloBaixo(p, "�7E seja o melhor");
				}
			}
		}.runTaskLater(FantasyPvP.getPlugin(), 100L);
		p.getInventory().clear();
		e.setJoinMessage((String) null);
		p.teleport(p.getWorld().getSpawnLocation());
		p.setFoodLevel(20);
		new BukkitRunnable() {
			public void run() {
				if (p.isOnline()) {
					p.setLevel(-100);
				}
			}
		}.runTaskLater(FantasyPvP.getPlugin(), 20L);
		if (p.hasPermission("fantasy.fly")) {
			p.setAllowFlight(true);
			p.sendMessage(String.valueOf(Manager.prefix) + " �7Fly Ativado.");
		}
		for (final String msg : ConfigAPI.pegarConfig().pegarconfigMensagens().getStringList("Mensagem_Entrar")) {
			p.sendMessage(msg
					.replace("<online>", new StringBuilder(String.valueOf(Bukkit.getOnlinePlayers().size())).toString())
					.replace("<jogador>", p.getName()).replace("&", "�"));
		}
		Manager.darItens(p);
		if (!Manager.Tell.containsKey(p.getName())) {
			Manager.Tell.put(p.getName(), true);
		}
		if (!Manager.Report.containsKey(p.getName())) {
			Manager.Report.put(p.getName(), true);
		}
		if (!Manager.comTellAtivado(p)) {
			p.sendMessage(String.valueOf(Manager.prefix)
					+ " �cAten\u00e7ao!, Voce N\u00e3o Esta Recebendo Mensagens De Tell, Caso Queira Ativar Use /toggle.");
		}
		if (!Manager.comReportAtivado(p) && p.hasPermission("fantasy.veravisos")) {
			p.sendMessage(String.valueOf(Manager.prefix)
					+ " �cAten\u00e7ao!, Voce N\u00e3o Esta Recebendo Reports, Caso Queira Ativar Use /toggle.");
		}
		String finaltag = Manager.pegarTagDisplay(p);
		if (finaltag.length() > 16) {
			finaltag = finaltag.substring(0, 16);
		}
	}

	@EventHandler
	private void login(final PlayerLoginEvent e) {
		final Player jogador = e.getPlayer();
		if (e.getResult() == PlayerLoginEvent.Result.ALLOWED && !jogador.hasPermission("fantasy.full")) {
			jogador.kickPlayer(String.valueOf(Manager.prefix)
					+ "\n   \n       �cOcorreu um erro ao entrar no servidor\n   �cpossivel motivo: �fO Servidor encontra-se cheio!.\n    \n�cPara mais informa\u00e7\u00f5es:\n \n�cSite: �f"
					+ ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Site") + "\n �cTwitter: �f"
					+ ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Twitter")
					+ "\n �cTeamSpeak: �fts.fantasynetwork.com.br");
		}
		if (e.getResult() == PlayerLoginEvent.Result.KICK_WHITELIST) {
			jogador.kickPlayer(String.valueOf(Manager.prefix)
					+ "\n   \n       �cOcorreu um erro ao entrar no servidor\n   �cpossivel motivo: �fO servidor encontra-se em manuten\u00e7\u00e3o.\n    \n�cPara mais informa\u00e7\u00f5es:\n \n�cSite: �f"
					+ ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Site") + "\n �cTwitter: �f"
					+ ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Twitter")
					+ "\n �cTeamSpeak: �ffantasynet.dnahost.com");
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPreProcessCommand(final PlayerCommandPreprocessEvent event) {
		if (event.getMessage().toLowerCase().startsWith("/me ")) {
			event.getPlayer().sendMessage(String.valueOf(Manager.prefix)
					+ " �cDesculpe, Mas Este Comando N\u00e3o Foi Encontrado No Banco De Dados Do Servidor.");
			event.setCancelled(true);
		}
		if (event.getMessage().toLowerCase().equalsIgnoreCase("/me")) {
			event.getPlayer().sendMessage(String.valueOf(Manager.prefix)
					+ " �cDesculpe, Mas Este Comando N\u00e3o Foi Encontrado No Banco De Dados Do Servidor.");
			event.setCancelled(true);
		}
		if (event.getMessage().toLowerCase().startsWith("/ver ")) {
			event.getPlayer().sendMessage(String.valueOf(Manager.prefix)
					+ " �cDesculpe, Mas Este Comando N\u00e3o Foi Encontrado No Banco De Dados Do Servidor.");
			event.setCancelled(true);
		}
		if (event.getMessage().toLowerCase().equalsIgnoreCase("/ver")) {
			event.getPlayer().sendMessage(String.valueOf(Manager.prefix)
					+ " �cDesculpe, Mas Este Comando N\u00e3o Foi Encontrado No Banco De Dados Do Servidor.");
			event.setCancelled(true);
		}
		if (event.getMessage().toLowerCase().startsWith("/help")) {
			event.getPlayer().sendMessage(String.valueOf(Manager.prefix)
					+ " �cDesculpe, Mas Este Comando N\u00e3o Foi Encontrado No Banco De Dados Do Servidor.");
			event.setCancelled(true);
		}
		if (event.getMessage().toLowerCase().startsWith("/help ")) {
			event.getPlayer().sendMessage(String.valueOf(Manager.prefix)
					+ " �cDesculpe, Mas Este Comando N\u00e3o Foi Encontrado No Banco De Dados Do Servidor.");
			event.setCancelled(true);
		}
		if (event.getMessage().toLowerCase().startsWith("/plugins")
				&& !event.getMessage().toLowerCase().startsWith("/pl")
				&& !event.getMessage().toLowerCase().startsWith("/plot")) {
			event.getPlayer().sendMessage("");
			event.getPlayer().sendMessage("�e�l\u279c �7Plugin FantasyNetwork �f- �aKitPvP");
			event.getPlayer().sendMessage(
					"�e�l\u279c �7Site �f" + ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Site"));
			event.getPlayer().sendMessage("");
			event.setCancelled(true);
		}
		if (event.getMessage().toLowerCase().startsWith("/pl")
				&& !event.getMessage().toLowerCase().startsWith("/plot")) {
			event.getPlayer().sendMessage("");
			event.getPlayer().sendMessage("�e�l\u279c �7Plugin FantasyNetwork �f- �aKitPvP");
			event.getPlayer().sendMessage(
					"�e�l\u279c �7Site �f" + ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Site"));
			event.getPlayer().sendMessage("");
			event.setCancelled(true);
		}
		if (event.getMessage().toLowerCase().startsWith("/server ")) {
			event.getPlayer().sendMessage(String.valueOf(Manager.prefix)
					+ " �cDesculpe, Mas Este Comando N\u00e3o Foi Encontrado No Banco De Dados Do Servidor.");
			event.setCancelled(true);
		}
		if (event.getMessage().split(" ")[0].contains(":")) {
			event.getPlayer().sendMessage(String.valueOf(Manager.prefix)
					+ " �cDesculpe, Mas Este Comando N\u00e3o Foi Encontrado No Banco De Dados Do Servidor.");
			event.setCancelled(true);
		}
	}

	@EventHandler
	private void semfogo(final BlockSpreadEvent e) {
		if (e.getNewState().getType() == Material.FIRE) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	private void respawar(final PlayerRespawnEvent e) {
		final Player p = e.getPlayer();
		Manager.darItens(p);
		p.teleport(p.getWorld().getSpawnLocation());
		if (Duelx1.isIn1v1(p)) {
			new BukkitRunnable() {
				public void run() {
					Duelx1.playersIn1v1.remove(p);
					Duelx1.teleport1v1(p);
				}
			}.runTaskLater(FantasyPvP.getPlugin(), 2L);
		}
	}

	@EventHandler
	private void naoquebraritens(final EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			final Player p2 = (Player) e.getDamager();
			if (p2.getItemInHand().getType().name().contains("SWORD")
					|| p2.getItemInHand().getType().name().contains("AXE")
					|| p2.getItemInHand().getType().name().contains("PICKAXE")
					|| p2.getItemInHand().getType().name().contains("AXE")) {
				p2.getItemInHand().setDurability((short) 0);
				p2.updateInventory();
			}
			if (p2.getItemInHand().getType() == Material.BOW) {
				p2.getItemInHand().setDurability((short) 0);
				p2.updateInventory();
			}
		}
	}

	@EventHandler
	private void naoquebraritens2(final EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			final Player p = (Player) e.getEntity();
			if (p.getInventory().getChestplate() != null) {
				p.getInventory().getChestplate().setDurability((short) 0);
			}
			if (p.getInventory().getHelmet() != null) {
				p.getInventory().getHelmet().setDurability((short) 0);
			}
			if (p.getInventory().getLeggings() != null) {
				p.getInventory().getLeggings().setDurability((short) 0);
			}
			if (p.getInventory().getBoots() != null) {
				p.getInventory().getBoots().setDurability((short) 0);
			}
		}
	}

	@EventHandler
	private void sopa(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		final EntityPlayer craftp = ((CraftPlayer) p).getHandle();
		if (p.getItemInHand().getType() == Material.MUSHROOM_SOUP && e.getAction().name().contains("RIGHT")
				&& craftp.getHealth() < craftp.getMaxHealth()) {
			craftp.setHealth(craftp.getHealth() + 7.0f);
			e.getItem().setType(Material.BOWL);
			final ItemStack pote = new ItemStack(Material.BOWL);
			final ItemMeta potem = pote.getItemMeta();
			potem.setDisplayName("�c\u25ba Pote");
			pote.setItemMeta(potem);
			p.setItemInHand(pote);
		}
	}

	@EventHandler
	private void semdanonospawn(final EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			final Player p = (Player) e.getEntity();
			if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
				e.setCancelled(true);
				p.teleport(p.getWorld().getSpawnLocation());
			}
			if (!Manager.comKit(p)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void chat(final AsyncPlayerChatEvent e) {
		final Player p = e.getPlayer();
		final Long cooldown = System.currentTimeMillis() / 1000L;
		final Long tempoDeEspera = this.cooldowncomando.get(p.getName());
		if (!p.hasPermission("fantasy.vip") && !Manager.Chat) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(String.valueOf(Manager.prefix) + " �cDesculpe, mas o chat esta desabilitado.");
			return;
		}
		if (this.cooldowncomando.containsKey(p.getName())) {
			if (cooldown - tempoDeEspera < 3L && !p.hasPermission("fantasy.cchat")) {
				p.sendMessage(String.valueOf(Manager.prefix) + " �cAguarde Para Escrever Novamente.");
				e.setCancelled(true);
				return;
			}
			this.cooldowncomando.remove(p.getName());
		}
		this.cooldowncomando.put(p.getName(), cooldown);
		if (e.getMessage().contains("%")) {
			e.setMessage("%2$s".replace("%2$s", " porcento"));
		}
		if (p.hasPermission("fantasy.chatcolor")) {
			if (!Manager.comStaffAtivado(p)) {
				for (final Player player : e.getRecipients()) {
					Manager.sendMessageChat(p, player, "�f" + e.getMessage().replace("&", "�"));
					e.setCancelled(true);
				}
			}
		} else if (!Manager.comStaffAtivado(p)) {
			for (final Player player : e.getRecipients()) {
				Manager.sendMessageChat(p, player, e.getMessage());
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void comandoerrado(final PlayerCommandPreprocessEvent e) {
		final Player p = e.getPlayer();
		final Long cooldown = System.currentTimeMillis() / 1000L;
		final Long tempoDeEspera = this.cooldowncomando.get(p.getName());
		if (this.cooldowncomando.containsKey(p.getName())) {
			if (cooldown - tempoDeEspera < 3L && !p.hasPermission("fantasy.ccomando")) {
				p.sendMessage(String.valueOf(Manager.prefix) + " �cAguarde para ultilizar outro comando.");
				e.setCancelled(true);
				return;
			}
			this.cooldowncomando.remove(p.getName());
		}
		this.cooldowncomando.put(p.getName(), cooldown);
		if (!e.isCancelled()) {
			final String cmd = e.getMessage().split(" ")[0];
			final HelpTopic help = FantasyPvP.getMain().getServer().getHelpMap().getHelpTopic(cmd);
			if (help == null) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(String.valueOf(Manager.prefix)
						+ " �cDesculpe, mas este comando n\u00e3o foi encontrado no banco de dados do servidor.");
			}
		}
	}

	@EventHandler
	private void naodrop(final PlayerDropItemEvent e) {
		if (e.getItemDrop().getItemStack().getType() == Material.EMERALD
				| e.getItemDrop().getItemStack().getType() == Material.CHEST
				| e.getItemDrop().getItemStack().getType() == Material.COMPASS
				| e.getItemDrop().getItemStack().getType() == Material.INK_SACK
				| e.getItemDrop().getItemStack().getType() == Material.BLAZE_POWDER
				| e.getItemDrop().getItemStack().getType() == Material.COOKIE
				| e.getItemDrop().getItemStack().getType() == Material.DIAMOND
				| e.getItemDrop().getItemStack().getType() == Material.FIREWORK
				| e.getItemDrop().getItemStack().getType() == Material.GOLD_BARDING
				| e.getItemDrop().getItemStack().getType() == Material.FIRE
				| e.getItemDrop().getItemStack().getType() == Material.DIAMOND_BARDING
				| e.getItemDrop().getItemStack().getType() == Material.FIREWORK_CHARGE
				| e.getItemDrop().getItemStack().getType() == Material.FLINT
				| e.getItemDrop().getItemStack().getType() == Material.STICK
				| e.getItemDrop().getItemStack().getType() == Material.TNT
				| e.getItemDrop().getItemStack().getType() == Material.MELON_BLOCK
				| e.getItemDrop().getItemStack().getType() == Material.MONSTER_EGG
				| e.getItemDrop().getItemStack().getType() == Material.GOLD_INGOT
				| e.getItemDrop().getItemStack().getType() == Material.WOOL
				| e.getItemDrop().getItemStack().getType() == Material.BED
				| e.getItemDrop().getItemStack().getType() == Material.ENCHANTED_BOOK
				| e.getItemDrop().getItemStack().getType() == Material.NETHER_STAR
				| e.getItemDrop().getItemStack().getType() == Material.SKULL_ITEM
				| e.getItemDrop().getItemStack().getType() == Material.BLAZE_ROD
				| e.getItemDrop().getItemStack().getType() == Material.ANVIL
				| e.getItemDrop().getItemStack().getType() == Material.WOOD_SWORD
				| e.getItemDrop().getItemStack().getType() == Material.IRON_SWORD
				| e.getItemDrop().getItemStack().getType() == Material.STONE_SWORD
				| e.getItemDrop().getItemStack().getType() == Material.IRON_SWORD
				| e.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD) {
			e.setCancelled(true);
		} else {
			e.setCancelled(false);
		}
	}

	@EventHandler
	private void tabcolorido(final PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		Manager.sendTabColor(p, "\n �5�lFANTASY �e�lNETWORK \n�eSeja bem-vindo �f" + p.getName() + "\n",
				"\n    \n�cPara mais informa\u00e7\u00f5es:\n \n�cSite: �f"
						+ ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Site")
						+ "\n�cTwitter: �f@FantasyServer_\n�cTeamSpeak: �fts.fantasynetwork.com.br\n\n�aServidor atual: �fKitPvP\n");
		for (final Player pl : Duelx1.playersIn1v1) {
			pl.hidePlayer(p);
		}
		FantasyPvP.getMain().getPlayerHideManager().hideForAll(p);
	}

	@EventHandler
	private void sair(final PlayerQuitEvent e) {
		final Player p = e.getPlayer();
		e.getPlayer().setAllowFlight(false);
		final PlayerData pd = PlayerData.get(e.getPlayer());
		if (pd != null) {
			pd.saveAsync();
			PlayerData.remove(e.getPlayer());
		}
		e.setQuitMessage((String) null);
		Manager.removerArrays(p);
	}

	@EventHandler
	private void placas(final SignChangeEvent e) {
		if (e.getLine(0).contains("&")) {
			e.setLine(0, e.getLine(0).replace("&", "�"));
		}
		if (e.getLine(1).contains("&")) {
			e.setLine(1, e.getLine(1).replace("&", "�"));
		}
		if (e.getLine(2).contains("&")) {
			e.setLine(2, e.getLine(2).replace("&", "�"));
		}
		if (e.getLine(3).contains("&")) {
			e.setLine(3, e.getLine(3).replace("&", "�"));
		}
	}

	@EventHandler
	private void naodroparitens(final PlayerDropItemEvent e) {
		if (e.getItemDrop().getItemStack().getType() == Material.STORAGE_MINECART
				| e.getItemDrop().getItemStack().getType() == Material.PAPER
				| e.getItemDrop().getItemStack().getType() == Material.GOLD_INGOT
				| e.getItemDrop().getItemStack().getType() == Material.IRON_CHESTPLATE
				| e.getItemDrop().getItemStack().getType() == Material.EMERALD
				| e.getItemDrop().getItemStack().getType() == Material.EXPLOSIVE_MINECART) {
			e.setCancelled(true);
		}
		if (Manager.pegarKit(e.getPlayer()).equalsIgnoreCase("1v1")) {
			if (e.getItemDrop().getItemStack().getType() == Material.INK_SACK) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			} else if (e.getItemDrop().getItemStack().getType() == Material.STONE_SWORD) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			} else if (e.getItemDrop().getItemStack().getType() == Material.INK_SACK) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			} else if (e.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			} else if (e.getItemDrop().getItemStack().getType() == Material.GOLD_SWORD) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			} else if (e.getItemDrop().getItemStack().getType() == Material.IRON_SWORD) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			} else if (e.getItemDrop().getItemStack().getType() == Material.STONE_SWORD) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			} else if (e.getItemDrop().getItemStack().getType() == Material.WOOD_SWORD) {
				e.setCancelled(true);
				e.getPlayer().updateInventory();
			}
		}
	}

	@EventHandler
	private void morrer(final PlayerDeathEvent e) {
		final Player vitima = e.getEntity();
		final Player matador = vitima.getKiller();
		if (vitima.getKiller() != null) {
			if (vitima.getKiller() instanceof Player && e.getEntity() instanceof Player) {
				final PlayerData datamatador = PlayerData.get(matador);
				final PlayerData datavitima = PlayerData.get(vitima);
				e.setDeathMessage((String) null);
				if (datamatador == null) {
					return;
				}
				if (datavitima == null) {
					return;
				}
				e.setDeathMessage((String) null);
				final int adddinheiro = Manager.numeroaleatorio(50, 80);
				final int removerdinheiro = Manager.numeroaleatorio(10, 50);
				final int addscore = Manager.numeroaleatorio(1, 10);
				final int removerscore = Manager.numeroaleatorio(1, 10);
				datamatador.addMoney(adddinheiro);
				datavitima.removeMoney(removerdinheiro);
				datamatador.addKills(1);
				datavitima.addDeaths(1);
				datamatador.addScore(addscore);
				datavitima.removeScore(removerscore);
				datamatador.addKillStreak(1);
				if (datavitima.getKillStreak() > 0) {
					datavitima.setKillStreak(0);
				}
				if (datamatador.getKillStreak() >= 10) {
					Manager.mandamensagemKillStreak(datamatador);
				}
				matador.sendMessage(String.valueOf(Manager.prefix) + " �7Voce Matou " + vitima.getName() + "�7.");
				matador.sendMessage(String.valueOf(Manager.prefix) + " �6Voce Ganhou " + adddinheiro + "$�6.");
				matador.playSound(matador.getLocation(), Sound.ORB_PICKUP, 2.0f, 2.0f);
				matador.sendMessage("�7+�a" + addscore + " �7Score �7(�a\u25b2" + datamatador.getScore() + "�7)");
				vitima.sendMessage("�7-�c" + addscore + " �7Score �7(�c\u25bc" + datavitima.getScore() + "�7)");
				vitima.sendMessage(String.valueOf(Manager.prefix) + " �7Voce Morreu Para " + matador.getName() + "�7.");
				vitima.sendMessage(String.valueOf(Manager.prefix) + " �cVoce Perdeu " + removerdinheiro + "$�c.");
				Manager.removerArrays(vitima);
				Bukkit.getPluginManager()
						.callEvent(new PlayerDeathInWarpEvent(vitima, matador, Manager.pegarKit(vitima)));
				for (final Player pl : Duelx1.playersIn1v1) {
					pl.hidePlayer(vitima);
				}
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FantasyPvP.getPlugin(),
						new Runnable() {
							@Override
							public void run() {
								if (vitima.isOnline()) {
									vitima.spigot().respawn();
									vitima.playSound(vitima.getLocation(), Sound.BLAZE_DEATH, 1.0f, 1.0f);
								}
							}
						}, 0L);
				Manager.removerArrays(vitima);
				e.getDrops().clear();
			}
		} else {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FantasyPvP.getPlugin(),
					new Runnable() {
						@Override
						public void run() {
							if (vitima.isOnline()) {
								vitima.spigot().respawn();
								vitima.playSound(vitima.getLocation(), Sound.BLAZE_DEATH, 1.0f, 1.0f);
							}
						}
					}, 1L);
			vitima.sendMessage(String.valueOf(Manager.prefix) + " �cVoc\u00ea morreu.");
			for (final Player pl2 : Duelx1.playersIn1v1) {
				pl2.hidePlayer(vitima);
			}
			FantasyPvP.getMain().getPlayerHideManager().hideForAll(vitima);
			Manager.removerArrays(vitima);
			Bukkit.getPluginManager()
					.callEvent(new PlayerDeathInWarpEvent(vitima, null, Manager.pegarKit(vitima)));
			e.setDeathMessage((String) null);
			e.getDrops().clear();
		}
	}

	@EventHandler
	private void naocontruir(final BlockPlaceEvent e) {
		final Player p = e.getPlayer();
		if (Manager.construir.contains(p)) {
			e.setCancelled(false);
		} else {
			e.setCancelled(true);
		}
	}

	@EventHandler
	private void naoquebrar(final BlockBreakEvent e) {
		final Player p = e.getPlayer();
		if (Manager.construir.contains(p)) {
			e.setCancelled(false);
		} else {
			e.setCancelled(true);
		}
	}

	@EventHandler
	private void motd(final ServerListPingEvent e) {
		e.setMotd(ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Motd").replace("&", "�").replace("<linha>",
				"\n"));
	}

	@EventHandler
	private void spawnitem(final ItemSpawnEvent e) {
		final Item item = e.getEntity();
		if (!item.isDead()) {
			item.remove();
			item.getWorld().playEffect(item.getLocation(), Effect.SMOKE, 1);
		}
	}

	@EventHandler
	private void naochover(final WeatherChangeEvent e) {
		e.setCancelled(true);
		e.getWorld().setWeatherDuration(0);
	}

	@EventHandler
	private void semfome(final FoodLevelChangeEvent e) {
		e.setFoodLevel(100);
		e.setCancelled(true);
	}

	@EventHandler
	private void onPlayerChatRankno(final AsyncPlayerChatEvent e) {
		if (e.getMessage().contains("%")) {
			e.setMessage("%2$s".replace("%2$s", ""));
		}
	}

	@EventHandler
	private void naoexplidir(final ExplosionPrimeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	private void kits(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if (p.getItemInHand().getType() == Material.CHEST && e.getAction().name().contains("RIGHT_")
				&& !Manager.comKit(p)) {
			p.updateInventory();
			e.setCancelled(true);
			new InventarioKits(p, MenuPaged.Type.KITS);
		}
		if (p.getItemInHand().getType() == Material.BOOK && e.getAction().name().contains("RIGHT_")
				&& !Manager.comKit(p)) {
			p.updateInventory();
			e.setCancelled(true);
			new InventarioKits(p, MenuPaged.Type.WARPS);
		}
		if (p.getItemInHand().getType() == Material.EMERALD && e.getAction().name().contains("RIGHT_")
				&& !Manager.comKit(p)) {
			p.updateInventory();
			e.setCancelled(true);
			p.sendMessage(String.valueOf(Manager.prefix) + " �7Site: �a"
					+ ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Site"));
		}
		if (p.getItemInHand().getType() == Material.SKULL_ITEM && e.getAction().name().contains("RIGHT_")
				&& !Manager.comKit(p)) {
			p.updateInventory();
			e.setCancelled(true);
			Inventarios.inventarioperfil(p);
		}
	}

	@EventHandler
	private void naomoverteleport(final PlayerMoveEvent e) {
		final Player p = e.getPlayer();
		if (Manager.teleportnaomover.contains(p) && (e.getFrom().getX() != e.getTo().getX()
				|| e.getFrom().getZ() != e.getTo().getZ() || e.getFrom().getY() != e.getTo().getY())) {
			Manager.teleportnaomover.remove(p);
		}
	}

	@EventHandler
	private void naodroparkits(final PlayerDropItemEvent e) {
		if (e.getItemDrop().getItemStack().getType() == Material.FIREWORK
				| e.getItemDrop().getItemStack().getType() == Material.IRON_FENCE
				| e.getItemDrop().getItemStack().getType() == Material.SNOW_BALL
				| e.getItemDrop().getItemStack().getType() == Material.STONE_AXE
				| e.getItemDrop().getItemStack().getType() == Material.FEATHER
				| e.getItemDrop().getItemStack().getType() == Material.FISHING_ROD
				| e.getItemDrop().getItemStack().getType() == Material.LEASH
				| e.getItemDrop().getItemStack().getType() == Material.IRON_FENCE
				| e.getItemDrop().getItemStack().getType() == Material.BOW
				| e.getItemDrop().getItemStack().getType() == Material.ARROW
				| e.getItemDrop().getItemStack().getType() == Material.REDSTONE_BLOCK
				| e.getItemDrop().getItemStack().getType() == Material.LAPIS_BLOCK
				| e.getItemDrop().getItemStack().getType() == Material.BOOK) {
			e.setCancelled(true);
			e.getPlayer().updateInventory();
		}
		if (e.getItemDrop().getItemStack().getType() == Material.STONE_SWORD) {
			e.setCancelled(true);
			e.getPlayer().updateInventory();
		}
		if (e.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD) {
			e.setCancelled(true);
			e.getPlayer().updateInventory();
		}
	}

	@EventHandler
	private void semflecha(final ProjectileHitEvent e) {
		final Entity entity = e.getEntity();
		if (entity.getType() == EntityType.ARROW) {
			entity.remove();
		}
	}

	@EventHandler
	private void bussola(final PlayerInteractEvent e) {
		final Player jogador = e.getPlayer();
		boolean achou = false;
		if (jogador.getItemInHand().getType() == Material.COMPASS) {
			for (int i = 0; i < 1000; ++i) {
				final List<?> pertos = jogador.getNearbyEntities(i, 125.0, i);
				for (final Object jogadores : pertos) {
					if (jogadores instanceof Player) {
						final Player jogadorencontrado = (Player) jogadores;
						if (jogador.getLocation().distance(jogadorencontrado.getLocation()) <= 10.0
								|| Manager.Admin.contains(jogadorencontrado)) {
							continue;
						}
						achou = true;
						jogador.sendMessage(String.valueOf(Manager.prefix) + " �aBussola apontando para �f"
								+ jogadorencontrado.getName() + "�a.");
					}
				}
				if (achou) {
					break;
				}
			}
			if (!achou) {
				jogador.sendMessage(String.valueOf(Manager.prefix) + " �aBussola apontado para �fSpawn�a.");
				jogador.setCompassTarget(Bukkit.getWorlds().get(0).getSpawnLocation());
			}
		}
	}

	@EventHandler
	private void nerfardano(final EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			final Player player = (Player) event.getDamager();
			if (event.getDamage() > 1.0) {
				event.setDamage(event.getDamage() - 1.0);
			}
			if (event.getDamager() instanceof Player) {
				if (player.getFallDistance() > 0.0f && !player.isOnGround()
						&& !player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
					final int NewDamage = (int) (event.getDamage() * 1.5) - (int) event.getDamage();
					if (event.getDamage() > 1.0) {
						event.setDamage(event.getDamage() - NewDamage);
					}
				}
				if (player.getItemInHand().getType() == Material.WOOD_SWORD) {
					event.setDamage(2.0);
				}
				if (player.getItemInHand().getType() == Material.STONE_SWORD) {
					event.setDamage(3.0);
				}
				if (player.getItemInHand().getType() == Material.IRON_SWORD) {
					event.setDamage(5.0);
				}
				if (player.getItemInHand().getType() == Material.DIAMOND_SWORD) {
					event.setDamage(6.0);
				}
				if (player.getItemInHand().containsEnchantment(Enchantment.DAMAGE_ALL)) {
					event.setDamage(event.getDamage() + 1.0);
				}
				if (player.getFallDistance() > 0.0f && !player.isOnGround()
						&& !player.hasPotionEffect(PotionEffectType.BLINDNESS)) {
					if (player.getItemInHand().getType() == Material.WOOD_SWORD) {
						event.setDamage(event.getDamage() + 1.0);
					}
					if (player.getItemInHand().getType() == Material.STONE_SWORD) {
						event.setDamage(event.getDamage() + 1.0);
					}
					if (player.getItemInHand().getType() == Material.IRON_SWORD) {
						event.setDamage(event.getDamage() + 1.0);
					}
					if (player.getItemInHand().getType() == Material.DIAMOND_SWORD) {
						event.setDamage(event.getDamage() + 1.0);
					}
					if (player.getFallDistance() > 0.0f && player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
						if (player.getItemInHand().getType() == Material.WOOD_SWORD) {
							event.setDamage(event.getDamage() + 1.5);
						}
						if (player.getItemInHand().getType() == Material.STONE_SWORD) {
							event.setDamage(event.getDamage() + 1.5);
						}
						if (player.getItemInHand().getType() == Material.IRON_SWORD) {
							event.setDamage(event.getDamage() + 1.5);
						}
						if (player.getItemInHand().getType() == Material.DIAMOND_SWORD) {
							event.setDamage(event.getDamage() + 1.5);
						}
					}
					if (player.getFallDistance() > 0.0f && player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)
							&& !player.isOnGround()) {
						if (player.getItemInHand().getType() == Material.WOOD_SWORD) {
							event.setDamage(event.getDamage() + 0.5);
						}
						if (player.getItemInHand().getType() == Material.STONE_SWORD) {
							event.setDamage(event.getDamage() + 0.5);
						}
						if (player.getItemInHand().getType() == Material.IRON_SWORD) {
							event.setDamage(event.getDamage() + 0.5);
						}
						if (player.getItemInHand().getType() == Material.DIAMOND_SWORD) {
							event.setDamage(event.getDamage() + 0.5);
						}
					}
				}
			}
		}
	}

	@EventHandler
	private void combatlog(final EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			final Player p = (Player) e.getEntity();
			final Player hitter = (Player) e.getDamager();
			if (!Manager.pegarCombat(hitter) && !Manager.pegarCombat(p) && !Manager.pegarKit(p).equalsIgnoreCase("none")
					&& !Manager.pegarKit(hitter).equalsIgnoreCase("none")) {
				if (Manager.pegarKit(p).equalsIgnoreCase("1v1") && !Duelx1.isIn1v1(p) && !Duelx1.isIn1v1(hitter)) {
					return;
				}
				Manager.Combate.add(p);
				Manager.Combate.add(hitter);
				p.sendMessage(String.valueOf(Manager.prefix) + " �7Voc\u00ea �aentrou �7de combate com �f"
						+ hitter.getName() + "�7.");
				hitter.sendMessage(String.valueOf(Manager.prefix) + " �7Voc\u00ea �aentrou �7de combate com �f"
						+ p.getName() + "�7.");
				Bukkit.getScheduler().scheduleSyncDelayedTask(FantasyPvP.getPlugin(), new Runnable() {
					@Override
					public void run() {
						Manager.Combate.remove(p);
						Manager.Combate.remove(hitter);
						p.sendMessage(String.valueOf(Manager.prefix) + " �7Voc\u00ea �csaiu �7de combate com �f"
								+ hitter.getName() + "�7.");
						hitter.sendMessage(String.valueOf(Manager.prefix) + " �7Voc\u00ea �csaiu �7de combate com �f"
								+ p.getName() + "�7.");
					}
				}, 800L);
			}
		}
	}

	@EventHandler
	private void naosair(final PlayerQuitEvent e) {
		final Player p = e.getPlayer();
		if (!p.isDead() && Manager.pegarCombat(p)) {
			Manager.mandarMensagem(String.valueOf(Manager.prefix) + " �f" + p.getName() + " �7deslogou em combate.");
			p.damage(1000.0);
		}
	}

	@EventHandler
	private void bloquear(final PlayerCommandPreprocessEvent event) {
		final Player p = event.getPlayer();
		if (Manager.pegarCombat(p) && (event.getMessage().toLowerCase().startsWith("/1v1")
				|| event.getMessage().toLowerCase().startsWith("/spawn")
				|| event.getMessage().toLowerCase().startsWith("/kit")
				|| event.getMessage().toLowerCase().startsWith("/warp"))) {
			event.setCancelled(true);
			p.sendMessage(
					String.valueOf(Manager.prefix) + " �cDesculpe, Mas E Este Comando Esta Bloqueado Em Combate.");
		}
	}

	@EventHandler
	private void placasinv(final SignChangeEvent e) {
		if (e.getLine(0).equalsIgnoreCase("Sopa")) {
			e.setLine(0, "�5Fantasy�eKits");
			e.setLine(1, "�a\u25ba Sopas");
			e.setLine(3, "�eClique!");
		}
		if (e.getLine(0).equalsIgnoreCase("Recraft")) {
			e.setLine(0, "�5Fantasy�eKits");
			e.setLine(1, "�c\u25ba Recraft");
			e.setLine(3, "�eClique!");
		}
	}

	@EventHandler
	private void inv(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getAction() == Action.RIGHT_CLICK_BLOCK
				&& e.getClickedBlock() != null && (e.getClickedBlock().getType() == Material.WALL_SIGN
						|| e.getClickedBlock().getType() == Material.SIGN_POST)) {
			final Sign s = (Sign) e.getClickedBlock().getState();
			final String[] lines = s.getLines();
			if (lines.length > 0 && lines[0].equals("�5Fantasy�eKits") && lines.length > 1
					&& lines[1].equals("�a\u25ba Sopas")) {
				final Inventory inve = Bukkit.getServer().createInventory(p, 54, "�a\u25ba Sopas");
				final ItemStack primary = Manager.criarItem2(Material.MUSHROOM_SOUP, "�a\u25ba Sopa", (short) 0, 1,
						new String[] { "�7Tome Para Regenerar Sua Vida." });
				for (int sopas = 0; sopas < inve.getSize(); ++sopas) {
					while (inve.getItem(sopas) == null) {
						inve.setItem(sopas, primary);
					}
				}
				p.openInventory(inve);
			}
			if (lines.length > 0 && lines[0].equals("�5Fantasy�eKits") && lines.length > 1
					&& lines[1].equals("�c\u25ba Recraft")) {
				p.getInventory().addItem(new ItemStack[] {
						Manager.criarItem2(Material.BOWL, "�b\u25ba Tigela", (short) 0, 64, new String[0]) });
				p.getInventory().addItem(new ItemStack[] {
						Manager.criarItem2(Material.RED_MUSHROOM, "�c\u25ba Cogumelo", (short) 0, 64, new String[0]) });
				p.getInventory().addItem(new ItemStack[] { Manager.criarItem2(Material.BROWN_MUSHROOM,
						"�6\u25ba Cogumelo", (short) 0, 64, new String[0]) });
				p.updateInventory();
			}
		}
	}

	@EventHandler
	public void onPlayerDamageSponge(final EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		final Player p = (Player) e.getEntity();
		if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
			return;
		}
		if (Eventos.jump.contains(p.getName())) {
			Eventos.jump.remove(p.getName());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerSpongeDamage(final EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		final Player p = (Player) e.getEntity();
		if (e.getCause() != EntityDamageEvent.DamageCause.FALL) {
			return;
		}
		if (Eventos.jump.contains(p.getName())) {
			Eventos.jump.remove(p.getName());
			e.setDamage(0.0);
		}
	}

	@EventHandler
	private void Esponja(final PlayerMoveEvent e) {
		final Player p = e.getPlayer();
		if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SPONGE) {
			p.getWorld().playEffect(p.getLocation(), Effect.SLIME, 1);
			p.getWorld().playEffect(p.getLocation(), Effect.SLIME, 1);
			p.getWorld().playEffect(p.getLocation(), Effect.SLIME, 1);
			p.getWorld().playEffect(p.getLocation(), Effect.SLIME, 1);
			p.getWorld().playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 2.0f, 2.0f);
			p.getWorld().playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 2.0f, 2.0f);
			p.getWorld().playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 2.0f, 2.0f);
			p.getWorld().playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 2.0f, 2.0f);
			p.setVelocity(p.getEyeLocation().getDirection().multiply(0).add(new Vector(0.0, 3.0, 0.0)));
			if (!this.nofalldamagec.contains(p.getName())) {
				this.nofalldamagec.add(p.getName());
			}
		}
	}

	@EventHandler
	private void SemDano2(final EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			if (this.nofalldamagec.contains(player.getName())
					&& event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
				event.setCancelled(true);
				this.nofalldamagec.remove(player.getName());
			}
		}
	}

	@EventHandler
	private void esponja2(final PlayerMoveEvent e) {
		final Player p = e.getPlayer();
		if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.PISTON_BASE) {
			final Vector sponge = p.getLocation().getDirection().multiply(0.1).setY(5.0);
			p.getWorld().playEffect(p.getLocation(), Effect.SLIME, 1);
			p.getWorld().playEffect(p.getLocation(), Effect.SLIME, 1);
			p.getWorld().playEffect(p.getLocation(), Effect.SLIME, 1);
			p.getWorld().playEffect(p.getLocation(), Effect.SLIME, 1);
			p.getWorld().playSound(p.getLocation(), Sound.PISTON_EXTEND, 2.0f, 2.0f);
			p.getWorld().playSound(p.getLocation(), Sound.PISTON_EXTEND, 2.0f, 2.0f);
			p.getWorld().playSound(p.getLocation(), Sound.PISTON_EXTEND, 2.0f, 2.0f);
			p.getWorld().playSound(p.getLocation(), Sound.PISTON_EXTEND, 2.0f, 2.0f);
			p.setVelocity(sponge);
			if (!this.nofalldamage.contains(p.getName())) {
				this.nofalldamage.add(p.getName());
			}
		}
	}

	@EventHandler
	private void SemDano(final EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player player = (Player) event.getEntity();
			if (this.nofalldamage.contains(player.getName())
					&& event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
				event.setCancelled(true);
				this.nofalldamage.remove(player.getName());
			}
		}
	}

	@EventHandler
	private void clicarinventariokits(final InventoryClickEvent e) {
		final Player jogador = (Player) e.getWhoClicked();
		if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null
				&& e.getCurrentItem().getItemMeta().getDisplayName() != null) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("�a\u279c �7Loja")) {
				e.setCancelled(true);
				jogador.updateInventory();
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("�e\u279c �7Kits")) {
				e.setCancelled(true);
				jogador.updateInventory();
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("�b\u279c �7Seu perfil")) {
				e.setCancelled(true);
				jogador.updateInventory();
			} else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("�b\u279c �7Warps")) {
				e.setCancelled(true);
				jogador.updateInventory();
			}
			if (e.getInventory().getTitle().equalsIgnoreCase("�7Ranks do servidor \u25ba")) {
				e.setCancelled(true);
				jogador.updateInventory();
			}
			if (e.getInventory().getTitle().equalsIgnoreCase("�7Estat\u00edsticas \u25ba ")) {
				e.setCancelled(true);
				jogador.updateInventory();
				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("�bVoltar")) {
					e.setCancelled(true);
					jogador.updateInventory();
					Inventarios.inventarioperfil(jogador);
					jogador.playSound(jogador.getLocation(), Sound.ORB_PICKUP, 2.0f, 2.0f);
				}
				e.setCancelled(true);
				jogador.updateInventory();
			}
			if (e.getInventory().getTitle().equalsIgnoreCase("�7Perfil \u25ba")) {
				e.setCancelled(true);
				jogador.updateInventory();
				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("�e\u25ba Prefer\u00eancias")) {
					e.setCancelled(true);
					jogador.updateInventory();
					Inventarios.inventariotoggle(jogador, MenuPaged.Type.PERFIL);
					jogador.playSound(jogador.getLocation(), Sound.ORB_PICKUP, 2.0f, 2.0f);
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("�a\u25ba Estat\u00edsticas")) {
					e.setCancelled(true);
					jogador.updateInventory();
					Inventarios.inventarioestatisticas(jogador);
					jogador.playSound(jogador.getLocation(), Sound.ORB_PICKUP, 2.0f, 2.0f);
				}
			}
			if (e.getInventory().getTitle().equalsIgnoreCase("�7Op\u00e7\u00f5es \u25ba ")) {
				e.setCancelled(true);
				jogador.updateInventory();
				if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("�bVoltar")) {
					e.setCancelled(true);
					jogador.updateInventory();
					Inventarios.inventarioperfil(jogador);
					jogador.playSound(jogador.getLocation(), Sound.ORB_PICKUP, 2.0f, 2.0f);
				}
				if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equalsIgnoreCase("�c\u25ba Voce N\u00e3o Tem Esta Op\u00e7\u00e3o")) {
					e.setCancelled(true);
					jogador.updateInventory();
					jogador.playSound(jogador.getLocation(), Sound.IRONGOLEM_HIT, 2.0f, 2.0f);
					jogador.sendMessage(String.valueOf(Manager.prefix) + " �cVoce N\u00e3o Tem Esta Op\u00e7\u00e3o.");
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equalsIgnoreCase("�a\u25ba Receber Report - Ativado")) {
					e.setCancelled(true);
					jogador.updateInventory();
					jogador.playSound(jogador.getLocation(), Sound.CLICK, 2.0f, 2.0f);
					e.setCurrentItem(Manager.criarItem(Material.INK_SACK, "�c\u25ba Receber Report - Desativado",
							(short) 8, new String[] { "", "�7Clique Para Alterar." }));
					Manager.Report.put(jogador.getName(), false);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equalsIgnoreCase("�c\u25ba Receber Report - Desativado")) {
					e.setCancelled(true);
					jogador.updateInventory();
					jogador.playSound(jogador.getLocation(), Sound.CLICK, 2.0f, 2.0f);
					e.setCurrentItem(Manager.criarItem(Material.INK_SACK, "�a\u25ba Receber Report - Ativado",
							(short) 10, new String[] { "", "�7Clique Para Alterar." }));
					Manager.Report.put(jogador.getName(), true);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equalsIgnoreCase("�a\u25ba Receber Tell - Ativado")) {
					e.setCancelled(true);
					jogador.updateInventory();
					jogador.playSound(jogador.getLocation(), Sound.CLICK, 2.0f, 2.0f);
					e.setCurrentItem(Manager.criarItem(Material.INK_SACK, "�c\u25ba Receber Tell - Desativado",
							(short) 8, new String[] { "", "�7Clique Para Alterar." }));
					Manager.Tell.put(jogador.getName(), false);
				} else if (e.getCurrentItem().getItemMeta().getDisplayName()
						.equalsIgnoreCase("�c\u25ba Receber Tell - Desativado")) {
					e.setCancelled(true);
					jogador.updateInventory();
					jogador.playSound(jogador.getLocation(), Sound.CLICK, 2.0f, 2.0f);
					e.setCurrentItem(Manager.criarItem(Material.INK_SACK, "�a\u25ba Receber Tell - Ativado", (short) 10,
							new String[] { "", "�7Clique Para Alterar." }));
					Manager.Tell.put(jogador.getName(), true);
				}
			}
		}
	}

	@EventHandler
	public void Damage(final EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player
				&& Manager.pegarKit((Player) e.getEntity()).equalsIgnoreCase("Lava")) {
			final Player p = (Player) e.getDamager();
			if (Manager.pegarKit(p).equalsIgnoreCase("Lava")
					&& Manager.pegarKit((Player) e.getEntity()).equalsIgnoreCase("Lava")) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	private void semcomandos(final PlayerCommandPreprocessEvent e) {
		if ((Manager.pegarKit(e.getPlayer()).equalsIgnoreCase("Fps")
				|| Manager.pegarKit(e.getPlayer()).equalsIgnoreCase("Lava")
				|| Manager.pegarKit(e.getPlayer()).equalsIgnoreCase("Potion")
				|| Manager.pegarKit(e.getPlayer()).equalsIgnoreCase("Main")) && !e.getMessage().startsWith("/spawn")) {
			e.getPlayer().sendMessage(
					String.valueOf(Manager.prefix) + " �cDesculpe,mas voc\u00ea n\u00e3o pode usar Comandos em warps.");
			e.setCancelled(true);
		}
	}
}
