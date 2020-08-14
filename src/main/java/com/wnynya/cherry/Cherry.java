package com.wnynya.cherry;

import com.wnynya.cherry.amethyst.CucumberySupport;
import com.wnynya.cherry.command.CherryCommand;
import com.wnynya.cherry.command.MenuCommand;
import com.wnynya.cherry.command.TabCompleter;
import com.wnynya.cherry.command.easy.*;
import com.wnynya.cherry.event.*;
import com.wnynya.cherry.network.BungeeCord;
import com.wnynya.cherry.network.Terminal;
import com.wnynya.cherry.player.PlayerMeta;
import com.wnynya.cherry.portal.Portal;
import com.wnynya.cherry.wand.Wand;
import com.wnynya.cherry.wanyfield.Wanyfield;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class Cherry extends JavaPlugin {

  public static Cherry plugin = null;
  public static FileConfiguration config;
  public static UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
  public static File file;
  public static File serverDir;
  public static int javaVersion = getJavaVersion();
  public static boolean debug = false;

  @Override
  public void onEnable() {

    plugin = this;

    initConfig();

    Msg.enable();

    // Basic Commands
    registerCommand("cherry", new CherryCommand(), new TabCompleter());
    registerCommand("menu", new MenuCommand(), new TabCompleter());

    // Functions
    PlayerMeta.enable();
    Wand.enable();
    Portal.enable();

    Terminal.enable();

    // Events
    registerEvent(new AsyncPlayerChat());
    registerEvent(new PlayerConnect());
    registerEvent(new PlayerInteract());
    registerEvent(new InventoryClick());
    registerEvent(new Command());

    // Channels
    BungeeCord.enable();

    // Soft Dependency Support
    Vault.enable();
    CucumberySupport.enable();

    // Easy
    registerCommand("rlc", new Rlc(), new EasyTabCompleter());

    // System Information
    serverDir = new File(new File(plugin.getDataFolder().getAbsoluteFile().getParent()).getParent());
    file = this.getFile();

    // Updater
    Updater.enable();

  }

  @Override
  public void onDisable() {
    Wand.disable();
    Updater.disable();
    Terminal.disable();
  }

  public void registerCommand(String command, CommandExecutor cmdExc, org.bukkit.command.TabCompleter cmdTab) {
    Map commandMap = Cherry.plugin.getDescription().getCommands();
    if (commandMap.containsKey(command)) {
      this.getCommand(command).setExecutor(cmdExc);
      this.getCommand(command).setTabCompleter(cmdTab);
    }
  }

  public void registerEvent(Listener eventListener) {
    PluginManager pm = Bukkit.getServer().getPluginManager();
    pm.registerEvents(eventListener, this);
  }

  // config.yml
  private boolean initConfig() {
    config = this.getConfig();
    this.getConfig().options().copyDefaults(true);
    this.saveDefaultConfig();

    debug = config.getBoolean("debug");
    if (Cherry.debug) {
      Msg.console(Msg.effect("[Cherry] #D2B0DD;[Debug] &rDebug Enabled"));
    }
    if (Cherry.debug) {
      Msg.console(Msg.effect("[Cherry] #D2B0DD;[Debug] &rConfig Loaded"));
    }

    String configVersion = "1.2.9-dev-1";
    String currentVersion = config.getString("config.version");

    // Config version check
    if (!currentVersion.equals(configVersion)) {
      Bukkit.getServer().getConsoleSender().sendMessage("§c[Cherry] Cannot enable plugin properly!");
      Bukkit.getServer().getConsoleSender().sendMessage("§c\t콘피그 버전이 다릅니다. [ 현재:" + currentVersion + ", 최신:" + configVersion + " ] " + "콘피그 파일을 삭제 후 리로드하여 재설정하거나, 콘피크 파일을 적절하게 수정하십시오.");
      return false;
    }

    return true;
  }

  // Boom it
  public static void boom() {
    System.exit(0);
  }

  public static void boom(int i) {
    System.exit(i);
  }

  private static int getJavaVersion() {
    String version = System.getProperty("java.version");
    if (version.startsWith("1.")) {
      version = version.substring(2, 3);
    }
    else {
      int dot = version.indexOf(".");
      if (dot != -1) {
        version = version.substring(0, dot);
      }
    }
    return Integer.parseInt(version);
  }

}
