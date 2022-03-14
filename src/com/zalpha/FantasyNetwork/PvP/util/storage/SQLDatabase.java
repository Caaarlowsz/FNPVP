package com.zalpha.FantasyNetwork.PvP.util.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.caaarlowsz.fantasymc.kitpvp.FantasyPvP;

public class SQLDatabase {
	private Player player;
	private Connection connection;

	public Player getPlayer() {
		return this.player;
	}

	public Connection getConnection() {
		return this.connection;
	}

	public SQLDatabase(final FantasyPvP plugin, final Player p) {
		this.player = p;
		this.connection = plugin.getStorage().getConnection();
		try {
			PreparedStatement ps = this.connection.prepareStatement("SELECT name FROM kitpvp WHERE uuid = ?");
			ps.setString(1, p.getUniqueId().toString());
			final ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				ps.close();
				final String sql = "INSERT INTO kitpvp (uuid, name, stats)";
				ps = this.connection.prepareStatement(String.valueOf(sql) + " VALUES (?, ?, ?)");
				ps.setString(1, p.getUniqueId().toString());
				ps.setString(2, p.getName());
				ps.setString(3, "Nenhum : 0 : 0 : 0 : 0 : 0 : 0 : 0");
				ps.executeUpdate();
				ps.close();
			} else if (!this.getString("name").equals(p.getName())) {
				this.set("name", p.getName());
			}
			rs.close();
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage("�aFailed to create account using sql:");
			e.printStackTrace();
		}
	}

	public boolean set(final String path, final Object value) {
		return false;
	}

	public Object get(final String path) {
		Object obj = null;
		try {
			final PreparedStatement ps = this.connection
					.prepareStatement("SELECT " + path + " FROM kitpvp WHERE uuid = ?");
			ps.setString(1, this.player.getUniqueId().toString());
			final ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return obj;
			}
			obj = rs.getObject(path);
			ps.close();
			rs.close();
			return obj;
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage("�aFailed to get value in sql:");
			e.printStackTrace();
			return obj;
		}
	}

	public int getInt(final String path) {
		int number = 0;
		try {
			final PreparedStatement ps = this.connection
					.prepareStatement("SELECT " + path + " FROM kitpvp WHERE uuid = ?");
			ps.setString(1, this.player.getUniqueId().toString());
			final ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return number;
			}
			number = rs.getInt(path);
			ps.close();
			rs.close();
			return number;
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage("�aFailed to get int in sql:");
			e.printStackTrace();
			return number;
		}
	}

	public long getLong(final String path) {
		long number = 0L;
		try {
			final PreparedStatement ps = this.connection
					.prepareStatement("SELECT " + path + " FROM kitpvp WHERE uuid = ?");
			ps.setString(1, this.player.getUniqueId().toString());
			final ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return number;
			}
			number = rs.getLong(path);
			ps.close();
			rs.close();
			return number;
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage("�aFailed to get long in sql:");
			e.printStackTrace();
			return number;
		}
	}

	public String getString(final String path) {
		String str = null;
		try {
			final PreparedStatement ps = this.connection
					.prepareStatement("SELECT " + path + " FROM kitpvp WHERE uuid = ?");
			ps.setString(1, this.player.getUniqueId().toString());
			final ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return str;
			}
			str = rs.getString(path);
			ps.close();
			rs.close();
			return str;
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage("�aFailed to get string in sql:");
			e.printStackTrace();
			return str;
		}
	}
}
