package com.wnynya.cherry.command;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Config;
import com.wnynya.cherry.Updater;
import com.wnynya.cherry.gui.CherryMenu;
import com.wnynya.cherry.wand.Wand;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.wnynya.cherry.Msg;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CherryCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (args.length == 0) {
      Msg.error(sender, Msg.NO_ARGS);
      return true;
    }

    // 플러그인 정보
    if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i")) {
      Msg.info(sender, Msg.n2s(Msg.Prefix.CHERRY + "&7Cherry v&d" + Cherry.getPlugin().getDescription().getVersion() + "&7 by Wnynya"));
      return true;
    }

    // 플러그인 버전
    if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("v")) {
      Msg.info(sender, Msg.n2s(Msg.Prefix.CHERRY + "&7Cherry v&d" + Cherry.getPlugin().getDescription().getVersion() + "&7 by Wnynya"));
      return true;
    }

    // 플러그인 리로드
    if (args[0].equalsIgnoreCase("reload")) {
      if (!sender.hasPermission("cherry.reload.all")) {
        Msg.error(sender, Msg.NO_PERMISSION);
      }
      Cherry.unload();
      Cherry.load();
      Msg.info(sender, Msg.Prefix.CHERRY + "플러그인을 리로드하였습니다.");
      return true;
    }

    // 플러그인 설정 리로드
    if (args[0].equalsIgnoreCase("reloadConfig")) {
      if (!sender.hasPermission("cherry.reload.config")) {
        Msg.error(sender, Msg.NO_PERMISSION);
      }
      Cherry.getPlugin().reloadConfig();
      Cherry.config = Cherry.getPlugin().getConfig();
      Msg.load();
      Wand.load();

      Msg.info(sender, Msg.Prefix.CHERRY + "플러그인의 설정 파일을 리로드하였습니다.");
      return true;
    }

    // 플러그인 업데이트
    if (args[0].equalsIgnoreCase("update")) {
      if (!sender.hasPermission("cherry.update")) {
        Msg.error(sender, Msg.NO_PERMISSION);
      }

      Updater.VersionInfo vi = Updater.checkCherry();

      if (args.length >= 2 && args[1].equalsIgnoreCase("confirm")) {
        if (vi.getState().equals(Updater.VersionInfo.State.OUTDATED)) {
          Msg.info(sender, Msg.Prefix.CHERRY + "플러그인을 업데이트합니다...");

          try {
            Updater.updateCherry(vi.getVersion());
          }
          catch (Exception e) {
            e.printStackTrace();
            Msg.error(sender, "플러그인 업데이트 중 오류가 발생하였습니다.");
          }

          Msg.info(sender, Msg.Prefix.CHERRY + "플러그인을 성공적으로 업데이트하였습니다.");

          return true;
        }
      }

      if (args.length >= 2 && args[1].equalsIgnoreCase("force")) {

        Msg.info(sender, Msg.Prefix.CHERRY + "플러그인을 강제로 업데이트합니다...");

        try {
          Updater.updateCherry(vi.getVersion());
        }
        catch (Exception e) {
          e.printStackTrace();
          Msg.error(sender, "플러그인 업데이트 중 오류가 발생하였습니다.");
        }

        Msg.info(sender, Msg.Prefix.CHERRY + "플러그인을 성공적으로 업데이트하였습니다.");

        return true;
      }

      if (vi.getState().equals(Updater.VersionInfo.State.LATEST)) {
        Msg.info(sender, Msg.Prefix.CHERRY + "플러그인이 최신 버전입니다.");
        return true;
      }
      else if (vi.getState().equals(Updater.VersionInfo.State.OUTDATED)) {
        Msg.info(sender, Msg.Prefix.CHERRY + "플러그인이 최신 버전이 아닙니다. 현재 버전: "
          + Cherry.getPlugin().getDescription().getVersion()
          + " | 최신 버전: " + vi.getVersion());
        Msg.info(sender, Msg.Prefix.CHERRY + "플러그인을 업데이트하려면 /cherry update confirm 명령어를 실행하십시오.");
        return true;
      }
      else if (vi.getState().equals(Updater.VersionInfo.State.ERROR)) {
        Msg.error(sender, "플러그인 버전 확인 중 오류가 발생하였습니다.");
        return true;
      }

      return true;
    }

    // 메뉴
    if (args[0].equalsIgnoreCase("menu")) {
      if (!sender.hasPermission("cherry.menu")) {
        Msg.error(sender, Msg.NO_PERMISSION);
      }
      Player player = null;
      if (sender instanceof Player) {
        player = (Player) sender;
      }
      else {
        Msg.error(sender, Msg.Player.ONLY);
        return true;
      }

      CherryMenu.MainMenu.showMenu(player);
      return true;
    }

    // 스폰 설정
    if (args[0].equalsIgnoreCase("setspawn")) {
      if (!sender.hasPermission("cherry.setspawn")) {
        Msg.error(sender, Msg.NO_PERMISSION);
      }
      Player player = null;
      if (sender instanceof Player) {
        player = (Player) sender;
      }
      else {
        Msg.error(sender, Msg.Player.ONLY);
        return true;
      }

      Config cherryConfig = new Config("cherry", true);

      cherryConfig.set("spawn.location", player.getLocation());
      player.getWorld().setSpawnLocation(player.getLocation());
      Msg.info(player, "서버의 스폰 지점을 지정하였습니다.");
      return true;
    }

    // Boooooooooom!
    if (args[0].equalsIgnoreCase("bomb")) {
      if (sender instanceof BlockCommandSender) {
        return true;
      }
      if (!sender.hasPermission("cherry.bomb")) {
        Msg.error(sender, Msg.UNKNOWN);
        return true;
      }
      if (args.length <= 1) {
        Msg.error(sender, Msg.UNKNOWN);
        return true;
      }
      if (!Cherry.config.getString("updater.type").equals("dev")) {
        Msg.warn(sender, "정말로 체리-밤을 터뜨리려면 /cherry bomb confirm [인증 코드] 를 입력하십시오.");
        return true;
      }
      int i = 0;
      String key = args[1];
      if (key.equals("fuck")) {
        try {
          for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer(Msg.n2s("&c&lBoooooooooom!"));
          }
          Cherry.boom(i);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
      Msg.error(sender, Msg.UNKNOWN);
      return true;
    }

    Msg.error(sender, Msg.UNKNOWN);
    return true;
  }
}
