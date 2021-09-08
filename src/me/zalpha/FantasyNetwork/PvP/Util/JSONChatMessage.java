package me.zalpha.FantasyNetwork.PvP.Util;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONObject;

public class JSONChatMessage
{
    private JSONObject chatObject;
    
    public JSONChatMessage(final String text, final JSONChatColor color, final List<JSONChatFormat> formats) {
        (this.chatObject = new JSONObject()).put((Object)"text", (Object)text);
        if (color != null) {
            this.chatObject.put((Object)"color", (Object)color.getColorString());
        }
        if (formats != null) {
            for (final JSONChatFormat format : formats) {
                this.chatObject.put((Object)format.getFormatString(), (Object)true);
            }
        }
    }
    
    public void addExtra(final JSONChatExtra extraObject) {
        if (!this.chatObject.containsKey((Object)"extra")) {
            this.chatObject.put((Object)"extra", (Object)new JSONArray());
        }
        final JSONArray extra = (JSONArray)this.chatObject.get((Object)"extra");
        extra.add((Object)extraObject.toJSON());
        this.chatObject.put((Object)"extra", (Object)extra);
    }
    
    public void sendToPlayer(final Player player) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(this.chatObject.toJSONString())));
    }
    
    @Override
    public String toString() {
        return this.chatObject.toJSONString();
    }
}
