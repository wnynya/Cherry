package com.wnynya.cherry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.File;

public class Config implements Listener {

  String configName = null;
  File configFile = null;
  FileConfiguration config = null;

  /* 한가지 콘피그만 사용할 때 */
  public Config(String configName) {

    this.configName = configName;
    this.configFile = new File(Cherry.plugin.getDataFolder() + "/" + this.configName + ".yml");

    if (!(configFile.exists())) {
      createConfig();
    }

  }

  public Config(String configName, boolean autoCreate) {

    this.configName = configName;
    this.configFile = new File(Cherry.plugin.getDataFolder() + "/" + this.configName + ".yml");

    if (autoCreate) {
      if (!(configFile.exists())) {
        createConfig();
      }
    }

  }

  public FileConfiguration getConfig() {

    //if (!(this.configFile.exists())) { createConfig(); }
    if (!(configFile.exists())) {
      return null;
    }
    else {
      config = YamlConfiguration.loadConfiguration(configFile);
      return config;
    }

  }

  /* 여러 가지 콘피그를 사용할 때 */
  public Config() {
  }

  public FileConfiguration getConfig(String configName) {

    this.configName = configName;
    this.configFile = new File(Cherry.plugin.getDataFolder() + "/" + this.configName + ".yml");

    if (!(this.configFile.exists())) {
      createConfig();
    }

    config = YamlConfiguration.loadConfiguration(configFile);
    return config;
  }

  /* 콘피그 저장 */
  public void saveConfig() {

    //if (!(this.configFile.exists())) { createConfig(); }

    try {
      this.config.save(configFile);
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

  /* 콘피그 만들기 */
  public void createConfig() {

    if (!(configFile.exists())) {

      try {
        if (Msg.enabled) {
          Msg.info("Create new config file (" + this.configName + ")");
        }
        else {
          Bukkit.getServer().getConsoleSender().sendMessage("[Cherry] Create new config file (" + this.configName + ")");
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        config.save(configFile);
      }
      catch (Exception e) {
        e.printStackTrace();
      }

    }
  }

  public void set(String path, Object value) {
    FileConfiguration config = getConfig(this.configName);
    config.set(path, value);
    saveConfig();
  }

  public boolean is(String path) {
    FileConfiguration config = getConfig(this.configName);
    if (config.isConfigurationSection(path) || config.isList(path) || config.isString(path) || config.isBoolean(path) || config.isColor(path) || config.isDouble(path) || config.isInt(path) || config.isItemStack(path) || config.isLocation(path) || config.isLong(path) || config.isOfflinePlayer(path) || config.isVector(path)) {
      return true;
    }
    return false;
  }

  public String getString(String path) {
    return this.getConfig().getString(path);
  }

  public boolean getBoolean(String path) {
    return this.getConfig().getBoolean(path);
  }

  public int getInt(String path) { return this.getConfig().getInt(path); }

  public double getDouble(String path) { return this.getConfig().getDouble(path); }

  public static boolean exist(String configName) {
    File configFile = new File(Cherry.plugin.getDataFolder() + "/" + configName + ".yml");
    return configFile.exists();
  }

}
