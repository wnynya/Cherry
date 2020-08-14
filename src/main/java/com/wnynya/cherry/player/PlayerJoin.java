package com.wnynya.cherry.player;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Config;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.amethyst.CucumberySupport;
import com.wnynya.cherry.portal.PortalEvent;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerJoin {

  private static ArrayList<Player> canceledPlayers = new ArrayList<>();
  private static HashMap<String, ArrayList<String>> changingPlayers = new HashMap<>();
  
  public static void server(PlayerJoinEvent event) {

    Player player = event.getPlayer();

    // First Join
    if (Config.exist("player/" + player.getUniqueId().toString())) {
      if (CucumberySupport.exist()) {
        CucumberySupport.playerFirstJoin(player);
      }
    }

    PlayerMeta.initPlayerMeta(player);

    if (changingPlayers.containsKey(player.getName())) {
      // Change Server Join
      // Player ChatBar
      if (Cherry.config.getBoolean("event.join.msg.change.chat.enable")) {
        event.setJoinMessage(null);
        PlayerJoin.Message.ChatBar.change(player, changingPlayers.get(player.getName()).get(0), changingPlayers.get(player.getName()).get(1));
      }
      // Console
      if (Cherry.config.getBoolean("event.join.msg.change.console.enable")) {
        PlayerJoin.Message.Console.change(player, changingPlayers.get(player.getName()).get(0), changingPlayers.get(player.getName()).get(1));
      }
    }
    else {
      // Normal Join
      // Player ChatBar
      if (Cherry.config.getBoolean("event.join.msg.normal.chat.enable")) {
        if (canceledPlayers.contains(player)) {
          event.setJoinMessage(null);
        }
        else {
          event.setJoinMessage(null);
          PlayerJoin.Message.ChatBar.normal(player);
        }
      }
      // Console
      if (Cherry.config.getBoolean("event.join.msg.normal.console.enable")) {
        PlayerJoin.Message.Console.normal(player);
      }
    }
    changingPlayers.remove(player.getName());
    canceledPlayers.remove(player);

    // Play Sound
    if (Cherry.config.getBoolean("event.join.sound.enable")) {
      Sound sound = Sound.valueOf(Cherry.config.getString("event.join.sound.sound"));
      SoundCategory soundCategory = SoundCategory.valueOf(Cherry.config.getString("event.join.sound.soundCategory"));
      float volume = (float) Cherry.config.getDouble("event.join.sound.volume");
      float pitch = (float) Cherry.config.getDouble("event.join.sound.pitch");

      if (Cherry.config.getBoolean("event.join.sound.targetPlayer")) {
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
          p.playSound(p.getLocation(), sound, soundCategory, volume, pitch);
        }
      }
      else {
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
          if (!p.equals(player)) {
            p.playSound(p.getLocation(), sound, soundCategory, volume, pitch);
          }
        }
      }
    }

    // setPlayerListName
    if (Cherry.config.getBoolean("event.join.list.enable")) {
      String format = Cherry.config.getString("event.join.list.format");
      player.setPlayerListName(Msg.playerFormatter(event.getPlayer(), format));
    }

    // set advancement
    Advancement advancement = Bukkit.getAdvancement(new NamespacedKey(Cherry.plugin, "wanyfield/root"));
    if (advancement != null) {
      player.getAdvancementProgress(advancement).awardCriteria("impossible");
    }

    // movetoSpawn
    if (Cherry.config.getBoolean("event.join.moveToSpawn.enable")) {
      Config spawnConfig = new Config("data/spawn", true);
      Location loc = spawnConfig.getConfig().getLocation("spawn.location");
      if (loc != null) {
        player.teleport(loc);
      }
    }

    // PortalEvent
    PortalEvent.playerJoin(event);

  }

  public static void change(String player, String fromServer, String gotoServer) {

    if (changingPlayers.containsKey(player)) {
      changingPlayers.remove(player);
    }

    ArrayList<String> servers = new ArrayList<>();
    servers.add(0, fromServer);
    servers.add(1, gotoServer);
    changingPlayers.put(player, servers);

  }

  private static class Message {

    public static class ChatBar {

      public static void normal(Player player) {
        String format = Cherry.config.getString("event.join.msg.normal.chat.format");
        if (Cherry.config.getBoolean("event.join.msg.normal.chat.targetPlayer")) {
          Msg.allP(Msg.playerFormatter(player, format));
        }
        else {
          Msg.allPwO(player, Msg.playerFormatter(player, format));
        }
      }

      public static void change(Player player, String fromServer, String gotoServer) {
        String format = Cherry.config.getString("event.join.msg.change.chat.format");
        String msg = Msg.playerFormatter(player, format);
        msg = msg.replace("{fromserver}", fromServer);
        msg = msg.replace("{gotoserver}", gotoServer);
        if (Cherry.config.getBoolean("event.join.msg.change.chat.targetPlayer")) {
          Msg.allP(msg);
        }
        else {
          Msg.allPwO(player, msg);
        }
      }

    }

    public static class Console {

      public static void normal(Player player) {
        String format = Cherry.config.getString("event.join.msg.normal.console.format");
        Msg.info(Msg.playerFormatter(player, format));
      }

      public static void change(Player player, String fromServer, String gotoServer) {
        String format = Cherry.config.getString("event.join.msg.change.console.format");
        String msg = Msg.playerFormatter(player, format);
        msg = msg.replace("{fromserver}", fromServer);
        msg = msg.replace("{gotoserver}", gotoServer);
        Msg.info(msg);
      }

    }

  }
  
}
