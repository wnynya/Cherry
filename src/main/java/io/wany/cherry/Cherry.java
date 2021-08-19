package io.wany.cherry;

import io.wany.cherry.amethyst.ServerPropertiesSorter;
import io.wany.cherry.commands.*;
import io.wany.cherry.itemonworld.ItemOnWorld;
import io.wany.cherry.listeners.*;
import io.wany.cherry.playerdata.PlayerData;
import io.wany.cherry.portal.Portal;
import io.wany.cherry.supports.bungeecord.BungeecordSupport;
import io.wany.cherry.supports.coreprotect.CoreProtectSupport;
import io.wany.cherry.supports.cucumbery.CucumberySupport;
import io.wany.cherry.supports.vault.VaultSupport;
import io.wany.cherry.terminal.Terminal;
import io.wany.cherry.wand.Wand;
import io.wany.cherry.wand.command.WandEditCommand;
import io.wany.cherry.wand.command.WandEditTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;
import java.util.UUID;

/**
 *
 * Cherry                 ©2019-2021 Wany
 *                 https://cherry.wany.io
 *
 */
public class Cherry extends JavaPlugin {

  public static Cherry PLUGIN;

  public static final String COLOR = "#D2B0DD;";
  public static final String PREFIX = COLOR + "&l[Cherry]:&r ";
  public static final UUID UUID = java.util.UUID.fromString("00000000-0000-0000-0000-000000000000");

  public static boolean DEBUG = false;
  public static boolean NIGHT = false;

  public static FileConfiguration CONFIG;
  public static File FILE;
  public static File DIR;
  public static File PLUGINS_DIR;
  public static File SERVER_DIR;

  @Override
  public void onLoad() {

    PLUGIN = this;
    CONFIG = Config.onLoad();
    Terminal.onLoad();

  }

  @Override
  public void onEnable() {

    Terminal.onEnable();
    PlayerData.onEnable();
    Wand.onEnable();
    Portal.onEnable();
    ItemOnWorld.onEnable();

    registerCommand("cherry", new CherryCommand(), new CherryTabCompleter());
    registerCommand("throw", new ThrowCommand(), new ThrowTabCompleter());
    registerCommand("wandedit", new WandEditCommand(), new WandEditTabCompleter());
    //registerCommand("wandedit", new WandEditCommand(), new WandEditTabCompleter());
    registerCommand("menu", new MenuCommand(), new CherryTabCompleter());
    registerCommand("troll", new TrollCommand(), new CherryTabCompleter());

    registerEvent(new PlayerJoin());
    registerEvent(new PlayerQuit());
    registerEvent(new PlayerChat());
    registerEvent(new PlayerDeath());
    registerEvent(new PlayerInteract());
    registerEvent(new PlayerCommandPreprocess());
    registerEvent(new BlockBreak());
    registerEvent(new BlockPhysics());
    registerEvent(new BlockPistonExtend());
    registerEvent(new BlockDestroy());
    registerEvent(new BlockDropItem());
    registerEvent(new BlockExplode());
    registerEvent(new EntityAddToWorld());
    registerEvent(new ItemSpawn());
    registerEvent(new EntityDeath());
    registerEvent(new ServerCommand());
    registerEvent(new RemoteServerCommand());
    registerEvent(new InventoryClick());
    registerEvent(new PluginEnable());
    registerEvent(new PluginDisable());

    BungeecordSupport.onEnable();
    VaultSupport.onEnable();
    CucumberySupport.onEnable();
    CoreProtectSupport.onEnable();

    FILE = this.getFile();
    DIR = this.getDataFolder().getAbsoluteFile();
    PLUGINS_DIR = new File(this.getDataFolder().getAbsoluteFile().getParent());
    SERVER_DIR = new File(new File(this.getDataFolder().getAbsoluteFile().getParent()).getParent());

    Updater.onEnable();
    ServerPropertiesSorter.onEnable();

  }

  @Override
  public void onDisable() {

    Terminal.onDisable();
    PlayerData.onDisable();
    Wand.onDisable();

    VaultSupport.onDisable();
    CucumberySupport.onDisable();
    CucumberySupport.onDisable();

    Updater.onDisable();

  }

  public void registerCommand(String command, CommandExecutor cmdExc, org.bukkit.command.TabCompleter cmdTab) {
    Map<String, Map<String, Object>> commandMap = Cherry.PLUGIN.getDescription().getCommands();
    if (commandMap.containsKey(command)) {
      PluginCommand pluginCommand = this.getCommand(command);
      if (pluginCommand == null) {
        return;
      }
      pluginCommand.setExecutor(cmdExc);
      pluginCommand.setTabCompleter(cmdTab);
    }
  }

  public void registerEvent(Listener eventListener) {
    PluginManager pm = Bukkit.getServer().getPluginManager();
    pm.registerEvents(eventListener, this);
  }

}
