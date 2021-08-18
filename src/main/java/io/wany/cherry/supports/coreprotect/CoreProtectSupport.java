package io.wany.cherry.supports.coreprotect;

import io.wany.cherry.Cherry;
import io.wany.cherry.Console;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class CoreProtectSupport {

  public static Plugin PLUGIN;

  public static String NAME = "CoreProtect";
  public static String COLORHEX = "#00A1CA";
  public static String COLOR = COLORHEX + ";";
  public static String PREFIX = COLOR + "&l[" + NAME + "]:&r ";

  public static boolean ENABLE = false;
  public static boolean EXIST = false;
  public static boolean LOADED = false;

  public static CoreProtectAPI API;

  public static boolean exist() {
    if (!ENABLE) {
      return false;
    }
    PLUGIN = Bukkit.getPluginManager().getPlugin(NAME);
    if (PLUGIN != null && PLUGIN.isEnabled()) {
      if (!EXIST) {
        EXIST = true;
        Console.debug(PREFIX + "Found " + NAME + " v" + PLUGIN.getDescription().getVersion());
      }
      if (!LOADED) {
        LOADED = true;
      }
      return true;
    }
    else {
      EXIST = false;
      LOADED = false;
      Console.debug(PREFIX + "ERROR: " + NAME + " plugin not exist");
      return false;
    }
  }

  public static void onPluginEnable(PluginEnableEvent event) {
    if (!ENABLE) {
      return;
    }
    Plugin plugin = event.getPlugin();
    if (!plugin.getName().equals(NAME)) {
      return;
    }
    Console.debug(PREFIX + NAME + " v" + plugin.getDescription().getVersion() + " Enabled");
    if (LOADED) {
      Console.debug(PREFIX + "Re-Enabling " + NAME + "-Support");
    }
    else {
      Console.debug(PREFIX + "Enabling " + NAME + "-Support");
    }
    if (!exist()) {
      Console.debug(PREFIX + NAME + "-Support Disabled");
      return;
    }
    onSupportEnable();
  }

  public static void onPluginDisable(PluginDisableEvent event) {
    if (!ENABLE) {
      return;
    }
    Plugin plugin = event.getPlugin();
    if (!plugin.getName().equals(NAME)) {
      return;
    }
    Console.debug(PREFIX + NAME + " v" + plugin.getDescription().getVersion() + " Disabled");
    EXIST = false;
    if (LOADED) {
      Console.debug(PREFIX + "Disabling " + NAME + "-Support");
    }
    onSupportDisable();
  }

  public static void onEnable() {
    if (!Cherry.CONFIG.getBoolean(NAME.toLowerCase() + "-support.enable")) {
      Console.debug(PREFIX + NAME + "-Support Disabled");
      return;
    }
    Console.debug(PREFIX + "Enabling " + NAME + "-Support");
    ENABLE = true;
    if (!exist()) {
      Console.debug(PREFIX + NAME + "-Support Disabled");
      return;
    }
    onSupportEnable();
  }

  public static void onDisable() {
    onSupportDisable();
  }

  public static void onSupportEnable() {
    API = CoreProtect.getInstance().getAPI();
  }

  public static void onSupportDisable() {
    API = null;
  }

}
