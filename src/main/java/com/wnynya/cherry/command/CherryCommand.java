package com.wnynya.cherry.command;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Config;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.Updater;
import com.wnynya.cherry.amethyst.PluginLoader;
import com.wnynya.cherry.gui.CherryMenu;
import com.wnynya.cherry.wand.Wand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CherryCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (args.length == 0) {
      Msg.error(sender, Msg.NO_ARGS);
      return true;
    }

    // 플러그인 정보
    if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i")) {
      sender.sendMessage("");
      TextComponent msg1 = new TextComponent("§rWnynya");
      msg1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§d클릭하여 링크 열기").create()));
      msg1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://wnynya.com"));
      sender.sendMessage(new TextComponent("§5§lCherry§r by "), msg1);
      sender.sendMessage("");
      sender.sendMessage( Msg.n2s("&rv&d" + Cherry.getPlugin().getDescription().getVersion()));
      sender.sendMessage("");
      TextComponent msg2 = new TextComponent("§rcherry.wnynya.com");
      msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§d클릭하여 링크 열기").create()));
      msg2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://cherry.wnynya.com"));
      sender.sendMessage(msg2);
      sender.sendMessage("");
      return true;
    }

    // 플러그인 버전
    if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("v")) {
      Msg.info(sender, Msg.Prefix.CHERRY + Msg.n2s("&rv&d" + Cherry.getPlugin().getDescription().getVersion()));
      return true;
    }

    // 플러그인 리로드
    if (args[0].equalsIgnoreCase("reload")) {
      if (!sender.hasPermission("cherry.reload.all")) {
        Msg.error(sender, Msg.NO_PERMISSION);
      }
      PluginLoader.unload();
      PluginLoader.load();
      Msg.info(sender, Msg.Prefix.CHERRY + "Plugin has been reloaded.");
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
      ExecutorService cmdExecutorService = Executors.newFixedThreadPool(10);

      cmdExecutorService.submit(new Runnable() {
        @Override
        public void run() {

          if (!sender.hasPermission("cherry.update")) {
            Msg.error(sender, Msg.NO_PERMISSION);
          }

          Updater.VersionInfo vi = Updater.checkCherry();

          if (args.length >= 2 && args[1].equalsIgnoreCase("confirm") && Updater.checked && vi.getState().equals(Updater.VersionInfo.State.OUTDATED)) {
            Msg.info(sender, Msg.Prefix.CHERRY, "플러그인을 업데이트합니다...");

            try {
              Updater.updateCherry(vi.getVersion());
            }
            catch (Exception e) {
              e.printStackTrace();
              Msg.error(sender, Msg.Prefix.CHERRY, "플러그인 업데이트 중 오류가 발생하였습니다.");
              return;
            }

            Msg.info(sender, Msg.Prefix.CHERRY, "플러그인을 성공적으로 업데이트하였습니다.");

            return;
          }

          if (args.length >= 2 && args[1].equalsIgnoreCase("force")) {

            Msg.info(sender, Msg.Prefix.CHERRY, "플러그인을 강제로 업데이트합니다...");

            if (vi.getState().equals(Updater.VersionInfo.State.ERROR)) {

              Msg.error(sender, Msg.Prefix.CHERRY, "플러그인 버전 확인 중 오류가 발생하였습니다.");
              return;

            }

            try {
              Updater.updateCherry(vi.getVersion());
            }
            catch (Exception e) {
              e.printStackTrace();
              Msg.error(sender, Msg.Prefix.CHERRY, "플러그인 업데이트 중 오류가 발생하였습니다.");
              return;
            }

            Msg.info(sender, Msg.Prefix.CHERRY, "플러그인을 성공적으로 업데이트하였습니다.");

            return;

          }

          if (vi.getState().equals(Updater.VersionInfo.State.LATEST)) {

            Msg.info(sender, Msg.Prefix.CHERRY, "플러그인이 최신 버전입니다. (&d" + Cherry.getPlugin().getDescription().getVersion() + "&r)");
            return;

          }

          else if (vi.getState().equals(Updater.VersionInfo.State.OUTDATED)) {

            Updater.checked = true;
            Msg.info(sender, Msg.Prefix.CHERRY, "플러그인이 최신 버전이 아닙니다. 현재 버전: &d" + Cherry.getPlugin().getDescription().getVersion() + "&r | 최신 버전: &a" + vi.getVersion() + "&r");
            Msg.info(sender, Msg.Prefix.CHERRY, "플러그인을 업데이트하려면 /cherry update confirm 명령어를 실행하십시오.");
            return;

          }

          else if (vi.getState().equals(Updater.VersionInfo.State.ERROR)) {

            Msg.error(sender, Msg.Prefix.CHERRY, "플러그인 버전 확인 중 오류가 발생하였습니다.");
            return;

          }

        }
      });

      cmdExecutorService.shutdown();

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
        Msg.error(sender, Msg.PlayerMSg.ONLY);
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
        Msg.error(sender, Msg.PlayerMSg.ONLY);
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
