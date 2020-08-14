package com.wnynya.cherry.command;

import com.sun.management.OperatingSystemMXBean;
import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Config;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.Updater;
import com.wnynya.cherry.amethyst.PluginLoader;
import com.wnynya.cherry.gui.CherryMenu;
import com.wnynya.cherry.wand.Wand;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Banner;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CherryCommand implements CommandExecutor {

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (args.length == 0) {
      Msg.error(sender, Msg.NO_ARGS);
      return true;
    }

    switch (args[0].toLowerCase()) {

      // 플러그인 정보
      case "i": {}
      case "info": {
        sender.sendMessage("");
        /*TextComponent msg1 = new TextComponent("§rWnynya");
        msg1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§d클릭하여 링크 열기").create()));
        msg1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://wnynya.com"));*/
        sender.sendMessage("#D2B0DD;§lCherry§r by " + "§rWnynya");
        sender.sendMessage("");
        sender.sendMessage( Msg.effect("&rv&d" + Cherry.plugin.getDescription().getVersion()));
        sender.sendMessage("");
        /*TextComponent msg2 = new TextComponent("§rcherry.wnynya.com");
        msg2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§d클릭하여 링크 열기").create()));
        msg2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://cherry.wnynya.com"));*/
        sender.sendMessage("§rcherry.wnynya.com");
        sender.sendMessage("");
        return true;
      }

      // 플러그인 버전
      case "v": {}
      case "version": {
        Msg.info(sender, Msg.Prefix.CHERRY + Msg.effect("&rv#D2B0DD;" + Cherry.plugin.getDescription().getVersion()));
        return true;
      }

      // 플러그인 리로드
      case "reload": {
        if (!sender.hasPermission("cherry.reload.all")) {
          Msg.error(sender, Msg.NO_PERMISSION);
        }
        PluginLoader.unload();
        PluginLoader.load();
        Msg.info(sender, Msg.Prefix.CHERRY + "Plugin has been reloaded.");
        return true;
      }

      // 플러그인 설정 리로드
      case "reloadconfig": {
        if (!sender.hasPermission("cherry.reload.config")) {
          Msg.error(sender, Msg.NO_PERMISSION);
        }
        Cherry.plugin.reloadConfig();
        Cherry.config = Cherry.plugin.getConfig();
        Msg.load();
        Wand.load();

        Msg.info(sender, Msg.Prefix.CHERRY + "플러그인의 설정 파일을 리로드하였습니다.");
        return true;
      }

      // 플러그인 업데이트
      case "update": {
        ExecutorService cmdExecutorService = Executors.newFixedThreadPool(10);

        cmdExecutorService.submit(new Runnable() {
          @Override
          public void run() {

            if (!sender.hasPermission("cherry.update")) {
              Msg.error(sender, Msg.NO_PERMISSION);
            }

            Updater.VersionInfo vi = Updater.checkPlugin();

            if (args.length >= 2 && args[1].equalsIgnoreCase("confirm") && Updater.checked && vi.getState().equals(Updater.VersionInfo.State.OUTDATED)) {
              Msg.info(sender, Msg.Prefix.CHERRY, "플러그인을 업데이트합니다...");

              try {
                Updater.updatePlugin(vi.getVersion());
              }
              catch (Exception e) {
                e.printStackTrace();
                Msg.error(sender, Msg.Prefix.CHERRY, "플러그인 업데이트 중 오류가 발생하였습니다.");
                return;
              }

              Msg.info(sender, Msg.Prefix.CHERRY, "플러그인 업데이트가 완료되었습니다.");

              return;
            }

            if (args.length >= 2 && args[1].equalsIgnoreCase("force")) {

              Msg.info(sender, Msg.Prefix.CHERRY, "플러그인을 강제로 업데이트합니다...");

              if (vi.getState().equals(Updater.VersionInfo.State.ERROR)) {

                Msg.error(sender, Msg.Prefix.CHERRY, "플러그인 버전 확인 중 오류가 발생하였습니다.");
                return;

              }

              try {
                Updater.updatePlugin(vi.getVersion());
              }
              catch (Exception e) {
                e.printStackTrace();
                Msg.error(sender, Msg.Prefix.CHERRY, "플러그인 업데이트 중 오류가 발생하였습니다.");
                return;
              }

              Msg.info(sender, Msg.Prefix.CHERRY, "플러그인 업데이트가 완료되었습니다.");

              return;

            }

            if (vi.getState().equals(Updater.VersionInfo.State.LATEST)) {

              Msg.info(sender, Msg.Prefix.CHERRY, "플러그인이 최신 버전입니다. (&d" + Cherry.plugin.getDescription().getVersion() + "&r)");
              return;

            }

            else if (vi.getState().equals(Updater.VersionInfo.State.OUTDATED)) {

              Updater.checked = true;
              Msg.info(sender, Msg.Prefix.CHERRY, "플러그인이 최신 버전이 아닙니다. 현재 버전: &d" + Cherry.plugin.getDescription().getVersion() + "&r | 최신 버전: &a" + vi.getVersion() + "&r");
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
      case "menu": {
        if (!sender.hasPermission("cherry.menu")) {
          Msg.error(sender, Msg.NO_PERMISSION);
          return true;
        }
        org.bukkit.entity.Player player = null;
        if (sender instanceof org.bukkit.entity.Player) {
          player = (org.bukkit.entity.Player) sender;
        }
        else {
          Msg.error(sender, Msg.Player.ONLY);
          return true;
        }

        CherryMenu.MainMenu.showMenu(player);
        return true;
      }

      // 시스템
      case "system": {
        if (!sender.hasPermission("cherry.system")) {
          Msg.error(sender, Msg.NO_PERMISSION);
          return true;
        }

        if (args.length <= 1) {
          Msg.error(sender, Msg.NO_ARGS);
          Msg.info(sender, "");
          return true;
        }

        if (args[1].equalsIgnoreCase("info")) {
          Runtime r = Runtime.getRuntime();
          //com.sun.management.OperatingSystemMXBean osb = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
          OperatingSystemMXBean osb = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

          Msg.info(sender, Msg.effect(""));
          Msg.info(sender, Msg.effect("#D2B0DD;&lSystem Information"));
          Msg.info(sender, Msg.effect("#9BFFDE;  OS&7: #FFF593;" + System.getProperty("os.name") + " " + System.getProperty("os.arch") + "&f (#FFF593;" + System.getProperty("os.version") + "&f)"));
          Msg.info(sender, Msg.effect("#9BFFDE;  Java&7: #FFF593;" + System.getProperty("java.version")));
          Msg.info(sender, Msg.effect("#9BFFDE;  Processor&7:"));
          Msg.info(sender, Msg.effect("&f    Cores&7: #FFF593;" + r.availableProcessors()));
          Msg.info(sender, Msg.effect("&f    Usage&7:"));
          Msg.info(sender, Msg.effect("&f      Server&7: #FFF593;" + Math.round(osb.getProcessCpuLoad() * 10000) / 100.0 + "&6%"));
          Msg.info(sender, Msg.effect("&f      System&7: #FFF593;" + Math.round(osb.getSystemCpuLoad() * 10000) / 100.0 + "&6%"));
          Msg.info(sender, Msg.effect("#9BFFDE;  Memory&7: "));
          Msg.info(sender, Msg.effect("&f    Usage&7:"));
          long svm = r.maxMemory() / (1024 * 1024);
          long svu = (r.totalMemory() - r.freeMemory()) / (1024 * 1024);
          Msg.info(sender, Msg.effect("&f      Server&7: #FFF593;" + svu + "#FFC393;M &f/ #FFF593;" + svm + "#FFC393;M"));
          Msg.info(sender, Msg.effect("&f        Total&7: #FFF593;" + r.totalMemory() / (1024 * 1024) + "#FFC393;M"));
          Msg.info(sender, Msg.effect("&f        Free&7: #FFF593;" + r.freeMemory() / (1024 * 1024) + "#FFC393;M"));
          long stm = osb.getTotalPhysicalMemorySize() / (1024 * 1024);
          long stu = (osb.getTotalPhysicalMemorySize() - osb.getFreePhysicalMemorySize()) / (1024 * 1024);
          Msg.info(sender, Msg.effect("&f      System&7: #FFF593;" + stu + "#FFC393;M &f/ #FFF593;" + stm + "#FFC393;M &f(#FFF593;" + Math.round(((stu) / 1024.0) * 100) / 100.0 + "#FFC393;G &f/ #FFF593;" + Math.round((stm / 1024.0) * 100) / 100.0 + "#FFC393;G&f)"));
          Msg.info(sender, Msg.effect("#9BFFDE;  Server&7: "));
          Msg.info(sender, Msg.effect("&f    Players&7: #FFF593;" + Bukkit.getOnlinePlayers().size() + "&f / &e" + Bukkit.getServer().getMaxPlayers()));
          Msg.info(sender, Msg.effect(""));
        }

        return true;
      }

      // 스폰 설정
      case "setspawn": {
        if (!sender.hasPermission("cherry.setspawn")) {
          Msg.error(sender, Msg.NO_PERMISSION);
          return true;
        }
        org.bukkit.entity.Player player = null;
        if (sender instanceof org.bukkit.entity.Player) {
          player = (org.bukkit.entity.Player) sender;
        }
        else {
          Msg.error(sender, Msg.Player.ONLY);
          return true;
        }

        Config spawnConfig = new Config("data/spawn", true);

        spawnConfig.set("spawn.location", player.getLocation());
        player.getWorld().setSpawnLocation(player.getLocation());
        Msg.info(player, Msg.Prefix.CHERRY + "서버의 스폰 지점을 지정하였습니다.");
        return true;
      }

      case "sleepconfig": {
        if (!sender.hasPermission("cherry.sleepconfig")) {
          Msg.error(sender, Msg.NO_PERMISSION);
          return true;
        }
        try {
          File cf = new File(Cherry.plugin.getDataFolder() + "/config.yml");
          cf.deleteOnExit();
          InputStream in = Cherry.plugin.getResource("config-sleep.yml");
          byte[] buffer = new byte[in.available()];
          in.read(buffer);
          OutputStream out = new FileOutputStream(cf);
          out.write(buffer);
        } catch (Exception ignored) {}
      }



      // Boooooooooom!
      case "bomb": {
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
            for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
              p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 5.0f, 2.0f);
              Bukkit.getScheduler().scheduleSyncDelayedTask(Cherry.plugin, new Runnable() {
                @Override
                public void run() {
                  String msg = "";
                  msg += "&r\n";
                  msg += "&r #AD4132;__.#96382C;-^-....,,-#AD4132;..__\n";
                  msg += "&r #C24838;_-                   -_\n";
                  msg += "&r  #DB5240;/#FDE4C3;/                      \\#DB5240;\\\n";
                  msg += "&r #F55A46;| #FDF0D5;|                         | #F55A46;|\n";
                  msg += "&r  #F55A46;\\#FDE4C3;\\.#F55A46;._                 #F55A46;_.#FDE4C3;./#F55A46;/\n";
                  msg += "&r #F55A46;```#E86D43;--. . , ; .--#F55A46;'''\n";
                  msg += "&r #DB4A40;| ;  :|\n";
                  msg += "&r #A13C2F;| |  |\n";
                  msg += "&r #96382C;.-#5E2220;#=#96382C;| |  ;|#5E2220;#=#96382C;-.\n";
                  msg += "&r #96382C;`-=#B84435;##F2BBA2;$%;|$#B84435;##96382C;=-'\n";
                  msg += "&r #8A3328;| |  |\n";
                  msg += "&r #5E2220;| ;  :|\n";
                  msg += "&r #80222C;_____.,-##B84435;%&$@%#&#80222C;#-,._____\n";
                  msg += "&r\n\n\n";
                  msg += "&r #FF4268;&l&oBoooooooooooooooom!\n";
                  msg += "&r\n";
                  p.kickPlayer(Msg.effect(msg));
                }
              }, 3);
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

      // NullPointerException
      case "null": {
        if (!sender.hasPermission("cherry.null")) {
          Msg.error(sender, Msg.NO_PERMISSION);
        }
        throw new NullPointerException();
      }

      case "regex": {
        String input = "{eval:1+2} {eval:(2^2)^2}    {eval:2^2^2}   {eval:2^2^2}";
        Pattern pattern = Pattern.compile("\\{eval:([^}]+)}");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
          String m = matcher.group();
          String str = matcher.group(2);
          input = input.replace(m, "[식: " + str + "]");
        }

        Msg.info(input);

        return true;
      }

      case "ks": {
        if (sender instanceof Player) {
          Player p = (Player) sender;
          p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 5.0f, 2.0f);
          Bukkit.getScheduler().scheduleSyncDelayedTask(Cherry.plugin, new Runnable() {
            @Override
            public void run() {
              String msg = "";
              msg += "&r\n";
              msg += "&r #AD4132;__.#96382C;-^-....,,-#AD4132;..__\n";
              msg += "&r #C24838;_-                   -_\n";
              msg += "&r  #DB5240;/#FDE4C3;/                      \\#DB5240;\\\n";
              msg += "&r #F55A46;| #FDF0D5;|                         | #F55A46;|\n";
              msg += "&r  #F55A46;\\#FDE4C3;\\.#F55A46;._                 #F55A46;_.#FDE4C3;./#F55A46;/\n";
              msg += "&r #F55A46;```#E86D43;--. . , ; .--#F55A46;'''\n";
              msg += "&r #DB4A40;| ;  :|\n";
              msg += "&r #A13C2F;| |  |\n";
              msg += "&r #96382C;.-#5E2220;#=#96382C;| |  ;|#5E2220;#=#96382C;-.\n";
              msg += "&r #96382C;`-=#B84435;##F2BBA2;$%;|$#B84435;##96382C;=-'\n";
              msg += "&r #8A3328;| |  |\n";
              msg += "&r #5E2220;| ;  :|\n";
              msg += "&r #80222C;_____.,-##B84435;%&$@%#&#80222C;#-,._____\n";
              msg += "&r\n\n\n";
              msg += "&r #FF4268;&l&oBoooooooooooooooom!\n";
              msg += "&r\n";
              p.kickPlayer(Msg.effect(msg));
            }
          }, 3);

        }
        return true;
      }



      default: {
        Msg.error(sender, Msg.UNKNOWN);
        return true;
      }

    }
  }
}
