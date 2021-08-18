package io.wany.cherry.listeners;

import io.wany.cherry.Cherry;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public static void onPlayerDeath(PlayerDeathEvent event) {
    playPlayerDeathTargetSound(event);
    playPlayerDeathKillerSound(event);
  }

  private static void playPlayerDeathTargetSound(PlayerDeathEvent event) {
    if (!Cherry.CONFIG.getBoolean("event.playerDeath.target.sound.enable")) {
      return;
    }
    Player player = event.getEntity();
    Sound sound = Sound.valueOf(Cherry.CONFIG.getString("event.playerDeath.target.sound.sound"));
    SoundCategory soundCategory = SoundCategory.valueOf(Cherry.CONFIG.getString("event.playerDeath.target.sound.soundCategory"));
    float volume = (float) Cherry.CONFIG.getDouble("event.playerDeath.target.sound.volume");
    float pitch = (float) Cherry.CONFIG.getDouble("event.playerDeath.target.sound.pitch");
    player.playSound(player.getLocation(), sound, soundCategory, volume, pitch);
    if (player.getBedSpawnLocation() != null) {
      player.playSound(player.getBedSpawnLocation(), sound, soundCategory, volume, pitch);
    }
  }

  private static void playPlayerDeathKillerSound(PlayerDeathEvent event) {
    if (!Cherry.CONFIG.getBoolean("event.playerDeath.killer.sound.enable")) {
      return;
    }
    Player player = event.getEntity();
    Player killer = player.getKiller();
    if (killer == null) {
      return;
    }
    Sound sound = Sound.valueOf(Cherry.CONFIG.getString("event.playerDeath.killer.sound.sound"));
    SoundCategory soundCategory = SoundCategory.valueOf(Cherry.CONFIG.getString("event.playerDeath.killer.sound.soundCategory"));
    float volume = (float) Cherry.CONFIG.getDouble("event.playerDeath.killer.sound.volume");
    float pitch = (float) Cherry.CONFIG.getDouble("event.playerDeath.killer.sound.pitch");
    killer.playSound(killer.getLocation(), sound, soundCategory, volume, pitch);
  }

}