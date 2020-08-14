package com.wnynya.cherry.wand;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.wand.area.Area;
import com.wnynya.cherry.wand.command.WandCommand;
import com.wnynya.cherry.wand.command.WandTabCompleter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Campfire;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Cherry Wand
 */

public class Wand {

  public static boolean enabled = false;

  private static HashMap<UUID, Wand> wands = new HashMap<>();

  private Player player = null;
  private UUID uuid;
  private WandEdit edit;
  private WandBrush brush;

  private Wand(UUID uuid) {
    this.uuid = uuid;
    this.edit = new WandEdit(this);
    this.brush = new WandBrush(this);
    wands.put(uuid, this);
    this.enableParticleArea();
  }

  private Wand(Player player) {
    this.player = player;
    this.uuid = player.getUniqueId();
    this.edit = new WandEdit(this);
    this.brush = new WandBrush(this);
    wands.put(uuid, this);
    this.enableParticleArea();
  }

  public WandEdit getEdit() {
    return edit;
  }

  public WandBrush getBrush() {
    return brush;
  }

  /**
   * Save Modify History
   */

  private Stack<List<WandBlock>> undoStack = new Stack<>();
  private Stack<List<WandBlock>> redoStack = new Stack<>();

  private static int undoLimit = 50;

  public boolean undo() {
    if (this.undoStack.size() == 0) {
      return false;
    }
    List<WandBlock> wBlocks = undoStack.peek();
    List<Location> area = new ArrayList<>();
    for (WandBlock wBlock : wBlocks) {
      area.add(wBlock.getLocation());
    }
    storeRedo(area);
    undoStack.pop();
    for (WandBlock wBlock : wBlocks) {
      Block block = wBlock.getLocation().getBlock();
      BlockData blockData = wBlock.getBlockData();
      block.setBlockData(blockData, false);
      wBlock.applyBlockState(block);
    }
    return true;
  }

  public boolean redo() {
    if (this.redoStack.size() == 0) {
      return false;
    }
    List<WandBlock> wBlocks = redoStack.peek();
    List<Location> area = new ArrayList<>();
    for (WandBlock wBlock : wBlocks) {
      area.add(wBlock.getLocation());
    }
    storeUndo(area);
    redoStack.pop();
    for (WandBlock wBlock : wBlocks) {
      Block block = wBlock.getLocation().getBlock();
      BlockData blockData = wBlock.getBlockData();
      block.setBlockData(blockData, false);
      wBlock.applyBlockState(block);
    }
    return true;
  }

  public void storeUndo(List<Location> area) {
    List<WandBlock> wBlocks = new ArrayList<>();
    for (Location loc : area) {
      Block block = loc.getBlock();
      WandBlock wBlock = new WandBlock(block);
      wBlocks.add(wBlock);
    }
    if (undoStack.size() >= undoLimit) {
      undoStack.remove(0);
    }
    undoStack.push(wBlocks);
  }

  public void storeRedo(List<Location> area) {
    List<WandBlock> wBlocks = new ArrayList<>();
    for (Location loc : area) {
      Block block = loc.getBlock();
      WandBlock wBlock = new WandBlock(block);
      wBlocks.add(wBlock);
    }
    if (redoStack.size() >= undoLimit) {
      redoStack.remove(0);
    }
    redoStack.push(wBlocks);
  }

  /**
   * Clipboard
   */

  private List<WandBlock> clipboardMemory = null;
  private Location clipboardWPlayer = null;
  private Location clipboardWPlayerB = null;

  public void copy(Location pos1, Location pos2, Location posP) {
    copy(pos1, pos2, posP, null);
  }

  public void copy(Location pos1, Location pos2, Location posP, List<Material> blackList) {
    if (pos1 == null || pos2 == null || posP == null) {
      return;
    }

    int minX = (int) Math.min(pos1.getX(), pos2.getX());
    int minY = (int) Math.min(pos1.getY(), pos2.getY());
    int minZ = (int) Math.min(pos1.getZ(), pos2.getZ());

    int xb, yb, zb;
    if ((int) posP.getX() >= minX) {
      xb = 0;
    }
    else {
      xb = 1;
    }
    if ((int) posP.getY() >= minY) {
      yb = 0;
    }
    else {
      yb = 1;
    }
    if ((int) posP.getZ() >= minZ) {
      zb = 0;
    }
    else {
      zb = 1;
    }

    this.clipboardWPlayerB = new Location(posP.getWorld(), xb, yb, zb);

    int disX = (int) Math.abs(posP.getX() - minX);
    int disY = (int) Math.abs(posP.getY() - minY);
    int disZ = (int) Math.abs(posP.getZ() - minZ);

    this.clipboardWPlayer = new Location(posP.getWorld(), disX, disY, disZ);

    List<WandBlock> wBlocks = new ArrayList<>();
    List<Location> area = Area.CUBE.getArea(pos1, pos2);
    for (Location loc : area) {
      Block block = loc.getBlock();
      if (blackList != null && blackList.size() > 0) {
        for ( Material m : blackList) {
          if (block.getType().equals(m)) {
            continue;
          }
        }
      }
      WandBlock wBlock = new WandBlock(block);
      wBlock.setLocation(new Location(loc.getWorld(), (int) loc.getX() - minX, (int) loc.getY() - minY, (int) loc.getZ() - minZ));
      wBlocks.add(wBlock);
    }
    this.clipboardMemory = wBlocks;
  }

