package io.wany.cherry.supports.noteblockapi;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import io.wany.cherry.Cherry;
import io.wany.cherry.Console;
import io.wany.cherry.amethyst.Songs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NoteBlockAPISupport {

  public static Plugin PLUGIN;

  public static String COLOR = "#00A1CA;";
  public static String PREFIX = COLOR + "&l[NoteBlockAPI]:&r ";

  public static boolean EXIST = false;

  public static boolean exist() {
    PLUGIN = Bukkit.getPluginManager().getPlugin("NoteBlockAPI");
    if (PLUGIN != null && PLUGIN.isEnabled()) {
      if (!EXIST) {
        EXIST = true;
        Console.debug(PREFIX + "Found NoteBlockAPI v" + PLUGIN.getDescription().getVersion());
      }
      return true;
    }
    else {
      if (EXIST) {
        EXIST = false;
        Console.debug(PREFIX + "ERROR: NoteBlockAPI plugin not exist");
      }
      return false;
    }
  }

  public static final List<PositionSongPlayer> positionSongPlayers = new ArrayList<>();
  public static SongPlayer playPositionSongPlayer(Song song, Location location) {
    if (!exist()) {
      return null;
    }
    PositionSongPlayer psp = getPositionSongPlayer(location);
    if (psp != null) {
      Console.log("contain");
      psp.setPlaying(false, false);
      psp.destroy();
      positionSongPlayers.remove(psp);
    }
    PositionSongPlayer songPlayer = new PositionSongPlayer(song);
    songPlayer.setTargetLocation(location);
    songPlayer.setDistance(3);
    songPlayer.setCategory(SoundCategory.RECORDS);
    for (Player player : Bukkit.getOnlinePlayers()) {
      songPlayer.addPlayer(player);
    }
    positionSongPlayers.add(songPlayer);
    Console.log(positionSongPlayers.hashCode()); // 1
    return songPlayer;
  }
  public static PositionSongPlayer getPositionSongPlayer(Location location) {
    Console.log("gpsp");
    Console.log(positionSongPlayers.hashCode()); // 0
    for (PositionSongPlayer positionSongPlayer : positionSongPlayers) {
      Console.log(positionSongPlayer.getTargetLocation() + "");
      Console.log(location + "");
      if (positionSongPlayer.getTargetLocation().equals(location)) {
        return positionSongPlayer;
      }
    }
    return null;
  }

  public static Song parse(File file) {
    return NBSDecoder.parse(file);
  }

  public static void onEnable() {
    if (!Cherry.CONFIG.getBoolean("noteblockapi-support.enable")) {
      Console.debug(PREFIX + "NoteBlockAPI-Support Disabled");
      return;
    }
    Console.debug(PREFIX + "Enabling NoteBlockAPI-Support");
    if (!exist()) {
      Console.debug(PREFIX + "ERROR: NoteBlockAPI plugin not exist");
      return;
    }

    Songs.onEnable();

    Cherry.PLUGIN.registerEvent(new NoteBlockAPISongEnd());
    Cherry.PLUGIN.registerEvent(new NoteBlockAPISongStopped());
  }

  public static void onDisable() {
    for (PositionSongPlayer positionSongPlayer : positionSongPlayers) {
      positionSongPlayer.setPlaying(false, false);
      positionSongPlayer.destroy();
    }
    Songs.onDisable();
  }

}
