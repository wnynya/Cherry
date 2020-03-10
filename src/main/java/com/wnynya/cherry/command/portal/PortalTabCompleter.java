package com.wnynya.cherry.command.portal;

import com.wnynya.cherry.Tool;
import com.wnynya.cherry.command.TabCompleter;
import com.wnynya.cherry.portal.Portal;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PortalTabCompleter implements org.bukkit.command.TabCompleter {
  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }

    if (command.getName().equalsIgnoreCase("portal")) {

      if (args.length == 1) {
        List<String> list = new ArrayList<>();
        list.add("add");
        if (Portal.getPortalNames().size() > 0) {
          list.add("remove");
          list.add("list");
          list.add("enable");
          list.add("disable");
          list.add("set");
          list.add("use");
          list.add("area");
          list.add("renew");
        }
        return TabCompleter.autoComplete(list, args[args.length - 1]);
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("create")) {

      }

      if (args.length > 1 && args[0].equalsIgnoreCase("remove")) {
        if (args.length == 2) {
          List<String> list = Portal.getPortalNames();
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("enable")) {
        if (args.length == 2) {
          List<String> list = Portal.getPortalNames();
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("disable")) {
        if (args.length == 2) {
          List<String> list = Portal.getPortalNames();
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("use")) {
        if (args.length == 2) {
          List<String> list = Portal.getPortalNames();
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        if (args.length == 3) {
          List<String> list = Tool.List.playerNames();
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("set")) {
        if (args.length == 2) {
          List<String> list = Portal.getPortalNames();
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        if (args.length == 3) {
          List<String> list = new ArrayList<>();
          list.add("displayname");
          list.add("goto");
          list.add("protocol");
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        if (args.length > 3 && args[2].equalsIgnoreCase("goto")) {
          if (args.length == 4) {
            List<String> list = Arrays.asList("location", "server");
            return TabCompleter.autoComplete(list, args[args.length - 1]);
          }
          if (args.length == 5 && args[3].equalsIgnoreCase("location")) {
            List<String> list = Arrays.asList("here", "<Location>");
            return TabCompleter.autoComplete(list, args[args.length - 1]);
          }
          if (args.length == 5 && args[3].equalsIgnoreCase("server")) {
            List<String> list = Arrays.asList("", "");
            return TabCompleter.autoComplete(list, args[args.length - 1]);
          }
        }
        if (args.length > 3 && args[2].equalsIgnoreCase("protocol")) {
          List<String> list = new ArrayList<>();
          list.add("server");
          list.add("bungeecord");
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("area")) {
        if (args.length == 2) {
          List<String> list = Portal.getPortalNames();
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        if (args.length == 3) {
          List<String> list = new ArrayList<>();
          list.add("list");
          list.add("add");
          list.add("remove");
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        if (args[2].equalsIgnoreCase("add")) {
          if (args.length == 5) {
            List<String> list = Arrays.asList("gate", "sign");
            return TabCompleter.autoComplete(list, args[args.length - 1]);
          }
          if (args.length >= 6 && args[4].equalsIgnoreCase("gate")) {
            if (args.length == 6) {
              List<String> list = Collections.singletonList("wandpos");
              return TabCompleter.autoComplete(list, args[args.length - 1]);
            }
            if (args.length == 7) {
              List<String> list = Arrays.asList("GATE_AIR", "GATE_NETHER", "GATE_ENDER", "GATE_WATER", "GATE_ENDER_LEGACY");
              return TabCompleter.autoComplete(list, args[args.length - 1]);
            }
          }
          if (args.length >= 6 && args[4].equalsIgnoreCase("sign")) {
            if (args.length == 6) {
              List<String> list = Arrays.asList("wandpos", "here");
              return TabCompleter.autoComplete(list, args[args.length - 1]);
            }
          }
        }
        if (args[2].equalsIgnoreCase("remove")) {
          Portal portal = Portal.getPortal(args[1]);
          if (portal == null) {
            return Collections.emptyList();
          }
          List<String> list = portal.getPortalAreaNames();
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("renew")) {
        if (args.length == 2) {
          List<String> list = Portal.getPortalNames();
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
      }

    }

    return Collections.emptyList();
  }
}
