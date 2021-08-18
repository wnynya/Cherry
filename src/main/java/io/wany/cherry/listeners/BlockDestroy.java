package io.wany.cherry.listeners;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class BlockDestroy implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public static void onBlockDestroy(BlockDestroyEvent event) {

  }

}
