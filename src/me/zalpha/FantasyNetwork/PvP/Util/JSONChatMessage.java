package me.zalpha.FantasyNetwork.PvP.Util;

import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

@SuppressWarnings("unchecked")
public class JSONChatMessage {
	private JSONObject chatObject;

	public JSONChatMessage(final String text, final JSONChatColor color, final List<JSONChatFormat> formats) {
		(this.chatObject = new JSONObject()).put("text", text);
		if (color != null) {
			this.chatObject.put("color", color.getColorString());
		}
		if (formats != null) {
			for (final JSONChatFormat format : formats) {
				this.chatObject.put(format.getFormatString(), true);
			}
		}
	}

	public void addExtra(final JSONChatExtra extraObject) {
		if (!this.chatObject.containsKey("extra")) {
			this.chatObject.put("extra", new JSONArray());
		}
		final JSONArray extra = (JSONArray) this.chatObject.get("extra");
		extra.add(extraObject.toJSON());
		this.chatObject.put("extra", extra);
	}

	public void sendToPlayer(final Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(
				new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(this.chatObject.toJSONString())));
	}

	@Override
	public String toString() {
		return this.chatObject.toJSONString();
	}
}
