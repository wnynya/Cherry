package io.wany.cherry.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class BlockExplode implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public static void onBlockExplode(BlockExplodeEvent event) {

  }

}
