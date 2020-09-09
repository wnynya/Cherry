package com.wnynya.cherry.event;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.gui.CherryMenuEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onInventoryClickEvent(InventoryClickEvent event) {

    CherryMenuEvent.inventoryClick(event);
  }

}
