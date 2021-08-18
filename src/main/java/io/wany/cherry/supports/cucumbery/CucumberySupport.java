package io.wany.cherry.supports.cucumbery;

import com.jho5245.cucumbery.Cucumbery;
import com.jho5245.cucumbery.util.storage.CustomConfig;
import com.jho5245.cucumbery.util.storage.data.Variable;
import io.wany.cherry.Cherry;
import io.wany.cherry.Config;
import io.wany.cherry.Console;
import io.wany.cherry.commands.CherryTabCompleter;
import io.wany.cherry.commands.MenuCommand;
import io.wany.cherry.playerdata.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class CucumberySupport {

  public static Plugin PLUGIN;

  public static String NAME = "Cucumbery";
  public static String COLORHEX = "#52EE52";
  public static String COLOR = COLORHEX + ";";
  public static String PREFIX = COLOR + "&l[" + NAME + "]:&r ";

  public static boolean ENABLE = false;
  public static boolean EXIST = false;
  public static boolean LOADED = false;



  public static void onPlayerJoin(PlayerJoinEvent event) {
    defaultUserData(event.getPlayer());
  }

  public static void defaultUserData(Player player) {
    if (!ENABLE || !EXIST) {
      return;
    }
    if (!Cherry.CONFIG.getBoolean("cucumbery-support.default-userdata.enable")) {
      return;
    }
    if (PlayerData.exist(player)) {
      return;
    }
    File file = new File(PLUGIN.getDataFolder() + "/data/UserData/" + player.getUniqueId() + ".yml");
    if (!file.exists()) {
      return;
    }
    Config config = new Config(file);
    ConfigurationSection configurationSection = Cherry.CONFIG.getConfigurationSection("cucumbery-support.default-userdata.userdata");
    if (configurationSection == null) {
      return;
    }
    for (String key : configurationSection.getKeys(false)) {
      if (key.equals("HP바")) {
        config.set(key, configurationSection.getDouble(key));
      }
      else if (key.equals("아이템-사용-딜레이") || key.equals("아이템-버리기-딜레이")) {
        config.set(key, configurationSection.getInt(key));
      }
      else {
        config.set(key, configurationSection.getBoolean(key));
      }
    }
  }

  public static Config getUserDataConfig(Player player) {
    if (!ENABLE || !EXIST) {
      return null;
    }
    if (Variable.userData.containsKey(player.getUniqueId())) {
      return new Config(Variable.userData.get(player.getUniqueId()));
    }
    else {
      return new Config(CustomConfig.getPlayerConfig(player.getUniqueId()).getFile());
    }
  }

  public static void overrideCucumberyMenuCommand() {
    if (!ENABLE || !EXIST) {
      return;
    }
    PluginCommand pluginCommand = Bukkit.getPluginCommand("menu");
    if (pluginCommand == null) {
      return;
    }
    if (!pluginCommand.getPlugin().getName().equals(NAME)) {
      return;
    }
    try {
      pluginCommand.setExecutor(new MenuCommand());
      pluginCommand.setTabCompleter(new CherryTabCompleter());
      Console.debug(PREFIX + "Override menu command");
    } catch (Exception | Error ignored) {}
  }

  public static void recoverCucumberyMenuCommand() {
    if (!ENABLE || !EXIST) {
      return;
    }
    PluginCommand pluginCommand = Bukkit.getPluginCommand("menu");
    if (pluginCommand == null) {
      return;
    }
    if (!pluginCommand.getPlugin().getName().equals(NAME)) {
      return;
    }
    try {
      pluginCommand.setExecutor(new com.jho5245.cucumbery.command.Menu());
      pluginCommand.setTabCompleter(Cucumbery.getPlugin().getTabCompleter());
      Console.debug(PREFIX + "Recover menu command");
    } catch (Exception | Error ignored) {}
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
    for (Player player : Bukkit.getOnlinePlayers()) {
      defaultUserData(player);
      PlayerData.get(player);
    }
    overrideCucumberyMenuCommand();
  }

  public static void onSupportDisable() {
    recoverCucumberyMenuCommand();
  }

}
