package com.zalpha.FantasyNetwork.PvP.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;

import com.github.caaarlowsz.fantasymc.kitpvp.FantasyPvP;

public class TopKills {
	public static ArrayList<Location> locations;
	public static ArrayList<ArmorStand> armors;
	public static ArrayList<Holograms_Fixed> holograms;

	static {
		TopKills.locations = new ArrayList<Location>();
		TopKills.armors = new ArrayList<ArmorStand>();
		TopKills.holograms = new ArrayList<Holograms_Fixed>();
	}

	public static void destroyStands() {
		for (final ArmorStand stand : TopKills.armors) {
			stand.remove();
		}
		Bukkit.getConsoleSender()
				.sendMessage("[TopKills - Fantasy] Foram removidos " + TopKills.armors.size() + " stands.");
	}

	public static Location getLocation(final String name) {
		final String[] sp = ConfigAPI.pegarConfig().pegarconfigMensagens().getString("Top." + name).split(",");
		final Location loc = new Location(Bukkit.getWorld(sp[0]), Double.parseDouble(sp[1]), Double.parseDouble(sp[2]),
				Double.parseDouble(sp[3]));
		loc.setYaw(Float.parseFloat(sp[4]));
		loc.setPitch(Float.parseFloat(sp[5]));
		return loc;
	}

	public static List<String> getTops() {
		try {
			final Statement st = FantasyPvP.getMain().storage.getConnection().createStatement();
			final ResultSet rs = st.executeQuery("SELECT * FROM kitpvp");
			final List<String> mysqlzao = new ArrayList<String>();
			while (rs.next()) {
				mysqlzao.add(String.valueOf(rs.getString("name")) + " : " + rs.getString("stats"));
			}
			final List<String> convert = new ArrayList<String>();
			for (final String str : mysqlzao) {
				convert.add(String.valueOf(str.split(" : ")[0]) + " : " + str.split(" : ")[3]);
			}
			Collections.sort(convert, new Comparator<String>() {
				@Override
				public int compare(final String pt1, final String pt2) {
					final int killpt1 = Integer.parseInt(pt1.split(" : ")[1]);
					final int killpt2 = Integer.parseInt(pt2.split(" : ")[1]);
					return Integer.compare(killpt1, killpt2);
				}
			});
			Collections.reverse(convert);
			if (convert.size() < 5) {
				while (convert.size() < 5) {
					convert.add("Ningu\u00e9m : 0");
				}
			}
			return convert;
		} catch (SQLException e) {
			return new ArrayList<String>();
		}
	}

	public static List<String> getTops_Lost() {
		try {
			final Statement st = FantasyPvP.getMain().storage.getConnection().createStatement();
			final ResultSet rs = st.executeQuery("SELECT * FROM `Kitpvp` ORDER BY `Kills` DESC LIMIT 5");
			final List<String> mysqlzao = new ArrayList<String>();
			while (rs.next()) {
				mysqlzao.add(String.valueOf(rs.getString("Nick")) + " : " + rs.getInt("kills"));
			}
			final List<String> convert = new ArrayList<String>();
			for (final String str : mysqlzao) {
				convert.add(String.valueOf(str.split(" : ")[0]) + " : " + str.split(" : ")[1]);
			}
			Collections.sort(convert, new Comparator<String>() {
				@Override
				public int compare(final String pt1, final String pt2) {
					final int killpt1 = Integer.parseInt(pt1.split(" : ")[1]);
					final int killpt2 = Integer.parseInt(pt2.split(" : ")[1]);
					return Integer.compare(killpt1, killpt2);
				}
			});
			Collections.reverse(convert);
			if (convert.size() < 5) {
				while (convert.size() < 5) {
					convert.add("Ningu\u00e9m : 0");
				}
			}
			return convert;
		} catch (SQLException e) {
			return new ArrayList<String>();
		}
	}

	public static void registerLists() {
		TopKills.locations.add(getLocation("T1"));
		TopKills.locations.add(getLocation("T2"));
		TopKills.locations.add(getLocation("T3"));
		TopKills.locations.add(getLocation("T4"));
		TopKills.locations.add(getLocation("T5"));
		final ArmorStand a1 = (ArmorStand) TopKills.locations.get(0).getWorld().spawn(TopKills.locations.get(0),
				ArmorStand.class);
		final ArmorStand a2 = (ArmorStand) TopKills.locations.get(1).getWorld().spawn(TopKills.locations.get(1),
				ArmorStand.class);
		final ArmorStand a3 = (ArmorStand) TopKills.locations.get(2).getWorld().spawn(TopKills.locations.get(2),
				ArmorStand.class);
		final ArmorStand a4 = (ArmorStand) TopKills.locations.get(2).getWorld().spawn(TopKills.locations.get(3),
				ArmorStand.class);
		final ArmorStand a5 = (ArmorStand) TopKills.locations.get(2).getWorld().spawn(TopKills.locations.get(4),
				ArmorStand.class);
		TopKills.armors.add(a1);
		TopKills.armors.add(a2);
		TopKills.armors.add(a3);
		TopKills.armors.add(a4);
		TopKills.armors.add(a5);
		final ArrayList<String> Hologram = new ArrayList<String>();
		for (final String s : ConfigAPI.pegarConfig().pegarconfigMensagens().getStringList("Top.Hologram")) {
			Hologram.add(s.replace("\u00c2", "").replace("&", "�"));
		}
		TopKills.holograms.add(new Holograms_Fixed(getLocation("Hologram_Cord"), Hologram));
	}

