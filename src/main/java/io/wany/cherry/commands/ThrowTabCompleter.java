package io.wany.cherry.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ThrowTabCompleter implements TabCompleter {

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

    if (name.equals("throw")) {

      if (args.length == 1) {
        List<String> list = Collections.singletonList("new");
        return autoComplete(list, args[args.length - 1]);
      }

      else if (args.length >= 1 && args[0].equals("new")) {

        if (args.length == 2) {
          if (sender.hasPermission("cherry.command.reload")) {
            List<String> list = Arrays.asList("UnhandledException", "NullPointerException", "StackOverflowError", "ArrayIndexOutOfBoundsException", "ClassCastException", "IllegalArgumentException", "ArithmeticException", "UnsupportedOperationException");
            return autoComplete(list, args[args.length - 1]);
          }
        }

      }

    }

    return Collections.emptyList();
  }

}
