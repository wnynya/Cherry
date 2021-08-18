package io.wany.cherry.wand.command;

import io.wany.cherry.Message;
import io.wany.cherry.wand.Wand;
import io.wany.cherry.wand.WandBrush;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WandBrushCommand implements CommandExecutor {

  public static double parseRelativeWave(double d, String s) throws Exception {
    double x = 0;
    try {
      x = Double.parseDouble(s);
    }
    catch (Exception e) {
      if (s.startsWith("~")) {
        try {
          double i = Double.parseDouble(s.substring(1));
          if (d < 0) {
            d--;
          }
          x = d + i;
        }
        catch (Exception ex) {
          throw new Exception("시발");
        }
      }
    }
    return x;
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (!Wand.ENABLED) {
      Message.info(sender, Wand.PREFIX, "완드 기능이 비활성화된 상태입니다");
      return true;
    }

    UUID uuid;
    Player player = null;
    Wand wand;
    WandBrush wandBrush;

    if (sender instanceof Player) {
      player = (Player) sender;
      wand = Wand.getWand(player);
      wandBrush = wand.getBrush();
    }
    else {
      uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
      wand = Wand.getWand(uuid);
      wandBrush = wand.getBrush();
    }

    if (args.length == 0) {
      Message.error(sender, Message.CommandFeedback.NO_ARGS);
      return true;
    }

    args[0] = args[0].toLowerCase();

    switch (args[0]) {

      case "touch": {

        Location loc;

        if (args.length >= 5) {
          World world = Bukkit.getWorld(args[4]);
          if (world == null) {
            Message.error(sender, "월드를 찾을 수 없습니다.");
            return true;
          }
          Location tloc = new Location(world, 0, 0, 0);
          if (player != null) {
            tloc = player.getLocation();
          }
          try {
            int x = (int) parseRelativeWave(tloc.getX(), args[1]);
            int y = (int) parseRelativeWave(tloc.getY(), args[2]);
            int z = (int) parseRelativeWave(tloc.getZ(), args[3]);
            loc = new Location(world, x, y, z);
          }
          catch (Exception e) {
            Message.error(sender, "올바른 좌표가 아닙니다.");
            return true;
          }
        }
        else if (args.length >= 4) {
          if (player == null) {
            Message.error(sender, Message.CommandFeedback.ONLY_PLAYER);
            return true;
          }
          World world = player.getWorld();
          try {
            int x = (int) parseRelativeWave(player.getLocation().getX(), args[1]);
            int y = (int) parseRelativeWave(player.getLocation().getY(), args[2]);
            int z = (int) parseRelativeWave(player.getLocation().getZ(), args[3]);
            loc = new Location(world, x, y, z);
          }
          catch (Exception e) {
            Message.error(sender, "올바른 좌표가 아닙니다.");
            return true;
          }
        }
        else {
          if (player == null) {
            Message.error(sender, Message.CommandFeedback.ONLY_PLAYER);
            return true;
          }
          Block block = player.getTargetBlock(wandBrush.reach);
          if (block == null) {
            return true;
          }
          if (block.getType().equals(Material.AIR)) {
            return true;
          }
          loc = block.getLocation();
        }

        boolean applyPhysics = false;
        boolean silent = false;
        if (args.length > 5) {
          for (String str : args) {
            if (str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
              applyPhysics = true;
            }
            if (str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
              silent = true;
            }
          }
        }

        wandBrush.brush.touch(loc, wand, applyPhysics);

        return true;
      }

      case "b":
      case "brush": {

        return true;
      }

      case "r":
      case "radius": {

        // blockdata
        String radiusString = args[1];
        int radius;
        try {
          radius = Integer.parseInt(radiusString);
        }
        catch (Exception e) {
          Wand.error(sender, "0 <= radius");
          return true;
        }

        // options
        boolean silent = false;
        int commandArgsLength = 2;
        if (args.length > commandArgsLength) {
          for (String str : args) {
            if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
              silent = true;
            }
          }
        }

        wandBrush.setting.setSize(radius);

        if (!silent) {
          Wand.info(sender, "Set brush radius: &6" + radius + "");
        }

        return true;
      }

      case "v":
      case "block": {

        // blockdata
        String blockDataString = args[1];
        BlockData blockData;
        try {
          blockData = Wand.getBlockData(blockDataString);
        }
        catch (Exception e) {
          Wand.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
          return true;
        }

        // options
        boolean silent = false;
        int commandArgsLength = 2;
        if (args.length > commandArgsLength) {
          for (String str : args) {
            if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
              silent = true;
            }
          }
        }

        wandBrush.setting.setBlockData(blockData);

        if (!silent) {
          Wand.info(sender, "Set brush block data: &b" + Wand.blockDataBeauty(blockData) + "");
        }

        return true;
      }

    }

    return true;
  }

}
