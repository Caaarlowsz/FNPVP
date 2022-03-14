package com.zalpha.FantasyNetwork.PvP.abilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSnowball;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;
import com.github.caaarlowsz.fantasymc.kitpvp.FantasyPvP;
import com.zalpha.FantasyNetwork.PvP.util.Manager;

import net.minecraft.server.v1_8_R3.EntitySnowball;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

public class Habilidades implements Listener {
	public ArrayList<String> Cima;
	private static ArrayList<Player> inkangaroo;
	HashMap<Player, Player> NinjaLocal;
	public static boolean generateGlass;
	public static HashMap<String, Location> oldl;
	public static HashMap<String, String> fighting;
	public static HashMap<Integer, ArrayList<Location>> blocks;
	public static HashMap<Player, Location> localizacao;
	public static HashMap<Location, Block> bloco;
	public static HashMap<Integer, String[]> players;
	public static HashMap<String, Integer> tasks;
	public static int nextID;
	public static int id1;
	public static int id2;
	private static HashMap<String, ItemStack[]> armaduras;
	public static ArrayList<Player> Particulas;
	public static ArrayList<Player> Cancel;
	public static ArrayList<String> Congelar;
	public boolean sendThroughInventory;
	public static ArrayList<Player> Zoom;

	static {
		Habilidades.inkangaroo = new ArrayList<Player>();
		Habilidades.generateGlass = true;
		Habilidades.oldl = new HashMap<String, Location>();
		Habilidades.fighting = new HashMap<String, String>();
		Habilidades.blocks = new HashMap<Integer, ArrayList<Location>>();
		Habilidades.localizacao = new HashMap<Player, Location>();
		Habilidades.bloco = new HashMap<Location, Block>();
		Habilidades.players = new HashMap<Integer, String[]>();
		Habilidades.tasks = new HashMap<String, Integer>();
		Habilidades.nextID = 0;
		Habilidades.armaduras = new HashMap<String, ItemStack[]>();
		Habilidades.Particulas = Lists.newArrayList();
		Habilidades.Cancel = Lists.newArrayList();
		Habilidades.Congelar = new ArrayList<String>();
		Habilidades.Zoom = new ArrayList<Player>();
	}

	public Habilidades() {
		this.Cima = new ArrayList<String>();
		this.NinjaLocal = new HashMap<Player, Player>();
		this.sendThroughInventory = true;
	}

