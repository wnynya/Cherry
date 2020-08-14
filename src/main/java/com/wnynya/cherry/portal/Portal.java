package com.wnynya.cherry.portal;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Config;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.Tool;
import com.wnynya.cherry.portal.command.PortalCommand;
import com.wnynya.cherry.portal.command.PortalTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Cherry Portal
 * 좌표계 / 월드계 / 서버계(번지코드) 간 포탈
 */
public class Portal {

  public static boolean enabled = false;

  public static final String prefix = "#A48DFF;&l[PORTAL]: &r";

  private static Config portalData = new Config("portal/data");

  private static HashMap<String, Portal> portals = new HashMap<>();

  private String name;
  private String displayName;
  private PortalProtocol protocol;
  private boolean enable;

  private HashMap<String, PortalArea> areas = new HashMap<>();

  private HashMap<String, Object[]> msg = new HashMap<>();

  private Location gotoLocation;
  private String gotoServer;
  private CmdExecutor cmdExecutor;
  private String cmdMsg;

  private Path path;

  private Portal(String name) {

    this.name = name;
    this.path = new Path(this.name);

    // name
    if (!portalData.getConfig().isString(path.NAME)) {
      portalData.set(path.NAME, this.name);
    }

    // displayname
    if (!portalData.getConfig().isString(path.DISPLAY_NAME)) {
      portalData.set(path.DISPLAY_NAME, this.name);
    }
    this.displayName = portalData.getString(path.DISPLAY_NAME);

    // protocol
    if (!portalData.getConfig().isString(path.PROTOCOL)) {
      portalData.set(path.PROTOCOL, Protocol.SERVER.toString());
    }
    this.protocol = Protocol.getProtocol(portalData.getString(path.PROTOCOL));
    if (this.protocol == null) {
      this.protocol = Protocol.SERVER;
      portalData.set(path.PROTOCOL, this.protocol.toString());
    }

    // enable
    if (!portalData.getConfig().isBoolean(path.ENABLE)) {
      portalData.set(path.ENABLE, false);
    }
    this.enable = portalData.getConfig().getBoolean(path.ENABLE);

    // data
    if (!portalData.getConfig().isConfigurationSection(path.DATA_RUN_GOTO)) {
      portalData.set(path.DATA_RUN_GOTO, "");
    }
    if (!portalData.getConfig().isConfigurationSection(path.DATA_RUN_CMD)) {
      portalData.set(path.DATA_RUN_CMD, "");
    }

    this.gotoLocation = portalData.getConfig().getLocation(path.DATA_RUN_GOTO_LOCATION);
    this.gotoServer = portalData.getString(path.DATA_RUN_GOTO_SERVER);
    if (!portalData.getConfig().isString(path.DATA_RUN_CMD_EXECUTOR)) {
      portalData.set(path.DATA_RUN_CMD_EXECUTOR, CmdExecutor.PLAYER.toString());
    }
    CmdExecutor ce = CmdExecutor.getCmdExecutor(portalData.getString(path.DATA_RUN_CMD_EXECUTOR));
    if (ce == null) {
      this.cmdExecutor = CmdExecutor.PLAYER;
    }
    else {
      this.cmdExecutor = ce;
    }
    this.cmdMsg = portalData.getString(path.DATA_RUN_CMD_MSG);

    // msg
    if (!portalData.getConfig().isBoolean(path.DATA_MSG_USE_ENABLE)) {
      portalData.set(path.DATA_MSG_USE_ENABLE, true);
    }
    if (!portalData.getConfig().isString(path.DATA_MSG_USE_FORMAT)) {
      portalData.set(path.DATA_MSG_USE_FORMAT, "{displayname}&r 포탈을 사용하였습니다.");
    }
    if (!portalData.getConfig().isBoolean(path.DATA_MSG_ERROR_PERM_ENABLE)) {
      portalData.set(path.DATA_MSG_ERROR_PERM_ENABLE, true);
    }
    if (!portalData.getConfig().isString(path.DATA_MSG_ERROR_PERM_FORMAT)) {
      portalData.set(path.DATA_MSG_ERROR_PERM_FORMAT, "{displayname}&c 포탈을 사용할 권한이 없습니다.");
    }
    if (!portalData.getConfig().isBoolean(path.DATA_MSG_ERROR_DISABLED_ENABLE)) {
      portalData.set(path.DATA_MSG_ERROR_DISABLED_ENABLE, true);
    }
    if (!portalData.getConfig().isString(path.DATA_MSG_ERROR_DISABLED_FORMAT)) {
      portalData.set(path.DATA_MSG_ERROR_DISABLED_FORMAT, "&c비활성화된 포탈입니다.");
    }
    if (!portalData.getConfig().isBoolean(path.DATA_MSG_ERROR_SETTING_ENABLE)) {
      portalData.set(path.DATA_MSG_ERROR_SETTING_ENABLE, true);
    }
    if (!portalData.getConfig().isString(path.DATA_MSG_ERROR_SETTING_FORMAT)) {
      portalData.set(path.DATA_MSG_ERROR_SETTING_FORMAT, "{displayname}&c 포탈의 최소 요구 설정이 완료되지 않았습니다.");
    }

    msg.put("use", new Object[] {portalData.getBoolean(path.DATA_MSG_USE_ENABLE), portalData.getString(path.DATA_MSG_USE_FORMAT)});
    msg.put("error-perm", new Object[] {portalData.getBoolean(path.DATA_MSG_ERROR_PERM_ENABLE), portalData.getString(path.DATA_MSG_ERROR_PERM_FORMAT)});
    msg.put("error-disabled", new Object[] {portalData.getBoolean(path.DATA_MSG_ERROR_DISABLED_ENABLE), portalData.getString(path.DATA_MSG_ERROR_DISABLED_FORMAT)});
    msg.put("error-setting", new Object[] {portalData.getBoolean(path.DATA_MSG_ERROR_SETTING_ENABLE), portalData.getString(path.DATA_MSG_ERROR_SETTING_FORMAT)});

    portals.put(name, this);
  }

