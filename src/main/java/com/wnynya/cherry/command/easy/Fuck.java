package com.wnynya.cherry.command.easy;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Fuck implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (args.length == 1) {
      if (Cherry.config.getString("fuck").equals("yeah")) {
        if (args[0].equals("yeah")) {
          try {
            for (Player p : Bukkit.getOnlinePlayers()) {
              p.kickPlayer(Msg.effect(""));
            }
            Cherry.boom(0);
          }
          catch (Exception ignored) {
          }
        }
      }
    }

    return true;

  }

}