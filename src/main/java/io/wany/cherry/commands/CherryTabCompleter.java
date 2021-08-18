package io.wany.cherry.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    String name = command.getName().toLowerCase();

    if (name.equals("cherry")) {

      if (args.length == 1) {
        List<String> list = new ArrayList<>();
        if (sender.hasPermission("cherry.command.reload")) {
          list.add("reload");
        }
        if (sender.hasPermission("cherry.command.update")) {
          list.add("update");
        }
        if (sender.hasPermission("cherry.command.version")) {
          list.add("version");
        }
        if (sender.hasPermission("cherry.command.explosion")) {
          list.add("explosion");
        }

        return autoComplete(list, args[0]);
      }

      args[0] = args[0].toLowerCase();

      if (args[0].equals("explosion")) {
        if (args.length == 3) {
          List<String> list = Arrays.asList("normal", "fire", "unbreak");
          return autoComplete(list, args[args.length - 1]);
        }
      }

    }

    return Collections.emptyList();
  }

}

