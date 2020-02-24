package com.wnynya.cherry.wand;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.player.PlayerMeta;
import com.wnynya.cherry.wand.area.Area;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class WandEvent {

  public static void playerInteract(PlayerInteractEvent event) {

    Player player = event.getPlayer();

    if (event.getItem() == null) {
      return;
    }

    if (event.getItem().equals(Wand.getWandItem(Wand.ItemType.EDIT_POSITIONER))) {
      event.setCancelled(true);
    }

    if (!Cherry.config.getBoolean("function.wand")) {
      return;
    }

    if (!player.hasPermission("cherry.wand.wand")) {
      return;
    }

    if (event.getItem().getType().equals(Wand.getWandItem(Wand.ItemType.EDIT_POSITIONER).getType())) {

      Wand wand = Wand.getWand(player);

      if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
        Block block = player.getTargetBlockExact(10);
        if (block == null) {
          return;
        }
        boolean bool = wand.getEdit().setPosition(1, block.getLocation());

        Location loc = block.getLocation();
        PlayerMeta pm = PlayerMeta.getPlayerMeta(player);
        if (pm == null) {
          return;
        }
        PlayerMeta.FunctionData fd = pm.getFunction(PlayerMeta.Function.WAND);
        if (fd == null) {
          return;
        }
        if (fd.getData().get("posMsg").equals("actionbar")) {
          if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
            Msg.actionBar(player, Msg.n2s("첫번째 포지션: (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r) (" + "&6" + Area.CUBE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)).size() + "&r블록)"));
          }
          else {
            Msg.actionBar(player, Msg.n2s("첫번째 포지션: (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r)"));
          }
        }
        if (fd.getData().get("posMsg").equals("normal") && bool) {
          if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
            Msg.info(player, Msg.Prefix.WAND + Msg.n2s("첫번째 포지션이 설정되었습니다. (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r) (" + "&6" + Area.CUBE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)).size() + "&r블록)"));
          }
          else {
            Msg.info(player, Msg.Prefix.WAND + Msg.n2s("첫번째 포지션이 설정되었습니다. (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r)"));
          }
        }

        if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
          wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)));
        }
        else {
          wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(1)));
        }

        wand.enableParticleArea();
        event.setCancelled(true);
      }

      else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
        Block block = player.getTargetBlockExact(10);
        if (block == null) {
          return;
        }
        boolean bool = wand.getEdit().setPosition(2, block.getLocation());
        Location loc = block.getLocation();
        PlayerMeta pm = PlayerMeta.getPlayerMeta(player);
        if (pm == null) {
          return;
        }
        PlayerMeta.FunctionData fd = pm.getFunction(PlayerMeta.Function.WAND);
        if (fd == null) {
          return;
        }
        if (fd.getData().get("posMsg").equals("actionbar")) {
          if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
            Msg.actionBar(player, Msg.n2s("두번째 포지션: (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r) (" + "&6" + Area.CUBE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)).size() + "&r블록)"));
          }
          else {
            Msg.actionBar(player, Msg.n2s("두번째 포지션: (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r)"));
          }
        }
        else if (fd.getData().get("posMsg").equals("normal") && bool) {
          if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
            Msg.info(player, Msg.Prefix.WAND + Msg.n2s("두번째 포지션이 설정되었습니다. (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r) (" + "&6" + Area.CUBE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)).size() + "&r블록)"));
          }
          else {
            Msg.info(player, Msg.Prefix.WAND + Msg.n2s("두번째 포지션이 설정되었습니다. (&6" + loc.getX() + "&r, &6" + loc.getY() + "&r, &6" + loc.getZ() + "&r)"));
          }
        }

        if (wand.getEdit().getPosition(1) != null && wand.getEdit().getPosition(2) != null) {
          wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(1), wand.getEdit().getPosition(2)));
        }
        else {
          wand.setParticleArea(Area.CUBE_PARTICLE.getArea(wand.getEdit().getPosition(2), wand.getEdit().getPosition(2)));
        }

        wand.enableParticleArea();
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
          wand.enableParticleArea();
        }
        else {
          wand.disableParticleArea();
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
          wand.enableParticleArea();
        }
        else {
          wand.disableParticleArea();
        }
      }

    }

  }

}
