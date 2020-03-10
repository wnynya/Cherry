package com.wnynya.cherry.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockBreakEvent(BlockBreakEvent event) {
    //SoilEvent.harvest(event);
  }

}