	public static void registerPlacements() {
		final List<String> lists = getTops();
		for (int x = 0; x < 5; ++x) {
			final int id = x + 1;
			TopKills.armors.get(x)
					.setHelmet(Manager.criarItemSkull("", new String[0], lists.get(x).split(" : ")[0], 1));
			String placement = "";
			if (id == 1) {
				placement = "�b�lANCESTRAL";
			}
			if (id == 2) {
				placement = "�f�lIMPERIAL";
			}
			if (id == 3) {
				placement = "�6�lLEND\u00c1RIO";
			}
			if (id == 4) {
				placement = "�7�lMESTRE";
			}
			if (id == 5) {
				placement = "�3�lAMADOR";
			}
			final ArrayList<String> Hologram_Top = new ArrayList<String>();
			for (final String s : ConfigAPI.pegarConfig().pegarconfigMensagens().getStringList("Top.Hologram_Top")) {
				Hologram_Top.add(s.replace("<id>", new StringBuilder(String.valueOf(placement)).toString()).replace(
						"<kills>",
						new StringBuilder(
								String.valueOf(Manager.modificarCoins(Integer.parseInt(lists.get(x).split(" : ")[1]))))
										.toString())
						.replace("<nick>", new StringBuilder(String.valueOf(lists.get(x).split(" : ")[0])).toString())
						.replace("\u00c2", "").replace("&", "�"));
			}
			TopKills.holograms.add(new Holograms_Fixed(TopKills.locations.get(x).add(0.0, 0.8, 0.0), Hologram_Top));
			TopKills.armors.get(x).setBasePlate(false);
			TopKills.armors.get(x).setRemoveWhenFarAway(false);
			TopKills.armors.get(x).setSmall(true);
			TopKills.armors.get(x).setArms(true);
			TopKills.armors.get(x).setGravity(false);
			TopKills.armors.get(x).setVisible(true);
			TopKills.armors.get(x).setMarker(false);
			TopKills.armors.get(x).setCustomName("�7" + id + "� " + lists.get(x).split(" - ")[0]);
			TopKills.armors.get(x).setCustomNameVisible(false);
			if (id == 5) {
				TopKills.armors.get(x).setChestplate(
						Manager.criarItem2(Material.LEATHER_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x)
						.setLeggings(Manager.criarItem2(Material.LEATHER_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x)
						.setBoots(Manager.criarItem2(Material.LEATHER_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x)
						.setItemInHand(Manager.criarItem2(Material.WOOD_SWORD, "", (short) 0, 1, new String[0]));
			} else if (id == 4) {
				TopKills.armors.get(x).setChestplate(
						Manager.criarItem2(Material.CHAINMAIL_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x).setLeggings(
						Manager.criarItem2(Material.CHAINMAIL_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x)
						.setBoots(Manager.criarItem2(Material.CHAINMAIL_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x)
						.setItemInHand(Manager.criarItem2(Material.STONE_SWORD, "", (short) 0, 1, new String[0]));
			} else if (id == 3) {
				TopKills.armors.get(x)
						.setChestplate(Manager.criarItem2(Material.GOLD_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x)
						.setLeggings(Manager.criarItem2(Material.GOLD_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x)
						.setBoots(Manager.criarItem2(Material.GOLD_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x)
						.setItemInHand(Manager.criarItem2(Material.GOLD_SWORD, "", (short) 0, 1, new String[0]));
			} else if (id == 2) {
				TopKills.armors.get(x)
						.setChestplate(Manager.criarItem2(Material.IRON_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x)
						.setLeggings(Manager.criarItem2(Material.IRON_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x)
						.setBoots(Manager.criarItem2(Material.IRON_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x)
						.setItemInHand(Manager.criarItem2(Material.IRON_SWORD, "", (short) 0, 1, new String[0]));
			} else if (id == 1) {
				TopKills.armors.get(x).setChestplate(
						Manager.criarItem2(Material.DIAMOND_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x)
						.setLeggings(Manager.criarItem2(Material.DIAMOND_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x)
						.setBoots(Manager.criarItem2(Material.DIAMOND_CHESTPLATE, "", (short) 0, 1, new String[0]));
				TopKills.armors.get(x)
						.setItemInHand(Manager.criarItem2(Material.DIAMOND_SWORD, "", (short) 0, 1, new String[0]));
			}
		}
	}

	public static long convert(final int value, final TimeUnit unit) {
		if (unit == TimeUnit.SECONDS) {
			return 20 * value;
		}
		if (unit == TimeUnit.MINUTES) {
			return 1200 * value;
		}
		if (unit == TimeUnit.HOURS) {
			return 72000 * value;
		}
		if (unit == TimeUnit.DAYS) {
			return 1728000 * value;
		}
		return 0L;
	}

	public static void createTop() {
		registerLists();
		registerPlacements();
	}
}
