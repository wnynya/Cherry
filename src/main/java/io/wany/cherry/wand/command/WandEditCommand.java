package io.wany.cherry.wand.command;

import io.wany.cherry.Console;
import io.wany.cherry.Message;
import io.wany.cherry.amethyst.DataTypeChecker;
import io.wany.cherry.wand.Wand;
import io.wany.cherry.wand.WandBlock;
import io.wany.cherry.wand.WandEdit;
import io.wany.cherry.wand.area.Area;
import net.kyori.adventure.text.format.TextColor;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WandEditCommand implements CommandExecutor {

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

    if (!Wand.ENABLED) {
      Message.info(sender, Wand.PREFIX, "완드 기능이 비활성화된 상태입니다");
      return true;
    }

    UUID uuid;
    Player player = null;
    Wand wand;

    if (sender instanceof Player) {
      player = (Player) sender;
      wand = Wand.getWand(player);
    }
    else {
      uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
      wand = Wand.getWand(uuid);
    }

    if (args.length == 0) {
      Message.error(sender, Message.CommandFeedback.NO_ARGS);
      return true;
    }

    args[0] = args[0].toLowerCase();

    switch (args[0]) {

      // 완드 에딧 아이템 얻기
      case "get" -> {
        if (!sender.hasPermission("cherry.wand.get")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }
        if (player == null) {
          Message.error(sender, Message.CommandFeedback.ONLY_PLAYER);
          return true;
        }
        if (args.length >= 2 && args[1].equalsIgnoreCase("edit")) {
          // 완드 에딧용 아이템을 뭐
          Message.info(sender, Wand.PREFIX + "완드 에딧용 아이템을 뭐");
        }
        ItemStack item = Wand.getWandItem(Wand.ItemType.EDIT_POSITIONER);
        player.getInventory().addItem(item);
        return true;
      }

      /*
       * 선택 영역
       */

      // 포지션 설정
      case "pos1" -> {
        if (!sender.hasPermission("cherry.wand.edit.pos")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        Location location;

        // 특정 월드의 특정 좌표
        if (args.length >= 5) {
          if (!sender.hasPermission("cherry.wand.edit.pos.location")) {
            Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
            return true;
          }
          World world = Bukkit.getWorld(args[4]);
          if (world == null) {
            Message.error(sender, "월드를 찾을 수 없습니다");
            return true;
          }
          try {
            location = new Location(world, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
          }
          catch (Exception e) {
            Message.error(sender, "올바른 좌표가 아닙니다");
            return true;
          }
        }
        // 플레이어가 있는 월드의 특정 좌표
        else if (args.length >= 4) {
          if (!sender.hasPermission("cherry.wand.edit.pos.location")) {
            Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
            return true;
          }
          if (player == null) {
            Message.error(sender, Message.CommandFeedback.ONLY_PLAYER);
            return true;
          }
          World world = player.getWorld();
          try {
            location = new Location(world, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
          }
          catch (Exception e) {
            Message.error(sender, "올바른 좌표가 아닙니다");
            return true;
          }
        }
        // 플레이어의 좌표
        else {
          if (player == null) {
            Message.error(sender, Message.CommandFeedback.ONLY_PLAYER);
            return true;
          }
          location = player.getLocation().getBlock().getLocation();
        }

        // 옵션
        boolean silent = false;
        if (args.length > 5) {
          for (String str : args) {
            if (str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
              silent = true;
              break;
            }
          }
        }

        WandEdit edit = wand.getEdit();

        edit.setPosition(1, location);

        String message = "첫번째 포지션이 설정되었습니다 (" + Wand.COLOR + location.getX() + "&r, " + Wand.COLOR + location.getY() + "&r, " + Wand.COLOR + location.getZ() + "&r)";
        if (edit.getPosition(1) != null && edit.getPosition(2) != null) {
          int count = Area.CUBE.getArea(edit.getPosition(1), edit.getPosition(2)).size();
          message += " (" + Wand.COLOR + count + "&r블록)";
          edit.setParticleArea(Area.CUBE_PARTICLE.getArea(edit.getPosition(1), edit.getPosition(2)));
        }
        else {
          edit.setParticleArea(Area.CUBE_PARTICLE.getArea(edit.getPosition(1), edit.getPosition(1)));
        }

        if (!silent) {
          Message.info(sender, Wand.PREFIX + Message.effect(message));
        }

        return true;
      }
      case "pos2" -> {
        if (!sender.hasPermission("cherry.wand.edit.pos")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        Location location;

        // 특정 월드의 특정 좌표
        if (args.length >= 5) {
          if (!sender.hasPermission("cherry.wand.edit.pos.location")) {
            Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
            return true;
          }
          World world = Bukkit.getWorld(args[4]);
          if (world == null) {
            Message.error(sender, "월드를 찾을 수 없습니다");
            return true;
          }
          try {
            location = new Location(world, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
          }
          catch (Exception e) {
            Message.error(sender, "올바른 좌표가 아닙니다");
            return true;
          }
        }
        // 플레이어가 있는 월드의 특정 좌표
        else if (args.length >= 4) {
          if (!sender.hasPermission("cherry.wand.edit.pos.location")) {
            Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
            return true;
          }
          if (player == null) {
            Message.error(sender, Message.CommandFeedback.ONLY_PLAYER);
            return true;
          }
          World world = player.getWorld();
          try {
            location = new Location(world, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
          }
          catch (Exception e) {
            Message.error(sender, "올바른 좌표가 아닙니다");
            return true;
          }
        }
        // 플레이어의 좌표
        else {
          if (player == null) {
            Message.error(sender, Message.CommandFeedback.ONLY_PLAYER);
            return true;
          }
          location = player.getLocation().getBlock().getLocation();
        }

        // 옵션
        boolean silent = false;
        if (args.length > 5) {
          for (String str : args) {
            if (str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
              silent = true;
              break;
            }
          }
        }

        WandEdit edit = wand.getEdit();

        edit.setPosition(2, location);

        String message = "두번째 포지션이 설정되었습니다 (" + Wand.COLOR + location.getX() + "&r, " + Wand.COLOR + location.getY() + "&r, " + Wand.COLOR + location.getZ() + "&r)";
        if (edit.getPosition(1) != null && edit.getPosition(2) != null) {
          int count = Area.CUBE.getArea(edit.getPosition(1), edit.getPosition(2)).size();
          message += " (" + Wand.COLOR + count + "&r블록)";
          edit.setParticleArea(Area.CUBE_PARTICLE.getArea(edit.getPosition(1), edit.getPosition(2)));
        }
        else {
          edit.setParticleArea(Area.CUBE_PARTICLE.getArea(edit.getPosition(2), edit.getPosition(2)));
        }

        if (!silent) {
          Message.info(sender, Wand.PREFIX + Message.effect(message));
        }

        return true;
      }
      case "pos3" -> {
        if (!sender.hasPermission("cherry.wand.edit.pos")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        Location location;

        // 특정 월드의 특정 좌표
        if (args.length >= 5) {
          if (!sender.hasPermission("cherry.wand.edit.pos.location")) {
            Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
            return true;
          }
          World world = Bukkit.getWorld(args[4]);
          if (world == null) {
            Message.error(sender, "월드를 찾을 수 없습니다");
            return true;
          }
          try {
            location = new Location(world, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
          }
          catch (Exception e) {
            Message.error(sender, "올바른 좌표가 아닙니다");
            return true;
          }
        }
        // 플레이어가 있는 월드의 특정 좌표
        else if (args.length >= 4) {
          if (!sender.hasPermission("cherry.wand.edit.pos.location")) {
            Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
            return true;
          }
          if (player == null) {
            Message.error(sender, Message.CommandFeedback.ONLY_PLAYER);
            return true;
          }
          World world = player.getWorld();
          try {
            location = new Location(world, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
          }
          catch (Exception e) {
            Message.error(sender, "올바른 좌표가 아닙니다");
            return true;
          }
        }
        // 플레이어의 좌표
        else {
          if (player == null) {
            Message.error(sender, Message.CommandFeedback.ONLY_PLAYER);
            return true;
          }
          location = player.getLocation().getBlock().getLocation();
        }

        // 옵션
        boolean silent = false;
        if (args.length > 5) {
          for (String str : args) {
            if (str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
              silent = true;
              break;
            }
          }
        }

        WandEdit wandEdit = wand.getEdit();

        wandEdit.setPosition(3, location);

        String message = "세번째 포지션이 설정되었습니다 (" + Wand.COLOR + location.getX() + "&r, " + Wand.COLOR + location.getY() + "&r, " + Wand.COLOR + location.getZ() + "&r)";

        if (!silent) {
          Message.info(sender, Wand.PREFIX + Message.effect(message));
        }

        return true;
      }

      /*
       * 작업 내역 (에딧, 브러시 공통)
       */

      // 되돌리기
      case "undo" -> {
        if (!sender.hasPermission("cherry.wand.undo")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        // 되돌리기 횟수 설정
        int repeat = 1; // undo count
        if (args.length >= 2) {
          try {
            repeat = Integer.parseInt(args[1]);
          }
          catch (Exception e) {
            if (DataTypeChecker.isInteger(args[1])) {
              Message.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다");
            }
            else {
              Message.error(sender, "반복 횟수 값은 정수만 입력할 수 있습니다");
            }
            return true;
          }
          if (repeat < 1) {
            Message.error(sender, "최소 반복 횟수 값은 1 입니다");
            return true;
          }
        }

        // 대상 플레이어 설정
        Wand target = wand;
        if (args.length >= 3) {
          String targetPlayerString = args[2];
          Player targetPlayer;
          try {
            targetPlayer = Bukkit.getPlayer(UUID.fromString(targetPlayerString));
          }
          catch (Exception e) {
            targetPlayer = Bukkit.getPlayer(targetPlayerString);
          }
          if (targetPlayer == null) {
            Message.error(sender, "플레이어를 찾을 수 없습니다");
            return true;
          }
          target = Wand.getWand(targetPlayer);
        }

        // 옵션
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
              Message.info(sender, Wand.PREFIX + "이전으로 되돌렸습니다");
            }
          }
          else {
            if (!silent) {
              Message.warn(sender, Wand.PREFIX + "이전으로 되돌릴 수 없습니다");
            }
            return true;
          }
        }

        return true;
      }
      // 되돌리기 취소
      case "redo" -> {
        if (!sender.hasPermission("cherry.wand.redo")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        // 되돌리기 횟수 설정
        int repeat = 1; // redo count
        if (args.length >= 2) {
          try {
            repeat = Integer.parseInt(args[1]);
          }
          catch (Exception e) {
            if (DataTypeChecker.isInteger(args[1])) {
              Message.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다");
            }
            else {
              Message.error(sender, "반복 횟수 값은 정수만 입력할 수 있습니다");
            }
            return true;
          }
          if (repeat < 1) {
            Message.error(sender, "최소 반복 횟수 값은 1 입니다");
            return true;
          }
        }

        // 대상 플레이어 설정
        Wand target = wand;
        if (args.length >= 3) {
          String targetPlayerString = args[2];
          Player targetPlayer;
          try {
            targetPlayer = Bukkit.getPlayer(UUID.fromString(targetPlayerString));
          }
          catch (Exception e) {
            targetPlayer = Bukkit.getPlayer(targetPlayerString);
          }
          if (targetPlayer == null) {
            Message.error(sender, "플레이어를 찾을 수 없습니다");
            return true;
          }
          target = Wand.getWand(targetPlayer);
        }

        // 옵션
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
              Message.info(sender, Wand.PREFIX + "되돌리기를 취소하였습니다");
            }
          }
          else {
            if (!silent) {
              Message.warn(sender, Wand.PREFIX + "되돌리기를 취소할 수 없습니다");
            }
            return true;
          }
        }

        return true;
      }

      /*
       * 클립보드
       */

      // 선택 영역을 클립보드로 복사
      case "copy" -> {
        if (!sender.hasPermission("cherry.wand.copy")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        if (args.length < 5 && player == null) {
          Message.error(sender, Message.CommandFeedback.NO_ARGS);
          Message.info(sender, "사용법: " + label + " " + args[0] + " <X> <Y> <Z> <World> [옵션...]");
          return true;
        }

        // area
        Location pos1 = wand.getEdit().getPosition(1);
        Location pos2 = wand.getEdit().getPosition(2);
        if (pos1 == null || pos2 == null) {
          Message.error(sender, "포지션이 지정되지 않았습니다");
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
          }
          catch (Exception e) {
            Console.error(e.getMessage());
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
              Console.error("월드를 찾을 수 없습니다");
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
          Message.info(sender, Wand.PREFIX + Message.effect("선택 영역을 클립보드로 복사하였습니다 (" + Wand.COLOR + wand.clipboard.size() + "&r블록)"));
        }

        return true;
      }

      // 선택 영역을 클립보드로 잘라내기
      case "cut" -> {
        if (!sender.hasPermission("cherry.wand.cut")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        if (args.length < 5 && player == null) {
          Message.error(sender, Message.CommandFeedback.NO_ARGS);
          Message.info(sender, "사용법: " + label + " " + args[0] + " <X> <Y> <Z> <World> [옵션...]");
          return true;
        }

        // area
        Location pos1 = wand.getEdit().getPosition(1);
        Location pos2 = wand.getEdit().getPosition(2);
        if (pos1 == null || pos2 == null) {
          Message.error(sender, "포지션이 지정되지 않았습니다");
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
          }
          catch (Exception e) {
            Console.error(e.getMessage());
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
              Console.error("월드를 찾을 수 없습니다");
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
          Message.info(sender, Wand.PREFIX + Message.effect("선택 영역을 클립보드로 잘라내었습니다 (" + Wand.COLOR + wand.clipboard.size() + "&r블록)"));
        }

        return true;
      }

      // 클립보드 붙여넣기
      case "paste" -> {
        if (!sender.hasPermission("cherry.wand.paste")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        if (wand.clipboard == null) {
          Message.error(sender, "클립보드에 저장된 데이터가 없습니다");
          return true;
        }
        if (args.length < 5 && player == null) {
          Message.error(sender, Message.CommandFeedback.NO_ARGS);
          Message.info(sender, "사용법: " + label + " " + args[0] + " <X> <Y> <Z> <World> [옵션...]");
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
          }
          catch (Exception e) {
            Console.error(e.getMessage());
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
              Console.error("월드를 찾을 수 없습니다");
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

        for (WandBlock wandBlock : wand.clipboard) {
          Location loc = new Location(posP.getWorld(), (int) wandBlock.getLocation().getX() + posP.getX(), (int) wandBlock.getLocation().getY() + posP.getY(), (int) wandBlock.getLocation().getZ() + posP.getZ());
          area.add(loc);
        }

        wand.storeUndo(area);

        wand.paste(posP, blackList, applyPhysics);

        if (!silent) {
          Message.info(sender, Wand.PREFIX + Message.effect("클립보드를 붙여넣었습니다 (" + Wand.COLOR + area.size() + "&r블록)"));
        }

        return true;
      }

      // 클립보드 회전
      case "rotate" -> {
        if (!sender.hasPermission("cherry.wand.paste")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }
        if (wand.clipboard == null) {
          Message.error(sender, "클립보드에 저장된 데이터가 없습니다");
          return true;
        }
        if (args.length < 2) {
          Message.error(sender, Message.CommandFeedback.NO_ARGS);
          Message.info(sender, "사용법: " + label + " " + args[0] + " <right|left> [옵션...]");
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
          case "right" -> {
            wand.rotate("right");
            if (!silent) {
              Message.info(sender, Wand.PREFIX + Message.effect("클립보드를 오른쪽으로 90도 회전하였습니다"));
            }
            return true;
          }
          case "left" -> {
            wand.rotate("left");
            if (!silent) {
              Message.info(sender, Wand.PREFIX + Message.effect("클립보드를 왼쪽으로 90도 회전하였습니다"));
            }
            return true;
          }
          case "100" -> {
            wand.multiply(-1, 1, 1);
            return true;
          }
          case "010" -> {
            wand.multiply(1, -1, 1);
            return true;
          }
          case "001" -> {
            wand.multiply(1, 1, -1);
            return true;
          }
          case "110" -> {
            wand.multiply(-1, -1, 1);
            return true;
          }
          case "101" -> {
            wand.multiply(-1, 1, -1);
            return true;
          }
          case "011" -> {
            wand.multiply(1, -1, -1);
            return true;
          }
          case "111" -> {
            wand.multiply(-1, -1, -1);
            return true;
          }
          default -> {
            Message.error(sender, "알 수 없는 방향입니다");
            return true;
          }
        }
      }

      // 클립보드 반전
      case "flip" -> {
        if (!sender.hasPermission("cherry.wand.paste")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }
        if (wand.clipboard == null) {
          Message.error(sender, "클립보드에 저장된 데이터가 없습니다");
          return true;
        }
        if (args.length < 2) {
          Message.error(sender, Message.CommandFeedback.NO_ARGS);
          Message.info(sender, "사용법: " + label + " " + args[0] + " <right|left> [옵션...]");
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
          Message.info(sender, Wand.PREFIX + Message.effect("rotate: " + dir));
        }

        return true;
      }

      /*
       * 쌓기e
       */

      // 선택 영역을 특정 방향으로 쌓기
      case "stack" -> {
        if (!sender.hasPermission("cherry.wand.stack")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        if (args.length < 3 && player == null) {
          Message.error(sender, Message.CommandFeedback.NO_ARGS);
          Message.info(sender, "사용법: " + label + " " + args[0] + " <반복 횟수> [방향] [옵션...]");
          return true;
        }

        Location pos1 = wand.getEdit().getPosition(1);
        Location pos2 = wand.getEdit().getPosition(2);
        if (pos1 == null || pos2 == null) {
          Message.error(sender, "포지션이 지정되지 않았습니다");
          return true;
        }

        // repeat
        int repeat = 1;
        if (args.length >= 2) {
          try {
            repeat = Integer.parseInt(args[1]);
          }
          catch (Exception e) {
            if (DataTypeChecker.isInteger(args[1])) {
              Message.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다");
            }
            else {
              Message.error(sender, "반복 횟수 값은 정수만 입력할 수 있습니다");
            }
            return true;
          }
          if (repeat > 1000) {
            Message.error(sender, "최대 반복 횟수 값은 1000 입니다");
            return true;
          }
          if (repeat < 1) {
            Message.error(sender, "최소 반복 횟수 값은 1 입니다");
            return true;
          }
        }

        String dir;
        if (args.length >= 3) {
          dir = args[2].toLowerCase();
        }
        else {
          Vector vector = player.getLocation().getDirection();

          double maxd = Math.max(Math.abs(vector.getX()), Math.max(Math.abs(vector.getY()), Math.abs(vector.getZ())));

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
            Console.error("올바르지 않은 방향입니다 (e)");
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
              case "east" -> newLoc = new Location(world, loc.getX() + (m * ((maxX + 1) - minX)), loc.getY(), loc.getZ());
              case "west" -> newLoc = new Location(world, loc.getX() - (m * ((maxX + 1) - minX)), loc.getY(), loc.getZ());
              case "south" -> newLoc = new Location(world, loc.getX(), loc.getY(), loc.getZ() + (m * ((maxZ + 1) - minZ)));
              case "north" -> newLoc = new Location(world, loc.getX(), loc.getY(), loc.getZ() - (m * ((maxZ + 1) - minZ)));
              case "up" -> newLoc = new Location(world, loc.getX(), loc.getY() + (m * ((maxY + 1) - minY)), loc.getZ());
              case "down" -> newLoc = new Location(world, loc.getX(), loc.getY() - (m * ((maxY + 1) - minY)), loc.getZ());
              default -> {
                Console.error("올바르지 않은 방향입니다");
                return true;
              }
            }
            undoArea.add(newLoc);
          }
        }

        wand.storeUndo(undoArea);

        if (!silent) {
          String dirKo = switch (dir) {
            case "east" -> "동쪽";
            case "west" -> "서쪽";
            case "south" -> "남쪽";
            case "north" -> "북쪽";
            default -> "";
          };
          Message.info(sender, Wand.PREFIX + Message.effect("지정 영역을 " + Wand.COLOR + dirKo + "&r으로 " + Wand.COLOR + repeat + "&r회 붙여넣었습니다 (" + Wand.COLOR + undoArea.size() + "블록)"));
        }

        wand.stack(pos1, pos2, dir, repeat, applyPhysics);

        return true;
      }

      /*
       * 이동
       */

      // 선택 영역을 특정 방향으로 이동
      case "move" -> {

      }

      /*
       * 교체
       */

      // 선택 영역의 특정 블록 교체
      case "replace" -> {
        if (!sender.hasPermission("cherry.wand.replace")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        if (args.length < 3) {
          Message.error(sender, Message.CommandFeedback.NO_ARGS);
          Message.info(sender, "사용법: " + command.getLabel() + " " + args[0] + " <블록> <블록> [옵션...]");
          return true;
        }

        // area
        Location pos1 = wand.getEdit().getPosition(1);
        Location pos2 = wand.getEdit().getPosition(2);
        if (pos1 == null || pos2 == null) {
          Message.error(sender, "포지션이 지정되지 않았습니다");
          return true;
        }
        List<Location> area = Area.CUBE.getArea(pos1, pos2);

        // blockdata
        String blockDataString_original = args[1];
        BlockData blockData_original;
        try {
          blockData_original = Wand.getBlockData(blockDataString_original);
        }
        catch (Exception e) {
          Message.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
          return true;
        }
        String dataString_original = Wand.getDataString(blockDataString_original);

        // blockdata
        String blockDataString_replace = args[2];
        BlockData blockData_replace;
        try {
          blockData_replace = Wand.getBlockData(blockDataString_replace);
        }
        catch (Exception e) {
          Message.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
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
          Message.error(sender, "바꿀 블럭이 없습니다");
          return true;
        }

        wand.storeUndo(filteredArea);

        if (!silent) {
          String blockNameOriginal = blockData_original.getMaterial().toString();
          String blockNameReplace = blockData_replace.getMaterial().toString();
          Message.info(sender, Wand.PREFIX + Message.effect("지정 영역의 " + blockNameOriginal + "을(를)" + " " + blockNameReplace + "(으)로" + " 바꾸었습니다 (&6" + filteredArea.size() + "&r블록)"));
        }

        wand.fill(blockData_replace, filteredArea, applyPhysics);

        return true;
      }

      // 특정 좌표 주변의 특정 블록 교체
      case "replacenear" -> {
        if (!sender.hasPermission("cherry.wand.replacenear")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        if (player == null) {
          Message.error(sender, Message.CommandFeedback.ONLY_PLAYER);
          return true;
        }

        if (args.length < 3) {
          Message.error(sender, Message.CommandFeedback.NO_ARGS);
          Message.info(sender, "사용법: " + command.getLabel() + " " + args[0] + " <블록> <블록> [반지름] [옵션...]");
          return true;
        }

        // area
        Location pos = player.getLocation().getBlock().getLocation();

        // blockdata
        String blockDataString_original = args[1];
        BlockData blockData_original;
        try {
          blockData_original = Wand.getBlockData(blockDataString_original);
        }
        catch (Exception e) {
          Message.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
          return true;
        }
        String dataString_original = Wand.getDataString(blockDataString_original);


        // blockdata
        String blockDataString_replace = args[2];
        BlockData blockData_replace;
        try {
          blockData_replace = Wand.getBlockData(blockDataString_replace);
        }
        catch (Exception e) {
          Message.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
          return true;
        }

        // radius
        int radius = wand.lastReplacenearRadius;
        if (args.length > 3) {
          try {
            radius = Integer.parseInt(args[3]);
          }
          catch (Exception e) {
            if (DataTypeChecker.isInteger(args[3])) {
              Message.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다");
            }
            else {
              Message.error(sender, "반지름 값은 정수만 입력할 수 있습니다");
            }
            return true;
          }
          if (radius > 100000) {
            Message.error(sender, "최대 반지름 값은 100000 입니다");
            return true;
          }
          if (radius < 1) {
            Message.error(sender, "최소 반지름 값은 1 입니다");
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
          Message.error(sender, "바꿀 블럭이 없습니다");
          return true;
        }

        wand.storeUndo(filteredArea);

        if (!silent) {
          String blockNameOriginal = blockData_original.getMaterial().toString();
          String blockNameReplace = blockData_replace.getMaterial().toString();
          Message.info(sender, Wand.PREFIX + Message.effect("주위 " + radius + "블록 반경의 " + blockNameOriginal + "을(를)" + " " + blockNameReplace + "(으)로" + " 바꾸었습니다 (&6" + filteredArea.size() + "&r블록)"));
        }

        wand.fill(blockData_replace, filteredArea, applyPhysics);

        return true;
      }

      /*
       * 육면체
       */

      // 선택 영역을 특정 형태의 욱면체로 채우기
      case "cube", "emptycube", "walledcube", "ecube", "wcube" -> {
        if (!sender.hasPermission("cherry.wand.edit.cube")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        if (args.length < 2) {
          Message.error(sender, Message.CommandFeedback.NO_ARGS);
          Message.info(sender, "사용법: " + command.getLabel() + " " + args[0] + " <블록> [옵션...]");
          return true;
        }

        // 작업 영역 가져오기
        Location pos1 = wand.getEdit().getPosition(1);
        Location pos2 = wand.getEdit().getPosition(2);
        if (pos1 == null || pos2 == null) {
          Message.error(sender, "포지션이 지정되지 않았습니다");
          return true;
        }
        List<Location> area = Area.CUBE.getArea(pos1, pos2);
        switch (args[0]) {
          case "emptycube", "ecube" -> {
            area = Area.CUBE_EMPTY.getArea(pos1, pos2);
          }
          case "walledcube", "wcube" -> {
            area = Area.CUBE_WALL.getArea(pos1, pos2);
          }
        }

        // 설치할 블록 데이터 파싱
        String blockDataString = args[1];
        BlockData blockData;
        try {
          blockData = Wand.getBlockData(blockDataString);
        }
        catch (Exception e) {
          Wand.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
          return true;
        }

        // 옵션
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
          Message.send(sender, Message.parse(Message.effect(Wand.PREFIX), Message.parse(blockData.getMaterial()).color(TextColor.fromHexString(Wand.COLORHEX)), Message.effect("&r을(를) " + Wand.COLOR + area.size() + "&r개 설치하였습니다")));
        }

        wand.fill(blockData, area, applyPhysics);

        return true;
      }

      /*
       * 원기둥
       */

      // 첫번째 선택 좌표 기준의 반지름 영역을 특정 형태의 원기둥으로 채우기
      case "cyl", "emptycyl", "walledcyl", "pointcyl", "emptypointcyl", "walledpointcyl", "ecyl", "wcyl", "pcyl", "wpcyl", "epcyl" -> {
        if (!sender.hasPermission("cherry.wand.edit.cyl")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        if (args.length < 2) {
          Message.error(sender, Message.CommandFeedback.NO_ARGS);
          Message.info(sender, "사용법: " + command.getLabel() + " " + args[0] + " <블록> [반지름] [높이] [옵션...]");
          return true;
        }

        // blockdata
        String blockDataString = args[1];
        BlockData blockData;
        try {
          blockData = Wand.getBlockData(blockDataString);
        }
        catch (Exception e) {
          Message.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
          return true;
        }

        // area
        Location pos1 = wand.getEdit().getPosition(1);
        if (pos1 == null) {
          Message.error(sender, "포지션이 지정되지 않았습니다");
          return true;
        }

        // radius
        int radius = 1;
        if (args.length >= 3) {
          try {
            radius = Integer.parseInt(args[2]);
          }
          catch (Exception e) {
            if (DataTypeChecker.isInteger(args[2])) {
              Message.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다");
            }
            else {
              Message.error(sender, "반지름 값은 정수만 입력할 수 있습니다");
            }
            return true;
          }
          if (radius > 100000) {
            Message.error(sender, "최대 반지름 값은 100000 입니다");
            return true;
          }
          if (radius < 1) {
            Message.error(sender, "최소 반지름 값은 1 입니다");
            return true;
          }
        }

        // height
        int height = 1;
        if (args.length >= 4) {
          try {
            height = Integer.parseInt(args[3]);
          }
          catch (Exception e) {
            if (DataTypeChecker.isInteger(args[3])) {
              Message.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다");
            }
            else {
              Message.error(sender, "높이 값은 정수만 입력할 수 있습니다");
            }
            return true;
          }
          if (height > 265) {
            Message.error(sender, "최대 높이 값은 256 입니다");
            return true;
          }
          if (height < 0) {
            Message.error(sender, "최소 높이 값은 1 입니다");
            return true;
          }
        }

        List<Location> area = Area.CYLINDER.getArea(pos1, radius, height);
        switch (args[0]) {
          case "ecyl", "emptycyl" -> {
            area = Area.CYLINDER_EMPTY.getArea(pos1, radius, height);
          }
          case "wcyl", "walledcyl" -> {
            Message.warn(sender, "미완성 기능입니다1");
            return true;
          }
          case "pcyl", "pointcyl" -> {
            area = Area.CYLINDER_POINTED.getArea(pos1, radius, height);
          }
          case "epcyl", "emptypointcyl" -> {
            area = Area.CYLINDER_POINTED_EMPTY.getArea(pos1, radius, height);
          }
          case "wpcyl", "walledpointcyl" -> {
            Message.warn(sender, "미완성 기능입니다2");
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
          Message.send(sender, Message.parse(Message.effect(Wand.PREFIX), Message.parse(blockData.getMaterial()).color(TextColor.fromHexString(Wand.COLORHEX)), Message.effect("&r을(를) " + Wand.COLOR + area.size() + "&r개 설치하였습니다")));
        }

        wand.fill(blockData, area, applyPhysics);

        return true;
      }

      /*
       * 구
       */

      // 첫번째 선택 좌표 기준의 반지름 영역을 특정 형태의 구로 채우기
      case "sphere", "emptysphere", "pointsphere", "emptypointsphere", "esphere", "psphere", "epsphere" -> {
        if (!sender.hasPermission("cherry.wand.edit.sphere")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        if (args.length < 2) {
          Message.error(sender, Message.CommandFeedback.NO_ARGS);
          Message.info(sender, "사용법: " + command.getLabel() + " " + args[0] + " <블록> [반지름] [옵션...]");
          return true;
        }

        // blockdata
        String blockDataString = args[1];
        BlockData blockData;
        try {
          blockData = Wand.getBlockData(blockDataString);
        }
        catch (Exception e) {
          Message.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
          return true;
        }

        // area
        Location pos1 = wand.getEdit().getPosition(1);
        if (pos1 == null) {
          Message.error(sender, "포지션이 지정되지 않았습니다");
          return true;
        }

        // radius
        int radius = 1;
        if (args.length >= 3) {
          try {
            radius = Integer.parseInt(args[2]);
          }
          catch (Exception e) {
            if (DataTypeChecker.isInteger(args[2])) {
              Message.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다");
            }
            else {
              Message.error(sender, "반지름 값은 정수만 입력할 수 있습니다");
            }
            return true;
          }
          if (radius > 100000) {
            Message.error(sender, "최대 반지름 값은 100000 입니다");
            return true;
          }
          if (radius < 1) {
            Message.error(sender, "최소 반지름 값은 1 입니다");
            return true;
          }
        }

        List<Location> area = Area.SPHERE.getArea(pos1, radius);
        switch (args[0]) {
          case "psphere", "pointsphere" -> {
            area = Area.SPHERE_POINTED.getArea(pos1, radius);
          }
          case "esphere", "emptysphere" -> {
            area = Area.SPHERE_EMPTY.getArea(pos1, radius);
          }
          case "epsphere", "emptypointsphere" -> {
            area = Area.SPHERE_POINTED_EMPTY.getArea(pos1, radius);
          }
          case "wsphere", "wpsphere", "walledsphere", "walledpointsphere" -> {
            Message.warn(sender, "미완성 기능입니다");
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
          Message.send(sender, Message.parse(Message.effect(Wand.PREFIX), Message.parse(blockData.getMaterial()).color(TextColor.fromHexString(Wand.COLORHEX)), Message.effect("&r을(를) " + Wand.COLOR + area.size() + "&r개 설치하였습니다")));
        }

        wand.fill(blockData, area, applyPhysics);

        return true;
      }

      // 특정 좌표 기준의 반지름 영역을 특정 형태의 구로 채우기
      case "spherenear", "emptyspherenear", "pointspherenear", "emptypointspherenear", "ensphere", "pnsphere", "epnsphere" -> {
        if (!sender.hasPermission("cherry.wand.edit.sphere")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        if (args.length < 2) {
          Message.error(sender, Message.CommandFeedback.NO_ARGS);
          Message.info(sender, "사용법: " + command.getLabel() + " " + args[0] + " <블록> [반지름] [옵션...]");
          return true;
        }

        // blockdata
        String blockDataString = args[1];
        BlockData blockData;
        try {
          blockData = Wand.getBlockData(blockDataString);
        }
        catch (Exception e) {
          Message.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
          return true;
        }

        // area
        Location pos1 = player.getLocation().getBlock().getLocation();

        // radius
        int radius = 1;
        if (args.length >= 3) {
          try {
            radius = Integer.parseInt(args[2]);
          }
          catch (Exception e) {
            if (DataTypeChecker.isInteger(args[2])) {
              Message.error(sender, "프로그램 상 사용할 수 없는 범위의 수입니다");
            }
            else {
              Message.error(sender, "반지름 값은 정수만 입력할 수 있습니다");
            }
            return true;
          }
          if (radius > 100000) {
            Message.error(sender, "최대 반지름 값은 100000 입니다");
            return true;
          }
          if (radius < 1) {
            Message.error(sender, "최소 반지름 값은 1 입니다");
            return true;
          }
        }

        List<Location> area = Area.SPHERE.getArea(pos1, radius);
        switch (args[0]) {
          case "pnsphere", "pointspherenear" -> {
            area = Area.SPHERE_POINTED.getArea(pos1, radius);
          }
          case "ensphere", "emptyspherenear" -> {
            area = Area.SPHERE_EMPTY.getArea(pos1, radius);
          }
          case "epnsphere", "emptypointspherenear" -> {
            area = Area.SPHERE_POINTED_EMPTY.getArea(pos1, radius);
          }
          case "wnsphere", "wpnsphere", "walledspherenear", "walledpointspherenear" -> {
            Message.warn(sender, "미완성 기능입니다");
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
          Message.send(sender, Message.parse(Message.effect(Wand.PREFIX), Message.parse(blockData.getMaterial()).color(TextColor.fromHexString(Wand.COLORHEX)), Message.effect("&r을(를) " + Wand.COLOR + area.size() + "&r개 설치하였습니다")));
        }

        wand.fill(blockData, area, applyPhysics);

        return true;
      }

      /*
       * 벽
       */

      // 선택 좌표를 가로지르는 벽 설치
      case "wall" -> {
        if (!sender.hasPermission("cherry.wand.edit.wall")) {
          Message.error(sender, Message.CommandFeedback.NO_PERMISSION);
          return true;
        }

        if (args.length <= 1) {
          Message.error(sender, Message.CommandFeedback.NO_ARGS);
          Message.info(sender, "사용법: " + command.getLabel() + " " + args[0] + " <블록> [옵션...]");
          return true;
        }

        // area
        Location pos1 = wand.getEdit().getPosition(1);
        Location pos2 = wand.getEdit().getPosition(2);
        if (pos1 == null || pos2 == null) {
          Message.error(sender, "포지션이 지정되지 않았습니다");
          return true;
        }
        List<Location> area = Area.WALL.getArea(pos1, pos2);

        // blockdata
        String blockDataString = args[1];
        BlockData blockData;
        try {
          blockData = Wand.getBlockData(blockDataString);
        }
        catch (Exception e) {
          Message.error(sender, "블록 데이터 파싱 에러: " + e.getMessage());
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
          Message.send(sender, Message.parse(Message.effect(Wand.PREFIX), Message.parse(blockData.getMaterial()).color(TextColor.fromHexString(Wand.COLORHEX)), Message.effect("&r을(를) " + Wand.COLOR + area.size() + "&r개 설치하였습니다")));
        }

        wand.fill(blockData, area, applyPhysics);

        return true;
      }

    }

    Message.error(sender, Message.CommandFeedback.UNKNOWN);
    return true;

  }

  private int verifyInteger(String name, int max, int min, String input) throws Exception {
    int i;
    try {
      i = Integer.parseInt(input);
    }
    catch (Exception e) {
      if (DataTypeChecker.isInteger(input)) {
        throw new Exception("프로그램 상 사용할 수 없는 범위의 수입니다");
      }
      else {
        throw new Exception(name + " 값은 정수만 입력할 수 있습니다");
      }
    }
    if (i > max) {
      throw new Exception("최대 " + name + " 값은 " + max + " 입니다");
    }
    if (i < min) {
      throw new Exception("최소 " + name + " 값은 " + min + " 입니다");
    }
    return i;
  }

}
