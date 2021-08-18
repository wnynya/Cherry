package io.wany.cherry.wand.brush;

import org.bukkit.Location;

import java.util.List;

public class BrushArea {

  private final String applyType;
  private final List<Location> area;

  public BrushArea(List<Location> area, String applyType) {
    this.area = area;
    this.applyType = applyType;
  }

  public List<Location> getArea() {
    return area;
  }

  public String getApplyType() {
    return applyType;
  }

  public static class ApplyType {
    public static final String BUILD = "build";
    public static final String DESTROY = "destroy";
  }


}