  public void paste(Location pos) {
    if (this.clipboardMemory == null) {
      return;
    }

    for (WandBlock wBlock : this.clipboardMemory) {
      Location bLoc = wBlock.getLocation();
      int x, y, z;
      if (this.clipboardWPlayerB.getX() == 1) {
        x = (int) (pos.getX() + bLoc.getX() + this.clipboardWPlayer.getX());
      }
      else {
        x = (int) (pos.getX() + bLoc.getX() - this.clipboardWPlayer.getX());
      }
      if (this.clipboardWPlayerB.getY() == 1) {
        y = (int) (pos.getY() + bLoc.getY() + this.clipboardWPlayer.getY());
      }
      else {
        y = (int) (pos.getY() + bLoc.getY() - this.clipboardWPlayer.getY());
      }
      if (this.clipboardWPlayerB.getZ() == 1) {
        z = (int) (pos.getZ() + bLoc.getZ() + this.clipboardWPlayer.getZ());
      }
      else {
        z = (int) (pos.getZ() + bLoc.getZ() - this.clipboardWPlayer.getZ());
      }
      Location loc = new Location(pos.getWorld(), x, y, z);
      Block block = loc.getBlock();
      block.setBlockData(wBlock.getBlockData());
      try {
        wBlock.applyBlockState(block);
      }
      catch (Exception ignored) {
      }
    }
  }

  public void rotate(int deg) {}

  public List<WandBlock> getClipboardMemory() {
    return this.clipboardMemory;
  }

  public Location getClipboardWPlayer() {
    return this.clipboardWPlayer;
  }

  public Location getClipboardWPlayerB() {
    return this.clipboardWPlayerB;
  }

  /**
   * Area Modify
   */

  /**
   * 좌표계를 특정 블록으로 채웁니다.
   * @param blockData 채울 블록
   * @param area 좌표계
   * @param applyPhysics 물리 적용 여부
   */
  public void fill(BlockData blockData, List<Location> area, boolean applyPhysics) {
    World world = area.get(0).getWorld();

    if (world == null) {
      return;
    }

    for (Location pos : area) {
      Block block = world.getBlockAt(pos);
      boolean need2removeBlockData = false;

      if (block.getState() instanceof Container) {
        Container c = (Container) block.getState();
        c.getInventory().setContents(new ItemStack[0]);
        c.update(true, false);
        need2removeBlockData = true;
      }

      if (block.getState() instanceof Campfire) {
        Campfire b = (Campfire) block.getState();
        Msg.info("CAMPFIRE");
        //Msg.warn("CAMPFIRE SIZE: " + b.getSize());
        for (int n = 0; n < 4; n++) {
          b.setItem(n, new ItemStack(Material.AIR));
        }
        b.update(true, false);
        need2removeBlockData = true;
      }

      if (block.getBlockData() instanceof Waterlogged) {
        Waterlogged is = (Waterlogged) block.getBlockData();
        is.isWaterlogged();
        need2removeBlockData = true;
      }

      if (need2removeBlockData) {
        block.setBlockData(Bukkit.createBlockData(Material.AIR), false);
      }

      block.setBlockData(blockData, applyPhysics);
    }
  }

  /**
   * 좌표계를 특정 블록으로 채웁니다.
   * @param material 채울 블록
   * @param area 좌표계
   * @param applyPhysics 물리 적용 여부
   * @param data 채울 블록의 데이터 태그
   */
  public void fill(Material material, List<Location> area, boolean applyPhysics, String data) {
    if (material == null || !material.isBlock() || area == null || area.isEmpty()) {
      return;
    }

    BlockData blockData = Bukkit.createBlockData(material, data);
    fill(blockData, area, applyPhysics);
  }

  /**
   * 좌표계를 특정 블록으로 채웁니다.
   * @param material 채울 블록
   * @param area 좌표계
   * @param applyPhysics 물리 적용 여부
   */
  public void fill(Material material, List<Location> area, boolean applyPhysics) {
    fill(material, area, applyPhysics, "[]");
  }

