package io.wany.cherry.listeners;

import io.wany.cherry.supports.coreprotect.CoreProtectSupport;
import io.wany.cherry.supports.cucumbery.CucumberySupport;
import io.wany.cherry.supports.vault.VaultSupport;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class PluginDisable implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public static void onPluginDisable(PluginDisableEvent event) {

    VaultSupport.onPluginDisable(event);
    CucumberySupport.onPluginDisable(event);
    CoreProtectSupport.onPluginDisable(event);

  }

}
