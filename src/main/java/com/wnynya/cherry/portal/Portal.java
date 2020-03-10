package com.wnynya.cherry.portal;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Config;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.wand.Wand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cherry Portal
 * 좌표계 / 월드계 / 서버계(번지코드) 간 포탈
 */
public class Portal {

  private static Config portalData = new Config("portal/data");

  private static HashMap<String, Portal> portals = new HashMap<>();

  private String name;
  private String displayName;
  private PortalProtocol protocol;
  private Location gotoLocation = null;
  private String gotoServer = null;
  private boolean enable = false;
  private HashMap<String, PortalArea> areas = new HashMap<>();

  private Portal(String name) {
    this.name = name;
    if (!portalData.getConfig().isString(this.name + ".name")) {
      portalData.set(this.name + ".name", this.name);
    }
    this.displayName = name;
    if (!portalData.getConfig().isString(this.name + ".displayName")) {
      portalData.set(this.name + ".displayName", this.name);
    }
    this.protocol = Protocol.SERVER;
    if (!portalData.getConfig().isString(this.name + ".protocol")) {
      portalData.set(this.name + ".protocol", this.protocol.toString());
    }
    if (!portalData.getConfig().isBoolean(this.name + ".enable")) {
      portalData.set(this.name + ".enable", this.enable);
    }
    if (!portalData.getConfig().isLocation(this.name + ".goto.location")) {
      portalData.set(this.name + ".goto.location", "");
    }
    if (!portalData.getConfig().isString(this.name + ".goto.server")) {
      portalData.set(this.name + ".goto.server", "");
    }
    if (!portalData.getConfig().isBoolean(this.name + ".msg.enter.enable")) {
      portalData.set(this.name + ".msg.enter.enable", true);
    }
    if (!portalData.getConfig().isBoolean(this.name + ".msg.permissionError.enable")) {
      portalData.set(this.name + ".msg.permissionError.enable", true);
    }
    if (!portalData.getConfig().isBoolean(this.name + ".msg.disabledError.enable")) {
      portalData.set(this.name + ".msg.disabledError.enable", true);
    }
    if (!portalData.getConfig().isBoolean(this.name + ".msg.destError.enable")) {
      portalData.set(this.name + ".msg.destError.enable", false);
    }
    portals.put(name, this);
  }


  public void use(Player player) {

    if (!player.hasPermission("cherry.portal.use." + this.name)) {
      if (portalData.getConfig().getBoolean(this.name + ".msg.permissionError.enable")) {
        Msg.error(player, "포탈을 사용할 권한이 없습니다");
      }
      return;
    }

    if (!this.enable) {
      if (portalData.getConfig().getBoolean(this.name + ".msg.disabledError.enable")) {
        Msg.error(player, "비활성화된 포탈입니다");
      }
      return;
    }

    if (this.protocol.equals(Protocol.SERVER)) {

      if (this.gotoLocation == null) {
        Msg.error(player, "목적지가 설정되지 않은 포탈입니다");
        return;
      }

      this.protocol.use(player, this.gotoLocation);

      if (portalData.getConfig().getBoolean(this.name + ".msg.enter.enable")) {
        Msg.info(player, Msg.Prefix.PORTAL + getDisplayName() + "포탈을 사용하였습니다");
      }

      Portal.setLastUsedTime(player);
    }

    else if (this.protocol.equals(Protocol.BUNGEECORD)) {

      if (this.gotoServer == null) {
        Msg.error(player, "목적지가 설정되지 않은 포탈입니다");
        return;
      }

      this.protocol.use(player, this.gotoServer);

      if (portalData.getConfig().getBoolean(this.name + ".msg.enter.enable")) {
        Msg.info(player, Msg.Prefix.PORTAL + getDisplayName() + "포탈을 사용하였습니다");
      }

      Portal.setLastUsedTime(player);
    }

  }


  public String getName() {
    return name;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
    portalData.set(this.name + ".displayName", this.displayName);
  }

  public String getDisplayName() {
    return Msg.n2s("&b" + this.displayName + "&r");
  }


  public void setEnable(boolean bool) {
    this.enable = bool;
    portalData.set(this.name + ".enable", this.enable);
  }

  public boolean isEnable() {
    return this.enable;
  }


