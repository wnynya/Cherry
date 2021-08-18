package io.wany.cherry.amethyst;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;

public class AppleOnWorld {

  private static final HashMap<Player, Long> lastPlaceTimestamp = new HashMap<>();

  public static void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    Block block = event.getClickedBlock();
    if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
      || !player.isSneaking()
      || player.getGameMode().equals(GameMode.ADVENTURE)
      || player.getGameMode().equals(GameMode.SPECTATOR)
      || (lastPlaceTimestamp.containsKey(player) && lastPlaceTimestamp.get(player) > System.currentTimeMillis() - 200)
      || block == null
    ) {
      return;
    }
    PlayerInventory inventory = player.getInventory();
    boolean isMainHand = true;
    ItemStack itemStack = inventory.getItemInMainHand();
    if (!itemStack.getType().equals(Material.APPLE)) {
      if (itemStack.getType().isBlock() && !itemStack.getType().equals(Material.AIR)) {
        return;
      }
      itemStack = inventory.getItemInOffHand();
      isMainHand = false;
      if (!itemStack.getType().equals(Material.APPLE)) {
        return;
      }
    }
    BlockFace blockFace = event.getBlockFace();
    Location targetLocation = null;
    BlockData blockData = null;
    if (blockFace.equals(BlockFace.UP)) {
      targetLocation = block.getLocation().add(0, 1 ,0);
      float yaw = event.getPlayer().getLocation().getYaw();
      if (yaw < 0) {
        yaw = 180 + (180 - Math.abs(yaw));
      }
      int rotation = (int) Math.round(yaw / 22.5);
      rotation = rotation % 16;
      blockData = Bukkit.createBlockData(Material.PLAYER_HEAD, "[rotation=" + rotation + "]");
    }
    else if (blockFace.equals(BlockFace.DOWN)) {
      targetLocation = block.getLocation().add(0, -1 ,0);
      float yaw = event.getPlayer().getLocation().getYaw();
      if (yaw < 0) {
        yaw = 180 + (180 - Math.abs(yaw));
      }
      int rotation = (int) Math.round(yaw / 22.5);
      rotation = rotation % 16;
      blockData = Bukkit.createBlockData(Material.PLAYER_HEAD, "[rotation=" + rotation + "]");
    }
    else if (blockFace.equals(BlockFace.EAST)) {
      targetLocation = block.getLocation().add(1, 0 ,0);
      blockData = Bukkit.createBlockData(Material.PLAYER_WALL_HEAD, "[facing=east]");
    }
    else if (blockFace.equals(BlockFace.WEST)) {
      targetLocation = block.getLocation().add(-1, 0 ,0);
      blockData = Bukkit.createBlockData(Material.PLAYER_WALL_HEAD, "[facing=west]");
    }
    else if (blockFace.equals(BlockFace.SOUTH)) {
      targetLocation = block.getLocation().add(0, 0 ,1);
      blockData = Bukkit.createBlockData(Material.PLAYER_WALL_HEAD, "[facing=south]");
    }
    else if (blockFace.equals(BlockFace.NORTH)) {
      targetLocation = block.getLocation().add(0, 0,-1);
      blockData = Bukkit.createBlockData(Material.PLAYER_WALL_HEAD, "[facing=north]");
    }
    if (targetLocation == null) {
      return;
    }
    Block targetBlock = block.getWorld().getBlockAt(targetLocation);
    if (!targetBlock.getType().equals(Material.AIR)) {
      return;
    }
    event.setCancelled(true);
    if (player.getGameMode().equals(GameMode.SURVIVAL)) {
      if (itemStack.getAmount() - 1 <= 0) {
        itemStack.setType(Material.AIR);
      }
      else {
        itemStack.setAmount(itemStack.getAmount() - 1);
      }
      if (isMainHand) {
        inventory.setItemInMainHand(itemStack);
      }
      else {
        inventory.setItemInOffHand(itemStack);
      }
    }
    targetBlock.setBlockData(blockData, true);
    Skull skull = (Skull) targetBlock.getState();
    skull.setPlayerProfile(io.wany.cherry.amethyst.Skull.APPLE.toPlayerProfile());
    skull.update(true);
    if (lastPlaceTimestamp.containsKey(player)) {
      lastPlaceTimestamp.replace(player, System.currentTimeMillis());
    }
    else {
      lastPlaceTimestamp.put(player, System.currentTimeMillis());
    }
  }

  public static void onItemSpawn(ItemSpawnEvent event) {
    Item item = event.getEntity();
    ItemStack itemStack = item.getItemStack();
    if (!(itemStack.getType().equals(Material.PLAYER_HEAD) || itemStack.getType().equals(Material.PLAYER_WALL_HEAD))) {
      return;
    }
    SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
    if (skullMeta.getPlayerProfile() == null || !skullMeta.getPlayerProfile().equals(io.wany.cherry.amethyst.Skull.APPLE.toPlayerProfile())) {
      return;
    }
    event.setCancelled(true);
    event.getLocation().getWorld().dropItem(event.getLocation(), new ItemStack(Material.APPLE));
  }

}
