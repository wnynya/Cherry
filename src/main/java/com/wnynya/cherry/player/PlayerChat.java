package com.wnynya.cherry.player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.amethyst.Color;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat {

  public static void chat(AsyncPlayerChatEvent event) {
    if (event.isCancelled()) {
      return;
    }

    if (Cherry.config.getBoolean("event.chat.msg.normal.chat.enable")) {
      event.setCancelled(true);
      Msg.allP(Msg.chatFormatter(event, Cherry.config.getString("event.chat.msg.normal.chat.format")));
    }
    if (Cherry.config.getBoolean("event.chat.msg.normal.console.enable")) {
      event.setCancelled(true);
      Msg.console(Color.mfc2ansi(Msg.chatFormatter(event, Cherry.config.getString("event.chat.msg.normal.console.format"))));
    }
    channelSend(event);
  }

  public static void channelSend(AsyncPlayerChatEvent event) {
    String channel = Cherry.config.getString("event.chat.msg.bungeecord.channel");
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("ChannelChat");
    out.writeUTF(channel);
    out.writeUTF(event.getPlayer().getName());
    out.writeUTF(event.getMessage());
    event.getPlayer().sendPluginMessage(Cherry.plugin, "cherry:networkchannel", out.toByteArray());
  }

  public static void channelReceive(String msg, String playerName, String fromServer) {
    if (Cherry.config.getBoolean("event.chat.msg.bungeecord.chat.enable")) {
      Msg.allP(Msg.channelChatFormatter(msg, playerName, fromServer, Cherry.config.getString("event.chat.msg.bungeecord.chat.format")));
    }
    if (Cherry.config.getBoolean("event.chat.msg.bungeecord.console.enable")) {
      Msg.console(Color.mfc2ansi(Msg.channelChatFormatter(msg, playerName, fromServer, Cherry.config.getString("event.chat.msg.bungeecord.console.format"))));
    }
  }

}
