package io.wany.cherry.listeners;

import io.wany.cherry.Cherry;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityDeath(EntityDeathEvent event) {

    playMonsterKilledByPlayerSound(event);

  }

  public static void playMonsterKilledByPlayerSound(EntityDeathEvent event) {
    if (!Cherry.CONFIG.getBoolean("event.monsterDeath.killer.sound.enable")) {
      return;
    }
    LivingEntity entity = event.getEntity();
    if (!(entity instanceof Monster)) {
      return;
    }
    Player killer = entity.getKiller();
    if (killer == null) {
      return;
    }
    Sound sound = Sound.valueOf(Cherry.CONFIG.getString("event.monsterDeath.killer.sound.sound"));
    SoundCategory soundCategory = SoundCategory.valueOf(Cherry.CONFIG.getString("event.monsterDeath.killer.sound.soundCategory"));
    float volume = (float) Cherry.CONFIG.getDouble("event.monsterDeath.killer.sound.volume");
    float pitch = (float) Cherry.CONFIG.getDouble("event.monsterDeath.killer.sound.pitch");
    killer.playSound(killer.getLocation(), sound, soundCategory, volume, pitch);
  }

}
