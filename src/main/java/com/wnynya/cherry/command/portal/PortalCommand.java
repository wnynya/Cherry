package com.wnynya.cherry.command.portal;

import com.wnynya.cherry.Msg;
import com.wnynya.cherry.Tool;
import com.wnynya.cherry.portal.Portal;
import com.wnynya.cherry.portal.PortalArea;
import com.wnynya.cherry.portal.PortalProtocol;
import com.wnynya.cherry.wand.Wand;
import com.wnynya.cherry.wand.area.Area;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class PortalCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    Player player = null;

    if (sender instanceof Player) {
      player = (Player) sender;
    }

    if (args.length == 0) {
      Msg.error(sender, Msg.NO_ARGS);
      return true;
    }

    // 포탈 추가 (생성)
    if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("create")) {

      // 권한 확인
      if (!sender.hasPermission("cherry.portal.add")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      if (args.length <= 1) {
        Msg.error(sender, Msg.Prefix.PORTAL, Msg.NO_ARGS);
        return true;
      }

      String name = args[1];

      if (Portal.getPortalNames().contains(name)) {
        Msg.error(sender, Msg.Prefix.PORTAL, "이미 " + name + " 포탈이 존재합니다.");
        return true;
      }

      Portal.createPortal(name);
      Portal portal = Portal.getPortal(name);
      Msg.info(sender, Msg.Prefix.PORTAL, portal.getDisplayName() + " 포탈이 추가되었습니다.");
      return true;

    }

    // 포탈 목록
    if (args[0].equalsIgnoreCase("list")) {

      if (!sender.hasPermission("cherry.portal.list")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      Msg.info(sender, Msg.Prefix.PORTAL, Portal.getPortalNames().toString());
      return true;

    }


    if (args.length <= 1) {
      Msg.error(sender, Msg.Prefix.PORTAL, Msg.NO_ARGS);
      return true;
    }
    String name = args[1];
    Portal portal = Portal.getPortal(name);
    if (portal == null) {
      Msg.error(sender, Msg.Prefix.PORTAL, name + " 포탈을 찾을 수 없습니다.");
      return true;
    }


    // 포탈 활성화
    if (args[0].equalsIgnoreCase("enable")) {

      if (!sender.hasPermission("cherry.portal.enable")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      portal.setEnable(true);
      Msg.info(sender, Msg.Prefix.PORTAL, portal.getDisplayName() + " 포탈을 활성화하였습니다.");
      return true;

    }

    // 포탈 비활성화
    if (args[0].equalsIgnoreCase("disable")) {

      if (!sender.hasPermission("cherry.portal.disable")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      portal.setEnable(false);
      Msg.info(sender, Msg.Prefix.PORTAL, portal.getDisplayName() + " 포탈을 비활성화하였습니다");
      return true;

    }

    // 포탈 설정
    if (args[0].equalsIgnoreCase("set")) {

      if (!sender.hasPermission("cherry.portal.set")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      if (args.length <= 3) {
        Msg.error(sender, Msg.NO_ARGS);
        return true;
      }

      if (args[2].equalsIgnoreCase("displayname")) {

        String oldName = portal.getDisplayName();
        StringBuilder displayName = new StringBuilder(args[3]);

        for (int n = 4; n < args.length; n++) {
          displayName.append(" ").append(args[n]);
        }
        portal.setDisplayName(Msg.n2s(displayName.toString()));

        Msg.info(sender, Msg.Prefix.PORTAL, Msg.n2s(oldName + " 포탈의 표시 이름이 " + portal.getDisplayName() + " (으)로 설정되었습니다"));
        return true;
      }

      if (args[2].equalsIgnoreCase("goto")) {
        if (args[3].equalsIgnoreCase("location")) {
          if (!(args.length > 4)) {
            Msg.error(sender, Msg.NO_ARGS);
            return true;
          }
          if (args[4].equalsIgnoreCase("here")) {
            if (player == null) {
              Msg.error(sender, "플레이어를 찾울 수 없습니다");
              return true;
            }
            Block block = player.getTargetBlockExact(10);
            if (block == null) {
              Msg.error(sender, "바라보는 블럭을 찾을 수 없습니다. 블럭을 바라본 상태에서 명령어를 사용하십시오");
              return true;
            }
            Location loc = new Location(block.getWorld(), block.getX() + 0.5, block.getY() + 1, block.getZ() + 0.5);
            portal.setGotoLocation(loc);

            Msg.info(sender, Msg.Prefix.PORTAL + Msg.n2s(portal.getDisplayName() + "포탈의 목적지 좌표가 &e" + Tool.loc2Str(loc) + "&r로 설정되었습니다"));
            return true;
          }
          if (args[4].equalsIgnoreCase("me")) {
            if (player == null) {
              Msg.error(sender, "플레이어를 찾울 수 없습니다");
              return true;
            }
            Location loc = player.getLocation();
            portal.setGotoLocation(loc);

            Msg.info(sender, Msg.Prefix.PORTAL + Msg.n2s(portal.getDisplayName() + "포탈의 목적지 좌표가 &e" + Tool.loc2Str(loc) + "&r로 설정되었습니다"));
            return true;
          }
          if (args.length == 7) {
            if (player == null) {
              Msg.error(sender, "플레이어를 찾울 수 없습니다");
              return true;
            }
            Location loc = new Location(player.getWorld(), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]));
            portal.setGotoLocation(loc);

            Msg.info(sender, Msg.Prefix.PORTAL + Msg.n2s(portal.getDisplayName() + "포탈의 목적지 좌표가 &e" + Tool.loc2Str(loc) + "&r로 설정되었습니다"));
            return true;
          }
          if (args.length == 8) {
            World world = Bukkit.getWorld(args[7]);
            if (world == null) {
              Msg.error(sender, "월드를 찾울 수 없습니다");
              return true;
            }
            Location loc = new Location(world, Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]));
            portal.setGotoLocation(loc);

            Msg.info(sender, Msg.Prefix.PORTAL + Msg.n2s(portal.getDisplayName() + "포탈의 목적지 좌표가 &e" + Tool.loc2StrWithWorld(loc) + "&r로 설정되었습니다"));
            return true;
          }
        }
        if (args[3].equalsIgnoreCase("server")) {
          if (!(args.length > 4)) {
            Msg.error(sender, Msg.NO_ARGS);
            return true;
          }
          portal.setGotoServer(args[4]);

          Msg.info(sender, Msg.Prefix.PORTAL + Msg.n2s(portal.getDisplayName() + "포탈의 목적지 서버가 &e" + args[4] + "&r서버로 설정되었습니다"));
          return true;
        }
      }

      if (args[2].equalsIgnoreCase("protocol")) {
        PortalProtocol protocol = Portal.Protocol.getProtocol(args[3]);
        if (protocol == null) {
          Msg.error(sender, "잘못된 프로토콜 이름입니다.");
          return true;
        }
        portal.setProtocol(protocol);

        Msg.info(sender, Msg.Prefix.PORTAL + "포탈의 프로토콜을 설정하였습니다.");
        return true;
      }

    }

    if (args[0].equalsIgnoreCase("use")) {

      if (!sender.hasPermission("cherry.portal.use")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      Player target = null;
      if (args.length > 2) {
        target = Bukkit.getPlayer(args[2]);
      }
      else {
        target = player;
      }

      if (target == null) {
        Msg.error(sender, Msg.Prefix.PORTAL, "이동 대상을 찾을 수 없습니다");
        return true;
      }

      portal.use(target);

      if (player == null || !player.equals(target)) {
        Msg.info(sender, Msg.Prefix.PORTAL, portal.getDisplayName() + " 포탈을 통해 " + target.getName() + "를 이동시켰습니다");
      }

      return true;

    }

    if (args[0].equalsIgnoreCase("area")) {

      if (!sender.hasPermission("cherry.portal.area")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      if (args.length <= 2) {
        Msg.error(sender, Msg.NO_ARGS);
        return true;
      }

      if (player == null) {
        Msg.error(sender, "플레이어를 찾울 수 없습니다");
        return true;
      }

      if (args[2].equalsIgnoreCase("add")) {

        if (args.length <= 3) {
          Msg.error(sender, Msg.NO_ARGS);
          return true;
        }

        if (args[4].equalsIgnoreCase("gate")) {
          if (args[5].equalsIgnoreCase("wandpos")) {
            Wand wand = Wand.getWand(player.getUniqueId());

            Location pos1 = wand.getEdit().getPosition(1);
            Location pos2 = wand.getEdit().getPosition(2);

            if (pos1 == null || pos2 == null) {
              Msg.error(sender, "포지션 지정이 완료되지 않았습니다");
              return true;
            }
            List<Location> area = Area.CUBE.getArea(pos1, pos2);
            if (area == null || area.isEmpty()) {
              Msg.error(sender, "영역 지정이 완료되지 않았습니다");
              return true;
            }

            PortalArea.Type type;
            if (args.length > 6) {
              try {
                type = PortalArea.Type.valueOf(args[6].toUpperCase());
              }
              catch (Exception e) {
                Msg.error(sender, "올바르지 않은 포탈 타입입니다.");
                return true;
              }
            }
            else {
              Msg.error(sender, Msg.NO_ARGS);
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
              Msg.error(sender, "영역의 형태가 잘못되었습니다.");
              return true;
            }

            portal.setPortalArea(new PortalArea(name, area, type, axis));


            Msg.info(sender, Msg.Prefix.PORTAL + "포탈의 출발 영역을 설정하였습니다 (" + portal.getName() + "." + name + ")");

            portal.fillPortalArea(portal.getPortalArea(name));
            return true;
          }
        }
        if (args[4].equalsIgnoreCase("sign")) {
          if (args[5].equalsIgnoreCase("wandpos")) {
            Wand wand = Wand.getWand(player.getUniqueId());
            Location pos1 = wand.getEdit().getPosition(1);
            if (pos1 == null) {
              Msg.error(sender, "포지션 지정이 완료되지 않았습니다");
              return true;
            }
            List<Location> area = Area.CUBE.getArea(pos1, pos1);
            if (area == null || area.isEmpty()) {
              Msg.error(sender, "영역 지정이 완료되지 않았습니다");
              return true;
            }

            portal.setPortalArea(new PortalArea(name, area, PortalArea.Type.SIGN, "none"));

            Msg.info(sender, Msg.Prefix.PORTAL + "포탈의 출발 영역이 설정되었습니다 (" + portal.getName() + "." + name + ")");
            return true;
          }
          if (args[5].equalsIgnoreCase("here")) {
            Block block = player.getTargetBlockExact(10);
            if (block == null) {
              Msg.error(sender, "바라보는 블럭을 찾을 수 없습니다");
              return true;
            }
            Location pos1 = block.getLocation();

            List<Location> area = Area.CUBE.getArea(pos1, pos1);
            if (area == null || area.isEmpty()) {
              return true;
            }

            portal.setPortalArea(new PortalArea(name, area, PortalArea.Type.SIGN, "none"));

            Msg.info(sender, Msg.Prefix.PORTAL + "포탈의 출발 영역이 설정되었습니다 (" + portal.getName() + "." + name + ")");
            return true;
          }
        }
      }

      if (args[2].equalsIgnoreCase("remove")) {
        if (args.length <= 3) {
          Msg.error(sender, Msg.NO_ARGS);
          return true;
        }

        if (!portal.getPortalAreaNames().contains(args[3])) {
          Msg.error(sender, "영역 " + portal.getName() + "." + args[3] + " 을 찾을 수 없습니다");
          return true;
        }
        portal.removePortalArea(args[3]);

        Msg.info(sender, Msg.Prefix.PORTAL + "포탈의 출발 영역을 제거하였습니다 (" + portal.getName() + "." + args[3] + ")");
        return true;
      }

      if (args[2].equalsIgnoreCase("list")) {
        Msg.info(sender, Msg.Prefix.PORTAL + portal.getPortalAreaNames());
        return true;
      }

    }

    if (args[0].equalsIgnoreCase("renew")) {

      if (!sender.hasPermission("cherry.portal.renew")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      portal.fillPortalAreas();
      Msg.info(sender, Msg.Prefix.PORTAL, portal.getDisplayName() + " 포탈의 트리거 영역을 갱신하였습니다.");
      return true;

    }

    // 포탈 제거
    if (args[0].equalsIgnoreCase("remove")) {

      // 권한 확인
      if (!sender.hasPermission("cherry.portal.remove")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      String portalDisplayName = portal.getDisplayName();
      Portal.removePortal(name);
      Msg.info(sender, Msg.Prefix.PORTAL, portalDisplayName + " 포탈이 제거되었습니다.");
      return true;

    }


    Msg.error(sender, Msg.UNKNOWN);
    return true;
  }
}
