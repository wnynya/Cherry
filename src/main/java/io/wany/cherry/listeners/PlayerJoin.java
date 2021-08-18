package io.wany.cherry.listeners;

import io.wany.cherry.Cherry;
import io.wany.cherry.Console;
import io.wany.cherry.Message;
import io.wany.cherry.playerdata.PlayerData;
import io.wany.cherry.supports.cucumbery.CucumberySupport;
import io.wany.cherry.supports.vault.VaultEconomySync;
import io.wany.cherry.terminal.TerminalPlayers;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class PlayerJoin implements Listener {

  public static HashMap<String, String> changeJoinPlayers = new HashMap<>();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    CucumberySupport.onPlayerJoin(event);
    PlayerData.onJoin(event);
    TerminalPlayers.onJoin(event);
    chatPlayerJoinMessage(event);
    consolePlayerJoinMessage(event);
    playPlayerJoinSound(event);
    updatePlayerJoin(event);
    VaultEconomySync.onPlayerJoin(event);
  }

  private void chatPlayerJoinMessage(PlayerJoinEvent event) {
    if (!Cherry.CONFIG.getBoolean("event.join.msg.normal.chat.enable")) {
      return;
    }
    Player player = event.getPlayer();
    String format = Cherry.CONFIG.getString("event.join.msg.normal.chat.format");
    if (changeJoinPlayers.containsKey(player.getName()) && Cherry.CONFIG.getBoolean("event.join.msg.change.chat.enable")) {
      format = Cherry.CONFIG.getString("event.join.msg.change.chat.format");
      format = format.replace("{fromserver}", changeJoinPlayers.get(player.getName()));
      changeJoinPlayers.remove(player.getName());
    }
    format = Message.effect(format);
    event.joinMessage(Message.formatPlayer(player, format));
  }

  private void consolePlayerJoinMessage(PlayerJoinEvent event) {
    if (!Cherry.CONFIG.getBoolean("event.join.msg.normal.console.enable")) {
      return;
    }
    Player player = event.getPlayer();
    String format = Cherry.CONFIG.getString("event.join.msg.normal.console.format");
    format = Message.effect(format);
    Console.log(Message.stringify(Message.formatPlayer(player, format)));
  }

  private void playPlayerJoinSound(PlayerJoinEvent event) {
    if (!Cherry.CONFIG.getBoolean("event.join.sound.enable")) {
      return;
    }
    Player player = event.getPlayer();
    Sound sound = Sound.valueOf(Cherry.CONFIG.getString("event.join.sound.sound"));
    SoundCategory soundCategory = SoundCategory.valueOf(Cherry.CONFIG.getString("event.join.sound.soundCategory"));
    float volume = (float) Cherry.CONFIG.getDouble("event.join.sound.volume");
    float pitch = (float) Cherry.CONFIG.getDouble("event.join.sound.pitch");

    if (Cherry.CONFIG.getBoolean("event.join.sound.targetPlayer")) {
      for (Player p : Bukkit.getOnlinePlayers()) {
        p.playSound(p.getLocation(), sound, soundCategory, volume, pitch);
      }
    }
    else {
      for (Player p : Bukkit.getOnlinePlayers()) {
        if (!p.equals(player)) {
          p.playSound(p.getLocation(), sound, soundCategory, volume, pitch);
        }
      }
    }
  }

  private void updatePlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (Cherry.CONFIG.getBoolean("event.join.list.enable")) {
      String format = Cherry.CONFIG.getString("event.join.list.format");
      format = Message.effect(format);
      player.playerListName(Message.formatPlayer(player, format));
    }
  }

}
