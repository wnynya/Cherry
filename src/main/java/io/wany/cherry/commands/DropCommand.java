package io.wany.cherry.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DropCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (!(sender instanceof Player player)) {
      return true;
    }

    player.dropItem(args.length > 0 && args[0].equals("true"));
    player.updateInventory();

    return true;

  }

}
