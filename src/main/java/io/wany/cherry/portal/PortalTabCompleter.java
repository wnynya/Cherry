package io.wany.cherry.portal;

import io.wany.cherry.amethyst.Tool;
import io.wany.cherry.commands.CherryTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
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

    if (command.getName().equalsIgnoreCase("portal")) {

      int a = args.length - 1;

      if (a == 0) {
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
          list.add("info");
          list.add("reload");
        }
        return CherryTabCompleter.autoComplete(list, args[a]);
      }


      if (a > 0 && args[0].equalsIgnoreCase("remove")) {
        if (a == 1) {
          List<String> list = Portal.getPortalNames();
          return CherryTabCompleter.autoComplete(list, args[a]);
        }
      }

      if (a > 0 && args[0].equalsIgnoreCase("enable")) {
        if (a == 1) {
          List<String> list = Portal.getPortalNames();
          return CherryTabCompleter.autoComplete(list, args[a]);
        }
      }

      if (a > 0 && args[0].equalsIgnoreCase("disable")) {
        if (a == 1) {
          List<String> list = Portal.getPortalNames();
          return CherryTabCompleter.autoComplete(list, args[a]);
        }
      }

      if (a > 0 && args[0].equalsIgnoreCase("use")) {
        if (a == 1) {
          List<String> list = Portal.getPortalNames();
          return CherryTabCompleter.autoComplete(list, args[a]);
        }
        if (a == 2) {
          List<String> list = Tool.Lista.playerNames();
          return CherryTabCompleter.autoComplete(list, args[a]);
        }
      }

      /*
      cmd    0   1        2    3
      portal set <portal> goto location

      cmd    0   1        2    3        4
      portal set <portal> goto location here

      cmd    0   1        2    3        4
      portal set <portal> goto location wandpos

      cmd    0   1        2    3        4 5 6
      portal set <portal> goto location x y z

      cmd    0   1        2    3        4 5 6 7
      portal set <portal> goto location x y z <world>

      cmd    0   1        2    3      4
      portal set <portal> goto server <server>

      cmd    0   1        2           3...
      portal set <portal> displayname <name...>

      cmd    0   1        2        3
      portal set <portal> protocol [protocol]
       */
      if (a > 0 && args[0].equalsIgnoreCase("set")) {

        if (a == 1) {
          List<String> list = Portal.getPortalNames();
          return CherryTabCompleter.autoComplete(list, args[a]);
        }

        Portal portal = Portal.getPortal(args[1]);
        if (portal == null) {
          return Collections.emptyList();
        }


        if (args[2].equalsIgnoreCase("goto")) {

          if (a == 4 && args[3].equalsIgnoreCase("location")) {

            List<String> list = Arrays.asList("here", "wandpos", "<Location>");
            return CherryTabCompleter.autoComplete(list, args[a]);

          }

          if (a == 5 && args[3].equalsIgnoreCase("server")) {

            List<String> list = Arrays.asList("", "");
            return CherryTabCompleter.autoComplete(list, args[a]);

          }

        }

        else if (args[2].equalsIgnoreCase("cmdexecutor")) {

          List<String> list = new ArrayList<>();
          list.add("console");
          list.add("player");
          return CherryTabCompleter.autoComplete(list, args[a]);

        }

        else if (args[2].equalsIgnoreCase("protocol")) {

          List<String> list = Arrays.asList("server", "bungeecord", "command");
          return CherryTabCompleter.autoComplete(list, args[a]);

        }

      }

      if (a > 0 && args[0].equalsIgnoreCase("area")) {

        if (a == 1) {
          List<String> list = Portal.getPortalNames();
          return CherryTabCompleter.autoComplete(list, args[a]);
        }

        Portal portal = Portal.getPortal(args[1]);
        if (portal == null) {
          return Collections.emptyList();
        }

        if (a == 2) {
          List<String> list = Arrays.asList("add", "list", "remove");
          return CherryTabCompleter.autoComplete(list, args[a]);
        }

        if (a > 2 && args[2].equalsIgnoreCase("add")) {

          if (a == 4) {
            List<String> list = Arrays.asList("gate", "sign");
            return CherryTabCompleter.autoComplete(list, args[a]);
          }

          /*
          cmd    0    1        2   3      4    5
          portal area <portal> add <name> gate [type]

          cmd    0    1        2   3      4    5      6   7  8  9  10 11
          portal area <portal> add <name> gate [type] x1 y1 z1 x2 y2 z2

          cmd    0    1        2   3      4    5      6  7  8  9  10 11 12
          portal area <portal> add <name> gate [type] x1 y1 z1 x2 y2 z2 <world>
           */
          if (a > 4 && args[4].equalsIgnoreCase("gate")) {

            if (a == 5) {
              List<String> list = Arrays.asList("NETHER_PORTAL", "END_GATEWAY", "END_PORTAL", "WATER");
              return CherryTabCompleter.autoComplete(list, args[a]);
            }

            if (a == 6 || a == 9) {
              List<String> list;
              if (sender instanceof Player) {
                Block b = ((Player) sender).getTargetBlockExact(10);
                if (b == null) {
                  list = Collections.emptyList();
                }
                else {
                  list = Collections.singletonList(b.getLocation().getBlockX() + "");
                }
              }
              else {
                list = Collections.emptyList();
              }
              return CherryTabCompleter.autoComplete(list, args[a]);
            }

            if (a == 7 || a == 10) {
              List<String> list;
              if (sender instanceof Player) {
                Block b = ((Player) sender).getTargetBlockExact(10);
                if (b == null) {
                  list = Collections.emptyList();
                }
                else {
                  list = Collections.singletonList(b.getLocation().getBlockY() + "");
                }
              }
              else {
                list = Collections.emptyList();
              }
              return CherryTabCompleter.autoComplete(list, args[a]);
            }

            if (a == 8 || a == 11) {
              List<String> list;
              if (sender instanceof Player) {
                Block b = ((Player) sender).getTargetBlockExact(10);
                if (b == null) {
                  list = Collections.emptyList();
                }
                else {
                  list = Collections.singletonList(b.getLocation().getBlockZ() + "");
                }
              }
              else {
                list = Collections.emptyList();
              }
              return CherryTabCompleter.autoComplete(list, args[a]);
            }

            if (a == 12) {
              List<String> list = new ArrayList<>();
              for (World world : Bukkit.getWorlds()) {
                list.add(world.getName());
              }
              return CherryTabCompleter.autoComplete(list, args[a]);
            }

          }

          /*
          cmd    0    1        2   3      4
          portal area <portal> add <name> sign

          cmd    0    1        2   3      4    5
          portal area <portal> add <name> sign wandpos

          cmd    0    1        2   3      4    5 6 7
          portal area <portal> add <name> sign x y z

          cmd    0    1        2   3      4    5 6 7 8
          portal area <portal> add <name> sign x y z <world>
           */
          if (a > 4 && args[4].equalsIgnoreCase("sign")) {

            if (a == 5) {
              List<String> list = new ArrayList<>();
              if (sender instanceof Player) {
                Block b = ((Player) sender).getTargetBlockExact(10);
                if (b != null) {
                  list.add(b.getLocation().getBlockX() + "");
                }
              }
              list.add("wandpos");
              return CherryTabCompleter.autoComplete(list, args[a]);
            }

            if (a == 6) {
              List<String> list = new ArrayList<>();
              if (sender instanceof Player) {
                Block b = ((Player) sender).getTargetBlockExact(10);
                if (b != null) {
                  list.add(b.getLocation().getBlockY() + "");
                }
              }
              return CherryTabCompleter.autoComplete(list, args[a]);
            }

            if (a == 7) {
              List<String> list = new ArrayList<>();
              if (sender instanceof Player) {
                Block b = ((Player) sender).getTargetBlockExact(10);
                if (b != null) {
                  list.add(b.getLocation().getBlockZ() + "");
                }
              }
              return CherryTabCompleter.autoComplete(list, args[a]);
            }

            if (a == 8) {
              List<String> list = new ArrayList<>();
              for (World world : Bukkit.getWorlds()) {
                list.add(world.getName());
              }
              return CherryTabCompleter.autoComplete(list, args[a]);
            }

          }

        }

      }

      if (a > 0 && args[0].equalsIgnoreCase("renew")) {
        if (a == 1) {
          List<String> list = Portal.getPortalNames();
          return CherryTabCompleter.autoComplete(list, args[a]);
        }
      }

    }

    return Collections.emptyList();
  }
}
