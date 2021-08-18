package io.wany.cherry.listeners;

import io.wany.cherry.itemonworld.ItemOnWorld;
import io.wany.cherry.wand.WandBrush;
import io.wany.cherry.wand.WandEdit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public static void onPlayerInteract(PlayerInteractEvent event) {

    WandEdit.onPlayerInteract(event);
    WandBrush.onPlayerInteract(event);
    ItemOnWorld.onPlayerInteract(event);

  }

}
