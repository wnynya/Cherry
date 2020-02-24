package com.wnynya.cherry.event;

import com.wnynya.cherry.amethyst.NoteTool;
import com.wnynya.cherry.amethyst.Item2Block;
import com.wnynya.cherry.farm.SoilEvent;
import com.wnynya.cherry.wand.WandEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteractEvent(PlayerInteractEvent event) {

    WandEvent.playerInteract(event);

    NoteTool.clickNoteBlock(event);

    Item2Block.blockClick(event);

  }

}
