package com.wnynya.cherry.wand;

import com.wnynya.cherry.Msg;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class WandBlock {

  private Location blockLocation;
  private final BlockData blockData;
  private final BlockState blockState;

  private Inventory inventory = null;
  private DyeColor baseColor = null;
  private List<Pattern> patterns = null;
  private String customName;
  private PotionEffect primaryEffect;
  private PotionEffect secondaryEffect;
  private ItemStack[] contents = null;
  private int brewingTime;
  private int fuelLevel;
  private short burnTime;
  private short fcookTime;
  private int cookTimeTotal;
  private int[] cookTime_ = new int[4];
  private int[] cookTimeTotal_ = new int[4];
  private ItemStack[] item_ = new ItemStack[4];
  private String command;
  private String name;
  private int delay;
  private int maxNearbyEntities;
  private int maxSpawnDelay;
  private int minSpawnDelay;
  private int requiredPlayerRange;
  private int spaenCount;
  private int spawnRange;
  private EntityType spawnedType;
  private long age;
  private Location exitLocation;
  private Material playing;
  private ItemStack record;
  private int page;
  private DyeColor color;
  private OfflinePlayer owningPlayer;
  private String[] lines;
  private Location flower;

  public WandBlock(org.bukkit.block.Block block) {
    this.blockLocation = block.getLocation().clone();
    this.blockData = block.getBlockData().clone();
    this.blockState = block.getState();
    parseBlockState();
  }

  public Location getLocation() {
    return blockLocation;
  }

  public BlockData getBlockData() {
    return blockData;
  }

  public BlockState getBlockState() {
    return blockState;
  }

  public void setLocation(Location loc) {
    this.blockLocation = loc;
  }

  /* Type Parser (BlockEntity) */
  public void parseBlockState() {
    final BlockState bs = this.blockState;

    /* Banner */
    if (bs instanceof Banner) {
      Banner b = (Banner) bs;
      this.baseColor = b.getBaseColor();
      this.patterns = b.getPatterns();
    }

    /* Barrel */
    /* BlastFurnace */
    /* BrewingStand */
    /* Chest */
    /* Dispenser */
    /* Dropper */
    /* Furnace */
    /* Hopper */
    /* ShulkerBox */
    /* Smoker */
    if (bs instanceof Container) {
      Container b = (Container) bs;
      this.contents = b.getSnapshotInventory().getContents().clone();
      this.customName = b.getCustomName();
    }

    /* Beacon */
    if (bs instanceof Beacon) {
      Beacon b = (Beacon) bs;
      this.primaryEffect = b.getPrimaryEffect();
      this.secondaryEffect = b.getSecondaryEffect();
    }

    /* Beehive */
    if (bs instanceof Beehive) {
      Beehive b = (Beehive) bs;
      this.flower = b.getFlower();
    }

    /* BlastFurnace */
    if (bs instanceof BlastFurnace) {
      BlastFurnace b = (BlastFurnace) bs;
      this.burnTime = b.getBurnTime();
      this.fcookTime = b.getCookTime();
      this.cookTimeTotal = b.getCookTimeTotal();
    }

    /* BrewingStand */
    if (bs instanceof BrewingStand) {
      BrewingStand b = (BrewingStand) bs;
      this.brewingTime = b.getBrewingTime();
      this.fuelLevel = b.getFuelLevel();
    }

    /* Campfire */
    if (bs instanceof Campfire) {
      Campfire b = (Campfire) bs;
      //Msg.warn("CAMPFIRE SIZE: " + b.getSize());
      for (int n = 0; n < 4; n++) {
        this.cookTime_[n] = b.getCookTime(n);
        this.cookTimeTotal_[n] = b.getCookTimeTotal(n);
        this.item_[n] = b.getItem(n);
      }
    }

    /* CommandBlock */
    if (bs instanceof CommandBlock) {
      CommandBlock b = (CommandBlock) bs;
      this.command = b.getCommand();
      this.name = b.getName();
    }

    /* CreatureSpawner */
    if (bs instanceof CreatureSpawner) {
      CreatureSpawner b = (CreatureSpawner) bs;
      this.delay = b.getDelay();
      this.maxNearbyEntities = b.getMaxNearbyEntities();
      this.maxSpawnDelay = b.getMaxSpawnDelay();
      this.minSpawnDelay = b.getMinSpawnDelay();
      this.requiredPlayerRange = b.getRequiredPlayerRange();
      this.spaenCount = b.getSpawnCount();
      this.spawnedType = b.getSpawnedType();
      this.spawnRange = b.getSpawnRange();
    }

    /* EnchantingTable */
    if (bs instanceof EnchantingTable) {
      EnchantingTable b = (EnchantingTable) bs;
      this.customName = b.getCustomName();
    }

    /* EndGateway */
    if (bs instanceof EndGateway) {
      EndGateway b = (EndGateway) bs;
      this.age = b.getAge();
      this.exitLocation = b.getExitLocation();
    }

    /* Furnace */
    if (bs instanceof Furnace) {
      Furnace b = (Furnace) bs;
      this.burnTime = b.getBurnTime();
      this.fcookTime = b.getCookTime();
      this.cookTimeTotal = b.getCookTimeTotal();
    }

    /* Jukebox */
    if (bs instanceof Jukebox) {
      Jukebox b = (Jukebox) bs;
      this.playing = b.getPlaying();
      this.record = b.getRecord();
    }

    /* Lectern */
    if (bs instanceof Lectern) {
      Lectern b = (Lectern) bs;
      this.contents = b.getSnapshotInventory().getContents().clone();
      this.page = b.getPage();
    }

    /* Sign */
    if (bs instanceof Sign) {
      Sign b = (Sign) bs;
      this.lines = b.getLines();
    }

    /* Skull */
    if (bs instanceof Skull) {
      Skull b = (Skull) bs;
      this.owningPlayer = b.getOwningPlayer();
    }

    /* Smoker */
    if (bs instanceof Smoker) {
      Smoker b = (Smoker) bs;
      this.burnTime = b.getBurnTime();
      this.fcookTime = b.getCookTime();
      this.cookTimeTotal = b.getCookTimeTotal();
    }

    /* ShulkerBox */
    if (bs instanceof ShulkerBox) {
      ShulkerBox b = (ShulkerBox) bs;
      // 무의미
    }

    /* Bell */
    if (bs instanceof Bell) {
      Bell b = (Bell) bs;
      // No Data
    }

    /* Comparator */
    if (bs instanceof Comparator) {
      Comparator b = (Comparator) bs;
      // No Data
    }

    /* Conduit */
    if (bs instanceof Conduit) {
      Conduit b = (Conduit) bs;
      // No Data
    }

    /* DaylightDetector */
    if (bs instanceof DaylightDetector) {
      DaylightDetector b = (DaylightDetector) bs;
      // No Data
    }

    /* EnderChest */
    if (bs instanceof EnderChest) {
      EnderChest b = (EnderChest) bs;
      // No Data
    }

    /* Jigsaw */
    if (bs instanceof Jigsaw) {
      Jigsaw b = (Jigsaw) bs;
      // No Data
    }

    /* Bed */
    // Deprecated
  }

  public void applyBlockState(Block block) {
    final BlockState bs = block.getState();

    /* Banner */
    if (bs instanceof Banner) {
      Banner b = (Banner) bs;
      b.setBaseColor(this.baseColor);
      b.setPatterns(this.patterns);
      b.update(true, false);
    }

    /* Beacon */
    if (bs instanceof Beacon) {
      Beacon b = (Beacon) bs;
      b.setPrimaryEffect(this.primaryEffect.getType());
      b.setSecondaryEffect(this.secondaryEffect.getType());
      b.update(true, false);
    }

    /* Beehive */
    if (bs instanceof Beehive) {
      Beehive b = (Beehive) bs;
      b.setFlower(this.flower);
    }

    /* BlastFurnace */
    if (bs instanceof BlastFurnace) {
      BlastFurnace b = (BlastFurnace) bs;
      b.setBurnTime(this.burnTime);
      b.setCookTime(this.fcookTime);
      b.setCookTimeTotal(this.cookTimeTotal);
      b.update(true, false);
    }

    /* BrewingStand */
    if (bs instanceof BrewingStand) {
      BrewingStand b = (BrewingStand) bs;
      b.setBrewingTime(this.brewingTime);
      b.setFuelLevel(this.fuelLevel);
      b.update(true, false);
    }

    /* Campfire */
    if (bs instanceof Campfire) {
      Campfire b = (Campfire) bs;
      for (int n = 0; n < 4; n++) {
        try {
          b.setCookTime(n, this.cookTime_[n]);
          b.setCookTimeTotal(n, this.cookTimeTotal_[n]);
          b.setItem(n, this.item_[n]);
        } catch (Exception ignored) { }
      }
      b.update(true, false);
    }

    /* CommandBlock */
    if (bs instanceof CommandBlock) {
      CommandBlock b = (CommandBlock) bs;
      b.setCommand(this.command);
      b.setName(this.name);
      b.update(true, false);
    }

    /* CreatureSpawner */
    if (bs instanceof CreatureSpawner) {
      CreatureSpawner b = (CreatureSpawner) bs;
      b.setDelay(this.delay);
      b.setMaxNearbyEntities(this.maxNearbyEntities);
      b.setMaxSpawnDelay(this.maxSpawnDelay);
      b.setMinSpawnDelay(this.minSpawnDelay);
      b.setRequiredPlayerRange(this.requiredPlayerRange);
      b.setSpawnCount(this.spaenCount);
      b.setSpawnedType(this.spawnedType);
      b.setSpawnRange(this.spawnRange);
      b.update(true, false);
    }

    /* EnchantingTable */
    if (bs instanceof EnchantingTable) {
      EnchantingTable b = (EnchantingTable) bs;
      b.setCustomName(this.customName);
      b.update(true, false);
    }

    /* EndGateway */
    if (bs instanceof EndGateway) {
      EndGateway b = (EndGateway) bs;
      b.setAge(this.age);
      b.setExitLocation(this.exitLocation);
      b.update(true, false);
    }

    /* Furnace */
    if (bs instanceof Furnace) {
      Furnace b = (Furnace) bs;
      b.setBurnTime(this.burnTime);
      b.setCookTime(this.fcookTime);
      b.setCookTimeTotal(this.cookTimeTotal);
      b.update(true, false);
    }

    /* Jukebox */
    if (bs instanceof Jukebox) {
      Jukebox b = (Jukebox) bs;
      b.setPlaying(this.playing);
      b.setRecord(this.record);
      b.update(true, false);
    }

    /* Lectern */
    if (bs instanceof Lectern) {
      Lectern b = (Lectern) bs;
      b.setPage(this.page);
      b.update(true, false);
      b.getInventory().setContents(this.contents);
    }

    /* Sign */
    if (bs instanceof Sign) {
      Sign b = (Sign) bs;
      for (int n = 0; n < this.lines.length; n++) {
        b.setLine(n, this.lines[n]);
      }
      b.update(true, false);
    }

    /* Skull */
    if (bs instanceof Skull) {
      Skull b = (Skull) bs;
      b.setOwningPlayer(this.owningPlayer);
      b.update(true, false);
    }

    /* Smoker */
    if (bs instanceof Smoker) {
      Smoker b = (Smoker) bs;
      b.setBurnTime(this.burnTime);
      b.setCookTime(this.fcookTime);
      b.setCookTimeTotal(this.cookTimeTotal);
      b.update(true, false);
    }

    /* Barrel */
    /* BlastFurnace */
    /* BrewingStand */
    /* Chest */
    /* Dispenser */
    /* Dropper */
    /* Furnace */
    /* Hopper */
    /* ShulkerBox */
    /* Smoker */
    if (bs instanceof Container) {
      Container b = (Container) bs;
      b.setCustomName(this.customName);
      b.update(true, false);
      b.getInventory().setContents(this.contents);
    }

    /* ShulkerBox */
    if (bs instanceof ShulkerBox) {
      ShulkerBox b = (ShulkerBox) bs;
      // 무의미
      this.color = b.getColor();
    }

    /* Bell */
    if (bs instanceof Bell) {
      Bell b = (Bell) bs;
      // No Data
    }

    /* Comparator */
    if (bs instanceof Comparator) {
      Comparator b = (Comparator) bs;
      // No Data
    }

    /* Conduit */
    if (bs instanceof Conduit) {
      Conduit b = (Conduit) bs;
      // No Data
    }

    /* DaylightDetector */
    if (bs instanceof DaylightDetector) {
      DaylightDetector b = (DaylightDetector) bs;
      // No Data
    }

    /* EnderChest */
    if (bs instanceof EnderChest) {
      EnderChest b = (EnderChest) bs;
      // No Data
    }

    /* Jigsaw */
    if (bs instanceof Jigsaw) {
      Jigsaw b = (Jigsaw) bs;
      // No Data
    }

    /* Bed */
    // Deprecated
  }

  /* get blockstates */

  public Inventory getInventory() {
    return inventory;
  }

  public DyeColor getBaseColor() {
    return baseColor;
  }

  public List<Pattern> getPatterns() {
    return patterns;
  }

  public String getCustomName() {
    return customName;
  }

  public PotionEffect getPrimaryEffect() {
    return primaryEffect;
  }

  public PotionEffect getSecondaryEffect() {
    return secondaryEffect;
  }

  public ItemStack[] getContents() {
    return contents;
  }

  public int getBrewingTime() {
    return brewingTime;
  }

  public int getFuelLevel() {
    return fuelLevel;
  }

  public Location getBlockLocation() {
    return blockLocation;
  }

  public int getBurnTime() {
    return burnTime;
  }

  public int[] getCookTime_() {
    return cookTime_;
  }

  public int[] getCookTimeTotal_() {
    return cookTimeTotal_;
  }

  public int getCookTimeTotal() {
    return cookTimeTotal;
  }

  public int getDelay() {
    return delay;
  }

  public int getMaxNearbyEntities() {
    return maxNearbyEntities;
  }

  public int getMaxSpawnDelay() {
    return maxSpawnDelay;
  }

  public int getMinSpawnDelay() {
    return minSpawnDelay;
  }

  public int getRequiredPlayerRange() {
    return requiredPlayerRange;
  }

  public String getCommand() {
    return command;
  }

  public int getSpaenCount() {
    return spaenCount;
  }

  public int getSpawnRange() {
    return spawnRange;
  }

  public String getName() {
    return name;
  }

  public ItemStack[] getItem_() {
    return item_;
  }

  public Location getExitLocation() {
    return exitLocation;
  }

  public long getAge() {
    return age;
  }

  public EntityType getSpawnedType() {
    return spawnedType;
  }

  public int getPage() {
    return page;
  }

  public Material getPlaying() {
    return playing;
  }

  public DyeColor getColor() {
    return color;
  }

  public ItemStack getRecord() {
    return record;
  }

  public OfflinePlayer getOwningPlayer() {
    return owningPlayer;
  }

  public String[] getLines() {
    return lines;
  }

  public Location getFlower() {
    return flower;
  }

}
