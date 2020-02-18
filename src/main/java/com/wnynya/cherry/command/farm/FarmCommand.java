package com.wnynya.cherry.command.farm;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.farm.Fertilizer;
import com.wnynya.cherry.farm.Soil;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FarmCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (args.length == 0) {
      Msg.error(sender, Msg.NO_ARGS);
      return true;
    }

    if (args[0].equalsIgnoreCase("soilinfo")) {

      Player player = null;
      if (sender instanceof Player) {
        player = (Player) sender;
      }
      else {
        Msg.error(sender, Msg.Player.ONLY);
        return true;
      }

      Block block = player.getTargetBlock(10);
      if (block == null) {
        return true;
      }

      Soil soil = Soil.getSoil(block.getLocation());

      Msg.info(player, Msg.Prefix.INFO + "토양 정보: 질소: " + soil.getN() + ", 인산: " + soil.getP() + ", 칼륨: " + soil.getK());

      return true;
    }

    if (args[0].equalsIgnoreCase("fertp")) {

      Player player = null;
      if (sender instanceof Player) {
        player = (Player) sender;
      }
      else {
        Msg.error(sender, Msg.Player.ONLY);
        return true;
      }

      Block block = player.getTargetBlock(10);
      if (block == null) {
        return true;
      }

      Soil soil = Soil.getSoil(block.getLocation());

      soil.fertilize(Fertilizer.FP);

      Msg.info(player, Msg.Prefix.INFO + "토양 정보: 질소: " + soil.getN() + ", 인산: " + soil.getP() + ", 칼륨: " + soil.getK());

      return true;
    }

    if (args[0].equalsIgnoreCase("fertm")) {

      Player player = null;
      if (sender instanceof Player) {
        player = (Player) sender;
      }
      else {
        Msg.error(sender, Msg.Player.ONLY);
        return true;
      }

      Block block = player.getTargetBlock(10);
      if (block == null) {
        return true;
      }

      Soil soil = Soil.getSoil(block.getLocation());

      soil.fertilize(Fertilizer.FM);

      Msg.info(player, Msg.Prefix.INFO + "토양 정보: 질소: " + soil.getN() + ", 인산: " + soil.getP() + ", 칼륨: " + soil.getK());

      return true;
    }

    return true;
  }
}