  private static class Path{
    private Path(String name) {
      NAME = name + ".name";
      DISPLAY_NAME = name + ".displayname";
      PROTOCOL = name + ".protocol";
      ENABLE = name + ".enable";
      DATA_RUN_GOTO = name + ".data.run.goto";
      DATA_RUN_GOTO_LOCATION = DATA_RUN_GOTO + ".location";
      DATA_RUN_GOTO_SERVER = DATA_RUN_GOTO + ".server";
      DATA_RUN_CMD = name + ".data.run.cmd";
      DATA_RUN_CMD_EXECUTOR = DATA_RUN_CMD + ".executor";
      DATA_RUN_CMD_MSG = DATA_RUN_CMD + ".msg";
      DATA_MSG = name + ".data.msg";
      DATA_MSG_USE_ENABLE = DATA_MSG + ".use.enable";
      DATA_MSG_USE_FORMAT = DATA_MSG + ".use.format";
      DATA_MSG_ERROR_PERM_ENABLE = DATA_MSG + ".error-perm.enable";
      DATA_MSG_ERROR_PERM_FORMAT = DATA_MSG + ".error-perm.format";
      DATA_MSG_ERROR_DISABLED_ENABLE = DATA_MSG + ".error-disabled.enable";
      DATA_MSG_ERROR_DISABLED_FORMAT = DATA_MSG + ".error-disabled.format";
      DATA_MSG_ERROR_SETTING_ENABLE = DATA_MSG + ".error-setting.enable";
      DATA_MSG_ERROR_SETTING_FORMAT = DATA_MSG + ".error-setting.format";
    }

    public String NAME, DISPLAY_NAME, PROTOCOL, ENABLE;
    public String DATA_RUN_GOTO;
    public String DATA_RUN_GOTO_LOCATION;
    public String DATA_RUN_GOTO_SERVER;
    public String DATA_RUN_CMD;
    public String DATA_RUN_CMD_EXECUTOR;
    public String DATA_RUN_CMD_MSG;
    public String DATA_MSG;
    public String DATA_MSG_USE_ENABLE;
    public String DATA_MSG_USE_FORMAT;
    public String DATA_MSG_ERROR_PERM_ENABLE;
    public String DATA_MSG_ERROR_PERM_FORMAT;
    public String DATA_MSG_ERROR_DISABLED_ENABLE;
    public String DATA_MSG_ERROR_DISABLED_FORMAT;
    public String DATA_MSG_ERROR_SETTING_ENABLE;
    public String DATA_MSG_ERROR_SETTING_FORMAT;
  }

  private String msgFormatter(String msg) {
    msg = msg.replace("{name}", this.name);
    msg = msg.replace("{displayname}", this.displayName);
    msg = Msg.effect(msg);
    return msg;
  }

