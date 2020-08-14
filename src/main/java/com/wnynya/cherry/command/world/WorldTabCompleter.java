package com.wnynya.cherry.command.world;

import com.wnynya.cherry.Tool;
import com.wnynya.cherry.world.World;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WorldTabCompleter implements org.bukkit.command.TabCompleter {

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    String name = command.getName();

    // 체리
    if (name.equalsIgnoreCase("world")) {

      if (args.length == 1) {
        List<String> list = Arrays.asList("load", "unload", "send", "list");
        return autoComplete(list, args[args.length - 1]);
      }

      if (args.length == 2 && args[0].equals("load")) {
        List<String> worlds = (List<String>) World.getWorldConfig().getConfig().getList("worlds");
        List<String> worldsClone = new ArrayList<>();
        worldsClone.addAll(worlds);
        for (String worldName : worldsClone) {
          if (Bukkit.getWorld(worldName) != null) {
            worlds.remove(worldName);
          }
        }
        List<String> list = worlds;
        return autoComplete(list, args[args.length - 1]);
      }

      if (args.length == 2 && args[0].equals("unload")) {
        List<String> worlds = (List<String>) World.getWorldConfig().getConfig().getList("worlds");
        List<String> worldsClone = new ArrayList<>();
        worldsClone.addAll(worlds);
        for (String worldName : worldsClone) {
          if (Bukkit.getWorld(worldName) == null) {
            worlds.remove(worldName);
          }
        }
        List<String> list = worlds;
        return autoComplete(list, args[args.length - 1]);
      }

      if (args.length == 2 && args[0].equals("send")) {
        List<String> list = Tool.List.playerNames();
        return autoComplete(list, args[args.length - 1]);
      }

      if (args.length == 3 && args[0].equals("send")) {
        List<String> worlds = (List<String>) World.getWorldConfig().getConfig().getList("worlds");
        List<String> worldsClone = new ArrayList<>();
        worldsClone.addAll(worlds);
        for (String worldName : worldsClone) {
          if (Bukkit.getWorld(worldName) == null) {
            worlds.remove(worldName);
          }
        }
        List<String> list = worlds;
        return autoComplete(list, args[args.length - 1]);
      }

    }

    return Collections.emptyList();
  }

  public static List<String> autoComplete(List<String> list, String arg) {
    if (!arg.equalsIgnoreCase("")) {
      List<String> listA = new ArrayList<>();
      for (String value : list) {
        if (value.toLowerCase().startsWith(arg.toLowerCase())) {
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

}