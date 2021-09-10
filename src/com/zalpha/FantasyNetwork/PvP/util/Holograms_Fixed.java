package com.zalpha.FantasyNetwork.PvP.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

public class Holograms_Fixed {
	private List<EntityArmorStand> entitylist;
	private List<String> Text;
	private Location location;
	private double DISTANCE;
	int count;

	public Holograms_Fixed(final Location location, final List<String> Text) {
		this.entitylist = new ArrayList<EntityArmorStand>();
		this.DISTANCE = 0.25;
		this.Text = Text;
		this.location = location;
		this.create();
	}

	public void showPlayer(final Player p) {
		for (final EntityArmorStand armor : this.entitylist) {
			final PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armor);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public void hidePlayer(final Player p) {
		for (final EntityArmorStand armor : this.entitylist) {
			final PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { armor.getId() });
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public void showAll() {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			for (final EntityArmorStand armor : this.entitylist) {
				final PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armor);
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}

	public void hideAll() {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			for (final EntityArmorStand armor : this.entitylist) {
				final PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { armor.getId() });
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}

	private void create() {
		for (final String Text : this.Text) {
			final EntityArmorStand entity = new EntityArmorStand(
					((CraftWorld) this.location.getWorld()).getHandle(), this.location.getX(),
					this.location.getY(), this.location.getZ());
			entity.setCustomName(Text);
			entity.setCustomNameVisible(true);
			entity.setInvisible(true);
			entity.setGravity(false);
			this.entitylist.add(entity);
			this.location.subtract(0.0, this.DISTANCE, 0.0);
			++this.count;
		}
		for (int i = 0; i < this.count; ++i) {
			this.location.add(0.0, this.DISTANCE, 0.0);
		}
		this.count = 0;
	}
}
