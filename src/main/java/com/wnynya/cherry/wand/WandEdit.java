package com.wnynya.cherry.wand;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;

public class WandEdit {

  private Wand wand;

  public WandEdit(Wand wand) {
    this.wand = wand;
  }

  private HashMap<Integer, Location> position = new HashMap<>();

  public boolean setPosition(int n, Location pos) {
    if (this.getPosition(n) == null || !this.getPosition(n).equals(pos)) {
      this.position.put(n, pos);
      return true;
    }
    return false;
  }

  public Location getPosition(int n) {
    return this.position.get(n);
  }

}
