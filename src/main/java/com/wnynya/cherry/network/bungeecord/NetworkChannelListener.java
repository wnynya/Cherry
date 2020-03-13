package com.wnynya.cherry.network.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.wnynya.cherry.player.PlayerJoin;
import com.wnynya.cherry.player.PlayerQuit;
import com.wnynya.cherry.portal.Portal;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class NetworkChannelListener implements PluginMessageListener {

  @Override
  public void onPluginMessageReceived(String channel, Player p, byte[] message) {

    if (!channel.equals("cherry:networkchannel")) {
      return;
    }

    ByteArrayDataInput in = ByteStreams.newDataInput(message);
    String subChannel = in.readUTF();

    switch (subChannel) {

      case "NetworkJoin": {
        String playerName = in.readUTF();
        String server = in.readUTF();
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
          PlayerJoin.network(player, server);
        }
        break;
      }

      case "NetworkQuit": {
        String playerName = in.readUTF();
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
          PlayerQuit.network(player);
        }
        break;
      }

      case "SwitchJoin": {
        String playerName = in.readUTF();
        String serverFrom = in.readUTF();
        String serverGoto = in.readUTF();
        PlayerJoin.switchS(playerName, serverFrom, serverGoto);
        break;
      }

      case "SwitchQuit": {
        String playerName = in.readUTF();
        String serverFrom = in.readUTF();
        String serverGoto = in.readUTF();
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
          PlayerQuit.switchS(player, serverFrom, serverGoto);
        }
        break;
      }

      case "ServerData": {
        String serverName = in.readUTF();
        ServerData s = ServerData.getServerData(serverName);
        String isOnline = in.readUTF();
        boolean b = Boolean.valueOf(isOnline);
        if (b) {
          String cps = in.readUTF();
          String mps = in.readUTF();
          int cp = Integer.parseInt(cps);
          int mp = Integer.parseInt(mps);
          s.setOnline(b);
          s.setCurrentPlayers(cp);
          s.setMaxPlayers(mp);
          Portal.updateBungeeSigns(serverName);
        }
        else {
          if (s.isOnline() != b) {
            s.setOnline(b);
            s.setCurrentPlayers(0);
            s.setMaxPlayers(0);
            Portal.updateBungeeSigns(serverName);
          }
        }
        break;
      }

    }

  }

}

