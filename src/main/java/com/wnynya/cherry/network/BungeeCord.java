package com.wnynya.cherry.network;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.network.bungeecord.NetworkChannelListener;

public class BungeeCord {

  public static void enable() {

    if (Cherry.config.getBoolean("bungeecord.enable")) {
      return;
    }

    // BungeeCord Messaging Channel
    Cherry.plugin.getServer().getMessenger().registerOutgoingPluginChannel(Cherry.plugin, "BungeeCord");

    // Cherry Messaging Channel
    Cherry.plugin.getServer().getMessenger().registerOutgoingPluginChannel(Cherry.plugin, "cherry:networkchannel");
    Cherry.plugin.getServer().getMessenger().registerIncomingPluginChannel(Cherry.plugin, "cherry:networkchannel", new NetworkChannelListener());

  }

}
