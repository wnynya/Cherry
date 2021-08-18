package io.wany.cherry.wand;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WandBlock {

  private BlockData blockData;
  private final BlockState blockState;
  private Biome biome;
  private Location location;

  private Component nameableCustomName;
  private boolean lockableLocked;
  private String lockableLock;

  private DyeColor bannerBaseColor;
  private List<Pattern> bannerPatterns;
  private Location beehiveFlower;
  private PotionEffect beaconPrimaryEffect;
  private PotionEffect beaconSecondaryEffect;
  private double beaconEffectRange;
  private Collection<LivingEntity> beaconEntitiesInRange;
  private int beaconTier;
  private int brewingStandBrewingTime;
  private int brewingStandFuelLevel;
  private int campfireSize;
  private List<Integer> campfireCookTime = new ArrayList<>(List.of(0, 0, 0, 0));
  private List<Integer> campfireCookTimeTotal = new ArrayList<>(List.of(0, 0, 0, 0));
  private List<ItemStack> campfireItem = new ArrayList<>(List.of(new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)));
  private ItemStack[] chestContents;
  private String commandBlockCommand;
  private String commandBlockName;
  private ItemStack[] containerContents;
  private int creatureSpawnerDelay;
  private int creatureSpawnerMaxNearbyEntities;
  private int creatureSpawnerMaxSpawnDelay;
  private int creatureSpawnerMinSpawnDelay;
  private int creatureSpawnerRequiredPlayerRange;
  private int creatureSpawnerSpawnCount;
  private EntityType creatureSpawnerSpawnedType;
  private int creatureSpawnerSpawnRange;
  private long endGatewayAge;
  private Location endGatewayExitLocation;
  private boolean endGatewayExactTeleport;
  private int entityBlockStorageEntityCount;
  private int entityBlockStorageMaxEntities;
  private boolean entityBlockStorageFull;
  private short furnaceBurnTime;
  private short furnaceCookTime;
  private int furnaceCookTimeTotal;
  private double furnaceCookSpeedMultiplier;
  private Material jukeBoxPlaying;
  private ItemStack jukeBoxRecord;
  private boolean jukeBoxPlayingBoolean;
  private ItemStack[] lecternContents;
  private int lecternPage;
  private int sculkSensorVibrationFrequency;
  private DyeColor shulkerBoxColor;
  private List<Component> signLines;
  private boolean signGlowing;
  private boolean signEditable;
  private OfflinePlayer skullOwningPlayer;
  private PlayerProfile skullPlayerProfile;
  private String structureAuthor;
  private float structureIntegrity;
  private String structureMetadata;
  private Mirror structureMirror;
  private BlockVector structureRelativePosition;
  private StructureRotation structureRotation;
  private long structureSeed;
  private String structureStructureName;
  private BlockVector structureStructureSize;
  private UsageMode structureUsageMode;
  private boolean structureBoundingBoxVisible;
  private boolean structureIgnoreEntities;
  private boolean structureShowAir;
  private PersistentDataContainer tileStatePersistentDataContainer;

  public WandBlock(Block block) {
    this.blockData = block.getBlockData().clone();
    this.blockState = block.getState(true);
    this.biome = block.getBiome();
    this.location = block.getLocation().clone();
    parseBlockState();
  }

  public BlockData getBlockData() {
    return blockData;
  }

  public void setBlockData(BlockData blockData) {
    this.blockData = blockData;
  }

  public BlockState getBlockState() {
    return blockState;
  }

  public Biome getBiome() {
    return biome;
  }

  public void setBiome(Biome biome) {
    this.biome = biome;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  /* Type Parser (BlockEntity) */
  public void parseBlockState() {

    final BlockState bs = this.blockState;

    /* Nameable */
    if (bs instanceof Nameable b) {
      this.nameableCustomName = b.customName();
    }

    /* Lockable */
    if (bs instanceof Lockable b) {
      this.lockableLocked = b.isLocked();
      this.lockableLock = b.getLock();
    }

    /* Banner */
    if (bs instanceof Banner b) {
      this.bannerBaseColor = b.getBaseColor();
      this.bannerPatterns = b.getPatterns();
    }

    /* Barrel */
    if (bs instanceof Barrel b) {
      // No Data
    }

    /* Bed */
    if (bs instanceof Bed b) {
      // Deprecated
    }

    /* Beehive */
    if (bs instanceof Beehive b) {
      this.beehiveFlower = b.getFlower();
    }

    /* Beacon */
    if (bs instanceof Beacon b) {
      this.beaconPrimaryEffect = b.getPrimaryEffect();
      this.beaconSecondaryEffect = b.getSecondaryEffect();
      this.beaconEffectRange = b.getEffectRange();
      this.beaconEntitiesInRange = b.getEntitiesInRange();
      this.beaconTier = b.getTier();
    }

    /* Bell */
    if (bs instanceof Bell b) {
      // No data
    }

    /* BlastFurnace */
    if (bs instanceof BlastFurnace b) {
      // No data
    }

    /* BrewingStand */
    if (bs instanceof BrewingStand b) {
      this.brewingStandBrewingTime = b.getBrewingTime();
      this.brewingStandFuelLevel = b.getFuelLevel();
    }

    /* Campfire */
    if (bs instanceof Campfire b) {
      this.campfireSize = b.getSize();
      for (int n = 0; n < this.campfireSize; n++) {
        this.campfireCookTime.set(n, b.getCookTime(n));
        this.campfireCookTimeTotal.set(n, b.getCookTimeTotal(n));
        this.campfireItem.set(n, b.getItem(n));
      }
    }

    /* Chest */
    if (bs instanceof Chest b) {
      this.chestContents = b.getBlockInventory().getContents();
    }

    /* CommandBlock */
    if (bs instanceof CommandBlock b) {
      this.commandBlockCommand = b.getCommand();
      this.commandBlockName = b.getName();
    }

    /* Comparator */
    if (bs instanceof Comparator b) {
      // No data
    }

    /* Conduit */
    if (bs instanceof Conduit b) {
      // No data
    }

    /* Container */
    if (bs instanceof Container b) {
      this.containerContents = b.getSnapshotInventory().getContents().clone();
    }

    /* CreatureSpawner */
    if (bs instanceof CreatureSpawner b) {
      this.creatureSpawnerDelay = b.getDelay();
      this.creatureSpawnerMaxNearbyEntities = b.getMaxNearbyEntities();
      this.creatureSpawnerMaxSpawnDelay = b.getMaxSpawnDelay();
      this.creatureSpawnerMinSpawnDelay = b.getMinSpawnDelay();
      this.creatureSpawnerRequiredPlayerRange = b.getRequiredPlayerRange();
      this.creatureSpawnerSpawnCount = b.getSpawnCount();
      this.creatureSpawnerSpawnedType = b.getSpawnedType();
      this.creatureSpawnerSpawnRange = b.getSpawnRange();
    }

    /* DaylightDetector */
    if (bs instanceof DaylightDetector b) {
      // No data
    }

    /* Dispenser */
    if (bs instanceof Dispenser b) {
      // No data
    }

    /* Dropper */
    if (bs instanceof Dropper b) {
      // No data
    }

    /* EnchantingTable */
    if (bs instanceof EnchantingTable b) {
      // No Data
    }

    /* EnderChest */
    if (bs instanceof EnderChest b) {
      // No data
    }

    /* EndGateway */
    if (bs instanceof EndGateway b) {
      this.endGatewayAge = b.getAge();
      this.endGatewayExitLocation = b.getExitLocation();
      this.endGatewayExactTeleport = b.isExactTeleport();
    }

    /* EntityBlockStorage */
    if (bs instanceof EntityBlockStorage b) {
      this.entityBlockStorageEntityCount = b.getEntityCount();
      this.entityBlockStorageMaxEntities = b.getMaxEntities();
      this.entityBlockStorageFull = b.isFull();
      /*List<Entity> entityBlockStorageEntities = b.releaseEntities();
      for (Entity entity : entityBlockStorageEntities) {
        b.addEntity(entity);
      }*/
    }

    /* Furnace */
    if (bs instanceof Furnace b) {
      this.furnaceBurnTime = b.getBurnTime();
      this.furnaceCookTime = b.getCookTime();
      this.furnaceCookTimeTotal = b.getCookTimeTotal();
      this.furnaceCookSpeedMultiplier = b.getCookSpeedMultiplier();
    }

    /* Hopper */
    if (bs instanceof Hopper b) {
      // No data
    }

    /* Jigsaw */
    if (bs instanceof Jigsaw b) {
      // No data
    }

    /* Jukebox */
    if (bs instanceof Jukebox b) {
      this.jukeBoxPlaying = b.getPlaying();
      this.jukeBoxRecord = b.getRecord();
      this.jukeBoxPlayingBoolean = b.isPlaying();
    }

    /* Lectern */
    if (bs instanceof Lectern b) {
      this.lecternContents = b.getSnapshotInventory().getContents().clone();
      this.lecternPage = b.getPage();
    }

    /* SculkSensor */
    if (bs instanceof SculkSensor b) {
      this.sculkSensorVibrationFrequency = b.getLastVibrationFrequency();
    }

    /* ShulkerBox */
    if (bs instanceof ShulkerBox b) {
      this.shulkerBoxColor = b.getColor();
    }

    /* Sign */
    if (bs instanceof Sign b) {
      this.signLines = b.lines();
      this.signGlowing = b.isGlowingText();
      this.signEditable = b.isEditable();
    }

    /* Skull */
    if (bs instanceof Skull b) {
      this.skullOwningPlayer = b.getOwningPlayer();
      this.skullPlayerProfile = b.getPlayerProfile();
    }

    /* Smoker */
    if (bs instanceof Smoker b) {
      // No Data
    }

    /* Structure */
    if (bs instanceof Structure b) {
      this.structureAuthor = b.getAuthor();
      this.structureIntegrity = b.getIntegrity();
      this.structureMetadata = b.getMetadata();
      this.structureMirror = b.getMirror();
      this.structureRelativePosition = b.getRelativePosition();
      this.structureRotation = b.getRotation();
      this.structureSeed = b.getSeed();
      this.structureStructureName = b.getStructureName();
      this.structureStructureSize = b.getStructureSize();
      this.structureUsageMode = b.getUsageMode();
      this.structureBoundingBoxVisible = b.isBoundingBoxVisible();
      this.structureIgnoreEntities = b.isIgnoreEntities();
      this.structureShowAir = b.isShowAir();
    }

    /* TileState */
    if (bs instanceof TileState b) {
      this.tileStatePersistentDataContainer = b.getPersistentDataContainer();
    }

  }

  public void applyBlockState(Block block, boolean applyPhysics) {

    final BlockState bs = block.getState(false);

    /* Nameable */
    if (bs instanceof Nameable b) {
      b.customName(this.nameableCustomName);
    }

    /* Lockable */
    if (bs instanceof Lockable b) {
      b.setLock(this.lockableLock);
    }

    /* Banner */
    if (bs instanceof Banner b) {
      b.setBaseColor(this.bannerBaseColor);
      b.setPatterns(this.bannerPatterns);
    }

    /* Barrel */
    if (bs instanceof Barrel b) {
      // No Data
    }

    /* Bed */
    if (bs instanceof Bed b) {
      // Deprecated
    }

    /* Beehive */
    if (bs instanceof Beehive b) {
      b.setFlower(this.beehiveFlower);
    }

    /* Beacon */
    if (bs instanceof Beacon b) {
      b.setEffectRange(this.beaconEffectRange);
      if (this.beaconPrimaryEffect != null) {
        b.setPrimaryEffect(this.beaconPrimaryEffect.getType());
      }
      if (this.beaconSecondaryEffect != null) {
        b.setSecondaryEffect(this.beaconSecondaryEffect.getType());
      }
    }

    /* Bell */
    if (bs instanceof Bell b) {
      // No data
    }

    /* BlastFurnace */
    if (bs instanceof BlastFurnace b) {
      // No data
    }

    /* BrewingStand */
    if (bs instanceof BrewingStand b) {
      b.setBrewingTime(this.brewingStandBrewingTime);
      b.setFuelLevel(this.brewingStandFuelLevel);
    }

    /* Campfire */
    if (bs instanceof Campfire b) {
      for (int n = 0; n < this.campfireSize; n++) {
        b.setItem(n, this.campfireItem.get(n));
        b.setCookTime(n, this.campfireCookTime.get(n));
        b.setCookTimeTotal(n, this.campfireCookTimeTotal.get(n));
      }
    }

    /* Chest */
    if (bs instanceof Chest b) {
      b.getBlockInventory().setStorageContents(this.chestContents);
    }

    /* CommandBlock */
    if (bs instanceof CommandBlock b) {
      b.setCommand(this.commandBlockCommand);
      b.setName(this.commandBlockName);
    }

    /* Comparator */
    if (bs instanceof Comparator b) {
      // No data
    }

    /* Conduit */
    if (bs instanceof Conduit b) {
      // No data
    }

    /* Container */
    if (bs instanceof Container b) {
      b.getInventory().setStorageContents(this.containerContents);
    }

    /* CreatureSpawner */
    if (bs instanceof CreatureSpawner b) {
      b.setDelay(this.creatureSpawnerDelay);
      b.setMaxNearbyEntities(this.creatureSpawnerMaxNearbyEntities);
      b.setMaxSpawnDelay(this.creatureSpawnerMaxSpawnDelay);
      b.setMinSpawnDelay(this.creatureSpawnerMinSpawnDelay);
      b.setRequiredPlayerRange(this.creatureSpawnerRequiredPlayerRange);
      b.setSpawnCount(this.creatureSpawnerSpawnCount);
      b.setSpawnedType(this.creatureSpawnerSpawnedType);
      b.setSpawnRange(this.creatureSpawnerSpawnRange);
    }

    /* DaylightDetector */
    if (bs instanceof DaylightDetector b) {
      // No data
    }

    /* Dispenser */
    if (bs instanceof Dispenser b) {
      // No data
    }

    /* Dropper */
    if (bs instanceof Dropper b) {
      // No data
    }

    /* EnchantingTable */
    if (bs instanceof EnchantingTable b) {

    }

    /* EnderChest */
    if (bs instanceof EnderChest b) {
      // No data
    }

    /* EndGateway */
    if (bs instanceof EndGateway b) {
      b.setAge(this.endGatewayAge);
      b.setExitLocation(this.endGatewayExitLocation);
      b.setExactTeleport(this.endGatewayExactTeleport);
    }

    /* EntityBlockStorage */
    if (bs instanceof EntityBlockStorage b) {
      b.setMaxEntities(this.entityBlockStorageMaxEntities);
    }

    /* Furnace */
    if (bs instanceof Furnace b) {
      b.setBurnTime(this.furnaceBurnTime);
      b.setCookTime(this.furnaceCookTime);
      b.setCookTimeTotal(this.furnaceCookTimeTotal);
      b.setCookSpeedMultiplier(this.furnaceCookSpeedMultiplier);
    }

    /* Hopper */
    if (bs instanceof Hopper b) {
      // No data
    }

    /* Jigsaw */
    if (bs instanceof Jigsaw b) {
      // No data
    }

    /* Jukebox */
    if (bs instanceof Jukebox b) {
      b.setPlaying(this.jukeBoxPlaying);
      b.setRecord(this.jukeBoxRecord);
    }

    /* Lectern */
    if (bs instanceof Lectern b) {
      b.getInventory().setStorageContents(this.lecternContents);
      b.setPage(this.lecternPage);
    }

    /* SculkSensor */
    if (bs instanceof SculkSensor b) {
      b.setLastVibrationFrequency(this.sculkSensorVibrationFrequency);
    }

    /* ShulkerBox */
    if (bs instanceof ShulkerBox b) {
      // No Data
    }

    /* Sign */
    if (bs instanceof Sign b) {
      for (int n = 0; n < this.signLines.size(); n++) {
        b.line(n, this.signLines.get(n));
      }
      b.setEditable(this.signEditable);
      b.setGlowingText(this.signGlowing);
    }

    /* Skull */
    if (bs instanceof Skull b) {
      b.setPlayerProfile(this.skullPlayerProfile);
    }

    /* Smoker */
    if (bs instanceof Smoker b) {
      // No Data
    }

    /* Structure */
    if (bs instanceof Structure b) {
      b.setAuthor(this.structureAuthor);
      b.setIntegrity(this.structureIntegrity);
      b.setMetadata(this.structureMetadata);
      b.setMirror(this.structureMirror);
      b.setRelativePosition(this.structureRelativePosition);
      b.setRotation(this.structureRotation);
      b.setSeed(this.structureSeed);
      b.setStructureName(this.structureStructureName);
      b.setStructureSize(this.structureStructureSize);
      b.setUsageMode(this.structureUsageMode);
      b.setBoundingBoxVisible(this.structureBoundingBoxVisible);
      b.setIgnoreEntities(this.structureIgnoreEntities);
      b.setShowAir(this.structureShowAir);
    }

    /* TileState */
    if (bs instanceof TileState b) {
      // No Data
    }

    bs.update(true, applyPhysics);

  }

  public void apply(Block block, boolean applyPhysics) {
    block.setBlockData(this.blockData, applyPhysics);
    try {
      applyBlockState(block, applyPhysics);
    } catch (Exception e) {
      e.printStackTrace();
    }
    block.setBiome(this.biome);
  }

}
