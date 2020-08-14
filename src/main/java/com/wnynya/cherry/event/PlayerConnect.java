package com.wnynya.cherry.event;

import com.wnynya.cherry.player.PlayerJoin;
import com.wnynya.cherry.player.PlayerQuit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnect implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerJoinEvent event) {

    PlayerJoin.server(event);

  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerQuit(PlayerQuitEvent event) {

    PlayerQuit.server(event);

  }

}
