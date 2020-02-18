package com.wnynya.cherry.farm;

import org.bukkit.Material;

public enum Crop implements CropType{

  POTATO {
    @Override
    public int[] getUsing() {
      return new int[]{100, 100, 100};
    }

    @Override
    public int[] getMinimum() {
      return new int[]{5000, 5000, 5000};
    }

    @Override
    public int[] getMaximum() {
      return new int[]{15000, 15000, 15000};
    }

    @Override
    public int[] getBest() {
      return new int[]{12000, 12000, 12000};
    }

    @Override
    public Material getHarvest() {
      return Material.POTATO;
    }

    @Override
    public Material getSeed() {
      return Material.POTATO;
    }

    @Override
    public Material getBonus() {
      return Material.GOLD_NUGGET;
    }

    @Override
    public Material getPenalty() {
      return Material.POISONOUS_POTATO;
    }
  }

}
