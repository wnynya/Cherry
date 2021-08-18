package io.wany.cherry.wand.area;

import org.bukkit.Location;

import java.util.List;

public enum Area implements AreaGenerator {

  CUBE {
    @Override
    public List<Location> getArea(Location pos1, Location pos2) {
      return Cube.getAreaFilled(pos1, pos2);
    }

    public List<Location> getArea(Location pos, int r) {
      Location pos1 = new Location(pos.getWorld(), pos.getX() + r, pos.getY() + r, pos.getZ() + r);
      Location pos2 = new Location(pos.getWorld(), pos.getX() - r, pos.getY() - r, pos.getZ() - r);
      return Cube.getAreaFilled(pos1, pos2);
    }

    public List<Location> getArea(Location pos, int r, int h) {
      Location pos1 = new Location(pos.getWorld(), pos.getX() + r, pos.getY() + h, pos.getZ() + r);
      Location pos2 = new Location(pos.getWorld(), pos.getX() - r, pos.getY(), pos.getZ() - r);
      return Cube.getAreaFilled(pos1, pos2);
    }
  },

  CUBE_EMPTY {
    public List<Location> getArea(Location pos1, Location pos2) {
      return Cube.getAreaEmpty(pos1, pos2);
    }

    public List<Location> getArea(Location pos, int r) {
      Location pos1 = new Location(pos.getWorld(), pos.getX() + r, pos.getY() + r, pos.getZ() + r);
      Location pos2 = new Location(pos.getWorld(), pos.getX() - r, pos.getY() - r, pos.getZ() - r);
      return Cube.getAreaEmpty(pos1, pos2);
    }

    public List<Location> getArea(Location pos, int r, int h) {
      Location pos1 = new Location(pos.getWorld(), pos.getX() + r, pos.getY() + h, pos.getZ() + r);
      Location pos2 = new Location(pos.getWorld(), pos.getX() - r, pos.getY(), pos.getZ() - r);
      return Cube.getAreaEmpty(pos1, pos2);
    }
  },

  CUBE_WALL {
    public List<Location> getArea(Location pos1, Location pos2) {
      return Cube.getAreaWall(pos1, pos2);
    }

    public List<Location> getArea(Location pos, int r) {
      Location pos1 = new Location(pos.getWorld(), pos.getX() + r, pos.getY() + r, pos.getZ() + r);
      Location pos2 = new Location(pos.getWorld(), pos.getX() - r, pos.getY() - r, pos.getZ() - r);
      return Cube.getAreaWall(pos1, pos2);
    }

    public List<Location> getArea(Location pos, int r, int h) {
      Location pos1 = new Location(pos.getWorld(), pos.getX() + r, pos.getY() + h, pos.getZ() + r);
      Location pos2 = new Location(pos.getWorld(), pos.getX() - r, pos.getY(), pos.getZ() - r);
      return Cube.getAreaWall(pos1, pos2);
    }
  },

  CUBE_PARTICLE {
    public List<Location> getArea(Location pos1, Location pos2) {
      return Cube.getAreaParticle(pos1, pos2);
    }

    public List<Location> getArea(Location pos, int r) {
      return null;
    }

    public List<Location> getArea(Location pos, int r, int h) {
      return null;
    }
  },

  CYLINDER {
    public List<Location> getArea(Location pos1, Location pos2) {
      double minX = Math.min(pos1.getX(), pos2.getX());
      double maxX = Math.max(pos1.getX(), pos2.getX());
      double minY = Math.min(pos1.getY(), pos2.getY());
      double maxY = Math.max(pos1.getY(), pos2.getY());
      double minZ = Math.min(pos1.getZ(), pos2.getZ());
      double maxZ = Math.max(pos1.getZ(), pos2.getZ());
      Location pos = new Location(pos1.getWorld(), (int) (maxX / 2), (int) (maxY / 2), (int) (maxZ / 2));
      int r = (int) Math.min((maxX - minX), (maxZ - minZ)) / 2;
      return Cylinder.getAreaFilled(pos, r, (int) (maxY - minY));
    }

    public List<Location> getArea(Location pos, int r) {
      return Cylinder.getAreaFilled(pos, r, 1);
    }

    public List<Location> getArea(Location pos, int r, int h) {
      return Cylinder.getAreaFilled(pos, r, h);
    }
  },

