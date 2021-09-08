package com.zalpha.FantasyNetwork.PvP.util;

import org.bukkit.configuration.file.YamlConfiguration;
import java.io.IOException;
import org.bukkit.Bukkit;
import com.zalpha.FantasyNetwork.PvP.FantasyKits;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigAPI
{
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
        this.Mensagens = new File(FantasyKits.getPlugin().getDataFolder(), "Mensagens.yml");
        this.Cords = new File(FantasyKits.getPlugin().getDataFolder(), "Cords.yml");
    }
    
    public static ConfigAPI pegarConfig() {
        return ConfigAPI.plugin;
    }
    
    public void carregarMensagens() {
        if (!this.Mensagens.exists()) {
            FantasyKits.getPlugin().saveResource("Mensagens.yml", true);
        }
        this.configDeMensagens = new File(FantasyKits.getPlugin().getDataFolder(), "Mensagens.yml");
        if (!this.configDeMensagens.exists()) {
            try {
                this.configDeMensagens.createNewFile();
            }
            catch (IOException e) {
                Bukkit.getLogger().info("FantasyKitsAPI - N\u00e3o Foi Possivel Criar O Arquivo Mensagens.yml");
                e.printStackTrace();
            }
        }
        ConfigAPI.configMensagens = (FileConfiguration)YamlConfiguration.loadConfiguration(this.configDeMensagens);
    }
    
    public FileConfiguration pegarconfigMensagens() {
        return ConfigAPI.configMensagens;
    }
    
    public void salvarMensagens() {
        try {
            ConfigAPI.configMensagens.save(this.configDeMensagens);
        }
        catch (IOException e) {
            Bukkit.getLogger().info("FantasyKitsAPI - N\u00e3o Foi Possivel Salvar O Arquivo Mensagens.yml");
            e.printStackTrace();
        }
    }
    
    public void carregarCords() {
        if (!this.Cords.exists()) {
            FantasyKits.getPlugin().saveResource("Cords.yml", true);
        }
        this.configDeCords = new File(FantasyKits.getPlugin().getDataFolder(), "Cords.yml");
        if (!this.configDeCords.exists()) {
            try {
                this.configDeCords.createNewFile();
            }
            catch (IOException e) {
                Bukkit.getLogger().info("FantasyKitsAPI - N\u00e3o Foi Possivel Criar O Arquivo Cords.yml");
                e.printStackTrace();
            }
        }
        ConfigAPI.configCords = (FileConfiguration)YamlConfiguration.loadConfiguration(this.configDeCords);
    }
    
    public FileConfiguration pegarconfigCords() {
        return ConfigAPI.configCords;
    }
    
    public void salvarCords() {
        try {
            ConfigAPI.configCords.save(this.configDeCords);
        }
        catch (IOException e) {
            Bukkit.getLogger().info("FantasyKitsAPI - N\u00e3o Foi Possivel Salvar O Arquivo Cords.yml");
            e.printStackTrace();
        }
    }
}
