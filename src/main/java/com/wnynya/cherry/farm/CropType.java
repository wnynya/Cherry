package com.wnynya.cherry.farm;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface CropType {

  public int[] getUsing();

  public int[] getMinimum();

  public int[] getMaximum();

  public int[] getBest();

  public Material getHarvest();

  public Material getSeed();

  public Material getBonus();

  public Material getPenalty();

}
