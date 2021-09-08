package com.zalpha.FantasyNetwork.PvP.util.player;

import com.zalpha.FantasyNetwork.PvP.util.Manager;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.zalpha.FantasyNetwork.PvP.FantasyKits;
import java.util.UUID;
import java.util.ArrayList;

public class PlayerHide
{
    private ArrayList<UUID> hideAllPlayers;
    
    public PlayerHide(final FantasyKits bc) {
        this.hideAllPlayers = new ArrayList<UUID>();
    }
    
    public void playerJoin(final Player p) {
        for (final UUID id : this.hideAllPlayers) {
            final Player hide = Bukkit.getPlayer(id);
            if (hide != null) {
                hide.hidePlayer(p);
            }
        }
    }
    
    public void hideAllPlayers(final Player p) {
        if (!this.hideAllPlayers.contains(p.getUniqueId())) {
            this.hideAllPlayers.add(p.getUniqueId());
        }
        for (final Player hide : Bukkit.getOnlinePlayers()) {
            if (hide.getUniqueId() != p.getUniqueId()) {
                p.hidePlayer(hide);
            }
        }
    }
    
    public void showAllPlayers(final Player p) {
        if (this.hideAllPlayers.contains(p.getUniqueId())) {
            this.hideAllPlayers.remove(p.getUniqueId());
        }
        for (final Player show : Bukkit.getOnlinePlayers()) {
            if (!this.hideAllPlayers.contains(show.getUniqueId()) && !Manager.Admin.contains(show) && show.getUniqueId() != p.getUniqueId()) {
                p.showPlayer(show);
            }
        }
    }
    
    public void showForAll(final Player p) {
    }
    
    public void hideForAll(final Player p) {
    }
    
    public boolean isHiding(final UUID id) {
        return this.hideAllPlayers.contains(id);
    }
    
    public boolean hideForAll(final UUID id) {
        return false;
    }
    
    public void stop() {
        for (final Player p : Bukkit.getOnlinePlayers()) {
            for (final Player show : Bukkit.getOnlinePlayers()) {
                p.showPlayer(show);
            }
        }
        this.hideAllPlayers.clear();
    }
    
    public void tryToRemoveFromLists(final UUID id) {
        this.hideAllPlayers.remove(id);
    }
}
