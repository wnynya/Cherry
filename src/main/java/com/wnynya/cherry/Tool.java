package com.wnynya.cherry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.UUID;

public class Tool {

	/* 포멧 변환 */
	public static String reFormatEventMsg(PlayerJoinEvent event, String msg) {
		Player player = event.getPlayer();
		msg = Tool.n2s(Tool.reFormatEventMsgPlayer(player, msg));
		return msg;
	}
	public static String reFormatEventMsg(PlayerQuitEvent event, String msg) {
		Player player = event.getPlayer();
		msg = Tool.n2s(Tool.reFormatEventMsgPlayer(player, msg));
		return msg;
	}
	public static String reFormatEventMsg(AsyncPlayerChatEvent event, String msg) {
		Player player = event.getPlayer();
		msg = Tool.n2s(Tool.reFormatEventMsgPlayer(player, msg));
		if (Cherry.getPlugin().getConfig().getBoolean("chat.effect.enable")) {
			msg = msg.replace(FormatHolder.MESSAGE_PLACEHOLDER, Tool.n2s(event.getMessage()));
			msg = msg.replace(FormatHolder.MESSAGE_PLACEHOLDER_ALIAS, Tool.n2s(event.getMessage()));
		}
		else {
			msg = msg.replace(FormatHolder.MESSAGE_PLACEHOLDER, event.getMessage());
			msg = msg.replace(FormatHolder.MESSAGE_PLACEHOLDER_ALIAS, event.getMessage());
		}
		return msg;
	}

	private static String reFormatEventMsgPlayer(Player player, String msg) {
		msg = msg.replace(FormatHolder.PREFIX_PLACEHOLDER, Vault.getPrefix(player));
		msg = msg.replace(FormatHolder.SUFFIX_PLACEHOLDER, Vault.getSuffix(player));
		msg = msg.replace(FormatHolder.NAME_PLACEHOLDER, player.getName());
		msg = msg.replace(FormatHolder.DISPLAYNAME_PLACEHOLDER, player.getDisplayName());
		msg = msg.replace(FormatHolder.FANCYNAME_PLACEHOLDER, Tool.getFancyName(player));

		msg = msg.replace(FormatHolder.PREFIX_PLACEHOLDER_ALIAS, Vault.getPrefix(player));
		msg = msg.replace(FormatHolder.SUFFIX_PLACEHOLDER_ALIAS, Vault.getSuffix(player));
		msg = msg.replace(FormatHolder.NAME_PLACEHOLDER_ALIAS, player.getName());
		msg = msg.replace(FormatHolder.DISPLAYNAME_PLACEHOLDER_ALIAS, player.getDisplayName());
		msg = msg.replace(FormatHolder.FANCYNAME_PLACEHOLDER_ALIAS, Tool.getFancyName(player));
		return msg;
	}

