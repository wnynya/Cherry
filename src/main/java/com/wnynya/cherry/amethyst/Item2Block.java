package com.wnynya.cherry.amethyst;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.wnynya.cherry.Msg;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class Item2Block {

  public static void blockClick(PlayerInteractEvent event) {

    ItemStack item = event.getItem();

    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

      String v = "";
      // Apple
      if (item != null && item.getType().equals(Material.APPLE)) {
        v = SkullBank.APPLE.getValue();
      }
      // Pumpkin Pie
      else if (item != null && item.getType().equals(Material.PUMPKIN_PIE)) {
        v = SkullBank.PUMPKIN_PIE.getValue();
      }
      else {
        return;
      }

      Player player = event.getPlayer();
      Block block = event.getClickedBlock();
      if (block == null) {
        return;
      }
      Location loc = null;

      if (event.getBlockFace().equals(BlockFace.UP)) {
        loc = new Location(
          block.getWorld(),
          block.getX(),
          block.getY() + 1,
          block.getZ()
        );
      }
      else if (event.getBlockFace().equals(BlockFace.DOWN)) {
        loc = new Location(
          block.getWorld(),
          block.getX(),
          block.getY() - 1,
          block.getZ()
        );
      }
      else if (event.getBlockFace().equals(BlockFace.EAST)) {
        loc = new Location(
          block.getWorld(),
          block.getX() + 1,
          block.getY(),
          block.getZ()
        );
      }
      else if (event.getBlockFace().equals(BlockFace.WEST)) {
        loc = new Location(
          block.getWorld(),
          block.getX() - 1,
          block.getY(),
          block.getZ()
        );
      }
      else if (event.getBlockFace().equals(BlockFace.SOUTH)) {
        loc = new Location(
          block.getWorld(),
          block.getX(),
          block.getY(),
          block.getZ() + 1
        );
      }
      else if (event.getBlockFace().equals(BlockFace.NORTH)) {
        loc = new Location(
          block.getWorld(),
          block.getX(),
          block.getY(),
          block.getZ() - 1
        );
      }

      if (!loc.getBlock().getType().equals(Material.AIR)) {
        return;
      }

      Block b = loc.getBlock();
      BlockPlaceEvent be = new BlockPlaceEvent(b, b.getState(), b, item, event.getPlayer(), true, event.getHand());
      Bukkit.getPluginManager().callEvent(be);
      if (be.isCancelled()) {
        return;
      }
      if (player.getGameMode().equals(GameMode.SURVIVAL)) {
        if (item.getAmount() == 0) { item.setType(Material.AIR); }
        else { item.setAmount(item.getAmount() - 1); }
        player.getInventory().setItem(event.getHand(), item);
      }
      b.setBlockData(Bukkit.createBlockData(Material.PLAYER_HEAD), false);
      Skull bss = (Skull) b.getState();
      PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID(), "");
      playerProfile.getProperties().add(new ProfileProperty("textures", v));
      bss.setPlayerProfile(playerProfile);
      bss.update(true);
      return;

    }

  }

  public static void blockBreak(BlockBreakEvent event) {
    if (event.isCancelled()) {
      return;
    }

    Block block = event.getBlock();
    if (!block.getWorld().getGameRuleValue(GameRule.DO_TILE_DROPS)) {
      event.setDropItems(false);
      return;
    }

    if (event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
      if (block.getBlockData().equals(Bukkit.createBlockData(Material.PLAYER_HEAD))) {
        Skull bss = (Skull) block.getState();

        Msg.info(bss.getPlayerProfile().getProperties().toArray().toString());

        // Apple
        if (bss.getPlayerProfile() != null && bss.getPlayerProfile().getProperties().equals(new ProfileProperty("textures", SkullBank.APPLE.getValue()))) {
          event.setDropItems(false);
          block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.APPLE, 1));
        }

        // Pumpkin Pie
        else if (bss.getPlayerProfile() != null && bss.getPlayerProfile().getProperties().equals(new ProfileProperty("textures", SkullBank.PUMPKIN_PIE.getValue()))) {
          event.setDropItems(false);
          block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.PUMPKIN_PIE, 1));
        }

      }
    }

  }

  public static void blockPlace(BlockPlaceEvent event) {

  }
}
