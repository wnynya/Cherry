package com.wnynya.cherry.wanyfield;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.wand.area.Area;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wanyfield {

  private static World world = Bukkit.getWorld("wanyfield");

  public static void applyWorldRandomTick(World world) {
    for (Chunk chunk : world.getLoadedChunks()) {
      for (int section = 1; section <= 16; section++) {
        for (int n = 0; n < world.getGameRuleValue(GameRule.RANDOM_TICK_SPEED); n++) {
          Block block = getRandomBlockInChunk(chunk, section);
          applyRandomTickToBlock(block);
        }
      }
    }
  }

  public static Block getRandomBlockInChunk(Chunk chunk, int section) {
    Random random = new Random();
    int x = random.ints(0, 16).findFirst().getAsInt();
    int y = random.ints((section - 1) * 16, (section * 16)).findFirst().getAsInt();
    int z = random.ints(0, 16).findFirst().getAsInt();
    return chunk.getBlock(x, y, z);
  }

  public static void applyRandomTickToBlock(Block block) {
    World world = block.getLocation().getWorld();
    int x = block.getLocation().getBlockX();
    int y = block.getLocation().getBlockY();
    int z = block.getLocation().getBlockZ();
    Biome biome = block.getBiome();
    int skyLight = (int) block.getLightFromSky();
    switch (biome) {
      case PLAINS: {
        if (skyLight > 10) {
          if (block.getType().equals(Material.GRASS)) {
            Location pos1 = new Location(world, x - 1, y - 1, z - 1);
            Location pos2 = new Location(world, x + 1, y + 1, z + 1);
            List<Location> area = Area.CUBE.getArea(pos1, pos2);
            List<Block> bl = new ArrayList<>();
            for (Location loc : area) {
              Block b = world.getBlockAt(loc);
              if (b.getType().equals(Material.GRASS_BLOCK) && (int) b.getLightFromSky() > 10) {
                Block bu = world.getBlockAt(new Location(world, b.getX(), b.getY() + 1, b.getZ()));
                if (bu.getType().equals(Material.AIR)) {
                  bl.add(bu);
                }
              }
            }
            if (bl.size() > 4) {
              int r = new Random().ints(0, bl.size()).findFirst().getAsInt();
              bl.get(r).setBlockData(Bukkit.createBlockData(Material.GRASS));
            }
          }
        }
      }
    }
  }

  public static void randomTickScheduler() {

    Bukkit.getScheduler().scheduleSyncRepeatingTask(Cherry.plugin, new Runnable() {
      @Override
      public void run() {
        applyWorldRandomTick(world);
      }
    }, 0L, 1L);

  }

  public static void enable() {
    if (!Cherry.config.getBoolean("wanyfield")) {
      return;
    }

    randomTickScheduler();

  }

}
