package com.wnynya.cherry.event;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.Tool;
import com.wnynya.cherry.amethyst.Config;
import com.wnynya.cherry.amethyst.CucumberySupport;
import com.wnynya.cherry.player.PlayerMeta;
import com.wnynya.cherry.portal.PortalEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnect implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {

    Player player = event.getPlayer();
    if (!Config.exist("player/" + player.getUniqueId().toString())) {
      //Msg.allP(player.getName() + "님이 서버에 처음 입장했다는 점을 주의 깊게 살펴보십시오.");
      if (CucumberySupport.exist()) {
        CucumberySupport.playerFirstJoin(player);
      }
    }
    PlayerMeta.initPlayerMeta(player);

    if (Cherry.config.getBoolean("event.join")) {
      if (Cherry.config.getBoolean("event.spawnjoin")) {
        Config cherryConfig = new Config("cherry", true);
        Location loc = cherryConfig.getConfig().getLocation("spawn.location");
        if (loc != null) {
          player.teleport(loc);
        }
      }

      String playerFancyName = Tool.getFancyName(player);
      player.setPlayerListName(playerFancyName);
      // 플레이어 입장 문구 설정
      event.setJoinMessage(null);
      if (Cherry.config.getBoolean("logger.player.join.player")) {
        String format = Cherry.config.getString("format.join.player");
        Msg.allP(Tool.n2s(Tool.reFormatEventMsg(event, format)));
      }
      if (Cherry.config.getBoolean("logger.player.join.console")) {
        String format = Cherry.config.getString("format.join.console");
        Msg.allP(Tool.n2s(Tool.reFormatEventMsg(event, format)));
      }
      CucumberySupport.playerFirstJoin(player);
    }

    PortalEvent.playerJoin(event);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    if (Cherry.config.getBoolean("event.quit")) {
      Player player = event.getPlayer();
      String playerFancyName = Tool.getFancyName(player);
      // 플레이어 퇴장 문구 설정
      event.setQuitMessage(null);
      if (Cherry.config.getBoolean("logger.player.quit.player")) {
        String format = Cherry.config.getString("format.quit.player");
        Msg.allP(Tool.reFormatEventMsg(event, format));
      }
      if (Cherry.config.getBoolean("logger.player.quit.console")) {
        String format = Cherry.config.getString("format.quit.console");
        Msg.allP(Tool.reFormatEventMsg(event, format));
      }
    }
  }

}
