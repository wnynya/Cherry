package com.wnynya.cherry.event;

import com.wnynya.cherry.portal.PortalEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerMoveEvent(PlayerMoveEvent event) {

    PortalEvent.playerMove(event);

  }

}
