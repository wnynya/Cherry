package com.wnynya.cherry;

import com.wnynya.cherry.amethyst.Color;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Msg {

  public static boolean enabled = false;



  public static void info(org.bukkit.entity.Player player, String msg) {
    player.sendMessage("§r" + msg);
  }

  public static void warn(org.bukkit.entity.Player player, String msg) {
    msg = Msg.Prefix.WARN + msg;
    player.sendMessage("§e" + msg);
  }

  public static void error(org.bukkit.entity.Player player, String msg) {
    msg = Msg.Prefix.ERROR + msg;
    player.sendMessage("§c" + msg);
  }

  public static void info(org.bukkit.entity.Player player, String prefix, String msg) {
    player.sendMessage(prefix + "§r" + msg);
  }

  public static void warn(org.bukkit.entity.Player player, String prefix, String msg) {
    msg = Msg.Prefix.WARN + msg;
    player.sendMessage(prefix + "§e" + msg);
  }

  public static void error(org.bukkit.entity.Player player, String prefix, String msg) {
    msg = Msg.Prefix.ERROR + msg;
    player.sendMessage(prefix + "§c" + msg);
  }

  public static void allP(String msg) {
    for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
      player.sendMessage(msg);
    }
  }

  public static void allPwO(org.bukkit.entity.Player player, String msg) {
    for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
      if (player != p) {
        p.sendMessage(msg);
      }
    }
  }

  public static void actionBar(org.bukkit.entity.Player player, String msg) {
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
  }



  public static void info(String msg) {
    msg = Msg.Console.INFO + msg;
    Msg.console(Msg.effect("&r[Cherry] &r" + msg));
  }

  public static void warn(String msg) {
    msg = Msg.Console.WARN + msg;
    Msg.console(Msg.effect("&e[Cherry] &e" + msg));
  }

  public static void error(String msg) {
    msg = Msg.Console.ERROR + msg;
    Msg.console(Msg.effect("&c[Cherry] &c" + msg));
  }

  public static void info(String prefix, String msg) {

    if (msg.length() >= Prefix.INFO.length() && (msg.substring(0, Prefix.INFO.length()).equals(Prefix.INFO) || msg.substring(0, Prefix.INFO.length()).equals(Msg.effect(Prefix.INFO)))) {
      msg = msg.substring(Prefix.INFO.length());
    }
    if (msg.length() >= Prefix.CHERRY.length() && (msg.substring(0, Prefix.CHERRY.length()).equals(Prefix.CHERRY) || msg.substring(0, Prefix.CHERRY.length()).equals(Msg.effect(Prefix.CHERRY)))) {
      msg = msg.substring(Prefix.CHERRY.length());
    }
    if (msg.length() >= Prefix.WAND.length() && (msg.substring(0, Prefix.WAND.length()).equals(Prefix.WAND) || msg.substring(0, Prefix.WAND.length()).equals(Msg.effect(Prefix.WAND)))) {
      msg = msg.substring(Prefix.WAND.length());
    }
    if (msg.length() >= Prefix.PORTAL.length() && (msg.substring(0, Prefix.PORTAL.length()).equals(Prefix.PORTAL) || msg.substring(0, Prefix.PORTAL.length()).equals(Msg.effect(Prefix.PORTAL)))) {
      msg = msg.substring(Prefix.PORTAL.length());
    }

    msg = Msg.Console.INFO + msg;
    Msg.console(Msg.effect("&r[Cherry] &r" + prefix + msg));
  }

  public static void warn(String prefix, String msg) {
    msg = Msg.Console.WARN + msg;
    Msg.console(Msg.effect("&e[Cherry] &e" + prefix + msg));
  }

  public static void error(String prefix, String msg) {
    msg = Msg.Console.ERROR + msg;
    Msg.console(Msg.effect("&c[Cherry] &c" + prefix + msg));
  }

  public static void debug(String msg) {
    if (Cherry.debug) {
      Msg.console(Msg.effect("[Cherry] #D2B0DD;[Debug] &r" + msg));
    }
  }

  public static void debugSystemInfo() {
    Msg.debug("System Information: ");
    Msg.debug("  OS  : " + System.getProperty("os.name") + " " + System.getProperty("os.arch") + " (" + System.getProperty("os.version") + ")");
    Msg.debug("  Java: " + System.getProperty("java.version"));
  }

  public static void console(String string) {
    System.out.println(Color.mfc2ansi(string + "\u001b[0m"));
  }



  public static void info(CommandSender sender, String msg) {
    if (sender instanceof org.bukkit.entity.Player) {
      org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
      info(player, msg);
    }
    else {
      info(msg);
    }
  }

  public static void warn(CommandSender sender, String msg) {
    if (sender instanceof org.bukkit.entity.Player) {
      org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
      warn(player, msg);
    }
    else {
      warn(msg);
    }
  }

  public static void error(CommandSender sender, String msg) {
    if (sender instanceof org.bukkit.entity.Player) {
      org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
      error(player, msg);
    }
    else {
      error(msg);
    }
  }

  public static void info(CommandSender sender, String prefix, String msg) {
    msg = Msg.effect(msg);
    if (sender instanceof org.bukkit.entity.Player) {
      org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
      info(player, prefix + msg);
    }
    else {
      info(prefix + msg);
    }
  }

  public static void warn(CommandSender sender, String prefix, String msg) {
    msg = Msg.effect(msg);
    if (sender instanceof org.bukkit.entity.Player) {
      org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
      warn(player, prefix, msg);
    }
    else {
      warn(prefix, msg);
    }
  }

  public static void error(CommandSender sender, String prefix, String msg) {
    msg = Msg.effect(msg);
    if (sender instanceof org.bukkit.entity.Player) {
      org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
      error(player, prefix, msg);
    }
    else {
      error(prefix, msg);
    }
  }



  public static String effect(String string) {
    string = Color.and2mfc(string);
    string = Color.hex2mfc(string);
    string = Color.rgb2mfc(string);
    string = Color.cmyk2mfc(string);
    string = Color.hsl2mfc(string);
    string = Color.hsv2mfc(string);
    return string;
  }



  public static String getJosa(String word, String type1, String type2) {
    if (word == null || word.length() == 0) {
      return null;
    }

    if (hasBatchim(word)) {
      return type1;
    }
    else {
      return type2;
    }
  }

  private static boolean hasBatchim(String word) {
    char lastChar = word.charAt(word.length() - 1);

    if (lastChar < 0xAC00 || lastChar > 0xD7A3) {
      if (!(lastChar == 'a') && !(lastChar == 'i') && !(lastChar == 'u') && !(lastChar == 'e') && !(lastChar == 'o') && !(lastChar == 'y') && !(lastChar == 'w') && !(lastChar == '2') && !(lastChar == '4') && !(lastChar == '5') && !(lastChar == '9')) {
        return true;
      }
      else {
        return false;
      }
    }

    if ((lastChar - 0xAC00) % 28 > 0) {
      return true;
    }
    else {
      return false;
    }
  }



  public static String playerFormatter(org.bukkit.entity.Player player, String format) {
    format = format.replace(FormatHolder.PREFIX_PLACEHOLDER, Vault.getPrefix(player));
    format = format.replace(FormatHolder.SUFFIX_PLACEHOLDER, Vault.getSuffix(player));
    format = format.replace(FormatHolder.NAME_PLACEHOLDER, player.getName());
    format = format.replace(FormatHolder.DISPLAYNAME_PLACEHOLDER, player.getDisplayName());
    format = Msg.effect(format);

    format = format.replace("%", "%%");

    return format;
  }

  public static String chatFormatter(AsyncPlayerChatEvent event, String format) {
    org.bukkit.entity.Player player = event.getPlayer();
    String msg = event.getMessage();

    format = playerFormatter(player, format);

    if (Cherry.config.getBoolean("event.chat.effect.enable")) {
      format = format.replace(FormatHolder.MESSAGE_PLACEHOLDER, Msg.effect(msg));
    }
    else {
      format = format.replace(FormatHolder.MESSAGE_PLACEHOLDER, msg);
    }

    return format;
  }

  public static String channelChatFormatter(String msg, String playerName, String fromServer, String format) {
    format = format.replace(FormatHolder.PREFIX_PLACEHOLDER, "");
    format = format.replace(FormatHolder.SUFFIX_PLACEHOLDER,"");
    format = format.replace(FormatHolder.NAME_PLACEHOLDER, playerName);
    format = format.replace(FormatHolder.DISPLAYNAME_PLACEHOLDER, playerName);
    format = format.replace("{server}", fromServer);
    format = Msg.effect(format);

    if (Cherry.config.getBoolean("event.chat.effect.enable")) {
      format = format.replace(FormatHolder.MESSAGE_PLACEHOLDER, Msg.effect(msg));
    }
    else {
      format = format.replace(FormatHolder.MESSAGE_PLACEHOLDER, msg);
    }
    format = format.replace("%", "%%");

    return format;
  }

  private static class FormatHolder {
    static final String NAME_PLACEHOLDER = "{name}";
    static final String DISPLAYNAME_PLACEHOLDER = "{displayname}";
    static final String MESSAGE_PLACEHOLDER = "{msg}";
    static final String PREFIX_PLACEHOLDER = "{prefix}";
    static final String SUFFIX_PLACEHOLDER = "{suffix}";
  }

  public static class Player {
    public static String ONLY;
  }

  public static class Console {
    public static String INFO, WARN, ERROR;
  }

  public static class Prefix {
    public static String INFO, WARN, ERROR, CHERRY, WAND, PORTAL;
  }

  public static String NO_PERMISSION, NO_ARGS, NO_PLAYER, UNKNOWN;
  public static String BLOCK = "블록";

  public static void load() {

    Prefix.INFO = Msg.effect("§r");
    Prefix.WARN = Msg.effect("§e");
    Prefix.ERROR = Msg.effect("§c");

    Prefix.CHERRY = Msg.effect("#D2B0DD;§l[Cherry]:§r ");
    Prefix.WAND = Msg.effect("#FF8543;§l[W]:§r ");
    Prefix.PORTAL = Msg.effect("#AD56FF;§l[P]:§r ");

    Console.INFO = Msg.effect("§r");
    Console.WARN = Msg.effect("§e");
    Console.ERROR = Msg.effect("§c");

    Player.ONLY = "§7플레이어만 사용 가능한 명령어입니다.";

    NO_PERMISSION = "§7명령어의 사용 권한이 없습니다.";
    NO_ARGS = "§7명령어의 구성 요소가 부족합니다.";

    NO_PLAYER = "§7플레이어를 찾을 수 없습니다.";

    UNKNOWN = "§7알 수 없는 명령어입니다.";

    String nmsVersion = Bukkit.getServer().getClass().getPackage().getName();
    nmsVersion = nmsVersion.substring(nmsVersion.lastIndexOf(".") + 1);

  }

  public static void enable() {
    load();
    enabled = true;
  }
}
