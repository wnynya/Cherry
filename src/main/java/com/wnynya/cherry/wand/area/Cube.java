package com.wnynya.cherry.wand.area;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cube {

  public static List<Location> getAreaFilled(Location pos1, Location pos2) {
    if (pos1 == null || pos2 == null || !Objects.equals(pos1.getWorld(), pos2.getWorld())) {
      return null;
    }

    double minX = Math.min(pos1.getX(), pos2.getX());
    double maxX = Math.max(pos1.getX(), pos2.getX());
    double minY = Math.min(pos1.getY(), pos2.getY());
    double maxY = Math.max(pos1.getY(), pos2.getY());
    double minZ = Math.min(pos1.getZ(), pos2.getZ());
    double maxZ = Math.max(pos1.getZ(), pos2.getZ());

    List<Location> list = new ArrayList<>();
    for (int x = (int) minX; x <= maxX; x++) {
      for (int y = (int) minY; y <= maxY; y++) {
        for (int z = (int) minZ; z <= maxZ; z++) {
          list.add(new Location(pos1.getWorld(), x, y, z));
        }
      }
    }

    return list;
  }

  public static List<Location> getAreaEmpty(Location pos1, Location pos2) {
    if (pos1 == null || pos2 == null || !Objects.equals(pos1.getWorld(), pos2.getWorld())) {
      return null;
    }

    double minX = Math.min(pos1.getX(), pos2.getX());
    double maxX = Math.max(pos1.getX(), pos2.getX());
    double minY = Math.min(pos1.getY(), pos2.getY());
    double maxY = Math.max(pos1.getY(), pos2.getY());
    double minZ = Math.min(pos1.getZ(), pos2.getZ());
    double maxZ = Math.max(pos1.getZ(), pos2.getZ());

    List<Location> list = new ArrayList<>();
    for (int x = (int) minX; x <= maxX; x++) {
      for (int y = (int) minY; y <= maxY; y++) {
        for (int z = (int) minZ; z <= maxZ; z++) {
          if (x == (int) minX
            || x == (int) maxX
            || y == (int) minY
            || y == (int) maxY
            || z == (int) minZ
            || z == (int) maxZ
          ) {
            list.add(new Location(pos1.getWorld(), x, y, z));
          }
        }
      }
    }

    return list;
  }

  public static List<Location> getAreaWall(Location pos1, Location pos2) {
    if (pos1 == null || pos2 == null || !Objects.equals(pos1.getWorld(), pos2.getWorld())) {
      return null;
    }

    double minX = Math.min(pos1.getX(), pos2.getX());
    double maxX = Math.max(pos1.getX(), pos2.getX());
    double minY = Math.min(pos1.getY(), pos2.getY());
    double maxY = Math.max(pos1.getY(), pos2.getY());
    double minZ = Math.min(pos1.getZ(), pos2.getZ());
    double maxZ = Math.max(pos1.getZ(), pos2.getZ());

    List<Location> list = new ArrayList<>();
    for (int x = (int) minX; x <= maxX; x++) {
      for (int y = (int) minY; y <= maxY; y++) {
        for (int z = (int) minZ; z <= maxZ; z++) {
          if (x == (int) minX
            || x == (int) maxX
            || z == (int) minZ
            || z == (int) maxZ
          ) {
            list.add(new Location(pos1.getWorld(), x, y, z));
          }
        }
      }
    }

    return list;
  }

}
