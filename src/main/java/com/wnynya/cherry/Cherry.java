package com.wnynya.cherry;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.wnynya.cherry.amethyst.*;
import com.wnynya.cherry.command.*;
import com.wnynya.cherry.command.easy.EasyTabCompleter;
import com.wnynya.cherry.command.easy.Gm;
import com.wnynya.cherry.command.easy.Rlc;
import com.wnynya.cherry.command.farm.FarmCommand;
import com.wnynya.cherry.command.playermeta.PlayerMetaCommand;
import com.wnynya.cherry.command.playermeta.PlayerMetaTabCompleter;
import com.wnynya.cherry.command.portal.PortalCommand;
import com.wnynya.cherry.command.portal.PortalTabCompleter;
import com.wnynya.cherry.command.wand.WandCommand;
import com.wnynya.cherry.command.wand.WandTabCompleter;
import com.wnynya.cherry.event.*;

import com.wnynya.cherry.player.PlayerMeta;
import com.wnynya.cherry.portal.Portal;
import com.wnynya.cherry.wand.Wand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.*;

public class Cherry extends JavaPlugin {

  # yee pull request test
  public static Cherry plugin = null;
  public static Cherry getPlugin() {
    return plugin;
  }
  public static FileConfiguration config;
  public static boolean debug = false;
  public static UUID getUUID() { return UUID.fromString("00000000-0000-0000-0000-000000000000"); }
  public static String fileName = "";
  public static boolean reloading = false;

  @Override
  public void onEnable() {
    plugin = this;

    this.init();

    reloading = false;

    if (Cherry.debug) { Msg.info("Cherry Plugin Enabled"); }

    Bukkit.getScheduler().runTaskLater(Cherry.getPlugin(), () -> {
      Updater.init();
    }, 100L);
  }

  @Override
  public void onDisable() {
    if (Cherry.debug) { Bukkit.getServer().getConsoleSender().sendMessage("[Cherry] Plugin Disabled"); }
  }

  // initiation
  private void init() {

    initConfig();

    Msg.init();

    /*
    Events Register
     */

    PluginManager pm = Bukkit.getServer().getPluginManager();

    // Player Events
    pm.registerEvents(new PlayerConnect(), this);
    pm.registerEvents(new PlayerChat(), this);
    pm.registerEvents(new PlayerInteract(), this);
    pm.registerEvents(new PlayerMove(), this);
    pm.registerEvents(new PlayerCommandPreprocess(), this);
    pm.registerEvents(new InventoryClick(), this);
    pm.registerEvents(new BlockBreak(), this);
    pm.registerEvents(new BlockPlace(), this);
    //pm.registerEvents(new EventTester(), this);
    //pm.registerEvents(new JumpChk(), this);

    // Other Events

    /*
    Commands Register
     */

    // Basic Commands
    registerCommand("cherry", new CmdCherry(), new TabCompleter());
    registerCommand("menu", new CmdMenu(), new TabCompleter());

    // Meta Commands
    registerCommand("playermeta", new PlayerMetaCommand(), new PlayerMetaTabCompleter());

    // Cherry Wand Commands
    registerCommand("wand", new WandCommand(), new WandTabCompleter());
    //registerCommand("edit", new WandEditCommand(), new WandTabCompleter());
    //registerCommand("brush", new WandBrushCommand(), new WandTabCompleter());

    // Portal Commands
    registerCommand("portal", new PortalCommand(), new PortalTabCompleter());

    // Minigame Commands

    //registerCommand("farm", new FarmCommand(), new TabCompleter());

    // EtcCommands
    registerCommand("gm", new Gm(), new EasyTabCompleter());
    registerCommand("rlc", new Rlc(), new EasyTabCompleter());

    /*
    Function Initiation
     */

    // Cherry Wand
    Wand.init();

    // Portal
    Portal.init();

    // PlayerMeta
    PlayerMeta.init();

    // Minigame

    Commander.init();

    // Vault
    if (Vault.exist()) {
      pm.registerEvents(new Vault(), this);
      Vault.loadVaultChat();
    }

    // BungeeCord Msg Channel\
    this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeCordMsg());

    fileName = this.getFile().getName();

