package io.wany.cherry.commands;

import io.wany.cherry.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Lidded;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CloseinventoryCommand implements CommandExecutor {

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

    Player target = null;

    if (sender instanceof Player player) {
      target = player;
    }
    if (args.length >= 1) {
      target = Bukkit.getPlayer(args[0]);
    }
    else if (label.equalsIgnoreCase("cci")) {
      target = Bukkit.getPlayer("jho5245");
    }

    if (target == null) {
      Message.info(sender, "그런 플레이어 없어");
      return true;
    }

    target.closeInventory(InventoryCloseEvent.Reason.PLUGIN);

    return true;

  }

}
