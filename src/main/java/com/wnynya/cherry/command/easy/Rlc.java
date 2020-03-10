package com.wnynya.cherry.command.easy;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Rlc implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    Bukkit.getServer().dispatchCommand(sender, "reload confirm");

    return true;

  }
}
