package com.wnynya.cherry.wand.command;

import com.wnynya.cherry.Msg;
import com.wnynya.cherry.Tool;
import com.wnynya.cherry.command.TabCompleter;
import com.wnynya.cherry.player.PlayerState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WandTabCompleter implements org.bukkit.command.TabCompleter {
  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }

    if (player != null) {
      PlayerState ps = PlayerState.getPlayerState(player);
      ps.setCancelTabComplete(false);
    }

    if (command.getName().equalsIgnoreCase("wand")) {

      if (args.length == 1) {
        List<String> list = new ArrayList<>();
        if (sender.hasPermission("cherry.wand.get")) {
          list.add("get");
        }
        if (sender.hasPermission("cherry.wand.undo")) {
          list.add("undo");
        }
        if (sender.hasPermission("cherry.wand.redo")) {
          list.add("redo");
        }
        if (sender.hasPermission("cherry.wand.stack")) {
          list.add("stack");
        }
        if (sender.hasPermission("cherry.wand.pos")) {
          list.addAll(Arrays.asList("pos1", "pos2"));
        }
        if (sender.hasPermission("cherry.wand.copy")) {
          list.add("copy");
        }
        if (sender.hasPermission("cherry.wand.cut")) {
          list.add("cut");
        }
        if (sender.hasPermission("cherry.wand.paste")) {
          list.add("paste");
        }
        if (sender.hasPermission("cherry.wand.paste")) {
          list.add("rotate");
        }
        if (sender.hasPermission("cherry.wand.replace")) {
          list.add("replace");
        }
        if (sender.hasPermission("cherry.wand.replacenear")) {
          list.add("replacenear");
        }
        if (sender.hasPermission("cherry.wand.edit.cube")) {
          list.addAll(Arrays.asList("cube", "emptycube", "walledcube"));
          list.addAll(Arrays.asList("ecube", "wcube"));
        }
        if (sender.hasPermission("cherry.wand.edit.cyl")) {
          list.addAll(Arrays.asList("cyl", "emptycyl"));
          list.addAll(Arrays.asList("pointcyl", "emptypointcyl"));
          list.addAll(Arrays.asList("ecyl", "pcyl", "epcyl"));
        }
        if (sender.hasPermission("cherry.wand.edit.sphere")) {
          list.addAll(Arrays.asList("sphere", "emptysphere"));
          list.addAll(Arrays.asList("pointsphere", "emptypointsphere"));
          list.addAll(Arrays.asList("esphere", "psphere", "epsphere"));
        }
        if (sender.hasPermission("cherry.wand.edit.wall")) {
          list.add("wall");
        }
        if (sender.hasPermission("cherry.wand.edit.cmdscan")) {
          list.add("cmdscan");
        }
        return TabCompleter.autoComplete(list, args[args.length - 1]);
      }

      args[0] = args[0].toLowerCase();

      if (args[0].equals("pos1") || args[0].equals("pos2")) {

        if (!sender.hasPermission("cherry.wand.pos")) {
          return Collections.emptyList();
        }

        if (args.length == 2) {
          if (args[args.length - 1].isEmpty()) {
            if (player != null) {
              Block block = player.getTargetBlock(10);
              if (block != null && !block.getType().isAir()) {
                return Collections.singletonList(block.getLocation().getBlockX() + "");
              }
              else {
                return Collections.singletonList(player.getLocation().getBlockX() + "");
              }
            }
            return Collections.singletonList("<X>");
          }
          else {
            return onIntegerTabComplete("X", 30000000, -30000000, args[args.length - 1]);
          }
        }

        if (args.length == 3) {
          if (args[args.length - 1].isEmpty()) {
            if (player != null) {
              Block block = player.getTargetBlock(10);
              if (block != null && !block.getType().isAir()) {
                return Collections.singletonList(block.getLocation().getBlockY() + "");
              }
              else {
                return Collections.singletonList(player.getLocation().getBlockY() + "");
              }
            }
            return Collections.singletonList("<Y>");
          }
          else {
            return onIntegerTabComplete("X", 256, 0, args[args.length - 1]);
          }
        }

        if (args.length == 4) {
          if (args[args.length - 1].isEmpty()) {
            if (player != null) {
              Block block = player.getTargetBlock(10);
              if (block != null && !block.getType().isAir()) {
                return Collections.singletonList(block.getLocation().getBlockZ() + "");
              }
              else {
                return Collections.singletonList(player.getLocation().getBlockZ() + "");
              }
            }
            return Collections.singletonList("<Z>");
          }
          else {
            return onIntegerTabComplete("Z", 30000000, -30000000, args[args.length - 1]);
          }
        }

        if (args.length == 5) {
          List<String> list = new ArrayList<>();
          return TabCompleter.autoComplete(Tool.List.worldNames(), args[args.length - 1]);
        }

        int commandArgsLength = 5;
        if (args.length <= commandArgsLength + 1) {
          List<String> list = new ArrayList<>(Arrays.asList("-silent", "-s"));
          int n = 0;
          for (String arg : args) {
            if (n >= commandArgsLength) {
              if (arg.equals("-silent") || arg.equals("-s")) {
                list.remove("-silent");
                list.remove("-s");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }

        return Collections.emptyList();
      }

      if (args[0].equals("undo") || args[0].equals("redo")) {

        if ((!sender.hasPermission("cherry.wand.undo") && args[0].equals("undo")) || (!sender.hasPermission("cherry.wand.redo") && args[0].equals("redo"))) {
          return Collections.emptyList();
        }

        if (args.length == 2) {
          return onIntegerTabComplete("반복 횟수", 1000, 1, args[args.length - 1]);
        }

        if (args.length == 3) {
          List<String> list = new ArrayList<>();
          list.addAll(Tool.List.playerNames());
          list.addAll(Tool.List.playerUUIDStrings());
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }

        int commandArgsLength = 3;
        if (args.length <= commandArgsLength + 2) {
          List<String> list = new ArrayList<>(Arrays.asList("-applyPhysics", "-ap", "-silent", "-s"));
          int n = 0;
          for (String arg : args) {
            if (n >= commandArgsLength) {
              if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
                list.remove("-applyPhysics");
                list.remove("-ap");
              }
              if (arg.equals("-silent") || arg.equals("-s")) {
                list.remove("-silent");
                list.remove("-s");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }

        return Collections.emptyList();
      }

      if (args[0].equals("copy")) {
        if (!sender.hasPermission("cherry.wand.copy")) {
          return Collections.emptyList();
        }

        if (args.length == 2) {
          if (args[args.length - 1].isEmpty()) {
            if (player != null) {
              Block block = player.getTargetBlock(10);
              if (block != null && !block.getType().isAir()) {
                return Collections.singletonList(block.getLocation().getBlockX() + "");
              }
              else {
                return Collections.singletonList(player.getLocation().getBlockX() + "");
              }
            }
            return Collections.singletonList("<X>");
          }
          else {
            return onIntegerTabComplete("X", 30000000, -30000000, args[args.length - 1]);
          }
        }

        if (args.length == 3) {
          if (args[args.length - 1].isEmpty()) {
            if (player != null) {
              Block block = player.getTargetBlock(10);
              if (block != null && !block.getType().isAir()) {
                return Collections.singletonList(block.getLocation().getBlockY() + "");
              }
              else {
                return Collections.singletonList(player.getLocation().getBlockY() + "");
              }
            }
            return Collections.singletonList("<Y>");
          }
          else {
            return onIntegerTabComplete("X", 256, 0, args[args.length - 1]);
          }
        }

        if (args.length == 4) {
          if (args[args.length - 1].isEmpty()) {
            if (player != null) {
              Block block = player.getTargetBlock(10);
              if (block != null && !block.getType().isAir()) {
                return Collections.singletonList(block.getLocation().getBlockZ() + "");
              }
              else {
                return Collections.singletonList(player.getLocation().getBlockZ() + "");
              }
            }
            return Collections.singletonList("<Z>");
          }
          else {
            return onIntegerTabComplete("Z", 30000000, -30000000, args[args.length - 1]);
          }
        }

        if (args.length == 5) {
          List<String> list = new ArrayList<>();
          return TabCompleter.autoComplete(Tool.List.worldNames(), args[args.length - 1]);
        }

        int commandArgsLength = 5;
        if (args.length <= commandArgsLength + 1) {
          List<String> list = new ArrayList<>(Arrays.asList("-silent", "-s"));
          int n = 0;
          for (String arg : args) {
            if (n >= commandArgsLength) {
              if (arg.equals("-silent") || arg.equals("-s")) {
                list.remove("-silent");
                list.remove("-s");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args[0].equals("paste")) {
        if (!sender.hasPermission("cherry.wand.paste")) {
          return Collections.emptyList();
        }

        if (args.length == 2) {
          if (args[args.length - 1].isEmpty()) {
            if (player != null) {
              Block block = player.getTargetBlock(10);
              if (block != null && !block.getType().isAir()) {
                return Collections.singletonList(block.getLocation().getBlockX() + "");
              }
              else {
                return Collections.singletonList(player.getLocation().getBlockX() + "");
              }
            }
            return Collections.singletonList("<X>");
          }
          else {
            return onIntegerTabComplete("X", 30000000, -30000000, args[args.length - 1]);
          }
        }

        if (args.length == 3) {
          if (args[args.length - 1].isEmpty()) {
            if (player != null) {
              Block block = player.getTargetBlock(10);
              if (block != null && !block.getType().isAir()) {
                return Collections.singletonList(block.getLocation().getBlockY() + "");
              }
              else {
                return Collections.singletonList(player.getLocation().getBlockY() + "");
              }
            }
            return Collections.singletonList("<Y>");
          }
          else {
            return onIntegerTabComplete("X", 256, 0, args[args.length - 1]);
          }
        }

        if (args.length == 4) {
          if (args[args.length - 1].isEmpty()) {
            if (player != null) {
              Block block = player.getTargetBlock(10);
              if (block != null && !block.getType().isAir()) {
                return Collections.singletonList(block.getLocation().getBlockZ() + "");
              }
              else {
                return Collections.singletonList(player.getLocation().getBlockZ() + "");
              }
            }
            return Collections.singletonList("<Z>");
          }
          else {
            return onIntegerTabComplete("Z", 30000000, -30000000, args[args.length - 1]);
          }
        }

        if (args.length == 5) {
          List<String> list = new ArrayList<>();
          return TabCompleter.autoComplete(Tool.List.worldNames(), args[args.length - 1]);
        }

        int commandArgsLength = 5;
        if (args.length <= commandArgsLength + 4) {
          List<String> list = new ArrayList<>(Arrays.asList("-silent", "-s", "-remove-air", "-remove-water", "-remove-lava"));
          int n = 0;
          for (String arg : args) {
            if (n >= commandArgsLength) {
              if (arg.equals("-silent") || arg.equals("-s")) {
                list.remove("-silent");
                list.remove("-s");
              }
              if (arg.equals("-remove-air")) {
                list.remove("-remove-air");
              }
              if (arg.equals("-remove-water")) {
                list.remove("-remove-water");
              }
              if (arg.equals("-remove-lava")) {
                list.remove("-remove-lava");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args[0].equals("rotate")) {
        if (!sender.hasPermission("cherry.wand.rotate")) {
          return Collections.emptyList();
        }

        if (args.length == 2) {
          return TabCompleter.autoComplete(Arrays.asList("right", "left"), args[args.length - 1]);
        }

        int commandArgsLength = 2;
        if (args.length <= commandArgsLength + 1) {
          List<String> list = new ArrayList<>(Arrays.asList("-silent", "-s"));
          int n = 0;
          for (String arg : args) {
            if (n >= commandArgsLength) {
              if (arg.equals("-silent") || arg.equals("-s")) {
                list.remove("-silent");
                list.remove("-s");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args[0].equals("flip")) {
        if (!sender.hasPermission("cherry.wand.flip")) {
          return Collections.emptyList();
        }

        if (args.length == 2) {
          return TabCompleter.autoComplete(Arrays.asList("east", "west", "south", "north", "up", "down"), args[args.length - 1]);
        }

        int commandArgsLength = 2;
        if (args.length <= commandArgsLength + 1) {
          List<String> list = new ArrayList<>(Arrays.asList("-silent", "-s"));
          int n = 0;
          for (String arg : args) {
            if (n >= commandArgsLength) {
              if (arg.equals("-silent") || arg.equals("-s")) {
                list.remove("-silent");
                list.remove("-s");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args[0].equals("move")) {
        if (!sender.hasPermission("cherry.wand.move")) {
          return Collections.emptyList();
        }

        // length
        if (args.length == 2) {
          return onIntegerTabComplete("거리", 100000, 1, args[args.length - 1]);
        }

        if (args.length == 3) {
          return TabCompleter.autoComplete(Arrays.asList("east", "west", "south", "north", "up", "down"), args[args.length - 1]);
        }

        int commandArgsLength = 3;
        if (args.length <= commandArgsLength + 2) {
          List<String> list = new ArrayList<>(Arrays.asList("-applyPhysics", "-ap", "-silent", "-s"));
          int n = 0;
          for (String arg : args) {
            if (n >= commandArgsLength) {
              if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
                list.remove("-applyPhysics");
                list.remove("-ap");
              }
              if (arg.equals("-silent") || arg.equals("-s")) {
                list.remove("-silent");
                list.remove("-s");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args[0].equals("stack")) {
        if (!sender.hasPermission("cherry.wand.stack")) {
          return Collections.emptyList();
        }

        // repeat
        if (args.length == 2) {
          return onIntegerTabComplete("반복 횟수", 1000, 1, args[args.length - 1]);
        }

        if (args.length == 3) {
          return TabCompleter.autoComplete(Arrays.asList("east", "west", "south", "north", "up", "down"), args[args.length - 1]);
        }

        int commandArgsLength = 3;
        if (args.length <= commandArgsLength + 2) {
          List<String> list = new ArrayList<>(Arrays.asList("-applyPhysics", "-ap", "-silent", "-s"));
          int n = 0;
          for (String arg : args) {
            if (n >= commandArgsLength) {
              if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
                list.remove("-applyPhysics");
                list.remove("-ap");
              }
              if (arg.equals("-silent") || arg.equals("-s")) {
                list.remove("-silent");
                list.remove("-s");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args[0].equals("replace")) {
        if (!sender.hasPermission("cherry.wand.replace")) {
          return Collections.emptyList();
        }

        if (args.length == 2 || args.length == 3) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }

        int commandArgsLength = 3;
        if (args.length <= commandArgsLength + 2) {
          List<String> list = new ArrayList<>(Arrays.asList("-applyPhysics", "-ap", "-silent", "-s"));
          int n = 0;
          for (String arg : args) {
            if (n >= commandArgsLength) {
              if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
                list.remove("-applyPhysics");
                list.remove("-ap");
              }
              if (arg.equals("-silent") || arg.equals("-s")) {
                list.remove("-silent");
                list.remove("-s");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args[0].equals("replacenear")) {
        if (!sender.hasPermission("cherry.wand.replacenear")) {
          return Collections.emptyList();
        }

        if (args.length == 2 || args.length == 3) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }

        // radius
        if (args.length == 4) {
          return onIntegerTabComplete("반지름", 100000, 1, args[args.length - 1]);
        }

        int commandArgsLength = 4;
        if (args.length <= commandArgsLength + 2) {
          List<String> list = new ArrayList<>(Arrays.asList("-applyPhysics", "-ap", "-silent", "-s"));
          int n = 0;
          for (String arg : args) {
            if (n >= commandArgsLength) {
              if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
                list.remove("-applyPhysics");
                list.remove("-ap");
              }
              if (arg.equals("-silent") || arg.equals("-s")) {
                list.remove("-silent");
                list.remove("-s");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args[0].equals("cube") || args[0].equals("emptycube") || args[0].equals("walledcube")
        || args[0].equals("ecube") || args[0].equals("wcube")
      ) {
        if (!sender.hasPermission("cherry.wand.edit.cube")) {
          return Collections.emptyList();
        }

        if (args.length == 2) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }

        int commandArgsLength = 2;
        if (args.length <= commandArgsLength + 2) {
          List<String> list = new ArrayList<>(Arrays.asList("-applyPhysics", "-ap", "-silent", "-s"));
          int n = 0;
          for (String arg : args) {
            if (n >= commandArgsLength) {
              if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
                list.remove("-applyPhysics");
                list.remove("-ap");
              }
              if (arg.equals("-silent") || arg.equals("-s")) {
                list.remove("-silent");
                list.remove("-s");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args[0].equals("cyl") || args[0].equals("emptycyl") || args[0].equals("walledcyl")
        || args[0].equals("ecyl") || args[0].equals("wcyl")
        || args[0].equals("pointcyl") || args[0].equals("emptypointcyl") || args[0].equals("walledpointcyl")
        || args[0].equals("pcyl") || args[0].equals("epcyl") || args[0].equals("wpcyl")
      ) {
        if (!sender.hasPermission("cherry.wand.edit.cyl")) {
          return Collections.emptyList();
        }

        if (args.length == 2) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }

        // radius
        if (args.length == 3) {
          return onIntegerTabComplete("반지름", 100000, 1, args[args.length - 1]);
        }

        // height
        if (args.length == 4) {
          return onIntegerTabComplete("높이", 100000, 1, args[args.length - 1]);
        }

        // 옵션 입력
        int commandArgsLength = 4;
        if (args.length <= commandArgsLength + 2) {
          List<String> list = new ArrayList<>(Arrays.asList("-applyPhysics", "-ap", "-silent", "-s"));
          int n = 0;
          for (String arg : args) {
            if (n >= commandArgsLength) {
              if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
                list.remove("-applyPhysics");
                list.remove("-ap");
              }
              if (arg.equals("-silent") || arg.equals("-s")) {
                list.remove("-silent");
                list.remove("-s");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args[0].equals("sphere") || args[0].equals("emptysphere") || args[0].equals("esphere")
        || args[0].equals("pointsphere") || args[0].equals("emptypointsphere")
        || args[0].equals("psphere") || args[0].equals("epsphere")
      ) {
        if (sender.hasPermission("cherry.wand.edit.sphere")) {
          return Collections.emptyList();
        }

        if (args.length == 2) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }

        // radius
        if (args.length == 3) {
          return onIntegerTabComplete("반지름", 100000, 1, args[args.length - 1]);
        }

        // 옵션 입력
        int commandArgsLength = 3;
        if (args.length <= commandArgsLength + 2) {
          List<String> list = new ArrayList<>(Arrays.asList("-applyPhysics", "-ap", "-silent", "-s"));
          int n = 0;
          for (String arg : args) {
            if (n >= commandArgsLength) {
              if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
                list.remove("-applyPhysics");
                list.remove("-ap");
              }
              if (arg.equals("-silent") || arg.equals("-s")) {
                list.remove("-silent");
                list.remove("-s");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args[0].equals("wall")) {
        if (!sender.hasPermission("cherry.wand.edit.cube")) {
          return Collections.emptyList();
        }

        if (args.length == 2) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }

        int commandArgsLength = 2;
        if (args.length <= commandArgsLength + 2) {
          List<String> list = new ArrayList<>(Arrays.asList("-applyPhysics", "-ap", "-silent", "-s"));
          int n = 0;
          for (String arg : args) {
            if (n >= commandArgsLength) {
              if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
                list.remove("-applyPhysics");
                list.remove("-ap");
              }
              if (arg.equals("-silent") || arg.equals("-s")) {
                list.remove("-silent");
                list.remove("-s");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args[0].equals("cmdscan")) {
        if (!sender.hasPermission("cherry.wand.cmdscan")) {
          return Collections.emptyList();
        }

        if (args.length == 2) {
          return onIntegerTabComplete("반지름", 100000, 1, args[args.length - 1]);
        }
      }

    }

    return Collections.emptyList();
  }

  private List<String> onIntegerTabComplete(String name, int max, int min, String input) {
    if (input.isEmpty()) {
      return Collections.singletonList("<" + name.replaceAll(" ", "_") + ">");
    }
    else {
      try {
        int i = Integer.parseInt(input);
        if (i > max) {
          return Collections.singletonList("최대 " + name + " 값은 " + max + " 입니다.");
        }
        if (i < min) {
          return Collections.singletonList("최소 " + name + " 값은 " + min + " 입니다.");
        }
      } catch (Exception e) {
        if (Tool.Check.isInteger(input)) {
          return Collections.singletonList("프로그램 상 사용할 수 없는 범위의 수입니다.");
        }
        else {
          return Collections.singletonList(name + " 값은 정수만 입력할 수 있습니다.");
        }
      }
      return Collections.singletonList(input);
    }
  }
}
