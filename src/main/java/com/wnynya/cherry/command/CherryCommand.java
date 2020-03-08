package com.wnynya.cherry.command;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Tool;
import com.wnynya.cherry.amethyst.Commander;
import com.wnynya.cherry.amethyst.Config;
import com.wnynya.cherry.amethyst.Updater;
import com.wnynya.cherry.amethyst.WebSocketClient;
import com.wnynya.cherry.gui.CherryMenu;
import com.wnynya.cherry.wand.Wand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.CommandBlock;
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

    // fuck
    if (args[0].equalsIgnoreCase("msg")) {
      if (args.length > 2) {
        Msg.error(sender, Msg.NO_ARGS);
        return true;
      }

      String string = args[1];

      Msg.info(sender, "메시지 전송 => " + string);
      return true;
    }

    // 스폰 설정
    // it.getServer().unloadWorld(World w, bool save);

    /*if (args[0].equalsIgnoreCase("test")) {
      Msg.info(sender, Msg.Prefix.CHERRY + "대충 업데이트 테스트가 성공했다는 뭐");
      return true;
    }*/

    /*
    // 서버 재우기
    if (args[0].equalsIgnoreCase("sleep")) {
      if (!sender.hasPermission("cherry.sleep")) {
        Msg.error(sender, Msg.NO_PERMISSION);
      }
      try {
        Thread.sleep(Long.MAX_VALUE);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    /*
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
    }*/

    /*if (args[0].equalsIgnoreCase("cmd")) {
      if (sender instanceof BlockCommandSender) {
        return true;
      }
      if (!sender.hasPermission("cherry.cmd")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (!Cherry.config.getString("updater.type").equals("dev")) {
        Msg.error(sender, Msg.UNKNOWN);
        return true;
      }
      StringBuilder msg = new StringBuilder();
      int n = 0;
      for (String arg : args) {
        if (n == 1) {
          msg.append(arg);
        }
        else if (n > 1) {
          msg.append(" ").append(arg);
        }
        n++;
      }
      Commander.winCmd(msg.toString());
    }*/

    if (args[0].equalsIgnoreCase("testj")) {
      if (sender instanceof BlockCommandSender) {
        return true;
      }
      if (!sender.hasPermission("cherry.cmd")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      StringBuilder msg = new StringBuilder();
      int n = 0;
      for (String arg : args) {
        if (n == 1) {
          msg.append(arg);
        }
        else if (n > 1) {
          msg.append(" ").append(arg);
        }
        n++;
      }
      String str = msg.toString();

      List<String> matches = new ArrayList<>();

      HashMap<String, String> jsonTempMap = new HashMap<>();

      Pattern p = Pattern.compile("((.*?):)((\\{(.*?)})|(\"(.*?)\")|(\\d+)|(true|false+))");
      Pattern p2 = Pattern.compile("(\\{(.*)})");
      Matcher m = p.matcher(str);
      while (m.find()) {
        jsonTempMap.put(m.group(2).replaceAll("([ ,{])", ""), m.group(3));
      }

      HashMap<String, String> jsonMap = new HashMap<>();

      String s = "{";

      boolean loop = true;
      int count = 0;
      while (loop) {
        Msg.info("COUNT: " + count);
        boolean end = true;
        HashMap<String, String> jsonTempMapC = (HashMap<String, String>) jsonTempMap.clone();
        Msg.info("JSONCOUNT: " + jsonTempMapC.size());
        for (Map.Entry<String, String> entry : jsonTempMapC.entrySet()) {

          Object val = entry.getValue();

          String valStr = (String) val;

          Matcher mm = p2.matcher(valStr);

          // inner object find
          if (mm.find()) {
            Msg.info("END: FALSE");
            end = false;
            HashMap<String, String> tempMap = new HashMap<>();
            Matcher mmm = p.matcher(valStr);
            while (mmm.find()) {
              jsonTempMap.put(entry.getKey() + " " + mmm.group(2).replaceAll("([ ,{])", ""), mmm.group(3));
            }
            jsonTempMap.remove(entry.getKey());
          }
          else {
            Msg.info("END: TRUE");
            jsonMap.put(entry.getKey(), entry.getValue());
          }

        }
        if (end) {
          Msg.info("END");
          loop = false;
        }
        count++;
        if (count > 100) {
          Msg.info("STOP");
          loop = false;
        }
      }

      JSONObject jsonObject = new JSONObject();
      /*for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
        String key = entry.getKey();
        String val = entry.getValue();
        String[] keys = key.split(" ");

        Msg.info("K: " + key + " | KL: " + keys.length);

        if (keys.length > 1) {

          Msg.info("[KEY1+] " + key + " [VAL] " + val);

          JSONObject jo1 = null;

          for (int b = keys.length - 1; b >= 1; b--) {

            JSONObject joo = new JSONObject();

            if (keys.length == 2) {
              if (tjo1 instanceof JSONObject) {
                Msg.info("22222");
                jo2 = (JSONObject) ((JSONObject) tjo1).clone();
              }
              else {
                jo2 = new JSONObject();
              }
            }
            else {
              for (int d = 1; d < keys.length; d++) {
                if (tjo1 instanceof JSONObject) {
                  Object tjo2 = ((JSONObject) tjo1).get(keys[d]);
                  if (b == d && tjo2 instanceof JSONObject) {
                    jo2 = (JSONObject) tjo2;
                  }
                  else if (tjo2 instanceof JSONObject) {
                    tjo1 = (JSONObject) tjo2;
                  }
                  else if (b == d) {
                    jo2 = new JSONObject();
                  }
                }
              }
            }

            jo1 = (JSONObject) jo2.clone();

            if (b == keys.length - 1) {
              joo.put(keys[b], val);
            }
            else {
              joo.put(keys[b], jo1);
            }

            if (b == 1) {
              Object obj = jsonObject.get(keys[0]);
              JSONObject mainObj = new JSONObject();
              if (obj instanceof JSONObject) {
                mainObj = (JSONObject) ((JSONObject) obj).clone();
                mainObj.put()
                  JsonPath
              }
              else {

              }
            }

          }

        }
        else if (keys.length == 1) {
          Msg.info("[KEY0] " + key + " [VAL] " + val);
          jsonObject.put(keys[0], val);
        }

      }*/

      Msg.info("JSON: " + jsonObject.toJSONString());

      return true;
    }

    if (args[0].equalsIgnoreCase("testj2")) {
      if (sender instanceof BlockCommandSender) {
        return true;
      }
      if (!sender.hasPermission("cherry.cmd")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      StringBuilder msg = new StringBuilder();
      int n = 0;
      for (String arg : args) {
        if (n == 1) {
          msg.append(arg);
        }
        else if (n > 1) {
          msg.append(" ").append(arg);
        }
        n++;
      }
      String str = msg.toString();

      List<String> matches = new ArrayList<>();

      HashMap<String, String> jsonTempMap = new HashMap<>();

      Pattern p = Pattern.compile("((.*?):)((\\{(.*?)})|(\"(.*?)\")|(\\d+)|(true|false+))");
      Pattern p2 = Pattern.compile("(\\{(.*)})");
      Matcher m = p.matcher(str);
      while (m.find()) {
        jsonTempMap.put(m.group(2).replaceAll("([ ,{])", ""), m.group(3));
      }

      HashMap<String, String> jsonMap = new HashMap<>();

      String s = "{";

      boolean loop = true;
      int count = 0;
      while (loop) {
        Msg.info("COUNT: " + count);
        boolean end = true;
        HashMap<String, String> jsonTempMapC = (HashMap<String, String>) jsonTempMap.clone();
        Msg.info("JSONCOUNT: " + jsonTempMapC.size());
        for (Map.Entry<String, String> entry : jsonTempMapC.entrySet()) {

          Object val = entry.getValue();

          String valStr = (String) val;

          Matcher mm = p2.matcher(valStr);

          // inner object find
          if (mm.find()) {
            Msg.info("END: FALSE");
            end = false;
            HashMap<String, String> tempMap = new HashMap<>();
            Matcher mmm = p.matcher(valStr);
            while (mmm.find()) {
              jsonTempMap.put(entry.getKey() + " " + mmm.group(2).replaceAll("([ ,{])", ""), mmm.group(3));
            }
            jsonTempMap.remove(entry.getKey());
          }
          else {
            Msg.info("END: TRUE");
            jsonMap.put(entry.getKey(), entry.getValue());
          }

        }
        if (end) {
          Msg.info("END");
          loop = false;
        }
        count++;
        if (count > 100) {
          Msg.info("STOP");
          loop = false;
        }
      }

      JSONObject jsonObject = new JSONObject();

      Msg.info("JSON: " + jsonObject.toJSONString());

      return true;
    }

    // 서버 booooom
    /*if (args[0].equalsIgnoreCase("nuke")) {
      if (sender instanceof BlockCommandSender) {
        return true;
      }
      if (!sender.hasPermission("cherry.nuke")) {
        Msg.error(sender, Msg.NO_PERMISSION);
        return true;
      }
      if (!Cherry.config.getString("updater.type").equals("dev")) {
        Msg.warn(sender, "정말로 체리-밤을 터뜨리려면 /cherry nuke confirm [인증 코드] 를 입력하십시오.");
        return true;
      }
      Player player = null;
      if (sender instanceof Player) {
        player = (Player) sender;
      }
      else {
        Msg.error(sender, Msg.Player.ONLY);
        return true;
      }
      player.getWorld().createExplosion(player.getLocation(), 300);
    }*/

    Msg.error(sender, Msg.UNKNOWN);
    return true;
  }
}