  /**
   * 좌표계의 블럭들을 제거합니다.
   * @param area 좌표계
   * @param applyPhysics 물리 적용 여부
   */
  public void remove(List<Location> area, boolean applyPhysics) {
    if (area == null || area.isEmpty()) {
      return;
    }

    World world = area.get(0).getWorld();
    BlockData blockData = Bukkit.createBlockData(Material.AIR, "[]");

    if (world == null) {
      return;
    }

    for (Location pos : area) {
      Block block = world.getBlockAt(pos);
      Material type = block.getType();

      if (block.getState() instanceof Container) {
        Container c = (Container) block.getState();
        c.getInventory().setContents(new ItemStack[0]);
      }

      block.setBlockData(blockData, applyPhysics);
    }
  }

  /**
   * 좌표계의 특정 블록을 다른 블록으로 바꿉니다.
   *
   * @param originalBlockData 찾을 블록
   * @param replaceBlockData 바꿀 블록
   * @param area 좌표계
   * @param applyPhysics 물리 적용 여부
   */
  public void replace(BlockData originalBlockData, BlockData replaceBlockData, List<Location> area, boolean applyPhysics) {
    World world = area.get(0).getWorld();

    if (world == null) {
      return;
    }

    for (Location pos : area) {
      Block block = world.getBlockAt(pos);
      if (originalBlockData.getAsString().equals("[]") || originalBlockData.getAsString().equals("")) {
        if (!block.getType().equals(originalBlockData.getMaterial())) {
          continue;
        }
      }
      else {
        if (!block.getBlockData().equals(originalBlockData)) {
          continue;
        }
      }

      if (block.getState() instanceof Container) {
        Container c = (Container) block.getState();
        c.getInventory().setContents(new ItemStack[0]);
      }

      block.setBlockData(replaceBlockData, applyPhysics);
    }
  }

  /**
   * 좌표계의 특정 블럭을 찾습니다.
   *
   * @param area 좌표계
   * @param blockData 찾을 블록
   * @return
   */
  public List<WandBlock> scan(List<Location> area, BlockData blockData) {
    World world = area.get(0).getWorld();

    if (world == null) {
      return null;
    }

    List<WandBlock> scannedBlocks = new ArrayList<>();

    for (Location pos : area) {
      Block block = world.getBlockAt(pos);

      if (block.getType().equals(blockData.getMaterial())) {
        scannedBlocks.add(new WandBlock(block));
      }
    }

    return scannedBlocks;
  }

  /**
   * 좌표계의 블록들을 특정 방향으로 n 회 복제합니다.
   * @param pos1 좌표계 시작 좌표
   * @param pos2 좌표계 끝 좌표
   * @param dir 복제할 방향
   * @param n 복제할 횟수
   * @param applyPhysics 물리 적용 여부
   */
  public void stack(Location pos1, Location pos2, String dir, int n, boolean applyPhysics) {

    if (pos1 == null || pos2 == null || dir == null || n <= 0) {
      return;
    }

    if (n > 1000) {
      return;
    }

    int minX = (int) Math.min(pos1.getX(), pos2.getX());
    int minY = (int) Math.min(pos1.getY(), pos2.getY());
    int minZ = (int) Math.min(pos1.getZ(), pos2.getZ());
    int maxX = (int) Math.max(pos1.getX(), pos2.getX());
    int maxY = (int) Math.max(pos1.getY(), pos2.getY());
    int maxZ = (int) Math.max(pos1.getZ(), pos2.getZ());

    List<WandBlock> area = new ArrayList<>();
    List<Location> locArea = Area.CUBE.getArea(pos1, pos2);
    for (Location loc : locArea) {
      Block block = loc.getBlock();
      WandBlock wBlock = new WandBlock(block);
      wBlock.setLocation(new Location(loc.getWorld(), (int) loc.getX(), (int) loc.getY(), (int) loc.getZ()));
      area.add(wBlock);
    }
    World world = pos1.getWorld();

    if (world == null) {
      return;
    }

    for (int m = 1; m <= n; m++) {
      for (WandBlock wandBlock : area) {
        Location loc = wandBlock.getLocation();
        Location newLoc;
        switch (dir) {
          case "east":
            newLoc = new Location(world, loc.getX() + (m * ((maxX + 1) - minX)), loc.getY(), loc.getZ());
            break;
          case "west":
            newLoc = new Location(world, loc.getX() - (m * ((maxX + 1) - minX)), loc.getY(), loc.getZ());
            break;
          case "south":
            newLoc = new Location(world, loc.getX(), loc.getY(), loc.getZ() + (m * ((maxZ + 1) - minZ)));
            break;
          case "north":
            newLoc = new Location(world, loc.getX(), loc.getY(), loc.getZ() - (m * ((maxZ + 1) - minZ)));
            break;
          case "up":
            newLoc = new Location(world, loc.getX(), loc.getY() + (m * ((maxY + 1) - minY)), loc.getZ());
            break;
          case "down":
            newLoc = new Location(world, loc.getX(), loc.getY() - (m * ((maxY + 1) - minY)), loc.getZ());
            break;
          default:
            return;
        }
        Block block = world.getBlockAt(newLoc);

        if (block.getState() instanceof Container) {
          Container c = (Container) block.getState();
          c.getInventory().setContents(new ItemStack[0]);
        }

        block.setBlockData(wandBlock.getBlockData(), applyPhysics);
        wandBlock.applyBlockState(block);
      }
    }

  }



