package com.zalpha.FantasyNetwork.PvP.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreBoard {
	public static String[] cutUnranked(final String[] content) {
		final String[] elements = Arrays.copyOf(content, 16);
		if (elements[0] == null) {
			elements[0] = "Unamed board";
		}
		if (elements[0].length() > 32) {
			elements[0] = elements[0].substring(0, 32);
		}
		for (int i = 1; i < elements.length; ++i) {
			if (elements[i] != null && elements[i].length() > 40) {
				elements[i] = elements[i].substring(0, 40);
			}
		}
		return elements;
	}

	public static String cutRankedTitle(final String title) {
		if (title == null) {
			return "Unamed board";
		}
		if (title.length() > 32) {
			return title.substring(0, 32);
		}
		return title;
	}

	public static HashMap<String, Integer> cutRanked(final HashMap<String, Integer> content) {
		final HashMap<String, Integer> elements = new HashMap<String, Integer>();
		elements.putAll(content);
		while (elements.size() > 15) {
			String minimumKey = (String) elements.keySet().toArray()[0];
			int minimum = elements.get(minimumKey);
			for (final String string : elements.keySet()) {
				if (elements.get(string) >= minimum) {
					if (elements.get(string) != minimum) {
						continue;
					}
					if (string.compareTo(minimumKey) >= 0) {
						continue;
					}
				}
				minimumKey = string;
				minimum = elements.get(string);
			}
			elements.remove(minimumKey);
		}
		for (final String s : elements.keySet()) {
			if (s != null) {
				if (s.length() <= 40) {
					continue;
				}
				final int value = elements.get(s);
				elements.remove(s);
				elements.put(s.substring(0, 40), value);
			}
		}
		return elements;
	}

	public static boolean unrankedSidebarDisplay(final Player p, String[] elements) {
		elements = cutUnranked(elements);
		try {
			if (p.getScoreboard() == null || p.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard()
					|| p.getScoreboard().getObjectives().size() != 1) {
				p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			}
			if (p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16)) == null) {
				p.getScoreboard().registerNewObjective(p.getUniqueId().toString().substring(0, 16), "dummy");
				p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16))
						.setDisplaySlot(DisplaySlot.SIDEBAR);
			}
			p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(elements[0]);
			for (int i = 1; i < elements.length; ++i) {
				if (elements[i] != null && p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(elements[i])
						.getScore() != 16 - i) {
					p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(elements[i]).setScore(16 - i);
					for (final Object string : p.getScoreboard().getEntries()) {
						if (p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16))
								.getScore((String) string).getScore() == 16 - i) {
							if (string.equals(elements[i])) {
								continue;
							}
							p.getScoreboard().resetScores((String) string);
						}
					}
				}
			}
			for (final String entry : p.getScoreboard().getEntries()) {
				boolean toErase = true;
				for (final String element : elements) {
					if (element != null && element.equals(entry)
							&& p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16))
									.getScore(entry).getScore() == 16 - Arrays.asList(elements).indexOf(element)) {
						toErase = false;
						break;
					}
				}
				if (!toErase) {
					continue;
				}
				p.getScoreboard().resetScores(entry);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean unrankedSidebarDisplay(final Collection<Player> players, final String[] elements) {
		for (final Player player : players) {
			if (unrankedSidebarDisplay(player, elements)) {
				continue;
			}
			return false;
		}
		return true;
	}

	public static boolean unrankedSidebarDisplay(final Collection<Player> players, String[] elements,
			Scoreboard board) {
		try {
			final String objName = "COLLAB-SB-WINTER";
			if (board == null) {
				board = Bukkit.getScoreboardManager().getNewScoreboard();
			}
			elements = cutUnranked(elements);
			for (final Player player : players) {
				if (player.getScoreboard() == board) {
					continue;
				}
				player.setScoreboard(board);
			}
			if (board.getObjective(objName) == null) {
				board.registerNewObjective(objName, "dummy");
				board.getObjective(objName).setDisplaySlot(DisplaySlot.SIDEBAR);
			}
			board.getObjective(DisplaySlot.SIDEBAR).setDisplayName(elements[0]);
			for (int i = 1; i < elements.length; ++i) {
				if (elements[i] != null
						&& board.getObjective(DisplaySlot.SIDEBAR).getScore(elements[i]).getScore() != 16 - i) {
					board.getObjective(DisplaySlot.SIDEBAR).getScore(elements[i]).setScore(16 - i);
					for (final Object string : board.getEntries()) {
						if (board.getObjective(objName).getScore((String) string).getScore() == 16 - i) {
							if (string.equals(elements[i])) {
								continue;
							}
							board.resetScores((String) string);
						}
					}
				}
			}
			for (final String entry : board.getEntries()) {
				boolean toErase = true;
				for (final String element : elements) {
					if (element != null && element.equals(entry) && board.getObjective(objName).getScore(entry)
							.getScore() == 16 - Arrays.asList(elements).indexOf(element)) {
						toErase = false;
						break;
					}
				}
				if (!toErase) {
					continue;
				}
				board.resetScores(entry);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean rankedSidebarDisplay(final Player p, String title, HashMap<String, Integer> elements) {
		try {
			title = cutRankedTitle(title);
			elements = cutRanked(elements);
			if (p.getScoreboard() == null || p.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard()
					|| p.getScoreboard().getObjectives().size() != 1) {
				p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			}
			if (p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16)) == null) {
				p.getScoreboard().registerNewObjective(p.getUniqueId().toString().substring(0, 16), "dummy");
				p.getScoreboard().getObjective(p.getUniqueId().toString().substring(0, 16))
						.setDisplaySlot(DisplaySlot.SIDEBAR);
			}
			p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).setDisplayName(title);
			for (final String string2 : elements.keySet()) {
				if (p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(string2).getScore() == elements
						.get(string2)) {
					continue;
				}
				p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(string2)
						.setScore(elements.get(string2));
			}
			for (final String string2 : p.getScoreboard().getEntries()) {
				if (elements.keySet().contains(string2)) {
					continue;
				}
				p.getScoreboard().resetScores(string2);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean rankedSidebarDisplay(final Collection<Player> players, final String title,
			final HashMap<String, Integer> elements) {
		for (final Player player : players) {
			if (rankedSidebarDisplay(player, title, elements)) {
				continue;
			}
			return false;
		}
		return true;
	}

	public static boolean rankedSidebarDisplay(final Collection<Player> players, String title,
			HashMap<String, Integer> elements, Scoreboard board) {
		try {
			title = cutRankedTitle(title);
			elements = cutRanked(elements);
			final String objName = "COLLAB-SB-WINTER";
			if (board == null) {
				board = Bukkit.getScoreboardManager().getNewScoreboard();
			}
			for (final Player player : players) {
				if (player.getScoreboard() == board) {
					continue;
				}
				player.setScoreboard(board);
			}
			if (board.getObjective(objName) == null) {
				board.registerNewObjective(objName, "dummy");
				board.getObjective(objName).setDisplaySlot(DisplaySlot.SIDEBAR);
			}
			board.getObjective(DisplaySlot.SIDEBAR).setDisplayName(title);
			for (final String string2 : elements.keySet()) {
				if (board.getObjective(DisplaySlot.SIDEBAR).getScore(string2).getScore() == elements.get(string2)) {
					continue;
				}
				board.getObjective(DisplaySlot.SIDEBAR).getScore(string2).setScore(elements.get(string2));
			}
			for (final String string2 : board.getEntries()) {
				if (elements.keySet().contains(string2)) {
					continue;
				}
				board.resetScores(string2);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