	/* & 을 § 로 */
	public static String n2s(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	/* § 을 ANSI ESCAPE 로 */
	public static String s2a(String string) {
		string = string.replaceAll("§0", "\u001b[30m");
		string = string.replaceAll("§4", "\u001b[31m");
		string = string.replaceAll("§2", "\u001b[32m");
		string = string.replaceAll("§6", "\u001b[33m");
		string = string.replaceAll("§1", "\u001b[34m");
		string = string.replaceAll("§5", "\u001b[35m");
		string = string.replaceAll("§3", "\u001b[36m");
		string = string.replaceAll("§7", "\u001b[37m");
		string = string.replaceAll("§8", "\u001b[90m");
		string = string.replaceAll("§c", "\u001b[91m");
		string = string.replaceAll("§a", "\u001b[92m");
		string = string.replaceAll("§e", "\u001b[93m");
		string = string.replaceAll("§9", "\u001b[94m");
		string = string.replaceAll("§d", "\u001b[95m");
		string = string.replaceAll("§b", "\u001b[96m");
		string = string.replaceAll("§f", "\u001b[97m");
		string = string.replaceAll("§l", "\u001b[1m");
		string = string.replaceAll("§m", "\u001b[2m");
		string = string.replaceAll("§n", "\u001b[4m");
		string = string.replaceAll("§o", "\u001b[3m");
		string = string.replaceAll("§r", "\u001b[0m");
		string = string.replaceAll("§k", "\u001b[5m");
		return string;
	}

	/* 접두사 접미사 붙은 이름 get */
	public static String getFancyName(Player player) {

		String prefix, suffix;
		if (Cherry.getPlugin().getConfig().getBoolean("chat.vault.enable")) {
			prefix = Vault.getPrefix(player);
			suffix = Vault.getSuffix(player);
		} else {
			prefix = "";
			suffix = "";
		}
		String playerName = player.getDisplayName();

		String playerFancyName = Tool.n2s("&r" + prefix + playerName + suffix + "&r");

		return playerFancyName;

	}

	public static String loc2Str(Location loc) {
		String x = (int) loc.getX() + "";
		String y = (int) loc.getY() + "";
		String z = (int) loc.getZ() + "";
		return "X: " + x + ", Y: " + y + ", Z: " + z;
	}

	public static String loc2Str2(Location loc) {
		String x = (int) loc.getX() + "";
		String y = (int) loc.getY() + "";
		String z = (int) loc.getZ() + "";
		return x + ", " + y + ", " + z;
	}

	public static String loc2StrWithWorld(Location loc) {
		String w = loc.getWorld().getName();
		String x = (int) loc.getX() + "";
		String y = (int) loc.getY() + "";
		String z = (int) loc.getZ() + "";
		return "W:" + w + " / X:" + x + ", Y:" + y + ", Z:" + z;
	}

	public class Event {
		public static final String PLAYERJOIN = "Player Join";
		public static final String PLAYERQUIT = "Player Quit";
		public static final String PLAYERCHAT = "Player Chat";
		public static final String COMMAND = "Command";
		public static final String SERVERLISTPING = "Server Ping";
	}

	private static class FormatHolder {
		static final String NAME_PLACEHOLDER = "{name}";
		static final String NAME_PLACEHOLDER_ALIAS = "{n}";
		static final String DISPLAYNAME_PLACEHOLDER = "{displayname}";
		static final String DISPLAYNAME_PLACEHOLDER_ALIAS = "{dn}";
		static final String FANCYNAME_PLACEHOLDER = "{fancyname}";
		static final String FANCYNAME_PLACEHOLDER_ALIAS = "{fn}";
		static final String MESSAGE_PLACEHOLDER = "{message}";
		static final String MESSAGE_PLACEHOLDER_ALIAS = "{msg}";
		static final String PREFIX_PLACEHOLDER = "{prefix}";
		static final String PREFIX_PLACEHOLDER_ALIAS = "{pf}";
		static final String SUFFIX_PLACEHOLDER = "{suffix}";
		static final String SUFFIX_PLACEHOLDER_ALIAS = "{sf}";
	}
	/*
	 * 인게임 접두사 public static String prefix(String type) { if (type == "i") { return
	 * "§5§l[W]: §r§7"; } else if (type == "e") { return "§c§l[!]: §r§7"; } else if
	 * (type == "ecmd") { return "§c§l[!]: §r§7명령어 사용 권한이 없습니다."; } else if (type ==
	 * "ecsl") { return "§c§l[!]: §r§7콘솔에서 사용 불가능한 명령어입니다."; } else if (type ==
	 * "csld") { return "[DEBUG]: "; } else { return "§e§l[WNYK]: §r§f"; } }
	 */

	public static class Check {
		public static boolean isMaterial(String material) {
			try {
				Material m = Material.valueOf(material.toUpperCase());
				return true;
			}
			catch (Exception e) {
				return false;
			}
		}

		public static boolean isInteger(String str) {
			if (str.matches("[+-]?[0-9]+"))
			{
				return true;
			}
			return false;
		}

		public static boolean isNatural(String str) {
			if (str.matches("[+]?[0-9]+"))
			{
				return true;
			}
			return false;
		}
	}

	public static class Math {
		public static int max(int[] array) {
			if (array == null || array.length == 0) {
				return 0;
			}
			int max = array[0];
			for (int value : array) {
				if (max < value) {
					max = value;
				}
			}
			return max;
		}

		public static int min(int[] array) {
			if (array == null || array.length == 0) {
				return 0;
			}
			int min = array[0];
			for (int value : array) {
				if (min > value) {
					min = value;
				}
			}
			return min;
		}

		public static double max(double[] array) {
			if (array == null || array.length == 0) {
				return 0;
			}
			double max = array[0];
			for (double value : array) {
				if (max < value) {
					max = value;
				}
			}
			return max;
		}

		public static double min(double[] array) {
			if (array == null || array.length == 0) {
				return 0;
			}
			double min = array[0];
			for (double value : array) {
				if (min > value) {
					min = value;
				}
			}
			return min;
		}

		public static float max(float[] array) {
			if (array == null || array.length == 0) {
				return 0;
			}
			float max = array[0];
			for (float value : array) {
				if (max < value) {
					max = value;
				}
			}
			return max;
		}

	}

	public static class List {
		public static java.util.List<String> materials() {
			java.util.List<String> list = new ArrayList<>();
			for (Material material : Material.values()) {
				list.add(material.toString());
			}
			return list;
		}

		public static java.util.List<String> materialBlocks() {
			java.util.List<String> list = new ArrayList<>();
			for (Material material : Material.values()) {
				if (material.isBlock()) {
					list.add(material.toString());
				}
			}
			return list;
		}

		public static java.util.List<String> materialItems() {
			java.util.List<String> list = new ArrayList<>();
			for (Material material : Material.values()) {
				if (material.isItem()) {
					list.add(material.toString());
				}
			}
			return list;
		}

		public static java.util.List<String> playerNames() {
			java.util.List<String> list = new ArrayList<>();
			for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
				list.add(player.getName());
			}
			return list;
		}

		public static java.util.List<String> playerNames(String prefix) {
			java.util.List<String> list = new ArrayList<>();
			for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
				list.add(prefix + player.getName());
			}
			return list;
		}

		public static java.util.List<UUID> playerUUIDs() {
			java.util.List<UUID> list = new ArrayList<>();
			for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
				list.add(player.getUniqueId());
			}
			return list;
		}

		public static java.util.List<String> playerUUIDStrings() {
			java.util.List<String> list = new ArrayList<>();
			for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
				list.add(player.getUniqueId().toString());
			}
			return list;
		}

		public static java.util.List<String> playerUUIDStrings(String prefix) {
			java.util.List<String> list = new ArrayList<>();
			for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
				list.add(prefix + player.getUniqueId().toString());
			}
			return list;
		}

	}

}