  private Timer particleTimer = new Timer();
  private List<Location> particleArea;
  private boolean particleAreaShow = false;

  public void setParticleArea(List<Location> area) {
    particleArea = area;
  }

  public void showParticleArea() {
    particleAreaShow = true;
  }

  public void hideParticleArea() {
    particleAreaShow = false;
  }

  public void enableParticleArea() {
    particleTimer.schedule(new TimerTask() {
      public void run() {
        if (particleAreaShow) {
          if (particleArea != null) {
            for (Location loc : particleArea) {
              player.spawnParticle(Particle.REDSTONE, loc, 0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(255, 170, 40), 1));
            }
          }
        }
      }
    }, 0, 100);
  }

  public void disableParticleArea() {
    particleTimer.cancel();
  }




  // Get Wand
  public static Wand getWand(UUID uuid) {
    if (wands.containsKey(uuid)) {
      return wands.get(uuid);
    }
    else {
      return new Wand(uuid);
    }
  }

  public static Wand getWand(Player player) {
    if (wands.containsKey(player.getUniqueId())) {
      return wands.get(player.getUniqueId());
    }
    else {
      return new Wand(player);
    }
  }

  public static List<Wand> getWands() {
    List<Wand> wandsList = new ArrayList<>();
    for (Map.Entry<UUID, Wand> w : wands.entrySet()) {
      wandsList.add(w.getValue());
    }
    return wandsList;
  }

  public static boolean exist(UUID uuid) {
    return wands.containsKey(uuid);
  }







  /*
   * 완드 콘피그
   */

  private static ItemStack wandItem_editPositioner = null;
  private static ItemStack wandItem_brushNormal = null;
  private static ItemStack wandItem_brushApplyPhysics = null;

  public static ItemStack getWandItem(Wand.ItemType itemType) {
    switch (itemType) {
      case EDIT_POSITIONER: {
        ItemStack item = wandItem_editPositioner;
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(Msg.effect("&6&l체리 완드"));
        List<String> lore = new ArrayList<String>();
        lore.add(Msg.effect(""));
        lore.add(Msg.effect("&7블록을 &9&o오른쪽 / 왼쪽 클릭&7하여 &a포지션을 지정&7할 수 있습니다."));
        lore.add(Msg.effect(""));
        lore.add(Msg.effect("&7허공을 &9&o오른쪽 / 왼쪽 클릭&7하여 &c포지션을 지정 해제&7할 수 있습니다."));
        im.setLore(lore);
        item.setItemMeta(im);
        return item;
      }

      case BRUSH_NORMAL: {
        return wandItem_brushNormal;
      }

      case BRUSH_APPLYPHYSICS: {
        return wandItem_brushApplyPhysics;
      }

      default:
        return null;
    }
  }

  public enum ItemType {

    EDIT_POSITIONER, BRUSH_NORMAL, BRUSH_APPLYPHYSICS;

  }


  // 로드
  public static void load() {

    Material material;
    Material mat = Material.getMaterial(Cherry.config.getString("wand.edit.normal-item:"));
    if (mat != null) {
      material = mat;
    }
    else {
      material = Material.SWEET_BERRIES;
    }
    wandItem_editPositioner = new ItemStack(material);

    material = Material.SPECTRAL_ARROW;
    wandItem_brushNormal = new ItemStack(material);

    material = Material.TIPPED_ARROW;
    wandItem_brushApplyPhysics = new ItemStack(material);

    if (Cherry.config.isInt("wand.undo-limit")) {
      undoLimit = Cherry.config.getInt("wand.undo-limit");
      Msg.debug(prefix + "Set undo-limit to " + undoLimit);
    }

  }

  public static void enable() {

    if (!Cherry.config.getBoolean("wand.enable")) {
      Msg.debug(Msg.effect(prefix + "#EB565B;Wand Disabled"));
      return;
    }

    Msg.debug(Msg.effect(prefix + "#A9EB00;Enabling Wand v0.3"));

    Cherry.plugin.registerCommand("wand", new WandCommand(), new WandTabCompleter());

    Cherry.plugin.registerEvent(new WandEvent());

    load();

    Wand.enabled = true;

  }

  public static final String prefix = "#FFCA01;&l[WAND]: &r";

  public static void disable() {
    for ( Wand w : getWands() ) {
      w.disableParticleArea();
    }
  }

}
