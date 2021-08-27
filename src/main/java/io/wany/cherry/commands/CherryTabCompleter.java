package io.wany.cherry.commands;

import io.wany.cherry.amethyst.troll.Troll;
import io.wany.cherry.amethyst.troll.Trolling;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CherryTabCompleter implements org.bukkit.command.TabCompleter {

  public static List<String> autoComplete(List<String> list, String arg) {
    if (!arg.equalsIgnoreCase("")) {
      List<String> listA = new ArrayList<>();
      for (String value : list) {
        if (value.toLowerCase().contains(arg.toLowerCase())) {
          listA.add(value);
        }
      }
      return sort(listA);
    }
    return sort(list);
  }

  private static List<String> sort(List<String> list) {
    Collections.sort(list);
    return list;
  }

  public static void usedFlags(String[] args, int commandArgsLength, List<String> list) {
    int n = 0;
    for (String arg : args) {
      if (n >= commandArgsLength) {
        if (arg.equals("-silent") || arg.equals("-s")) {
          list.remove("-silent");
          list.remove("-s");
        }
        if (arg.equals("-force") || arg.equals("-f")) {
          list.remove("-force");
          list.remove("-f");
        }
        if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
          list.remove("-applyPhysics");
          list.remove("-ap");
        }
      }
      n++;
    }
  }

  @Override
  public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {
    String name = command.getName().toLowerCase();

    switch (name) {

      case "cherry" -> {

        if (args.length == 1) {
          List<String> list = new ArrayList<>();
          if (sender.hasPermission("cherry.version")) {
            list.add("version");
            list.add("v");
          }
          if (sender.hasPermission("cherry.reload")) {
            list.add("reload");
            list.add("r");
          }
          if (sender.hasPermission("cherry.update")) {
            list.add("update");
            list.add("u");
          }
          if (sender.hasPermission("cherry.menu")) {
            list.add("menu");
            list.add("m");
          }
          if (sender.hasPermission("cherry.system")) {
            list.add("system");
            list.add("s");
          }
          if (sender.hasPermission("cherry.explosion")) {
            list.add("explosion");
          }

          return autoComplete(list, args[args.length - 1]);
        }

        args[0] = args[0].toLowerCase();

        switch (args[0].toLowerCase()) {

          case "update", "u" -> {
            if (!sender.hasPermission("cherry.update")) {
              return Collections.emptyList();
            }
            int commandArgsLength = 1;
            // flags
            List <String> flags = List.of("-silent", "-force");
            if (args.length <= commandArgsLength + flags.size()) {
              List<String> list = new ArrayList<>(flags);
              usedFlags(args, commandArgsLength, list);
              return autoComplete(list, args[args.length - 1]);
            }
          }

          case "menu", "m" -> {
            if (!sender.hasPermission("cherry.menu")) {
              return Collections.emptyList();
            }
          }

          case "system", "s" -> {
            if (!sender.hasPermission("cherry.system")) {
              return Collections.emptyList();
            }
            if (args.length == 2) {
              List<String> list = new ArrayList<>();
              if (sender.hasPermission("cherry.system.info")) {
                list.add("info");
                list.add("i");
              }
              if (sender.hasPermission("cherry.system.java")) {
                list.add("java");
                list.add("j");
              }
              if (sender.hasPermission("cherry.system.gc")) {
                list.add("gc");
              }

              return autoComplete(list, args[args.length - 1]);
            }
            switch (args[1].toLowerCase()) {

              case "gc" -> {
                int commandArgsLength = 2;
                // flags
                List <String> flags = List.of("-silent");
                if (args.length <= commandArgsLength + flags.size()) {
                  List<String> list = new ArrayList<>(flags);
                  usedFlags(args, commandArgsLength, list);
                  return autoComplete(list, args[args.length - 1]);
                }
              }

            }
          }

          case "explosion" -> {
            if (!sender.hasPermission("cherry.explosion")) {
              return Collections.emptyList();
            }
            if (args.length == 2) {
              List<String> list = new ArrayList<>(List.of(args[1]));
              return autoComplete(list, args[args.length - 1]);
            }
            if (args.length == 3) {
              List<String> list = Arrays.asList("normal", "fire", "unbreak");
              return autoComplete(list, args[args.length - 1]);
            }
          }

        }

      }

      case "troll" -> {

        if (args.length == 1) {
          List<String> list = new ArrayList<>();
          for (Player player : Bukkit.getOnlinePlayers()) {
            list.add(player.getName());
          }
          return autoComplete(list, args[args.length - 1]);
        }

        if (args.length == 2) {
          List<String> list = new ArrayList<>();
          for (Troll troll : Troll.values()) {
            list.add(troll.name().toLowerCase());
          }
          return autoComplete(list, args[args.length - 1]);
        }

      }

      case "drop" -> {

        if (args.length == 1) {
          List<String> list = new ArrayList<>(List.of("true", "false"));
          return autoComplete(list, args[args.length - 1]);
        }

      }

      case "lid" -> {

        if (args.length == 1) {
          List<String> list = new ArrayList<>(List.of("toggle", "open", "close"));
          return autoComplete(list, args[args.length - 1]);
        }

        // location x y z world
        if (args.length == 2) {
          List<String> list = new ArrayList<>(List.of("x"));
          if (sender instanceof Player player) {
            Block block = player.getTargetBlock(10);
            if (block != null) {
              if (!args[args.length - 1].equals("")) {
                list = new ArrayList<>(List.of(
                  args[args.length - 1] + "",
                  args[args.length - 1] + " " + block.getY(),
                  args[args.length - 1] + " " + block.getY() + " " + block.getZ(),
                  args[args.length - 1] + " " + block.getY() + " " + block.getZ() + " " + block.getWorld().getName()
                ));
              }
              else {
                list = new ArrayList<>(List.of(
                  block.getX() + "",
                  block.getX() + " " + block.getY(),
                  block.getX() + " " + block.getY() + " " + block.getZ(),
                  block.getX() + " " + block.getY() + " " + block.getZ() + " " + block.getWorld().getName()
                ));
              }
            }
          }
          return autoComplete(list, args[args.length - 1]);
        }
        if (args.length == 3) {
          List<String> list = new ArrayList<>(List.of("y"));
          if (sender instanceof Player player) {
            Block block = player.getTargetBlock(10);
            if (block != null) {
              if (!args[args.length - 1].equals("")) {
                list = new ArrayList<>(List.of(
                  args[args.length - 1] + "",
                  args[args.length - 1] + " " + block.getZ(),
                  args[args.length - 1] + " " + block.getZ() + " " + block.getWorld().getName()
                ));
              }
              else {
                list = new ArrayList<>(List.of(
                  block.getY() + "",
                  block.getY() + " " + block.getZ(),
                  block.getY() + " " + block.getZ() + " " + block.getWorld().getName()
                ));
              }
            }
          }
          return autoComplete(list, args[args.length - 1]);
        }
        if (args.length == 4) {
          List<String> list = new ArrayList<>(List.of("z"));
          if (sender instanceof Player player) {
            Block block = player.getTargetBlock(10);
            if (block != null) {
              if (!args[args.length - 1].equals("")) {
                list = new ArrayList<>(List.of(
                  args[args.length - 1] + "",
                  args[args.length - 1] + " " + block.getWorld().getName()
                ));
              }
              else {
                list = new ArrayList<>(List.of(
                  block.getZ() + "",
                  block.getZ() + " " + block.getWorld().getName()
                ));
              }
            }
          }
          return autoComplete(list, args[args.length - 1]);
        }
        if (args.length == 5) {
          List<String> list = new ArrayList<>();
          for (World world : Bukkit.getWorlds()) {
            list.add(world.getName());
          }
          return autoComplete(list, args[args.length - 1]);
        }

      }

    }

    return Collections.emptyList();
  }

}

