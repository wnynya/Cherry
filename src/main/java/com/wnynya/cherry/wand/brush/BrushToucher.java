package com.wnynya.cherry.wand.brush;

import com.wnynya.cherry.wand.Wand;
import org.bukkit.Location;

public interface BrushToucher {

  public void touch(Location location, Wand wand, boolean applyPhysics);

}
