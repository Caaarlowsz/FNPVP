package me.zalpha.FantasyNetwork.PvP.Util;

public enum JSONChatHoverEventType
{
    SHOW_TEXT("SHOW_TEXT", 0, "show_text"), 
    SHOW_ITEM("SHOW_ITEM", 1, "show_item"), 
    SHOW_ACHIEVEMENT("SHOW_ACHIEVEMENT", 2, "show_achievement");
    
    private final String type;
    
    private JSONChatHoverEventType(final String s, final int n, final String type) {
        this.type = type;
    }
    
    public String getTypeString() {
        return this.type;
    }
}
