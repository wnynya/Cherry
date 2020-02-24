package com.wnynya.cherry.player;

import com.wnynya.cherry.Cherry;
import com.wnynya.cherry.Msg;
import com.wnynya.cherry.Tool;
import com.wnynya.cherry.amethyst.Config;
import com.wnynya.cherry.amethyst.CucumberySupport;
import com.wnynya.cherry.portal.PortalEvent;
import com.wnynya.cherry.wand.Wand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin {

  public static void server(PlayerJoinEvent event) {

    Player player = event.getPlayer();

    PlayerMeta.initPlayerMeta(player);

    // 플레이어 입장시 스폰으로 이동
    if (Cherry.config.getBoolean("event.join.moveToSpawn.enable")) {
      if (Config.exist("cherry")) {
        Location loc = new Config("cherry", false).getConfig().getLocation("spawn.location");
        if (loc != null) {
          player.teleport(loc);
        }
      }
    }

    // 탭리스트 이름 변경
    if (Cherry.config.getBoolean("event.join.setTabList.enable")) {
      String format = Cherry.config.getString("event.join.setTabList.format");
      player.setPlayerListName(Msg.playerFormatter(event.getPlayer(), format));
    }

    // 입장 메시지 (플레이어 채팅)
    if (Cherry.config.getBoolean("event.join.setMessage.playerChat.enable")) {
      event.setJoinMessage(null);
      String format = Cherry.config.getString("event.join.setMessage.playerChat.format");
      Msg.allP(Msg.playerFormatter(event.getPlayer(), format));
    }

    // 입장 메시지 (플레이어 액션바)
    if (Cherry.config.getBoolean("event.join.setMessage.playerActionbar.enable")) {
      event.setJoinMessage(null);
      String format = Cherry.config.getString("event.join.setMessage.playerActionbar.format");
      for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
        Msg.actionBar(p, Msg.playerFormatter(event.getPlayer(), format));
      }
    }

    // 입장 메시지 (콘솔)
    if (Cherry.config.getBoolean("event.join.setMessage.console.enable")) {
      event.setJoinMessage(null);
      String format = Cherry.config.getString("event.join.setMessage.console.format");
      Msg.allP(Msg.playerFormatter(event.getPlayer(), format));
    }

    Advancement advancement = Bukkit.getAdvancement(new NamespacedKey(Cherry.getPlugin(), "wanyfield/root"));
    if (advancement != null) {
      player.getAdvancementProgress(advancement).awardCriteria("impossible");
    }

    if (Wand.exist(player.getUniqueId())) {
      Wand.getWand(player).setPlayer(player);
    }

    PortalEvent.playerJoin(event);

    CucumberySupport.playerFirstJoin(player);

  }


  public static void serverFirst(Player player, PlayerJoinEvent event) {

    // Cucumbery Support
    if (CucumberySupport.exist()) {
      CucumberySupport.playerFirstJoin(player);
    }

  }

}
