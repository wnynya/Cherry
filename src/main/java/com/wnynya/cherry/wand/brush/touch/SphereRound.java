package com.wnynya.cherry.wand.brush.touch;

import com.wnynya.cherry.wand.Wand;
import com.wnynya.cherry.wand.area.Area;
import com.wnynya.cherry.wand.brush.BrushSetting;
import com.wnynya.cherry.wand.brush.BrushToucher;
import org.bukkit.Location;

import java.util.List;

public class SphereRound implements BrushToucher {

  public SphereRound() {}

  @Override
  public void touch(Location location, Wand wand, boolean applyPhysics) {
    BrushSetting setting = wand.getBrush().getSetting();
    List<Location> area = Area.SPHERE.getArea(location, setting.getSize());
    wand.fill(setting.getMaterial(), area, applyPhysics);
  }

}
