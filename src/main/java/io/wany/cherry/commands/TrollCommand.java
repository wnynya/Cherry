package io.wany.cherry.commands;

import io.wany.cherry.Message;
import io.wany.cherry.amethyst.troll.Troll;
import io.wany.cherry.amethyst.troll.Trolling;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TrollCommand implements CommandExecutor {

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

    if (args.length <= 0) {
      Message.error(sender, "존나짧네ㅔ요");
      return true;
    }

    Player target = Bukkit.getPlayer(args[0]);

    if (target == null) {
      Message.error(sender, "플레이어 못찾음ㅅㄱ");
      return true;
    }

    Trolling trolling;
    if (args.length == 1) {
      trolling = Troll.RAMDOM;
    }
    else {
      try {
        trolling = Troll.valueOf(args[1].toUpperCase());
      } catch (Exception e) {
        trolling = Troll.NULL;
      }
    }
    String[] trollArgs = null;
    if (args.length - 2 > 0) {
      trollArgs = new String[args.length - 2];
      System.arraycopy(args, 2, trollArgs, 0, args.length - 2);
    }

    Message.info(sender, "Troll " + target.getName() + " => " + trolling);
    trolling.troll(target, trollArgs);

    return true;

  }

}
