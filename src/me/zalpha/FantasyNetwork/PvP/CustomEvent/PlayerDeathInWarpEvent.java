package me.zalpha.FantasyNetwork.PvP.CustomEvent;

import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerDeathInWarpEvent extends PlayerEvent
{
    public static HandlerList handlers;
    private Player killer;
    private String kitName;
    private String warp;
    
    static {
        PlayerDeathInWarpEvent.handlers = new HandlerList();
    }
    
    public PlayerDeathInWarpEvent(final Player p, final Player killer, final String warpName) {
        super(p);
        if (killer != null) {
            this.killer = killer;
        }
        this.warp = warpName;
    }
    
    public String getWarp() {
        return this.warp;
    }
    
    public UUID getPlayerUUID() {
        return this.player.getUniqueId();
    }
    
    public Player getKiller() {
        return this.killer;
    }
    
    public UUID getKillerUUID() {
        return this.killer.getUniqueId();
    }
    
    public boolean hasKiller() {
        return this.killer != null;
    }
    
    public String getKitName() {
        return this.kitName;
    }
    
    public HandlerList getHandlers() {
        return PlayerDeathInWarpEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return PlayerDeathInWarpEvent.handlers;
    }
}
