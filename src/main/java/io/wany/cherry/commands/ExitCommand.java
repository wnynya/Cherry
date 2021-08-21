package io.wany.cherry.commands;

import io.wany.cherry.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExitCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (!(sender instanceof Player player)) {
      return true;
    }

    player.kick(Message.parse("Bye"));

    return true;

  }

}
