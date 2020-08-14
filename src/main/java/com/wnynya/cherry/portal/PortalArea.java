package com.wnynya.cherry.portal;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.network.bungeecord.ServerData;
import com.wnynya.cherry.wand.Wand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PortalArea {

  private Portal portal;
  private String name;
  private List<Location> area;
  private PortalArea.Type type;
  private BlockData fill;


  private String[] lines;



  public PortalArea(Portal portal, String name, List<Location> area, PortalArea.Type type, BlockData fill) {
    this.portal = portal;
    this.name = name;
    this.area = area;
    this.type = type;
    this.fill = fill;
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

  public BlockData getFill() { return fill; }

  public String[] getLines() { return lines; }

  public void setLines(String[] lines) {

    this.lines = lines;

    if (!this.type.equals(Type.SIGN)) {
      return;
    }

    for (Location loc : this.area) {
      Block block = loc.getBlock();
      if (block == null) {
        return;
      }
      BlockState bs = block.getState();
      if (!(bs instanceof Sign)) {
        return;
      }
      Sign sign = (Sign) bs;
      for (int n = 0; n < lines.length; n++) {
        String line = lines[n];
        Matcher m1 = Pattern.compile("\\{enabletoggle:(.*):(.*)}").matcher(line);
        while (m1.find()) {
          if (portal.isEnable()) {
            line = line.replaceAll("\\{enabletoggle:(.*):(.*)}", Msg.effect(m1.group(1)));
          }
          else {
            line = line.replaceAll("\\{enabletoggle:(.*):(.*)}", Msg.effect(m1.group(2)));
          }
        }
        line = line.replace("{name}", portal.getName());
        line = line.replace("{displayname}", portal.getDisplayName());
        if (portal.getProtocol().equals(Portal.Protocol.BUNGEECORD)) {
          if (portal.getGotoServer() != null) {
            ServerData s = ServerData.getServerData(portal.getGotoServer());
            Matcher m2 = Pattern.compile("\\{servertoggle:(.*):(.*)}").matcher(line);
            while (m2.find()) {
              if (s.isOnline()) {
                line = line.replaceAll("\\{servertoggle:(.*):(.*)}", Msg.effect(m2.group(1)));
              }
              else {
                line = line.replaceAll("\\{servertoggle:(.*):(.*)}", Msg.effect(m2.group(2)));
              }
            }
            line = line.replace("{server}", portal.getGotoServer());
            line = line.replace("{servername}", portal.getGotoServer());
            line = line.replace("{cp}", s.getCurrentPlayers() + "");
            line = line.replace("{mp}", s.getMaxPlayers() + "");
            line = line.replace("{current}", s.getCurrentPlayers() + "");
            line = line.replace("{max}", s.getMaxPlayers() + "");
          }
          else {
            Matcher m2 = Pattern.compile("\\{servertoggle:(.*):(.*)}").matcher(line);
            while (m2.find()) {
              line = line.replaceAll("\\{servertoggle:(.*):(.*)}", Msg.effect(m2.group(2)));
            }
            line = line.replace("{server}", "&4null");
            line = line.replace("{servername}", "&4null");
            line = line.replace("{cp}", "0");
            line = line.replace("{mp}",  "0");
            line = line.replace("{current}", "0");
            line = line.replace("{max}", "0");
          }
        }
        line = Msg.effect(line);
        sign.setLine(n, line);
      }
      bs.update(true, false);
    }

  }



  public void fill() {

    Wand wand = Wand.getWand(Cherry.uuid);

    if (this.type.equals( PortalArea.Type.GATE )) {
      wand.replace(Bukkit.createBlockData(Material.AIR, "[]"), this.fill, this.area, false);
      wand.replace(Bukkit.createBlockData(Material.WATER, "[]"), this.fill, this.area, false);
      wand.replace(Bukkit.createBlockData(Material.END_GATEWAY, "[]"), this.fill, this.area, false);
      wand.replace(Bukkit.createBlockData(Material.END_PORTAL, "[]"), this.fill, this.area, false);
      wand.replace(Bukkit.createBlockData(Material.NETHER_PORTAL, "[]"), this.fill, this.area, false);
    }

    else if (this.type.equals( PortalArea.Type.SIGN )) {
      wand.fill(this.fill, this.area, false);
    }

  }





  public enum Type {
    GATE,
    SIGN,
    TEMP
  }

}
