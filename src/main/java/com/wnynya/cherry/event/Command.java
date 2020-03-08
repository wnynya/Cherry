package com.wnynya.cherry.event;

import com.wnynya.cherry.amethyst.WebSocketClient;
import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

public class Command implements Listener {

  @EventHandler
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

    if (event.getMessage().equalsIgnoreCase("/reload")) {
      Bukkit.getServer().dispatchCommand(event.getPlayer(), "reload confirm");
      event.setCancelled(true);
    }

    WebSocketClient.Message.command(event.getPlayer(), event.getMessage());

  }

  @EventHandler
  public void onServerCommandEvent(ServerCommandEvent event) {

    CommandSender sender = event.getSender();
    String command = event.getCommand();

    if (sender instanceof Player) {
    }

    else if (sender instanceof ConsoleCommandSender) {
      WebSocketClient.Message.command("CONSOLE", command);
    }

    else if (sender instanceof BlockCommandSender) {
      BlockCommandSender bs = (BlockCommandSender) sender;
      if (bs.getBlock().getState() instanceof CommandBlock) {
        WebSocketClient.Message.command((CommandBlock) bs.getBlock().getState(), command);
      }
      else {
        WebSocketClient.Message.command(bs.getBlock().getType().name(), command);
      }
    }

    else if (sender instanceof Entity) {
      WebSocketClient.Message.command((Entity) sender, command);
    }

  }

}
