package com.wnynya.cherry.farm;

import com.wnynya.cherry.Msg;
import com.wnynya.cherry.amethyst.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Soil {

  private Config soilConfig = new Config("data/farm/soil", true);

  private static HashMap <Location, Soil> soilMap = new HashMap<>();

  private Location loc;

  private int N;
  private int P;
  private int K;

  String path = "";

  public Soil(Location loc) {
    this.loc = loc;

    Block block = loc.getBlock();

    if (!block.getType().equals(Material.FARMLAND)) {
      return;
    }

    Biome biome = block.getBiome();

    block.getHumidity();

    block.getTemperature();

    path = loc.getWorld().getName() + "_" + (int) loc.getX() + "_" + (int) loc.getY() + "_" + (int) loc.getZ();

    if (soilConfig.getConfig().isConfigurationSection(path)) {
      this.N = soilConfig.getConfig().getInt(path + ".n");
      this.P = soilConfig.getConfig().getInt(path + ".p");
      this.K = soilConfig.getConfig().getInt(path + ".k");
    }
    else {
      this.N = 10000;
      this.P = 10000;
      this.K = 10000;
    }

    update();

    soilMap.put(loc, this);
  }

  private void update () {
    soilConfig.set(path + ".n", this.N);
    soilConfig.set(path + ".p", this.P);
    soilConfig.set(path + ".k", this.K);
  }

  public void harvest (Crop crop) {
    int badScore = 0;
    int goodScore = 0;
    int[] mn = crop.getMinimum();
    int[] mx = crop.getMaximum();
    if (this.N < mn[0]) {
      badScore += 1;
    }
    if (this.P < mn[0]) {
      badScore += 1;
    }
    if (this.K < mn[0]) {
      badScore += 1;
    }
    if (this.N > mx[0]) {
      badScore += 1;
    }
    if (this.P > mx[0]) {
      badScore += 1;
    }
    if (this.K > mx[0]) {
      badScore += 1;
    }
    int[] b = crop.getBest();
    Msg.info((float) b[0] - (b[0] * (10.0/100.0)) + "");
    Msg.info((float) b[0] + (b[0] * (10.0/100.0)) + "");
    if ((float) b[0] - (b[0] * (10.0/100.0)) < this.N && this.N < (float) b[0] + (b[0] * (10.0/100.0))) {
      goodScore += 2;
    }
    if ((float) b[1] - (b[1] * (10.0/100.0)) < this.P && this.P < (float) b[1] + (b[1] * (10.0/100.0))) {
      goodScore += 2;
    }
    if ((float) b[2] - (b[2] * (10.0/100.0)) < this.K && this.K < (float) b[2] + (b[2] * (10.0/100.0))) {
      goodScore += 2;
    }

    int score = goodScore - badScore;

    Location newLoc = new Location(
      loc.getWorld(),
      loc.getX(),
      loc.getY(),
      loc.getZ()
    );

    if (score >= 4) {
      loc.getWorld().dropItemNaturally(newLoc, new ItemStack(crop.getHarvest(), 1 + (score) ));
      loc.getWorld().dropItemNaturally(newLoc, new ItemStack(crop.getSeed(), 1 ));
      loc.getWorld().dropItemNaturally(newLoc, new ItemStack(crop.getBonus(), 1 ));
      Msg.info("A" + score);
    }
    else if (score >= 2) {
      loc.getWorld().dropItemNaturally(newLoc, new ItemStack(crop.getHarvest(), 1 + (score) ));
      loc.getWorld().dropItemNaturally(newLoc, new ItemStack(crop.getSeed(), 1 ));
      Msg.info("B" + score);
    }
    else if (score >= 0) {
      loc.getWorld().dropItemNaturally(newLoc, new ItemStack(crop.getHarvest(), 2 ));
      loc.getWorld().dropItemNaturally(newLoc, new ItemStack(crop.getSeed(), 1 ));
      Msg.info("C" + score);
    }
    else if (score >= -1) {
      loc.getWorld().dropItemNaturally(newLoc, new ItemStack(crop.getSeed(), 1 ));
      loc.getWorld().dropItemNaturally(newLoc, new ItemStack(crop.getPenalty(), 2 ));
      Msg.info("D" + score);
    }
    else if (score >= -2) {
      loc.getWorld().dropItemNaturally(newLoc, new ItemStack(crop.getPenalty(), 2 ));
      Msg.info("E" + score);
    }
    else {
      loc.getWorld().dropItemNaturally(newLoc, new ItemStack(crop.getPenalty(), 1 ));
      Msg.info("F" + score);
    }

    int[] u = crop.getUsing();
    this.N -= u[0];
    this.P -= u[1];
    this.K -= u[2];

    update();
  }

  public void fertilize (Fertilizer fertilizer) {
    switch (fertilizer) {
      case FP:
        N += 200;
        P += 200;
        K += 200;
        break;
      case FM:
        N -= 200;
        P -= 200;
        K -= 200;
        break;
    }

    update();
  }

  public int getK() {
    return K;
  }

  public int getN() {
    return N;
  }

  public int getP() {
    return P;
  }

  public static Soil getSoil(Location loc) {
    if (soilMap.containsKey(loc)) {
      return soilMap.get(loc);
    }
    else {
      return new Soil(loc);
    }
  }

}
