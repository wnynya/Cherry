package com.wnynya.cherry.event;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.terminal.WebSocketClient;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

  @EventHandler
  public void onChat(AsyncPlayerChatEvent event) {
    if (Cherry.config.getBoolean("event.chat.setFormat.enable")) {
      event.setFormat(Msg.chatFormatter(event));
    }
    if (Cherry.config.getBoolean("event.chat.websocket") &&
      Cherry.config.getBoolean("websocket.enable") && WebSocketClient.isConnected ) {
      WebSocketClient.Message.chat(event.getPlayer(), event.getMessage());
    }
  }

}
