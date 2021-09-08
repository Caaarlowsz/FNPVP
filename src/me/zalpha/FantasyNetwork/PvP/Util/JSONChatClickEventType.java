package me.zalpha.FantasyNetwork.PvP.Util;

public enum JSONChatClickEventType
{
    RUN_COMMAND("RUN_COMMAND", 0, "run_command"), 
    SUGGEST_COMMAND("SUGGEST_COMMAND", 1, "suggest_command"), 
    OPEN_URL("OPEN_URL", 2, "open_url");
    
    private final String type;
    
    private JSONChatClickEventType(final String s, final int n, final String type) {
        this.type = type;
    }
    
    public String getTypeString() {
        return this.type;
    }
}
