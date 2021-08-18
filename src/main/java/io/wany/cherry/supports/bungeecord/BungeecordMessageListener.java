package io.wany.cherry.supports.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.wany.cherry.listeners.PlayerJoin;
import io.wany.cherry.listeners.PlayerQuit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public class BungeecordMessageListener implements PluginMessageListener {

  @Override
  public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {

    if (!channel.equals(BungeecordSupport.CHANNEL)) {
      return;
    }

    ByteArrayDataInput in;
    String event;

    try {
      in = ByteStreams.newDataInput(message);
      event = in.readUTF();
    }
    catch (Exception e) {
      return;
    }

    switch (event) {
      case "ServerChangeJoin": {
        String name = in.readUTF();
        String from = in.readUTF();
        String to = in.readUTF();
        PlayerJoin.changeJoinPlayers.put(name, from);
        break;
      }
      case "ServerChangeQuit": {
        String name = in.readUTF();
        String from = in.readUTF();
        String to = in.readUTF();
        PlayerQuit.changeQuitPlayers.put(name, to);
        break;
      }
    }

  }

}
