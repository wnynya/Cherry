package com.wnynya.cherry.wand.area;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Sphere {

  public static List<Location> getAreaFilled(Location pos, int r) {
    Location pos1 = new Location(pos.getWorld(), pos.getX() + r, pos.getY() + r, pos.getZ() + r);
    Location pos2 = new Location(pos.getWorld(), pos.getX() - r, pos.getY() - r, pos.getZ() - r);

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
          Location loc = new Location(pos1.getWorld(), x, y, z);
          if ((int) Math.round(pos.distance(loc)) <= r) {
            list.add(loc);
          }
        }
      }
    }
    return list;
  }

  // 비어있는 구 형태의 에리어를 만들꺼임 (아마도)
  public static List<Location> getAreaEmpty(Location pos, int r) {
    Location pos1 = new Location(pos.getWorld(), pos.getX() + r, pos.getY() + r, pos.getZ() + r);
    Location pos2 = new Location(pos.getWorld(), pos.getX() - r, pos.getY() - r, pos.getZ() - r);

    double minX = Math.min(pos1.getX(), pos2.getX());
    double maxX = Math.max(pos1.getX(), pos2.getX());
    double minY = Math.min(pos1.getY(), pos2.getY());
    double maxY = Math.max(pos1.getY(), pos2.getY());
    double minZ = Math.min(pos1.getZ(), pos2.getZ());
    double maxZ = Math.max(pos1.getZ(), pos2.getZ());

    List<Location> list = new ArrayList<>();

    for (int x = (int) minX; x <= maxX; x++) {
      for (int y = (int) minY; y <= maxY; y++) {
        z:for (int z = (int) minZ; z <= maxZ; z++) {
          Location loc = new Location(pos1.getWorld(), x, y, z); // 테스트할 위치
          int[] array = new int[3];
          if (x >= pos.getX()) {
            array[0] = 1;
          }
          else {
            array[0] = -1;
          }
          if (y >= pos.getY()) {
            array[1] = 1;
          }
          else {
            array[1] = -1;
          }
          if (z >= pos.getZ()) {
            array[2] = 1;
          }
          else {
            array[2] = -1;
          }
          if (check(pos, x, y, z, r, array)) {
            continue;
          }
          if (r - 1 < (int) Math.round(pos.distance(loc)) && (int) Math.round(pos.distance(loc)) <= r) {
            list.add(loc);
          }
        }
      }
    }
    return list;
  }

  private static boolean check(Location pos, int x, int y, int z, int r, int[] ints) {
    int locX = (int) Math.round(pos.distance(new Location(pos.getWorld(), x + ints[0], y, z)));
    int locY = (int) Math.round(pos.distance(new Location(pos.getWorld(), x, y + ints[1], z)));
    int locZ = (int) Math.round(pos.distance(new Location(pos.getWorld(), x, y, z + ints[2])));
    if ((r - 1 < locX && locX <= r) && (r - 1 < locY && locY <= r) && (r - 1 < locZ && locZ <= r)) {
      return true;
    }
    return false;
  }

  public static List<Location> getAreaPoint(Location pos, int r) {
    Location pos1 = new Location(pos.getWorld(), pos.getX() + r, pos.getY() + r, pos.getZ() + r);
    Location pos2 = new Location(pos.getWorld(), pos.getX() - r, pos.getY() - r, pos.getZ() - r);

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
          Location loc = new Location(pos1.getWorld(), x, y, z);
          if (pos.distance(loc) <= r) {
            list.add(loc);
          }
        }
      }
    }
    return list;
  }

  public static List<Location> getAreaPointEmpty(Location pos, int r) {
    // 작동 안함
    Location pos1 = new Location(pos.getWorld(), pos.getX() + r, pos.getY() + r, pos.getZ() + r);
    Location pos2 = new Location(pos.getWorld(), pos.getX() - r, pos.getY() - r, pos.getZ() - r);

    double minX = Math.min(pos1.getX(), pos2.getX());
    double maxX = Math.max(pos1.getX(), pos2.getX());
    double minY = Math.min(pos1.getY(), pos2.getY());
    double maxY = Math.max(pos1.getY(), pos2.getY());
    double minZ = Math.min(pos1.getZ(), pos2.getZ());
    double maxZ = Math.max(pos1.getZ(), pos2.getZ());

    List<Location> list = new ArrayList<>();

    int[][] array = new int[][]{{1, 1, 1}, {-1, 1, 1}, {1, -1, 1}, {1, 1, -1}, {-1, -1, 1}, {1, -1, -1}, {-1, 1, -1}, {-1, -1, -1},};

    for (int x = (int) minX; x <= maxX; x++) {
      for (int y = (int) minY; y <= maxY; y++) {
        z:
        for (int z = (int) minZ; z <= maxZ; z++) {
          Location loc = new Location(pos1.getWorld(), x, y, z);
          for (int[] ints : array) {
            if (x >= pos.getX() && y >= pos.getY() && z >= pos.getZ()) {
              double locX = pos.distance(new Location(pos.getWorld(), x + ints[0], y, z));
              double locY = pos.distance(new Location(pos.getWorld(), x, y + ints[1], z));
              double locZ = pos.distance(new Location(pos.getWorld(), x, y, z + ints[2]));
              if ((r - 1 < locX && locX <= r) && (r - 1 < locY && locY <= r) && (r - 1 < locZ && locZ <= r)) {
                continue z;
              }
            }
          }
          if (r - 1 < pos.distance(loc) && pos.distance(loc) <= r) {
            list.add(loc);
          }
        }
      }
    }
    return list;
  }

}
