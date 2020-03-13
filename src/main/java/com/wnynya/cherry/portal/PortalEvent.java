package com.wnynya.cherry.portal;

import com.wnynya.cherry.Msg;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.HashMap;

public class PortalEvent {

  private static HashMap<Player, Portal> portalInner = new HashMap<>();

  public static void playerMove(PlayerMoveEvent event) {

    Player player = event.getPlayer();

    Location loc = player.getLocation().getBlock().getLocation();

    Object[] pd = Portal.getPortal(loc);

    if (pd == null) {
      portalInner.remove(player);
      return;
    }

    Portal portal = (Portal) pd[0];

    PortalArea pa = (PortalArea) pd[1];

    if (pa.getType().equals(PortalArea.Type.GATE)) {
      if (portalInner.containsKey(player) && portalInner.get(player).getName().equals(portal.getName())) {
        return;
      }

      portal.use(player);

      if (portalInner.containsKey(player)) {
        portalInner.replace(player, portal);
      }
      else {
        portalInner.put(player, portal);
      }
    }

  }

  public static void playerInteract(PlayerInteractEvent event) {

    if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      Player player = event.getPlayer();

      Block block = event.getClickedBlock();
      if (block == null) {
        return;
      }

      Location loc = block.getLocation();

      Object[] pd = Portal.getPortal(loc);

      if (pd == null) {
        return;
      }

      Portal portal = (Portal) pd[0];

      PortalArea pa = (PortalArea) pd[1];

      if (portal.isEnable()) {
        event.setCancelled(true);
      }
      else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
        return;
      }

      if (pa.getType().equals(PortalArea.Type.SIGN)) {
        portal.use(player);
      }
    }

  }

  public static void playerJoin(PlayerJoinEvent event) {

    Player player = event.getPlayer();

    Location loc = player.getLocation().getBlock().getLocation();

    Object[] pd = Portal.getPortal(loc);

    if (pd == null) {
      return;
    }

    Portal portal = (Portal) pd[0];

    if (portal == null) {
      return;
    }

    if (!portalInner.containsKey(player)) {
      portalInner.put(player, portal);
    }

  }

  public static void playerPortal(PlayerPortalEvent event) {

    Player player = event.getPlayer();

    Location loc = player.getLocation().getBlock().getLocation();

    Object[] pd = Portal.getPortal(loc);

    if (pd == null) {
      return;
    }

    Portal portal = (Portal) pd[0];

    if (portal != null) {
      event.setCancelled(true);
    }

  }

}
