package io.wany.cherry.wand.area;

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
          if (x == (int) minX || x == (int) maxX || y == (int) minY || y == (int) maxY || z == (int) minZ || z == (int) maxZ) {
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
          if (x == (int) minX || x == (int) maxX || z == (int) minZ || z == (int) maxZ) {
            list.add(new Location(pos1.getWorld(), x, y, z));
          }
        }
      }
    }

    return list;
  }

  public static List<Location> getAreaParticle(Location pos1, Location pos2) {
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
          if ((x == (int) maxX && y == (int) maxY) || (y == (int) maxY && z == (int) maxZ) || (x == (int) maxX && z == (int) maxZ) || (x == (int) minX && y == (int) minY) || (y == (int) minY && z == (int) minZ) || (x == (int) minX && z == (int) minZ) || (x == (int) maxX && y == (int) minY) || (x == (int) maxX && z == (int) minZ) || (y == (int) maxY && x == (int) minX) || (y == (int) maxY && z == (int) minZ) || (z == (int) maxZ && x == (int) minX) || (z == (int) maxZ && y == (int) minY)) {
            double x1 = x;
            double y1 = y;
            double z1 = z;

            if ((x == (int) maxX)) {
              x1 += 1;
            }
            if ((y == (int) maxY)) {
              y1 += 1;
            }
            if ((z == (int) maxZ)) {
              z1 += 1;
            }

            if ((maxX == minX) && (maxY == minY) && (maxZ == minZ)) {
              list.add(new Location(pos1.getWorld(), x, y1, z1));
              list.add(new Location(pos1.getWorld(), x1, y, z1));
              list.add(new Location(pos1.getWorld(), x1, y1, z));
              list.add(new Location(pos1.getWorld(), x1, y, z));
              list.add(new Location(pos1.getWorld(), x, y1, z));
              list.add(new Location(pos1.getWorld(), x, y, z1));
              list.add(new Location(pos1.getWorld(), x, y, z));
            }
            else if ((maxX == minX) && (maxY == minY)) {
              list.add(new Location(pos1.getWorld(), x, y1, z1));
              list.add(new Location(pos1.getWorld(), x1, y, z1));
              list.add(new Location(pos1.getWorld(), x1, y1, z));
              list.add(new Location(pos1.getWorld(), x1, y, z));
              list.add(new Location(pos1.getWorld(), x, y1, z));
              list.add(new Location(pos1.getWorld(), x, y, z1));
              list.add(new Location(pos1.getWorld(), x, y, z));
            }
            else if ((maxY == minY) && (maxZ == minZ)) {
              list.add(new Location(pos1.getWorld(), x, y1, z1));
              list.add(new Location(pos1.getWorld(), x1, y, z1));
              list.add(new Location(pos1.getWorld(), x1, y1, z));
              list.add(new Location(pos1.getWorld(), x1, y, z));
              list.add(new Location(pos1.getWorld(), x, y1, z));
              list.add(new Location(pos1.getWorld(), x, y, z1));
              list.add(new Location(pos1.getWorld(), x, y, z));
            }
            else if ((maxX == minX) && (maxZ == minZ)) {
              list.add(new Location(pos1.getWorld(), x, y1, z1));
              list.add(new Location(pos1.getWorld(), x1, y, z1));
              list.add(new Location(pos1.getWorld(), x1, y1, z));
              list.add(new Location(pos1.getWorld(), x1, y, z));
              list.add(new Location(pos1.getWorld(), x, y1, z));
              list.add(new Location(pos1.getWorld(), x, y, z1));
              list.add(new Location(pos1.getWorld(), x, y, z));
            }
            else if ((maxX == minX)) {
              if (y == (int) maxY && z == (int) maxZ) {
                list.add(new Location(pos1.getWorld(), x, y1, z1));
                list.add(new Location(pos1.getWorld(), x1, y, z1));
                list.add(new Location(pos1.getWorld(), x1, y1, z));
                list.add(new Location(pos1.getWorld(), x, y, z1));
                list.add(new Location(pos1.getWorld(), x, y1, z));
              }
              else if (y == (int) maxY && z == (int) minZ) {
                list.add(new Location(pos1.getWorld(), x, y1, z1));
                list.add(new Location(pos1.getWorld(), x, y, z1));
                list.add(new Location(pos1.getWorld(), x1, y, z));
              }
              else if (y == (int) minY && z == (int) maxZ) {
                list.add(new Location(pos1.getWorld(), x1, y1, z));
                list.add(new Location(pos1.getWorld(), x, y, z));
                list.add(new Location(pos1.getWorld(), x, y, z1));
              }
              else if (y == (int) maxY) {
                list.add(new Location(pos1.getWorld(), x, y1, z1));
              }
              else if (z == (int) maxZ) {
                list.add(new Location(pos1.getWorld(), x, y1, z1));
              }
              else if (y == (int) minY) {
                list.add(new Location(pos1.getWorld(), x, y, z));
              }
              else if (z == (int) minZ) {
                list.add(new Location(pos1.getWorld(), x, y, z));
              }
            }
            else if ((maxY == minY)) {
              if (x == (int) maxX && z == (int) maxZ) {
                list.add(new Location(pos1.getWorld(), x, y1, z1));
                list.add(new Location(pos1.getWorld(), x1, y, z1));
                list.add(new Location(pos1.getWorld(), x1, y1, z));
                list.add(new Location(pos1.getWorld(), x, y, z1));
                list.add(new Location(pos1.getWorld(), x1, y, z));
              }
              else if (x == (int) maxX && z == (int) minZ) {
                list.add(new Location(pos1.getWorld(), x, y1, z1));
                list.add(new Location(pos1.getWorld(), x, y, z1));
                list.add(new Location(pos1.getWorld(), x1, y, z));
              }
              else if (x == (int) minX && z == (int) maxZ) {
                list.add(new Location(pos1.getWorld(), x1, y1, z));
                list.add(new Location(pos1.getWorld(), x1, y, z));
                list.add(new Location(pos1.getWorld(), x, y, z1));
              }
              else if (x == (int) maxX) {
                list.add(new Location(pos1.getWorld(), x1, y, z1));
              }
              else if (z == (int) maxZ) {
                list.add(new Location(pos1.getWorld(), x1, y, z1));
              }
              else if (x == (int) minX) {
                list.add(new Location(pos1.getWorld(), x, y, z));
              }
              else if (z == (int) minZ) {
                list.add(new Location(pos1.getWorld(), x, y, z));
              }
            }
            else if ((maxZ == minZ)) {
              if (x == (int) maxX && y == (int) maxY) {
                list.add(new Location(pos1.getWorld(), x, y1, z1));
                list.add(new Location(pos1.getWorld(), x1, y, z1));
                list.add(new Location(pos1.getWorld(), x1, y1, z));
                list.add(new Location(pos1.getWorld(), x, y1, z));
                list.add(new Location(pos1.getWorld(), x1, y, z));
              }
              else if (x == (int) maxX && y == (int) minY) {
                list.add(new Location(pos1.getWorld(), x, y, z));
                list.add(new Location(pos1.getWorld(), x, y, z1));
                list.add(new Location(pos1.getWorld(), x1, y, z));
              }
              else if (x == (int) minX && y == (int) maxY) {
                list.add(new Location(pos1.getWorld(), x1, y1, z));
                list.add(new Location(pos1.getWorld(), x, y, z1));
                list.add(new Location(pos1.getWorld(), x, y, z));
              }
              else if (x == (int) maxX) {
                list.add(new Location(pos1.getWorld(), x1, y1, z));
              }
              else if (y == (int) maxY) {
                list.add(new Location(pos1.getWorld(), x1, y1, z));
              }
              else if (x == (int) minX) {
                list.add(new Location(pos1.getWorld(), x, y, z));
              }
              else if (y == (int) minY) {
                list.add(new Location(pos1.getWorld(), x, y, z));
              }
            }
            else {
              if (x == (int) maxX && y == (int) maxY && z == (int) maxZ) {
                list.add(new Location(pos1.getWorld(), x, y1, z1));
                list.add(new Location(pos1.getWorld(), x1, y, z1));
                list.add(new Location(pos1.getWorld(), x1, y1, z));
              }
              else if (x == (int) maxX && y == (int) maxY && z == (int) minZ) {
                list.add(new Location(pos1.getWorld(), x, y1, z));
                list.add(new Location(pos1.getWorld(), x1, y, z));
              }
              else if (x == (int) maxX && y == (int) minY && z == (int) maxZ) {
                list.add(new Location(pos1.getWorld(), x1, y, z));
                list.add(new Location(pos1.getWorld(), x, y, z1));
              }
              else if (x == (int) minX && y == (int) maxY && z == (int) maxZ) {
                list.add(new Location(pos1.getWorld(), x, y, z1));
                list.add(new Location(pos1.getWorld(), x, y1, z));
              }
              else if (x == (int) maxX && y == (int) minY && z == (int) minZ) {
                list.add(new Location(pos1.getWorld(), x, y, z));
              }
              else if (x == (int) minX && y == (int) minY && z == (int) maxZ) {
                list.add(new Location(pos1.getWorld(), x, y, z));
              }
              else if (x == (int) minX && y == (int) maxY && z == (int) minZ) {
                list.add(new Location(pos1.getWorld(), x, y, z));
              }
            }

            list.add(new Location(pos1.getWorld(), x1, y1, z1));

            /*else if ((x == (int) maxX && y == (int) minZ)) {
              x1 += 1;
            }
            else if ((x == (int) maxY && x == (int) minX)) {
              x1 += 1;
            }
            else if ((y == (int) maxY && z == (int) minZ)) {
              y1 += 1;
            }
            else if ((y == (int) maxX && y == (int) minY)) {
              y1 += 1;
            }
            else if ((z == (int) maxZ && x == (int) minX)) {
              z1 += 1;
            }
            else if ((z == (int) maxZ && y == (int) minY)) {
              z1 += 1;
            }*/
            /*
            if (x1 == (int) minX) { x1 -= 0.5; }
            if (y1 == (int) minY) { y1 -= 0.5; }
            if (z1 == (int) minZ) { z1 -= 0.5; }*/
          }
        }
      }
    }

    return list;
  }

}
