package io.wany.cherry.listeners;

import org.bukkit.event.*;

public class PlayerEvent implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public static void onPlayerEvent(org.bukkit.event.player.PlayerEvent event) {
    PlayerDeath.onDeathPlayerEvent(event);
  }

  public static HandlerList getHandlerList(Event event) {
    return event.getHandlers();
  }

}
