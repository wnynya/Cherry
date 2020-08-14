package com.wnynya.cherry.world;

import com.wnynya.cherry.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;

public class World {

  private static Config worldConfig = new Config("world");
  private static HashMap<String, World> worlds = new HashMap<>();

  private String name;
  private File path;
  private org.bukkit.World world = null;

  private World(String name, File path) {
    this.name = name;
    this.path = path;
    if (worldConfig.getConfig().getString(this.name + ".load").toLowerCase() == "auto") {
      try {
        this.load();
      } catch (Exception ignored) {}
    }
  }

  private World(org.bukkit.World world) {
    this.world = world;
    this.name = world.getName();
    this.path = world.getWorldFolder();
  }

  public void load() throws Exception {
    if (!this.path.exists()) {
      throw new Exception("월드를 찾을 수 없습니다.");
    }
    this.world = new WorldCreator(name).createWorld();
  }

  public void unload(boolean save) throws Exception {
    if (this.world == null) {
      throw new Exception("서버에 로드되지 않은 월드입니다.");
    }
    Bukkit.getServer().unloadWorld(name, save);
    this.world = null;
  }

  public void send(Player player, Location loc) throws Exception {
    if (this.world == null) {
      throw new Exception("서버에 로드되지 않은 월드입니다.");
    }
    player.teleport(loc);
  }

  public String getName() {
    return this.name;
  }

  public org.bukkit.World getWorld() {
    return this.world;
  }

  public static World getWorld(String name) {
    return worlds.getOrDefault(name, null);
  }

  public static Config getWorldConfig() {
    return worldConfig;
  }

  public static void enable() {
    for (org.bukkit.World world : Bukkit.getWorlds()) {
      if (getWorld(world.getName()) != null) {
        continue;
      }
      World cw = new World(world);
      worlds.put(cw.getName(), cw);
    }
    for (String name : worldConfig.getConfig().getConfigurationSection("").getKeys(false)) {
      if (getWorld(name) != null) {
        continue;
      }
      ConfigurationSection config = worldConfig.getConfig().getConfigurationSection(name);
      World cw = new World(name, new File(config.getString("path")));
      worlds.put(cw.getName(), cw);
    }
  }

}
