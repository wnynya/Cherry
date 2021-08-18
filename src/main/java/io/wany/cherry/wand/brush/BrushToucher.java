package io.wany.cherry.wand.brush;

import io.wany.cherry.wand.Wand;
import org.bukkit.Location;

public interface BrushToucher {

  void touch(Location location, Wand wand, boolean applyPhysics);

}
