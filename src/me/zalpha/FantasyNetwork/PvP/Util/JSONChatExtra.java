package me.zalpha.FantasyNetwork.PvP.Util;

import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class JSONChatExtra {
	private JSONObject chatExtra;

	public JSONChatExtra(final String text) {
		(this.chatExtra = new JSONObject()).put("text", text);
	}

	public void setClickEvent(final JSONChatClickEventType action, final String value) {
		final JSONObject clickEvent = new JSONObject();
		clickEvent.put("action", action.getTypeString());
		clickEvent.put("value", value);
		this.chatExtra.put("clickEvent", clickEvent);
	}

	public void setHoverEvent(final JSONChatHoverEventType action, final String value) {
		final JSONObject hoverEvent = new JSONObject();
		hoverEvent.put("action", action.getTypeString());
		hoverEvent.put("value", value);
		this.chatExtra.put("hoverEvent", hoverEvent);
	}

	public JSONObject toJSON() {
		return this.chatExtra;
	}
}
