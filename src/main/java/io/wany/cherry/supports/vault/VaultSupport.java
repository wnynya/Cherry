package io.wany.cherry.supports.vault;

import io.wany.cherry.Cherry;
import io.wany.cherry.Console;
import io.wany.cherry.playerdata.PlayerData;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.bukkit.plugin.Plugin;

public class VaultSupport implements Listener {

  public static Plugin PLUGIN;

  public static String NAME = "Vault";
  public static String COLORHEX = "#00C8C7";
  public static String COLOR = COLORHEX + ";";
  public static String PREFIX = COLOR + "&l[" + NAME + "]:&r ";

  public static boolean ENABLE = false;
  public static boolean EXIST = false;
  public static boolean LOADED = false;

  public static Chat CHAT;
  public static Economy ECONOMY;
  public static Permission PERMISSION;

  private static void loadChat() {
    Console.debug(PREFIX + "Load Chat module");
    CHAT = Bukkit.getServer().getServicesManager().load(Chat.class);
  }

  private static void loadEconomy() {
    Console.debug(PREFIX + "Load Economy module");
    ECONOMY = Bukkit.getServer().getServicesManager().load(Economy.class);
  }

  private static void loadPermission() {
    Console.debug(PREFIX + "Load Permission module");
    PERMISSION = Bukkit.getServer().getServicesManager().load(Permission.class);
  }

  @EventHandler
  private void onServiceChange(ServiceRegisterEvent event) {
    if (event.getProvider().getService() == Chat.class) {
      if (Cherry.CONFIG.getBoolean("vault-support.chat.enable")) {
        loadChat();
      }
    }
    if (event.getProvider().getService() == Economy.class) {
      if (Cherry.CONFIG.getBoolean("vault-support.economy.enable")) {
        loadEconomy();
      }
    }
    if (event.getProvider().getService() == Permission.class) {
      if (Cherry.CONFIG.getBoolean("vault-support.economy.enable")) {
        loadPermission();
      }
    }
  }


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
    Cherry.PLUGIN.registerEvent(new VaultSupport());

    VaultEconomySync.onEnable();

    if (Cherry.CONFIG.getBoolean("vault-support.chat.enable")) {
      loadChat();
    }
    if (Cherry.CONFIG.getBoolean("vault-support.permission.enable")) {
      loadEconomy();
    }
    if (Cherry.CONFIG.getBoolean("vault-support.economy.enable")) {
      loadPermission();
    }
  }

  public static void onSupportDisable() {
    VaultEconomySync.onDisable();
  }

}
