package com.wnynya.cherry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Msg {

  public static boolean enabled = false;

  // Player

  /**
   * 플레이어에게 일반 메시지를 보냅니다.
   *
   * @param player 보여줄 플레이어
   * @param msg    글자들
   */
  public static void info(org.bukkit.entity.Player player, String msg) {
    player.sendMessage("§f" + msg);
  }

  /**
   * 플레이어에게 경고 메시지를 보냅니다.
   *
   * @param player 보여줄 플레이어
   * @param msg    글자들
   */
  public static void warn(org.bukkit.entity.Player player, String msg) {
    msg = Msg.Prefix.WARN + msg;
    player.sendMessage("§e" + msg);
  }

  /**
   * 플레이어에게 에러 메시지를 보냅니다.
   *
   * @param player 보여줄 플레이어
   * @param msg    글자들
   */
  public static void error(org.bukkit.entity.Player player, String msg) {
    msg = Msg.Prefix.ERROR + msg;
    player.sendMessage("§c" + msg);
  }

  // Console

  /**
   * 콘솔에 일반 메시지를 출력합니다.
   *
   * @param msg 글자들
   */
  public static void info(String msg) {

    if (msg.length() >= Prefix.INFO.length() && (msg.substring(0, Prefix.INFO.length()).equals(Prefix.INFO) || msg.substring(0, Prefix.INFO.length()).equals(Msg.n2s(Prefix.INFO)))) {
      msg = msg.substring(Prefix.INFO.length());
    }
    if (msg.length() >= Prefix.CHERRY.length() && (msg.substring(0, Prefix.CHERRY.length()).equals(Prefix.CHERRY) || msg.substring(0, Prefix.CHERRY.length()).equals(Msg.n2s(Prefix.CHERRY)))) {
      msg = msg.substring(Prefix.CHERRY.length());
    }
    if (msg.length() >= Prefix.WAND.length() && (msg.substring(0, Prefix.WAND.length()).equals(Prefix.WAND) || msg.substring(0, Prefix.WAND.length()).equals(Msg.n2s(Prefix.WAND)))) {
      msg = msg.substring(Prefix.WAND.length());
    }
    if (msg.length() >= Prefix.PORTAL.length() && (msg.substring(0, Prefix.PORTAL.length()).equals(Prefix.PORTAL) || msg.substring(0, Prefix.PORTAL.length()).equals(Msg.n2s(Prefix.PORTAL)))) {
      msg = msg.substring(Prefix.PORTAL.length());
    }

    msg = Msg.Console.INFO + msg;
    Bukkit.getServer().getConsoleSender().sendMessage(Msg.n2s("&r[Cherry] &r" + msg));
  }

  /**
   * 콘솔에 경고 메시지를 출력합니다.
   *
   * @param msg 글자들
   */
  public static void warn(String msg) {
    msg = Msg.Console.WARN + msg;
    Bukkit.getServer().getConsoleSender().sendMessage(Msg.n2s("&e[Cherry] &e" + msg));
  }

  /**
   * 콘솔에 에러 메시지를 출력합니다.
   *
   * @param msg 글자들
   */
  public static void error(String msg) {
    msg = Msg.Console.ERROR + msg;
    Bukkit.getServer().getConsoleSender().sendMessage(Msg.n2s("&c[Cherry] &c" + msg));
  }

  // CommandSender

  /**
   * 커멘드 센더에게 일반 메시지를 보냅니다.
   *
   * @param sender 보여줄 플레이어
   * @param msg    글자들
   */
  public static void info(CommandSender sender, String msg) {
    if (sender instanceof org.bukkit.entity.Player) {
      org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
      info(player, msg);
    }
    else {
      info(msg);
    }
  }

  /**
   * 커멘드 센더에게 에러 메시지를 보냅니다.
   *
   * @param sender 보여줄 플레이어
   * @param msg    글자들
   */
  public static void warn(CommandSender sender, String msg) {
    if (sender instanceof org.bukkit.entity.Player) {
      org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
      warn(player, msg);
    }
    else {
      warn(msg);
    }
  }

  /**
   * 커멘드 센더에게 에러 메시지를 보냅니다.
   *
   * @param sender 보여줄 플레이어
   * @param msg    글자들
   */
  public static void error(CommandSender sender, String msg) {
    if (sender instanceof org.bukkit.entity.Player) {
      org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
      error(player, msg);
    }
    else {
      error(msg);
    }
  }


  public static void info(org.bukkit.entity.Player player, String prefix, String msg) {
    player.sendMessage(prefix + "§f" + msg);
  }

  public static void warn(org.bukkit.entity.Player player, String prefix, String msg) {
    msg = Msg.Prefix.WARN + msg;
    player.sendMessage(prefix + "§e" + msg);
  }

  public static void error(org.bukkit.entity.Player player, String prefix, String msg) {
    msg = Msg.Prefix.ERROR + msg;
    player.sendMessage(prefix + "§c" + msg);
  }

  public static void info(String prefix, String msg) {

    if (msg.length() >= Prefix.INFO.length() && (msg.substring(0, Prefix.INFO.length()).equals(Prefix.INFO) || msg.substring(0, Prefix.INFO.length()).equals(Msg.n2s(Prefix.INFO)))) {
      msg = msg.substring(Prefix.INFO.length());
    }
    if (msg.length() >= Prefix.CHERRY.length() && (msg.substring(0, Prefix.CHERRY.length()).equals(Prefix.CHERRY) || msg.substring(0, Prefix.CHERRY.length()).equals(Msg.n2s(Prefix.CHERRY)))) {
      msg = msg.substring(Prefix.CHERRY.length());
    }
    if (msg.length() >= Prefix.WAND.length() && (msg.substring(0, Prefix.WAND.length()).equals(Prefix.WAND) || msg.substring(0, Prefix.WAND.length()).equals(Msg.n2s(Prefix.WAND)))) {
      msg = msg.substring(Prefix.WAND.length());
    }
    if (msg.length() >= Prefix.PORTAL.length() && (msg.substring(0, Prefix.PORTAL.length()).equals(Prefix.PORTAL) || msg.substring(0, Prefix.PORTAL.length()).equals(Msg.n2s(Prefix.PORTAL)))) {
      msg = msg.substring(Prefix.PORTAL.length());
    }

    msg = Msg.Console.INFO + msg;
    Bukkit.getServer().getConsoleSender().sendMessage(Msg.n2s("&r[Cherry] &r" + prefix + msg));
  }

  public static void warn(String prefix, String msg) {
    msg = Msg.Console.WARN + msg;
    Bukkit.getServer().getConsoleSender().sendMessage(Msg.n2s("&e[Cherry] &e" + prefix + msg));
  }

  public static void error(String prefix, String msg) {
    msg = Msg.Console.ERROR + msg;
    Bukkit.getServer().getConsoleSender().sendMessage(Msg.n2s("&c[Cherry] &c" + prefix + msg));
  }

  public static void info(CommandSender sender, String prefix, String msg) {
    msg = Msg.n2s(msg);
    if (sender instanceof org.bukkit.entity.Player) {
      org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
      info(player, prefix + msg);
    }
    else {
      info(prefix + msg);
    }
  }

  public static void warn(CommandSender sender, String prefix, String msg) {
    msg = Msg.n2s(msg);
    if (sender instanceof org.bukkit.entity.Player) {
      org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
      warn(player, prefix, msg);
    }
    else {
      warn(prefix, msg);
    }
  }

  public static void error(CommandSender sender, String prefix, String msg) {
    msg = Msg.n2s(msg);
    if (sender instanceof org.bukkit.entity.Player) {
      org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
      error(player, prefix, msg);
    }
    else {
      error(prefix, msg);
    }
  }


  public static void debug(String msg) {
    if (Cherry.debug) {
      Bukkit.getServer().getConsoleSender().sendMessage(Msg.n2s("[Cherry] [Debug] " + msg));
    }
  }

  public static void debugSystemInfo() {
    Msg.debug("System Information: ");
    Msg.debug("  OS  : " + System.getProperty("os.name") + " " + System.getProperty("os.arch") + " (" + System.getProperty("os.version") + ")");
    Msg.debug("  Java: " + System.getProperty("java.version"));
  }

  /**
   * 모든 플레이어에게 메시지를 보냅니다.
   *
   * @param msg 글자들
   */
  public static void allP(String msg) {
    for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
      player.sendMessage(msg);
    }
  }

  public static void allPwO(String msg, org.bukkit.entity.Player p) {
    for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
      if (p != player) {
        player.sendMessage(msg);
      }
    }
  }

  /**
   * 모든 대상에게 메시지를 보냅니다.
   *
   * @param msg 글자들
   */
  public static void all(String msg) {
    Bukkit.broadcastMessage(msg);
  }

  private static String nmsVersion;

  /**
   * 플레이어에게 액션바 메시지를 보여줍니다.
   *
   * @param player 보여줄 플레이어
   * @param msg    글자들
   */
  public static void actionBar(org.bukkit.entity.Player player, String msg) {
    try {
      Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".entity.CraftPlayer");
      Object craftPlayer = craftPlayerClass.cast(player);
      Object packet;
      Class<?> packetPlayOutChatClass = Class.forName("net.minecraft.server." + nmsVersion + ".PacketPlayOutChat");
      Class<?> packetClass = Class.forName("net.minecraft.server." + nmsVersion + ".Packet");
      Class<?> chatComponentTextClass = Class.forName("net.minecraft.server." + nmsVersion + ".ChatComponentText");
      Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + nmsVersion + ".IChatBaseComponent");
      try {
        Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nmsVersion + ".ChatMessageType");
        Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
        Object chatMessageType = null;
        for (Object obj : chatMessageTypes) {
          if (obj.toString().equals("GAME_INFO")) {
            chatMessageType = obj;
          }
        }
        Object chatCompontentText = chatComponentTextClass.getConstructor(new Class<?>[]{String.class}).newInstance(msg);
        packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, chatMessageTypeClass}).newInstance(chatCompontentText, chatMessageType);
      }
      catch (ClassNotFoundException cnfe) {
        Object chatCompontentText = chatComponentTextClass.getConstructor(new Class<?>[]{String.class}).newInstance(msg);
        packet = packetPlayOutChatClass.getConstructor(new Class<?>[]{iChatBaseComponentClass, byte.class}).newInstance(chatCompontentText, (byte) 2);
      }
      Method craftPlayerHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle");
      Object craftPlayerHandle = craftPlayerHandleMethod.invoke(craftPlayer);
      Field playerConnectionField = craftPlayerHandle.getClass().getDeclaredField("playerConnection");
      Object playerConnection = playerConnectionField.get(craftPlayerHandle);
      Method sendPacketMethod = playerConnection.getClass().getDeclaredMethod("sendPacket", packetClass);
      sendPacketMethod.invoke(playerConnection, packet);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * & 을 마인크래프트에서 가능한 효과 문자로 대채합니다.
   *
   * @param string 변환할 문자열
   */
  public static String n2s(String string) {
    return ChatColor.translateAlternateColorCodes('&', string);
  }

  /**
   * § 을 & 으로 대채합니다.
   *
   * @param string 변환할 문자열
   */
  public static String s2n(String string) {
    return string.replaceAll("§", "&");
  }

  /**
   * § 을 터미널에서 표현 가능한 ANSI ESCAPE 문자로 대채합니다.
   *
   * @param string 변환할 문자열
   */
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

  /**
   * 한글 단어 뒤에 올 적절한 조사를 선택합니다.
   *
   * @param word  단어
   * @param type1 조사 1
   * @param type2 조사 2
   */
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
    format = Msg.n2s(format);

    format = format.replace("%", "%%");

    return format;
  }

  public static String chatFormatter(AsyncPlayerChatEvent event) {
    org.bukkit.entity.Player player = event.getPlayer();
    String msg = event.getMessage();

    String format = Cherry.config.getString("event.chat.setFormat.format");

    format = playerFormatter(player, format);

    if (Cherry.config.getBoolean("chat.chat.setFormat.effect.enable")) {
      format = format.replace(FormatHolder.MESSAGE_PLACEHOLDER, Msg.n2s(msg));
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

  public static class PlayerMSg {
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

    Prefix.INFO = Msg.n2s("§r");
    Prefix.WARN = Msg.n2s("§e");
    Prefix.ERROR = Msg.n2s("§c");

    Prefix.CHERRY = Msg.n2s("§5§l[Cherry]:§r ");
    Prefix.WAND = Msg.n2s("§6§l[W]:§r ");
    Prefix.PORTAL = Msg.n2s("§b§l[P]:§r ");

    Console.INFO = Msg.n2s("§r");
    Console.WARN = Msg.n2s("§e");
    Console.ERROR = Msg.n2s("§c");

    PlayerMSg.ONLY = "§7플레이어만 사용 가능한 명령어입니다.";

    NO_PERMISSION = "§7명령어의 사용 권한이 없습니다.";
    NO_ARGS = "§7명령어의 구성 요소가 부족합니다.";

    NO_PLAYER = "§7플레이어를 찾을 수 없습니다.";

    UNKNOWN = "§7알 수 없는 명령어입니다.";

    nmsVersion = Bukkit.getServer().getClass().getPackage().getName();
    nmsVersion = nmsVersion.substring(nmsVersion.lastIndexOf(".") + 1);

  }

  public static void enable() {
    load();
    enabled = true;
  }
}