  /**
   * 포탈을 사용합니다.
   *
   * @param player 포탈을 사용할 플레이어
   */
  public void use(Player player) {

    // 권한 확인
    if (!player.hasPermission("cherry.portal.use." + this.name)) {
      if ((boolean) this.msg.get("error-perm")[0]) {
        Msg.error(player, msgFormatter( (String) this.msg.get("error-perm")[1]) );
      }
      return;
    }

    // 활성화 여부 확인
    if (!this.enable) {
      if ((boolean) this.msg.get("error-disabled")[0]) {
        Msg.error(player, msgFormatter( (String) this.msg.get("error-disabled")[1]) );
      }
      return;
    }

    if (this.protocol.equals(Protocol.SERVER)) {
      if (this.gotoLocation == null) {
        if ((boolean) this.msg.get("error-setting")[0]) {
          Msg.error(player, msgFormatter( (String) this.msg.get("error-setting")[1]) );
        }
        return;
      }

      this.protocol.use(player, this.gotoLocation);
    }

    else if (this.protocol.equals(Protocol.BUNGEECORD)) {
      if (this.gotoServer == null) {
        if ((boolean) this.msg.get("error-setting")[0]) {
          Msg.error(player, msgFormatter( (String) this.msg.get("error-setting")[1]) );
        }
        return;
      }

      this.protocol.use(player, this.gotoServer);
    }

    else if (this.protocol.equals(Protocol.COMMAND)) {
      if (this.cmdExecutor == null || this.cmdMsg == null) {
        if ((boolean) this.msg.get("error-setting")[0]) {
          Msg.error(player, msgFormatter( (String) this.msg.get("error-setting")[1]) );
        }
        return;
      }

      this.protocol.use(player, this.cmdExecutor, this.cmdMsg);
    }

    if ((boolean) msg.get("use")[0]) {
      Msg.info(player, msgFormatter( (String) msg.get("use")[1]) );
    }

    Portal.setLastUsedTime(player);
  }



  public String getName() {
    return name;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
    portalData.set(path.DISPLAY_NAME, this.displayName);
  }

  public String getDisplayName() {
    return Msg.effect(this.displayName + "&r");
  }



  public void setEnable(boolean bool) {
    this.enable = bool;
    portalData.set(path.ENABLE, this.enable);
  }

  public boolean isEnable() {
    return this.enable;
  }



  public void setProtocol(PortalProtocol protocol) {
    this.protocol = protocol;
    portalData.set(path.PROTOCOL, this.protocol.toString());
  }

  public PortalProtocol getProtocol() {
    return this.protocol;
  }

  public enum Protocol implements PortalProtocol {

    /**
     * 서버 내 (월드 내 / 월드 간) 이동
     */
    SERVER {
      @Override
      public void use(Player player, Location loc) {
        Location tpLoc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        player.teleport(tpLoc);
      }

      @Override
      public void use(Player player, String server) {
      }

      @Override
      public void use(Player player, CmdExecutor executor, String msg) {
      }
    },

    /**
     * 번지코드 네트워크 내 (서버 간) 이동
     */
    BUNGEECORD {
      @Override
      public void use(Player player, Location loc) {
      }

      @Override
      public void use(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(Cherry.plugin, "BungeeCord", out.toByteArray());
      }

      @Override
      public void use(Player player, CmdExecutor executor, String msg) {
      }
    },

    COMMAND {
      @Override
      public void use(Player player, Location loc) {
      }

      @Override
      public void use(Player player, String server) {
      }

      @Override
      public void use(Player player, CmdExecutor executor, String msg) {
        if (executor.equals(CmdExecutor.PLAYER)) {
          Bukkit.dispatchCommand(player, msg);
        }
        else if (executor.equals(CmdExecutor.CONSOLE)) {
          Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg);
        }
      }
    },

    ;

