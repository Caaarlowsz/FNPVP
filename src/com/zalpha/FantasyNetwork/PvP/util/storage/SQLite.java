package com.zalpha.FantasyNetwork.PvP.util.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.scheduler.BukkitRunnable;

import com.zalpha.FantasyNetwork.PvP.FantasyKits;

public class SQLite implements Sql {
	private File file;
	private Connection connection;

	public File getFile() {
		return this.file;
	}

	@Override
	public Connection getConnection() {
		return this.connection;
	}

	public SQLite(final File file) {
		this.file = file;
		if (!this.file.exists()) {
			try {
				this.file.getParentFile().mkdir();
				this.file.createNewFile();
			} catch (Exception ex) {
			}
		}
		try {
			Class.forName("org.sqlite.JDBC");
			this.connection = DriverManager.getConnection("jdbc:sqlite:" + file);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.print("Falha ao pegar classe do SQLite");
		} catch (SQLException e2) {
			e2.printStackTrace();
			System.out.print("Falha ao criar conex\u00e3o no SQLite");
		}
		this.createTable();
	}

	public void createTable() {
		try {
			this.connection.createStatement().execute(
					"CREATE TABLE IF NOT EXISTS kitpvp (uuid VARCHAR(36), name VARCHAR(32), stats VARCHAR(255));");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(final String query, final boolean flag, final Object... objects) {
		try {
			final PreparedStatement ps = this.connection.prepareStatement(query);
			for (int i = 0; i < objects.length; ++i) {
				ps.setObject(i + 1, objects[i]);
			}
			if (flag) {
				this.queue(ps);
			} else {
				ps.execute();
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void queue(final PreparedStatement ps) {
		new BukkitRunnable() {
			public void run() {
				try {
					ps.execute();
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(FantasyKits.getPlugin());
	}
}
