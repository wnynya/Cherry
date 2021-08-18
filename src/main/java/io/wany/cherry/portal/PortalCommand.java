package io.wany.cherry.portal;

import io.wany.cherry.Cherry;
import io.wany.cherry.Message;
import io.wany.cherry.wand.Wand;
import io.wany.cherry.wand.area.Area;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PortalCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (!Portal.ENABLED) {
      Message.info(sender, Portal.PREFIX, "포탈 기능이 비활성화된 상태입니다");
      return true;
    }

    if (args.length == 0) {
      Message.error(sender, Message.CommandFeedback.NO_ARGS);
      return true;
    }

    /*
    명령어 구조

    포탈 추가
    cmd    0   1
    portal add <portal>

    포탈 제거
    cmd    0   1
    portal remove <portal>

    포탈 갱신
    cmd    0     1
    portal renew <portal>

    포탈 목록
    cmd    0
    portal list

    포탈 활성화
    cmd    0      1
    portal enable <portal>

    포탈 비활성화
    cmd    0       1
    portal disable <portal>

    포탈 사용
    cmd    0   1
    portal use <portal>

    cmd    0   1        2
    portal use <portal> [player]

    포탈 영역
    cmd    0    1        2   3      4    5
    portal area <portal> add <name> gate [type]

    cmd    0    1        2   3      4    5      6  7  8  9  10 11
    portal area <portal> add <name> gate [type] x1 y1 z1 x2 y2 z2

    cmd    0    1        2   3      4    5      6  7  8  9  10 11 12
    portal area <portal> add <name> gate [type] x1 y1 z1 x2 y2 z2 <world>

    cmd    0    1        2   3      4
    portal area <portal> add <name> sign

    cmd    0    1        2   3      4    5
    portal area <portal> add <name> sign wandpos

    cmd    0    1        2   3      4    5 6 7
    portal area <portal> add <name> sign x y z

    cmd    0    1        2   3      4    5 6 7 8
    portal area <portal> add <name> sign x y z <world>

    cmd    0    1        2      3
    portal area <portal> remove <name>

    cmd    0    1        2
    portal area <portal> list

    포탈 설정
    cmd    0   1        2           3...
    portal set <portal> displayname <name...>

    cmd    0   1        2        3
    portal set <portal> protocol [protocol]

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

    cmd    0   1        2   3...
    portal set <portal> cmd <msg...>

    cmd    0   1        2           3
    portal set <portal> cmdexecutor [executor]

     */

    // 포탈 지정이 불필요한 명령어
    // 포탈 추가 (생성)
    if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("create")) {

      // 권한 확인
      if (!sender.hasPermission("cherry.portal.add")) {
        Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
        return true;
      }

      if (args.length <= 1) {
        Message.error(sender, Portal.PREFIX, Message.CommandFeedback.NO_ARGS);
        return true;
      }

      String name = args[1];

      if (Portal.getPortalNames().contains(name)) {
        Message.error(sender, Portal.PREFIX, "이미 " + name + " 포탈이 존재합니다.");
        return true;
      }

      Portal.addPortal(name);
      Portal portal = Portal.getPortal(name);
      Message.info(sender, Portal.PREFIX, portal.displayNameString() + " 포탈이 추가되었습니다.");
      return true;

    }

    // 포탈 목록
    if (args[0].equalsIgnoreCase("list")) {

      if (!sender.hasPermission("cherry.portal.list")) {
        Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
        return true;
      }

      Message.info(sender, Portal.PREFIX, Portal.getPortalNames().toString());
      return true;

    }

    // 포탈 리로드
    if (args[0].equalsIgnoreCase("reload")) {

      if (!sender.hasPermission("cherry.portal.reload")) {
        Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
        return true;
      }

      Portal.onReload();
      Message.info(sender, Portal.PREFIX, "포탈 데이터를 리로드하였습니다.");
      return true;

    }

    // 포탈 지정이 필요한 명령어
    if (args.length <= 1) {
      Message.error(sender, Portal.PREFIX, Message.CommandFeedback.NO_ARGS);
      Message.error(sender, Portal.PREFIX, "usage: " + cmd.getLabel() + " " + args[0] + " [portalname] [args...]");
      return true;
    }
    String name = args[1];
    Portal portal = Portal.getPortal(name);
    if (portal == null) {
      Message.error(sender, Portal.PREFIX, name + " 포탈을 찾을 수 없습니다.");
      return true;
    }

    // 포탈 활성화
    if (args[0].equalsIgnoreCase("enable")) {

      if (!sender.hasPermission("cherry.portal.enable")) {
        Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
        return true;
      }

      portal.open();

      Message.info(sender, Portal.PREFIX, portal.displayNameString() + " 포탈을 활성화하였습니다.");

      portal.renew();

      return true;

    }

    // 포탈 비활성화
    if (args[0].equalsIgnoreCase("disable")) {

      if (!sender.hasPermission("cherry.portal.disable")) {
        Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
        return true;
      }

      portal.close();

      Message.info(sender, Portal.PREFIX, portal.displayNameString() + " 포탈을 비활성화하였습니다.");

      portal.renew();

      return true;

    }

    // 포탈 설정
    if (args[0].equalsIgnoreCase("set")) {

      if (!sender.hasPermission("cherry.portal.set")) {
        Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
        return true;
      }

      if (args.length <= 3) {
        Message.error(sender, Message.CommandFeedback.NO_ARGS);
        return true;
      }

      if (args[2].equalsIgnoreCase("displayname")) {

        String oldName = portal.displayNameString();
        StringBuilder displayName = new StringBuilder(args[3]);

        for (int n = 4; n < args.length; n++) {
          displayName.append(" ").append(args[n]);
        }
        portal.displayName(displayName.toString());

        Message.info(sender, Portal.PREFIX, Message.effect(oldName + " 포탈의 표시 이름이 " + Message.effect(portal.displayNameString()) + " (으)로 설정되었습니다"));

        portal.renew();

        return true;

      }

      /*else if (args[2].equalsIgnoreCase("cmd")) {

        StringBuilder cmdStr = new StringBuilder(args[3]);

        for (int n = 4; n < args.length; n++) {
          cmdStr.append(" ").append(args[n]);
        }
        portal.setCmdMsg(cmdStr.toString());

        Message.info(sender, Portal.PREFIX, Message.effect(portal.displayNameString() + " 포탈의 명령어가 " + cmdStr.toString() + " (으)로 설정되었습니다"));

        portal.renew();

        return true;

      }

      else if (args[2].equalsIgnoreCase("cmdexecutor")) {

        Portal.CmdExecutor ce = Portal.CmdExecutor.getCmdExecutor(args[3]);

        if (ce == null) {
          return true;
        }

        portal.setCmdExecutor(ce);

        Message.info(sender, Portal.PREFIX, Message.effect(portal.displayNameString() + " 포탈의 명령어 실행자가 " + ce.toString() + " (으)로 설정되었습니다"));

        portal.renew();

        return true;

      }*/

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
       */
      /*else if (args[2].equalsIgnoreCase("goto")) {

        if (args[3].equalsIgnoreCase("location")) {

          if (!(args.length > 4)) {
            Message.error(sender, Message.CommandFeedback.NO_ARGS);
            Message.error(sender, Portal.PREFIX, "usage: " + cmd.getLabel() + " set " + portal.name() + " goto location <here|me|wandpos|Location>");
            return true;
          }

          Location loc;

          if (args[4].equalsIgnoreCase("here")) {
            if (sender instanceof Player) {
              Player player = (Player) sender;
              loc = player.getLocation();
            }
            else {
              Message.error(sender, Portal.PREFIX, "콘솔에서 사용할 수 없는 명령어입니다. " + "콘솔에서 설정하려면 좌표를 입력하십시오.");
              return true;
            }
          }

          else if (args[4].equalsIgnoreCase("wandpos")) {
            Wand wand;
            if (sender instanceof Player) {
              Player player = (Player) sender;
              wand = Wand.getWand(player.getUniqueId());
            }
            else {
              wand = Wand.getWand(Cherry.UUID);
            }
            Location pos1 = wand.getEdit().getPosition(1);
            if (pos1 == null) {
              Message.error(sender, Portal.PREFIX, "영역 지정이 완료되지 않았습니다.");
              return true;
            }
            loc = pos1;
          }

          else if (args[4].equalsIgnoreCase("me")) {
            if (sender instanceof Player) {
              Player player = (Player) sender;
              loc = player.getLocation();
            }
            else {
              Message.error(sender, Portal.PREFIX, "콘솔에서 사용할 수 없는 명령어입니다. " + "콘솔에서 설정하려면 좌표를 입력하십시오.");
              return true;
            }
          }

          else if (args.length > 7) {
            World world = Bukkit.getWorld(args[7]);
            if (world == null) {
              Message.error(sender, Portal.PREFIX, "월드를 찾을 수 없습니다.");
              return true;
            }

            Location pos1;

            try {
              int x1 = Integer.parseInt(args[4]);
              int y1 = Integer.parseInt(args[5]);
              int z1 = Integer.parseInt(args[6]);
              pos1 = new Location(world, x1, y1, z1);
            }
            catch (Exception e) {
              Message.error(sender, Portal.PREFIX, "잘못된 좌표입니다.");
              return true;
            }

            loc = pos1;
          }

          else if (args.length > 6) {
            World world;
            if (sender instanceof Player) {
              Player player = (Player) sender;
              world = player.getWorld();
            }
            else {
              Message.error(sender, Portal.PREFIX, "콘솔에서 사용할 수 없는 명령어입니다. " + "콘솔에서 좌표를 직접 설정하려면 월드 이름을 입력하십시오.");
              return true;
            }

            Location pos1;

            try {
              int x1 = Integer.parseInt(args[4]);
              int y1 = Integer.parseInt(args[5]);
              int z1 = Integer.parseInt(args[6]);
              pos1 = new Location(world, x1, y1, z1);
            }
            catch (Exception e) {
              Message.error(sender, Portal.PREFIX, "잘못된 좌표입니다.");
              return true;
            }

            loc = pos1;
          }

          else {
            Message.error(sender, Message.CommandFeedback.UNKNOWN);
            Message.error(sender, Portal.PREFIX, "usage: " + cmd.getLabel() + " set " + portal.name() + " goto location <here|me|wandpos|Location>");
            return true;
          }

          portal.setGotoLocation(loc);

          Message.info(sender, Portal.PREFIX, Message.effect( portal.displayNameString() + " 포탈의 목적지 좌표가 &e" + loc.toString() + "&r(으)로 설정되었습니다"));

          portal.renew();

          return true;

        }

        else if (args[3].equalsIgnoreCase("server")) {

          if (!(args.length > 4)) {
            Message.error(sender, Message.CommandFeedback.NO_ARGS);
            return true;
          }

          portal.setGotoServer(args[4]);

          Message.info(sender, Portal.PREFIX, portal.displayNameString() + " 포탈의 목적지 서버가 &e" + args[4] + "&r서버로 설정되었습니다");

          portal.renew();

          return true;

        }

        else {
          Message.error(sender, Message.CommandFeedback.UNKNOWN);
          Message.error(sender, Portal.PREFIX, "usage: " + cmd.getLabel() + " set " + portal.name() + " goto <location|server> [args...]");
          return true;
        }

      }

      else if (args[2].equalsIgnoreCase("protocol")) {

        PortalProtocol protocol = Portal.Protocol.getProtocol(args[3]);

        if (protocol == null) {
          Message.error(sender, Portal.PREFIX, "잘못된 프로토콜입니다.");
          return true;
        }

        portal.setProtocol(protocol);

        Message.info(sender, Portal.PREFIX, portal.displayNameString() + " 포탈의 프로토콜을 설정하였습니다.");
        return true;

      }

      else {
        Message.error(sender, Message.CommandFeedback.UNKNOWN);
        Message.error(sender, Portal.PREFIX, "usage: " + cmd.getLabel() + " set " + portal.name() + " <displayname|protocol|goto|cmd> [args...]");
        return true;
      }

    }

    // 포탈 사용
    if (args[0].equalsIgnoreCase("use")) {

      if (!sender.hasPermission("cherry.portal.use")) {
        Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
        return true;
      }

      Player target = null;
      if (args.length > 2) {
        target = Bukkit.getPlayer(args[2]);
      }
      else {
        if (sender instanceof Player) {
          target = (Player) sender;
        }
        else {
          Message.error(sender, Portal.PREFIX, "콘솔에서 사용할 수 없는 명령어입니다. " + "콘솔에서 사용하려면 플래이어를 지정하십시오.");
          return true;
        }
      }

      if (target == null) {
        Message.error(sender, Portal.PREFIX, "대상을 찾을 수 없습니다.");
        return true;
      }

      portal.use(target);

      if (sender instanceof Player && sender.equals(target)) {
        return true;
      }
      else {
        Message.info(sender, Portal.PREFIX, portal.displayNameString() + " 포탈을 통해 " + target.name() + "을(를) 이동시켰습니다.");
      }

      return true;

    }

    // 포탈 영역
    if (args[0].equalsIgnoreCase("area")) {

      if (!sender.hasPermission("cherry.portal.area")) {
        Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
        return true;
      }

      if (args.length <= 2) {
        Message.error(sender, Portal.PREFIX,  Message.CommandFeedback.NO_ARGS);
        Message.error(sender, Portal.PREFIX, "usage: " + cmd.getLabel() + " area " + portal.name() + " <add|remove|list> [args...]");
        return true;
      }

      if (args[2].equalsIgnoreCase("list")) {
        Message.info(sender, Portal.PREFIX, portal.getPortalAreaNames().toString());
        return true;
      }

      if (args.length <= 3) {
        Message.error(sender, Portal.PREFIX,  Message.CommandFeedback.NO_ARGS);
        Message.error(sender, Portal.PREFIX, "usage: " + cmd.getLabel() + " area " + portal.name() + " " + args[2] + " [areaname] [args...]");
        return true;
      }

      String areaName = args[3];

      if (args[2].equalsIgnoreCase("remove")) {

        if (!portal.getPortalAreaNames().contains(areaName)) {
          Message.error(sender, Portal.PREFIX, "영역 " + name + "." + areaName + " 을(를) 찾을 수 없습니다.");
          return true;
        }
        portal.removePortalArea(areaName);

        Message.info(sender, Portal.PREFIX, portal.displayNameString() + " 포탈의 출발 영역을 제거하였습니다 (" + name + "." + areaName + ")");

        portal.renew();

        return true;

      }*/

      /*
      cmd    0    1        2   3
      portal area <portal> add [gate|sign]

      cmd    0    1        2      3
      portal area <portal> remove <name>

      cmd    0    1        2
      portal area <portal> list
       */
      else if (args[2].equalsIgnoreCase("add")) {

        if (args.length <= 4) {
          Message.error(sender, Portal.PREFIX, Message.CommandFeedback.NO_ARGS);
          return true;
        }

        /*
        cmd    0    1        2   3      4    5
        portal area <portal> add <name> gate [type]

        cmd    0    1        2   3      4    5      6  7  8  9  10 11
        portal area <portal> add <name> gate [type] x1 y1 z1 x2 y2 z2

        cmd    0    1        2   3      4    5      6  7  8  9  10 11 12
        portal area <portal> add <name> gate [type] x1 y1 z1 x2 y2 z2 <world>
         */
        else if (args[4].equalsIgnoreCase("gate")) {

          if (args.length <= 5) {
            Message.error(sender, Portal.PREFIX, Message.CommandFeedback.NO_ARGS);
            return true;
          }

          List<Location> area = null;

          Location pos1;
          Location pos2;

          if (args.length > 13) {
            World world = Bukkit.getWorld(args[12]);
            if (world == null) {
              Message.error(sender, Portal.PREFIX, "월드를 찾을 수 없습니다.");
              return true;
            }

            try {
              int x1 = Integer.parseInt(args[6]);
              int y1 = Integer.parseInt(args[7]);
              int z1 = Integer.parseInt(args[8]);
              int x2 = Integer.parseInt(args[9]);
              int y2 = Integer.parseInt(args[10]);
              int z2 = Integer.parseInt(args[11]);
              pos1 = new Location(world, x1, y1, z1);
              pos2 = new Location(world, x2, y2, z2);
            }
            catch (Exception e) {
              Message.error(sender, Portal.PREFIX, "잘못된 좌표입니다.");
              return true;
            }

            area = Area.CUBE.getArea(pos1, pos2);
          }

          else if (args.length > 12) {
            World world;
            if (sender instanceof Player) {
              Player player = (Player) sender;
              world = player.getWorld();
            }
            else {
              Message.error(sender, Portal.PREFIX, "콘솔에서 사용할 수 없는 명령어입니다. " + "콘솔에서 좌표를 직접 설정하려면 월드 이름을 입력하십시오.");
              return true;
            }

            try {
              int x1 = Integer.parseInt(args[6]);
              int y1 = Integer.parseInt(args[7]);
              int z1 = Integer.parseInt(args[8]);
              int x2 = Integer.parseInt(args[9]);
              int y2 = Integer.parseInt(args[10]);
              int z2 = Integer.parseInt(args[11]);
              pos1 = new Location(world, x1, y1, z1);
              pos2 = new Location(world, x2, y2, z2);
            }
            catch (Exception e) {
              Message.error(sender, Portal.PREFIX, "잘못된 좌표입니다.");
              return true;
            }

            area = Area.CUBE.getArea(pos1, pos2);
          }

          else {
            Wand wand;
            if (sender instanceof Player) {
              Player player = (Player) sender;
              wand = Wand.getWand(player.getUniqueId());
            }
            else {
              wand = Wand.getWand(Cherry.UUID);
            }

            pos1 = wand.getEdit().getPosition(1);
            pos2 = wand.getEdit().getPosition(2);

            if (pos1 == null || pos2 == null) {
              Message.error(sender, Portal.PREFIX, "포지션 지정이 완료되지 않았습니다.");
              return true;
            }

            area = Area.CUBE.getArea(pos1, pos2);
          }

          if (area == null || area.isEmpty()) {
            Message.error(sender, Portal.PREFIX, "영역 지정이 완료되지 않았습니다.");
            return true;
          }

          String axis = "";

          if (pos1.getX() == pos2.getX()) {
            axis = "z";
          }
          else if (pos1.getZ() == pos2.getZ()) {
            axis = "x";
          }
          else if (pos1.getY() == pos2.getY()) {
            axis = "y";
          }
          else {
            axis = "d";
          }

          BlockData fill = null;

          String type = args[5];

          switch (type.toLowerCase()) {

            case "nether_portal": {
              if (axis.equals("x") || axis.equals("z")) {
                fill = Bukkit.createBlockData(Material.NETHER_PORTAL, "[axis=" + axis + "]");
              }
              else {
                Message.error(sender, Portal.PREFIX, "영역의 형태가 잘못되었습니다.");
                return true;
              }
              break;
            }

            case "end_gateway": {
              fill = Bukkit.createBlockData(Material.END_GATEWAY);
              break;
            }

            case "end_portal": {
              if (axis.equals("z")) {
                fill = Bukkit.createBlockData(Material.END_PORTAL);
              }
              else {
                Message.error(sender, Portal.PREFIX, "영역의 형태가 잘못되었습니다.");
                return true;
              }
              break;
            }

            case "water": {
              fill = Bukkit.createBlockData(Material.WATER);
              break;
            }

            case "air": {
              fill = Bukkit.createBlockData(Material.AIR);
              break;
            }

            default: {
              Message.error(sender, Portal.PREFIX, "알 수 없는 채우기 타입입니다.");
              return true;
            }

          }

          //Message.info(sender, Portal.PREFIX, portal.displayNameString() + " 포탈의 출발 영역을 설정하였습니다 (" + name + "." + areaName + ")");

          return true;

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
        else if (args[4].equalsIgnoreCase("sign")) {

          Location loc;

          if (args.length > 6 && args[5].equalsIgnoreCase("wandpos")) {
            if (sender instanceof Player) {
              Player player = (Player) sender;
              Wand wand = Wand.getWand(player.getUniqueId());
              Location pos1 = wand.getEdit().getPosition(1);
              if (pos1 == null) {
                Message.error(sender, Portal.PREFIX, "영역 지정이 완료되지 않았습니다.");
                return true;
              }
              else {
                Block block = pos1.getBlock();
                if (block.getState() instanceof Sign) {
                  loc = block.getLocation();
                }
                else {
                  Message.error(sender, Portal.PREFIX, "바라보는 블록이 표지판이 아닙니다.");
                  return true;
                }
              }
            }
            else {
              Wand wand = Wand.getWand(Cherry.UUID);
              Location pos1 = wand.getEdit().getPosition(1);
              if (pos1 == null) {
                Message.error(sender, Portal.PREFIX, "영역 지정이 완료되지 않았습니다.");
                return true;
              }
              else {
                Block block = pos1.getBlock();
                if (block.getState() instanceof Sign) {
                  loc = block.getLocation();
                }
                else {
                  Message.error(sender, Portal.PREFIX, "바라보는 블록이 표지판이 아닙니다.");
                  return true;
                }
              }
            }
          }

          else if (args.length > 9) {
            World world = Bukkit.getWorld(args[8]);
            if (world == null) {
              Message.error(sender, Portal.PREFIX, "월드를 찾을 수 없습니다.");
              return true;
            }

            Location pos1;

            try {
              int x1 = Integer.parseInt(args[5]);
              int y1 = Integer.parseInt(args[6]);
              int z1 = Integer.parseInt(args[7]);
              pos1 = new Location(world, x1, y1, z1);
            }
            catch (Exception e) {
              Message.error(sender, Portal.PREFIX, "잘못된 좌표입니다.");
              return true;
            }

            Block block = pos1.getBlock();
            if (block.getState() instanceof Sign) {
              loc = block.getLocation();
            }
            else {
              Message.error(sender, Portal.PREFIX, "바라보는 블록이 표지판이 아닙니다.");
              return true;
            }
          }

          else if (args.length > 8) {
            World world;
            if (sender instanceof Player) {
              Player player = (Player) sender;
              world = player.getWorld();
            }
            else {
              Message.error(sender, Portal.PREFIX, "콘솔에서 사용할 수 없는 명령어입니다. " + "콘솔에서 좌표를 직접 설정하려면 월드 이름을 입력하십시오.");
              return true;
            }

            Location pos1;

            try {
              int x1 = Integer.parseInt(args[5]);
              int y1 = Integer.parseInt(args[6]);
              int z1 = Integer.parseInt(args[7]);
              pos1 = new Location(world, x1, y1, z1);
            }
            catch (Exception e) {
              Message.error(sender, Portal.PREFIX, "잘못된 좌표입니다.");
              return true;
            }

            Block block = pos1.getBlock();
            if (block.getState() instanceof Sign) {
              loc = block.getLocation();
            }
            else {
              Message.error(sender, Portal.PREFIX, "바라보는 블록이 표지판이 아닙니다.");
              return true;
            }
          }

          else {
            if (sender instanceof Player) {
              Player player = (Player) sender;
              Block block = player.getTargetBlockExact(10);
              if (block == null) {
                Message.error(sender, Portal.PREFIX, "바라보는 블록을 찾을 수 없습니다.");
                return true;
              }
              else if (block.getState() instanceof Sign) {
                loc = block.getLocation();
              }
              else {
                Message.error(sender, Portal.PREFIX, "바라보는 블록이 표지판이 아닙니다.");
                return true;
              }
            }
            else {
              Message.error(sender, Portal.PREFIX, "콘솔에서 사용할 수 없는 명령어입니다. " + "콘솔에서 설정하려면 좌표를 입력하십시오.");
              return true;
            }
          }


          //Message.info(sender, Portal.PREFIX, portal.displayNameString() + " 포탈 표지판을 설정하였습니다 (" + name + "." + areaName + ")");

          return true;

        }

        else {
          Message.error(sender, Portal.PREFIX, Message.CommandFeedback.UNKNOWN);
          //Message.error(sender, Portal.PREFIX, "usage: " + cmd.getLabel() + " area " + portal.name() + " add " + areaName + " <sign|gate> [args...]");
          return true;
        }

      }

      else {
        Message.error(sender, Portal.PREFIX, Message.CommandFeedback.UNKNOWN);
        Message.error(sender, Portal.PREFIX, "usage: " + cmd.getLabel() + " area " + portal.name() + " <list|remove|add> [areaname] [args...]");
        return true;
      }

    }

    // 포탈 갱신
    if (args[0].equalsIgnoreCase("renew")) {

      if (!sender.hasPermission("cherry.portal.renew")) {
        Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
        return true;
      }

      portal.renew();

      Message.info(sender, Portal.PREFIX, portal.displayNameString() + " 포탈의 트리거 영역을 갱신하였습니다.");
      return true;

    }

    // 포탈 정보
    /*if (args[0].equalsIgnoreCase("info")) {

      // 권한 확인
      if (!sender.hasPermission("cherry.portal.info")) {
        Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
        return true;
      }

      Message.info(sender, Portal.PREFIX, Message.effect(portal.displayNameString() + "&f 포탈 정보:"));
      Message.info(sender, Portal.PREFIX, Message.effect("&f이름 (ID): &e" + portal.name()));
      Message.info(sender, Portal.PREFIX, Message.effect("&f표시 이름: &e" + portal.displayNameString()));
      Message.info(sender, Portal.PREFIX, Message.effect("&f포탈 영역: (&e" + portal.getPortalAreaNames().size() + "&f)"));
      for (String areaName : portal.getPortalAreaNames()) {
        PortalArea pa = portal.getPortalArea(areaName);
        Message.info(sender, Portal.PREFIX, Message.effect("&f  [" + portal.name() + ".&e" + pa.name() + "&f]: "));
        Message.info(sender, Portal.PREFIX, Message.effect("&f    Type: &e" + pa.getType() + ""));
        Message.info(sender, Portal.PREFIX, Message.effect("&f    Fill: &e" + pa.getFill().getMaterial().toString()));
        Message.info(sender, Portal.PREFIX, Message.effect("&f    Area: (&e" + pa.getArea().size() + "&f)"));
        Message.info(sender, Portal.PREFIX, Message.effect("&f      area[0]: &e" + Tool.loc2StrWithWorld(pa.getArea().get(0)) ));
      }

      return true;

    }

    // 포탈 제거
    if (args[0].equalsIgnoreCase("remove")) {

      // 권한 확인
      if (!sender.hasPermission("cherry.portal.remove")) {
        Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
        return true;
      }

      String portalDisplayName = portal.displayNameString();
      Portal.removePortal(name);
      Message.info(sender, Portal.PREFIX, portalDisplayName + " 포탈이 제거되었습니다.");

      portal.renew();

      return true;

    }*/


    Message.error(sender, Portal.PREFIX, Message.CommandFeedback.UNKNOWN);
    Message.error(sender, Portal.PREFIX, "usage: " + cmd.getLabel() + " <add|remove|list|info|set|area|use|renew>");

    return true;

  }
}
