package com.wnynya.cherry;

import com.wnynya.cherry.amethyst.CucumberySupport;
import com.wnynya.cherry.command.CherryCommand;
import com.wnynya.cherry.command.MenuCommand;
import com.wnynya.cherry.command.TabCompleter;
import com.wnynya.cherry.command.easy.EasyTabCompleter;
import com.wnynya.cherry.command.easy.Gm;
import com.wnynya.cherry.command.easy.Rlc;
import com.wnynya.cherry.command.playermeta.PlayerMetaCommand;
import com.wnynya.cherry.command.playermeta.PlayerMetaTabCompleter;
import com.wnynya.cherry.command.portal.PortalCommand;
import com.wnynya.cherry.command.portal.PortalTabCompleter;
import com.wnynya.cherry.command.wand.WandCommand;
import com.wnynya.cherry.command.wand.WandTabCompleter;
import com.wnynya.cherry.event.*;
import com.wnynya.cherry.network.bungeecord.NetworkChannelListener;
import com.wnynya.cherry.network.terminal.WebSocketClient;
import com.wnynya.cherry.player.PlayerMeta;
import com.wnynya.cherry.portal.Portal;
import com.wnynya.cherry.wand.Wand;
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

  public static Cherry getPlugin() {
    return plugin;
  }

  public static FileConfiguration config;

  public static UUID getUUID() {
    return UUID.fromString("00000000-0000-0000-0000-000000000000");
  }

  public static File file;
  public static File serverDir;
  public static WebSocketClient.Status status;

  public static boolean debug = false;

  @Override
  public void onEnable() {

    plugin = this;

    initConfig();

    Msg.enable();

    WebSocketClient.enable();

    serverDir = new File(new File(plugin.getDataFolder().getAbsoluteFile().getParent()).getParent());
    file = this.getFile();

    // 기본 명령어
    registerCommand("cherry", new CherryCommand(), new TabCompleter());
    registerCommand("menu", new MenuCommand(), new TabCompleter());

    // PlayerMeta
    PlayerMeta.init();
    registerCommand("playermeta", new PlayerMetaCommand(), new PlayerMetaTabCompleter());

    // Wand
    Wand.init();
    registerCommand("wand", new WandCommand(), new WandTabCompleter());

    // Portal
    registerCommand("portal", new PortalCommand(), new PortalTabCompleter());

    // World
    //CherryWorld.init();
    //registerCommand("world", new WorldCommand(), new WorldTabCompleter());

    // Events
    registerEvent(new PlayerChat());
    registerEvent(new PlayerConnect());
    registerEvent(new PlayerInteract());
    registerEvent(new PlayerMove());
    registerEvent(new PlayerPortal());
    registerEvent(new InventoryClick());
    registerEvent(new Command());
    registerEvent(new BlockBreak());
    registerEvent(new BlockPlace());

    // BungeeCord Messaging Channel
    this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeCordMsg());

    // Cherry Messaging Channel
    this.getServer().getMessenger().registerOutgoingPluginChannel(this, "cherry:networkchannel");
    this.getServer().getMessenger().registerIncomingPluginChannel(this, "cherry:networkchannel", new NetworkChannelListener());

    // Vault
    if (Vault.exist()) {
      registerEvent(new Vault());
      Vault.loadVaultChat();
    }

    // Cucumbery Support
    if (CucumberySupport.exist()) {
      CucumberySupport.init();
    }

    // Easy
    registerCommand("gm", new Gm(), new EasyTabCompleter());
    registerCommand("rlc", new Rlc(), new EasyTabCompleter());

    if (Cherry.debug) {
      Bukkit.getServer().getConsoleSender().sendMessage("[Cherry] Plugin Enabled");
    }

    //Portal.init();

    Bukkit.getScheduler().runTaskLater(Cherry.getPlugin(), new Runnable() {
      public void run() {
        Portal.init();
      }
    }, 10L);

    // Updater
    Bukkit.getScheduler().runTaskLater(Cherry.getPlugin(), new Runnable() {
      public void run() {
        if (Cherry.config.getBoolean("updater.auto")) {
          Updater.enable();
        }
      }
    }, 100L);

  }

  @Override
  public void onDisable() {
    Updater.disable();
    WebSocketClient.disable();
    if (Cherry.debug) {
      Bukkit.getServer().getConsoleSender().sendMessage("[Cherry] Plugin Disabled");
    }
  }

  private void registerCommand(String command, CommandExecutor cmdExc, org.bukkit.command.TabCompleter cmdTab) {
    Map commandMap = Cherry.getPlugin().getDescription().getCommands();
    if (commandMap.containsKey(command)) {
      this.getCommand(command).setExecutor(cmdExc);
      this.getCommand(command).setTabCompleter(cmdTab);
    }
  }

  private void registerEvent(Listener eventListener) {
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
      Bukkit.getServer().getConsoleSender().sendMessage("§d[Cherry]§r Debug Enabled");
    }
    if (Cherry.debug) {
      Bukkit.getServer().getConsoleSender().sendMessage("§d[Cherry]§r Config Loaded");
    }

    String configVersion = "1.2.4";
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

}
