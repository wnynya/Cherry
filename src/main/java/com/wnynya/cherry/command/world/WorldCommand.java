package com.wnynya.cherry.command.world;

import com.wnynya.cherry.Msg;
import com.wnynya.cherry.world.CherryWorld;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class WorldCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (args.length == 0) {
      Msg.error(sender, Msg.NO_ARGS);
      return true;
    }

    if (args[0].equalsIgnoreCase("load")) {
      if (args.length < 2) {
        return true;
      }

      String worldName = args[1];

      Msg.info(sender, Msg.Prefix.INFO + worldName + " 월드를 로드합니다...");

      try {
        CherryWorld.load(worldName);
      }
      catch (Exception e) {
        Msg.warn(sender, worldName + " 월드 로드에 실패하였습니다. (" + e.getMessage() + ")");
        return true;
      }

      Msg.info(sender, Msg.Prefix.INFO + worldName + " 월드 로드를 완료하였습니다.");
      return true;
    }

    if (args[0].equalsIgnoreCase("unload")) {
      if (args.length < 2) {
        return true;
      }

      String worldName = args[1];

      Msg.info(sender, Msg.Prefix.INFO + worldName + " 월드를 언로드합니다...");

      try {
        CherryWorld.unload(worldName);
      }
      catch (Exception e) {
        Msg.warn(sender, worldName + " 월드 언로드에 실패하였습니다. (" + e.getMessage() + ")");
        return true;
      }

      Msg.info(sender, Msg.Prefix.INFO + worldName + " 월드 언로드를 완료하였습니다.");
      return true;
    }

    if (args[0].equalsIgnoreCase("send")) {
      if (args.length < 3) {
        return true;
      }

      String playerName = args[1];

      Player player = Bukkit.getServer().getPlayer(playerName);

      if (player == null) {
        Msg.error(sender, Msg.NO_PLAYER);
        return true;
      }

      String worldName = args[2];

      try {
        CherryWorld.send(player, worldName);
      }
      catch (Exception e) {
        Msg.warn(sender, player.getDisplayName() + "(을)를 " + worldName + " 월드로 보내는 데 실패하였습니다. (" + e.getMessage() + ")");
        return true;
      }

      Msg.info(sender, Msg.Prefix.INFO + player.getDisplayName() + "(을)를 " + worldName + " 월드로 보냈습니다.");
      return true;
    }

    if (args[0].equalsIgnoreCase("list")) {
      List<String> worlds = (List<String>) CherryWorld.getWorldConfig().getConfig().getList("worlds");
      Msg.info(sender, Msg.Prefix.INFO + "콘피그에 설정된 월드 목록:");
      for (String worldName : worlds) {
        Msg.info(sender, Msg.Prefix.INFO + worldName);
      }
      Msg.info(sender, Msg.Prefix.INFO + "서버에 로드된 월드 목록:");
      for (String worldName : worlds) {
        if (Bukkit.getWorld(worldName) != null) {
          Msg.info(sender, Msg.Prefix.INFO + worldName);
        }
      }
      return true;
    }

    if (args[0].equalsIgnoreCase("goto")) {
      if (args.length < 2) {
        return true;
      }

      Player player;
      if (sender instanceof Player) {
        player = (Player) sender;
      }
      else {
        Msg.error(sender, Msg.Player.ONLY);
        return true;
      }

      String worldName = args[1];

      try {
        CherryWorld.send(player, worldName);
      }
      catch (Exception e) {
        Msg.warn(sender, worldName + " 월드로 이동하는 데 실패하였습니다. (" + e.getMessage() + ")");
        return true;
      }

      Msg.info(sender, Msg.Prefix.INFO + worldName + " 월드로 이동합니다.");
      return true;
    }

    Msg.error(sender, Msg.UNKNOWN);
    return true;
  }
}
