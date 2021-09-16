package io.wany.cherry.listeners;

import io.wany.cherry.Cherry;
import io.wany.cherry.Console;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

  public static HashMap<UUID, DeathPlayerData> deathPlayers = new HashMap<>();

  private static class DeathPlayerData {
    public UUID uuid;
    public long time;
    public GameMode gamemode;
    DeathPlayerData(UUID uuid, long time, GameMode gamemode) {
      this.uuid = uuid;
      this.time = time;
      this.gamemode = gamemode;
    }
  }

  private static void playerDeath(PlayerDeathEvent event) {
    event.setCancelled(true);
    Player player = event.getEntity();
    deathPlayers.put(player.getUniqueId(), new DeathPlayerData(player.getUniqueId(), System.currentTimeMillis(), player.getGameMode()));
    // item drop
    List<ItemStack> drops = event.getDrops();
    if (drops.size() <= 0) {
      player.getInventory().clear();
    }
    for (ItemStack item : drops) {
      player.getWorld().dropItemNaturally(player.getLocation(), item);
    }
    // exp drop
    if (event.getDroppedExp() != 0) {
      ExperienceOrb exp = (ExperienceOrb) player.getWorld().spawnEntity(player.getLocation(), EntityType.EXPERIENCE_ORB);
      exp.setExperience(event.getDroppedExp());
    }
    Console.log(event.getDroppedExp() + " " + event.getNewExp() + " " + event.getNewLevel() + " " + event.getNewTotalExp());
    Component deathMessage = event.deathMessage();
    Bukkit.broadcast(deathMessage != null ? deathMessage : Component.text("뭐"));
    player.setGameMode(GameMode.SPECTATOR);
    event.deathMessage();
  }

  protected static void onDeathPlayerEvent(PlayerEvent event) {
    Player player = event.getPlayer();
    if (deathPlayers.containsKey(player.getUniqueId())) {
      if (event instanceof PlayerMoveEvent playerMoveEvent) {
      }
      else if (event instanceof Cancellable cancellable) {
        cancellable.setCancelled(true);
        if (event instanceof PlayerInteractEvent playerInteractEvent) {
          onDeathPlayerInteract(playerInteractEvent);
        }
      }
    }
  }

  private static void onDeathPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    DeathPlayerData dpd = deathPlayers.get(player.getUniqueId());
    if (dpd.time < System.currentTimeMillis() - 2000) {
      player.sendMessage("Respawnseq");
      player.setGameMode(dpd.gamemode);
      deathPlayers.remove(player.getUniqueId());
    }
  }


  public static void onEnable() {

  }

  public static void onDisable() {

  }

}