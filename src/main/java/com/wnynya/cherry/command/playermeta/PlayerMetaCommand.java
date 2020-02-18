package com.wnynya.cherry.command.playermeta;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.player.PlayerMeta;
import com.wnynya.cherry.wand.Wand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class PlayerMetaCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (args.length == 0) {
      Msg.error(sender, Msg.NO_ARGS);
      return true;
    }

    Player player = Bukkit.getPlayer(args[0]);
    if (player == null) {
      Msg.error(sender, "플레이어를 찾을 수 없습니다.");
      return true;
    }

    PlayerMeta pm = PlayerMeta.getPlayerMeta(player);

    if (args.length <= 1) {
      Msg.error(player, Msg.NO_ARGS);
      return true;
    }

    // wand
    if (args[1].equalsIgnoreCase("wand")) {
      PlayerMeta.FunctionData fd = pm.getFunction(PlayerMeta.Function.WAND);
      if (args.length == 2 || args[2] == null) {
        if (fd.isEnable()) { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s(Msg.Prefix.WAND + "&a활성화됨")); }
        else { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s(Msg.Prefix.WAND + "&c비활성화됨")); }
        JSONObject data = fd.getData();
        if (data == null) { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s("Data: 없음")); }
        else { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s("Data: " + data.toJSONString())); }
        return true;
      }

      if (args[2].equalsIgnoreCase("enable")) {
        fd.enable();
        pm.getConfig().set("function.wand.enable", true);
        Msg.info(player, Msg.Prefix.CHERRY + "완드가 활성화되었습니다.");
        return true;
      }
      else if (args[2].equalsIgnoreCase("disable")) {
        fd.disable();
        pm.getConfig().set("function.wand.enable", false);
        Msg.info(player, Msg.Prefix.CHERRY + "완드가 비활성화되었습니다.");
        return true;
      }

      else if (args[2].equalsIgnoreCase("set")) {
        if (args.length <= 4) {
          Msg.error(player, Msg.NO_ARGS);
          return true;
        }
        String s = "";
        for (int n = 4; n < args.length; n++) {
          s = args[n];
          if (n != args.length - 1) {
            s += " ";
          }
        }

        if (fd.setData(args[3], s, false)) {
          pm.getConfig().set("function.wand.data", fd.getData().toJSONString());
          Msg.info(player, Msg.Prefix.CHERRY + "완드의 개인 설정값을 변경하였습니다.");
        }
        else {
          Msg.error(player, "없는 설정값입니다.");
        }
      }
    }

    // portal
    if (args[1].equalsIgnoreCase("portal")) {
      PlayerMeta.FunctionData fd = pm.getFunction(PlayerMeta.Function.PORTAL);
      if (args.length == 2 || args[2] == null) {
        if (fd.isEnable()) { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s(Msg.Prefix.PORTAL + "&a활성화됨")); }
        else { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s(Msg.Prefix.PORTAL + "&c비활성화됨")); }
        JSONObject data = fd.getData();
        if (data == null) { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s("Data: 없음")); }
        else { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s("Data: " + data.toJSONString())); }
        return true;
      }
      if (args[2].equalsIgnoreCase("enable")) {
        fd.enable();
        pm.getConfig().set("function.portal.enable", true);
        Msg.info(player, Msg.Prefix.CHERRY + "기능 사용이 활성화되었습니다.");
        return true;
      }
      else if (args[2].equalsIgnoreCase("disable")) {
        fd.disable();
        pm.getConfig().set("function.portal.enable", false);
        Msg.info(player, Msg.Prefix.CHERRY + "기능 사용이 비활성화되었습니다.");
        return true;
      }

      else if (args[2].equalsIgnoreCase("set")) {
        if (args.length <= 4) {
          Msg.error(player, Msg.NO_ARGS);
          return true;
        }
        String s = "";
        for (int n = 4; n < args.length; n++) {
          s = args[n];
          if (n != args.length - 1) {
            s += " ";
          }
        }

        if (fd.setData(args[3], s, false)) {
          pm.getConfig().set("function.portal.data", fd.getData().toJSONString());
          Msg.info(player, Msg.Prefix.CHERRY + "개인 설정값을 변경하였습니다. (" + args[3] + ": " + s + ")") ;
        }
        else {
          Msg.error(player, "없는 설정값입니다.");
        }
      }
    }

    // nickname
    else if (args[1].equalsIgnoreCase("nickname")) {
      PlayerMeta.FunctionData fd = pm.getFunction(PlayerMeta.Function.NICKNAME);
      if (args.length == 2 || args[2] == null) {
        if (fd.isEnable()) { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s("닉네임: &a활성화됨")); }
        else { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s( "닉네임: &c비활성화됨")); }
        JSONObject data = fd.getData();
        if (data == null) { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s("Data: 없음")); }
        else { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s("Data: " + data.toJSONString())); }
        return true;
      }
      if (args[2].equalsIgnoreCase("enable")) {
        fd.enable();
        pm.getConfig().set("function.nickname.enable", true);
        Msg.info(player, Msg.Prefix.CHERRY + "기능 사용이 활성화되었습니다.");
        return true;
      }
      else if (args[2].equalsIgnoreCase("disable")) {
        fd.disable();
        pm.getConfig().set("function.nickname.enable", false);
        Msg.info(player, Msg.Prefix.CHERRY + "기능 사용이 비활성화되었습니다.");
        return true;
      }

      else if (args[2].equalsIgnoreCase("set")) {
        if (args.length <= 4) {
          Msg.error(player, Msg.NO_ARGS);
          return true;
        }
        String s = "";
        for (int n = 4; n < args.length; n++) {
          s = args[n];
          if (n != args.length - 1) {
            s += " ";
          }
        }

        if (fd.setData(args[3], s, false)) {
          pm.getConfig().set("function.nickname.data", fd.getData().toJSONString());
          Msg.info(player, Msg.Prefix.CHERRY + "개인 설정값을 변경하였습니다. (" + args[3] + ": " + s + ")") ;
        }
        else {
          Msg.error(player, "없는 설정값입니다.");
        }
      }
    }

    // notetool
    else if (args[1].equalsIgnoreCase("notetool")) {
      PlayerMeta.FunctionData fd = pm.getFunction(PlayerMeta.Function.NOTETOOL);
      if (args.length == 2 || args[2] == null) {
        if (fd.isEnable()) { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s("노트블럭 도구: &a활성화됨")); }
        else { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s( "노트블럭 도구: &c비활성화됨")); }
        JSONObject data = fd.getData();
        if (data == null) { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s("Data: 없음")); }
        else { Msg.info(player, Msg.Prefix.CHERRY + Msg.n2s("Data: " + data.toJSONString())); }
        return true;
      }
      if (args[2].equalsIgnoreCase("enable")) {
        fd.enable();
        pm.getConfig().set("function.notetool.enable", true);
        Msg.info(player, Msg.Prefix.CHERRY + "기능 사용이 활성화되었습니다.");
        return true;
      }
      else if (args[2].equalsIgnoreCase("disable")) {
        fd.disable();
        pm.getConfig().set("function.notetool.enable", false);
        Msg.info(player, Msg.Prefix.CHERRY + "기능 사용이 비활성화되었습니다.");
        return true;
      }

      else if (args[2].equalsIgnoreCase("set")) {
        if (args.length <= 4) {
          Msg.error(player, Msg.NO_ARGS);
          return true;
        }
        String s = "";
        for (int n = 4; n < args.length; n++) {
          s = args[n];
          if (n != args.length - 1) {
            s += " ";
          }
        }

        if (fd.setData(args[3], s, false)) {
          pm.getConfig().set("function.notetool.data", fd.getData().toJSONString());
          Msg.info(player, Msg.Prefix.CHERRY + "개인 설정값을 변경하였습니다. (" + args[3] + ": " + s + ")") ;
        }
        else {
          Msg.error(player, "없는 설정값입니다.");
        }
      }
    }

    return true;
  }

}
