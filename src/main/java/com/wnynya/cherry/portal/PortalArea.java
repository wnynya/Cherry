package com.wnynya.cherry.portal;

import org.bukkit.Location;

import java.util.List;

public class PortalArea {

  private String name;
  private List<Location> area;
  private PortalArea.Type type;
  private String axis;

  public PortalArea(String name, List<Location> area, PortalArea.Type type, String axis) {
    this.name = name;
    this.area = area;
    this.type = type;
    this.axis = axis;
  }

  public List<Location> getArea() {
    return this.area;
  }

  public PortalArea.Type getType() {
    return this.type;
  }

  public String getName() {
    return name;
  }

  public String getAxis() {
    return axis;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public enum Type{
    GATE_AIR,
    GATE_NETHER,
    GATE_ENDER,
    GATE_ENDER_LEGACY,
    GATE_WATER,
    SIGN,
    TEMP
  }

}