  public void setProtocol(PortalProtocol protocol) {
    this.protocol = protocol;
    portalData.set(this.name + ".protocol", this.protocol.toString());
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
    };

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
      return null;
    }
  }


  public void setGotoLocation(Location loc) {
    this.gotoLocation = loc;
    portalData.set(this.name + ".goto.location", this.gotoLocation);
  }

  public void setGotoServer(String server) {
    this.gotoServer = server;
    portalData.set(this.name + ".goto.server", this.gotoServer);
  }


  public void setPortalArea(PortalArea portalArea) {
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

  public List<String> getPortalAreaNames() {
    List<String> list = new ArrayList<>();
    for (Map.Entry<String, PortalArea> data : areas.entrySet()) {
      list.add(data.getValue().getName());
    }
    return list;
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

  public void fillPortalArea(PortalArea portalArea) {
    List<Location> area = portalArea.getArea();
    Wand wand = Wand.getWand(Cherry.getUUID());
    BlockData rBlockData = null;
    if (portalArea.getType().equals(PortalArea.Type.GATE_AIR)) {
      rBlockData = Bukkit.createBlockData(Material.AIR, "[]");
    }
    if (portalArea.getType().equals(PortalArea.Type.GATE_WATER)) {
      rBlockData = Bukkit.createBlockData(Material.WATER, "[]");
    }
    if (portalArea.getType().equals(PortalArea.Type.GATE_ENDER)) {
      rBlockData = Bukkit.createBlockData(Material.END_GATEWAY, "[]");
    }
    if (portalArea.getType().equals(PortalArea.Type.GATE_ENDER_LEGACY)) {
      if (portalArea.getAxis().equals("y")) {
        rBlockData = Bukkit.createBlockData(Material.END_PORTAL, "[]");
      }
    }
    if (portalArea.getType().equals(PortalArea.Type.GATE_NETHER)) {
      if (portalArea.getAxis().equals("x")) {
        rBlockData = Bukkit.createBlockData(Material.NETHER_PORTAL, "[axis=x]");
      }
      else if (portalArea.getAxis().equals("z")) {
        rBlockData = Bukkit.createBlockData(Material.NETHER_PORTAL, "[axis=z]");
      }
      else {
        rBlockData = Bukkit.createBlockData(Material.AIR, "[]");
      }
    }
    if (rBlockData != null) {
      wand.replace(Bukkit.createBlockData(Material.AIR, "[]"), rBlockData, area, false);
      wand.replace(Bukkit.createBlockData(Material.WATER, "[]"), rBlockData, area, false);
      wand.replace(Bukkit.createBlockData(Material.END_GATEWAY, "[]"), rBlockData, area, false);
      wand.replace(Bukkit.createBlockData(Material.END_PORTAL, "[]"), rBlockData, area, false);
      wand.replace(Bukkit.createBlockData(Material.NETHER_PORTAL, "[]"), rBlockData, area, false);
    }

  }

  public void fillPortalAreas() {
    for (Map.Entry<String, PortalArea> data : areas.entrySet()) {
      PortalArea portalArea = data.getValue();
      if (portalArea.getType().equals(PortalArea.Type.GATE_AIR) || portalArea.getType().equals(PortalArea.Type.GATE_NETHER) || portalArea.getType().equals(PortalArea.Type.GATE_ENDER) || portalArea.getType().equals(PortalArea.Type.GATE_WATER)) {
        fillPortalArea(portalArea);
      }
    }
  }

  public void saveAreas() {
    for (Map.Entry<String, PortalArea> data : areas.entrySet()) {
      portalData.set(this.name + ".areas." + data.getKey() + ".type", data.getValue().getType().toString());
      portalData.set(this.name + ".areas." + data.getKey() + ".axis", data.getValue().getAxis());
      portalData.set(this.name + ".areas." + data.getKey() + ".location", data.getValue().getArea());
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
  public static Portal getPortal(Location location) {
    Location loc = new Location(location.getWorld(), (int) location.getX(), (int) location.getY(), (int) location.getZ());
    for (Map.Entry<String, Portal> data : portals.entrySet()) {
      Portal portal = data.getValue();
      if (portal.isPortalArea(loc)) {
        return portal;
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


  /**
   * 포탈의 이동 프로토콜
   */


  // 데이터 및 설정 로드
  public static void load() {
    if (Cherry.debug) {
      Msg.info("Portal : v0.3");
    }

    for (String key : portalData.getConfig().getConfigurationSection("").getKeys(false)) {

      createPortal(key);
      Portal portal = getPortal(key);
      if (portal == null) {
        return;
      }

      if (Cherry.debug) {
        Msg.info("  load : " + key);
      }

      FileConfiguration data = portalData.getConfig();

      if (data.isString(key + ".displayName")) {
        portal.setDisplayName(data.getString(key + ".displayName"));
      }
      if (data.isBoolean(key + ".enable")) {
        portal.setEnable(data.getBoolean(key + ".enable"));
      }
      if (data.isString(key + ".protocol")) {
        portal.setProtocol(Protocol.getProtocol(data.getString(key + ".protocol")));
      }
      if (data.isLocation(key + ".goto.location")) {
        portal.setGotoLocation(data.getLocation(key + ".goto.location"));
      }
      if (data.isString(key + ".goto.server")) {
        portal.setGotoServer(data.getString(key + ".goto.server"));
      }
      if (data.isConfigurationSection(key + ".areas") && data.getConfigurationSection(key + ".areas") != null) {
        for (String areaKey : portalData.getConfig().getConfigurationSection(key + ".areas").getKeys(false)) {
          if (areaKey == null) {
            break;
          }
          if (data.isString(key + ".areas." + areaKey + ".type") && data.isList(key + ".areas." + areaKey + ".location")) {
            String areaName = areaKey;
            if (Cherry.debug) {
              Msg.info("    " + areaName);
            }
            List<Location> list = (List<Location>) data.getList(key + ".areas." + areaKey + ".location");
            PortalArea.Type type = PortalArea.Type.valueOf(data.getString(key + ".areas." + areaKey + ".type"));
            String axis = data.getString(key + ".areas." + areaKey + ".axis");
            portal.setPortalArea(new PortalArea(areaName, list, type, axis));
          }
        }
      }

    }
  }

  public static void init() {
    load();
  }

}
