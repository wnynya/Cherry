package com.wnynya.cherry.event;

import com.wnynya.cherry.player.PlayerChat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
    PlayerChat.chat(event);
  }

}
