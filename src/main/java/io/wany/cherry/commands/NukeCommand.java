package io.wany.cherry.commands;

import io.wany.cherry.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NukeCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    Message.info(sender, "nuke fuck");

    return true;

  }

}
