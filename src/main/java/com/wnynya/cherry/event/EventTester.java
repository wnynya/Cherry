package com.wnynya.cherry.event;

import com.wnynya.cherry.Msg;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventTester implements Listener {

  @EventHandler
  public void onEvent(PlayerInteractEvent event) {

    Msg.info("pie");

  }

  @EventHandler
  public void onEvent(InventoryClickEvent event) {

    Msg.info("ice");

    String hi = "hello";

    String a = "hi";

  }

}
