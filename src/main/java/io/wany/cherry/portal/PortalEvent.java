package io.wany.cherry.portal;

import org.bukkit.event.Listener;

public class PortalEvent implements Listener {

  /*private static HashMap<Player, Portal> portalInner = new HashMap<>();

  @EventHandler
  public static void playerMove(PlayerMoveEvent event) {

    if (!Portal.ENABLED) {
      return;
    }

    Player player = event.getPlayer();

    if (event.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
      return;
    }

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

  @EventHandler
  public static void playerInteract(PlayerInteractEvent event) {

    if (!Portal.ENABLED) {
      return;
    }

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

  @EventHandler
  public static void playerJoin(PlayerJoinEvent event) {

    if (!Portal.ENABLED) {
      return;
    }

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

  @EventHandler
  public static void playerPortal(PlayerPortalEvent event) {

    if (!Portal.ENABLED) { return; }

    Object[] pd = Portal.getPortal(event.getPlayer().getLocation().getBlock().getLocation());
    if (pd == null) { return; }
    Portal portal = (Portal) pd[0];
    if (portal == null) { return; }

    event.setCancelled(true);

  }

  @EventHandler
  public static void entityPortal(EntityPortalEvent event) {

    if (!Portal.ENABLED) { return; }

    Object[] pd = Portal.getPortal(event.getEntity().getLocation().getBlock().getLocation());
    if (pd == null) { return; }
    Portal portal = (Portal) pd[0];
    if (portal == null) { return; }

    event.setCancelled(true);
  }*/
}
