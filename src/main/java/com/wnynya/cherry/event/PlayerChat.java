package com.wnynya.cherry.event;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.network.terminal.WebSocket;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

  @EventHandler
  public void onChat(AsyncPlayerChatEvent event) {
    if (Cherry.config.getBoolean("event.chat.setFormat.enable")) {
      event.setFormat(Msg.chatFormatter(event));
    }
    if (Cherry.config.getBoolean("websocket.enable") && Cherry.config.getBoolean("event.chat.websocket") && WebSocket.isConnected) {
      //WebSocket.Message.chat(event.getPlayer(), event.getMessage());
    }
  }

}
