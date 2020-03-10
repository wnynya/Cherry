package com.wnynya.cherry.world;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Config;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class CherryWorld {

  private static Config worldConfig = new Config("world");
  private static List<String> worlds = new ArrayList<>();

  public static void load(String worldName) throws Exception {
    if (!worlds.contains(worldName)) {
      throw new Exception("콘피그에 설정되지 않은 월드입니다.");
    }
    try {
      File originWorldDir = new File(worldConfig.getConfig().getString("worldsPath") + "/" + worldName);
      if (!originWorldDir.exists()) {
        throw new Exception("원본 월드를 찾을 수 없습니다.");
      }

      File originWorld_level_dat = new File(originWorldDir + "/level.dat");
      File originWorld_level_dat_old = new File(originWorldDir+ "/level.dat_old");
      File originWorld_region = new File(originWorldDir+ "/region");

      File instantWorldDir = new File( Cherry.serverDir + "/" + worldName);
      if (!instantWorldDir.exists()) { instantWorldDir.mkdir(); }

      File instantWorld_level_dat = new File(instantWorldDir + "/level.dat");
      File instantWorld_level_dat_old = new File(instantWorldDir + "/level.dat_old");
      File instantWorld_region = new File(instantWorldDir + "/region");
      if (!instantWorld_region.exists()) { instantWorld_region.mkdir(); }

      Files.copy(originWorld_level_dat.toPath(), instantWorld_level_dat.toPath(), StandardCopyOption.REPLACE_EXISTING);
      Files.copy(originWorld_level_dat_old.toPath(), instantWorld_level_dat_old.toPath(), StandardCopyOption.REPLACE_EXISTING);
      FileUtils.copyDirectory(originWorld_region, instantWorld_region);
    }
    catch (Exception e) {
      throw e;
    }

    World world = new WorldCreator(worldName).createWorld();
  }

  public static void unload(String worldName) throws Exception {
    try {

      Bukkit.getServer().unloadWorld(worldName, false);

      File instantWorldDir = new File( Cherry.serverDir + "/" + worldName);
      if (!instantWorldDir.exists()) {
        throw new Exception("월드를 찾을 수 없습니다.");
      }

      FileUtils.deleteDirectory(instantWorldDir);
    }
    catch (Exception e) {
      throw e;
    }
  }

  public static void send(Player player, String worldName) throws Exception {
    World world = Bukkit.getWorld(worldName);
    if (world == null) {
      throw new Exception("서버에 로드되지 않은 월드입니다.");
    }

    player.teleport(world.getSpawnLocation());
  }

  public static Config getWorldConfig() {
    return worldConfig;
  }

  public static void init() {
    if (!worldConfig.getConfig().isString("worldsPath")) {
      worldConfig.set("worldsPath", "");
    }
    if (!worldConfig.getConfig().isList("worlds")) {
      worldConfig.set("worlds", new ArrayList<>());
    }

    worlds = (List<String>) worldConfig.getConfig().getList("worlds");
  }

}
