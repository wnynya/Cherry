package io.wany.cherry.commands;

import com.jho5245.cucumbery.util.storage.ComponentUtil;
import io.wany.cherry.supports.vault.VaultChat;
import io.wany.cherry.supports.vault.VaultSupport;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DropCommand implements CommandExecutor {

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

    if (!(sender instanceof Player player)) {
      return true;
    }

    player.dropItem(args.length > 0 && args[0].equals("true"));
    player.updateInventory();

    return true;

  }

}
