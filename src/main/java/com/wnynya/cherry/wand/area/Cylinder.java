package com.wnynya.cherry.wand.area;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Cylinder {

  public static List<Location> getAreaFilled(Location pos, int r, int h) {
    Location pos1 = new Location(pos.getWorld(), pos.getX() + r, pos.getY(), pos.getZ() + r);
    Location pos2 = new Location(pos.getWorld(), pos.getX() - r, pos.getY(), pos.getZ() - r);

    double minX = Math.min(pos1.getX(), pos2.getX());
    double maxX = Math.max(pos1.getX(), pos2.getX());
    double minY = Math.min(pos1.getY(), pos2.getY());
    double maxY = Math.max(pos1.getY(), pos2.getY());
    double minZ = Math.min(pos1.getZ(), pos2.getZ());
    double maxZ = Math.max(pos1.getZ(), pos2.getZ());

    List<Location> tempList = new ArrayList<>();
    for (int x = (int) minX; x <= maxX; x++) {
      for (int z = (int) minZ; z <= maxZ; z++) {
        Location loc = new Location(pos1.getWorld(), x, minY, z);
        if ((int) Math.round(pos.distance(loc)) <= r) {
          tempList.add(loc);
        }
      }
    }

    List<Location> list = new ArrayList<>();
    for (int y = (int) minY; y < (minY + h); y++) {
      for (Location loc : tempList) {
        Location locc = new Location(pos.getWorld(), loc.getX(), y, loc.getZ());
        list.add(locc);
      }
    }

    return list;
  }

  public static List<Location> getAreaEmpty(Location pos, int r, int h) {
    Location pos1 = new Location(pos.getWorld(), pos.getX() + r, pos.getY(), pos.getZ() + r);
    Location pos2 = new Location(pos.getWorld(), pos.getX() - r, pos.getY(), pos.getZ() - r);

    double minX = Math.min(pos1.getX(), pos2.getX());
    double maxX = Math.max(pos1.getX(), pos2.getX());
    double minY = Math.min(pos1.getY(), pos2.getY());
    double maxY = Math.max(pos1.getY(), pos2.getY());
    double minZ = Math.min(pos1.getZ(), pos2.getZ());
    double maxZ = Math.max(pos1.getZ(), pos2.getZ());

    List<Location> tempList = new ArrayList<>();

    int[][] array = new int[][]{{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};

    for (int x = (int) minX; x <= maxX; x++) {
      for (int z = (int) minZ; z <= maxZ; z++) {
        Location loc = new Location(pos.getWorld(), x, minY, z);
        for (int[] ints : array) {
          if (x >= pos.getX() && z >= pos.getZ()) {
            Location locX = new Location(pos.getWorld(), x + 1, minY, z);
            Location locZ = new Location(pos.getWorld(), x, minY, z + 1);
            if ((r - 1 < (int) Math.round(pos.distance(locX)) && (int) Math.round(pos.distance(locX)) <= r) && (r - 1 < (int) Math.round(pos.distance(locZ)) && (int) Math.round(pos.distance(locZ)) <= r)) {
              continue;
            }
          }
        }
        if (x >= pos.getX() && z >= pos.getZ()) {
          Location locX = new Location(pos.getWorld(), x + 1, minY, z);
          Location locZ = new Location(pos.getWorld(), x, minY, z + 1);
          if ((r - 1 < (int) Math.round(pos.distance(locX)) && (int) Math.round(pos.distance(locX)) <= r) && (r - 1 < (int) Math.round(pos.distance(locZ)) && (int) Math.round(pos.distance(locZ)) <= r)) {
            continue;
          }
        }
        if (x >= pos.getX() && z < pos.getZ()) {
          Location locX = new Location(pos.getWorld(), x + 1, minY, z);
          Location locZ = new Location(pos.getWorld(), x, minY, z - 1);
          if ((r - 1 < (int) Math.round(pos.distance(locX)) && (int) Math.round(pos.distance(locX)) <= r) && (r - 1 < (int) Math.round(pos.distance(locZ)) && (int) Math.round(pos.distance(locZ)) <= r)) {
            continue;
          }
        }
        if (x < pos.getX() && z >= pos.getZ()) {
          Location locX = new Location(pos.getWorld(), x - 1, minY, z);
          Location locZ = new Location(pos.getWorld(), x, minY, z + 1);
          if ((r - 1 < (int) Math.round(pos.distance(locX)) && (int) Math.round(pos.distance(locX)) <= r) && (r - 1 < (int) Math.round(pos.distance(locZ)) && (int) Math.round(pos.distance(locZ)) <= r)) {
            continue;
          }
        }
        if (x < pos.getX() && z < pos.getZ()) {
          Location locX = new Location(pos.getWorld(), x - 1, minY, z);
          Location locZ = new Location(pos.getWorld(), x, minY, z - 1);
          if ((r - 1 < (int) Math.round(pos.distance(locX)) && (int) Math.round(pos.distance(locX)) <= r) && (r - 1 < (int) Math.round(pos.distance(locZ)) && (int) Math.round(pos.distance(locZ)) <= r)) {
            continue;
          }
        }
        if ((r - 1 < (int) Math.round(pos.distance(loc)) && (int) Math.round(pos.distance(loc)) <= r)) {
          tempList.add(loc);
        }
      }
    }

    List<Location> list = new ArrayList<>();
    for (int y = (int) minY; y < (minY + h); y++) {
      for (Location loc : tempList) {
        Location locc = new Location(pos.getWorld(), loc.getX(), y, loc.getZ());
        list.add(locc);
      }
    }

    return list;
  }

  public static List<Location> getAreaPointFilled(Location pos, int r, int h) {
    Location pos1 = new Location(pos.getWorld(), pos.getX() + r, pos.getY(), pos.getZ() + r);
    Location pos2 = new Location(pos.getWorld(), pos.getX() - r, pos.getY(), pos.getZ() - r);

    double minX = Math.min(pos1.getX(), pos2.getX());
    double maxX = Math.max(pos1.getX(), pos2.getX());
    double minY = Math.min(pos1.getY(), pos2.getY());
    double maxY = Math.max(pos1.getY(), pos2.getY());
    double minZ = Math.min(pos1.getZ(), pos2.getZ());
    double maxZ = Math.max(pos1.getZ(), pos2.getZ());

    List<Location> tempList = new ArrayList<>();
    for (int x = (int) minX; x <= maxX; x++) {
      for (int z = (int) minZ; z <= maxZ; z++) {
        Location loc = new Location(pos1.getWorld(), x, minY, z);
        if (pos.distance(loc) <= r) {
          tempList.add(loc);
        }
      }
    }

    List<Location> list = new ArrayList<>();
    for (int y = (int) minY; y < (minY + h); y++) {
      for (Location loc : tempList) {
        Location locc = new Location(pos.getWorld(), loc.getX(), y, loc.getZ());
        list.add(locc);
      }
    }

    return list;
  }

  public static List<Location> getAreaPointEmpty(Location pos, int r, int h) {
    Location pos1 = new Location(pos.getWorld(), pos.getX() + r, pos.getY(), pos.getZ() + r);
    Location pos2 = new Location(pos.getWorld(), pos.getX() - r, pos.getY(), pos.getZ() - r);

    double minX = Math.min(pos1.getX(), pos2.getX());
    double maxX = Math.max(pos1.getX(), pos2.getX());
    double minY = Math.min(pos1.getY(), pos2.getY());
    double maxY = Math.max(pos1.getY(), pos2.getY());
    double minZ = Math.min(pos1.getZ(), pos2.getZ());
    double maxZ = Math.max(pos1.getZ(), pos2.getZ());

    List<Location> tempList = new ArrayList<>();

    int[][] array = new int[][]{{1, 1}, {-1, 1}, {1, -1}, {-1, -1}};

    for (int x = (int) minX; x <= maxX; x++) {
      for (int z = (int) minZ; z <= maxZ; z++) {
        Location loc = new Location(pos.getWorld(), x, minY, z);
        for (int[] ints : array) {
          if (x >= pos.getX() && z >= pos.getZ()) {
            Location locX = new Location(pos.getWorld(), x + 1, minY, z);
            Location locZ = new Location(pos.getWorld(), x, minY, z + 1);
            if ((r - 1 < pos.distance(locX) && pos.distance(locX) <= r) && (r - 1 < pos.distance(locZ) && pos.distance(locZ) <= r)) {
              continue;
            }
          }
        }
        if (x >= pos.getX() && z >= pos.getZ()) {
          Location locX = new Location(pos.getWorld(), x + 1, minY, z);
          Location locZ = new Location(pos.getWorld(), x, minY, z + 1);
          if ((r - 1 < pos.distance(locX) && pos.distance(locX) <= r) && (r - 1 < pos.distance(locZ) && pos.distance(locZ) <= r)) {
            continue;
          }
        }
        if (x >= pos.getX() && z < pos.getZ()) {
          Location locX = new Location(pos.getWorld(), x + 1, minY, z);
          Location locZ = new Location(pos.getWorld(), x, minY, z - 1);
          if ((r - 1 < pos.distance(locX) && pos.distance(locX) <= r) && (r - 1 < pos.distance(locZ) && pos.distance(locZ) <= r)) {
            continue;
          }
        }
        if (x < pos.getX() && z >= pos.getZ()) {
          Location locX = new Location(pos.getWorld(), x - 1, minY, z);
          Location locZ = new Location(pos.getWorld(), x, minY, z + 1);
          if ((r - 1 < pos.distance(locX) && pos.distance(locX) <= r) && (r - 1 < pos.distance(locZ) && pos.distance(locZ) <= r)) {
            continue;
          }
        }
        if (x < pos.getX() && z < pos.getZ()) {
          Location locX = new Location(pos.getWorld(), x - 1, minY, z);
          Location locZ = new Location(pos.getWorld(), x, minY, z - 1);
          if ((r - 1 < pos.distance(locX) && pos.distance(locX) <= r) && (r - 1 < pos.distance(locZ) && pos.distance(locZ) <= r)) {
            continue;
          }
        }
        if ((r - 1 < pos.distance(loc) && pos.distance(loc) <= r)) {
          tempList.add(loc);
        }
      }
    }

    List<Location> list = new ArrayList<>();
    for (int y = (int) minY; y < (minY + h); y++) {
      for (Location loc : tempList) {
        Location locc = new Location(pos.getWorld(), loc.getX(), y, loc.getZ());
        list.add(locc);
      }
    }

    return list;
  }

}
