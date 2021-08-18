package io.wany.cherry.portal;

import io.wany.cherry.Cherry;
import io.wany.cherry.Config;
import io.wany.cherry.Console;
import io.wany.cherry.Message;
import io.wany.cherry.wand.Wand;
import io.wany.cherry.wand.area.Cube;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Portal {

  public static String COLOR = "#A48DFF;";
  public static String PREFIX = COLOR + "&l[Portal]:&r ";
  public static boolean ENABLED = false;
  public static String DATAPATH = "portals";

  private static final HashMap<String, Portal> portals = new HashMap<>();

  private final String name;
  private Component displayName;
  private Destination destination;
  private final List<Area> areas = new ArrayList<>();
  private boolean opened;

  private final Config storage;

  private Portal(String name) {
    name = name.replaceAll("^[0-9]+", "");
    name = name.replaceAll("[^a-zA-Z0-9_]+", "");
    this.name = name;
    this.storage = new Config(DATAPATH + "/" + name);
    if (this.storage.isComponent("displayName")) {
      this.displayName(this.storage.getComponent("displayName"));
    }
    else {
      this.displayName(Message.parse(Message.effect("&e" + this.name)));
    }
    if (this.storage.isLocation("destination")) {
      this.destination(new Destination(this.storage.getLocation("destination")));
    }
    else if (this.storage.isList("destination")) {
      String[] commands;
      List<String> commandsList = (List<String>) this.storage.getList("destination");
      commands = (String[]) commandsList.toArray();
      this.destination(new Destination(commands));
    }
    else if (this.storage.isString("destination")) {
      this.destination(new Destination(this.storage.getString("destination")));
    }
    else {
      this.destination(null);
    }
    portals.remove(this.name);
    portals.put(this.name, this);
  }

  public String name() {
    return this.name;
  }

  public Component displayName() {
    return this.displayName;
  }

  public String displayNameString() {
    return Message.stringify(this.displayName);
  }

  public void displayName(Component component) {
    this.displayName = component;
    this.storage.set("displayName", component);
  }

  public void displayName(String string) {
    this.displayName(Message.parse(Message.effect(string)));
  }

  public Destination destination() {
    return this.destination;
  }

  public void destination(Destination destination) {
    this.destination = destination;
    if (this.destination == null) {
      this.storage.set("destination", null);
    }
    else if (this.destination.type == Destination.Type.LOCATION) {
      this.storage.set("destination", this.destination.location);
    }
    else if (this.destination.type == Destination.Type.BUNGEECORD) {
      this.storage.set("destination", this.destination.server);
    }
    else if (this.destination.type == Destination.Type.COMMANDS) {
      this.storage.set("destination", List.of(this.destination.commands));
    }
  }

  public void addArea(Area area) {
    removeArea(area);
    this.areas.add(area);
  }

  public void removeArea(Area area) {
    if (containsArea(area)) {
      this.areas.remove(area);
    }
  }

  public boolean containsArea(Area area) {
    return this.areas.contains(area);
  }

  public void renew() {

  }

  public boolean contains(Location location) {
    for (Area area : this.areas) {
      if (area.contains(location)) {
        return true;
      }
    }
    return false;
  }

  public boolean contains(Entity entity) {
    return contains(entity.getLocation().toBlockLocation());
  }

  public boolean contains(Player player) {
    return contains((Entity) player);
  }

  public void open() {
    this.opened = true;
  }

  public void close() {
    this.opened = false;
  }

  public boolean isOpened() {
    return this.opened;
  }

  public void use(Player player) {
    if (this.destination == null) {
      return;
    }
    this.destination.send(player);
  }

  public void use(Entity entity) {
    if (entity instanceof Player player) {
      this.use((Player) entity);
    }
    if (this.destination == null) {
      return;
    }
    this.destination.send(entity);
  }

  public static class Destination {

    private final Type type;
    private Location location;
    private String server;
    private String[] commands;

    public Destination(Location location) {
      this.type = Type.LOCATION;
      this.location = location;
    }

    public Destination(String server) {
      this.type = Type.BUNGEECORD;
      this.server = server;
    }

    public Destination(String[] commands) {
      this.type = Type.COMMANDS;
      this.commands = commands;
    }

    public void send(Player player) {
      switch (type) {
        case LOCATION: {
          player.teleport(this.location);
        }
        case BUNGEECORD: {
          Console.log("send " + player.getName() + " to " + this.server);
          ///aaaaaaaaa
        }
        case COMMANDS: {
          for (String command : this.commands) {
            command = command.replace("@s", player.getName());
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
          }
        }
      }
    }

    public void send(Entity entity) {
      Console.log(entity.getName());
    }

    public enum Type {
      LOCATION, BUNGEECORD, COMMANDS
    }

  }

  public static class Area {

    private static final List<Material> accpetedMaterials = List.of(Material.AIR, Material.WATER, Material.NETHER_PORTAL, Material.END_PORTAL, Material.END_GATEWAY);
    private final String name;
    private final Type type;
    private final List<Location> locations = new ArrayList<>();
    private final BlockData blockData;
    private String[] signLines;

    public Area(String name, Location max, Location min, BlockData blockData, @Nullable String[] signLines) {
      this.name = name;
      this.blockData = blockData;
      if (max.getBlockX() == min.getBlockX() && max.getBlockY() == min.getBlockY() && max.getBlockZ() == min.getBlockZ()) {
        if (this.blockData instanceof Sign) {
          this.type = Type.SIGN;
          this.signLines = signLines;
        }
        else {
          this.type = Type.SINGLE;
        }
      }
      else if (max.getBlockX() == min.getBlockX() && max.getBlockZ() != min.getBlockZ()) {
        this.type = Type.GATE_EW;
      }
      else if (max.getBlockX() != min.getBlockX() && max.getBlockZ() == min.getBlockZ()) {
        this.type = Type.GATE_SN;
      }
      else if (max.getBlockX() != min.getBlockX() && max.getBlockY() == min.getBlockY() && max.getBlockZ() != min.getBlockZ()) {
        this.type = Type.GATE_TB;
      }
      else {
        this.type = Type.AREA;
      }
      List<Location> cubeArea = Cube.getAreaFilled(min, max);
      for (Location location : cubeArea) {
        if (this.type == Type.SIGN) {
          if (location.getBlock().getBlockData() instanceof Sign) {
            locations.add(location);
          }
        }
        else {
          if (accpetedMaterials.contains(location.getBlock().getType())) {
            locations.add(location);
          }
        }
      }
    }

    public Area(String name, Location max, Location min, BlockData blockData) {
      this(name, max, min, blockData, null);
    }

    public boolean contains(Location location) {
      return this.locations.contains(location);
    }

    public void fill() {
      if (this.type == Type.SIGN) {
        Sign sign = (Sign) this.blockData;
        for (var i = 0; i < Math.min(this.signLines.length, 4); i++) {
          sign.line(i, parseSignline(this.signLines[i]));
        }
        sign.update(true, false);
      }
      Wand wand = Wand.getWand(Cherry.UUID);
      wand.fill(this.blockData, this.locations, false);
      /*if (this.type == Type.SIGN) {
        for (Location location : this.locations) {
          Sign sign = (Sign) location.getBlock().getBlockData();
          for (var i = 0; i < Math.min(this.signLines.length, 4); i++) {
            sign.line(i, parseSignline(this.signLines[i]));
          }
          sign.update(true, false);
        }
      }*/
    }

    public String name() {
      return this.name;
    }

    public Type type() {
      return this.type;
    }

    private Component parseSignline(String line) {
      return Message.parse(line);
    }

    public enum Type {
      SINGLE, SIGN, GATE_EW, GATE_SN, GATE_TB, AREA
    }

  }

  public static Portal getPortal(String name) {
    return portals.get(name);
  }

  public static Portal getPortal(Location location) {
    for (Portal portal : portals.values()) {
      if (portal.contains(location)) {
        return portal;
      }
    }
    return null;
  }

  public static Portal getPortal(Entity entity) {
    for (Portal portal : portals.values()) {
      if (portal.contains(entity)) {
        return portal;
      }
    }
    return null;
  }

  public static List<Portal> getPortals() {
    return new ArrayList<>(portals.values());
  }

  public static List<String> getPortalNames() {
    return new ArrayList<>(portals.keySet());
  }

  public static Portal addPortal(String name) {
    return new Portal(name);
  }

  public static void removePortal(String name) {
    Portal portal = portals.get(name);
    if (portal == null) {
      return;
    }
    portal.storage.file().delete();
    portals.remove(name);
  }

  public static void onEnable() {

    if (!Cherry.CONFIG.getBoolean("portal.enable")) {
      Console.debug(Message.effect(PREFIX + "Portal Disabled"));
      return;
    }
    Console.debug(Message.effect(PREFIX + "Enabling Portal"));
    Portal.ENABLED = true;

    Bukkit.getScheduler().runTaskLater(Cherry.PLUGIN, () -> {
      File dataDirectory = new File(Cherry.DIR + "/" + DATAPATH);
      if (!dataDirectory.exists()) {
        dataDirectory.mkdirs();
      }
      File[] files = dataDirectory.listFiles();
      if (files == null) {
        return;
      }
      for (File file : files) {
        Console.log(file.getName());
      }
      dataDirectory.listFiles();
      /*for (String name : portalData.getConfig().getConfigurationSection("").getKeys(false)) {

        Portal portal;

        if (portals.containsKey(name)) {
          portal = portals.get(name);
        } else {
          portal = new Portal(name);
        }

        Msg.debug(Portal.prefix + "Load Portal " + portal.getName() + " (" + portal.getProtocol().toString() + ", D:" + portal.getDisplayName() + ")");

        if (portal.getProtocol().equals(Protocol.SERVER) && portal.getGotoLocation() != null) {
          Msg.debug(Portal.prefix + "  Dest: " + Tool.loc2StrWithWorld(portal.getGotoLocation()));
        } else if (portal.getProtocol().equals(Protocol.BUNGEECORD) && portal.getGotoServer() != null) {
          Msg.debug(Portal.prefix + "  Dest: " + portal.getGotoServer());
        } else if (portal.getProtocol().equals(Protocol.COMMAND) && portal.getCmdExecutor() != null && portal.getCmdMsg() != null) {
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
              Bukkit.getScheduler().runTask(Cherry.plugin, new Runnable() {
                @Override
                public void run() {
                  portal.addGatePortalArea(areaName, area, fill);
                }
              });
              Msg.debug(Portal.prefix + "  Load Area " + portal.getName() + "&r." + areaName + "&r (" + type.toString() + "&r, " + Tool.loc2StrWithWorld(area.get(0)) + "&r, SIZE:" + area.size() + "&r)");
            } else if (type.equals(PortalArea.Type.SIGN)) {
              List<?> lines = data.getList(portalName + ".areas." + areaName + ".fill.lines");
              if (lines == null) {
                continue;
              }
              String[] linesStr = new String[]{"", "", "", ""};
              for (int n = 0; n < lines.size(); n++) {
                linesStr[n] = String.valueOf(lines.get(n));
              }
              Bukkit.getScheduler().runTask(Cherry.plugin, new Runnable() {
                @Override
                public void run() {
                  portal.addSignPortalArea(areaName, area.get(0), fill, linesStr);
                }
              });
              Msg.debug(Portal.prefix + "  Load Area " + portal.getName() + "&r." + areaName + "&r (" + type.toString() + "&r, " + Tool.loc2StrWithWorld(area.get(0)) + "&r)");
            }

          }
        }

      }*/
    }, 10L);

  }

  public static void onReload() {

  }

}
