package com.wnynya.cherry.wand.area;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Wall {

  public static List<Location> getArea(Location pos1, Location pos2) {
    if (pos1 == null || pos2 == null || !Objects.equals(pos1.getWorld(), pos2.getWorld())) {
      return null;
    }

    double minX = Math.min(pos1.getX(), pos2.getX());
    double maxX = Math.max(pos1.getX(), pos2.getX());
    double minY = Math.min(pos1.getY(), pos2.getY());
    double maxY = Math.max(pos1.getY(), pos2.getY());
    double minZ = Math.min(pos1.getZ(), pos2.getZ());
    double maxZ = Math.max(pos1.getZ(), pos2.getZ());

    double xx = maxX - minX;
    double zz = maxZ - minZ;

    boolean x1 = pos1.getX() < pos2.getX();
    xx = (x1) ? xx : -xx;

    boolean z1 = pos1.getZ() < pos2.getZ();
    zz = (z1) ? zz : -zz;

    double zxMax = Math.max(Math.abs(xx), Math.abs(zz));

    Vector step = new Vector((float) xx / zxMax, 0, (float) zz / zxMax);

    Location startPos = pos1.clone();
    startPos.setDirection(step);

    List<Location> listT = new ArrayList<>();
    for (int i = 0; i <= zxMax; i++) {

      Location loc = new Location(pos1.getWorld(), (int) startPos.getX(), (int) minY, (int) startPos.getZ());
      startPos.add(step);
      listT.add(loc);
    }

    List<Location> list = new ArrayList<>();
    for (int y = (int) minY; y <= (int) maxY; y++) {
      for (Location locc : listT) {
        Location loc = new Location(locc.getWorld(), locc.getX(), y, locc.getZ());
        list.add(loc);
      }
    }

    return list;
  }

}
