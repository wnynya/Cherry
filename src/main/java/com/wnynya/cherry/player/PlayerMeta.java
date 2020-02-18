package com.wnynya.cherry.player;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.amethyst.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;

public class PlayerMeta {

  private static HashMap<Player, PlayerMeta> playerMetas = new HashMap<>();

  private HashMap<Function, FunctionData> functions = new HashMap<>();
  private static Config playerConfig;

  PlayerMeta(Player player) {

    playerConfig = new Config("player/" + player.getUniqueId().toString());

    if (playerConfig.getConfig().get("name") == null) {
      playerConfig.set("name", player.getName());
    }

    if (playerConfig.getConfig().get("uuid") == null) {
      playerConfig.set("uuid", player.getUniqueId().toString());
    }

    // 완드
    functions.put(Function.WAND, initFunctionData("wand", "{\"posMsg\":\"actionbar\"}"));

    // 포탈
    functions.put(Function.PORTAL, initFunctionData("portal", "{\"effect\":\"\"}"));

    functions.put(Function.NICKNAME, initFunctionData("nickname", "{\"nickname\":\"\"}"));

    functions.put(Function.NOTETOOL, initFunctionData("notetool", "{\"blockOnHand\":\"use\"}"));

  }

  public FunctionData getFunction(Function function) {
    return functions.getOrDefault(function, null);
  }

  public Config getConfig() {
    return playerConfig;
  }

  public static class FunctionData {
    private String name;
    private boolean enable;
    private JSONObject data;

    FunctionData(boolean defaultEnable, JSONObject defaultData) {
      name = name;
      enable = defaultEnable;
      data = defaultData;
    }

    public void enable() {
      enable = true;
    }
    public void disable() {
      enable = false;
    }
    public boolean isEnable() {
      return enable;
    }

    public boolean setData(String key, String val, boolean force) {
      if (data.get(key) != null) {
        data.replace(key, val);
        return true;
      }
      else {
        if (force) {
          data.put(key, val);
        }
        return false;
      }
    }

    public JSONObject getData() {
      return data;
    }
  }

  private static FunctionData initFunctionData(String name, String defaultData) {
    if (!playerConfig.getConfig().isBoolean("function." + name + ".enable") ) {
      playerConfig.set("function." + name + ".enable", Cherry.config.getBoolean("playermeta.function." + name + ".enable"));
    }
    if (!playerConfig.getConfig().isString("function." + name + ".data") ) {
      playerConfig.set("function." + name + ".data", Cherry.config.getString("playermeta.function." + name + ".data"));
    }
    JSONParser parser = new JSONParser();
    JSONObject object = null;
    try {
      object = (JSONObject) parser.parse(playerConfig.getConfig().getString("function." + name + ".data"));
    }
    catch (Exception e) {
      try {
        object = (JSONObject) parser.parse(defaultData);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    boolean bool = playerConfig.getConfig().getBoolean("function." + name + ".enable");
    FunctionData fd = new FunctionData(bool, object);
    return fd;
  }

  public static enum Function {
    WAND,
    PORTAL,
    NICKNAME,
    NOTETOOL
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
