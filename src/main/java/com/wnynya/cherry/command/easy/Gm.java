package com.wnynya.cherry.command.easy;

import com.wnynya.cherry.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Gm implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (args.length == 0) {
      Msg.error(sender, Msg.NO_ARGS);
      return true;
    }

    if (!(sender instanceof Player)) {
      Msg.error(sender, Msg.Player.ONLY);
      return true;
    }
    Player player = (Player) sender;

    if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("sv")) {
      StringBuilder leftArgs = new StringBuilder();
      for (int n = 1; n < args.length; n++) {
        leftArgs.append(" ").append(args[n]);
      }
      player.chat("/gamemode survival" + leftArgs);
      return true;
    }

    if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c")) {
      StringBuilder leftArgs = new StringBuilder();
      for (int n = 1; n < args.length; n++) {
        leftArgs.append(" ").append(args[n]);
      }
      player.chat("/gamemode creative" + leftArgs);
      return true;
    }

    if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a")) {
      StringBuilder leftArgs = new StringBuilder();
      for (int n = 1; n < args.length; n++) {
        leftArgs.append(" ").append(args[n]);
      }
      player.chat("/gamemode adventure" + leftArgs);
      return true;
    }

    if (args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp")) {
      StringBuilder leftArgs = new StringBuilder();
      for (int n = 1; n < args.length; n++) {
        leftArgs.append(" ").append(args[n]);
      }
      player.chat("/gamemode spectator" + leftArgs);
      return true;
    }

    return false;
  }
}
