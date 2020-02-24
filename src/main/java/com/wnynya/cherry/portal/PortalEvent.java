package com.wnynya.cherry.portal;

import com.wnynya.cherry.player.PlayerMeta;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.ArrayList;
import java.util.List;

public class PortalEvent {

  private static List<Player> onJoinPortal = new ArrayList<>();

  public static void playerMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();

    PlayerMeta pm = PlayerMeta.getPlayerMeta(player);

    if (pm == null) {
      return;
    }

    PlayerMeta.FunctionData fd = pm.getFunction(PlayerMeta.Function.PORTAL);

    if (fd == null || !fd.isEnable()) {
      return;
    }

    Long time = System.currentTimeMillis();
    Long lastTime = Portal.getLastUsedTime(player);
    if (lastTime == null) {
      lastTime = 0L;
    }
    if (time - lastTime <= 2000) {
      return;
    }

    Location loc = player.getLocation().getBlock().getLocation();
    Material material = loc.getBlock().getType();

    Portal portal = Portal.getPortal(loc);
    if (portal == null) {
      if (onJoinPortal.contains(player)) {
        onJoinPortal.remove(player);
      }
      return;
    }

    if (onJoinPortal.contains(player)) {
      return;
    }

    portal.use(player);

    if (material.equals(Material.AIR)
      || material.equals(Material.NETHER_PORTAL)
      || material.equals(Material.END_GATEWAY)
      || material.equals(Material.END_PORTAL)
      || material.equals(Material.WATER)
    ) {
    }

  }

  public static void playerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    if (!PlayerMeta.getPlayerMeta(player).getFunction(PlayerMeta.Function.PORTAL).isEnable()) {
      return;
    }

    Location loc = player.getLocation().getBlock().getLocation();

    Portal portal = Portal.getPortal(loc);
    if (portal == null) {
      return;
    }

    if (!onJoinPortal.contains(player)) {
      onJoinPortal.add(player);
    }

  }

  public static void playerPortal(PlayerPortalEvent event) {
    Player player = event.getPlayer();


  }

}
