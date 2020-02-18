package com.wnynya.cherry.wand.brush;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.wand.Wand;
import org.bukkit.Material;

public class BrushSetting {

  private Wand wand;
  private int size = 5;
  private Material material = Material.STONE;

  public BrushSetting(Wand wand) {
    this.wand = wand;
  }

  public void setSize(int size) {
    int max = Cherry.config.getInt("wand.brush.max-radius");
    if (size > max) {
      size = max;
    }
    if (size < 0) {
      size = 0;
    }
    this.size = size;
  }

  public void setMaterial(Material material) {
    if (material == null || !material.isBlock()) {
      return;
    }
    this.material = material;
  }

  public int getSize() {
    return size;
  }

  public Material getMaterial() {
    return material;
  }

}
