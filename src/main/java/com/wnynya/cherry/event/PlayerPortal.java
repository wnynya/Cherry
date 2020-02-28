package com.wnynya.cherry.event;

import com.wnynya.cherry.player.PlayerJoin;
import com.wnynya.cherry.portal.PortalEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PlayerPortal implements Listener {

  @EventHandler
  public void onPlayerPortal(PlayerPortalEvent event) {

    PortalEvent.playerPortal(event);

  }

}
