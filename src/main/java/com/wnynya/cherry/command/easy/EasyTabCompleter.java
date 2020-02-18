package com.wnynya.cherry.command.easy;

import com.wnynya.cherry.Tool;
import com.wnynya.cherry.command.TabCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EasyTabCompleter implements org.bukkit.command.TabCompleter {

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

    if (command.getName().equalsIgnoreCase("gm")) {

      if (args.length == 1) {
        List<String> list = new ArrayList<>();
        list.add("survival");
        list.add("creative");
        list.add("adventure");
        list.add("spectator");
        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("s");
        list.add("sv");
        list.add("c");
        list.add("a");
        list.add("p");
        list.add("sp");
        return TabCompleter.autoComplete(list, args[args.length - 1]);
      }

      if (args.length == 2) {
        List<String> list = Tool.List.playerNames();
        list.addAll(Arrays.asList("@p", "@a", "@r", "@e", "@s"));
        return TabCompleter.autoComplete(list, args[args.length - 1]);
      }

    }

    return Collections.emptyList();
  }
}
