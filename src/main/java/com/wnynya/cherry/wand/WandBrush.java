package com.wnynya.cherry.wand;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Tool;
import com.wnynya.cherry.wand.brush.Brush;
import com.wnynya.cherry.wand.brush.BrushSetting;
import org.bukkit.Location;
import org.bukkit.Material;

public class WandBrush {

  private Wand wand;
  private BrushSetting setting;
  private Brush brush;

  WandBrush(Wand wand) {
    this.setting = new BrushSetting(wand);
    this.brush = Brush.CUBE;
    this.wand = wand;
  }

  public void touch(Location loc, boolean applyPhysics) {
    this.brush.touch(loc, this.wand, applyPhysics);
  }

  public void touch(Location loc) {
    this.touch(loc, false);
  }

  public BrushSetting getSetting() {
    return setting;
  }

  public Brush getBrush() {
    return brush;
  }

  /* ==== ==== ==== ==== */

  public static Material nonPhysicsBrushItem, physicsBrushItem;

  public static void init() {
    nonPhysicsBrushItem = Material.SPECTRAL_ARROW;
    physicsBrushItem = Material.ARROW;
    String nonPhysics = Cherry.config.getString("wand.brush.non-physics-item");
    String physics = Cherry.config.getString("wand.brush.physics-item");
    if (Tool.Check.isMaterial(nonPhysics)) {
      nonPhysicsBrushItem = Material.getMaterial(nonPhysics);
    }
    if (Tool.Check.isMaterial(physics)) {
      physicsBrushItem = Material.getMaterial(physics);
    }
  }

}
