package com.wnynya.cherry.wand.brush;

import com.wnynya.cherry.wand.Wand;
import com.wnynya.cherry.wand.brush.touch.*;
import org.bukkit.Location;

public enum Brush implements BrushToucher{

  CUBE {
    @Override
    public void touch(Location location, Wand wand, boolean applyPhysics) {
      new Cube().touch(location, wand, applyPhysics);
    }
  },
  BALL_POINT {
    @Override
    public void touch(Location location, Wand wand, boolean applyPhysics) {
      new SpherePoint().touch(location, wand, applyPhysics);
    }
  },
  BALL_ROUND {
    @Override
    public void touch(Location location, Wand wand, boolean applyPhysics) {
      new SphereRound().touch(location, wand, applyPhysics);
    }
  };

}
