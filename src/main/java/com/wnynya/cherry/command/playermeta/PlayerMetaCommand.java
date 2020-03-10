package com.wnynya.cherry.command.playermeta;

import com.wnynya.cherry.Msg;
import com.wnynya.cherry.player.PlayerMeta;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerMetaCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (args.length == 0) {
      Msg.error(sender, Msg.NO_ARGS);
      return true;
    }

    Player player = Bukkit.getPlayer(args[0]);
    if (player == null) {
      Msg.error(sender, "플레이어를 찾을 수 없습니다.");
      return true;
    }

    PlayerMeta pm = PlayerMeta.getPlayerMeta(player);

    if (args.length <= 1) {
      Msg.error(player, Msg.NO_ARGS);
      return true;
    }

    return true;
  }

}
