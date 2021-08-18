package io.wany.cherry.supports.noteblockapi;

import com.xxmicloxx.NoteBlockAPI.event.SongStoppedEvent;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NoteBlockAPISongStopped implements Listener {

  @EventHandler
  public void onSongStopped(SongStoppedEvent event){

    SongPlayer songPlayer = event.getSongPlayer();
    if (songPlayer instanceof PositionSongPlayer positionSongPlayer) {
      NoteBlockAPISupport.positionSongPlayers.remove(positionSongPlayer);
    }

  }

}
