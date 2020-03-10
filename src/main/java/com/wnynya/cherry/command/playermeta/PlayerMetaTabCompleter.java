package com.wnynya.cherry.command.playermeta;

import com.wnynya.cherry.Tool;
import com.wnynya.cherry.command.TabCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerMetaTabCompleter implements org.bukkit.command.TabCompleter {

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

    if (command.getName().equalsIgnoreCase("playermeta")) {

      if (args.length == 1) {
        List<String> list = Tool.List.playerNames();
        return TabCompleter.autoComplete(list, args[args.length - 1]);
      }

      if (args.length == 2) {
        List<String> list = new ArrayList<>();
        list.add("wand");
        list.add("portal");
        list.add("nickname");
        list.add("notetool");
        return TabCompleter.autoComplete(list, args[args.length - 1]);
      }

      if (args[1].equalsIgnoreCase("wand")) {
        if (args.length == 3) {
          List<String> list = Arrays.asList("enable", "disable", "set");
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        else if (args.length == 4 && args[2].equalsIgnoreCase("set")) {
          List<String> list = Arrays.asList("posMsg");
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        else if (args.length == 5) {
          if (args[3].equalsIgnoreCase("posMsg")) {
            List<String> list = Arrays.asList("actionbar", "normal");
            return TabCompleter.autoComplete(list, args[args.length - 1]);
          }
        }
      }

      if (args[1].equalsIgnoreCase("portal")) {
        if (args.length == 3) {
          List<String> list = Arrays.asList("enable", "disable");
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
      }

      if (args[1].equalsIgnoreCase("nickname")) {
        if (args.length == 3) {
          List<String> list = Arrays.asList("enable", "disable", "set");
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        else if (args.length == 4 && args[2].equalsIgnoreCase("set")) {
          List<String> list = Arrays.asList("nickname");
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        else if (args.length == 5) {
        }
      }

      if (args[1].equalsIgnoreCase("notetool")) {
        if (args.length == 3) {
          List<String> list = Arrays.asList("enable", "disable", "set");
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        else if (args.length == 4 && args[2].equalsIgnoreCase("set")) {
          List<String> list = Arrays.asList("blockOnHand");
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        else if (args.length == 5) {
          if (args[3].equalsIgnoreCase("blockOnHand")) {
            List<String> list = Arrays.asList("use", "click");
            return TabCompleter.autoComplete(list, args[args.length - 1]);
          }
        }
      }


    }

    return Collections.emptyList();
  }
}
