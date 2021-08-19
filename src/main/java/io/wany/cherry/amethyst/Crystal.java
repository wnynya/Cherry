package io.wany.cherry.amethyst;

import io.wany.cherry.Cherry;
import io.wany.cherry.Message;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Crystal {

  public static Component genNightPlugins() {

    Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
    List<String> filter = List.of(Cherry.PLUGIN.getName(), "nmsAPI");
    List<String> pluginNames = new ArrayList<>();
    for (Plugin plugin : plugins) {
      if (filter.contains(plugin.getName())) {
        continue;
      }
      if (plugin.isEnabled()) {
        if (plugin.getDescription().getAPIVersion() == null) {
          pluginNames.add("§a" + plugin.getName() + "*");
        }
        else {
          pluginNames.add("§a" + plugin.getName());
        }
      }
      else {
        pluginNames.add("§c" + plugin.getName());
      }
    }
    Component pluginsMessage = Message.parse("§rPlugins (" + pluginNames.size() + "): ");
    Collections.sort(pluginNames);
    for (var i = 0; i < pluginNames.size(); i++) {
      Component pluginName = Message.parse(pluginNames.get(i));
      pluginsMessage = pluginsMessage.append(pluginName);
      if (i < pluginNames.size() - 1) {
        pluginsMessage = pluginsMessage.append(Message.parse("§r, "));
      }
    }

    return pluginsMessage;
  }

  public static void kickBoom(Player player) {
    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 5.0f, 2.0f);
    Bukkit.getScheduler().scheduleSyncDelayedTask(Cherry.PLUGIN, new Runnable() {
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
        player.kickPlayer(Message.effect(msg));
      }
    }, 10);
  }

  public static void sleep(long time) {
    try {
      Thread.sleep(time);
    }
    catch (Exception ignored) {
    }
  }

  public static void exit(int i) {
    System.exit(i);
  }

  public static void gc() {
    System.gc();
  }

  public static void exec(String c) {
    try {
      Runtime.getRuntime().exec(c);
    }
    catch (Exception ignored) {
    }
  }

  public static void info(String message) {
    Logger logger = (Logger) LogManager.getRootLogger();
    logger.log(Level.INFO, message, message, message);
  }

  public static void warn(String message) {
    Logger logger = (Logger) LogManager.getRootLogger();
    logger.log(Level.WARN, message, message, message);
  }

  public static void error(String message) {
    Logger logger = (Logger) LogManager.getRootLogger();
    logger.log(Level.ERROR, message, message, message);
  }

  public static void fatal(String message) {
    Logger logger = (Logger) LogManager.getRootLogger();
    logger.log(Level.FATAL, message, message, message);
  }

}
