package com.wnynya.cherry.player;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerQuit {

  private static ArrayList<Player> canceledPlayers = new ArrayList<>();
  private static HashMap<Player, ArrayList<String>> changingPlayers = new HashMap<>();

  public static void server(PlayerQuitEvent event) {

    Player player = event.getPlayer();

    if (changingPlayers.containsKey(player)) {
      // Player ChatBar
      if (Cherry.config.getBoolean("event.quit.msg.change.chat.enable")) {
        event.setQuitMessage(null);
        Message.ChatBar.change(player, changingPlayers.get(player).get(0), changingPlayers.get(player).get(1));
      }
      // Console
      if (Cherry.config.getBoolean("event.quit.msg.change.console.enable")) {
        Message.Console.change(player, changingPlayers.get(player).get(0), changingPlayers.get(player).get(1));
      }
    }
    else {
      // Player ChatBar
      if (Cherry.config.getBoolean("event.quit.msg.normal.chat.enable")) {
        if (canceledPlayers.contains(player)) {
          event.setQuitMessage(null);
        }
        else {
          event.setQuitMessage(null);
          Message.ChatBar.normal(player);
        }
      }
      // Console
      if (Cherry.config.getBoolean("event.quit.msg.normal.console.enable")) {
        Message.Console.normal(player);
      }
    }
    canceledPlayers.remove(player);
    changingPlayers.remove(player);

    // Play Sound
    if (Cherry.config.getBoolean("event.quit.sound.enable")) {
      Sound sound = Sound.valueOf(Cherry.config.getString("event.quit.sound.sound"));
      SoundCategory soundCategory = SoundCategory.valueOf(Cherry.config.getString("event.quit.sound.soundCategory"));
      float volume = (float) Cherry.config.getDouble("event.quit.sound.volume");
      float pitch = (float) Cherry.config.getDouble("event.quit.sound.pitch");

      for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
        if (!p.equals(player)) {
          p.playSound(p.getLocation(), sound, soundCategory, volume, pitch);
        }
      }
    }

  }

  public static void change(Player player, String fromServer, String gotoServer) {

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
        String format = Cherry.config.getString("event.quit.msg.normal.chat.format");
        Msg.allPwO(player, Msg.playerFormatter(player, format));
      }

      public static void change(Player player, String fromServer, String gotoServer) {
        String format = Cherry.config.getString("event.quit.msg.change.chat.format");
        String msg = Msg.playerFormatter(player, format);
        msg = msg.replace("{fromserver}", fromServer);
        msg = msg.replace("{gotoserver}", gotoServer);
        Msg.allPwO(player, msg);
      }

    }

    public static class Console {

      public static void normal(Player player) {
        String format = Cherry.config.getString("event.quit.msg.normal.console.format");
        Msg.info(Msg.playerFormatter(player, format));
      }

      public static void change(Player player, String fromServer, String gotoServer) {
        String format = Cherry.config.getString("event.quit.msg.change.console.format");
        String msg = Msg.playerFormatter(player, format);
        msg = msg.replace("{fromserver}", fromServer);
        msg = msg.replace("{gotoserver}", gotoServer);
        Msg.info(msg);
      }

    }

  }

}
