package io.wany.cherry.amethyst;

import io.wany.cherry.Cherry;
import io.wany.cherry.Console;
import io.wany.cherry.Message;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RandomTick {

  private static final ExecutorService executorService = Executors.newFixedThreadPool(1);
  private static BukkitTask bukkitTask1t;
  private static final Random random = new Random();

  public static void tick(World world) {
    int speed = world.getGameRuleValue(GameRule.RANDOM_TICK_SPEED);
    Chunk[] chunks = world.getLoadedChunks();
    for (Chunk chunk : chunks) {
      tick(chunk, speed);
    }
    cc += chunks.length;
  }

  public static void tick(Chunk chunk, int count) {
    for (int i = 0; i < count; i++) {
      int x = random.nextInt(16);
      int y = random.nextInt(256);
      int z = random.nextInt(16);
      tick(chunk.getBlock(x, y, z));
    }
  }

  public static void tick(Block block) {
    Material material = block.getType();
    switch (material) {
      case GRASS_BLOCK -> {
        gbc++;
        Block target = block.getLocation().add(0, 1, 0).getBlock();
        Block up = target.getLocation().add(0, 1, 0).getBlock();
        Block east = target.getLocation().add(1, 0, 0).getBlock();
        Block west = target.getLocation().add(-1, 0, 0).getBlock();
        Block south = target.getLocation().add(0, 0, 1).getBlock();
        Block north = target.getLocation().add(0, 0, -1).getBlock();
        if (!target.getType().equals(Material.AIR)
          || !target.getBiome().equals(Biome.FOREST)
          || !up.getType().equals(Material.AIR)
          || !east.getType().equals(Material.AIR)
          || !west.getType().equals(Material.AIR)
          || !south.getType().equals(Material.AIR)
          || !north.getType().equals(Material.AIR)
        ) {
          return;
        }
        if (random.nextInt(100) < 10) {
        }
      }
    }
  }

  private static Block upperBlock(Block block) {
    return block.getLocation().add(0, 1, 0).getBlock();
  }

  private static int cc;
  private static int gbc;

  public static void onEnable() {
    executorService.submit(() -> {
      bukkitTask1t = Bukkit.getScheduler().runTaskTimerAsynchronously(Cherry.PLUGIN, new Runnable() {
        @Override
        public void run() {
          cc = 0;
          gbc = 0;
          for (World world : Bukkit.getWorlds()) {
            tick(world);
          }
          for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(Message.parse("loaded: " + cc + " grass: " + gbc));
          }
        }
      }, 0L, 1L);
    });
  }

  public static void onDisable() {
    bukkitTask1t.cancel();
    executorService.shutdownNow();
  }

}
