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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WandCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

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

    args[0] = args[0].toLowerCase();

    // 완드 아이템 얻기 (에딧)
    if (args[0].equals("get")) {
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

    // 포지션 설정
    if (args[0].equals("pos1")) {
      if (!sender.hasPermission("cherry.wand.edit.pos")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      Location loc = null;

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

      boolean silent = false;
      if (args.length > 5) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
            break;
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

    if (args[0].equals("pos2")) {
      if (!sender.hasPermission("cherry.wand.edit.pos")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      Location loc = null;

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

      boolean silent = false;
      if (args.length > 5) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
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

    // undo
    if (args[0].equals("undo")) {
      if (!sender.hasPermission("cherry.wand.undo")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      // repeat
      int repeat = 1; // undo count
      if (args.length >= 2) {
        try {
          repeat = Integer.parseInt(args[1]);
        } catch (Exception e) {
          if (Tool.Check.isInteger(args[1])) {
            Msg.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다.");
          }
          else {
            Msg.error(sender, "반복 횟수 값은 정수만 입력할 수 있습니다.");
          }
          return true;
        }
        if (repeat < 1) {
          Msg.error(sender, "최소 반복 횟수 값은 1 입니다.");
          return true;
        }
      }

      Wand target = wand;
      if (args.length >= 3) {
        String targetString = args[2];
        if (targetString.equals("@s")) {

        }
        else {
          try {
            Player p = Bukkit.getPlayer(UUID.fromString(targetString));
            if (p == null) {
              Msg.error(sender, "플레이어를 찾을 수 없습니다.");
              return true;
            }
            target = Wand.getWand(p);
          } catch (Exception e) {
            Player p = Bukkit.getPlayer(targetString);
            if (p == null) {
              Msg.error(sender, "플레이어를 찾을 수 없습니다.");
              return true;
            }
            target = Wand.getWand(p);
          }
        }
      }

      // options
      boolean applyPhysics = false;
      boolean silent = false;
      if (args.length > 3) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if (str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
          }
        }
      }

      for (int i = 0; i < repeat; i++) {
        if (target.undo(applyPhysics)) {
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
    if (args[0].equals("redo")) {
      if (!sender.hasPermission("cherry.wand.redo")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }

      // repeat
      int repeat = 1; // undo count
      if (args.length >= 2) {
        try {
          repeat = Integer.parseInt(args[1]);
        } catch (Exception e) {
          if (Tool.Check.isInteger(args[1])) {
            Msg.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다.");
          }
          else {
            Msg.error(sender, "반복 횟수 값은 정수만 입력할 수 있습니다.");
          }
          return true;
        }
        if (repeat < 1) {
          Msg.error(sender, "최소 반복 횟수 값은 1 입니다.");
          return true;
        }
      }

      Wand target = wand;
      if (args.length >= 3) {
        String targetString = args[2];
        if (targetString.equals("@s")) {

        }
        else {
          try {
            Player p = Bukkit.getPlayer(UUID.fromString(targetString));
            if (p == null) {
              Msg.error(sender, "플레이어를 찾을 수 없습니다.");
              return true;
            }
            target = Wand.getWand(p);
          } catch (Exception e) {
            Player p = Bukkit.getPlayer(targetString);
            if (p == null) {
              Msg.error(sender, "플레이어를 찾을 수 없습니다.");
              return true;
            }
            target = Wand.getWand(p);
          }
        }
      }

      // options
      boolean applyPhysics = false;
      boolean silent = false;
      if (args.length > 3) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if (str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
          }
        }
      }

      for (int i = 0; i < repeat; i++) {
        if (target.redo(applyPhysics)) {
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

    /*
     * 잘라내기 / 복사 / 붙여넣기 / 클립보드 회전
     */

    if (args[0].equals("copy")) {
      if (!sender.hasPermission("cherry.wand.copy")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (args.length < 5 && player == null) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, "사용법: " + label + " " + args[0] + " <X> <Y> <Z> <World> [옵션...]");
        return true;
      }

      // area
      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }
      List<Location> area = Area.CUBE.getArea(pos1, pos2);

      // location
      Location loc = null;
      int x;
      int y;
      int z;
      World world;
      if (args.length >= 4) {
        try {
          x = verifyInteger("X", 30000000, -30000000, args[1]);
          y = verifyInteger("Y", 256, 0, args[2]);
          z = verifyInteger("Z", 30000000, -30000000, args[3]);
        } catch (Exception e) {
          Msg.error(e.getMessage());
          return true;
        }
        if (args.length == 4) {
          loc = new Location(player.getWorld(), x, y, z);
        }
        if (args.length >= 5) {
          world = Bukkit.getWorld(args[4]);
          if (world != null) {
            loc = new Location(world, x, y, z);
          }
          else {
            Msg.error("월드를 찾을 수 없습니다.");
            return true;
          }
        }
      }
      else {
        loc = player.getLocation().getBlock().getLocation();
      }

      // options
      boolean silent = false;
      int commandArgsLength = 5;
      if (args.length > commandArgsLength) {
        for (String str : args) {
          if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
            break;
          }
        }
      }

      wand.copy(area, loc, null);

      if (!silent) {
        Msg.info(sender, Msg.Prefix.WAND + Msg.effect("지정 영역을 클립보드에 복사하였습니다. (&6" + wand.clipboardMemory.size() + "&r블록)"));
      }

      return true;
    }

    if (args[0].equals("cut")) {
      if (!sender.hasPermission("cherry.wand.cut")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (args.length < 5 && player == null) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, "사용법: " + label + " " + args[0] + " <X> <Y> <Z> <World> [옵션...]");
        return true;
      }

      // area
      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }
      List<Location> area = Area.CUBE.getArea(pos1, pos2);

      // location
      Location loc = null;
      int x;
      int y;
      int z;
      World world;
      if (args.length >= 4) {
        try {
          x = verifyInteger("X", 30000000, -30000000, args[1]);
          y = verifyInteger("Y", 256, 0, args[2]);
          z = verifyInteger("Z", 30000000, -30000000, args[3]);
        } catch (Exception e) {
          Msg.error(e.getMessage());
          return true;
        }
        if (args.length == 4) {
          loc = new Location(player.getWorld(), x, y, z);
        }
        if (args.length >= 5) {
          world = Bukkit.getWorld(args[4]);
          if (world != null) {
            loc = new Location(world, x, y, z);
          }
          else {
            Msg.error("월드를 찾을 수 없습니다.");
            return true;
          }
        }
      }
      else {
        loc = player.getLocation().getBlock().getLocation();
      }

      // options
      boolean applyPhysics = false;
      boolean silent = false;
      int commandArgsLength = 2;
      if (args.length > commandArgsLength) {
        for (String str : args) {
          if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
          }
        }
      }

      wand.remove(area, applyPhysics);

      wand.copy(area, loc, null);

      if (!silent) {
        Msg.info(sender, Msg.Prefix.WAND + Msg.effect("지정 영역을 클립보드애 복사하였습니다. (&6" + wand.clipboardMemory.size() + "&r블록)"));
      }

      return true;
    }

    if (args[0].equals("paste")) {
      if (!sender.hasPermission("cherry.wand.paste")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (wand.clipboardMemory == null) {
        Msg.error(sender, "클립보드에 저장된 데이터가 없습니다.");
        return true;
      }
      if (args.length < 5 && player == null) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, "사용법: " + label + " " + args[0] + " <X> <Y> <Z> <World> [옵션...]");
        return true;
      }

      // area
      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }

      // location
      Location posP = null;
      int lx;
      int ly;
      int lz;
      World world;
      if (args.length >= 4) {
        try {
          lx = verifyInteger("X", 30000000, -30000000, args[1]);
          ly = verifyInteger("Y", 256, 0, args[2]);
          lz = verifyInteger("Z", 30000000, -30000000, args[3]);
        } catch (Exception e) {
          Msg.error(e.getMessage());
          return true;
        }
        if (args.length == 4) {
          posP = new Location(player.getWorld(), lx, ly, lz);
        }
        if (args.length >= 5) {
          world = Bukkit.getWorld(args[4]);
          if (world != null) {
            posP = new Location(world, lx, ly, lz);
          }
          else {
            Msg.error("월드를 찾을 수 없습니다.");
            return true;
          }
        }
      }
      else {
        posP = player.getLocation().getBlock().getLocation();
      }

      // options
      boolean applyPhysics = false;
      boolean silent = false;
      List<Material> blackList = new ArrayList<>();
      int commandArgsLength = 5;
      int optionsCount = 5;
      if (commandArgsLength < args.length && args.length <= commandArgsLength + optionsCount) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if (str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
          }
          if (str.equalsIgnoreCase("-remove-air")) {
            blackList.add(Material.AIR);
          }
          if (str.equalsIgnoreCase("-remove-water")) {
            blackList.add(Material.WATER);
          }
          if (str.equalsIgnoreCase("-remove-lava")) {
            blackList.add(Material.LAVA);
          }
        }
      }

      List<Location> area = new ArrayList<>();

      for (WandBlock wandBlock : wand.clipboardMemory) {
        Location loc = new Location(
          posP.getWorld(),
          (int) wandBlock.getLocation().getX() + posP.getX(),
          (int) wandBlock.getLocation().getY() + posP.getY(),
          (int) wandBlock.getLocation().getZ() + posP.getZ()
        );
        area.add(loc);
      }

      wand.storeUndo(area);

      wand.paste(posP, blackList, applyPhysics);

      if (!silent) {
        Msg.info(sender, Msg.Prefix.WAND + Msg.effect("클립보드 데이터를 붙여넣었습니다. (&6" + area.size() + "&r블록)"));
      }

      return true;
    }

    if (args[0].equals("rotate")) {
      if (!sender.hasPermission("cherry.wand.paste")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (wand.clipboardMemory == null) {
        Msg.error(sender, "클립보드에 저장된 데이터가 없습니다.");
        return true;
      }
      if (args.length < 2) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, "사용법: " + label + " " + args[0] + " <right|left> [옵션...]");
        return true;
      }

      String dir = args[1];

      // options
      boolean silent = false;
      int commandArgsLength = 2;
      if (args.length > commandArgsLength) {
        for (String str : args) {
          if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
            break;
          }
        }
      }

      switch (dir) {
        case "right": {
          wand.multiply(-1, 1, 1);
          if (!silent) {
            Msg.info(sender, Msg.Prefix.WAND + Msg.effect("클립보드를 오른쪽으로 90 도 회전하였습니다."));
          }
          return true;
        }
        case "left": {
          wand.multiply(1, 1, -1);
          if (!silent) {
            Msg.info(sender, Msg.Prefix.WAND + Msg.effect("클립보드를 오른쪽으로 90 도 회전하였습니다."));
          }
          return true;
        }
        case "100": {
          wand.multiply(-1, 1, 1);
          return true;
        }
        case "010": {
          wand.multiply(1, -1, 1);
          return true;
        }
        case "001": {
          wand.multiply(1, 1, -1);
          return true;
        }
        case "110": {
          wand.multiply(-1, -1, 1);
          return true;
        }
        case "101": {
          wand.multiply(-1, 1, -1);
          return true;
        }
        case "011": {
          wand.multiply(1, -1, -1);
          return true;
        }
        case "111": {
          wand.multiply(-1, -1, -1);
          return true;
        }
        default: {
          Msg.error(sender, "알 수 없는 방향입니다.");
          return true;
        }
      }

    }

    if (args[0].equals("flip")) {
      if (!sender.hasPermission("cherry.wand.paste")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (wand.clipboardMemory == null) {
        Msg.error(sender, "클립보드에 저장된 데이터가 없습니다.");
        return true;
      }
      if (args.length < 2) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, "사용법: " + label + " " + args[0] + " <right|left> [옵션...]");
        return true;
      }

      String dir = args[1];

      // options
      boolean silent = false;
      int commandArgsLength = 2;
      if (args.length > commandArgsLength) {
        for (String str : args) {
          if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
            break;
          }
        }
      }

      wand.flip(dir);

      if (!silent) {
        Msg.info(sender, Msg.Prefix.WAND + Msg.effect("rotate: " + dir));
      }

      return true;
    }

    /*
     * 쌓기
     */
    if (args[0].equals("stack")) {

      if (!sender.hasPermission("cherry.wand.stack")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (args.length < 3 && player == null) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, "사용법: " + label + " " + args[0] + " <반복 횟수> [방향] [옵션...]");
        return true;
      }

      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }

      // repeat
      int repeat = 1;
      if (args.length >= 2) {
        try {
          repeat = Integer.parseInt(args[1]);
        } catch (Exception e) {
          if (Tool.Check.isInteger(args[1])) {
            Msg.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다.");
          }
          else {
            Msg.error(sender, "반복 횟수 값은 정수만 입력할 수 있습니다.");
          }
          return true;
        }
        if (repeat > 1000) {
          Msg.error(sender, "최대 반복 횟수 값은 1000 입니다.");
          return true;
        }
        if (repeat < 1) {
          Msg.error(sender, "최소 반복 횟수 값은 1 입니다.");
          return true;
        }
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

      // options
      boolean applyPhysics = false;
      boolean silent = false;
      if (args.length > 3) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if (str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
          }
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
      for (int m = 1; m <= repeat; m++) {
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

      if (!silent) {
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
        Msg.info(sender, Msg.Prefix.WAND + Msg.effect("지정 영역을 " + dirKo + "으로 " + repeat + "회 붙여넣었습니다. (" + undoArea.size() + "블록)"));
      }

      wand.stack(pos1, pos2, dir, repeat, applyPhysics);

      return true;
    }

    /*
     * 교체
     * wand replace material material
     */
    if (args[0].equals("replace")) {

      if (!sender.hasPermission("cherry.wand.replace")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (args.length < 3) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, "사용법: " + command.getLabel() + " " + args[0] + " <블록> <블록> [옵션...]");
        return true;
      }

      // area
      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }
      List<Location> area = Area.CUBE.getArea(pos1, pos2);

      // blockdata
      String blockDataString_original = args[1];
      BlockData blockData_original;
      try {
        blockData_original = Wand.getBlockData(blockDataString_original);
      } catch (Exception e) {
        Msg.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
        return true;
      }
      String dataString_original = Wand.getDataString(blockDataString_original);

      // blockdata
      String blockDataString_replace = args[2];
      BlockData blockData_replace;
      try {
        blockData_replace = Wand.getBlockData(blockDataString_replace);
      } catch (Exception e) {
        Msg.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
        return true;
      }

      // options
      boolean applyPhysics = false;
      boolean silent = false;
      if (args.length > 3) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if (str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
          }
        }
      }

      // filter area
      List<Location> filteredArea = wand.scan(area, blockData_original, !dataString_original.equals("[]") && !dataString_original.equals(""));

      if (filteredArea.size() == 0) {
        Msg.error(sender, "바꿀 블럭이 없습니다.");
        return true;
      }

      wand.storeUndo(filteredArea);

      if (!silent) {
        String blockNameOriginal = BlockNames.valueOf(blockData_original.getMaterial().toString()).getName();
        String blockNameReplace = BlockNames.valueOf(blockData_replace.getMaterial().toString()).getName();
        Msg.info(sender, Msg.Prefix.WAND + Msg.effect("지정 영역의 "
          + blockNameOriginal + Msg.getJosa(blockNameOriginal, "을", "를")
          + " " + blockNameReplace + Msg.getJosa(blockNameReplace, "으로", "로")
          + " 바꾸었습니다. (&6" + filteredArea.size() + "&r블록)"
        ));
      }

      wand.fill(blockData_replace, filteredArea, applyPhysics);

      return true;
    }

    /*
     * 주위만 교체
     * wand replace material material
     */
    if (args[0].equals("replacenear")) {

      if (!sender.hasPermission("cherry.wand.replacenear")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (player == null) {
        Msg.error(sender, Msg.Player.ONLY);
        return true;
      }
      if (args.length < 3) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, "사용법: " + command.getLabel() + " " + args[0] + " <블록> <블록> [반지름] [옵션...]");
        return true;
      }

      // area
      Location pos = player.getLocation().getBlock().getLocation();

      // blockdata
      String blockDataString_original = args[1];
      BlockData blockData_original;
      try {
        blockData_original = Wand.getBlockData(blockDataString_original);
      } catch (Exception e) {
        Msg.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
        return true;
      }
      String dataString_original = Wand.getDataString(blockDataString_original);


      // blockdata
      String blockDataString_replace = args[2];
      BlockData blockData_replace;
      try {
        blockData_replace = Wand.getBlockData(blockDataString_replace);
      } catch (Exception e) {
        Msg.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
        return true;
      }

      // radius
      int radius = wand.lastReplacenearRadius;
      if (args.length > 3) {
        try {
          radius = Integer.parseInt(args[3]);
        } catch (Exception e) {
          if (Tool.Check.isInteger(args[3])) {
            Msg.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다.");
          }
          else {
            Msg.error(sender, "반지름 값은 정수만 입력할 수 있습니다.");
          }
          return true;
        }
        if (radius > 100000) {
          Msg.error(sender, "최대 반지름 값은 100000 입니다.");
          return true;
        }
        if (radius < 1) {
          Msg.error(sender, "최소 반지름 값은 1 입니다.");
          return true;
        }
      }
      wand.lastReplacenearRadius = radius;
      List<Location> area = Area.CUBE.getArea(pos, radius);

      // options
      boolean applyPhysics = false;
      boolean silent = false;
      if (args.length > 4) {
        for (String str : args) {
          if (str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if (str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
          }
        }
      }

      // filter area
      List<Location> filteredArea = wand.scan(area, blockData_original, !dataString_original.equals("[]") && !dataString_original.equals(""));

      if (filteredArea.size() == 0) {
        Msg.error(sender, "바꿀 블럭이 없습니다.");
        return true;
      }

      wand.storeUndo(filteredArea);

      if (!silent) {
        String blockNameOriginal = BlockNames.valueOf(blockData_original.getMaterial().toString()).getName();
        String blockNameReplace = BlockNames.valueOf(blockData_replace.getMaterial().toString()).getName();
        Msg.info(sender, Msg.Prefix.WAND + Msg.effect("주위 " + radius + "블록 반경의 "
          + blockNameOriginal + Msg.getJosa(blockNameOriginal, "을", "를")
          + " " + blockNameReplace + Msg.getJosa(blockNameReplace, "으로", "로")
          + " 바꾸었습니다. (&6" + filteredArea.size() + "&r블록)"
        ));
      }

      wand.fill(blockData_replace, filteredArea, applyPhysics);

      return true;
    }

    /*
     * 육면체
     * cube <material> [options...]
     */
    if (args[0].equals("cube") || args[0].equals("emptycube") || args[0].equals("walledcube")
      || args[0].equals("ecube") || args[0].equals("wcube")
    ) {

      if (!sender.hasPermission("cherry.wand.edit.cube")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (args.length < 2) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, "사용법: " + command.getLabel() + " " + args[0] + " <블록> [옵션...]");
        return true;
      }

      // area
      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }
      List<Location> area = Area.CUBE.getArea(pos1, pos2);
      switch (args[0]) {
        case "ecube":
        case "emptycube": {
          area = Area.CUBE_EMPTY.getArea(pos1, pos2);
          break;
        }
        case "wcube":
        case "walledcube": {
          area = Area.CUBE_WALL.getArea(pos1, pos2);
          break;
        }
      }

      // blockdata
      String blockDataString = args[1];
      BlockData blockData;
      try {
        blockData = Wand.getBlockData(blockDataString);
      } catch (Exception e) {
        Msg.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
        return true;
      }

      // options
      boolean applyPhysics = false;
      boolean silent = false;
      int commandArgsLength = 2;
      if (args.length > commandArgsLength) {
        for (String str : args) {
          if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
          }
        }
      }

      wand.storeUndo(area);

      if (!silent) {
        String blockName = BlockNames.valueOf(blockData.getMaterial().name()).getName();
        Msg.info(sender, Msg.Prefix.WAND + Msg.effect("&6" + blockName + "&r" + Msg.getJosa(blockName, "을", "를") + " &6" + area.size() + "&r개 설치하였습니다."));
      }

      wand.fill(blockData, area, applyPhysics);

      return true;

    }

    /*
     * 윈기둥
     * cyl <material> <radius> <height> [options...]
     */
    if (args[0].equals("cyl") || args[0].equals("emptycyl") || args[0].equals("walledcyl")
      || args[0].equals("ecyl") || args[0].equals("wcyl")
      || args[0].equals("pointcyl") || args[0].equals("emptypointcyl") || args[0].equals("walledpointcyl")
      || args[0].equals("pcyl") || args[0].equals("epcyl") || args[0].equals("wpcyl")
    ) {

      if (!sender.hasPermission("cherry.wand.edit.cyl")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (args.length < 2) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, "사용법: " + command.getLabel() + " " + args[0] + " <블록> [반지름] [높이] [옵션...]");
        return true;
      }

      // blockdata
      String blockDataString = args[1];
      BlockData blockData;
      try {
        blockData = Wand.getBlockData(blockDataString);
      } catch (Exception e) {
        Msg.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
        return true;
      }

      // area
      Location pos1 = wand.getEdit().getPosition(1);
      if (pos1 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }

      // radius
      int radius = 1;
      if (args.length >= 3) {
        try {
          radius = Integer.parseInt(args[2]);
        } catch (Exception e) {
          if (Tool.Check.isInteger(args[2])) {
            Msg.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다.");
          }
          else {
            Msg.error(sender, "반지름 값은 정수만 입력할 수 있습니다.");
          }
          return true;
        }
        if (radius > 100000) {
          Msg.error(sender, "최대 반지름 값은 100000 입니다.");
          return true;
        }
        if (radius < 1) {
          Msg.error(sender, "최소 반지름 값은 1 입니다.");
          return true;
        }
      }

      // height
      int height = 1;
      if (args.length >= 4) {
        try {
          height = Integer.parseInt(args[3]);
        } catch (Exception e) {
          if (Tool.Check.isInteger(args[3])) {
            Msg.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다.");
          }
          else {
            Msg.error(sender, "높이 값은 정수만 입력할 수 있습니다.");
          }
          return true;
        }
        if (height > 265) {
          Msg.error(sender, "최대 높이 값은 256 입니다.");
          return true;
        }
        if (height < 0) {
          Msg.error(sender, "최소 높이 값은 1 입니다.");
          return true;
        }
      }

      List<Location> area = Area.CYLINDER.getArea(pos1, radius, height);
      switch (args[0]) {
        case "pcyl":
        case "pointcyl": {
          area = Area.CYLINDER_POINTED.getArea(pos1, radius, height);
          break;
        }
        case "ecyl":
        case "emptycyl": {
          area = Area.CYLINDER_EMPTY.getArea(pos1, radius, height);
          break;
        }
        case "epcyl":
        case "emptypointcyl": {
          area = Area.CYLINDER_POINTED_EMPTY.getArea(pos1, radius, height);
          break;
        }
        case "walledcyl":
        case "walledpointcyl": {
          Msg.warn(sender, "미완성 기능입니다.");
          return true;
        }
      }

      // options
      boolean applyPhysics = false;
      boolean silent = false;
      int commandArgsLength = 4;
      if (args.length > commandArgsLength) {
        for (String str : args) {
          if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
          }
        }
      }

      wand.storeUndo(area);

      if (!silent) {
        String blockName = BlockNames.valueOf(blockData.getMaterial().name()).getName();
        Msg.info(sender, Msg.Prefix.WAND + Msg.effect("&6" + blockName + "&r" + Msg.getJosa(blockName, "을", "를") + " &6" + area.size() + "&r개 설치하였습니다."));
      }

      wand.fill(blockData, area, applyPhysics);

      return true;

    }

    /*
     * 구
     * sphere <material> <radius> [options...]
     */
    if (args[0].equals("sphere") || args[0].equals("emptysphere") || args[0].equals("esphere")
      || args[0].equals("pointsphere") || args[0].equals("emptypointsphere")
      || args[0].equals("psphere") || args[0].equals("epsphere")
    ) {

      if (!sender.hasPermission("cherry.wand.edit.sphere")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (args.length < 2) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, "사용법: " + command.getLabel() + " " + args[0] + " <블록> [반지름] [옵션...]");
        return true;
      }

      // blockdata
      String blockDataString = args[1];
      BlockData blockData;
      try {
        blockData = Wand.getBlockData(blockDataString);
      } catch (Exception e) {
        Msg.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
        return true;
      }

      // area
      Location pos1 = wand.getEdit().getPosition(1);
      if (pos1 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }

      // radius
      int radius = 1;
      if (args.length >= 3) {
        try {
          radius = Integer.parseInt(args[2]);
        } catch (Exception e) {
          if (Tool.Check.isInteger(args[2])) {
            Msg.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다.");
          }
          else {
            Msg.error(sender, "반지름 값은 정수만 입력할 수 있습니다.");
          }
          return true;
        }
        if (radius > 100000) {
          Msg.error(sender, "최대 반지름 값은 100000 입니다.");
          return true;
        }
        if (radius < 1) {
          Msg.error(sender, "최소 반지름 값은 1 입니다.");
          return true;
        }
      }

      List<Location> area = Area.SPHERE.getArea(pos1, radius);
      switch (args[0]) {
        case "psphere":
        case "pointsphere": {
          area = Area.SPHERE_POINTED.getArea(pos1, radius);
          break;
        }
        case "esphere":
        case "emptysphere": {
          area = Area.SPHERE_EMPTY.getArea(pos1, radius);
          break;
        }
        case "epsphere":
        case "emptypointsphere": {
          area = Area.SPHERE_POINTED_EMPTY.getArea(pos1, radius);
          break;
        }
        case "wsphere":
        case "wpsphere":
        case "walledsphere":
        case "walledpointsphere": {
          Msg.warn(sender, "미완성 기능입니다.");
          return true;
        }
      }

      // options
      boolean applyPhysics = false;
      boolean silent = false;
      int commandArgsLength = 3;
      if (args.length > commandArgsLength) {
        for (String str : args) {
          if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
          }
        }
      }

      wand.storeUndo(area);

      if (!silent) {
        String blockName = BlockNames.valueOf(blockData.getMaterial().name()).getName();
        Msg.info(sender, Msg.Prefix.WAND + Msg.effect("&6" + blockName + "&r" + Msg.getJosa(blockName, "을", "를") + " &6" + area.size() + "&r개 설치하였습니다."));
      }

      wand.fill(blockData, area, applyPhysics);

      return true;

    }

    /*
     * 벽
     * wall <material> [option...]
     */
    if (args[0].equals("wall")) {

      if (!sender.hasPermission("cherry.wand.edit.wall")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (args.length <= 1) {
        Msg.error(sender, Msg.NO_ARGS);
        Msg.info(sender, "사용법: " + command.getLabel() + " " + args[0] + " <블록> [옵션...]");
        return true;
      }

      // area
      Location pos1 = wand.getEdit().getPosition(1);
      Location pos2 = wand.getEdit().getPosition(2);
      if (pos1 == null || pos2 == null) {
        Msg.error(sender, "포지션이 지정되지 않았습니다.");
        return true;
      }
      List<Location> area = Area.WALL.getArea(pos1, pos2);

      // blockdata
      String blockDataString = args[1];
      BlockData blockData;
      try {
        blockData = Wand.getBlockData(blockDataString);
      } catch (Exception e) {
        Msg.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
        return true;
      }

      // options
      boolean applyPhysics = false;
      boolean silent = false;
      int commandArgsLength = 2;
      if (args.length > commandArgsLength) {
        for (String str : args) {
          if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-applyPhysics") || str.equalsIgnoreCase("-ap")) {
            applyPhysics = true;
          }
          if ((args.length <= commandArgsLength + 2) && str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
            silent = true;
          }
        }
      }

      wand.storeUndo(area);

      if (!silent) {
        String blockName = BlockNames.valueOf(blockData.getMaterial().name()).getName();
        Msg.info(sender, Msg.Prefix.WAND + Msg.effect("&6" + blockName + "&r" + Msg.getJosa(blockName, "을", "를") + " &6" + area.size() + "&r개 설치하였습니다."));
      }

      wand.fill(blockData, area, applyPhysics);

      return true;

    }

    Msg.error(sender, Msg.UNKNOWN);
    return true;
  }

  private int verifyInteger(String name, int max, int min, String input) throws Exception {
    int i;
    try {
      i = Integer.parseInt(input);
    } catch (Exception e) {
      if (Tool.Check.isInteger(input)) {
        throw new Exception("프로그램 상 사용할 수 없는 범위의 수입니다.");
      }
      else {
        throw new Exception(name + " 값은 정수만 입력할 수 있습니다.");
      }
    }
    if (i > max) {
      throw new Exception("최대 " + name + " 값은 " + max + " 입니다.");
    }
    if (i < min) {
      throw new Exception("최소 " + name + " 값은 " + min + " 입니다.");
    }
    return i;
  }
}
