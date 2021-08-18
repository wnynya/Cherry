package io.wany.cherry.wand.brush.touch;


import io.wany.cherry.wand.Wand;
import io.wany.cherry.wand.area.Area;
import io.wany.cherry.wand.brush.BrushSetting;
import io.wany.cherry.wand.brush.BrushToucher;
import org.bukkit.Location;

import java.util.List;

public class SpherePoint implements BrushToucher {

  public SpherePoint() {
  }

  @Override
  public void touch(Location location, Wand wand, boolean applyPhysics) {
    BrushSetting setting = wand.getBrush().setting;
    List<Location> area = Area.SPHERE_POINTED.getArea(location, setting.getSize());
    wand.storeUndo(area);
    wand.fill(setting.getBlockData(), area, applyPhysics);
  }

}
