package io.wany.cherry.commands;

import io.wany.cherry.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Lidded;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LidCommand implements CommandExecutor {

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

    String type = "toggle";

    if (args.length >= 1) {
      type = args[0];
      if (!List.of("toggle", "open", "close").contains(args[0])) {
        sender.sendMessage(Message.commandErrorTranslatable("command.unknown.argument"));
        sender.sendMessage(Message.commandErrorArgsComponent(label, args, 0));
        return true;
      }
    }

    Block block;
    if (args.length == 5) {
      double x, y, z;
      try {
        x = Double.parseDouble(args[1]);
      } catch (Exception e) {
        sender.sendMessage(Message.commandErrorTranslatable("parsing.int.expected"));
        sender.sendMessage(Message.commandErrorArgsComponent(label, args, 1));
        return true;
      }
      if (x < -30000000 || 30000000 < x || x % 1 != 0) {
        sender.sendMessage(Message.commandErrorTranslatable("parsing.int.invalid", Message.parse(x)));
        sender.sendMessage(Message.commandErrorArgsComponent(label, args, 1));
        return true;
      }
      try {
        y = Double.parseDouble(args[2]);
      } catch (Exception e) {
        sender.sendMessage(Message.commandErrorTranslatable("parsing.int.expected"));
        sender.sendMessage(Message.commandErrorArgsComponent(label, args, 2));
        return true;
      }
      if (y < 0 || 255 < y || y % 1 != 0) {
        sender.sendMessage(Message.commandErrorTranslatable("parsing.int.invalid", Message.parse(y)));
        sender.sendMessage(Message.commandErrorArgsComponent(label, args, 2));
        return true;
      }
      try {
        z = Double.parseDouble(args[3]);
      } catch (Exception e) {
        sender.sendMessage(Message.commandErrorTranslatable("parsing.int.expected"));
        sender.sendMessage(Message.commandErrorArgsComponent(label, args, 3));
        return true;
      }
      if (z < -30000000 || 30000000 < z || z % 1 != 0) {
        sender.sendMessage(Message.commandErrorTranslatable("parsing.int.invalid", Message.parse(z)));
        sender.sendMessage(Message.commandErrorArgsComponent(label, args, 3));
        return true;
      }
      World world = Bukkit.getWorld(args[4]);
      if (world == null) {
        sender.sendMessage(Message.commandErrorTranslatable("월드를 찾을 수 없습니다"));
        return true;
      }
      block = new Location(world, x, y, z).getBlock();
    }
    else if (args.length == 4) {
      double x, y, z;
      try {
        x = Double.parseDouble(args[1]);
      } catch (Exception e) {
        sender.sendMessage(Message.commandErrorTranslatable("parsing.int.expected"));
        sender.sendMessage(Message.commandErrorArgsComponent(label, args, 1));
        return true;
      }
      if (x < -30000000 || 30000000 < x || x % 1 != 0 || args[1].contains(".")) {
        sender.sendMessage(Message.commandErrorTranslatable("parsing.int.invalid", Message.parse(args[1])));
        sender.sendMessage(Message.commandErrorArgsComponent(label, args, 1));
        return true;
      }
      try {
        y = Double.parseDouble(args[2]);
      } catch (Exception e) {
        sender.sendMessage(Message.commandErrorTranslatable("parsing.int.expected"));
        sender.sendMessage(Message.commandErrorArgsComponent(label, args, 2));
        return true;
      }
      if (y < 0 || 255 < y || y % 1 != 0 || args[2].contains(".")) {
        sender.sendMessage(Message.commandErrorTranslatable("parsing.int.invalid", Message.parse(args[2])));
        sender.sendMessage(Message.commandErrorArgsComponent(label, args, 2));
        return true;
      }
      try {
        z = Double.parseDouble(args[3]);
      } catch (Exception e) {
        sender.sendMessage(Message.commandErrorTranslatable("parsing.int.expected"));
        sender.sendMessage(Message.commandErrorArgsComponent(label, args, 3));
        return true;
      }
      if (z < -30000000 || 30000000 < z || z % 1 != 0 || args[3].contains(".")) {
        sender.sendMessage(Message.commandErrorTranslatable("parsing.int.invalid", Message.parse(args[3])));
        sender.sendMessage(Message.commandErrorArgsComponent(label, args, 3));
        return true;
      }
      if (!(sender instanceof Player player)) {
        block = new Location(Bukkit.getWorlds().get(0), x, y, z).getBlock();
      }
      else {
        block = new Location(player.getWorld(), x, y, z).getBlock();
      }
    }
    else if (args.length == 3 || args.length == 2) {
      sender.sendMessage(Message.commandErrorTranslatable("argument.pos3d.incomplete"));
      sender.sendMessage(Message.commandErrorArgsComponent(label, args, 1));
      return true;
    }
    else {
      if (!(sender instanceof Player player)) {
        block = Bukkit.getWorlds().get(0).getSpawnLocation().getBlock();
      }
      else {
        block = player.getTargetBlock(10);
      }
    }

    if (block == null) {
      sender.sendMessage(Message.commandErrorTranslatable("블록을 찾을 수 없습니다"));
      return true;
    }

    BlockState blockState = block.getState();

    if (!(blockState instanceof Lidded lidded)) {
      sender.sendMessage(Message.commandErrorTranslatable("commands.setblock.failed"));
      return true;
    }

    switch (type) {
      case "toggle" -> {
        if (lidded.isOpen()) {
          lidded.close();
        }
        else {
          lidded.open();
        }
      }
      case "open" -> lidded.open();
      case "close" -> lidded.close();
    }

    return true;

  }

}
