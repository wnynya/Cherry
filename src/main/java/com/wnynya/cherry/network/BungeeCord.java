package com.wnynya.cherry.network;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.network.bungeecord.BungeeCordMsg;
import com.wnynya.cherry.network.bungeecord.NetworkChannelListener;

public class BungeeCord {

  public static void enable() {

    if (Cherry.config.getBoolean("bungeecord.enable")) {
      return;
    }

    // BungeeCord Messaging Channel
    Cherry.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(Cherry.getPlugin(), "BungeeCord");
    Cherry.getPlugin().getServer().getMessenger().registerIncomingPluginChannel(Cherry.getPlugin(), "BungeeCord", new BungeeCordMsg());

    // Cherry Messaging Channel
    Cherry.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(Cherry.getPlugin(), "cherry:networkchannel");
    Cherry.getPlugin().getServer().getMessenger().registerIncomingPluginChannel(Cherry.getPlugin(), "cherry:networkchannel", new NetworkChannelListener());

  }

}
