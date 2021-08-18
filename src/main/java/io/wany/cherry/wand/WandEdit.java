package io.wany.cherry.wand;

import io.wany.cherry.Message;
import io.wany.cherry.wand.area.Area;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

public class WandEdit {

  private final Wand wand;
  private final HashMap<Integer, Location> position = new HashMap<>();

  public WandEdit(Wand wand) {
    this.wand = wand;
  }

  public boolean setPosition(int n, Location pos) {
    if (this.getPosition(n) == null || !this.getPosition(n).equals(pos)) {
      this.position.put(n, pos);
      return true;
    }
    return false;
  }

  public Location getPosition(int n) {
    return this.position.get(n);
  }

  public static void onPlayerInteract(PlayerInteractEvent event) {

    if (!Wand.ENABLED) {
      return;
    }

    Player player = event.getPlayer();

    if (!player.hasPermission("cherry.wand.edit.wand")) {
      return;
    }
    if (event.getItem() == null || !event.getItem().getType().equals(Material.SWEET_BERRIES)) {
      return;
    }

    event.setCancelled(true);
    Wand wand = Wand.getWand(player);
    WandEdit wandEdit = wand.getEdit();

    if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
      Block block = player.getTargetBlockExact(10);
      if (block == null) {
        return;
      }
      Location location = block.getLocation();
      wandEdit.setPosition(1, location);
      String message = "첫번째 포지션이 설정되었습니다. (" + Wand.COLOR + location.getX() + "&r, " + Wand.COLOR + location.getY() + "&r, " + Wand.COLOR + location.getZ() + "&r)";
      if (wandEdit.getPosition(1) != null && wandEdit.getPosition(2) != null) {
        int count = Area.CUBE.getArea(wandEdit.getPosition(1), wandEdit.getPosition(2)).size();
        message += " (" + Wand.COLOR + count + "&r블록)";
        wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wandEdit.getPosition(1), wandEdit.getPosition(2)));
      }
      else {
        wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wandEdit.getPosition(1), wandEdit.getPosition(1)));
      }

      Message.info(player, Wand.PREFIX + Message.effect(message));
      wand.showParticleArea();
    }

    else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      Block block = player.getTargetBlockExact(10);
      if (block == null) {
        return;
      }
      Location location = block.getLocation();
      wandEdit.setPosition(2, location);
      String message = "두번째 포지션이 설정되었습니다. (" + Wand.COLOR + location.getX() + "&r, " + Wand.COLOR + location.getY() + "&r, " + Wand.COLOR + location.getZ() + "&r)";
      if (wandEdit.getPosition(1) != null && wandEdit.getPosition(2) != null) {
        int count = Area.CUBE.getArea(wandEdit.getPosition(1), wandEdit.getPosition(2)).size();
        message += " (" + Wand.COLOR + count + "&r블록)";
        wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wandEdit.getPosition(1), wandEdit.getPosition(2)));
      }
      else {
        wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wandEdit.getPosition(2), wandEdit.getPosition(2)));
      }

      Message.info(player, Wand.PREFIX + Message.effect(message));
      wand.showParticleArea();
    }

    else if (event.getAction().equals(Action.LEFT_CLICK_AIR)) {
      wandEdit.setPosition(1, null);
      if (wandEdit.getPosition(1) != null || wandEdit.getPosition(2) != null) {
        if (wandEdit.getPosition(1) != null && wandEdit.getPosition(2) != null) {
          wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wandEdit.getPosition(1), wandEdit.getPosition(2)));
        }
        else {
          wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wandEdit.getPosition(2), wandEdit.getPosition(2)));
        }
        wand.showParticleArea();
      }
      else {
        wand.hideParticleArea();
      }
    }

    else if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
      wandEdit.setPosition(2, null);
      if (wandEdit.getPosition(1) != null || wandEdit.getPosition(2) != null) {
        if (wandEdit.getPosition(1) != null && wandEdit.getPosition(2) != null) {
          wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wandEdit.getPosition(1), wandEdit.getPosition(2)));
        }
        else {
          wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wandEdit.getPosition(1), wandEdit.getPosition(1)));
        }
        wand.showParticleArea();
      }
      else {
        wand.hideParticleArea();
      }
    }

  }

}