	@EventHandler
	private void anchor(final EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			if (e.isCancelled()) {
				return;
			}
			final Player p = (Player) e.getEntity();
			final Player a = (Player) e.getDamager();
			if (Manager.pegarKit(p).equalsIgnoreCase("anchor")) {
				p.setVelocity(new Vector());
				p.playSound(p.getLocation(), Sound.ANVIL_USE, 2.0f, 2.0f);
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FantasyPvP.getPlugin(),
						new Runnable() {
							@Override
							public void run() {
								p.setVelocity(new Vector());
							}
						}, 1L);
			}
			if (Manager.pegarKit(a).equalsIgnoreCase("anchor")) {
				a.playSound(a.getLocation(), Sound.ANVIL_USE, 2.0f, 2.0f);
				p.setVelocity(new Vector());
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FantasyPvP.getPlugin(),
						new Runnable() {
							@Override
							public void run() {
								p.setVelocity(new Vector());
							}
						}, 1L);
			}
		}
	}

	@EventHandler
	public void Madman(final EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			final Player p = (Player) e.getEntity();
			final Player t = (Player) e.getDamager();
			if (Manager.pegarKit(t) == "Madman" && t.getItemInHand().getType() == Material.STONE_SWORD
					&& new Random().nextInt(100) <= 40) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 3));
			}
		}
	}

	@EventHandler
	public void Goku(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& Manager.pegarKit(p) == "Goku" && p.getItemInHand().getType() == Material.IRON_BLOCK
				&& (!Manager.Cooldown.containsKey(p.getName())
						|| Manager.Cooldown.get(p.getName()) <= System.currentTimeMillis())) {
			final Location location = p.getEyeLocation();
			final BlockIterator blocksToAdd = new BlockIterator(location, 0.0, 40);
			while (blocksToAdd.hasNext()) {
				final Location blockToAdd = blocksToAdd.next().getLocation();
				p.getWorld().playEffect(blockToAdd, Effect.STEP_SOUND, (Object) Material.IRON_BLOCK, 20);
				p.playSound(blockToAdd, Sound.IRONGOLEM_THROW, 3.0f, 3.0f);
			}
			final Snowball h = (Snowball) p.launchProjectile(Snowball.class);
			final Vector velo1 = p.getLocation().getDirection().normalize().multiply(10);
			h.setVelocity(velo1);
			h.setMetadata("Goku",
					new FixedMetadataValue(FantasyPvP.getInstace(), true));
			Manager.Cooldown.put(p.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15L));
			p.sendMessage(String.valueOf(Manager.prefix) + "�aVoc\u00ea usou seu �fGOKU�a!");
		} else if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& Manager.pegarKit(p) == "Goku" && p.getItemInHand().getType() == Material.IRON_BLOCK) {
			p.sendMessage(String.valueOf(Manager.prefix) + "�cAguarde mais �f" + Manager.pegarCooldown(p)
					+ "�c Segundos para usar o GOKU!");
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void DanoGoku(final EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Snowball) {
			final Snowball s = (Snowball) e.getDamager();
			if (s.hasMetadata("Goku")) {
				e.setDamage(e.getDamage() + 9.0);
			}
		}
	}

	@EventHandler
	private void fisherman(final PlayerFishEvent e) {
		if (e.getCaught() instanceof Player) {
			final Player jogador = e.getPlayer();
			final Player fisgado = (Player) e.getCaught();
			if (Manager.pegarKit(jogador).equalsIgnoreCase("fisherman")) {
				jogador.sendMessage(String.valueOf(Manager.prefix) + " �aVoce Puxou �f" + fisgado.getName() + "�a.");
				fisgado.teleport(jogador.getLocation());
			}
		}
	}

	@EventHandler
	public void Boxer(final EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			final Player t = (Player) e.getDamager();
			if (Manager.pegarKit(t) == "Boxer" && t.getItemInHand().getType() == Material.STONE_SWORD) {
				e.setDamage(e.getDamage() + 4.0);
			}
		}
	}

	@EventHandler
	private void kangaroo(final PlayerInteractEvent e) {
		final Player jogador = e.getPlayer();
		if ((e.getAction().name().contains("RIGHT") || e.getAction().name().contains("LEFT"))
				&& jogador.getItemInHand().getType() == Material.FIREWORK
				&& Manager.pegarKit(jogador).equalsIgnoreCase("kangaroo")) {
			e.setCancelled(true);
			if (Manager.pegarCombat(jogador)) {
				jogador.sendMessage(String.valueOf(Manager.prefix)
						+ " �cDesculpe, Mais Voce Esta Em Combate Ou Seja Seu Kit Foi Desabilitado Temporariamante.");
				Manager.adicionarEfeito(jogador, 20, 1, PotionEffectType.SLOW);
				Manager.adicionarCooldown(jogador, 5);
				return;
			}
			if (!Habilidades.inkangaroo.contains(jogador)) {
				if (!jogador.isSneaking()) {
					jogador.setFallDistance(-3.0f);
					final Vector vector = jogador.getEyeLocation().getDirection();
					vector.multiply(0.6f);
					vector.setY(1.4f);
					jogador.setVelocity(vector);
				} else {
					jogador.setFallDistance(-3.0f);
					final Vector vector = jogador.getEyeLocation().getDirection();
					vector.multiply(2.0f);
					vector.setY(0.6f);
					jogador.setVelocity(vector);
				}
				Habilidades.inkangaroo.add(jogador);
			}
		}
	}

	@EventHandler
	private void moverkangaroo(final PlayerMoveEvent e) {
		final Player jogador = e.getPlayer();
		if (Manager.pegarKit(jogador).equalsIgnoreCase("kangaroo") && Habilidades.inkangaroo.contains(jogador)
				&& (jogador.getLocation().getBlock().getType() != Material.AIR
						|| jogador.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR)) {
			Habilidades.inkangaroo.remove(jogador);
		}
	}

	@EventHandler
	private void kangaroodano(final EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			final Player jogador = (Player) e.getEntity();
			if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)
					&& Manager.pegarKit(jogador).equalsIgnoreCase("kangaroo")
					&& jogador.getInventory().contains(Material.FIREWORK) && e.getDamage() >= 12.0) {
				e.setDamage(12.0);
			}
		}
	}

	@EventHandler
	private void switcher(final EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Snowball) {
			final Snowball snow = (Snowball) e.getDamager();
			final Player jogador = (Player) snow.getShooter();
			final Player jogadorpuxado = (Player) e.getEntity();
			if (snow.getShooter() instanceof Player && Manager.pegarKit(jogador).equalsIgnoreCase("switcher")
					&& jogadorpuxado != jogador) {
				final Location ploc = jogador.getLocation();
				final Location hitloc = jogadorpuxado.getLocation();
				jogador.teleport(hitloc);
				jogadorpuxado.teleport(ploc);
				e.setCancelled(true);
				jogadorpuxado.getWorld().playEffect(jogadorpuxado.getLocation(), Effect.PORTAL, 1);
				jogador.getWorld().playEffect(jogador.getLocation(), Effect.PORTAL, 1);
				jogador.playSound(jogador.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0f, 2.0f);
				jogadorpuxado.playSound(jogadorpuxado.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0f, 2.0f);
			}
		}
	}

	@EventHandler
	private void thor(final PlayerInteractEvent e) {
		final Player jogador = e.getPlayer();
		if (Manager.pegarKit(jogador).equalsIgnoreCase("thor") && e.getAction().name().contains("RIGHT")
				&& jogador.getItemInHand().getType() == Material.STONE_AXE) {
			if (Manager.naoTemCooldown(jogador)) {
				Manager.adicionarCooldown(jogador, 7);
				Location loc = Manager.getTargetBlock(jogador, 40);
				loc = loc.getWorld().getHighestBlockAt(loc).getLocation();
				jogador.getWorld().strikeLightning(loc).setFireTicks(2);
				final Block block = loc.add(0.0, 1.0, 0.0).getBlock();
				block.setType(Material.AIR);
			} else {
				Manager.mensagemCooldown(jogador);
			}
		}
	}

	@EventHandler
	public void ninjaHabilidade(final EntityDamageByEntityEvent evento) {
		if (evento.getEntity() instanceof Player && evento.getDamager() instanceof Player) {
			final Player jogadorHitado = (Player) evento.getEntity();
			final Player jogador = (Player) evento.getDamager();
			if (Manager.pegarKit(jogador).equalsIgnoreCase("ninja")) {
				this.NinjaLocal.put(jogador, jogadorHitado);
			}
		}
	}

	@EventHandler
	public void teleportarNinja(final PlayerToggleSneakEvent evento) {
		final Player jogador = evento.getPlayer();
		final Player hitado = this.NinjaLocal.get(jogador);
		if (Manager.pegarKit(jogador).equalsIgnoreCase("ninja") && jogador.isSneaking()) {
			if (Manager.naoTemCooldown(jogador)) {
				if (this.NinjaLocal.containsKey(jogador)) {
					if (!Habilidades.fighting.containsKey(jogador.getName())) {
						if (!Habilidades.fighting.containsKey(hitado.getName())) {
							if (jogador.getLocation().distance(hitado.getLocation()) < 25.0) {
								Manager.adicionarCooldown(jogador, 7);
								jogador.teleport(this.NinjaLocal.get(jogador));
								jogador.sendMessage(String.valueOf(Manager.prefix) + " �aVoce Teleportou Para �f�l"
										+ hitado.getName() + "�a.");
								jogador.getWorld().playEffect(jogador.getLocation(), Effect.SMOKE, 1);
								jogador.getWorld().playEffect(jogador.getLocation(), Effect.SMOKE, 1);
								jogador.getWorld().playEffect(jogador.getLocation(), Effect.SMOKE, 1);
								jogador.getWorld().playEffect(jogador.getLocation(), Effect.SMOKE, 1);
								jogador.getWorld().playEffect(jogador.getLocation(), Effect.SMOKE, 1);
								jogador.getWorld().playEffect(jogador.getLocation(), Effect.SMOKE, 1);
							} else {
								jogador.sendMessage(String.valueOf(Manager.prefix)
										+ " �cDesculpe, Mais O Ultimo Jogador Hitado Esta Muito Longe.");
							}
						} else {
							jogador.sendMessage(String.valueOf(Manager.prefix)
									+ " �cDesculpe, Mais O Ultimo Jogador Hitado Esta No Gladiator.");
						}
					} else {
						jogador.sendMessage(String.valueOf(Manager.prefix)
								+ " �cDesculpe, Mais Voce Nao Pode Usar O Ninja No Gladiator.");
					}
				}
			} else {
				Manager.mensagemCooldown(jogador);
			}
		}
	}

	@EventHandler
	private void gladiator(final PlayerInteractEntityEvent event) {
		final Player p = event.getPlayer();
		if (!(event.getRightClicked() instanceof Player)) {
			return;
		}
		final Player r = (Player) event.getRightClicked();
		if (p.getItemInHand().getType() == Material.IRON_FENCE && Manager.pegarKit(p).equalsIgnoreCase("gladiator")) {
			event.setCancelled(true);
			final Location loc = new Location(p.getWorld(), p.getLocation().getBlockX(),
					p.getLocation().getBlockY() + 70, p.getLocation().getBlockZ());
			Habilidades.localizacao.put(p, loc);
			Habilidades.localizacao.put(r, loc);
			final Location loc2 = new Location(p.getWorld(), p.getLocation().getBlockX() + 6,
					p.getLocation().getBlockY() + 73, p.getLocation().getBlockZ() + 6);
			final Location loc3 = new Location(p.getWorld(), p.getLocation().getBlockX() - 5,
					p.getLocation().getBlockY() + 73, p.getLocation().getBlockZ() - 5);
			if (Habilidades.fighting.containsKey(p.getName()) || Habilidades.fighting.containsKey(r.getName())) {
				event.setCancelled(true);
				p.sendMessage(String.valueOf(Manager.prefix) + " �cDesculpe, Mais Voce Ja Esta Em Um Gladiator.");
				return;
			}
			final Integer currentID = Habilidades.nextID;
			++Habilidades.nextID;
			final ArrayList<String> list = new ArrayList<String>();
			list.add(p.getName());
			list.add(r.getName());
			Habilidades.players.put(currentID, list.toArray(new String[1]));
			Habilidades.oldl.put(p.getName(), p.getLocation());
			Habilidades.oldl.put(r.getName(), r.getLocation());
			if (Habilidades.generateGlass) {
				final List<Location> cuboid = new ArrayList<Location>();
				cuboid.clear();
				for (int bX = -7; bX <= 7; ++bX) {
					for (int bZ = -7; bZ <= 7; ++bZ) {
						for (int bY = -1; bY <= 7; ++bY) {
							final Block b = loc.clone().add(bX, bY, bZ).getBlock();
							if (!b.isEmpty()) {
								event.setCancelled(true);
								p.sendMessage(
										String.valueOf(Manager.prefix) + " �cVoce Nao Pode Gerar Um Gladiator Aqui.");
								return;
							}
							if (bY == 7) {
								cuboid.add(loc.clone().add(bX, bY, bZ));
							} else if (bY == -1) {
								cuboid.add(loc.clone().add(bX, bY, bZ));
							} else if (bX == -7 || bZ == -7 || bX == 7 || bZ == 7) {
								cuboid.add(loc.clone().add(bX, bY, bZ));
							}
						}
					}
				}
				for (final Location loc4 : cuboid) {
					loc4.getBlock().setType(Material.GLASS);
					Habilidades.bloco.put(loc4, loc4.getBlock());
				}
				loc2.setYaw(135.0f);
				p.teleport(loc2);
				loc3.setYaw(-45.0f);
				r.teleport(loc3);
				p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 17, 5));
				r.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 17, 5));
				p.sendMessage(
						String.valueOf(Manager.prefix) + " �aVoce Desafiou �f" + r.getName() + " �aPara Um Batalha.");
				p.sendMessage(String.valueOf(Manager.prefix)
						+ " �aCaso N\u00e3o Tenha Um Vencedor Definido Apos �f4 Minutos �aVoce Voltar\u00e1 Ao Seu Lugar De Origem.");
				r.sendMessage(String.valueOf(Manager.prefix) + " �aVoce Foi Desafiado Por �f" + p.getName()
						+ " �aPara Um Batalha.");
				r.sendMessage(String.valueOf(Manager.prefix)
						+ " �aCaso N\u00e3o Tenha Um Vencedor Definido Apos �f4 Minutos �aVoce Voltar\u00e1 Ao Seu Lugar De Origem.");
				Habilidades.fighting.put(p.getName(), r.getName());
				Habilidades.fighting.put(r.getName(), p.getName());
				Habilidades.id2 = Bukkit.getScheduler().scheduleSyncDelayedTask(FantasyPvP.getPlugin(),
						new Runnable() {
							@Override
							public void run() {
								if (Habilidades.fighting.containsKey(p.getName())
										&& Habilidades.fighting.get(p.getName()).equalsIgnoreCase(r.getName())
										&& Habilidades.fighting.containsKey(r.getName())
										&& Habilidades.fighting.get(r.getName()).equalsIgnoreCase(p.getName())) {
									p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 2000000, 5));
									r.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 2000000, 5));
								}
							}
						}, 2400L);
				Habilidades.id1 = Bukkit.getScheduler().scheduleSyncDelayedTask(FantasyPvP.getPlugin(),
						new Runnable() {
							@Override
							public void run() {
								if (Habilidades.fighting.containsKey(p.getName())
										&& Habilidades.fighting.get(p.getName()).equalsIgnoreCase(r.getName())
										&& Habilidades.fighting.containsKey(r.getName())
										&& Habilidades.fighting.get(r.getName()).equalsIgnoreCase(p.getName())) {
									Habilidades.fighting.remove(p.getName());
									Habilidades.fighting.remove(r.getName());
									p.teleport(Habilidades.oldl.get(p.getName()));
									r.teleport(Habilidades.oldl.get(r.getName()));
									Habilidades.oldl.remove(p.getName());
									Habilidades.oldl.remove(r.getName());
									p.sendMessage(String.valueOf(Manager.prefix)
											+ " �cN\u00e3o Houve Um Vencedor Definido, Voce Voltou Ao Seu Lugar De Origem.");
									r.sendMessage(String.valueOf(Manager.prefix)
											+ " �cN\u00e3o Houve Um Vencedor Definido, Voce Voltou Ao Seu Lugar De Origem.");
									r.removePotionEffect(PotionEffectType.WITHER);
									p.removePotionEffect(PotionEffectType.WITHER);
									final Location loc = Habilidades.localizacao.get(p);
									final List<Location> cuboid = new ArrayList<Location>();
									for (int bX = -7; bX <= 7; ++bX) {
										for (int bZ = -7; bZ <= 7; ++bZ) {
											for (int bY = -1; bY <= 7; ++bY) {
												if (bY == 7) {
													cuboid.add(loc.clone().add(bX, bY, bZ));
												} else if (bY == -1) {
													cuboid.add(loc.clone().add(bX, bY, bZ));
												} else if (bX == -7 || bZ == -7 || bX == 7 || bZ == 7) {
													cuboid.add(loc.clone().add(bX, bY, bZ));
												}
											}
										}
									}
									for (final Location loc2 : cuboid) {
										loc2.getBlock().setType(Material.AIR);
										Habilidades.bloco.get(loc2).setType(Material.AIR);
									}
								}
							}
						}, 4800L);
			}
		}
	}

	@EventHandler
	private void gladiatornaoclicar(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if (p.getItemInHand().getType() == Material.IRON_FENCE && Manager.pegarKit(p).equalsIgnoreCase("gladiator")) {
			e.setCancelled(true);
			p.updateInventory();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void gladiatornaobater(final PlayerInteractEvent e) {
		if (e.getAction() == Action.LEFT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.GLASS
				&& e.getPlayer().getGameMode() != GameMode.CREATIVE
				&& Habilidades.fighting.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
			e.getClickedBlock().setType(Material.BEDROCK);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(FantasyPvP.getPlugin(),
					new Runnable() {
						@Override
						public void run() {
							if (Habilidades.fighting.containsKey(e.getPlayer().getName())) {
								e.getClickedBlock().setType(Material.GLASS);
							}
						}
					}, 70L);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void gladiatornaosair(final PlayerQuitEvent e) {
		final Player p = e.getPlayer();
		if (Habilidades.fighting.containsKey(p.getName())) {
			final Player t = Bukkit.getServer().getPlayer(Habilidades.fighting.get(p.getName()));
			Habilidades.fighting.remove(t.getName());
			Habilidades.fighting.remove(p.getName());
			final Location old = Habilidades.oldl.get(t.getName());
			t.teleport(old);
			t.sendMessage(String.valueOf(Manager.prefix) + " �cSeu Oponente Deslogou.");
			Bukkit.getScheduler().cancelTask(Habilidades.id1);
			Bukkit.getScheduler().cancelTask(Habilidades.id2);
			t.removePotionEffect(PotionEffectType.WITHER);
			final Location loc = Habilidades.localizacao.get(p);
			final List<Location> cuboid = new ArrayList<Location>();
			for (int bX = -7; bX <= 7; ++bX) {
				for (int bZ = -7; bZ <= 7; ++bZ) {
					for (int bY = -1; bY <= 7; ++bY) {
						if (bY == 7) {
							cuboid.add(loc.clone().add(bX, bY, bZ));
						} else if (bY == -1) {
							cuboid.add(loc.clone().add(bX, bY, bZ));
						} else if (bX == -7 || bZ == -7 || bX == 7 || bZ == 7) {
							cuboid.add(loc.clone().add(bX, bY, bZ));
						}
					}
				}
			}
			for (final Location loc2 : cuboid) {
				loc2.getBlock().setType(Material.AIR);
				Habilidades.bloco.get(loc2).setType(Material.AIR);
			}
			for (final Location loc2 : cuboid) {
				loc2.getBlock().setType(Material.AIR);
				Habilidades.bloco.get(loc2).setType(Material.AIR);
			}
			for (final Location loc2 : cuboid) {
				loc2.getBlock().setType(Material.AIR);
				Habilidades.bloco.get(loc2).setType(Material.AIR);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void gladiatormorrer(final PlayerDeathEvent e) {
		final Player p = e.getEntity();
		if (Habilidades.fighting.containsKey(p.getName())) {
			final Player k = Bukkit.getServer().getPlayer(Habilidades.fighting.get(p.getName()));
			final Location old = Habilidades.oldl.get(p.getName());
			k.teleport(old);
			p.getInventory().clear();
			k.sendMessage(String.valueOf(Manager.prefix) + " �aVoce Ganhou A Batalha Contra �f" + p.getName() + "�a.");
			p.sendMessage(String.valueOf(Manager.prefix) + " �cVoce Perdeu A Batalha Contra �f" + k.getName() + "�c.");
			Bukkit.getScheduler().cancelTask(Habilidades.id1);
			Bukkit.getScheduler().cancelTask(Habilidades.id2);
			k.removePotionEffect(PotionEffectType.WITHER);
			k.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 70, 7));
			Habilidades.fighting.remove(k.getName());
			Habilidades.fighting.remove(p.getName());
			final Location loc = Habilidades.localizacao.get(p);
			final List<Location> cuboid = new ArrayList<Location>();
			cuboid.clear();
			for (int bX = -7; bX <= 7; ++bX) {
				for (int bZ = -7; bZ <= 7; ++bZ) {
					for (int bY = -1; bY <= 7; ++bY) {
						if (bY == 7) {
							cuboid.add(loc.clone().add(bX, bY, bZ));
						} else if (bY == -1) {
							cuboid.add(loc.clone().add(bX, bY, bZ));
						} else if (bX == -7 || bZ == -7 || bX == 7 || bZ == 7) {
							cuboid.add(loc.clone().add(bX, bY, bZ));
						}
					}
				}
			}
			for (final Location loc2 : cuboid) {
				loc2.getBlock().setType(Material.AIR);
				Habilidades.bloco.get(loc2).setType(Material.AIR);
			}
		}
	}

	@EventHandler
	private void kitsdedano(final EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			final Player jogador = (Player) e.getEntity();
			final Player jogadordano = (Player) e.getDamager();
			if (Manager.pegarKit(jogadordano).equalsIgnoreCase("viper")) {
				Manager.chanceefeito(jogador, 40, 1, 5, PotionEffectType.POISON, false);
			}
			if (Manager.pegarKit(jogadordano).equalsIgnoreCase("snail")) {
				Manager.chanceefeito(jogador, 40, 1, 10, PotionEffectType.SLOW, false);
			}
			if (Manager.pegarKit(jogadordano).equalsIgnoreCase("magma")) {
				Manager.chanceefeito(jogador, 200, 1, 10, null, true);
			}
		}
	}

	@EventHandler
	private void danomagma(final PlayerMoveEvent e) {
		final Player jogador = e.getPlayer();
		if (Manager.pegarKit(jogador).equalsIgnoreCase("magma")
				&& jogador.getLocation().getBlock().getType().name().contains("WATER")) {
			jogador.damage(2.5);
		}
		if (Manager.pegarKit(jogador).equalsIgnoreCase("poseidon")
				&& jogador.getLocation().getBlock().getType().name().contains("WATER")) {
			Manager.adicionarEfeito(jogador, 100, 0, PotionEffectType.WATER_BREATHING);
			Manager.adicionarEfeito(jogador, 100, 0, PotionEffectType.REGENERATION);
			Manager.adicionarEfeito(jogador, 100, 0, PotionEffectType.INCREASE_DAMAGE);
		}
	}

	@EventHandler
	private void phantom(final PlayerInteractEvent e) {
		final Player jogador = e.getPlayer();
		if (Manager.pegarKit(jogador).equalsIgnoreCase("phantom") && e.getAction().name().contains("RIGHT")
				&& jogador.getItemInHand().getType() == Material.FEATHER) {
			if (Manager.naoTemCooldown(jogador)) {
				jogador.setAllowFlight(true);
				Manager.adicionarCooldown(jogador, 30);
				jogador.sendMessage(String.valueOf(Manager.prefix) + " �aVoce Tem �f10 Segundos �aDe V\u00f4o.");
				for (final Entity pertos : jogador.getNearbyEntities(10.0, 10.0, 10.0)) {
					if (pertos instanceof Player) {
						final Player jogadores = (Player) pertos;
						jogadores.sendMessage(String.valueOf(Manager.prefix) + " �a�lH\u00e1 Um Phantom Por Perto...");
						jogadores.sendMessage(
								String.valueOf(Manager.prefix) + " �a�lIsto N\u00e3o E Um Hack E Sim Um Kit.");
					}
				}
				Habilidades.armaduras.put(jogador.getName(), jogador.getInventory().getArmorContents());
				jogador.getInventory()
						.setHelmet(Manager.setarCor(Material.LEATHER_HELMET, Color.WHITE, "�8Armadura Fantasma"));
				jogador.getInventory().setChestplate(
						Manager.setarCor(Material.LEATHER_CHESTPLATE, Color.WHITE, "�8Armadura Fantasma"));
				jogador.getInventory()
						.setLeggings(Manager.setarCor(Material.LEATHER_LEGGINGS, Color.WHITE, "�8Armadura Fantasma"));
				jogador.getInventory()
						.setBoots(Manager.setarCor(Material.LEATHER_BOOTS, Color.WHITE, "�8Armadura Fantasma"));
				jogador.updateInventory();
				new BukkitRunnable() {
					int tempophantom = 10;

					public void run() {
						--this.tempophantom;
						jogador.setAllowFlight(true);
						if (this.tempophantom > 0) {
							if (this.tempophantom == 1) {
								jogador.sendMessage(String.valueOf(Manager.prefix) + " �aVoce Tem �f"
										+ this.tempophantom + " Segundo �aDe V\u00f4o.");
							} else {
								jogador.sendMessage(String.valueOf(Manager.prefix) + " �aVoce Tem �f"
										+ this.tempophantom + " Segundos �aDe V\u00f4o.");
							}
						}
						if (this.tempophantom == 0) {
							jogador.sendMessage(String.valueOf(Manager.prefix) + " �cVoce N\u00e3o Pode Mais Voar.");
							jogador.setAllowFlight(false);
							jogador.getInventory()
									.setArmorContents(Habilidades.armaduras.get(jogador.getName()));
							jogador.updateInventory();
							this.cancel();
						}
						if (!Manager.pegarKit(jogador).equalsIgnoreCase("phantom")) {
							this.cancel();
						}
					}
				}.runTaskTimer(FantasyPvP.getPlugin(), 20L, 20L);
			} else {
				Manager.mensagemCooldown(jogador);
			}
		}
	}

	@EventHandler
	private void phantomnoclickarmadura(final InventoryClickEvent e) {
		final Player jogador = (Player) e.getWhoClicked();
		if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null
				&& e.getCurrentItem().getItemMeta().getDisplayName() != null) {
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("�8Armadura Fantasma")
					&& Manager.pegarKit(jogador).equalsIgnoreCase("phantom")) {
				e.setCancelled(true);
				jogador.updateInventory();
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("�cDeshfire")
					&& Manager.pegarKit(jogador).equalsIgnoreCase("deshfire")) {
				e.setCancelled(true);
				jogador.updateInventory();
			}
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("�9Sonic")
					&& Manager.pegarKit(jogador).equalsIgnoreCase("sonic")) {
				e.setCancelled(true);
				jogador.updateInventory();
			}
		}
	}

	@EventHandler
	private void fireman(final EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			final Player jogador = (Player) e.getEntity();
			if ((Manager.pegarKit(jogador).equalsIgnoreCase("fireman")
					|| Manager.pegarKit(jogador).equalsIgnoreCase("magma"))
					&& (e.getCause().name().contains("FIRE") || e.getCause().name().contains("LAVA"))) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void deshfire(final PlayerInteractEvent e) {
		final Player jogador = e.getPlayer();
		if (e.getAction().name().contains("RIGHT") && jogador.getItemInHand().getType() == Material.REDSTONE_BLOCK
				&& Manager.pegarKit(jogador).equalsIgnoreCase("deshfire")) {
			e.setCancelled(true);
			jogador.updateInventory();
			if (Manager.naoTemCooldown(jogador)) {
				Habilidades.armaduras.put(jogador.getName(), jogador.getInventory().getArmorContents());
				Habilidades.Particulas.add(jogador);
				Manager.adicionarCooldown(jogador, 20);
				new BukkitRunnable() {
					public void run() {
						Habilidades.Cancel.add(jogador);
					}
				}.runTaskLater(FantasyPvP.getPlugin(), 20L);
				jogador.getInventory().setHelmet(Manager.setarCor(Material.LEATHER_HELMET, Color.RED, "�cDeshfire"));
				jogador.getInventory()
						.setChestplate(Manager.setarCor(Material.LEATHER_CHESTPLATE, Color.RED, "�cDeshfire"));
				jogador.getInventory()
						.setLeggings(Manager.setarCor(Material.LEATHER_LEGGINGS, Color.RED, "�cDeshfire"));
				jogador.getInventory().setBoots(Manager.setarCor(Material.LEATHER_BOOTS, Color.RED, "�cDeshfire"));
				jogador.updateInventory();
				for (final Entity pertos : jogador.getNearbyEntities(10.0, 10.0, 10.0)) {
					if (pertos instanceof Player) {
						final Player jogadores = (Player) pertos;
						jogadores.setFireTicks(100);
					}
				}
				jogador.setVelocity(jogador.getEyeLocation().getDirection().multiply(3).add(new Vector(0, 1, 0)));
				new BukkitRunnable() {
					public void run() {
						if (Habilidades.Particulas.contains(jogador)) {
							Habilidades.Particulas.remove(jogador);
							Habilidades.Cancel.remove(jogador);
							jogador.getInventory()
									.setArmorContents(Habilidades.armaduras.get(jogador.getName()));
							jogador.updateInventory();
						}
					}
				}.runTaskLater(FantasyPvP.getPlugin(), 200L);
			} else {
				Manager.mensagemCooldown(jogador);
			}
		}
	}

	@EventHandler
	private void sonic(final PlayerInteractEvent e) {
		final Player jogador = e.getPlayer();
		if (e.getAction().name().contains("RIGHT") && jogador.getItemInHand().getType() == Material.LAPIS_BLOCK
				&& Manager.pegarKit(jogador).equalsIgnoreCase("sonic")) {
			e.setCancelled(true);
			jogador.updateInventory();
			if (Manager.naoTemCooldown(jogador)) {
				Habilidades.armaduras.put(jogador.getName(), jogador.getInventory().getArmorContents());
				Habilidades.Particulas.add(jogador);
				Manager.adicionarCooldown(jogador, 20);
				new BukkitRunnable() {
					public void run() {
						Habilidades.Cancel.add(jogador);
					}
				}.runTaskLater(FantasyPvP.getPlugin(), 20L);
				jogador.getInventory().setHelmet(Manager.setarCor(Material.LEATHER_HELMET, Color.BLUE, "�9Sonic"));
				jogador.getInventory()
						.setChestplate(Manager.setarCor(Material.LEATHER_CHESTPLATE, Color.BLUE, "�9Sonic"));
				jogador.getInventory().setLeggings(Manager.setarCor(Material.LEATHER_LEGGINGS, Color.BLUE, "�9Sonic"));
				jogador.getInventory().setBoots(Manager.setarCor(Material.LEATHER_BOOTS, Color.BLUE, "�9Sonic"));
				jogador.updateInventory();
				jogador.setVelocity(jogador.getEyeLocation().getDirection().multiply(3).add(new Vector(0, 1, 0)));
				new BukkitRunnable() {
					public void run() {
						if (Habilidades.Particulas.contains(jogador)) {
							Habilidades.Particulas.remove(jogador);
							Habilidades.Cancel.remove(jogador);
							jogador.getInventory()
									.setArmorContents(Habilidades.armaduras.get(jogador.getName()));
							jogador.updateInventory();
						}
					}
				}.runTaskLater(FantasyPvP.getPlugin(), 200L);
			} else {
				Manager.mensagemCooldown(jogador);
			}
		}
	}

	@EventHandler
	private void moverparticulas(final PlayerMoveEvent e) {
		final Player jogador = e.getPlayer();
		if ((Manager.pegarKit(jogador).equalsIgnoreCase("deshfire")
				|| Manager.pegarKit(jogador).equalsIgnoreCase("sonic")) && Habilidades.Cancel.contains(jogador)
				&& jogador.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
			Habilidades.Particulas.remove(jogador);
			jogador.getInventory().setArmorContents(Habilidades.armaduras.get(jogador.getName()));
			jogador.updateInventory();
		}
	}

	@EventHandler
	private void particulas(final PlayerMoveEvent e) {
		final Player jogador = e.getPlayer();
		if (Manager.pegarKit(jogador).equalsIgnoreCase("deshfire") && Habilidades.Particulas.contains(jogador)) {
			for (final Entity pertos : jogador.getNearbyEntities(10.0, 10.0, 10.0)) {
				if (pertos instanceof Player) {
					final Player jogadores = (Player) pertos;
					jogadores.setFireTicks(100);
				}
			}
			jogador.getWorld().playEffect(jogador.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
			jogador.getWorld().playEffect(jogador.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
		}
		if (Manager.pegarKit(jogador).equalsIgnoreCase("sonic") && Habilidades.Particulas.contains(jogador)) {
			jogador.getWorld().playEffect(jogador.getLocation(), Effect.WATERDRIP, 10);
			for (final Entity pertos : jogador.getNearbyEntities(10.0, 10.0, 10.0)) {
				if (pertos instanceof Player) {
					final Player jogadores = (Player) pertos;
					Manager.adicionarEfeito(jogadores, 100, 0, PotionEffectType.POISON);
				}
			}
		}
	}

	@EventHandler
	private void specialist(final PlayerInteractEvent e) {
		final Player jogador = e.getPlayer();
		if (e.getAction().name().contains("RIGHT") && Manager.pegarKit(jogador).equalsIgnoreCase("specialist")
				&& jogador.getItemInHand().getType() == Material.BOOK) {
			jogador.openEnchanting(jogador.getWorld().getSpawnLocation(), true);
			jogador.updateInventory();
		}
	}

	@EventHandler
	private void specialistmorrer(final PlayerDeathEvent e) {
		final Player vitima = e.getEntity();
		final Player jogador = vitima.getKiller();
		if (vitima.getKiller() != null && Manager.pegarKit(jogador).equalsIgnoreCase("specialist")) {
			jogador.setLevel(jogador.getLevel() + 1);
		}
	}

	@EventHandler
	private void turtle(final EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			final Player jogador = (Player) e.getEntity();
			if (Manager.pegarKit(jogador).equalsIgnoreCase("turtle") && jogador.isSneaking()) {
				e.setDamage(1.0);
			}
		}
	}

	@EventHandler
	private void turtlebater(final EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			final Player jogador = (Player) e.getDamager();
			if (Manager.pegarKit(jogador).equalsIgnoreCase("turtle") && jogador.isSneaking()) {
				jogador.sendMessage(
						String.valueOf(Manager.prefix) + " �cVoc\u00ea N\u00e3o Pode Bater Enquanto Estiver No Shift.");
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void stomper(final EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			final Player jogador = (Player) e.getEntity();
			if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)
					&& Manager.pegarKit(jogador).equalsIgnoreCase("stomper")) {
				if (e.getDamage() <= 4.0) {
					e.setCancelled(true);
					return;
				}
				for (final Entity jogadorespertos : jogador.getNearbyEntities(4.0, 2.0, 4.0)) {
					if (jogadorespertos instanceof Player) {
						final Player jogadores = (Player) jogadorespertos;
						if (jogadores.isSneaking()) {
							jogadores.damage(3.5, jogador);
							jogador.getWorld().playSound(jogador.getLocation(), Sound.ANVIL_LAND, 1.0f, -1.0f);
						} else {
							jogadores.damage(e.getDamage(), jogador);
							jogador.getWorld().playSound(jogador.getLocation(), Sound.ANVIL_LAND, 1.0f, -1.0f);
						}
					}
				}
				e.setDamage(4.0);
			}
		}
	}

	@EventHandler
	private void headshot(final EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			final Player jogador = (Player) e.getDamager();
			if (Manager.pegarKit(jogador).equalsIgnoreCase("headshot")
					&& String.valueOf(jogador.getEyeLocation().getPitch()).contains("-")) {
				e.setDamage(e.getDamage() + 2.0);
				jogador.getWorld().playSound(jogador.getLocation(), Sound.VILLAGER_HIT, 2.0f, 2.0f);
				jogador.getWorld().playEffect(e.getEntity().getLocation().add(0.0, 1.0, 0.0), Effect.STEP_SOUND,
						(Object) Material.REDSTONE_BLOCK, 10);
			}
		}
	}

	@EventHandler
	private void flash(final PlayerInteractEvent e) {
		final Player jogador = e.getPlayer();
		if (e.getAction().name().contains("RIGHT") && jogador.getItemInHand().getType() == Material.REDSTONE_BLOCK
				&& Manager.pegarKit(jogador).equalsIgnoreCase("flash")) {
			e.setCancelled(true);
			jogador.updateInventory();
			if (Manager.naoTemCooldown(jogador)) {
				Habilidades.armaduras.put(jogador.getName(), jogador.getInventory().getArmorContents());
				Manager.adicionarCooldown(jogador, 20);
				Manager.adicionarEfeito(jogador, 200, 2, PotionEffectType.SPEED);
				jogador.getInventory().setHelmet(Manager.setarCor(Material.LEATHER_HELMET, Color.RED, "�4Flash"));
				jogador.getInventory()
						.setChestplate(Manager.setarCor(Material.LEATHER_CHESTPLATE, Color.RED, "�4Flash"));
				jogador.getInventory().setLeggings(Manager.setarCor(Material.LEATHER_LEGGINGS, Color.RED, "�4Flash"));
				jogador.getInventory().setBoots(Manager.setarCor(Material.LEATHER_BOOTS, Color.RED, "�4Flash"));
				jogador.updateInventory();
				for (final Entity pertos : jogador.getNearbyEntities(10.0, 10.0, 10.0)) {
					if (pertos instanceof Player) {
						final Player jogadores = (Player) pertos;
						Habilidades.Congelar.add(jogadores.getName());
						new BukkitRunnable() {
							public void run() {
								Habilidades.Congelar.remove(jogadores.getName());
							}
						}.runTaskLater(FantasyPvP.getPlugin(), 200L);
					}
				}
				new BukkitRunnable() {
					public void run() {
						Habilidades.Congelar.remove(jogador.getName());
						jogador.getInventory()
								.setArmorContents(Habilidades.armaduras.get(jogador.getName()));
						jogador.updateInventory();
					}
				}.runTaskLater(FantasyPvP.getPlugin(), 200L);
			} else {
				Manager.mensagemCooldown(jogador);
			}
		}
	}

	@EventHandler
	private void flashmove(final EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			final Player p = (Player) e.getDamager();
			final Player p2 = (Player) e.getEntity();
			if (Habilidades.Congelar.contains(p2.getName())) {
				final int slot = new Random().nextInt(this.sendThroughInventory ? 36 : 9);
				ItemStack replaced = p2.getInventory().getItemInHand();
				if (replaced == null) {
					replaced = new ItemStack(0);
				}
				ItemStack replacer = p2.getInventory().getItem(slot);
				if (replacer == null) {
					replacer = new ItemStack(0);
				}
				p2.getInventory().setItemInHand(replacer);
				p2.getInventory().setItem(slot, replaced);
				e.setCancelled(true);
			}
			if (Habilidades.Congelar.contains(p.getName())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	private void flashnaomover(final PlayerMoveEvent event) {
		final Player p = event.getPlayer();
		if (Habilidades.Congelar.contains(p.getName())) {
			event.setTo(p.getLocation());
		}
	}

	@EventHandler
	private void draigfogo(final PlayerInteractEvent e) {
		final Player jogador = e.getPlayer();
		if (e.getAction().name().contains("LEFT") && jogador.getItemInHand().getType() == Material.BLAZE_POWDER
				&& Manager.pegarKit(jogador).equalsIgnoreCase("draig")) {
			e.setCancelled(true);
			jogador.updateInventory();
			if (Manager.naoTemCooldown(jogador)) {
				Manager.adicionarCooldown(jogador, 20);
				jogador.updateInventory();
				final Location location = jogador.getEyeLocation();
				final BlockIterator blocksToAdd = new BlockIterator(location, 0.0, 40);
				while (blocksToAdd.hasNext()) {
					final Location blockToAdd = blocksToAdd.next().getLocation();
					jogador.getWorld().playEffect(blockToAdd, Effect.MOBSPAWNER_FLAMES, 20);
					jogador.getWorld().playEffect(blockToAdd, Effect.MOBSPAWNER_FLAMES, 20);
					jogador.getWorld().playEffect(blockToAdd, Effect.MOBSPAWNER_FLAMES, 20);
					jogador.getWorld().playEffect(blockToAdd, Effect.MOBSPAWNER_FLAMES, 20);
				}
				final Snowball h = (Snowball) jogador.launchProjectile(Snowball.class);
				final Vector velo1 = jogador.getLocation().getDirection().normalize().multiply(10);
				h.setVelocity(velo1);
				h.setMetadata("Draig", new FixedMetadataValue(FantasyPvP.getPlugin(), true));
			} else {
				Manager.mensagemCooldown(jogador);
			}
		}
		if (e.getAction().name().contains("RIGHT") && jogador.getItemInHand().getType() == Material.BLAZE_POWDER
				&& Manager.pegarKit(jogador).equalsIgnoreCase("draig")) {
			e.setCancelled(true);
			jogador.updateInventory();
			if (Manager.naoTemCooldown(jogador)) {
				Manager.adicionarCooldown(jogador, 20);
				new BukkitRunnable() {
					int tempodraig = 10;

					public void run() {
						--this.tempodraig;
						jogador.setAllowFlight(true);
						if (this.tempodraig > 0) {
							if (this.tempodraig == 1) {
								jogador.sendMessage(String.valueOf(Manager.prefix) + " �aVoce Tem �f" + this.tempodraig
										+ " Segundo �aDe V\u00f4o.");
							} else {
								jogador.sendMessage(String.valueOf(Manager.prefix) + " �aVoce Tem �f" + this.tempodraig
										+ " Segundos �aDe V\u00f4o.");
							}
						}
						if (this.tempodraig == 0) {
							jogador.sendMessage(String.valueOf(Manager.prefix) + " �cVoce N\u00e3o Pode Mais Voar.");
							jogador.setAllowFlight(false);
							this.cancel();
						}
						if (!Manager.pegarKit(jogador).equalsIgnoreCase("draig")) {
							this.cancel();
						}
					}
				}.runTaskTimer(FantasyPvP.getPlugin(), 20L, 20L);
			} else {
				Manager.mensagemCooldown(jogador);
			}
		}
	}

	@EventHandler
	private void draigdano(final EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Snowball) {
			final Snowball s = (Snowball) e.getDamager();
			if (s.hasMetadata("Draig")) {
				e.setDamage(e.getDamage() + 2.0);
				e.getEntity().setFireTicks(100);
			}
		}
	}

	@EventHandler
	private void thresh(final PlayerInteractEvent e) {
		final Player jogador = e.getPlayer();
		if (e.getAction().name().contains("RIGHT") && jogador.getItemInHand().getType() == Material.LEASH
				&& Manager.pegarKit(jogador).equalsIgnoreCase("thresh")) {
			e.setCancelled(true);
			jogador.updateInventory();
			if (Manager.naoTemCooldown(jogador)) {
				Manager.adicionarCooldown(jogador, 20);
				final Snowball snow = (Snowball) jogador.launchProjectile(Snowball.class);
				final EntitySnowball ball = ((CraftSnowball) snow).getHandle();
				final Vector v = jogador.getLocation().getDirection().normalize().multiply(1000);
				final PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { ball.getId() });
				snow.setVelocity(v);
				for (final Player p2 : Bukkit.getOnlinePlayers()) {
					((CraftPlayer) p2).getHandle().playerConnection.sendPacket(packet);
				}
				jogador.getWorld().playEffect(jogador.getLocation(), Effect.SMOKE, 20);
				jogador.getWorld().playEffect(jogador.getLocation(), Effect.SMOKE, 20);
				jogador.getWorld().playEffect(jogador.getLocation(), Effect.SMOKE, 20);
				jogador.getWorld().playEffect(jogador.getLocation(), Effect.SMOKE, 20);
				jogador.getWorld().playEffect(jogador.getLocation(), Effect.SMOKE, 20);
				jogador.getWorld().playEffect(jogador.getLocation(), Effect.SMOKE, 20);
				jogador.getWorld().playEffect(jogador.getLocation(), Effect.SMOKE, 20);
				jogador.getWorld().playEffect(jogador.getLocation(), Effect.SMOKE, 20);
				jogador.sendMessage(String.valueOf(Manager.prefix) + " �aVoce Lan\u00e7ou Sua Corda Thresh.");
				snow.setMetadata("thresh",
						new FixedMetadataValue(FantasyPvP.getPlugin(), true));
			} else {
				Manager.mensagemCooldown(jogador);
			}
		}
	}

	@EventHandler
	private void threshdano(final EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Snowball) {
			final Snowball s = (Snowball) e.getDamager();
			final Entity p = e.getEntity();
			final Player t = (Player) s.getShooter();
			if (s.hasMetadata("thresh")) {
				e.setDamage(4.0);
				p.teleport(t.getLocation());
				p.getWorld().playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 20);
			}
		}
	}

	@EventHandler
	private void bloquear(final PlayerCommandPreprocessEvent event) {
		final Player p = event.getPlayer();
		if (Habilidades.fighting.containsKey(p.getName()) && event.getMessage().toLowerCase().startsWith("/")) {
			event.setCancelled(true);
			p.sendMessage(
					String.valueOf(Manager.prefix) + " �cDesculpe, Mais E Este Comando Esta Bloqueado No Gladiator.");
		}
	}

	@EventHandler
	public void openInventoryEvent(final InventoryOpenEvent e) {
		final Dye d = new Dye();
		d.setColor(DyeColor.BLUE);
		final ItemStack lapis = d.toItemStack();
		lapis.setAmount(3);
		if (e.getInventory() instanceof EnchantingInventory) {
			e.getInventory().setItem(1, lapis);
			FantasyPvP.getMain().inventories.add((EnchantingInventory) e.getInventory());
		}
	}

	@EventHandler
	public void closeInventoryEvent(final InventoryCloseEvent e) {
		if (e.getInventory() instanceof EnchantingInventory
				&& FantasyPvP.getMain().inventories.contains(e.getInventory())) {
			e.getInventory().setItem(1, (ItemStack) null);
			FantasyPvP.getMain().inventories.remove(e.getInventory());
		}
	}

	@EventHandler
	public void inventoryClickEvent(final InventoryClickEvent e) {
		if (e.getClickedInventory() instanceof EnchantingInventory
				&& FantasyPvP.getMain().inventories.contains(e.getInventory()) && e.getSlot() == 1) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void enchantItemEvent(final EnchantItemEvent e) {
		final Dye d = new Dye();
		d.setColor(DyeColor.BLUE);
		final ItemStack lapis = d.toItemStack();
		lapis.setAmount(3);
		if (FantasyPvP.getMain().inventories.contains(e.getInventory())) {
			e.getInventory().setItem(1, lapis);
		}
	}

	@EventHandler
	public void hadouken(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& Manager.pegarKit(p).equalsIgnoreCase("Sniper")
				&& p.getItemInHand().getType() == Material.BLAZE_ROD) {
			if (Manager.naoTemCooldown(p)) {
				e.setCancelled(true);
				p.updateInventory();
				final Location location = p.getEyeLocation();
				final BlockIterator blocksToAdd = new BlockIterator(location, 0.0, 40);
				while (blocksToAdd.hasNext()) {
					final Location blockToAdd = blocksToAdd.next().getLocation();
					p.getWorld().playEffect(blockToAdd, Effect.SMOKE, 20);
					p.playSound(blockToAdd, Sound.SKELETON_DEATH, 3.0f, 3.0f);
					p.getLocation().getWorld().playEffect(p.getLocation(), Effect.BLAZE_SHOOT, 10000, 10000);
				}
				final Arrow h = (Arrow) p.launchProjectile(Arrow.class);
				final Vector velo1 = p.getLocation().getDirection().normalize().multiply(30);
				h.setVelocity(velo1);
				p.removePotionEffect(PotionEffectType.SLOW);
				Habilidades.Zoom.remove(p);
				h.setMetadata("sniper", new FixedMetadataValue(FantasyPvP.getPlugin(), true));
				Manager.adicionarCooldown(p, 10);
			} else {
				Manager.mensagemCooldown(p);
			}
		}
		if ((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)
				&& Manager.pegarKit(p).equalsIgnoreCase("Sniper") && p.getItemInHand().getType() == Material.BLAZE_ROD
				&& !Habilidades.Zoom.contains(p)) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 999999999, 999));
			Habilidades.Zoom.add(p);
			p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 2.0f, 2.0f);
		} else if ((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)
				&& Manager.pegarKit(p).equalsIgnoreCase("Sniper") && p.getItemInHand().getType() == Material.BLAZE_ROD
				&& Habilidades.Zoom.contains(p)) {
			p.removePotionEffect(PotionEffectType.SLOW);
			p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 2.0f, 2.0f);
			Habilidades.Zoom.remove(p);
		}
	}

	@EventHandler
	public void dano(final EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
			final Arrow s = (Arrow) e.getDamager();
			final Player p = (Player) e.getEntity();
			if (s.hasMetadata("sniper")) {
				p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, (Object) Material.REDSTONE_BLOCK, 2);
				e.setDamage(10.0);
			}
		}
	}
}
