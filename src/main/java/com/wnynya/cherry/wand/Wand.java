package com.wnynya.cherry.wand;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Config;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.Tool;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cherry Wand
 */

public class Wand {

  public static boolean enabled = false;

  private static HashMap<UUID, Wand> wands = new HashMap<>();

  public static Config config = null;

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

  public static int undoLimit = 100;

  public boolean undo(boolean applyPhysics) {
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
      block.setBlockData(blockData, applyPhysics);
      wBlock.applyBlockState(block);
    }
    return true;
  }

  public boolean redo(boolean applyPhysics) {
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
      block.setBlockData(blockData, applyPhysics);
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

  public List<WandBlock> clipboardMemory = null;

  public void copy(List<Location> area, Location posP, List<Material> blackList) {
    List<WandBlock> wandBlocks = new ArrayList<>();
    for (Location loc : area) {
      Block block = loc.getBlock();
      if (blackList != null && blackList.contains(block.getType())) {
        continue;
      }
      WandBlock wandBlock = new WandBlock(block);
      wandBlock.setLocation(new Location(
        loc.getWorld(),
        (int) loc.getX() - posP.getX(),
        (int) loc.getY() - posP.getY(),
        (int) loc.getZ() - posP.getZ()
      ));
      wandBlocks.add(wandBlock);
    }
    this.clipboardMemory = wandBlocks;

    for (WandBlock wandBlock : this.clipboardMemory) {
      Msg.info(Tool.loc2Str2(wandBlock.getLocation()) + " " + wandBlock.getBlockData().getMaterial().toString());
    }
  }

  public void paste(Location posP, List<Material> blackList, boolean applyPhysics) {
    if (posP == null) {
      return;
    }

    for (WandBlock wandBlock : this.clipboardMemory) {
      if (blackList != null && blackList.contains(wandBlock.getBlockData().getMaterial())) {
        continue;
      }
      Location loc = new Location(
        posP.getWorld(),
        (int) wandBlock.getLocation().getX() + posP.getX(),
        (int) wandBlock.getLocation().getY() + posP.getY(),
        (int) wandBlock.getLocation().getZ() + posP.getZ()
        );
      Block block = loc.getBlock();
      block.setBlockData(wandBlock.getBlockData(), applyPhysics);
      try {
        wandBlock.applyBlockState(block);
      }
      catch (Exception ignored) {}
    }
  }

  public void multiply(int x, int y, int z) {
    if (this.clipboardMemory == null) {
      return;
    }

    for (WandBlock wandBlock : this.clipboardMemory) {
      Location loc = new Location(
        wandBlock.getLocation().getWorld(),
        (int) wandBlock.getLocation().getZ() * x,
        (int) wandBlock.getLocation().getY() * y,
        (int) wandBlock.getLocation().getX() * z
      );
      wandBlock.setLocation(loc);
    }

  }

  public void rotate(String facing) {
    if (this.clipboardMemory == null) {
      return;
    }

    switch (facing) {
      case "right": {
        for (WandBlock wandBlock : this.clipboardMemory) {
          Location loc = new Location(
            wandBlock.getLocation().getWorld(),
            (int) wandBlock.getLocation().getZ() * -1,
            (int) wandBlock.getLocation().getY(),
            (int) wandBlock.getLocation().getX()
          );
          wandBlock.setLocation(loc);
        }
        break;
      }
      case "left": {
        for (WandBlock wandBlock : this.clipboardMemory) {
          Location loc = new Location(
            wandBlock.getLocation().getWorld(),
            (int) wandBlock.getLocation().getZ(),
            (int) wandBlock.getLocation().getY(),
            (int) wandBlock.getLocation().getX() * -1
          );
          wandBlock.setLocation(loc);
        }
        break;
      }
      case "up": {
        for (WandBlock wandBlock : this.clipboardMemory) {
          Location loc = new Location(
            wandBlock.getLocation().getWorld(),
            (int) wandBlock.getLocation().getZ(),
            (int) wandBlock.getLocation().getY() * -1,
            (int) wandBlock.getLocation().getX()
          );
          wandBlock.setLocation(loc);
        }
        break;
      }
    }

  }

  public void flip(String facing) {
    if (this.clipboardMemory == null) {
      return;
    }

    switch (facing) {
      case "right": {
        for (WandBlock wandBlock : this.clipboardMemory) {
          Location loc = new Location(
            wandBlock.getLocation().getWorld(),
            (int) wandBlock.getLocation().getZ(),
            (int) wandBlock.getLocation().getY(),
            (int) wandBlock.getLocation().getX() * -1
          );
          wandBlock.setLocation(loc);
        }
        break;
      }
      case "left": {
        for (WandBlock wandBlock : this.clipboardMemory) {
          Location loc = new Location(
            wandBlock.getLocation().getWorld(),
            (int) wandBlock.getLocation().getZ(),
            (int) wandBlock.getLocation().getY(),
            (int) wandBlock.getLocation().getX()
          );
          wandBlock.setLocation(loc);
        }
        break;
      }
      case "up": {
        for (WandBlock wandBlock : this.clipboardMemory) {
          Location loc = new Location(
            wandBlock.getLocation().getWorld(),
            (int) wandBlock.getLocation().getZ(),
            (int) wandBlock.getLocation().getY() * -1,
            (int) wandBlock.getLocation().getX()
          );
          wandBlock.setLocation(loc);
        }
        break;
      }
    }

  }

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
   * 좌표계의 특정 블록을 찾습니다.
   *
   * @param area 좌표계
   * @param blockData 찾을 블록
   * @param matchData 블록 데이터 확인 여부
   * @return Location 리스트를 반환합니다.
   */
  public List<Location> scan(List<Location> area, BlockData blockData, boolean matchData) {
    World world = area.get(0).getWorld();

    if (world == null) {
      return null;
    }

    List<Location> scannedBlocks = new ArrayList<>();

    for (Location pos : area) {
      Block block = world.getBlockAt(pos);

      if (matchData) {
        if (block.getBlockData().equals(blockData)) {
          scannedBlocks.add(pos);
        }
      }
      else {
        if (block.getType().equals(blockData.getMaterial())) {
          scannedBlocks.add(pos);
        }
      }
    }

    return scannedBlocks;
  }

  /**
   * 블록 목록에서 특정 블록을 찾습니다.
   *
   * @param area 좌표계
   * @param blockData 찾을 블록
   * @param matchData 블록 데이터 확인 여부
   * @return WandBlock 리스트를 반환합니다.
   */
  public List<WandBlock> scanw(List<WandBlock> area, BlockData blockData, boolean matchData) {

    List<WandBlock> scannedBlocks = new ArrayList<>();

    for (WandBlock wb : area) {
      if (matchData) {
        if (wb.getBlockData().equals(blockData)) {
          scannedBlocks.add(wb);
        }
      }
      else {
        if (wb.getBlockData().getMaterial().equals(blockData.getMaterial())) {
          scannedBlocks.add(wb);
        }
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



  private final Timer particleTimer = new Timer();
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

  /**
   * ETC Value
   */

  public int lastReplacenearRadius = 5;

  /**
   * Get Wand
   */
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



  public static BlockData getBlockData(String string) throws Exception {

    Pattern pattern = Pattern.compile("([a-zA-Z0-9_]+)(\\[(.*)\\])?");

    Matcher matcher = pattern.matcher(string);
    if (matcher.find()) {
      String name = matcher.group(1);
      String data = matcher.group(3);

      if (data == null) { data = ""; }

      Material material;
      try {
        material = Material.valueOf(name.toUpperCase());
      } catch (Exception e) {
        throw e;
      }
      if (!material.isBlock()) {
        throw new Exception("not block");
      }

      BlockData blockData;
      try {
        blockData = Bukkit.createBlockData(material, "[" + data + "]");
      } catch (Exception e) {
        throw e;
      }
      return blockData;
    }

    throw new Exception("string parsing error");

  }

  public static String getDataString(String string) {

    Pattern pattern = Pattern.compile("([a-zA-Z0-9_]+)(\\[(.*)\\])?");

    Matcher matcher = pattern.matcher(string);
    if (matcher.find()) {
      if (matcher.group(2) != null) {
        return matcher.group(2);
      }
    }

    return "";

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

  public static class Message {

    public static String PREFIX;

    public static class Error {
      public static String PERMISSION;
      public static String ARGS;
      public static String USAGE;
      public static String PLAYER_ONLY;
      public static String BLOCKDATA_PARSE;
    }

  }

  // 로드
  public static void load() {

    Wand.config = new Config("wand");

    Message.PREFIX = Wand.config.initString("message.prefix", "[WAND]");
    Message.Error.PERMISSION = Wand.config.initString("message.error.permission", "명령어를 사용할 권한이 없습니다.");
    Message.Error.ARGS = Wand.config.initString("message.error.args", "명령어의 구성 요소가 부족합니다.");
    Message.Error.USAGE = Wand.config.initString("message.error.usage", "사용 방법: ");
    Message.Error.PLAYER_ONLY = Wand.config.initString("message.error.player_only", "플레이어만 사용 가능한 명령어입니다.");
    Message.Error.BLOCKDATA_PARSE = Wand.config.initString("message.error.blockdata_parse", "블록 데이터 파싱 중 에러가 발생하였습니다. {message}");
    Wand.config.initString("message.pos1", "첫번째 포지션");
    Wand.config.initString("message.pos2", "두번째 포지션");
    Wand.config.initString("message.pos3", "세번째 포지션");
    Wand.config.initString("message.pos_set", "{position_name:을:를} {location_xyz} 로 설정하였습니다.");
    Wand.config.initString("message.area", "지정된 영역");
    Wand.config.initString("message.fill", "{blockname:을:를} {count} 개 설치하였습니다.");
    Wand.config.initString("message.remove", "{count} 개의 블럭을 제거하였습니다.");
    Wand.config.initString("message.replace", "[WAND]");

    Material material;
    Material mat = Material.getMaterial(Cherry.config.getString("wand.edit.normal-item"));
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