    // Cucumbery Support
    if (CucumberySupport.exist()) { CucumberySupport.init(); }

  }

  private void registerCommand(String command, CommandExecutor cmdExc, org.bukkit.command.TabCompleter cmdTab) {
    this.getCommand(command).setExecutor(cmdExc);
    this.getCommand(command).setTabCompleter(cmdTab);
  }

  // config.yml
  public boolean initConfig() {
    config = new Config("config").getConfig();

    this.getConfig().options().copyDefaults(true);
    this.saveDefaultConfig();
    config = this.getConfig();

    debug = config.getBoolean("debug");
    if (Cherry.debug) { Bukkit.getServer().getConsoleSender().sendMessage("§d[Cherry]§r Debug Enabled"); }
    if (Cherry.debug) { Bukkit.getServer().getConsoleSender().sendMessage("§d[Cherry]§r Config Loaded"); }

    String configVersion = "1.1.27";
    String currentVersion = config.getString("config.version");
    if (!currentVersion.equals(configVersion)) {
      Bukkit.getServer().getConsoleSender().sendMessage(
        "§c[Cherry] Cannot enable plugin properly!");
      Bukkit.getServer().getConsoleSender().sendMessage(
        "§c\t콘피그 버전이 다릅니다. [ 현재:"
        + currentVersion + ", 최신:" + configVersion + " ] "
        + "콘피그 파일을 삭제 후 리로드하여 재설정하거나, 콘피크 파일을 적절하게 수정하십시오.");
      return false;
    }
    return true;
  }

  // Unload Cherry
  public static void unload() {
    reloading = true;
    Plugin plugin = Cherry.getPlugin();
    String name = plugin.getName();
    PluginManager pluginManager = Bukkit.getPluginManager();
    SimpleCommandMap commandMap = null;
    List<Plugin> plugins = null;
    Map<String, Plugin> names = null;
    Map<String, Command> commands = null;
    Map<Event, SortedSet<RegisteredListener>> listeners = null;
    boolean reloadlisteners = true;
    pluginManager.disablePlugin(plugin);
    try {
      Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
      pluginsField.setAccessible(true);
      plugins = (List<Plugin>) pluginsField.get(pluginManager);
      Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
      lookupNamesField.setAccessible(true);
      names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);
      try {
        Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
        listenersField.setAccessible(true);
        listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
      }
      catch (Exception e) { reloadlisteners = false; }
      Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
      commandMapField.setAccessible(true);
      commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);
      Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
      knownCommandsField.setAccessible(true);
      commands = (Map<String, Command>) knownCommandsField.get(commandMap);
    }
    catch (Exception e) { e.printStackTrace(); }
    pluginManager.disablePlugin(plugin);
    if (plugins != null && plugins.contains(plugin)) { plugins.remove(plugin); }
    if (names != null && names.containsKey(name)) { names.remove(name); }
    if (listeners != null && reloadlisteners) {
      for (SortedSet<RegisteredListener> set : listeners.values()) { set.removeIf(value -> value.getPlugin() == plugin); }
    }
    if (commandMap != null) {
      for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext(); ) {
        Map.Entry<String, Command> entry = it.next();
        if (entry.getValue() instanceof PluginCommand) {
          PluginCommand c = (PluginCommand) entry.getValue();
          if (c.getPlugin() == plugin) { c.unregister(commandMap); it.remove(); }
        }
      }
    }
    ClassLoader cl = plugin.getClass().getClassLoader();
    if (cl instanceof URLClassLoader) {
      try {
        Field pluginField = cl.getClass().getDeclaredField("plugin");
        pluginField.setAccessible(true);
        pluginField.set(cl, null);
        Field pluginInitField = cl.getClass().getDeclaredField("pluginInit");
        pluginInitField.setAccessible(true);
        pluginInitField.set(cl, null);
      }
      catch (Exception ignored) { }
      try { ((URLClassLoader) cl).close(); }
      catch (Exception ignored) { }
    }
    System.gc();
  }

  // Load Cherry
  public static void load() {
    Plugin plugin = null;
    File cherryFile = Cherry.getPlugin().getFile();
    if (!cherryFile.isFile()) { return; }
    try { plugin = Bukkit.getPluginManager().loadPlugin(cherryFile); }
    catch (Exception e) { e.printStackTrace(); return; }
    if (plugin == null) { return; }
    plugin.onLoad();
    Bukkit.getPluginManager().enablePlugin(plugin);
  }

  public static void load(File file) {
    Plugin plugin = null;
    if (!file.isFile()) { return; }
    try { plugin = Bukkit.getPluginManager().loadPlugin(file); }
    catch (Exception e) { e.printStackTrace(); return; }
    if (plugin == null) { return; }
    plugin.onLoad();
    Bukkit.getPluginManager().enablePlugin(plugin);
  }

  // Boom it
  public static void boom() {
    System.exit(0);
  }

  public static void boom(int i) {
    System.exit(i);
  }

}
