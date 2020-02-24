package com.wnynya.cherry.event;

import com.wnynya.cherry.player.PlayerJoin;
import com.wnynya.cherry.player.PlayerQuit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnect implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {

    PlayerJoin.server(event);

  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {

    PlayerQuit.server(event);

  }

}
