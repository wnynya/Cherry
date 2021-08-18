package io.wany.cherry.listeners;

import io.wany.cherry.itemonworld.ItemOnWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class ItemSpawn implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public static void onItemSpawn(ItemSpawnEvent event) {

    ItemOnWorld.onItemSpawn(event);

  }

}
