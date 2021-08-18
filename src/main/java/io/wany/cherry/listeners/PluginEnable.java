package io.wany.cherry.listeners;

import io.wany.cherry.supports.coreprotect.CoreProtectSupport;
import io.wany.cherry.supports.cucumbery.CucumberySupport;
import io.wany.cherry.supports.vault.VaultSupport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

public class PluginEnable implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public static void onPluginEnable(PluginEnableEvent event) {

    VaultSupport.onPluginEnable(event);
    CucumberySupport.onPluginEnable(event);
    CoreProtectSupport.onPluginEnable(event);

  }

}
