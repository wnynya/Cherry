package io.wany.cherry.supports.noteblockapi;

import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NoteBlockAPISongEnd implements Listener {

  @EventHandler
  public void onSongEnd(SongEndEvent event){

    SongPlayer songPlayer = event.getSongPlayer();
    if (songPlayer instanceof PositionSongPlayer positionSongPlayer) {
      NoteBlockAPISupport.positionSongPlayers.remove(positionSongPlayer);
    }

  }

}
