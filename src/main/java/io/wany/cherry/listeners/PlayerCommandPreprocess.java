package io.wany.cherry.listeners;

import io.wany.cherry.Cherry;
import io.wany.cherry.Message;
import io.wany.cherry.amethyst.Crystal;
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
    //String reverseString = new StringBuffer(message).reverse().toString();
    if (
      message.equals("/pl") || message.equals("/plugins") || message.equals("/bukkit:pl") || message.equals("/bukkit:plugins")
      || message.startsWith("/pl ") || message.startsWith("/plugins ") || message.startsWith("/bukkit:pl ") || message.startsWith("/bukkit:plugins ")
    ) {
      Player player = event.getPlayer();
      if (!player.hasPermission("bukkit.command.plugins")) {
        return;
      }
      event.setCancelled(true);
      player.sendMessage(Crystal.genNightPlugins());
    }
  }

}
