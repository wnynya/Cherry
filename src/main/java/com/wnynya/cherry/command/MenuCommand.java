package com.wnynya.cherry.command;

import com.wnynya.cherry.Msg;
import com.wnynya.cherry.gui.CherryMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MenuCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (!sender.hasPermission("cherry.menu")) {
      Msg.error(sender, Msg.NO_PERMISSION);
      return true;
    }

    org.bukkit.entity.Player player = null;
    if (sender instanceof org.bukkit.entity.Player) {
      player = (org.bukkit.entity.Player) sender;
    }
    else {
      Msg.error(sender, Msg.Player.ONLY);
      return true;
    }

    CherryMenu.MainMenu.showMenu(player);
    return true;

  }

}
