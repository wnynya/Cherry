package com.wnynya.cherry.event;

import com.wnynya.cherry.amethyst.NoteTool;
import com.wnynya.cherry.portal.PortalEvent;
import com.wnynya.cherry.wand.WandEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteractEvent(PlayerInteractEvent event) {

    WandEvent.playerInteract(event);

    PortalEvent.playerInteract(event);

    NoteTool.clickNoteBlock(event);

  }

}
