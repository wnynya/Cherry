package io.wany.cherry.wand.brush;

import io.wany.cherry.wand.Wand;
import io.wany.cherry.wand.brush.touch.Cube;
import io.wany.cherry.wand.brush.touch.SpherePoint;
import io.wany.cherry.wand.brush.touch.SphereRound;
import org.bukkit.Location;

public enum Brush implements BrushToucher {

  CUBE {
    @Override
    public void touch(Location location, Wand wand, boolean applyPhysics) {
      new Cube().touch(location, wand, applyPhysics);
    }
  }, BALL_POINT {
    @Override
    public void touch(Location location, Wand wand, boolean applyPhysics) {
      new SpherePoint().touch(location, wand, applyPhysics);
    }
  }, BALL_ROUND {
    @Override
    public void touch(Location location, Wand wand, boolean applyPhysics) {
      new SphereRound().touch(location, wand, applyPhysics);
    }
  }

}
