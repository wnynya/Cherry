package com.wnynya.cherry.amethyst;

import com.wnynya.cherry.portal.Portal;
import com.wnynya.cherry.portal.PortalArea;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignCommand {

  public static void playerInteract(PlayerInteractEvent event) {

    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      Player player = event.getPlayer();

      Block block = event.getClickedBlock();
      if (block == null) {
        return;
      }

      if (!(block.getState() instanceof Sign)) {
        return;
      }

      Sign s = (Sign) block.getState();

    }

  }

}
