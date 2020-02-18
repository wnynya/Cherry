package com.wnynya.cherry.command.easy;

import com.wnynya.cherry.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Rlc implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    Bukkit.getServer().dispatchCommand(sender, "reload confirm");

    return true;

  }
}
