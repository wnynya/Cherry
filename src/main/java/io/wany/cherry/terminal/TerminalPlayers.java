package io.wany.cherry.terminal;

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.json.JSONArray;
import org.json.JSONObject;

public class TerminalPlayers {

  public static void onJoin(PlayerJoinEvent event) {
    sendPlayers();
    String eventName = "players-join";
    Player player = event.getPlayer();
    String message = "player " + player.getName() + " join";
    JSONObject data = new JSONObject();
    data.put("name", player.getName());
    data.put("displayname", GsonComponentSerializer.gson().serialize(player.displayName()));
    data.put("time", System.currentTimeMillis());
    Terminal.send(eventName, message, data);
    TerminalWorlds.sendWorld(player.getWorld());
  }

  public static void onQuit(PlayerQuitEvent event) {
    sendPlayers();
    String eventName = "players-quit";
    Player player = event.getPlayer();
    String message = "player " + player.getName() + " quit";
    JSONObject data = new JSONObject();
    data.put("name", player.getName());
    data.put("displayname", GsonComponentSerializer.gson().serialize(player.displayName()));
    data.put("time", System.currentTimeMillis());
    Terminal.send(eventName, message, data);
    TerminalWorlds.sendWorld(player.getWorld());
  }

  public static void sendPlayers() {
    String event = "players-data";
    String message = "players";
    JSONObject data = new JSONObject();
    JSONArray players = new JSONArray();
    for (Player player : Bukkit.getOnlinePlayers()) {
      JSONObject playerData = new JSONObject();
      playerData.put("name", player.getName());
      playerData.put("displayName", GsonComponentSerializer.gson().serialize(player.displayName()));
      JSONObject playerLocation = new JSONObject();
      Location location = player.getLocation();
      playerLocation.put("world", location.getWorld().getName());
      playerLocation.put("x", location.getX());
      playerLocation.put("y", location.getY());
      playerLocation.put("z", location.getZ());
      playerLocation.put("pitch", location.getPitch());
      playerLocation.put("yaw", location.getYaw());
      playerData.put("location", playerLocation);
      JSONObject playerStatus = new JSONObject();
      playerStatus.put("health", player.getHealth());
      playerStatus.put("hunger", player.getFoodLevel());
      playerStatus.put("armor", player.getAttribute(Attribute.GENERIC_ARMOR).getValue());
      playerStatus.put("level", player.getLevel());
      playerStatus.put("exp", player.getExp());
      playerData.put("status", playerStatus);
      playerData.put("connection", player.getAddress().toString());
      playerData.put("isOp", player.isOp());
      players.put(playerData);
    }
    data.put("players", players);
    Terminal.send(event, message, data);
  }

}
