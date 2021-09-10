package me.zalpha.FantasyNetwork.PvP.Util;

public enum JSONChatColor {
	WHITE("WHITE", 0, "white"), YELLOW("YELLOW", 1, "yellow"), LIGHT_PURPLE("LIGHT_PURPLE", 2, "light_purple"),
	RED("RED", 3, "red"), AQUA("AQUA", 4, "aqua"), GREEN("GREEN", 5, "green"), BLUE("BLUE", 6, "blue"),
	DARK_GRAY("DARK_GRAY", 7, "dark_gray"), GRAY("GRAY", 8, "gray"), GOLD("GOLD", 9, "gold"),
	DARK_PURPLE("DARK_PURPLE", 10, "dark_purple"), DARK_RED("DARK_RED", 11, "dark_red"),
	DARK_AQUA("DARK_AQUA", 12, "dark_aqua"), DARK_GREEN("DARK_GREEN", 13, "dark_green"),
	DARK_BLUE("DARK_BLUE", 14, "dark_blue"), BLACK("BLACK", 15, "black");

	private final String color;

	private JSONChatColor(final String s, final int n, final String color) {
		this.color = color;
	}

	String getColorString() {
		return this.color;
	}
}
