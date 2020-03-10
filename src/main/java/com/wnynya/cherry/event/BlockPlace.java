package com.wnynya.cherry.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockPlaceEvent(BlockPlaceEvent event) {

  }

}
