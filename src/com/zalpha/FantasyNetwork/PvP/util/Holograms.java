package com.zalpha.FantasyNetwork.PvP.util;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import org.bukkit.Bukkit;
import java.lang.reflect.Constructor;
import org.bukkit.Location;
import java.util.UUID;
import java.util.List;

public class Holograms
{
    private List<Object> destroyCache;
    private List<Object> spawnCache;
    private List<UUID> players;
    private List<String> lines;
    private Location loc;
    private static final double ABS = 0.23;
    private static String path;
    private static String version;
    private static Class<?> armorStand;
    private static Class<?> worldClass;
    private static Class<?> nmsEntity;
    private static Class<?> craftWorld;
    private static Class<?> packetClass;
    private static Class<?> entityLivingClass;
    private static Constructor<?> armorStandConstructor;
    private static Class<?> destroyPacketClass;
    private static Constructor<?> destroyPacketConstructor;
    private static Class<?> nmsPacket;
    
    static {
        Holograms.path = Bukkit.getServer().getClass().getPackage().getName();
        Holograms.version = Holograms.path.substring(Holograms.path.lastIndexOf(".") + 1, Holograms.path.length());
        try {
            Holograms.armorStand = Class.forName("net.minecraft.server." + Holograms.version + ".EntityArmorStand");
            Holograms.worldClass = Class.forName("net.minecraft.server." + Holograms.version + ".World");
            Holograms.nmsEntity = Class.forName("net.minecraft.server." + Holograms.version + ".Entity");
            Holograms.craftWorld = Class.forName("org.bukkit.craftbukkit." + Holograms.version + ".CraftWorld");
            Holograms.packetClass = Class.forName("net.minecraft.server." + Holograms.version + ".PacketPlayOutSpawnEntityLiving");
            Holograms.entityLivingClass = Class.forName("net.minecraft.server." + Holograms.version + ".EntityLiving");
            Holograms.armorStandConstructor = Holograms.armorStand.getConstructor(Holograms.worldClass);
            Holograms.destroyPacketClass = Class.forName("net.minecraft.server." + Holograms.version + ".PacketPlayOutEntityDestroy");
            Holograms.destroyPacketConstructor = Holograms.destroyPacketClass.getConstructor(int[].class);
            Holograms.nmsPacket = Class.forName("net.minecraft.server." + Holograms.version + ".Packet");
        }
        catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex3) {
            final Exception ex2;
            final Exception ex = ex2;
            System.err.println("Error - Classes not initialized!");
            ex.printStackTrace();
        }
    }
    
    public Holograms(final Location loc, final String... lines) {
        this(loc, Arrays.asList(lines));
    }
    
    public Holograms(final Location loc, final List<String> lines) {
        this.lines = lines;
        this.loc = loc;
        this.players = new ArrayList<UUID>();
        this.spawnCache = new ArrayList<Object>();
        this.destroyCache = new ArrayList<Object>();
        final Location displayLoc = loc.clone().add(0.0, 0.23 * lines.size() - 1.97, 0.0);
        for (int i = 0; i < lines.size(); ++i) {
            final Object packet = this.getPacket(this.loc.getWorld(), displayLoc.getX(), displayLoc.getY(), displayLoc.getZ(), this.lines.get(i).replace("&", "§"));
            this.spawnCache.add(packet);
            try {
                final Field field = Holograms.packetClass.getDeclaredField("a");
                field.setAccessible(true);
                this.destroyCache.add(this.getDestroyPacket((int)field.get(packet)));
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            displayLoc.add(0.0, -0.23, 0.0);
        }
    }
    
    public boolean display(final Player p) {
        for (int i = 0; i < this.spawnCache.size(); ++i) {
            this.sendPacket(p, this.spawnCache.get(i));
        }
        if (!this.players.contains(p.getUniqueId())) {
            this.players.add(p.getUniqueId());
        }
        return true;
    }
    
    public boolean destroy(final Player p) {
        if (this.players.contains(p.getUniqueId())) {
            for (int i = 0; i < this.destroyCache.size(); ++i) {
                this.sendPacket(p, this.destroyCache.get(i));
            }
            this.players.remove(p.getUniqueId());
            return true;
        }
        return false;
    }
    
    private Object getPacket(final World w, final double x, final double y, final double z, final String text) {
        try {
            final Object craftWorldObj = Holograms.craftWorld.cast(w);
            final Method getHandleMethod = craftWorldObj.getClass().getMethod("getHandle", (Class<?>[])new Class[0]);
            final Object entityObject = Holograms.armorStandConstructor.newInstance(getHandleMethod.invoke(craftWorldObj, new Object[0]));
            final Method setCustomName = entityObject.getClass().getMethod("setCustomName", String.class);
            setCustomName.invoke(entityObject, text);
            final Method setCustomNameVisible = Holograms.nmsEntity.getMethod("setCustomNameVisible", Boolean.TYPE);
            setCustomNameVisible.invoke(entityObject, !text.equals("§7"));
            final Method setGravity = entityObject.getClass().getMethod("setGravity", Boolean.TYPE);
            setGravity.invoke(entityObject, false);
            final Method setLocation = entityObject.getClass().getMethod("setLocation", Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE);
            setLocation.invoke(entityObject, x, y, z, 0.0f, 0.0f);
            final Method setInvisible = entityObject.getClass().getMethod("setInvisible", Boolean.TYPE);
            setInvisible.invoke(entityObject, true);
            final Constructor<?> cw = Holograms.packetClass.getConstructor(Holograms.entityLivingClass);
            final Object packetObject = cw.newInstance(entityObject);
            return packetObject;
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
            return null;
        }
    }
    
    private Object getDestroyPacket(final int... id) {
        try {
            return Holograms.destroyPacketConstructor.newInstance(id);
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
            return null;
        }
    }
    
    private void sendPacket(final Player p, final Object packet) {
        try {
            final Method getHandle = p.getClass().getMethod("getHandle", (Class<?>[])new Class[0]);
            final Object entityPlayer = getHandle.invoke(p, new Object[0]);
            final Object pConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
            final Method sendMethod = pConnection.getClass().getMethod("sendPacket", Holograms.nmsPacket);
            sendMethod.invoke(pConnection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
