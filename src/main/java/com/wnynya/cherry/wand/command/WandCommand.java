package com.wnynya.cherry.wand.command;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.Tool;
import com.wnynya.cherry.korean.BlockNames;
import com.wnynya.cherry.wand.Wand;
import com.wnynya.cherry.wand.WandBlock;
import com.wnynya.cherry.wand.area.Area;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WandCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (!Wand.enabled) {
      Msg.info(sender, Msg.Prefix.WAND, "완드 기능이 비활성화된 상태입니다");
      return true;
    }

    UUID uuid;
    org.bukkit.entity.Player player = null;
    Wand wand;

    if (sender instanceof org.bukkit.entity.Player) {
      player = (org.bukkit.entity.Player) sender;
      wand = Wand.getWand(player);
    }
    else {
      uuid = Cherry.uuid;
      wand = Wand.getWand(uuid);
    }

    if (args.length == 0) {
      Msg.error(sender, Msg.NO_ARGS);
      return true;
    }

    // 완드 아이템 얻기 (에딧)
    if (args[0].equalsIgnoreCase("get")) {
      if (!sender.hasPermission("cherry.wand.get")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (player == null) {
        Msg.error(sender, Msg.Player.ONLY);
        return true;
      }
      if (args.length >= 2 && args[1].equalsIgnoreCase("edit")) {
        // 완드 에딧용 아이템을 뭐
        Msg.info(sender, Msg.Prefix.WAND + "완드 에딧용 아이템을 뭐");
      }
      ItemStack item = Wand.getWandItem(Wand.ItemType.EDIT_POSITIONER);
      player.getInventory().addItem(item);
      return true;
    }

    // undo
    if (args[0].equalsIgnoreCase("undo")) {
      if (!sender.hasPermission("cherry.wand.undo")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      int n = 1; // undo count

      // 옵션 사용시
      boolean silent = false;
      if (args.length >= 2) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-player:::console") || str.equalsIgnoreCase("-p:::console")) {
            if (sender.hasPermission("cherry.wand.undo.another")) {
              wand = Wand.getWand(Cherry.uuid);
            }
            else {
              Msg.error(sender, Msg.NO_PERMISSION);
              return true;
            }
          }
          if (Pattern.compile("-player:([a-zA-Z0-9_]{3,20})", Pattern.CASE_INSENSITIVE).matcher(str).matches() || Pattern.compile("-p:([a-zA-Z0-9_]{3,20})", Pattern.CASE_INSENSITIVE).matcher(str).matches()) {
            if (sender.hasPermission("cherry.wand.undo.another")) {
              org.bukkit.entity.Player thisPlayer = Bukkit.getPlayer(str.replaceAll("-player:", "").replaceAll("-p:", ""));
              if (thisPlayer == null) {
                Msg.error(sender, Msg.NO_PLAYER);
                return true;
              }
              wand = Wand.getWand(thisPlayer);
            }
            else {
              Msg.error(sender, Msg.NO_PERMISSION);
              return true;
            }
          }
          if (Pattern.compile("-uuid:([0-9]{8}-[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{12})", Pattern.CASE_INSENSITIVE).matcher(str).matches() || Pattern.compile("-u:([0-9]{8}-[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{12})", Pattern.CASE_INSENSITIVE).matcher(str).matches()) {
            if (sender.hasPermission("cherry.wand.undo.another")) {
              UUID uuidTemp = UUID.fromString(str.replaceAll("-uuid:", "").replaceAll("-u:", ""));
              org.bukkit.entity.Player thisPlayer = (org.bukkit.entity.Player) Bukkit.getOfflinePlayer(uuidTemp);
              if (thisPlayer == null) {
                Msg.error(sender, Msg.NO_PLAYER);
                return true;
              }
              wand = Wand.getWand(thisPlayer);
            }
            else {
              Msg.error(sender, Msg.NO_PERMISSION);
              return true;
            }
          }
          if (Pattern.compile("-n:([0-9]{1,3})", Pattern.CASE_INSENSITIVE).matcher(str).matches()) {
            if (sender.hasPermission("cherry.wand.undo.multiple")) {
              Msg.error(sender, Msg.NO_PERMISSION);
              return true;
            }
            n = Integer.parseInt(str.replaceAll("-n:", ""));
          }
          if (str.equalsIgnoreCase("-silent")) {
            silent = true;
          }
        }
      }

      for (int i = 0; i < n; i++) {
        if (wand.undo()) {
          if (!silent) {
            Msg.info(sender, Msg.Prefix.WAND + "이전으로 되돌렸습니다.");
          }
        }
        else {
          if (!silent) {
            Msg.warn(sender, Msg.Prefix.WAND + "이전으로 되돌릴 수 없습니다.");
          }
          return true;
        }
      }

      return true;
    }

    // redo
    if (args[0].equalsIgnoreCase("redo")) {
      if (!sender.hasPermission("cherry.wand.redo")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      int n = 1; // undo count

      // 옵션 사용시
      boolean silent = false;
      if (args.length >= 2) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-player:::console") || str.equalsIgnoreCase("-p:::console")) {
            if (sender.hasPermission("cherry.wand.redo.another")) {
              wand = Wand.getWand(Cherry.uuid);
            }
            else {
              Msg.error(sender, Msg.NO_PERMISSION);
              return true;
            }
          }
          if (Pattern.compile("-player:([a-zA-Z0-9_]{3,20})", Pattern.CASE_INSENSITIVE).matcher(str).matches() || Pattern.compile("-p:([a-zA-Z0-9_]{3,20})", Pattern.CASE_INSENSITIVE).matcher(str).matches()) {
            if (sender.hasPermission("cherry.wand.redo.another")) {
              org.bukkit.entity.Player thisPlayer = Bukkit.getPlayer(str.replaceAll("-player:", "").replaceAll("-p:", ""));
              if (thisPlayer == null) {
                Msg.error(sender, Msg.NO_PLAYER);
                return true;
              }
              wand = Wand.getWand(thisPlayer);
            }
            else {
              Msg.error(sender, Msg.NO_PERMISSION);
              return true;
            }
          }
          if (Pattern.compile("-uuid:([0-9]{8}-[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{12})", Pattern.CASE_INSENSITIVE).matcher(str).matches() || Pattern.compile("-u:([0-9]{8}-[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{12})", Pattern.CASE_INSENSITIVE).matcher(str).matches()) {
            if (sender.hasPermission("cherry.wand.redo.another")) {
              UUID uuidTemp = UUID.fromString(str.replaceAll("-uuid:", "").replaceAll("-u:", ""));
              org.bukkit.entity.Player thisPlayer = (org.bukkit.entity.Player) Bukkit.getOfflinePlayer(uuidTemp);
              if (thisPlayer == null) {
                Msg.error(sender, Msg.NO_PLAYER);
                return true;
              }
              wand = Wand.getWand(thisPlayer);
            }
            else {
              Msg.error(sender, Msg.NO_PERMISSION);
              return true;
            }
          }
          if (Pattern.compile("-n:([0-9]{1,3})", Pattern.CASE_INSENSITIVE).matcher(str).matches()) {
            if (sender.hasPermission("cherry.wand.redo.multiple")) {
              Msg.error(sender, Msg.NO_PERMISSION);
              return true;
            }
            n = Integer.parseInt(str.replaceAll("-n:", ""));
          }
          if (str.equalsIgnoreCase("-silent")) {
            silent = true;
          }
        }
      }

      for (int i = 0; i < n; i++) {
        if (wand.redo()) {
          if (!silent) {
            Msg.info(sender, Msg.Prefix.WAND + "되돌리기를 취소하였습니다.");
          }
        }
        else {
          if (!silent) {
            Msg.warn(sender, Msg.Prefix.WAND + "되돌리기를 취소할 수 없습니다.");
          }
          return true;
        }
      }
      return true;
    }

    // 포지션 설정
    if (args[0].equalsIgnoreCase("pos1")) {
      if (!sender.hasPermission("cherry.wand.edit.pos")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      Location loc = null;

      boolean silent = false;

      if (args.length >= 5) {
        if (sender.hasPermission("cherry.wand.edit.pos.loc")) {
          World world = Bukkit.getWorld(args[4]);
          if (world == null) {
            Msg.error(sender, "월드를 찾을 수 없습니다.");
            return true;
          }
          loc = new Location(world, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }
        else {
          Msg.error(sender, Msg.NO_PERMISSION);
          return true;
        }
      }
      else if (args.length >= 4) {
        if (sender.hasPermission("cherry.wand.edit.pos.loc")) {
          if (player == null) {
            Msg.error(sender, Msg.Player.ONLY);
            return true;
          }
          World world = player.getWorld();
          try {
            loc = new Location(world, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
          }
          catch (Exception e) {
            Msg.error(sender, "올바른 좌표가 아닙니다.");
            return true;
          }
        }
        else {
          Msg.error(sender, Msg.NO_PERMISSION);
          return true;
        }
      }
      else {
        if (player == null) {
          Msg.error(sender, Msg.Player.ONLY);
          return true;
        }
        loc = player.getLocation().getBlock().getLocation();
      }

      if (args.length > 5) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-silent")) {
            silent = true;
          }
        }
      }

      wand.getEdit().setPosition(1, loc);

      if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
        if (!silent) {
          Msg.info(sender, Msg.Prefix.WAND + Msg.effect("첫번째 포지션이 설정되었습니다. (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r) (" + "&6" + Area.CUBE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)).size() + "&r블록)"));
        }
        wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)));
        wand.showParticleArea();
      }
      else {
        if (!silent) {
          Msg.info(sender, Msg.Prefix.WAND + Msg.effect("첫번째 포지션이 설정되었습니다. (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r)"));
        }
        wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(1)));
        wand.showParticleArea();
      }

      return true;
    }

    if (args[0].equalsIgnoreCase("pos2")) {
      if (!sender.hasPermission("cherry.wand.edit.pos")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }


      Location loc = null;

      boolean silent = false;

      if (args.length >= 5) {
        if (sender.hasPermission("cherry.wand.edit.pos.loc")) {
          World world = Bukkit.getWorld(args[4]);
          if (world == null) {
            Msg.error(sender, "없는 월드입니다.");
            return true;
          }
          loc = new Location(world, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }
        else {
          Msg.error(sender, Msg.NO_PERMISSION);
          return true;
        }
      }
      else if (args.length >= 4) {
        if (sender.hasPermission("cherry.wand.edit.pos.loc")) {
          if (player == null) {
            Msg.error(sender, Msg.Player.ONLY);
            return true;
          }
          World world = player.getWorld();
          try {
            loc = new Location(world, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
          }
          catch (Exception e) {
            Msg.error(sender, "올바른 좌표가 아닙니다.");
            return true;
          }
        }
        else {
          Msg.error(sender, Msg.NO_PERMISSION);
          return true;
        }
      }
      else {
        if (player == null) {
          Msg.error(sender, Msg.Player.ONLY);
          return true;
        }
        loc = player.getLocation().getBlock().getLocation();
      }

      if (args.length > 5) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-silent")) {
            silent = true;
          }
        }
      }

      wand.getEdit().setPosition(2, loc);

      if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
        if (!silent) {
          Msg.info(sender, Msg.Prefix.WAND + Msg.effect("두번째 포지션이 설정되었습니다. (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r) (" + "&6" + Area.CUBE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)).size() + "&r블록)"));
        }
        wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)));
        wand.showParticleArea();
      }
      else {
        if (!silent) {
          Msg.info(sender, Msg.Prefix.WAND + Msg.effect("두번째 포지션이 설정되었습니다. (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r)"));
        }
        wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(2), wand.getEdit().getPosition(2)));
        wand.showParticleArea();
      }
      return true;
    }

    /*
     * 잘라내기 / 복사 / 붙여넣기 / 클립보드 회전
     */
    if (args[0].equalsIgnoreCase("cut")) {
      if (!sender.hasPermission("cherry.wand.edit.cut")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      if (args.length < 5 && player == null) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, Msg.Prefix.WAND + label + " cut Location:<x> <y> <z> <world>");
        return true;
      }


      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }

      Location loc;
      if (args.length == 4) {
        if (Tool.Check.isInteger(args[1]) && Tool.Check.isInteger(args[2]) && Tool.Check.isInteger(args[3])) {
          loc = new Location(player.getLocation().getWorld(), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }
        else {
          Msg.error("올바른 좌표가 아닙니다.");
          return true;
        }
      }
      else if (args.length == 5) {
        World world = Bukkit.getWorld(args[4]);
        if (Tool.Check.isInteger(args[1]) && Tool.Check.isInteger(args[2]) && Tool.Check.isInteger(args[3]) && world != null) {
          loc = new Location(world, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }
        else {
          Msg.error("올바른 좌표가 아닙니다.");
          return true;
        }
      }
      else {
        loc = player.getLocation().getBlock().getLocation();
      }

      wand.copy(pos1, pos2, loc);

      List<Location> area = Area.CUBE.getArea(pos1, pos2);
      wand.storeUndo(area);
      wand.fill(Material.AIR, area, false);
      Msg.info(sender, Msg.Prefix.WAND + Msg.effect("지정 영역을 잘라내었습니다. (&6" + wand.getClipboardMemory().size() + "&r블록)"));
      return true;
    }

    // ww copy -loc:[0,0,0,world name] -s -ba -b[air,stone]
    if (args[0].equalsIgnoreCase("copy")) {
      if (!sender.hasPermission("cherry.wand.edit.copy")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      if (args.length < 5 && player == null) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, Msg.Prefix.WAND + label + " copy Location:<x> <y> <z> <world>");
        return true;
      }


      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }

      Location loc;
      if (args.length == 4) {
        if (Tool.Check.isInteger(args[1]) && Tool.Check.isInteger(args[2]) && Tool.Check.isInteger(args[3])) {
          loc = new Location(player.getLocation().getWorld(), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }
        else {
          Msg.error("올바른 좌표가 아닙니다.");
          return true;
        }
      }
      else if (args.length == 5) {
        World world = Bukkit.getWorld(args[4]);
        if (Tool.Check.isInteger(args[1]) && Tool.Check.isInteger(args[2]) && Tool.Check.isInteger(args[3]) && world != null) {
          loc = new Location(world, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }
        else {
          Msg.error("올바른 좌표가 아닙니다.");
          return true;
        }
      }
      else {
        loc = player.getLocation().getBlock().getLocation();
      }

      wand.copy(pos1, pos2, loc);

      Msg.info(sender, Msg.Prefix.WAND + Msg.effect("지정 영역을 클립보드애 복사하였습니다. (&6" + wand.getClipboardMemory().size() + "&r블록)"));
      return true;
    }

    if (args[0].equalsIgnoreCase("paste")) {
      if (!sender.hasPermission("cherry.wand.edit.paste")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      if (args.length < 5 && player == null) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, Msg.Prefix.WAND + label + " paste Location:<x> <y> <z> <world>");
        return true;
      }


      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }

      Location loc;
      if (args.length == 4) {
        if (Tool.Check.isInteger(args[1]) && Tool.Check.isInteger(args[2]) && Tool.Check.isInteger(args[3])) {
          loc = new Location(player.getLocation().getWorld(), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }
        else {
          Msg.error("올바른 좌표가 아닙니다.");
          return true;
        }
      }
      else if (args.length == 5) {
        World world = Bukkit.getWorld(args[4]);
        if (Tool.Check.isInteger(args[1]) && Tool.Check.isInteger(args[2]) && Tool.Check.isInteger(args[3]) && world != null) {
          loc = new Location(world, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }
        else {
          Msg.error("올바른 좌표가 아닙니다.");
          return true;
        }
      }
      else {
        loc = player.getLocation().getBlock().getLocation();
      }

      List<WandBlock> wBlocks = wand.getClipboardMemory();
      List<Location> area = new ArrayList<>();

      Location cpb = wand.getClipboardWPlayerB();
      Location cp = wand.getClipboardWPlayer();

      for (WandBlock wBlock : wBlocks) {
        Location bLoc = wBlock.getLocation();
        int x, y, z;
        if (cpb.getX() == 1) {
          x = (int) (loc.getX() + bLoc.getX() + cp.getX());
        }
        else {
          x = (int) (loc.getX() + bLoc.getX() - cp.getX());
        }
        if (cpb.getY() == 1) {
          y = (int) (loc.getY() + bLoc.getY() + cp.getY());
        }
        else {
          y = (int) (loc.getY() + bLoc.getY() - cp.getY());
        }
        if (cpb.getZ() == 1) {
          z = (int) (loc.getZ() + bLoc.getZ() + cp.getZ());
        }
        else {
          z = (int) (loc.getZ() + bLoc.getZ() - cp.getZ());
        }
        Location location = new Location(loc.getWorld(), x, y, z);
        area.add(location);
      }

      wand.storeUndo(area);

      wand.paste(loc);
      Msg.info(sender, Msg.Prefix.WAND + Msg.effect("클립보드 데이터를 붙여넣었습니다. (&6" + wBlocks.size() + "&r블록)"));
      return true;
    }

    if (args[0].equalsIgnoreCase("rotate")) {
      if (!sender.hasPermission("cherry.wand.edit.copy")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      if (player == null) {
        Msg.error(sender, Msg.Player.ONLY);
        return true;
      }


      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }

      wand.copy(pos1, pos2, player.getLocation().getBlock().getLocation());

      Msg.info(sender, Msg.Prefix.WAND + Msg.effect("지정 영역을 클립보드애 복사하였습니다. (&6" + wand.getClipboardMemory().size() + "&r블록)"));
      return true;
    }

    /*
     * 쌓기
     */
    if (args[0].equalsIgnoreCase("stack")) {
      if (!sender.hasPermission("cherry.wand.edit.stack")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      if (args.length < 3 && player == null) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, Msg.Prefix.WAND + label + " stack repeat:<n> direction:<south|west|north|east>");
        return true;
      }


      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }

      int n = 1;
      if (args.length >= 2 && Tool.Check.isNatural(args[1])) {
        n = Integer.parseInt(args[1]);
      }

      String dir = "";
      if (args.length >= 3) {
        dir = args[2].toLowerCase();
      }
      else {
        Vector vector = player.getLocation().getDirection();

        double maxd = Tool.Math.max(new double[]{Math.abs(vector.getX()), Math.abs(vector.getY()), Math.abs(vector.getZ())});

        if (maxd == Math.abs(vector.getX())) {
          if (vector.getX() > 0) {
            dir = "east";
          }
          else {
            dir = "west";
          }
        }
        else if (maxd == Math.abs(vector.getY())) {
          if (vector.getY() > 0) {
            dir = "up";
          }
          else {
            dir = "down";
          }
        }
        else if (maxd == Math.abs(vector.getZ())) {
          if (vector.getZ() > 0) {
            dir = "south";
          }
          else {
            dir = "north";
          }
        }
        else {
          Msg.error("올바르지 않은 방향입니다. (e)");
          return true;
        }
      }


      int minX = (int) Math.min(pos1.getX(), pos2.getX());
      int minY = (int) Math.min(pos1.getY(), pos2.getY());
      int minZ = (int) Math.min(pos1.getZ(), pos2.getZ());
      int maxX = (int) Math.max(pos1.getX(), pos2.getX());
      int maxY = (int) Math.max(pos1.getY(), pos2.getY());
      int maxZ = (int) Math.max(pos1.getZ(), pos2.getZ());

      List<WandBlock> area = new ArrayList<>();
      List<Location> locArea = Area.CUBE.getArea(pos1, pos2);
      for (Location loc : locArea) {
        Block block = loc.getBlock();
        WandBlock wBlock = new WandBlock(block);
        wBlock.setLocation(new Location(loc.getWorld(), (int) loc.getX(), (int) loc.getY(), (int) loc.getZ()));
        area.add(wBlock);
      }
      World world = pos1.getWorld();

      List<Location> undoArea = new ArrayList<>();
      for (int m = 1; m <= n; m++) {
        for (WandBlock wandBlock : area) {
          Location loc = wandBlock.getLocation();
          Location newLoc;
          switch (dir) {
            case "east":
              newLoc = new Location(world, loc.getX() + (m * ((maxX + 1) - minX)), loc.getY(), loc.getZ());
              break;
            case "west":
              newLoc = new Location(world, loc.getX() - (m * ((maxX + 1) - minX)), loc.getY(), loc.getZ());
              break;
            case "south":
              newLoc = new Location(world, loc.getX(), loc.getY(), loc.getZ() + (m * ((maxZ + 1) - minZ)));
              break;
            case "north":
              newLoc = new Location(world, loc.getX(), loc.getY(), loc.getZ() - (m * ((maxZ + 1) - minZ)));
              break;
            case "up":
              newLoc = new Location(world, loc.getX(), loc.getY() + (m * ((maxY + 1) - minY)), loc.getZ());
              break;
            case "down":
              newLoc = new Location(world, loc.getX(), loc.getY() - (m * ((maxY + 1) - minY)), loc.getZ());
              break;
            default:
              Msg.error("올바르지 않은 방향입니다.");
              return true;
          }
          undoArea.add(newLoc);
        }
      }

      wand.storeUndo(undoArea);

      wand.stack(pos1, pos2, dir, n, false);

      String dirKo = "";
      switch (dir) {
        case "east":
          dirKo = "동쪽";
          break;
        case "west":
          dirKo = "서쪽";
          break;
        case "south":
          dirKo = "남쪽";
          break;
        case "north":
          dirKo = "북쪽";
          break;
      }
      Msg.info(sender, Msg.Prefix.WAND + Msg.effect("지정 영역을 " + dirKo + "으로 " + n + "회 붙여넣었습니다. (" + undoArea.size() + "블록)"));
      return true;
    }

    /*
     * 교체
     * wand replace material material
     */
    if (args[0].equalsIgnoreCase("replace")) {
      if (!sender.hasPermission("cherry.wand.edit.replace")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }

      if (args.length < 3) {
        Msg.error(sender, Msg.NO_ARGS);
        return true;
      }

      args[1] = args[1].toUpperCase();
      args[2] = args[2].toUpperCase();

      if (!(Tool.Check.isMaterial(args[1]) && Tool.Check.isMaterial(args[2]))) {
        Msg.error(sender, "올바른 물질이 아닙니다.");
        return true;
      }
      if (!(Material.getMaterial(args[1]).isBlock() && Material.getMaterial(args[2]).isBlock())) {
        Msg.error(sender, "올바른 블록이 아닙니다.");
        return true;
      }

      BlockData originalBlockData = Bukkit.createBlockData(Material.getMaterial(args[1]));
      BlockData blockDataReplace = Bukkit.createBlockData(Material.getMaterial(args[2]));

      List<Location> area = Area.CUBE.getArea(pos1, pos2);

      List<Location> undoArea = new ArrayList<>();
      World world = area.get(0).getWorld();
      for (Location pos : area) {
        Block block = world.getBlockAt(pos);
        if (originalBlockData.getAsString().equals("[]") || originalBlockData.getAsString().equals("")) {
          if (block.getType().equals(originalBlockData.getMaterial())) {
            undoArea.add(pos);
          }
        }
        else {
          if (block.getBlockData().equals(originalBlockData)) {
            undoArea.add(pos);
          }
        }
      }

      wand.storeUndo(undoArea);

      wand.replace(originalBlockData, blockDataReplace, area, false);

      String blockNameOriginal = BlockNames.valueOf(originalBlockData.getMaterial().toString()).getName();
      String blockNameReplace = BlockNames.valueOf(blockDataReplace.getMaterial().toString()).getName();
      Msg.info(sender, Msg.Prefix.WAND + Msg.effect("지정 영역의 " + blockNameOriginal + Msg.getJosa(blockNameOriginal, "을", "를") + " " + blockNameReplace + Msg.getJosa(blockNameReplace, "으로", "로") + " 바꾸었습니다. (&6" + undoArea.size() + "&r블록)"));
      return true;
    }

    /*
     * 육면채
     * cube <material> [options...]
     */
    if (args[0].equalsIgnoreCase("cube")) {
      if (!sender.hasPermission("cherry.wand.edit.cube")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (args.length <= 1) {
        Msg.error(sender, Msg.NO_ARGS);
        return true;
      }


      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }

      // material
      Material material = Material.matchMaterial(args[1]);
      if (material == null) {
        Msg.error(sender, "적당한 블록 이름이 아닙니다");
        return true;
      }
      if (!material.isBlock()) {
        Msg.error(sender, "블록만 설치할 수 있습니다");
        return true;
      }

      // options
      boolean empty = false;
      boolean walled = false;
      boolean applyPhysics = false;
      boolean silent = false;
      String data = "[]";
      if (args.length > 2) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-empty") || str.equalsIgnoreCase("-e")) {
            empty = true;
            walled = false;
          }
          if (str.equalsIgnoreCase("-walled") || str.equalsIgnoreCase("-w")) {
            walled = true;
            empty = false;
          }
          if (str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if (Pattern.compile("-data:\\[([^\\]]*)\\]", Pattern.CASE_INSENSITIVE).matcher(str).matches()) {
            Matcher matcher = Pattern.compile("-data:\\[([^\\]]*)\\]", Pattern.CASE_INSENSITIVE).matcher(str);
            if (matcher.find()) {
              data = "[" + matcher.group(1) + "]";
            }
          }
          if (str.equalsIgnoreCase("-silent")) {
            silent = true;
          }
        }
      }

      List<Location> area = null;
      if (empty) {
        area = Area.CUBE_EMPTY.getArea(pos1, pos2);
      }
      else if (walled) {
        area = Area.CUBE_WALL.getArea(pos1, pos2);
      }
      else {
        area = Area.CUBE.getArea(pos1, pos2);
      }

      BlockData blockData = null;
      try {
        blockData = Bukkit.createBlockData(material, data);
      }
      catch (Exception ex) {
        Msg.error(player, "BlockData parse error: " + ex);
        return true;
      }

      wand.storeUndo(area);

      String blockName = BlockNames.valueOf(material.toString()).getName();
      if (!silent) {
        Msg.info(sender, Msg.Prefix.WAND + Msg.effect("&6" + blockName + "&r" + Msg.getJosa(blockName, "을", "를") + " &6" + area.size() + "&r개 설치하였습니다."));
      }
      if (!data.equals("[]")) {
        if (!silent) {
          Msg.info(sender, Msg.Prefix.WAND + Msg.effect("BlockData: " + data));
        }
      }

      wand.fill(blockData, area, applyPhysics);
      return true;
    }

    /*
     * 윈기둥
     * cyl <material> <radius> <height> [options...]
     */
    if (args[0].equalsIgnoreCase("cyl")) {
      if (!sender.hasPermission("cherry.wand.edit.cyl")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (args.length <= 2) {
        Msg.error(sender, Msg.NO_ARGS);
        return true;
      }


      Location pos = wand.getEdit().getPosition(1);
      if (pos == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다");
        return true;
      }

      // material
      Material material = Material.matchMaterial(args[1]);
      if (material == null) {
        Msg.error(sender, "적당한 블록 이름이 아닙니다");
        return true;
      }
      if (!material.isBlock()) {
        Msg.error(sender, "블록만 설치할 수 있습니다");
        return true;
      }

      // radius
      if (!Tool.Check.isInteger(args[2])) {
        Msg.error(sender, "반지름은 정수만 지정할 수 있습니다");
        return true;
      }
      int radius = Integer.parseInt(args[2]);

      // height
      int height = 1;
      if (args.length > 3) {
        if (!Tool.Check.isInteger(args[3])) {
          Msg.error(sender, "높이는 정수만 지정할 수 있습니다");
          return true;
        }
        height = Integer.parseInt(args[3]);
      }

      // options
      boolean empty = false;
      boolean pointed = false;
      boolean applyPhysics = false;
      boolean silent = false;
      if (args.length > 4) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-empty") || str.equalsIgnoreCase("-e")) {
            empty = true;
          }
          if (str.equalsIgnoreCase("-pointed") || str.equalsIgnoreCase("-p")) {
            pointed = true;
          }
          if (str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if (str.equalsIgnoreCase("-silent")) {
            silent = true;
          }
        }
      }

      List<Location> area = null;
      if (empty) {
        if (pointed) {
          area = Area.CYLINDER_POINTED_EMPTY.getArea(pos, radius, height);
        }
        else {
          area = Area.CYLINDER_EMPTY.getArea(pos, radius, height);
        }
      }
      else {
        if (pointed) {
          area = Area.CYLINDER_POINTED.getArea(pos, radius, height);
        }
        else {
          area = Area.CYLINDER.getArea(pos, radius, height);
        }
      }

      if (area == null) {
        Msg.error(sender, "영역 설정이 완료되지 않았습니다");
        return true;
      }

      wand.storeUndo(area);

      String blockName = BlockNames.valueOf(material.toString()).getName();
      Msg.info(sender, Msg.Prefix.WAND + Msg.effect("&6" + blockName + "&r" + Msg.getJosa(blockName, "을", "를") + " &6" + area.size() + "&r개 설치하였습니다."));

      wand.fill(material, area, applyPhysics);
      return true;
    }

    /*
     * 구
     * sphere <material> <radius> [options...]
     */
    if (args[0].equalsIgnoreCase("sphere")) {
      if (!sender.hasPermission("cherry.wand.edit.sphere")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (args.length <= 2) {
        Msg.error(sender, Msg.NO_ARGS);
        return true;
      }


      Location pos = wand.getEdit().getPosition(1);
      if (pos == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다");
        return true;
      }

      // material
      Material material = Material.matchMaterial(args[1]);
      if (material == null) {
        Msg.error(sender, "적당한 블록 이름이 아닙니다");
        return true;
      }
      if (!material.isBlock()) {
        Msg.error(sender, "블록만 설치할 수 있습니다");
        return true;
      }

      // radius
      if (!Tool.Check.isInteger(args[2])) {
        Msg.error(sender, "반지름은 정수만 지정할 수 있습니다");
        return true;
      }
      int radius = Integer.parseInt(args[2]);

      // options
      boolean empty = false;
      boolean pointed = false;
      boolean applyPhysics = false;
      boolean silent = false;
      if (args.length > 3) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-empty") || str.equalsIgnoreCase("-e")) {
            empty = true;
          }
          if (str.equalsIgnoreCase("-pointed") || str.equalsIgnoreCase("-p")) {
            pointed = true;
          }
          if (str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if (str.equalsIgnoreCase("-silent")) {
            silent = true;
          }
        }
      }

      List<Location> area = null;
      if (empty) {
        if (pointed) {
          area = Area.SPHERE_POINTED_EMPTY.getArea(pos, radius);
        }
        else {
          area = Area.SPHERE_EMPTY.getArea(pos, radius);
        }
      }
      else {
        if (pointed) {
          area = Area.SPHERE_POINTED.getArea(pos, radius);
        }
        else {
          area = Area.SPHERE.getArea(pos, radius);
        }
      }

      if (area == null) {
        Msg.error(sender, "영역 설정이 완료되지 않았습니다");
        return true;
      }

      wand.storeUndo(area);

      String blockName = BlockNames.valueOf(material.toString()).getName();
      if (!silent) {
        Msg.info(sender, Msg.Prefix.WAND + Msg.effect("&6" + blockName + "&r" + Msg.getJosa(blockName, "을", "를") + " &6" + area.size() + "&r개 설치하였습니다."));
      }

      wand.fill(material, area, applyPhysics);
      return true;
    }

    /*
     * 벽
     * wall <material> [option...]
     */
    if (args[0].equalsIgnoreCase("wall")) {
      if (!sender.hasPermission("cherry.wand.edit.wall")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (args.length <= 1) {
        Msg.error(sender, Msg.NO_ARGS);
        return true;
      }


      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }

      // material
      Material material = Material.matchMaterial(args[1]);
      if (material == null) {
        Msg.error(sender, "적당한 블록 이름이 아닙니다");
        return true;
      }
      if (!material.isBlock()) {
        Msg.error(sender, "블록만 설치할 수 있습니다");
        return true;
      }

      // options
      boolean empty = false;
      boolean walled = false;
      boolean applyPhysics = false;
      boolean silent = false;
      if (args.length > 2) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-empty") || str.equalsIgnoreCase("-e")) {
            empty = true;
            walled = false;
          }
          if (str.equalsIgnoreCase("-walled") || str.equalsIgnoreCase("-w")) {
            walled = true;
            empty = false;
          }
          if (str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if (str.equalsIgnoreCase("-silent")) {
            silent = true;
          }
        }
      }

      List<Location> area = Area.WALL.getArea(pos1, pos2);

      wand.storeUndo(area);

      String blockName = BlockNames.valueOf(material.toString()).getName();
      if (!silent) {
        Msg.info(sender, Msg.Prefix.WAND + Msg.effect("&6" + blockName + "&r" + Msg.getJosa(blockName, "을", "를") + " &6" + area.size() + "&r개 설치하였습니다."));
      }

      wand.fill(material, area, applyPhysics);
      return true;
    }

    if (args[0].equalsIgnoreCase("cmdscan")) {
      if (!sender.hasPermission("cherry.wand.cmdscan")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      if (args.length <= 1) {
        Msg.error(sender, Msg.NO_ARGS);
        return true;
      }

      if (!Tool.Check.isInteger(args[1])) {
        Msg.error(sender, "반지름은 정수만 지정할 수 있습니다");
        return true;
      }
      int radius = Integer.parseInt(args[1]);

      Location loc = player.getLocation().getBlock().getLocation();

      List<Location> area = Area.SPHERE.getArea(loc, radius);


      List<WandBlock> wbs = new ArrayList<>();
      wbs.addAll(wand.scan(area, Bukkit.createBlockData(Material.COMMAND_BLOCK)));
      wbs.addAll(wand.scan(area, Bukkit.createBlockData(Material.CHAIN_COMMAND_BLOCK)));
      wbs.addAll(wand.scan(area, Bukkit.createBlockData(Material.REPEATING_COMMAND_BLOCK)));

      for (WandBlock wb : wbs) {
        if (wb.getBlockData().getMaterial().equals(Material.COMMAND_BLOCK)) {
          Msg.info(sender, Msg.effect("&6" + Tool.loc2Str(wb.getLocation()) + "&7: &e" + wb.getCommand()));
        }
        else if (wb.getBlockData().getMaterial().equals(Material.CHAIN_COMMAND_BLOCK)) {
          Msg.info(sender, Msg.effect("&3" + Tool.loc2Str(wb.getLocation()) + "&7: &b" + wb.getCommand()));
        }
        else if (wb.getBlockData().getMaterial().equals(Material.REPEATING_COMMAND_BLOCK)) {
          Msg.info(sender, Msg.effect("&5" + Tool.loc2Str(wb.getLocation()) + "&7: &d" + wb.getCommand()));
        }
      }

      return true;
    }

    Msg.error(sender, Msg.UNKNOWN);
    return true;
  }

}
