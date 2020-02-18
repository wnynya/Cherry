package com.wnynya.cherry.event;

import com.wnynya.cherry.amethyst.NoteTool;
import com.wnynya.cherry.amethyst.Item2Block;
import com.wnynya.cherry.farm.SoilEvent;
import com.wnynya.cherry.wand.WandEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerInteractEvent(PlayerInteractEvent event) {

    if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
      WandEvent.rightClick(event);
    }

    else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      WandEvent.rightClick(event);
      //SoilEvent.plow(event);
    }

    else if (event.getAction().equals(Action.LEFT_CLICK_AIR)) {
      WandEvent.leftClick(event);
    }

    else if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
      WandEvent.leftClick(event);
    }

    NoteTool.clickNoteBlock(event);

    Item2Block.blockClick(event);

  }

}
