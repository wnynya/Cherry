package com.wnynya.cherry.wand.area;

import org.bukkit.Location;

import java.util.List;

public interface AreaGenerator {

  public List<Location> getArea(Location pos1, Location pos2);

  public List<Location> getArea(Location pos, int r);

  public List<Location> getArea(Location pos, int r, int h);

}
