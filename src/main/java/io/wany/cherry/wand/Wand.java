package io.wany.cherry.wand;

import io.wany.cherry.Cherry;
import io.wany.cherry.Config;
import io.wany.cherry.Console;
import io.wany.cherry.Message;
import io.wany.cherry.amethyst.DataTypeChecker;
import io.wany.cherry.wand.area.Area;
import io.wany.cherry.wand.command.WandBrushCommand;
import io.wany.cherry.wand.command.WandEditCommand;
import io.wany.cherry.wand.command.WandEditTabCompleter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Campfire;
import org.bukkit.block.Container;
import org.bukkit.block.data.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wand {

  public static String COLORHEX = "#FF782D";
  public static String COLOR = COLORHEX + ";";
  public static String PREFIX = COLOR + "&l[Wand]:&r ";
  public static boolean ENABLED = false;

  public static Config config = null;
  public static int undoLimit = 100;
  private static final HashMap<UUID, Wand> wands = new HashMap<>();
  private static ItemStack wandItem_editPositioner = null;
  private static ItemStack wandItem_brushNormal = null;
  private static ItemStack wandItem_brushApplyPhysics = null;

  private final Timer particleTimer = new Timer();
  public List<WandBlock> clipboard = null;
  public int lastReplacenearRadius = 5;
  private Player player = null;
  private final UUID uuid;
  private final WandEdit edit;
  private final WandBrush brush;
  private final Stack<List<WandBlock>> undoStack = new Stack<>();
  private final Stack<List<WandBlock>> redoStack = new Stack<>();
  private List<Location> particleArea;
  private boolean particleAreaShow = false;

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
   * 작업을 되돌립니다.
   *
   * @param applyPhysics 물리 적용 여부
   */
  public boolean undo(boolean applyPhysics) {
    if (this.undoStack.size() == 0) {
      return false;
    }
    List<WandBlock> wandBlocks = undoStack.peek();
    List<Location> area = new ArrayList<>();
    for (WandBlock wandBlock : wandBlocks) {
      area.add(wandBlock.getLocation());
    }
    storeRedo(area);
    undoStack.pop();
    for (WandBlock wandBlock : wandBlocks) {
      Block block = wandBlock.getLocation().getBlock();
      wandBlock.apply(block, applyPhysics);
    }
    return true;
  }

  /**
   * 작업의 되돌리기를 취소합니다.
   *
   * @param applyPhysics 물리 적용 여부
   */
  public boolean redo(boolean applyPhysics) {
    if (this.redoStack.size() == 0) {
      return false;
    }
    List<WandBlock> wandBlocks = redoStack.peek();
    List<Location> area = new ArrayList<>();
    for (WandBlock wandBlock : wandBlocks) {
      area.add(wandBlock.getLocation());
    }
    storeUndo(area);
    redoStack.pop();
    for (WandBlock wandBlock : wandBlocks) {
      Block block = wandBlock.getLocation().getBlock();
      wandBlock.apply(block, applyPhysics);
    }
    return true;
  }

  /**
   * 좌표계의 블록들을 되돌릴 수 있도록 저장합니다.
   *
   * @param area         좌표계
   */
  public void storeUndo(List<Location> area) {
    List<WandBlock> wandBlocks = new ArrayList<>();
    for (Location loc : area) {
      Block block = loc.getBlock();
      WandBlock wandBlock = new WandBlock(block);
      wandBlocks.add(wandBlock);
    }
    if (undoStack.size() >= undoLimit) {
      undoStack.remove(0);
    }
    undoStack.push(wandBlocks);
  }

  /**
   * 좌표계의 되돌리기를 취소할 수 있도록 저장합니다.
   *
   * @param area         좌표계
   */
  public void storeRedo(List<Location> area) {
    List<WandBlock> wandBlocks = new ArrayList<>();
    for (Location loc : area) {
      Block block = loc.getBlock();
      WandBlock wandBlock = new WandBlock(block);
      wandBlocks.add(wandBlock);
    }
    if (redoStack.size() >= undoLimit) {
      redoStack.remove(0);
    }
    redoStack.push(wandBlocks);
  }

  /**
   * 좌표계의 블록들을 특정 좌표 기준으로 클립보드에 저장합니다.
   *
   * @param area         좌표계 시작 좌표
   * @param pos          기준 좌표
   * @param blackList    저장하지 않을 블록
   */
  public void copy(List<Location> area, Location pos, List<Material> blackList) {
    List<WandBlock> wandBlocks = new ArrayList<>();
    for (Location loc : area) {
      Block block = loc.getBlock();
      if (blackList != null && blackList.contains(block.getType())) {
        continue;
      }
      WandBlock wandBlock = new WandBlock(block);
      wandBlock.setLocation(new Location(loc.getWorld(), (int) loc.getX() - pos.getX(), (int) loc.getY() - pos.getY(), (int) loc.getZ() - pos.getZ()));
      wandBlocks.add(wandBlock);
    }
    this.clipboard = wandBlocks;
  }

  /**
   * 클립보드의 블록 데이터를 특정 좌표 기준으로 설치합니다.
   *
   * @param pos          기준 좌표
   * @param blackList    저장하지 않을 블록
   * @param applyPhysics 물리 적용 여부
   */
  public void paste(Location pos, List<Material> blackList, boolean applyPhysics) {
    if (pos == null) {
      return;
    }

    for (WandBlock wandBlock : this.clipboard) {
      if (blackList != null && blackList.contains(wandBlock.getBlockData().getMaterial())) {
        continue;
      }
      Location location = new Location(
        pos.getWorld(),
        (int) wandBlock.getLocation().getX() + pos.getX(),
        (int) wandBlock.getLocation().getY() + pos.getY(),
        (int) wandBlock.getLocation().getZ() + pos.getZ()
      );
      Block block = location.getBlock();
      wandBlock.apply(block, applyPhysics);
    }
  }

  /**
   * WIP
   * 클립보드의 블록 데이터의 좌표를 수치만큼 곱합니다.
   *
   * @param x            x 좌표에 곱해질 수치
   * @param y            y 좌표에 곱해질 수치
   * @param z            z 좌표에 곱해질 수치
   */
  public void multiply(int x, int y, int z) {
    if (this.clipboard == null) {
      return;
    }

    for (WandBlock wandBlock : this.clipboard) {
      Location loc = new Location(wandBlock.getLocation().getWorld(), (int) wandBlock.getLocation().getZ() * x, (int) wandBlock.getLocation().getY() * y, (int) wandBlock.getLocation().getX() * z);
      wandBlock.setLocation(loc);
    }

  }

  /**
   * WIP
   * 클립보드의 블록 데이터를 특정 방향으로 회전시킵니다.
   *
   * @param facing       회전시킬 방향
   */
  public void rotate(String facing) {
    if (this.clipboard == null) {
      return;
    }

    List<BlockFace> blockFaces = List.of(
      BlockFace.EAST_NORTH_EAST,
      BlockFace.EAST,
      BlockFace.EAST_SOUTH_EAST,
      BlockFace.SOUTH_EAST,
      BlockFace.SOUTH_SOUTH_EAST,
      BlockFace.SOUTH,
      BlockFace.SOUTH_SOUTH_WEST,
      BlockFace.SOUTH_WEST,
      BlockFace.WEST_SOUTH_WEST,
      BlockFace.WEST,
      BlockFace.WEST_NORTH_WEST,
      BlockFace.NORTH_WEST,
      BlockFace.NORTH_NORTH_WEST,
      BlockFace.NORTH,
      BlockFace.NORTH_NORTH_EAST,
      BlockFace.NORTH_EAST
    );

    switch (facing) {
      case "right" -> {
        multiply(-1, 1, 1);
        for (WandBlock wandBlock : this.clipboard) {
          BlockData blockData = wandBlock.getBlockData();
          Console.log(blockData.toString());
          if (blockData instanceof Directional directional) {
            int facingIndex = blockFaces.indexOf(directional.getFacing());
            facingIndex += 4;
            facingIndex = facingIndex % 16;
            directional.setFacing(blockFaces.get(facingIndex));
          }
          if (blockData instanceof Rotatable rotatable) {
            int facingIndex = blockFaces.indexOf(rotatable.getRotation());
            facingIndex += 4;
            facingIndex = facingIndex % 16;
            rotatable.setRotation(blockFaces.get(facingIndex));
          }
          if (blockData instanceof MultipleFacing multipleFacing) {
            boolean east = multipleFacing.hasFace(BlockFace.EAST);
            boolean south = multipleFacing.hasFace(BlockFace.SOUTH);
            boolean west = multipleFacing.hasFace(BlockFace.WEST);
            boolean north = multipleFacing.hasFace(BlockFace.NORTH);
            multipleFacing.setFace(BlockFace.EAST, north);
            multipleFacing.setFace(BlockFace.SOUTH, east);
            multipleFacing.setFace(BlockFace.WEST, west);
            multipleFacing.setFace(BlockFace.NORTH, south);
          }
          if (blockData instanceof Rail rail) {
            Rail.Shape shape = rail.getShape();
            switch (shape) {
              case ASCENDING_EAST -> rail.setShape(Rail.Shape.ASCENDING_SOUTH);
              case ASCENDING_SOUTH -> rail.setShape(Rail.Shape.ASCENDING_WEST);
              case ASCENDING_WEST -> rail.setShape(Rail.Shape.ASCENDING_NORTH);
              case ASCENDING_NORTH -> rail.setShape(Rail.Shape.ASCENDING_EAST);
              case SOUTH_EAST -> rail.setShape(Rail.Shape.SOUTH_WEST);
              case SOUTH_WEST -> rail.setShape(Rail.Shape.NORTH_WEST);
              case NORTH_WEST -> rail.setShape(Rail.Shape.NORTH_EAST);
              case NORTH_EAST -> rail.setShape(Rail.Shape.SOUTH_EAST);
              case EAST_WEST -> rail.setShape(Rail.Shape.NORTH_SOUTH);
              case NORTH_SOUTH -> rail.setShape(Rail.Shape.EAST_WEST);
            }
          }
          wandBlock.setBlockData(blockData);
        }
      }
      case "left" -> {
        multiply(1, 1, -1);
      }
      case "up" -> {
        for (WandBlock wandBlock : this.clipboard) {
          Location loc = new Location(wandBlock.getLocation().getWorld(), (int) wandBlock.getLocation().getZ(), (int) wandBlock.getLocation().getY() * -1, (int) wandBlock.getLocation().getX());
          wandBlock.setLocation(loc);
        }
      }
    }

  }

  /**
   * WIP
   * 클립보드의 블록 데이터를 특정 방향으로 반전시킵니다.
   *
   * @param facing       반전시킬 방향
   */
  public void flip(String facing) {
    if (this.clipboard == null) {
      return;
    }

    switch (facing) {
      case "right": {
        for (WandBlock wandBlock : this.clipboard) {
          Location loc = new Location(wandBlock.getLocation().getWorld(), (int) wandBlock.getLocation().getZ(), (int) wandBlock.getLocation().getY(), (int) wandBlock.getLocation().getX() * -1);
          wandBlock.setLocation(loc);
        }
        break;
      }
      case "left": {
        for (WandBlock wandBlock : this.clipboard) {
          Location loc = new Location(wandBlock.getLocation().getWorld(), (int) wandBlock.getLocation().getZ(), (int) wandBlock.getLocation().getY(), (int) wandBlock.getLocation().getX());
          wandBlock.setLocation(loc);
        }
        break;
      }
      case "up": {
        for (WandBlock wandBlock : this.clipboard) {
          Location loc = new Location(wandBlock.getLocation().getWorld(), (int) wandBlock.getLocation().getZ(), (int) wandBlock.getLocation().getY() * -1, (int) wandBlock.getLocation().getX());
          wandBlock.setLocation(loc);
        }
        break;
      }
    }

  }

  /**
   * 좌표계를 특정 블록으로 채웁니다.
   *
   * @param blockData    채울 블록
   * @param area         좌표계
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

      if (block.getState() instanceof BlockInventoryHolder) {
        BlockInventoryHolder h = (BlockInventoryHolder) block.getState();
        Inventory i = h.getInventory();
        i.clear();
        need2removeBlockData = true;
      }

      if (block.getState() instanceof Campfire) {
        Campfire b = (Campfire) block.getState();
        //Message.warn("CAMPFIRE SIZE: " + b.getSize());
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
   *
   * @param material     채울 블록
   * @param area         좌표계
   * @param applyPhysics 물리 적용 여부
   * @param data         채울 블록의 데이터 태그
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
   *
   * @param material     채울 블록
   * @param area         좌표계
   * @param applyPhysics 물리 적용 여부
   */
  public void fill(Material material, List<Location> area, boolean applyPhysics) {
    fill(material, area, applyPhysics, "[]");
  }

  /**
   * 좌표계의 블럭들을 제거합니다.
   *
   * @param area         좌표계
   * @param applyPhysics 물리 적용 여부
   */
  public void remove(List<Location> area, boolean applyPhysics) {
    BlockData blockData = Bukkit.createBlockData(Material.AIR, "[]");
    fill(blockData, area, applyPhysics);
  }

  /**
   * 좌표계의 특정 블록을 다른 블록으로 바꿉니다.
   *
   * @param originalBlockData 찾을 블록
   * @param replaceBlockData  바꿀 블록
   * @param area              좌표계
   * @param applyPhysics      물리 적용 여부
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
   * @param area      좌표계
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
   * 좌표계의 블록들을 특정 방향으로 n 회 복제합니다.
   *
   * @param pos1         좌표계 시작 좌표
   * @param pos2         좌표계 끝 좌표
   * @param dir          복제할 방향
   * @param n            복제할 횟수
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
      WandBlock wandBlock = new WandBlock(block);
      wandBlock.setLocation(new Location(loc.getWorld(), (int) loc.getX(), (int) loc.getY(), (int) loc.getZ()));
      area.add(wandBlock);
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

        wandBlock.apply(block, applyPhysics);
      }
    }

  }



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
              player.spawnParticle(Particle.REDSTONE, loc, 0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(255, 120, 45), 1));
            }
          }
        }
      }
    }, 0, 100);
  }

  public void disableParticleArea() {
    particleTimer.cancel();
  }

  public enum ItemType {

    EDIT_POSITIONER, BRUSH_NORMAL, BRUSH_APPLYPHYSICS

  }

  public static void info(CommandSender sender, String message) {
    Message.info(sender, Message.effect(PREFIX + "&r" + message));
  }

  public static void warn(CommandSender sender, String message) {
    Message.info(sender, Message.effect(PREFIX + "&r&e" + message));
  }

  public static void error(CommandSender sender, String message) {
    Message.info(sender, Message.effect(PREFIX + "&r&c" + message));
  }

  public static String blockDataBeauty(BlockData blockData) {
    String msg = "";
    String s = blockData.getAsString();
    Pattern p = Pattern.compile("(.*):(.*)\\[(.*)\\]");
    Matcher m = p.matcher(s);

    if (m.find()) {
      if (m.group(1).equals("minecraft")) {
        msg += "&r&e" + m.group(2) + " &7[";
      }
      else {
        msg += "&r&8" + m.group(1) + ":&e" + m.group(2) + " &7[";
      }
      String data = m.group(3);
      String[] ds = data.split(",");
      Pattern p2 = Pattern.compile("(.*)=(.*)");
      for (String d : ds) {
        Matcher m2 = p2.matcher(d);
        if (m2.find()) {
          msg += "&3" + m2.group(1) + "&7=";
          String v = m2.group(2);
          if (v.equals("true")) {
            msg += "&a" + v;
          }
          else if (v.equals("false")) {
            msg += "&c" + v;
          }
          else if (DataTypeChecker.isInteger(v)) {
            msg += "&6" + v;
          }
          else {
            msg += "&b" + v;
          }
          msg += "&7, ";
        }
      }
      msg = msg.substring(0, msg.length() - 4);
      msg += "&7]";
    }

    return msg;
  }

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

  /*public void replaceData(int x, int y, int z) {
    return;
    if (this.clipboardMemory == null) {
      return;
    }

    for (WandBlock wandBlock : this.clipboardMemory) {
      BlockData blockData = wandBlock.getBlockData();
      if (!(blockData instanceof Directional)) {
        continue;
      }
      Directional directional = (Directional) blockData;
      BlockFace face = directional.getFacing();
      BlockFace newFace;
      switch (face) {
        case NORTH: {
          face = BlockFace.EAST;
          break;
        }
        case EAST: {
          face = BlockFace.SOUTH;
          break;
        }
        case SOUTH: {
          face = BlockFace.WEST;
          break;
        }
        case WEST: {
          face = BlockFace.NORTH;
          break;
        }
        directional.setFacing(face);
      }
      wandBlock.setLocation(loc);
    }
  }*/

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

      if (data == null) {
        data = "";
      }

      Material material;
      try {
        material = Material.valueOf(name.toUpperCase());
      }
      catch (Exception e) {
        throw e;
      }
      if (!material.isBlock()) {
        throw new Exception("not block");
      }

      BlockData blockData;
      try {
        blockData = Bukkit.createBlockData(material, "[" + data + "]");
      }
      catch (Exception e) {
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

  public static ItemStack getWandItem(ItemType itemType) {
    switch (itemType) {
      case EDIT_POSITIONER: {
        ItemStack item = wandItem_editPositioner;
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(Message.effect("&6&l체리 완드"));
        List<String> lore = new ArrayList<String>();
        lore.add(Message.effect(""));
        lore.add(Message.effect("&7블록을 &9&o오른쪽 / 왼쪽 클릭&7하여 &a포지션을 지정&7할 수 있습니다."));
        lore.add(Message.effect(""));
        lore.add(Message.effect("&7허공을 &9&o오른쪽 / 왼쪽 클릭&7하여 &c포지션을 지정 해제&7할 수 있습니다."));
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

  // 로드
  public static void load() {

    Material material;
    Material mat = Material.getMaterial(Cherry.CONFIG.getString("wand.edit.normal-item"));
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

    if (Cherry.CONFIG.isInt("wand.undo-limit")) {
      undoLimit = Cherry.CONFIG.getInt("wand.undo-limit");
      Console.debug(PREFIX + "Set undo-limit to " + undoLimit);
    }

  }

  public static void onEnable() {

    if (!Cherry.CONFIG.getBoolean("wand.enable")) {
      Console.debug(Message.effect(PREFIX + "Wand Disabled"));
      return;
    }
    Console.debug(Message.effect(PREFIX + "Enabling Wand"));
    Wand.ENABLED = true;

    Cherry.PLUGIN.registerCommand("wandedit", new WandEditCommand(), new WandEditTabCompleter());
    Cherry.PLUGIN.registerCommand("wandbrush", new WandBrushCommand(), new WandEditTabCompleter());

    load();

  }

  public static void onDisable() {
    for (Wand w : getWands()) {
      w.disableParticleArea();
    }
  }

}
