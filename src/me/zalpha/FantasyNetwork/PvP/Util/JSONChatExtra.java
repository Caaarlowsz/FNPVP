package me.zalpha.FantasyNetwork.PvP.Util;

import org.json.simple.JSONObject;

public class JSONChatExtra
{
    private JSONObject chatExtra;
    
    public JSONChatExtra(final String text) {
        (this.chatExtra = new JSONObject()).put((Object)"text", (Object)text);
    }
    
    public void setClickEvent(final JSONChatClickEventType action, final String value) {
        final JSONObject clickEvent = new JSONObject();
        clickEvent.put((Object)"action", (Object)action.getTypeString());
        clickEvent.put((Object)"value", (Object)value);
        this.chatExtra.put((Object)"clickEvent", (Object)clickEvent);
    }
    
    public void setHoverEvent(final JSONChatHoverEventType action, final String value) {
        final JSONObject hoverEvent = new JSONObject();
        hoverEvent.put((Object)"action", (Object)action.getTypeString());
        hoverEvent.put((Object)"value", (Object)value);
        this.chatExtra.put((Object)"hoverEvent", (Object)hoverEvent);
    }
    
    public JSONObject toJSON() {
        return this.chatExtra;
    }
}
