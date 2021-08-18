package io.wany.cherry.itemonworld;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.wany.cherry.Cherry;
import io.wany.cherry.Console;
import io.wany.cherry.Message;
import io.wany.cherry.amethyst.Skull;
import io.wany.cherry.supports.coreprotect.CoreProtectSupport;
import io.wany.cherry.supports.cucumbery.CucumberySupport;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class ItemOnWorld {

  public static String COLOR = "#FF782D;";
  public static String PREFIX = COLOR + "&l[ItemOnWorld]:&r ";
  public static boolean ENABLED = false;

  private static final List<ItemOnWorld> itemOnWorlds = new ArrayList<>();

  private final ItemStack itemStack;
  private final List<Skull> skulls;
  private final HashMap<Player, Long> lastPlaceTimestamp = new HashMap<>();

  public ItemOnWorld(ItemStack itemStack, List<Skull> skulls) {
    this.itemStack = itemStack;
    this.skulls = skulls;
    itemOnWorlds.add(this);
  }

  public ItemOnWorld(Material material, int amount, List<Skull> skulls) {
    this(new ItemStack(material, amount), skulls);
  }

  private void onPlayerInteractEvent(PlayerInteractEvent event) {
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
    if (!itemStack.getType().equals(this.itemStack.getType())) {
      if (itemStack.getType().isBlock() && !itemStack.getType().equals(Material.AIR)) {
        return;
      }
      itemStack = inventory.getItemInOffHand();
      isMainHand = false;
      if (!itemStack.getType().equals(this.itemStack.getType())) {
        return;
      }
    }
    if (player.getGameMode().equals(GameMode.SURVIVAL) && !(itemStack.getAmount() >= this.itemStack.getAmount())) {
      return;
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
      if (itemStack.getAmount() - this.itemStack.getAmount() <= 0) {
        itemStack.setType(Material.AIR);
      }
      else {
        itemStack.setAmount(itemStack.getAmount() - this.itemStack.getAmount());
      }
      if (isMainHand) {
        inventory.setItemInMainHand(itemStack);
      }
      else {
        inventory.setItemInOffHand(itemStack);
      }
    }
    targetBlock.setBlockData(blockData, true);
    org.bukkit.block.Skull skull = (org.bukkit.block.Skull) targetBlock.getState();
    Skull randomSkull = this.skulls.get(new Random().nextInt(this.skulls.size()));
    PlayerProfile playerProfile = randomSkull.toPlayerProfile();
    skull.setPlayerProfile(playerProfile);
    skull.update(true);
    if (CoreProtectSupport.ENABLE && CoreProtectSupport.EXIST) {
      CoreProtectSupport.API.logPlacement(player.getName(), targetBlock.getLocation(), targetBlock.getType(), targetBlock.getBlockData());
    }
    if (lastPlaceTimestamp.containsKey(player)) {
      lastPlaceTimestamp.replace(player, System.currentTimeMillis());
    }
    else {
      lastPlaceTimestamp.put(player, System.currentTimeMillis());
    }
  }

  private void onItemSpawnEvent(ItemSpawnEvent event) {
    Item item = event.getEntity();
    ItemStack itemStack = item.getItemStack();
    if (!(itemStack.getType().equals(Material.PLAYER_HEAD) || itemStack.getType().equals(Material.PLAYER_WALL_HEAD))) {
      return;
    }
    SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
    PlayerProfile skullPlayerProfile = skullMeta.getPlayerProfile();
    if (skullPlayerProfile == null) {
      return;
    }
    List<ProfileProperty> profileProperties = skullPlayerProfile.getProperties().stream().toList();
    if (profileProperties.size() <= 0) {
      return;
    }
    String texture = profileProperties.get(0).getValue();
    List<String> textures = new ArrayList<>();
    for (Skull skull : this.skulls) {
      textures.add(skull.toPlayerProfile().getProperties().stream().toList().get(0).getValue());
    }
    if (!textures.contains(texture)) {
      return;
    }
    event.setCancelled(true);
    ItemStack drop = new ItemStack(this.itemStack.getType());
    drop.setAmount(this.itemStack.getAmount());
    event.getLocation().getWorld().dropItem(event.getLocation(), drop);
  }

  public static void onPlayerInteract(PlayerInteractEvent event) {
    if (!ENABLED) {
      return;
    }

    for (ItemOnWorld itemOnWorld : itemOnWorlds) {
      itemOnWorld.onPlayerInteractEvent(event);
    }

    DiceOnWorld.onPlayerInteractEvent(event);
  }

  public static void onItemSpawn(ItemSpawnEvent event) {
    if (!ENABLED) {
      return;
    }

    for (ItemOnWorld itemOnWorld : itemOnWorlds) {
      itemOnWorld.onItemSpawnEvent(event);
    }

    DiceOnWorld.onItemSpawnEvent(event);
  }

  public static void onEnable() {

    if (!Cherry.CONFIG.getBoolean("itemonworld.enable")) {
      Console.debug(Message.effect(PREFIX + "ItemOnWorld Disabled"));
      return;
    }
    Console.debug(Message.effect(PREFIX + "Enabling ItemOnWorld"));
    ENABLED = true;

    try {

      for (String materialName : Cherry.CONFIG.getConfigurationSection("itemonworld.items").getKeys(false)) {
        Material material;
        try {
          material = Material.valueOf(materialName);
        } catch (Exception e) {
          continue;
        }
        int amount = Cherry.CONFIG.getInt("itemonworld.items." + materialName + ".amount");
        amount = Math.min(127, Math.max(1, amount));
        List<String> textures = Cherry.CONFIG.getStringList("itemonworld.items." + materialName + ".textures");
        if (textures.size() <= 0) {
          continue;
        }
        List<Skull> skulls = new ArrayList<>();
        for (String texture : textures) {
          String base64Data;
          byte[] decodedBytes;
          String decodedString;
          try {
            decodedBytes = Base64.getDecoder().decode(texture);
            decodedString = new String(decodedBytes);
          } catch (Exception e) {
            decodedString = "";
          }
          if (decodedString.startsWith("{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/")) {
            base64Data = texture;
          }
          else {
            if (!texture.startsWith("http://textures.minecraft.net/texture/")) {
              texture = "http://textures.minecraft.net/texture/" + texture;
            }
            String JSONData = "{\"textures\":{\"SKIN\":{\"url\":\"" + texture + "\"}}}";
            base64Data = Base64.getEncoder().encodeToString(JSONData.getBytes());
          }
          skulls.add(new Skull(base64Data));
        }
        new ItemOnWorld(material, amount, skulls);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    DiceOnWorld.onEnable();
  }

}
