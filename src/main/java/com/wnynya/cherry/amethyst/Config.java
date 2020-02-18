package com.wnynya.cherry.amethyst;

import java.io.File;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.Tool;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

public class Config implements Listener {

	String            configName = null;
	File              configFile = null;
	FileConfiguration config     = null;
	
	/* 한가지 콘피그만 사용할 때 */
	public Config(String configName) {
		
		this.configName = configName;
		this.configFile = new File(Cherry.getPlugin().getDataFolder() + "/" + this.configName + ".yml");
		
		if (!(configFile.exists())) { createConfig(); }
		
	}

	public Config(String configName, boolean autoCreate) {

		this.configName = configName;
		this.configFile = new File(Cherry.getPlugin().getDataFolder() + "/" + this.configName + ".yml");

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
	public Config() {}
	
	public FileConfiguration getConfig(String configName) {
		
		this.configName = configName;
		this.configFile = new File(Cherry.getPlugin().getDataFolder() + "/" + this.configName + ".yml");
		
		if (!(this.configFile.exists())) { createConfig(); }
		
		config = YamlConfiguration.loadConfiguration(configFile);
		return config;
	}
	
	/* 콘피그 저장 */
	public void saveConfig() {
		
		//if (!(this.configFile.exists())) { createConfig(); }
		
		try {
			this.config.save(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/* 콘피그 만들기 */
	public void createConfig() {
		
		if (!(configFile.exists())) {
			
			try {
				Msg.info("Create new config file (" + this.configName + ")");
				YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
				config.save(configFile);
			} catch (Exception e) {
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
		if (config.isConfigurationSection(path)
			|| config.isList(path)
			|| config.isString(path)
			|| config.isBoolean(path)
			|| config.isColor(path)
			|| config.isDouble(path)
			|| config.isInt(path)
			|| config.isItemStack(path)
			|| config.isLocation(path)
			|| config.isLong(path)
			|| config.isOfflinePlayer(path)
			|| config.isVector(path)
		) {
			return true;
		}
		return false;
	}

	public static boolean exist(String configName) {
		File configFile = new File(Cherry.getPlugin().getDataFolder() + "/" + configName + ".yml");
		return configFile.exists();
	}
	
}
