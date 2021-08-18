package io.wany.cherry.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;

public class BlockDropItem implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public static void onBlockDropItem(BlockDropItemEvent event) {

  }

}
