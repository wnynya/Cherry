package com.wnynya.cherry.wand;

import com.wnynya.cherry.Msg;
import com.wnynya.cherry.player.PlayerMeta;
import com.wnynya.cherry.wand.area.Area;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandEvent implements Listener {

  @EventHandler
  public static void playerInteract(PlayerInteractEvent event) {

    if (!Wand.enabled) {
      return;
    }

    Player player = event.getPlayer();

    if (event.getItem() == null) {
      return;
    }

    if (event.getItem().equals(Wand.getWandItem(Wand.ItemType.EDIT_POSITIONER))) {
      event.setCancelled(true);
    }

    if (!player.hasPermission("cherry.wand.wand")) {
      return;
    }

    PlayerMeta pm = PlayerMeta.getPlayerMeta(player);
    if (!pm.is(PlayerMeta.Path.WAND_ENABLE)) {
      return;
    }

    if (event.getItem().getType().equals(Wand.getWandItem(Wand.ItemType.EDIT_POSITIONER).getType())) {

      Wand wand = Wand.getWand(player);

      if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
        Block block = player.getTargetBlockExact(10);
        if (block == null) {
          return;
        }

        Location loc = block.getLocation();

        wand.getEdit().setPosition(1, loc);

        if (pm.get(PlayerMeta.Path.WAND_MSG).equals("actionbar")) {
          if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
            Msg.actionBar(player, Msg.effect("첫번째 포지션: (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r) (" + "&6" + Area.CUBE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)).size() + "&r블록)"));
          }
          else {
            Msg.actionBar(player, Msg.effect("첫번째 포지션: (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r)"));
          }
        }
        if (pm.get(PlayerMeta.Path.WAND_MSG).equals("normal")) {
          if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
            Msg.info(player, Msg.Prefix.WAND + Msg.effect("첫번째 포지션이 설정되었습니다. (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r) (" + "&6" + Area.CUBE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)).size() + "&r블록)"));
          }
          else {
            Msg.info(player, Msg.Prefix.WAND + Msg.effect("첫번째 포지션이 설정되었습니다. (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r)"));
          }
        }

        if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
          wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)));
        }
        else {
          wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(1)));
        }

        wand.showParticleArea();
        event.setCancelled(true);
      }

      else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
        Block block = player.getTargetBlockExact(10);
        if (block == null) {
          return;
        }

        Location loc = block.getLocation();

        wand.getEdit().setPosition(2, loc);

        if (pm.get(PlayerMeta.Path.WAND_MSG).equals("actionbar")) {
          if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
            Msg.actionBar(player, Msg.effect("두번째 포지션: (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r) (" + "&6" + Area.CUBE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)).size() + "&r블록)"));
          }
          else {
            Msg.actionBar(player, Msg.effect("두번째 포지션: (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r)"));
          }
        }
        else if (pm.get(PlayerMeta.Path.WAND_MSG).equals("normal")) {
          if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
            Msg.info(player, Msg.Prefix.WAND + Msg.effect("두번째 포지션이 설정되었습니다. (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r) (" + "&6" + Area.CUBE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)).size() + "&r블록)"));
          }
          else {
            Msg.info(player, Msg.Prefix.WAND + Msg.effect("두번째 포지션이 설정되었습니다. (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r)"));
          }
        }

        if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
          wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)));
        }
        else {
          wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(2), wand.getEdit().getPosition(2)));
        }

        wand.showParticleArea();
        event.setCancelled(true);
      }

      else if (event.getAction().equals(Action.LEFT_CLICK_AIR)) {
        wand.getEdit().setPosition(1, null);
        if (wand.getEdit().getPosition(1) != null || wand.getEdit().getPosition(2) != null) {
          if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
            wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)));
          }
          else {
            wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(2), wand.getEdit().getPosition(2)));
          }
          wand.showParticleArea();
        }
        else {
          wand.hideParticleArea();
        }
      }

      else if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
        wand.getEdit().setPosition(2, null);
        if (wand.getEdit().getPosition(1) != null || wand.getEdit().getPosition(2) != null) {
          if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
            wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)));
          }
          else {
            wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(1)));
          }
          wand.showParticleArea();
        }
        else {
          wand.hideParticleArea();
        }
      }

    }

  }

}
