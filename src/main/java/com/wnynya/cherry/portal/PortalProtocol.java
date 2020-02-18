package com.wnynya.cherry.portal;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface PortalProtocol {

  public void use(Player player, Location loc);

  public void use(Player player, String server);

}
