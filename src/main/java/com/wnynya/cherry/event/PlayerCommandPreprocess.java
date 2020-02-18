package com.wnynya.cherry.event;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocess implements Listener {

  @EventHandler
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

    if (event.getMessage().equalsIgnoreCase("/reload")) {
      Bukkit.getServer().dispatchCommand(event.getPlayer(), "reload confirm");
      event.setCancelled(true);
    }

  }

}
