package com.wnynya.cherry.event;

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

    //WebSocket.Message.command(event.getPlayer(), event.getMessage());

  }

  @EventHandler
  public void onServerCommandEvent(ServerCommandEvent event) {

    CommandSender sender = event.getSender();
    String command = event.getCommand();

    if (sender instanceof Player) {
    }

    else if (sender instanceof ConsoleCommandSender) {
      //WebSocket.Message.command("CONSOLE", command);
    }

    else if (sender instanceof BlockCommandSender) {
      BlockCommandSender bs = (BlockCommandSender) sender;
      if (bs.getBlock().getState() instanceof CommandBlock) {
        //WebSocket.Message.command((CommandBlock) bs.getBlock().getState(), command);
      }
      else {
        //WebSocket.Message.command(bs.getBlock().getType().name(), command);
      }
    }

    else if (sender instanceof Entity) {
      //WebSocket.Message.command((Entity) sender, command);
    }

  }

}
