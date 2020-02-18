package com.wnynya.cherry.wand.brush.touch;

import com.wnynya.cherry.Msg;
import com.wnynya.cherry.wand.Wand;
import com.wnynya.cherry.wand.WandBrush;
import com.wnynya.cherry.wand.area.Area;
import com.wnynya.cherry.wand.brush.BrushSetting;
import com.wnynya.cherry.wand.brush.BrushToucher;
import org.bukkit.Location;

import java.util.List;

public class Cube implements BrushToucher {

  public Cube() {}

  @Override
  public void touch(Location location, Wand wand, boolean applyPhysics) {
    WandBrush brush = wand.getBrush();
    BrushSetting setting = brush.getSetting();
    List<Location> area = Area.CUBE.getArea(location, setting.getSize());
    wand.fill(setting.getMaterial(), area, applyPhysics);
  }

}