  CYLINDER_POINTED {
    public List<Location> getArea(Location pos1, Location pos2) {
      double minX = Math.min(pos1.getX(), pos2.getX());
      double maxX = Math.max(pos1.getX(), pos2.getX());
      double minY = Math.min(pos1.getY(), pos2.getY());
      double maxY = Math.max(pos1.getY(), pos2.getY());
      double minZ = Math.min(pos1.getZ(), pos2.getZ());
      double maxZ = Math.max(pos1.getZ(), pos2.getZ());
      Location pos = new Location(pos1.getWorld(), (int) (maxX / 2), (int) (maxY / 2), (int) (maxZ / 2));
      int r = (int) Math.min((maxX - minX), (maxZ - minZ)) / 2;
      return Cylinder.getAreaPointFilled(pos, r, (int) (maxY - minY));
    }

    public List<Location> getArea(Location pos, int r) {
      return Cylinder.getAreaPointFilled(pos, r, 1);
    }

    public List<Location> getArea(Location pos, int r, int h) {
      return Cylinder.getAreaPointFilled(pos, r, h);
    }
  },

  CYLINDER_EMPTY {
    public List<Location> getArea(Location pos1, Location pos2) {
      double minX = Math.min(pos1.getX(), pos2.getX());
      double maxX = Math.max(pos1.getX(), pos2.getX());
      double minY = Math.min(pos1.getY(), pos2.getY());
      double maxY = Math.max(pos1.getY(), pos2.getY());
      double minZ = Math.min(pos1.getZ(), pos2.getZ());
      double maxZ = Math.max(pos1.getZ(), pos2.getZ());
      Location pos = new Location(pos1.getWorld(), (int) (maxX / 2), (int) (maxY / 2), (int) (maxZ / 2));
      int r = (int) Math.min((maxX - minX), (maxZ - minZ)) / 2;
      return Cylinder.getAreaEmpty(pos, r, (int) (maxY - minY));
    }

    public List<Location> getArea(Location pos, int r) {
      return Cylinder.getAreaEmpty(pos, r, 1);
    }

    public List<Location> getArea(Location pos, int r, int h) {
      return Cylinder.getAreaEmpty(pos, r, h);
    }
  },

  CYLINDER_POINTED_EMPTY {
    public List<Location> getArea(Location pos1, Location pos2) {
      double minX = Math.min(pos1.getX(), pos2.getX());
      double maxX = Math.max(pos1.getX(), pos2.getX());
      double minY = Math.min(pos1.getY(), pos2.getY());
      double maxY = Math.max(pos1.getY(), pos2.getY());
      double minZ = Math.min(pos1.getZ(), pos2.getZ());
      double maxZ = Math.max(pos1.getZ(), pos2.getZ());
      Location pos = new Location(pos1.getWorld(), (int) (maxX / 2), (int) (maxY / 2), (int) (maxZ / 2));
      int r = (int) Math.min((maxX - minX), (maxZ - minZ)) / 2;
      return Cylinder.getAreaPointEmpty(pos, r, (int) (maxY - minY));
    }

    public List<Location> getArea(Location pos, int r) {
      return Cylinder.getAreaPointEmpty(pos, r, 1);
    }

    public List<Location> getArea(Location pos, int r, int h) {
      return Cylinder.getAreaPointEmpty(pos, r, h);
    }
  },

  SPHERE {
    @Override
    public List<Location> getArea(Location pos1, Location pos2) {
      double minX = Math.min(pos1.getX(), pos2.getX());
      double maxX = Math.max(pos1.getX(), pos2.getX());
      double minY = Math.min(pos1.getY(), pos2.getY());
      double maxY = Math.max(pos1.getY(), pos2.getY());
      double minZ = Math.min(pos1.getZ(), pos2.getZ());
      double maxZ = Math.max(pos1.getZ(), pos2.getZ());
      Location pos = new Location(pos1.getWorld(), (int) (maxX / 2), (int) (maxY / 2), (int) (maxZ / 2));
      int r = (int) Math.min((maxX - minX), Math.min((maxY - minY), (maxZ - minZ))) / 2;
      return Sphere.getAreaFilled(pos, r);
    }

    @Override
    public List<Location> getArea(Location pos, int r) {
      return Sphere.getAreaFilled(pos, r);
    }

    @Override
    public List<Location> getArea(Location pos, int r, int h) {
      return Sphere.getAreaFilled(pos, r);
    }
  },

