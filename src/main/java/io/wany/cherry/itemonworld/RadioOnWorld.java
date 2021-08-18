package io.wany.cherry.itemonworld;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import io.wany.cherry.Console;
import io.wany.cherry.Message;
import io.wany.cherry.amethyst.Skull;
import io.wany.cherry.amethyst.Songs;
import io.wany.cherry.supports.noteblockapi.NoteBlockAPISupport;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.util.*;

public class RadioOnWorld {

  private static List<Skull> skulls;
  private static final HashMap<Player, Long> lastUseTimestamp = new HashMap<>();

  private static boolean isRadioSkull(Block block) {
    if (!(block.getState() instanceof org.bukkit.block.Skull skull)) {
      return false;
    }
    PlayerProfile playerProfile = skull.getPlayerProfile();
    if (playerProfile == null) {
      return false;
    }
    List<ProfileProperty> profileProperties = playerProfile.getProperties().stream().toList();
    if (profileProperties.size() <= 0) {
      return false;
    }
    String texture = profileProperties.get(0).getValue();
    List<String> textures = new ArrayList<>();
    for (Skull s : skulls) {
      textures.add(s.toPlayerProfile().getProperties().stream().toList().get(0).getValue());
    }
    return textures.contains(texture);
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

    if (!NoteBlockAPISupport.exist()) {
      return;
    }

    Player player = event.getPlayer();
    Block block = event.getClickedBlock();

    if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
      || player.getGameMode().equals(GameMode.SPECTATOR)
      || player.isSneaking()
      || (lastUseTimestamp.containsKey(player) && lastUseTimestamp.get(player) > System.currentTimeMillis() - 200)
      || block == null
    ) {
      return;
    }

    Location location = block.getLocation();

    // Check is Radio
    if (!isRadioSkull(block)) {
      return;
    }

    if (new Random().nextInt(100) < 3) {
      block.setType(Material.AIR);
      if (new Random().nextInt(100) < 3) {
        for (Player p : block.getLocation().getNearbyPlayers(5.0)) {
          block.getLocation().createExplosion(player, 1);
          p.playSound(block.getLocation(), Sound.ENTITY_SHULKER_OPEN, SoundCategory.MASTER, 1.0f, 1.5f);
          Message.send(p, Message.parse(Message.effect("&e&o이런! &7라디오가 터져버렸어요! 너무 세게 던졌나 봐요")));
        }
      }
      else {
        for (Player p : block.getLocation().getNearbyPlayers(5.0)) {
          p.playSound(block.getLocation(), Sound.ENTITY_SHULKER_OPEN, SoundCategory.MASTER, 1.0f, 1.5f);
          Message.send(p, Message.parse(Message.effect("&e&o이런! &7라디오가 망가졌어요! 너무 세게 던졌나 봐요")));
        }
      }
      return;
    }

    event.setCancelled(true);

    // Play Radio
    if (NoteBlockAPISupport.getPositionSongPlayer(location) == null) {
      String songName = Songs.getRandomSongName();
      File songFile = null;
      try {
        songFile = Songs.download(songName);
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (songFile == null) {
        return;
      }
      Console.log("play radio " + songName);
      Song song = NoteBlockAPISupport.parse(songFile);
      SongPlayer songPlayer = NoteBlockAPISupport.playPositionSongPlayer(song, location);
      if (songPlayer == null) {
        return;
      }
      songPlayer.setPlaying(true, true);
    }
    // Stop Radio
    else {
      Console.log("stop radio ");
      PositionSongPlayer songPlayer = NoteBlockAPISupport.getPositionSongPlayer(location);
      songPlayer.setPlaying(false, true);
      songPlayer.destroy();
      NoteBlockAPISupport.positionSongPlayers.remove(location);
    }

    if (lastUseTimestamp.containsKey(player)) {
      lastUseTimestamp.replace(player, System.currentTimeMillis());
    }
    else {
      lastUseTimestamp.put(player, System.currentTimeMillis());
    }

  }

  public static void onItemSpawnEvent(ItemSpawnEvent event) {

    if (!NoteBlockAPISupport.exist()) {
      return;
    }

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
    if (NoteBlockAPISupport.getPositionSongPlayer(item.getLocation()) != null) {
      SongPlayer songPlayer = NoteBlockAPISupport.getPositionSongPlayer(item.getLocation());
      songPlayer.setPlaying(false, true);
      NoteBlockAPISupport.positionSongPlayers.remove(item.getLocation());
    }
    int index = textures.indexOf(texture);
    Component radioName = Message.parse(Message.effect("&f라디오"));
    Component displayName = itemMeta.displayName();
    if (displayName != null && displayName.equals(radioName)) {
      return;
    }
    event.setCancelled(true);
    ItemStack drop = new ItemStack(Material.PLAYER_HEAD);
    ItemMeta dropMeta = drop.getItemMeta();
    SkullMeta dropSkullMeta = (SkullMeta) dropMeta;
    dropSkullMeta.setPlayerProfile(skulls.get(index).toPlayerProfile());
    dropMeta.displayName(radioName);
    drop.setItemMeta(dropMeta);
    event.getLocation().getWorld().dropItem(event.getLocation(), drop);
  }

  public static void onEnable() {

    if (!NoteBlockAPISupport.exist()) {
      return;
    }

    skulls = List.of(
      new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQ4YThjNTU4OTFkZWM3Njc2NDQ0OWY1N2JhNjc3YmUzZWU4OGEwNjkyMWNhOTNiNmNjN2M5NjExYTdhZiJ9fX0="),
      new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMGEyODAxY2QwZGZiYzUyOTZjM2JmNjE0MWM0NzdjZmNmNzNjZTg3Zjc4NWIwOWExNzQxNWNkOGUzNjE5MSJ9fX0="),
      new Skull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODQ1ZjQ3OTIzY2E4ZDc1N2FhNTU1ZTE1YjMyOTYxMjEwZjRkNDAyZDgyNzgzMTgxYmIxMTM4YTI2ZDE0MGRkYyJ9fX0=")
    );

  }

}
