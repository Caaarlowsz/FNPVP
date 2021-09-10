package me.zalpha.FantasyNetwork.PvP.Util;

public enum JSONChatFormat {
	BOLD("BOLD", 0, "bold"), UNDERLINED("UNDERLINED", 1, "underlined"),
	STRIKETHROUGH("STRIKETHROUGH", 2, "strikethrough"), ITALIC("ITALIC", 3, "italic"),
	OBFUSCATED("OBFUSCATED", 4, "obfuscated");

	private final String format;

	private JSONChatFormat(final String s, final int n, final String format) {
		this.format = format;
	}

	public String getFormatString() {
		return this.format;
	}
}
