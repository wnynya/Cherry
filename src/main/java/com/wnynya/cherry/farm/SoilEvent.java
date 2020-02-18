package com.wnynya.cherry.farm;

import com.wnynya.cherry.event.BlockBreak;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SoilEvent {

  public static void harvest(BlockBreakEvent event) {

    Block block = event.getBlock();
    Location loc = block.getLocation();
    Block soilBlock = loc.getWorld().getBlockAt(new Location( loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ()));

    if (block.getBlockData() instanceof Ageable) {
      if (soilBlock.getType().equals(Material.FARMLAND)) {

        Ageable ab = (Ageable) block.getBlockData();

        if (ab.getAge() == ab.getMaximumAge()) {
          Soil soil = Soil.getSoil(soilBlock.getLocation());
          if (block.getType().equals(Material.POTATOES)) {
            event.setDropItems(false);
            soil.harvest(Crop.POTATO);
          }
        }
        else {

        }

      }
    }
  }

  public static void plow(PlayerInteractEvent event) {
    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      if (event.getItem() != null
        && event.getItem().getType().equals(Material.IRON_HOE)
        && event.getItem().getType().equals(Material.WOODEN_HOE)
        && event.getItem().getType().equals(Material.STONE_HOE)
        && event.getItem().getType().equals(Material.GOLDEN_HOE)
        && event.getItem().getType().equals(Material.DIAMOND_HOE)) {

        Block block = event.getClickedBlock();

        if (block.getType().equals(Material.GRASS_BLOCK)
          && block.getType().equals(Material.DIRT)
          && block.getType().equals(Material.PODZOL)
          && block.getType().equals(Material.MYCELIUM)) {

          Soil soil = Soil.getSoil(block.getLocation());

        }

      }
    }
  }

}
