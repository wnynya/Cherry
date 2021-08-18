package io.wany.cherry.listeners;

import io.wany.cherry.Cherry;
import io.wany.cherry.amethyst.Color;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerCommand implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onServerCommand(ServerCommandEvent event) {
    onPluginsCommand(event);
  }

  public void onPluginsCommand(ServerCommandEvent event) {
    if (!Cherry.NIGHT) {
      return;
    }
    String message = event.getCommand();
    message = message.toLowerCase();
    if (
      message.equals("pl") || message.equals("plugins") || message.equals("bukkit:plugins")
        || message.startsWith("pl ") || message.startsWith("plugins ") || message.startsWith("bukkit:plugins ")
    ) {
      Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
      List<String> filter = List.of(Cherry.PLUGIN.getName());
      List<String> pluginNames = new ArrayList<>();
      for (Plugin plugin : plugins) {
        if (filter.contains(plugin.getName())) {
          continue;
        }
        if (plugin.isEnabled()) {
          if (plugin.getDescription().getAPIVersion() == null) {
            pluginNames.add("§a" + plugin.getName() + "*");
          }
          else {
            pluginNames.add("§a" + plugin.getName());
          }
        }
        else {
          pluginNames.add("§c" + plugin.getName());
        }
      }
      StringBuilder stringBuilder = new StringBuilder();
      Collections.sort(pluginNames);
      stringBuilder.append("§rPlugins (").append(pluginNames.size()).append("): ");
      for (var i = 0; i < pluginNames.size(); i++) {
        stringBuilder.append(pluginNames.get(i));
        if (i < pluginNames.size() - 1) {
          stringBuilder.append("§r, ");
        }
      }
      event.setCancelled(true);
      Bukkit.getConsoleSender().sendMessage(Color.mfc2ansi(stringBuilder.toString()));
    }
  }

}
