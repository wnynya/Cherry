package io.wany.cherry.listeners;

import io.wany.cherry.Cherry;
import io.wany.cherry.amethyst.Color;
import io.wany.cherry.amethyst.Crystal;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RemoteServerCommand implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onRemoteServerCommand(RemoteServerCommandEvent event) {
    onPluginsCommand(event);
  }

  public void onPluginsCommand(RemoteServerCommandEvent event) {
    if (!Cherry.NIGHT) {
      return;
    }
    String message = event.getCommand();
    message = message.toLowerCase();
    if (
      message.equals("pl") || message.equals("plugins") || message.equals("bukkit:pl") || message.equals("bukkit:plugins")
        || message.startsWith("pl ") || message.startsWith("plugins ") || message.startsWith("bukkit:pl ") || message.startsWith("bukkit:plugins ")
    ) {
      event.setCancelled(true);
      Bukkit.getConsoleSender().sendMessage(Crystal.genNightPlugins());
    }
  }

}
