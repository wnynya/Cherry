package io.wany.cherry;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.List;

public class Config {

  public FileConfiguration config = null;
  private final String name;
  private final File file;

  public Config(String name) {
    this.name = name;
    this.file = new File(Cherry.PLUGIN.getDataFolder() + "/" + this.name + ".yml");
    try {
      this.config = YamlConfiguration.loadConfiguration(file);
      this.config.save(this.file);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Config(File file) {
    this.name = file.getName();
    this.file = file;
    try {
      this.config = YamlConfiguration.loadConfiguration(file);
      this.config.save(this.file);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Config(YamlConfiguration yamlConfiguration) {
    this.name = yamlConfiguration.getName();
    this.file = null;
    this.config = yamlConfiguration;
  }

  public static boolean exist(String configName) {
    File configFile = new File(Cherry.PLUGIN.getDataFolder() + "/" + configName + ".yml");
    return configFile.exists();
  }

  public void save() {

    try {
      if (!(this.file.exists())) {
        this.config = YamlConfiguration.loadConfiguration(file);
      }
      this.config.save(file);
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void set(String path, Object value) {
    if (value instanceof Component) {
      this.config.set(path, Message.stringify((Component) value));
    }
    else {
      this.config.set(path, value);
    }
    save();
  }

  public File file() {
    return this.file;
  }

  public String getString(String path) {
    return this.config.getString(path);
  }

  public boolean getBoolean(String path) {
    return this.config.getBoolean(path);
  }

  public int getInt(String path) {
    return this.config.getInt(path);
  }

  public double getDouble(String path) {
    return this.config.getDouble(path);
  }

  public float getFloat(String path) {
    return (float) this.config.getDouble(path);
  }

  public long getLong(String path) {
    return this.config.getLong(path);
  }

  public List<?> getList(String path) {
    return this.config.getList(path);
  }

  public Component getComponent(String path) {
    if (!isString(path)) {
      return null;
    }
    return Message.parse(this.getString(path));
  }

  public Location getLocation(String path) {
    return this.config.getLocation(path);
  }

  public ItemStack getItenStack(String path) {
    return this.config.getItemStack(path);
  }

  public Vector getVector(String path) {
    return this.config.getVector(path);
  }

  public boolean isString(String path) {
    return this.config.isString(path);
  }

  public boolean isBoolean(String path) {
    return this.config.isBoolean(path);
  }

  public boolean isInt(String path) {
    return this.config.isInt(path);
  }

  public boolean isDouble(String path) {
    return this.config.isDouble(path);
  }

  public boolean isLong(String path) {
    return this.config.isLong(path);
  }

  public boolean isList(String path) {
    return this.config.isList(path);
  }

  public boolean isComponent(String path) {
    return isString(path);
  }

  public boolean isLocation(String path) {
    return this.config.isLocation(path);
  }

  public boolean isItemStack(String path) {
    return this.config.isItemStack(path);
  }

  public void toggle(String path) {
    set(path, !getBoolean(path));
  }

  public String initString(String path, String value) {
    if (!this.isString(path)) {
      this.set(path, value);
    }
    return this.getString(path);
  }

  public static FileConfiguration onLoad() {
    FileConfiguration config = Cherry.PLUGIN.getConfig();
    Cherry.PLUGIN.getConfig().options().copyDefaults(true);
    Cherry.PLUGIN.saveDefaultConfig();
    Cherry.DEBUG = config.getBoolean("debug");
    Cherry.NIGHT = config.getBoolean("night");
    return config;
  }

}
