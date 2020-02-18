package com.wnynya.cherry.event;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

  @EventHandler
  public void onChat(AsyncPlayerChatEvent event) {
    if (Cherry.config.getBoolean("event.chat")) {
      event.setFormat(Msg.chatFormatter(event));
    }
  }

}
