package io.wany.cherry.wand;

import io.wany.cherry.wand.brush.Brush;
import io.wany.cherry.wand.brush.BrushSetting;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class WandBrush {

  private final Wand wand;
  public Brush brush;
  public BrushSetting setting;
  public int delay = 100;
  public long lastUsed = 0;
  public int reach = 10;

  WandBrush(Wand wand) {
    this.setting = new BrushSetting(wand);
    this.brush = Brush.CUBE;
    this.wand = wand;
  }

  public void touch(Location loc, boolean applyPhysics) {
    lastUsed = System.currentTimeMillis();
    this.brush.touch(loc, this.wand, applyPhysics);
  }

  public static void onPlayerInteract(PlayerInteractEvent event) {

    if (!Wand.ENABLED) {
      return;
    }

    Player player = event.getPlayer();

    if (!player.hasPermission("cherry.wand.edit.wand")) {
      return;
    }
    if (event.getItem() == null || !event.getItem().getType().equals(Material.SPECTRAL_ARROW)) {
      return;
    }

    event.setCancelled(true);

    Wand wand = Wand.getWand(player);
    WandBrush wandBrush = wand.getBrush();

    if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      Block block = player.getTargetBlock(10);
      if (block == null) {
        return;
      }
      if (block.getType().equals(Material.AIR)) {
        return;
      }
      if (System.currentTimeMillis() - wandBrush.lastUsed < wandBrush.delay) {
        return;
      }

      wandBrush.touch(block.getLocation(), false);
    }

  }

}