  SPHERE_POINTED {
    @Override
    public List<Location> getArea(Location pos1, Location pos2) {
      double minX = Math.min(pos1.getX(), pos2.getX());
      double maxX = Math.max(pos1.getX(), pos2.getX());
      double minY = Math.min(pos1.getY(), pos2.getY());
      double maxY = Math.max(pos1.getY(), pos2.getY());
      double minZ = Math.min(pos1.getZ(), pos2.getZ());
      double maxZ = Math.max(pos1.getZ(), pos2.getZ());
      Location pos = new Location(pos1.getWorld(), (int) (maxX / 2), (int) (maxY / 2), (int) (maxZ / 2));
      int r = (int) Math.min((maxX - minX), Math.min((maxY - minY), (maxZ - minZ))) / 2;
      return Sphere.getAreaPoint(pos, r);
    }

    @Override
    public List<Location> getArea(Location pos, int r) {
      return Sphere.getAreaPoint(pos, r);
    }

    @Override
    public List<Location> getArea(Location pos, int r, int h) {
      return Sphere.getAreaPoint(pos, r);
    }
  },

  SPHERE_EMPTY {
    @Override
    public List<Location> getArea(Location pos1, Location pos2) {
      double minX = Math.min(pos1.getX(), pos2.getX());
      double maxX = Math.max(pos1.getX(), pos2.getX());
      double minY = Math.min(pos1.getY(), pos2.getY());
      double maxY = Math.max(pos1.getY(), pos2.getY());
      double minZ = Math.min(pos1.getZ(), pos2.getZ());
      double maxZ = Math.max(pos1.getZ(), pos2.getZ());
      Location pos = new Location(pos1.getWorld(), (int) (maxX / 2), (int) (maxY / 2), (int) (maxZ / 2));
      int r = (int) Math.min((maxX - minX), Math.min((maxY - minY), (maxZ - minZ))) / 2;
      return Sphere.getAreaEmpty(pos, r);
    }

    @Override
    public List<Location> getArea(Location pos, int r) {
      return Sphere.getAreaEmpty(pos, r);
    }

    @Override
    public List<Location> getArea(Location pos, int r, int h) {
      return Sphere.getAreaEmpty(pos, r);
    }
  },

  SPHERE_POINTED_EMPTY {
    @Override
    public List<Location> getArea(Location pos1, Location pos2) {
      double minX = Math.min(pos1.getX(), pos2.getX());
      double maxX = Math.max(pos1.getX(), pos2.getX());
      double minY = Math.min(pos1.getY(), pos2.getY());
      double maxY = Math.max(pos1.getY(), pos2.getY());
      double minZ = Math.min(pos1.getZ(), pos2.getZ());
      double maxZ = Math.max(pos1.getZ(), pos2.getZ());
      Location pos = new Location(pos1.getWorld(), (int) (maxX / 2), (int) (maxY / 2), (int) (maxZ / 2));
      int r = (int) Math.min((maxX - minX), Math.min((maxY - minY), (maxZ - minZ))) / 2;
      return Sphere.getAreaPointEmpty(pos, r);
    }

    @Override
    public List<Location> getArea(Location pos, int r) {
      return Sphere.getAreaPointEmpty(pos, r);
    }

    @Override
    public List<Location> getArea(Location pos, int r, int h) {
      return Sphere.getAreaPointEmpty(pos, r);
    }
  },

  WALL {
    @Override
    public List<Location> getArea(Location pos1, Location pos2) {
      return Wall.getArea(pos1, pos2);
    }

    @Override
    public List<Location> getArea(Location pos, int r) {
      return null;
    }

    @Override
    public List<Location> getArea(Location pos, int r, int h) {
      return null;
    }
  }

}
