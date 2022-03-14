package com.zalpha.FantasyNetwork.PvP.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.caaarlowsz.fantasymc.kitpvp.FantasyPvP;

public class ConfigAPI {
	public static FileConfiguration configMensagens;
	public static FileConfiguration configCords;
	private static ConfigAPI plugin;
	File configDeMensagens;
	File configDeCords;
	File Mensagens;
	File Cords;

	static {
		ConfigAPI.plugin = new ConfigAPI();
	}

	public ConfigAPI() {
		this.Mensagens = new File(FantasyPvP.getPlugin().getDataFolder(), "Mensagens.yml");
		this.Cords = new File(FantasyPvP.getPlugin().getDataFolder(), "Cords.yml");
	}

	public static ConfigAPI pegarConfig() {
		return ConfigAPI.plugin;
	}

	public void carregarMensagens() {
		if (!this.Mensagens.exists()) {
			FantasyPvP.getPlugin().saveResource("Mensagens.yml", true);
		}
		this.configDeMensagens = new File(FantasyPvP.getPlugin().getDataFolder(), "Mensagens.yml");
		if (!this.configDeMensagens.exists()) {
			try {
				this.configDeMensagens.createNewFile();
			} catch (IOException e) {
				Bukkit.getLogger().info("FantasyKitsAPI - N\u00e3o Foi Possivel Criar O Arquivo Mensagens.yml");
				e.printStackTrace();
			}
		}
		ConfigAPI.configMensagens = YamlConfiguration.loadConfiguration(this.configDeMensagens);
	}

	public FileConfiguration pegarconfigMensagens() {
		return ConfigAPI.configMensagens;
	}

	public void salvarMensagens() {
		try {
			ConfigAPI.configMensagens.save(this.configDeMensagens);
		} catch (IOException e) {
			Bukkit.getLogger().info("FantasyKitsAPI - N\u00e3o Foi Possivel Salvar O Arquivo Mensagens.yml");
			e.printStackTrace();
		}
	}

	public void carregarCords() {
		if (!this.Cords.exists()) {
			FantasyPvP.getPlugin().saveResource("Cords.yml", true);
		}
		this.configDeCords = new File(FantasyPvP.getPlugin().getDataFolder(), "Cords.yml");
		if (!this.configDeCords.exists()) {
			try {
				this.configDeCords.createNewFile();
			} catch (IOException e) {
				Bukkit.getLogger().info("FantasyKitsAPI - N\u00e3o Foi Possivel Criar O Arquivo Cords.yml");
				e.printStackTrace();
			}
		}
		ConfigAPI.configCords = YamlConfiguration.loadConfiguration(this.configDeCords);
	}

	public FileConfiguration pegarconfigCords() {
		return ConfigAPI.configCords;
	}

	public void salvarCords() {
		try {
			ConfigAPI.configCords.save(this.configDeCords);
		} catch (IOException e) {
			Bukkit.getLogger().info("FantasyKitsAPI - N\u00e3o Foi Possivel Salvar O Arquivo Cords.yml");
			e.printStackTrace();
		}
	}
}
