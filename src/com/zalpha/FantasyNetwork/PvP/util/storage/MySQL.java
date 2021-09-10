package com.zalpha.FantasyNetwork.PvP.util.storage;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

public class MySQL implements Sql {
	private String url_base;
	private String host;
	private String name;
	private String username;
	private String password;
	private Connection connection;

	@Override
	public Connection getConnection() {
		return this.connection;
	}

	public MySQL(final String url_base, final String host, final String database, final String username,
			final String password) {
		this.host = host;
		this.name = database;
		this.username = username;
		this.password = password;
		this.url_base = url_base;
		try {
			this.connection = DriverManager.getConnection(String.valueOf(this.url_base) + this.host + "/" + this.name,
					this.username, this.password);
			this.createTable();
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage("§aFailed to create Sql Connection:");
			e.printStackTrace();
		}
	}

	public boolean deconnection() {
		if (!this.isConnected()) {
			return false;
		}
		try {
			this.connection.close();
			return true;
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage("§aFailed to deconnect sql:");
			e.printStackTrace();
			return false;
		}
	}

	public boolean isConnected() {
		try {
			return this.connection != null && !this.connection.isClosed() && this.connection.isValid(5);
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("§aFailed to get boolean connected from sql:");
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void execute(final String query, final boolean flag, final Object... objects) {
		try {
			final PreparedStatement ps = this.connection.prepareStatement(query);
			for (int i = 0; i < objects.length; ++i) {
				ps.setObject(i + 1, objects[i]);
			}
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createTable() {
		try {
			this.connection.createStatement().execute(
					"CREATE TABLE IF NOT EXISTS kitpvp (uuid VARCHAR(36), name VARCHAR(32), stats VARCHAR(255));");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean tableExists(final String table) {
		try {
			final DatabaseMetaData dbmd = this.connection.getMetaData();
			final ResultSet rs = dbmd.getTables(null, null, table, null);
			return rs.getRow() == 1;
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("§aFailed to get boolean tableExists from sql:");
			e.printStackTrace();
			return false;
		}
	}
}
