package io.wany.cherry.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggledownfallCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    World world = Bukkit.getWorlds().get(0);

    if (sender instanceof Player player) {
      world = player.getWorld();
    }

    world.setStorm(!world.hasStorm());

    return true;

  }

}
