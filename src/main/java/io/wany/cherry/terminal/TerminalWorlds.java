package io.wany.cherry.terminal;

import io.wany.cherry.Cherry;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.json.JSONArray;
import org.json.JSONObject;

public class TerminalWorlds {

  public static void sendWorlds() {
    Bukkit.getScheduler().runTask(Cherry.PLUGIN, () -> {
      String event = "worlds-worlds";
      String message = "worlds";
      JSONObject data = new JSONObject();
      JSONArray worlds = new JSONArray();
      for (World world : Bukkit.getWorlds()) {
        JSONObject worldData = new JSONObject();
        worldData.put("name", world.getName());
        worldData.put("chunks", world.getLoadedChunks().length);
        worldData.put("chunks-force", world.getForceLoadedChunks().size());
        worldData.put("environment", world.getEnvironment().toString());
        worldData.put("seed", world.getSeed());
        worldData.put("time", world.getTime());
        worldData.put("gametime", world.getGameTime());
        worldData.put("fulltime", world.getFullTime());
        worldData.put("difficulty", world.getDifficulty());
        worldData.put("players", world.getPlayerCount()); // Bukkit Sync
        worldData.put("entities", world.getEntityCount()); // Bukkit Sync
        JSONObject gamerulesData = new JSONObject();
        for (String key : world.getGameRules()) {
          String value = world.getGameRuleValue(GameRule.getByName(key)).toString();
          gamerulesData.put(key, value);
        }
        worldData.put("gamerules", gamerulesData);
        worlds.put(worldData);
      }
      data.put("worlds", worlds);
      Terminal.send(event, message, data);
    });
  }

  public static void sendWorld(World world) {
    Bukkit.getScheduler().runTask(Cherry.PLUGIN, () -> {
      String event = "worlds-world";
      String message = "world";
      JSONObject data = new JSONObject();
      data.put("name", world.getName());
      data.put("chunks", world.getLoadedChunks().length);
      data.put("chunks-force", world.getForceLoadedChunks().size());
      data.put("environment", world.getEnvironment().toString());
      data.put("seed", world.getSeed());
      data.put("time", world.getTime());
      data.put("gametime", world.getGameTime());
      data.put("fulltime", world.getFullTime());
      data.put("difficulty", world.getDifficulty());
      data.put("players", world.getPlayerCount()); // Bukkit Sync
      data.put("entities", world.getEntityCount()); // Bukkit Sync
      JSONObject gamerulesData = new JSONObject();
      for (String key : world.getGameRules()) {
        String value = world.getGameRuleValue(GameRule.getByName(key)).toString();
        gamerulesData.put(key, value);
      }
      data.put("gamerules", gamerulesData);
      Terminal.send(event, message, data);
    });
  }

  public static void sendGamerules(World world) {
    String event = "worlds-world-gamerules";
    String message = "world";
    JSONObject data = new JSONObject();
    data.put("name", world.getName());
    JSONObject gamerulesData = new JSONObject();
    for (String key : world.getGameRules()) {
      String value = world.getGameRuleValue(GameRule.getByName(key)).toString();
      gamerulesData.put(key, value);
    }
    data.put("gamerules", gamerulesData);
    Terminal.send(event, message, data);
  }

  public static void setGameRule(World world, GameRule gameRule, Object value) {
    Bukkit.getScheduler().runTask(Cherry.PLUGIN, () -> {
      try {
        world.setGameRule(gameRule, value);
        sendGamerules(world);
      }
      catch (Exception e) {
      }
      sendGamerules(world);
    });
  }

  public static void setDifficulty(World world, Difficulty difficulty) {
    world.setDifficulty(difficulty);
    sendWorld(world);
  }

}
