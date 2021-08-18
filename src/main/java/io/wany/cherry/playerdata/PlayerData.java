package io.wany.cherry.playerdata;

import io.wany.cherry.Cherry;
import io.wany.cherry.Config;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

  private static final HashMap<UUID, PlayerData> playerDataMap = new HashMap<>();
  private final Config storage;
  private final UUID uuid;
  private final String name;
  private Component displayName;
  private String ip;
  private Location location;

  private PlayerData(Player player) {
    this.uuid = player.getUniqueId();
    this.name = player.getName();
    this.storage = new Config("players/" + this.uuid);
    this.pull();
    if (this.displayName == null) {
      this.displayName = player.displayName();
    }
    if (this.location == null) {
      this.location = player.getLocation();
    }
    if (this.ip == null) {
      if (player.getAddress() == null) {
        this.ip = "Unknown";
      }
      else {
        this.ip = player.getAddress().toString();
      }
    }
    this.push();
  }

  private PlayerData(OfflinePlayer player) {
    this.uuid = player.getUniqueId();
    this.name = player.getName();
    this.storage = new Config("players/" + this.uuid);
    this.pull();
    this.push();
  }

  public static PlayerData get(Player player) {
    if (playerDataMap.containsKey(player.getUniqueId())) {
      return playerDataMap.get(player.getUniqueId());
    }
    else {
      PlayerData playerData = new PlayerData(player);
      playerDataMap.put(player.getUniqueId(), playerData);
      return playerData;
    }
  }

  public static PlayerData get(OfflinePlayer player) {
    if (playerDataMap.containsKey(player.getUniqueId())) {
      return playerDataMap.get(player.getUniqueId());
    }
    else {
      PlayerData playerData = new PlayerData(player);
      playerDataMap.put(player.getUniqueId(), playerData);
      return playerData;
    }
  }

  public static boolean exist(Player player) {
    return Config.exist("players/" + player.getUniqueId());
  }

  public static boolean exist(OfflinePlayer player) {
    return Config.exist("players/" + player.getUniqueId());
  }

  public static void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    PlayerData playerData = get(player);
    playerData.displayName(player.displayName());
    playerData.location(player);
    playerData.push();
  }

  public static void onQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    PlayerData playerData = get(player);
    playerData.displayName(player.displayName());
    playerData.location(player);
    playerData.push();
  }

  public static void onEnable() {
    if (Cherry.CONFIG.getBoolean("cucumbery-support.enable")
    && Cherry.CONFIG.getBoolean("cucumbery-support.default-userdata.enable")) {
      return;
    }
    for (Player player : Bukkit.getOnlinePlayers()) {
      get(player);
    }
  }

  public static void onDisable() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      get(player);
    }
  }

  public void pull() {
    this.displayName = this.storage.getComponent("displayName");
    this.location = this.storage.getLocation("location");
    this.ip = this.storage.getString("ip");
  }

  public void push() {
    this.storage.set("uuid", this.uuid.toString());
    this.storage.set("name", this.name);
    this.storage.set("displayName", this.displayName);
    this.storage.set("ip", this.ip);
    this.storage.set("location", this.location);
  }

  public Component displayName() {
    return this.displayName;
  }

  public void displayName(Component displayName) {
    this.displayName = displayName;
  }

  public Location location() {
    return this.location;
  }

  public void location(Player player) {
    this.location = player.getLocation();
  }

}