    /**
     * 문자열에 해당하는 포탈 이동 프로토콜을 반환합니다.
     *
     * @param str 프로토콜 이름
     */
    public static Protocol getProtocol(String str) {
      if (str.equalsIgnoreCase("server")) {
        return SERVER;
      }
      if (str.equalsIgnoreCase("bungeecord")) {
        return BUNGEECORD;
      }
      if (str.equalsIgnoreCase("command")) {
        return COMMAND;
      }
      return null;
    }
  }


  public void setGotoLocation(Location loc) {
    this.gotoLocation = loc;
    portalData.set(path.DATA_RUN_GOTO_LOCATION, this.gotoLocation);
  }

  public void setGotoServer(String server) {
    this.gotoServer = server;
    portalData.set(path.DATA_RUN_GOTO_SERVER, this.gotoServer);
  }

  public void setCmdExecutor(CmdExecutor executor) {
    this.cmdExecutor = executor;
    portalData.set(path.DATA_RUN_CMD_EXECUTOR, this.cmdExecutor.toString());
  }

  public void setCmdMsg(String msg) {
    this.cmdMsg = msg;
    portalData.set(path.DATA_RUN_CMD_MSG, this.cmdMsg);
  }

  public Location getGotoLocation() {
    return gotoLocation;
  }

  public String getGotoServer() {
    return gotoServer;
  }

  public CmdExecutor getCmdExecutor() {
    return cmdExecutor;
  }

  public String getCmdMsg() {
    return cmdMsg;
  }

  public void addGatePortalArea(String areaName, List<Location> area, BlockData fill) {
    PortalArea pa = new PortalArea(this, areaName, area, PortalArea.Type.GATE, fill);
    addPortalArea(pa);
  }

  public void addSignPortalArea(String areaName, Block block) {
    String[] lines = new String[] {"", "", "", ""};
    if (this.protocol.equals(Protocol.SERVER)) {
      lines = new String[] {"&5[Portal]", "{displayname}", "{enabletoggle:&aEnabled:&cDisabled}", ""};
    }
    else if (this.protocol.equals(Protocol.BUNGEECORD)) {
      lines = new String[] {"", "{displayname}", "{servertoggle:&aOnline {cp}/{mp}:&cOffline}", "{enabletoggle::&4Disabled}"};
    }
    else if (this.protocol.equals(Protocol.COMMAND)) {
      lines = new String[] {"&9[Cmd]", "{displayname}", "", ""};
    }
    addSignPortalArea(areaName, block.getLocation(), block.getBlockData(), lines);
  }

  public void addSignPortalArea(String areaName, Location loc, BlockData fill, String[] lines) {
    PortalArea pa = new PortalArea(this, areaName, Collections.singletonList(loc), PortalArea.Type.SIGN, fill);
    pa.setLines(lines);
    addPortalArea(pa);
  }

  private void addPortalArea(PortalArea portalArea) {
    if (areas.containsKey(portalArea.getName())) {
      areas.replace(portalArea.getName(), portalArea);
    }
    else {
      areas.put(portalArea.getName(), portalArea);
    }
    saveAreas();
  }

  public void removePortalArea(String areaName) {
    areas.remove(areaName);
    saveAreas();
  }

  public PortalArea getPortalArea(String areaName) {
    if (areas.containsKey(areaName)) {
      return areas.get(areaName);
    }
    return null;
  }

  public PortalArea getPortalArea(Location location) {
    for (Map.Entry<String, PortalArea> data : this.areas.entrySet()) {
      PortalArea portalArea = data.getValue();
      List<Location> area = portalArea.getArea();
      if (area == null || area.isEmpty() || location == null) {
        return null;
      }
      for (Location loc : area) {
        if (loc == null) {
          return null;
        }
        if (loc.equals(location)) {
          return portalArea;
        }
      }
    }
    return null;
  }

  public HashMap<String, PortalArea> getPortalAreas() {
    return areas;
  }

  public boolean isPortalArea(Location location) {
    for (Map.Entry<String, PortalArea> data : this.areas.entrySet()) {
      PortalArea portalArea = data.getValue();
      List<Location> area = portalArea.getArea();
      if (area == null || area.isEmpty() || location == null) {
        return false;
      }
      for (Location loc : area) {
        if (loc == null) {
          return false;
        }
        if (loc.equals(location)) {
          return true;
        }
      }
    }
    return false;
  }

  public List<String> getPortalAreaNames() {
    List<String> list = new ArrayList<>();
    for (Map.Entry<String, PortalArea> data : areas.entrySet()) {
      list.add(data.getValue().getName());
    }
    return list;
  }



  private void saveAreas() {
    portalData.set(this.name + ".areas", null);
    for (Map.Entry<String, PortalArea> data : areas.entrySet()) {
      PortalArea pa = data.getValue();
      portalData.set(this.name + ".areas." + pa.getName() + ".type", pa.getType().toString());
      portalData.set(this.name + ".areas." + pa.getName() + ".fill.material", pa.getFill().getMaterial().toString());
      portalData.set(this.name + ".areas." + pa.getName() + ".fill.data", pa.getFill().getAsString(true));
      if (pa.getType().equals(PortalArea.Type.SIGN)) {
        portalData.set(this.name + ".areas." + pa.getName() + ".fill.lines", pa.getLines());
      }
      portalData.set(this.name + ".areas." + pa.getName() + ".location", pa.getArea());
    }
  }

  public void renewAreas() {
    for (Map.Entry<String, PortalArea> data : areas.entrySet()) {
      PortalArea pa = data.getValue();
      pa.fill();
      if (pa.getType().equals(PortalArea.Type.SIGN)) {
        pa.setLines(pa.getLines());
      }
    }
  }



  public static enum CmdExecutor {
    CONSOLE, PLAYER,;

    public static CmdExecutor getCmdExecutor(String c) {
      switch (c.toLowerCase()) {
        case "console": {
          return CONSOLE;
        }
        case "player": {
          return PLAYER;
        }
        default: {
          return null;
        }
      }
    }
  }

  /**
   * 새로운 포탈을 생성합니다.
   *
   * @param name 포탈 이름 (UNIQUE)
   */
  public static void createPortal(String name) {
    if (!portals.containsKey(name)) {
      new Portal(name);
    }
  }

  /**
   * 포탈의 이름으로 포탈을 찾아 반환합니다.
   *
   * @param name 확인할 포탈 이름 (UNIQUE)
   */
  public static Portal getPortal(String name) {
    if (portals.containsKey(name)) {
      return portals.get(name);
    }
    return null;
  }

  /**
   * 월드 좌표로 포탈을 찾아 반환합니다.
   *
   * @param location 확인할 좌표
   */
  public static Object[] getPortal(Location location) {
    Location loc = new Location(location.getWorld(), (int) location.getX(), (int) location.getY(), (int) location.getZ());
    for (Map.Entry<String, Portal> data : portals.entrySet()) {
      Portal portal = data.getValue();
      PortalArea pa = portal.getPortalArea(loc);
      if (pa != null) {
        return new Object[] {portal, pa};
      }
    }
    return null;
  }

  /**
   * 포탈을 제거합니다.
   *
   * @param name 포탈 이름 (UNIQUE)
   */
  public static void removePortal(String name) {
    portals.remove(name);
    portalData.set(name, null);
  }

  /**
   * 포탈의 목록을 반환합니다.
   */
  public static List<String> getPortalNames() {
    List<String> list = new ArrayList<>();
    for (Map.Entry<String, Portal> data : portals.entrySet()) {
      list.add(data.getValue().getName());
    }
    return list;
  }

  /**
   * 활성화된 포탈의 목록을 반환합니다.
   */
  public static List<String> getPortalNamesEnabled() {
    List<String> list = new ArrayList<>();
    for (Map.Entry<String, Portal> data : portals.entrySet()) {
      list.add(data.getValue().getName());
    }
    return list;
  }

  public static void updateBungeeSigns() {
    for(Map.Entry<String, Portal> data : portals.entrySet()) {
      Portal p = data.getValue();
      if (p.getProtocol().equals(Protocol.BUNGEECORD)) {
        for(Map.Entry<String, PortalArea> data2 : p.getPortalAreas().entrySet()) {
          PortalArea pa = data2.getValue();
          if (pa.getType().equals(PortalArea.Type.SIGN)) {
            pa.fill();
            pa.setLines(pa.getLines());
          }
        }
      }
    }
  }

  public static void updateBungeeSigns(String server) {
    for(Map.Entry<String, Portal> data : portals.entrySet()) {
      Portal p = data.getValue();
      if (p.getProtocol().equals(Protocol.BUNGEECORD)) {
        String gotoServer = p.getGotoServer();
        if (gotoServer != null) {
          for (Map.Entry<String, PortalArea> data2 : p.getPortalAreas().entrySet()) {
            PortalArea pa = data2.getValue();
            if (pa.getType().equals(PortalArea.Type.SIGN)) {
              pa.fill();
              pa.setLines(pa.getLines());
            }
          }
        }
      }
    }
  }


  public static HashMap<Player, Long> lastUsedTime = new HashMap<>();

  /**
   * 마지막으로 플레이어가 포탈을 사용한 시각을 기록합니다. (메모리 저장)
   *
   * @param player 포탈을 사용한 플레이어
   * @param time   시각 (밀리세컨드)
   */
  public static void setLastUsedTime(Player player, long time) {
    if (lastUsedTime.containsKey(player)) {
      lastUsedTime.replace(player, time);
    }
    else {
      lastUsedTime.put(player, time);
    }
  }

  /**
   * 마지막으로 플레이어가 포탈을 사용한 시각을 현재의 시각으로 기록합니다. (메모리 저장)
   *
   * @param player 포탈을 사용한 플레이어
   */
  public static void setLastUsedTime(Player player) {
    setLastUsedTime(player, System.currentTimeMillis());
  }

  /**
   * 마지막으로 플레이어가 포탈을 사용한 시각을 반환합니다.
   *
   * @param player 확인할 플레이어
   */
  public static Long getLastUsedTime(Player player) {
    return lastUsedTime.getOrDefault(player, null);
  }



  // 데이터 및 설정 로드
  public static void load() {

    for (String name : portalData.getConfig().getConfigurationSection("").getKeys(false)) {

      Portal portal;

      if (portals.containsKey(name)) {
        portal = portals.get(name);
      }
      else {
        portal = new Portal(name);
      }

      Msg.debug(Portal.prefix + "Load Portal " + portal.getName() + " (" + portal.getProtocol().toString() + ", D:" + portal.getDisplayName() + ")");

      if (portal.getProtocol().equals(Protocol.SERVER) && portal.getGotoLocation() != null) {
        Msg.debug(Portal.prefix + "  Dest: " + Tool.loc2StrWithWorld(portal.getGotoLocation()));
      }
      else if (portal.getProtocol().equals(Protocol.BUNGEECORD) && portal.getGotoServer() != null) {
        Msg.debug(Portal.prefix + "  Dest: " + portal.getGotoServer());
      }
      else if (portal.getProtocol().equals(Protocol.COMMAND) && portal.getCmdExecutor() != null && portal.getCmdMsg() != null) {
        Msg.debug(Portal.prefix + "  Command: " + portal.getCmdExecutor().toString() + " run " + portal.getCmdMsg());
      }

      String portalName = portal.getName();

      FileConfiguration data = portalData.getConfig();

      ConfigurationSection areas = data.getConfigurationSection(portalName + ".areas");

      if (areas != null) {
        for (String areaName : areas.getKeys(false)) {

          if (areaName == null) {
            continue;
          }

          String typeStr = data.getString(portalName + ".areas." + areaName + ".type");
          List<?> locList = data.getList(portalName + ".areas." + areaName + ".location");
          String matStr = data.getString(portalName + ".areas." + areaName + ".fill.material");
          String dataStr = data.getString(portalName + ".areas." + areaName + ".fill.data");

          if (typeStr == null || locList == null || matStr == null || dataStr == null) {
            continue;
          }

          List<Location> area = (List<Location>) locList;
          Material material = Material.getMaterial(dataStr);
          BlockData fill = Bukkit.createBlockData(material, dataStr);
          PortalArea.Type type = PortalArea.Type.valueOf(typeStr);

          if (type.equals(PortalArea.Type.GATE)) {
            portal.addGatePortalArea(areaName, area, fill);
            Msg.debug(Portal.prefix + "  Load Area " + portal.getName() + "&r." + areaName + "&r (" + type.toString() + "&r, " + Tool.loc2StrWithWorld(area.get(0)) + "&r, SIZE:" + area.size() + "&r)");
          }
          else if (type.equals(PortalArea.Type.SIGN)) {
            List<?> lines = data.getList(portalName + ".areas." + areaName + ".fill.lines");
            if (lines == null) {
              continue;
            }
            String[] linesStr = new String[]{"", "", "", ""};
            for (int n = 0; n < lines.size(); n++) {
              linesStr[n] = String.valueOf(lines.get(n));
            }
            portal.addSignPortalArea(areaName, area.get(0), fill, linesStr);
            Msg.debug(Portal.prefix + "  Load Area " + portal.getName() + "&r." + areaName + "&r (" + type.toString() + "&r, " + Tool.loc2StrWithWorld(area.get(0)) + "&r)");
          }

        }
      }

    }

  }

  public static void enable() {

    if (!Cherry.config.getBoolean("portal.enable")) {
      Msg.debug(Portal.prefix + "#EB565B;Portal Disabled");
      return;
    }

    Msg.debug(Portal.prefix + "#A9EB00;Enabling Portal v0.4");

    Cherry.plugin.registerCommand("portal", new PortalCommand(), new PortalTabCompleter());

    Cherry.plugin.registerEvent(new PortalEvent());

    Bukkit.getScheduler().runTaskLater(Cherry.plugin, new Runnable() {
      public void run() {
        load();
      }
    }, 10L);

    Portal.enabled = true;

  }

}
