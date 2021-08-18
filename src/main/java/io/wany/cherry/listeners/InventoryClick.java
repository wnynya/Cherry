package io.wany.cherry.listeners;

import io.wany.cherry.gui.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInventoryClick(InventoryClickEvent event) {

    Menu.onInventoryClick(event);

  }

}
