package com.wnynya.cherry.event;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeCordMsg implements PluginMessageListener {

  @Override
  public void onPluginMessageReceived(String channel, Player player, byte[] message) {
    if (!channel.equals("BungeeCord")) {
      return;
    }
    ByteArrayDataInput in = ByteStreams.newDataInput(message);
    String subchannel = in.readUTF();
    if (subchannel.equals("SomeSubChannel")) {
      // Use the code sample in the 'Response' sections below to read
      // the data.
    }
  }

}
