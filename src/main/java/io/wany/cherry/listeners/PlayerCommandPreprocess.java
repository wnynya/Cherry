package io.wany.cherry.listeners;

import io.wany.cherry.Cherry;
import io.wany.cherry.Message;
import io.wany.cherry.supports.cucumbery.CucumberySupport;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerCommandPreprocess implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    onPluginsCommand(event);
  }

  public void onPluginsCommand(PlayerCommandPreprocessEvent event) {
    if (!Cherry.NIGHT) {
      return;
    }
    String message = event.getMessage();
    message = message.toLowerCase();
    if (
      message.equals("/pl") || message.equals("/plugins") || message.equals("/bukkit:plugins")
      || message.startsWith("/pl ") || message.startsWith("/plugins ") || message.startsWith("/bukkit:plugins ")
    ) {
      Player player = event.getPlayer();
      if (!player.hasPermission("bukkit.command.plugins")) {
        return;
      }
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
      player.sendMessage(Message.parse(stringBuilder.toString()));
    }
  }

}
