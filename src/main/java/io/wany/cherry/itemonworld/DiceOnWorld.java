package io.wany.cherry.itemonworld;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.wany.cherry.Cherry;
import io.wany.cherry.Message;
import io.wany.cherry.amethyst.Skull;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class DiceOnWorld {

  private static List<Skull> skulls;
  private static final HashMap<Player, Long> lastUseTimestamp = new HashMap<>();
  private static final List<Block> runningDices = new ArrayList<>();

  private static Skull isDiceSkull(Block block) {
    if (!(block.getState() instanceof org.bukkit.block.Skull skull)) {
      return null;
    }
    PlayerProfile playerProfile = skull.getPlayerProfile();
    if (playerProfile == null) {
      return null;
    }
    List<ProfileProperty> profileProperties = playerProfile.getProperties().stream().toList();
    if (profileProperties.size() <= 0) {
      return null;
    }
    String texture = profileProperties.get(0).getValue();
    List<String> textures = new ArrayList<>();
    for (Skull s : skulls) {
      textures.add(s.toPlayerProfile().getProperties().stream().toList().get(0).getValue());
    }
    if (!textures.contains(texture)) {
      return null;
    }
    int i = textures.indexOf(texture);
    return skulls.get(i);
  }

  private static Skull getRandomDiceSkull() {
    return skulls.get(new Random().nextInt(skulls.size()));
  }

  private static Skull getRandomDiceSkullNotThis(Skull skull) {
    Skull randomSkull = getRandomDiceSkull();
    while (randomSkull.equals(skull)) {
      randomSkull = getRandomDiceSkull();
    }
    return randomSkull;
  }

  public static void onPlayerInteractEvent(PlayerInteractEvent event) {

    Player player = event.getPlayer();
    Block block = event.getClickedBlock();

    if (event.getAction().equals(Action.LEFT_CLICK_BLOCK) && runningDices.contains(block)) {
      event.setCancelled(true);
      return;
    }

    if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
      || player.getGameMode().equals(GameMode.SPECTATOR)
      || player.isSneaking()
      || (lastUseTimestamp.containsKey(player) && lastUseTimestamp.get(player) > System.currentTimeMillis() - 200)
      || block == null
    ) {
      return;
    }

    // Check is Dice
    AtomicReference<Skull> lastSkull = new AtomicReference<>(isDiceSkull(block));
    if (lastSkull.get() == null) {
      return;
    }

    if (new Random().nextInt(100) < 3) {
      block.setType(Material.AIR);
      for (Player p : block.getLocation().getNearbyPlayers(5.0)) {
        p.playSound(block.getLocation(), Sound.ENTITY_SHULKER_OPEN, SoundCategory.MASTER, 1.0f, 1.5f);
        Message.send(p, Message.parse(Message.effect("&e&o이런! &7주사위가 사라졌네요! 너무 세게 던졌나 봐요")));
      }
      return;
    }

    event.setCancelled(true);

    // Run Dice
    runningDices.add(block);
    BlockState blockState = block.getState();
    org.bukkit.block.Skull skull = (org.bukkit.block.Skull) blockState;
    int time = new Random().nextInt(6000) + 1000;
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        Bukkit.getScheduler().runTask(Cherry.PLUGIN, () -> {
          Skull randomSkull = getRandomDiceSkullNotThis(lastSkull.get());
          skull.setPlayerProfile(randomSkull.toPlayerProfile());
          lastSkull.set(randomSkull);
          blockState.update(true, false);
          for (Player p : block.getLocation().getNearbyPlayers(3.0)) {
            p.playSound(block.getLocation(), Sound.ENTITY_CHICKEN_STEP, SoundCategory.MASTER, 1.0f, 1.5f);
          }
        });
      }
    }, 0, 200);
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        timer.cancel();
        runningDices.remove(block);
      }
    }, time);

    if (lastUseTimestamp.containsKey(player)) {
      lastUseTimestamp.replace(player, System.currentTimeMillis());
    }
    else {
      lastUseTimestamp.put(player, System.currentTimeMillis());
    }

  }

  public static void onItemSpawnEvent(ItemSpawnEvent event) {
    Item item = event.getEntity();
    ItemStack itemStack = item.getItemStack();
    if (!(itemStack.getType().equals(Material.PLAYER_HEAD) || itemStack.getType().equals(Material.PLAYER_WALL_HEAD))) {
      return;
    }
    ItemMeta itemMeta = itemStack.getItemMeta();
    SkullMeta skullMeta = (SkullMeta) itemMeta;
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
    for (Skull skull : skulls) {
      textures.add(skull.toPlayerProfile().getProperties().stream().toList().get(0).getValue());
    }
    if (!textures.contains(texture)) {
      return;
    }
    if (runningDices.contains(item.getLocation().getBlock())) {
      event.setCancelled(true);
      return;
    }
    int index = textures.indexOf(texture);
    Component diceName = Message.parse(Message.effect("&f주사위 " + (index + 1)));
    Component displayName = itemMeta.displayName();
    if (displayName != null && displayName.equals(diceName)) {
      return;
    }
    event.setCancelled(true);
    ItemStack drop = new ItemStack(Material.PLAYER_HEAD);
    ItemMeta dropMeta = drop.getItemMeta();
    SkullMeta dropSkullMeta = (SkullMeta) dropMeta;
    dropSkullMeta.setPlayerProfile(skulls.get(index).toPlayerProfile());
    dropMeta.displayName(diceName);
    drop.setItemMeta(dropMeta);
    event.getLocation().getWorld().dropItem(event.getLocation(), drop);
  }

  public static void onEnable() {

    skulls = List.of(
      new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWNmYWRhZWM3NzZlZWRlNDlhNDMzZTA5OTdmZTA0NDJmNzY1OTk4OTI0MTliYWFiODNkMjhlOGJhN2YxZjMifX19"),
      new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTAzNGYwYmUzNTYxN2ZmMGM3ZDc2MjkyMTY1Zjk3Mzc1ZmRhNDAzM2JjODJkMmVlNjE2NGM2ODc2OTI3ZjM4YyJ9fX0="),
      new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzgzMTEzOGMyMDYxMWQzMDJjNDIzZmEzMjM3MWE3NDNkMTc0MzdhMTg5NzNjMzUxOTczNDQ3MGE3YWJiNCJ9fX0="),
      new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQyMDY0YWE0ZGUzNWY2NjliMDRlODdiMWUwNGQ0MmVjZWU4MjliOWJjNTY2MTM4YWEyNzk0Nzk4YWZiZWMifX19"),
      new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTFlMTJhZDk3NTlhM2QzNjhlNWQ5Njk2ZWQxMjRmNzMzNDA2YzRmNzE2MmJhYzRmYTM4YTk4MjE4YjdkN2M2In19fQ=="),
      new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZlMjY3MWU3ZTFhNzUzODI3Y2M0ZTMyZTUwNmYyOWFhMjM4ZTQ5NmFjNWE1NTcyNmFjNWRjZGM5ZDMifX19")
    );

  }

}
