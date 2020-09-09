package com.wnynya.cherry.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    String name = command.getName();

    // 체리
    if (name.equalsIgnoreCase("cherry")) {

      if (args.length == 1) {
        List<String> list = Arrays.asList("reload", "update", "version", "info");
        return autoComplete(list, args[0]);
      }

    }

    return Collections.emptyList();
  }

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

}
