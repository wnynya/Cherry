package com.wnynya.cherry.command.wand;

import com.wnynya.cherry.Msg;
import com.wnynya.cherry.Tool;
import com.wnynya.cherry.command.TabCompleter;
import com.wnynya.cherry.player.PlayerState;
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
          list.add("pos1");
        }
        if (sender.hasPermission("cherry.wand.pos")) {
          list.add("pos2");
        }
        if (sender.hasPermission("cherry.wand.copy")) {
          list.add("copy");
        }
        if (sender.hasPermission("cherry.wand.paste")) {
          list.add("paste");
        }
        if (sender.hasPermission("cherry.wand.cut")) {
          list.add("cut");
        }
        if (sender.hasPermission("cherry.wand.replace")) {
          list.add("replace");
        }
        if (sender.hasPermission("cherry.wand.cube")) {
          list.add("cube");
        }
        if (sender.hasPermission("cherry.wand.cyl")) {
          list.add("cyl");
        }
        if (sender.hasPermission("cherry.wand.sphere")) {
          list.add("sphere");
        }
        if (sender.hasPermission("cherry.wand.wall")) {
          list.add("wall");
        }
        if (sender.hasPermission("cherry.wand.cmdscan")) {
          list.add("cmdscan");
        }
        return TabCompleter.autoComplete(list, args[args.length - 1]);
      }

      // pos1, pos2
      if (args.length > 1 && args[0].equalsIgnoreCase("pos1") || args[0].equalsIgnoreCase("pos2")) {

        if ((!sender.hasPermission("cherry.wand.pos") && args[0].equalsIgnoreCase("pos1")) || (!sender.hasPermission("cherry.wand.pos") && args[0].equalsIgnoreCase("pos2"))) {
          return Collections.emptyList();
        }

        if (args.length == 2) {
          if (args[args.length - 1].isEmpty()) {
            return Collections.singletonList("<Integer X>");
          }
          else if (Tool.Check.isInteger(args[args.length - 1])) {
            int i = 0;
            try {
              i = Integer.parseInt(args[args.length - 1]);
            }
            catch (Exception e) {
              return Collections.emptyList();
            }
            if (i > 30000000) {
              return Collections.singletonList("너무 큰 수입니다 (n <= 30000000)");
            }
            if (i < -30000000) {
              return Collections.singletonList("너무 작은 수입니다 (n >= -30000000)");
            }
            return Collections.singletonList(args[args.length - 1] + " ");
          }
          return Collections.singletonList(Msg.n2s("정수만 입력할 수 있습니다"));
        }
        if (args.length == 3) {
          if (args[args.length - 1].isEmpty()) {
            return Collections.singletonList("<Integer Y>");
          }
          else if (Tool.Check.isInteger(args[args.length - 1])) {
            int i = 0;
            try {
              i = Integer.parseInt(args[args.length - 1]);
            }
            catch (Exception e) {
              return Collections.emptyList();
            }
            if (i > 256) {
              return Collections.singletonList("너무 큰 수입니다 (n <= 256)");
            }
            if (i < 0) {
              return Collections.singletonList("너무 작은 수입니다 (n >= 0)");
            }
            return Collections.singletonList(args[args.length - 1] + " ");
          }
          return Collections.singletonList(Msg.n2s("정수만 입력할 수 있습니다"));
        }
        if (args.length == 4) {
          if (args[args.length - 1].isEmpty()) {
            return Collections.singletonList("<Integer Z>");
          }
          else if (Tool.Check.isInteger(args[args.length - 1])) {
            int i = 0;
            try {
              i = Integer.parseInt(args[args.length - 1]);
            }
            catch (Exception e) {
              return Collections.emptyList();
            }
            if (i > 30000000) {
              return Collections.singletonList("너무 큰 수입니다 (n <= 30000000)");
            }
            if (i < -30000000) {
              return Collections.singletonList("너무 작은 수입니다 (n >= -30000000)");
            }
            return Collections.singletonList(args[args.length - 1] + " ");
          }
          return Collections.singletonList(Msg.n2s("정수만 입력할 수 있습니다"));
        }
        if (args.length == 5) {
          if (args[args.length - 1].isEmpty()) {
            return Collections.singletonList("<String worldName>");
          }
          else {
            return Collections.singletonList(args[args.length - 1] + " ");
          }
        }
        if (args.length == 6) {
          return Collections.singletonList("-silent");
        }

        return Collections.emptyList();
      }

      // undo, redo [options...]
      if (args.length > 1 && args[0].equalsIgnoreCase("undo") || args[0].equalsIgnoreCase("redo")) {

        if ((!sender.hasPermission("cherry.wand.undo") && args[0].equalsIgnoreCase("undo")) || (!sender.hasPermission("cherry.wand.redo") && args[0].equalsIgnoreCase("redo"))) {
          return Collections.emptyList();
        }

        if (2 <= args.length && args.length <= 4) {
          List<String> list = new ArrayList<>();
          list.addAll(Arrays.asList("-player:", "-uuid:", "-n:", "-p:", "-u:", "-silent"));
          if (!sender.hasPermission("cherry.wand.undo.another")) {
            list.remove("-player:");
            list.remove("-uuid:");
            list.remove("-p:");
            list.remove("-u:");
          }
          if (!sender.hasPermission("cherry.wand.undo.multiple")) {
            list.remove("-n:");
          }
          int n = 0;
          for (String arg : args) {
            if (n >= 1) {

              if (Pattern.compile("-player:([a-zA-Z0-9_:]{3,20})?", Pattern.CASE_INSENSITIVE).matcher(arg).matches() || Pattern.compile("-p:([a-zA-Z0-9_:]{3,20})?", Pattern.CASE_INSENSITIVE).matcher(arg).matches()) {

                if (!sender.hasPermission("cherry.wand.undo.another")) {
                  return Collections.emptyList();
                }

                Msg.info("캐치");

                if (args.length - 1 == n) {
                  Msg.info("캐치2");
                  List<String> lista = new ArrayList<>();
                  if (Pattern.compile("-player:([a-zA-Z0-9_:]{3,20})?", Pattern.CASE_INSENSITIVE).matcher(arg).matches()) {
                    lista = Tool.List.playerNames("-player:");
                  }
                  else if (Pattern.compile("-p:([a-zA-Z0-9_:]{3,20})?", Pattern.CASE_INSENSITIVE).matcher(arg).matches()) {
                    lista = Tool.List.playerNames("-p:");
                  }
                  lista.add("::CONSOLE");
                  return TabCompleter.autoComplete(lista, args[args.length - 1]);
                }

                list.remove("-player:");
                list.remove("-uuid:");
                list.remove("-p:");
                list.remove("-u:");
              }
              if (Pattern.compile("-uuid:([0-9]{8}-[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{12})?", Pattern.CASE_INSENSITIVE).matcher(arg).matches() || Pattern.compile("-u:([0-9]{8}-[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{12})?", Pattern.CASE_INSENSITIVE).matcher(arg).matches()) {

                if (!sender.hasPermission("cherry.wand.undo.another")) {
                  return Collections.emptyList();
                }

                if (args.length - 1 == n) {
                  List<String> lista = new ArrayList<>();
                  if (Pattern.compile("-uuid:([0-9]{8}-[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{12})?", Pattern.CASE_INSENSITIVE).matcher(arg).matches()) {
                    lista = Tool.List.playerNames("-uuid:");
                  }
                  else if (Pattern.compile("-u:([0-9]{8}-[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{12})?", Pattern.CASE_INSENSITIVE).matcher(arg).matches()) {
                    lista = Tool.List.playerNames("-u:");
                  }
                  return TabCompleter.autoComplete(lista, args[args.length - 1]);
                }

                list.remove("-player:");
                list.remove("-uuid:");
                list.remove("-p:");
                list.remove("-u:");
              }
              if (arg.equals("-n:")) {
                list.remove("-n:");
              }
              if (arg.equals("-silent")) {
                list.remove("-silent");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("stack")) {
        if (player == null || !player.hasPermission("cherry.wand.stack")) {
          return Collections.emptyList();
        }
        if (args.length == 3) {
          List<String> list = Arrays.asList("east", "west", "south", "north", "up", "down");
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("replace")) {
        if (player == null || !player.hasPermission("cherry.wand.replace")) {
          return Collections.emptyList();
        }
        if (args.length == 2 || args.length == 3) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("cube")) {
        if (player == null || !player.hasPermission("cherry.wand.cube")) {
          return Collections.emptyList();
        }
        if (args.length == 2) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }
        if (3 <= args.length && args.length <= 7) {
          List<String> list = new ArrayList<>();
          list.addAll(Arrays.asList("-empty", "-walled", "-applyPhysics", "-data:[", "-e", "-w", "-ap", "-silent"));
          int n = 0;
          for (String arg : args) {
            if (n >= 2) {
              if (arg.equals("-empty") || arg.equals("-e")) {
                list.remove("-empty");
                list.remove("-e");
              }
              if (arg.equals("-walled") || arg.equals("-w")) {
                list.remove("-walled");
                list.remove("-w");
              }
              if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
                list.remove("-applyPhysics");
                list.remove("-ap");
              }
              if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
                list.remove("-applyPhysics");
                list.remove("-ap");
              }
              if (Pattern.compile("-data:\\[([^\\]]*)\\]").matcher(arg).matches() || Pattern.compile("-data:\\[([^\\]]*)").matcher(arg).matches()) {
                list.remove("-data:[");
              }
              if (Pattern.compile("-data:\\[([^\\]]*)").matcher(arg).matches() && n == args.length - 1) {
                String data = "";
                Matcher matcher = Pattern.compile("-data:\\[([^\\]]*)", Pattern.CASE_INSENSITIVE).matcher(arg);
                if (matcher.find()) {
                  data = matcher.group(1);
                }
                List<String> tlist = new ArrayList<>(Arrays.asList("-data:[" + data + "]"));
                return TabCompleter.autoComplete(tlist, args[args.length - 1]);
              }
              if (arg.equals("-silent")) {
                list.remove("-silent");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("cyl")) {
        if (player == null || !player.hasPermission("cherry.wand.cyl")) {
          return Collections.emptyList();
        }
        // 설치 가능한 블록 목록
        if (args.length == 2) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }
        // 정수 입력 (반지름)
        if (args.length == 3) {
          if (args[args.length - 1].isEmpty()) {
            return Collections.singletonList("<Integer radius>");
          }
          else if (Tool.Check.isInteger(args[args.length - 1])) {
            if (Integer.parseInt(args[args.length - 1]) > 1000) {
              return Collections.singletonList("너무 큰 수입니다 (n <= 1000)");
            }
            if (Integer.parseInt(args[args.length - 1]) < 1) {
              return Collections.singletonList("너무 작은 수입니다 (n >= 1)");
            }
            return Collections.singletonList(args[args.length - 1] + " ");
          }
          return Collections.singletonList(Msg.n2s("정수만 입력할 수 있습니다"));
        }
        // 정수 입력 (높이)
        if (args.length == 4) {
          if (args[args.length - 1].isEmpty()) {
            return Collections.singletonList("<Integer height>");
          }
          else if (Tool.Check.isInteger(args[args.length - 1])) {
            if (Integer.parseInt(args[args.length - 1]) > 256) {
              return Collections.singletonList("너무 큰 수입니다 (n <= 256)");
            }
            return Collections.singletonList(args[args.length - 1] + " ");
          }
          return Collections.singletonList(Msg.n2s("정수만 입력할 수 있습니다"));
        }
        // 옵션 입력
        if (5 <= args.length && args.length <= 8) {
          List<String> list = new ArrayList<>();
          list.addAll(Arrays.asList("-empty", "-pointed", "-applyPhysics", "-e", "-p", "-ap", "-silent"));
          int n = 0;
          for (String arg : args) {
            if (n >= 4) {
              if (arg.equals("-empty") || arg.equals("-e")) {
                list.remove("-empty");
                list.remove("-e");
              }
              if (arg.equals("-pointed") || arg.equals("-p")) {
                list.remove("-pointed");
                list.remove("-p");
              }
              if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
                list.remove("-applyPhysics");
                list.remove("-ap");
              }
              if (arg.equals("-silent")) {
                list.remove("-silent");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("sphere")) {
        if (player == null || !player.hasPermission("cherry.wand.sphere")) {
          return Collections.emptyList();
        }
        // 설치 가능한 블록 목록
        if (args.length == 2) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }
        // 정수 입력 (반지름)
        if (args.length == 3) {
          if (args[args.length - 1].isEmpty()) {
            return Collections.singletonList("<Integer radius>");
          }
          else if (Tool.Check.isInteger(args[args.length - 1])) {
            if (Integer.parseInt(args[args.length - 1]) > 1000) {
              return Collections.singletonList("너무 큰 수입니다 (n <= 1000)");
            }
            return Collections.singletonList(args[args.length - 1] + " ");
          }
          return Collections.singletonList(Msg.n2s("정수만 입력할 수 있습니다"));
        }
        // 옵션 입력
        if (4 <= args.length && args.length <= 7) {
          List<String> list = new ArrayList<>();
          list.addAll(Arrays.asList("-empty", "-pointed", "-applyPhysics", "-e", "-p", "-ap", "-silent"));
          int n = 0;
          for (String arg : args) {
            if (n >= 3) {
              if (arg.equals("-empty") || arg.equals("-e")) {
                list.remove("-empty");
                list.remove("-e");
              }
              if (arg.equals("-pointed") || arg.equals("-p")) {
                list.remove("-pointed");
                list.remove("-p");
              }
              if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
                list.remove("-applyPhysics");
                list.remove("-ap");
              }
              if (arg.equals("-silent")) {
                list.remove("-silent");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
        return Collections.emptyList();
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("wall")) {
        if (player == null || !player.hasPermission("cherry.wand.wall")) {
          return Collections.emptyList();
        }
        // 설치 가능한 블록 목록
        if (args.length == 2) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }
        // 옵션 입력
        if (3 <= args.length && args.length <= 4) {
          List<String> list = new ArrayList<>();
          list.addAll(Arrays.asList("-applyPhysics", "-ap", "-silent"));
          int n = 0;
          for (String arg : args) {
            if (n >= 2) {
              if (arg.equals("-applyPhysics") || arg.equals("-ap")) {
                list.remove("-applyPhysics");
                list.remove("-ap");
              }
              if (arg.equals("-silent")) {
                list.remove("-silent");
              }
            }
            n++;
          }
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("cmdscan")) {
        if (player == null || !player.hasPermission("cherry.wand.cmdscan")) {
          return Collections.emptyList();
        }
        if (args.length == 1) {
          if (args[args.length - 1].isEmpty()) {
            return Collections.singletonList("<Integer radius>");
          }
          else if (Tool.Check.isInteger(args[args.length - 1])) {
            if (Integer.parseInt(args[args.length - 1]) > 1000) {
              return Collections.singletonList("너무 큰 수입니다 (n <= 1000)");
            }
            return Collections.singletonList(args[args.length - 1] + " ");
          }
          return Collections.singletonList(Msg.n2s("정수만 입력할 수 있습니다"));
        }
      }

    }

    /*if (command.getName().equalsIgnoreCase("edit")) {

      if (args.length == 1) {
        List<String> list = Arrays.asList(
          "pos1", "pos2",
          "copy", "paste", "cut", "stack", "replace",
          "cube", "cyl", "sphere", "wall"
        );
        return TabCompleter.autoComplete(list, args[args.length - 1]);
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("stack")) {
        if (args.length == 3) {
          List<String> list = Arrays.asList("east", "west", "south", "north", "up", "down");
          return TabCompleter.autoComplete(list, args[args.length - 1]);
        }
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("replace")) {
        if (args.length == 2 || args.length == 3) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("cube")) {
        if (args.length == 2) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }
        List<String> list = Arrays.asList("-empty", "-walled", "-applyPhysics", "-e", "-w", "-ap");
        return TabCompleter.autoComplete(list, args[args.length - 1]);
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("cyl")) {
        if (args.length == 2) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }
        if (args.length == 3) {
          if (args[args.length - 1].isEmpty()) {
            List<String> list = Collections.singletonList("<Integer radius>");
            return list;
          }
          else if (Tool.Check.isInteger(args[args.length - 1])) {
            if (Integer.parseInt(args[args.length - 1]) > 1000) {
              List<String> list = Collections.singletonList("너무 큰 수입니다 (n <= 1000)");
              return list;
            }
            List<String> list = Collections.singletonList(args[args.length - 1] + " ");
            return list;
          }
          List<String> list = Collections.singletonList(Msg.n2s("정수만 입력할 수 있습니다"));
          return list;
        }
        if (args.length == 4) {
          if (args[args.length - 1].isEmpty()) {
            List<String> list = Collections.singletonList("<Integer height>");
            return list;
          }
          else if (Tool.Check.isInteger(args[args.length - 1])) {
            if (Integer.parseInt(args[args.length - 1]) > 256) {
              List<String> list = Collections.singletonList("너무 큰 수입니다 (n <= 256)");
              return list;
            }
            List<String> list = Collections.singletonList(args[args.length - 1] + " ");
            return list;
          }
          List<String> list = Collections.singletonList(Msg.n2s("정수만 입력할 수 있습니다"));
          return list;
        }
        List<String> list = Arrays.asList("-empty", "-pointed", "-applyPhysics", "-e", "-p", "-ap");
        return TabCompleter.autoComplete(list, args[args.length - 1]);
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("sphere")) {
        if (args.length == 2) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }
        if (args.length == 3) {
          if (args[args.length - 1].isEmpty()) {
            List<String> list = Collections.singletonList(Msg.n2s("&d<&5&oInteger&r&d &aradius&d>&r"));
            return list;
          }
          else if (Tool.Check.isInteger(args[args.length - 1])) {
            if (Integer.parseInt(args[args.length - 1]) > 1000) {
              List<String> list = Collections.singletonList(Msg.n2s("&c너무 큰 수입니다 (num <= 1000)&r"));
              return list;
            }
            List<String> list = Collections.singletonList(Msg.n2s("&a" + args[args.length - 1] + "&r"));
            return list;
          }
          List<String> list = Collections.singletonList(Msg.n2s("&c정수만 입력할 수 있습니다&r"));
          return list;
        }
        List<String> list = Arrays.asList("-empty", "-pointed", "-applyPhysics", "-e", "-p", "-ap");
        return TabCompleter.autoComplete(list, args[args.length - 1]);
      }

      if (args.length > 1 && args[0].equalsIgnoreCase("wall")) {
        if (args.length == 2) {
          return TabCompleter.autoComplete(Tool.List.materialBlocks(), args[args.length - 1]);
        }
        List<String> list = Arrays.asList("-applyPhysics", "-ap");
        return TabCompleter.autoComplete(list, args[args.length - 1]);
      }

    }

    if (command.getName().equalsIgnoreCase("brush")) {

      if (args.length == 1) {
        List<String> list = Arrays.asList(
          "set"
        );
        return TabCompleter.autoComplete(list, args[args.length - 1]);
      }

    }*/

    return Collections.emptyList();
  }
}
