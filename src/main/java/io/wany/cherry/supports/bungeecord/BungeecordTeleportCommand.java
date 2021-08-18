package io.wany.cherry.supports.bungeecord;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BungeecordTeleportCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (args.length <= 1) {
      return true;
    }

    return true;

  }

}
