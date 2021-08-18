package io.wany.cherry.listeners;

import io.wany.cherry.Cherry;
import io.wany.cherry.Console;
import io.wany.cherry.Message;
import io.wany.cherry.playerdata.PlayerData;
import io.wany.cherry.terminal.TerminalPlayers;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerQuit implements Listener {

  public static HashMap<String, String> changeQuitPlayers = new HashMap<>();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerQuit(PlayerQuitEvent event) {
    PlayerData.onQuit(event);
    TerminalPlayers.onQuit(event);
    chatPlayerQuitMessage(event);
    consolePlayerQuitMessage(event);
    playPlayerQuitSound(event);
    chatPlayerChangeQuitMessage(event);
    consolePlayerChangeQuitMessage(event);
  }

  private void chatPlayerQuitMessage(PlayerQuitEvent event) {
    if (!Cherry.CONFIG.getBoolean("event.quit.msg.normal.chat.enable")) {
      return;
    }
    Player player = event.getPlayer();
    String format = Cherry.CONFIG.getString("event.quit.msg.normal.chat.format");
    if (changeQuitPlayers.containsKey(player.getName()) && Cherry.CONFIG.getBoolean("event.quit.msg.change.chat.enable")) {
      format = Cherry.CONFIG.getString("event.quit.msg.change.chat.format");
      format = format.replace("{gotoserver}", changeQuitPlayers.get(player.getName()));
      changeQuitPlayers.remove(player.getName());
    }
    format = Message.effect(format);
    event.quitMessage(Message.formatPlayer(player, format));
  }

  private void consolePlayerQuitMessage(PlayerQuitEvent event) {
    if (!Cherry.CONFIG.getBoolean("event.quit.msg.normal.console.enable")) {
      return;
    }
    Player player = event.getPlayer();
    String format = Cherry.CONFIG.getString("event.quit.msg.normal.console.format");
    format = Message.effect(format);
    Console.log(Message.stringify(Message.formatPlayer(player, format)));
  }

  private void playPlayerQuitSound(PlayerQuitEvent event) {
    if (!Cherry.CONFIG.getBoolean("event.quit.sound.enable")) {
      return;
    }
    Sound sound = Sound.valueOf(Cherry.CONFIG.getString("event.quit.sound.sound"));
    SoundCategory soundCategory = SoundCategory.valueOf(Cherry.CONFIG.getString("event.quit.sound.soundCategory"));
    float volume = (float) Cherry.CONFIG.getDouble("event.quit.sound.volume");
    float pitch = (float) Cherry.CONFIG.getDouble("event.quit.sound.pitch");

    for (Player p : Bukkit.getOnlinePlayers()) {
      p.playSound(p.getLocation(), sound, soundCategory, volume, pitch);
    }
  }

  private void chatPlayerChangeQuitMessage(PlayerQuitEvent event) {

  }

  private void consolePlayerChangeQuitMessage(PlayerQuitEvent event) {

  }

}
