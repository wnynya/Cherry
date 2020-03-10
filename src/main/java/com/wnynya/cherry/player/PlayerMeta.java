package com.wnynya.cherry.player;

import com.wnynya.cherry.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerMeta {

  private static HashMap<Player, PlayerMeta> playerMetas = new HashMap<>();
  private static Config playerConfig;

  PlayerMeta(Player player) {

    playerConfig = new Config("player/" + player.getUniqueId().toString());

    FileConfiguration config = playerConfig.getConfig();

    if (config.get("name") == null) {
      playerConfig.set("name", player.getName());
    }

    if (config.get("uuid") == null) {
      playerConfig.set("uuid", player.getUniqueId().toString());
    }

    if (!config.isBoolean(Path.WAND_ENABLE.val())) {
      playerConfig.set(Path.WAND_ENABLE.val(), true);
    }

    if (!config.isString(Path.WAND_MSG.val())) {
      playerConfig.set(Path.WAND_MSG.val(), "actionbar");
    }

    if (!config.isBoolean(Path.NOTETOOL_ENABLE.val())) {
      playerConfig.set(Path.NOTETOOL_ENABLE.val(), true);
    }

  }

  public boolean is(Path path) {
    return playerConfig.getConfig().getBoolean(path.val());
  }

  public String get(Path path) {
    return playerConfig.getConfig().getString(path.val());
  }

  public void set(Path path, Object val) {
    playerConfig.set(path.val(), val);
  }

  public Config getConfig() {
    return playerConfig;
  }

  public static enum Path {
    WAND_ENABLE("wand.enable"), WAND_MSG("wand.msg"), NOTETOOL_ENABLE("notetool.enable"),
    ;

    private String path;

    Path(String path) {
      this.path = path;
    }

    @Override
    public String toString() {
      return path;
    }

    public String val() {
      return path;
    }
  }

  public static void initPlayerMeta(Player player) {
    if (!playerMetas.containsKey(player)) {
      PlayerMeta pm = new PlayerMeta(player);
      playerMetas.put(player, pm);
    }
  }

  public static PlayerMeta getPlayerMeta(Player player) {
    if (playerMetas.containsKey(player)) {
      return playerMetas.get(player);
    }
    else {
      initPlayerMeta(player);
      return playerMetas.get(player);
    }
  }

  public static void init() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      PlayerMeta.initPlayerMeta(player);
    }
  }
}
