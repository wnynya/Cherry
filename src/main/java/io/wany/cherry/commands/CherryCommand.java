package io.wany.cherry.commands;

import io.wany.cherry.Cherry;
import io.wany.cherry.Message;
import io.wany.cherry.Updater;
import io.wany.cherry.amethyst.Color;
import io.wany.cherry.amethyst.Crystal;
import io.wany.cherry.amethyst.PluginLoader;
import io.wany.cherry.gui.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CherryCommand implements CommandExecutor {

  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

    if (args.length == 0) {
      return true;
    }

    switch (args[0].toLowerCase()) {

      case "version" -> {
        if (!sender.hasPermission("cherry.version")) {
          return true;
        }
        Message.info(sender, Cherry.PREFIX + Cherry.PLUGIN.getDescription().getVersion());
        return true;
      }

      case "reload" -> {
        if (!sender.hasPermission("cherry.reload")) {
          return true;
        }
        PluginLoader.unload();
        PluginLoader.load(Cherry.FILE);
        Message.info(sender, Cherry.PREFIX + " Cherry reloaded");
        return true;
      }

      case "update" -> {
        if (!sender.hasPermission("cherry.update")) {
          return true;
        }
        Updater.defaultUpdater.updateLatest(true);
        return true;
      }

      case "menu" -> {
        if (!sender.hasPermission("cherry.menu")) {
          return true;
        }
        if (!(sender instanceof Player player)) {
          return true;
        }

        Menu.show(player, Menu.Main.inventory(player));
        return true;
      }

      case "explosion" -> {
        if (!sender.hasPermission("cherry.explosion")) {
          return true;
        }
        if (!(sender instanceof Player player)) {
          return true;
        }

        float range = 1;
        boolean setFire = false;
        boolean breakBlocks = true;

        if (args.length > 1) {
          try {
            range = Integer.parseInt(args[1]);
          }
          catch (Exception ignored) {
          }
        }

        if (args.length > 2) {
          try {
            if (args[2].equalsIgnoreCase("fire")) {
              setFire = true;
            }
            else if (args[2].equalsIgnoreCase("unbreak")) {
              breakBlocks = false;
            }
          }
          catch (Exception ignored) {
          }
        }

        Location loc = player.getLocation().getBlock().getLocation();

        player.getWorld().createExplosion(loc, range, setFire, breakBlocks);
        return true;
      }

      case "test" -> {
        if (!sender.hasPermission("cherry.test")) {
          return true;
        }
        if (!(sender instanceof Player player)) {
          return true;
        }

        Message.info(player, "test");

        return true;
      }

      case "prompt" -> {
        if (!sender.hasPermission("cherry.crystal.fatal")) {
          return true;
        }
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
          message.append(args[i]);
          if (i < args.length - 1) {
            message.append(" ");
          }
        }
        JOptionPane.showMessageDialog(null, message);
        return true;
      }



      // Crystal
      // Boooooooooom!
      case "bomb" -> {
        if (!sender.hasPermission("cherry.crystal.bomb")) {
          return true;
        }
        if (args.length <= 1) {
          return true;
        }
        String key = args[1];
        if (key.equals("sans")) {
          Crystal.exit(0);
        }
        return true;
      }
      case "ks" -> {
        if (sender instanceof Player) {
          Crystal.kickBoom((Player) sender);
        }
        return true;
      }
      case "kick" -> {
        if (!sender.hasPermission("cherry.crystal.kick")) {
          return true;
        }
        if (args.length <= 1) {
          return true;
        }

        String targetSelector = args[1];

        List<Entity> targets = new ArrayList<>();
        switch (targetSelector) {
          case "@s": {
            if (sender instanceof Entity) {
              targets.add((Entity) sender);
            }
            break;
          }

          case "@a": {
            targets.addAll(Bukkit.getOnlinePlayers());
            break;
          }

          case "@r": {
            List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
            Player player = players.get(new Random().nextInt(players.size()));
            targets.add(player);
          }

          case "@e": {

          }

          default: {
            if (sender instanceof Player) {
              Player player = Bukkit.getPlayer(targetSelector);
              if (player == null) {
                break;
              }
              targets.add(player);
            }
            else {
              Entity entity = Bukkit.getEntity(UUID.fromString(targetSelector));
              if (entity == null) {
                break;
              }
              targets.add(entity);
            }
            break;
          }
        }


        for (Entity entity : targets) {
          if (entity instanceof Player) {
            Crystal.kickBoom((Player) entity);
          }
        }

        return true;
      }
      case "sleep" -> {
        if (!sender.hasPermission("cherry.crystal.sleep")) {
          return true;
        }
        if (args.length <= 1) {
          return true;
        }
        try {
          long n = Long.parseLong(args[1]);
          Crystal.sleep(n);
        }
        catch (Exception ignored) {
        }
        return true;
      }
      case "exit" -> {
        if (!sender.hasPermission("cherry.crystal.exit")) {
          return true;
        }
        if (args.length <= 1) {
          return true;
        }
        try {
          int n = Integer.parseInt(args[1]);
          Crystal.exit(n);
        }
        catch (Exception ignored) {
        }
        return true;
      }
      case "gc" -> {
        if (!sender.hasPermission("cherry.crystal.gc")) {
          return true;
        }
        long t = System.currentTimeMillis();
        Crystal.gc();
        long t2 = System.currentTimeMillis();
        sender.sendMessage("System.gc(): " + (t2 - t) + " millis");
        return true;
      }
      case "info" -> {
        if (!sender.hasPermission("cherry.crystal.info")) {
          return true;
        }
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
          message.append(args[i]);
          if (i < args.length - 1) {
            message.append(" ");
          }
        }
        Crystal.info(Color.mfc2ansi(Message.effect(message.toString())));
        return true;
      }
      case "warn" -> {
        if (!sender.hasPermission("cherry.crystal.warn")) {
          return true;
        }
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
          message.append(args[i]);
          if (i < args.length - 1) {
            message.append(" ");
          }
        }
        Crystal.warn(Color.mfc2ansi(Message.effect(message.toString())));
        return true;
      }
      case "error" -> {
        if (!sender.hasPermission("cherry.crystal.error")) {
          return true;
        }
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
          message.append(args[i]);
          if (i < args.length - 1) {
            message.append(" ");
          }
        }
        Crystal.error(Color.mfc2ansi(Message.effect(message.toString())));
        return true;
      }
      case "fatal" -> {
        if (!sender.hasPermission("cherry.crystal.fatal")) {
          return true;
        }
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
          message.append(args[i]);
          if (i < args.length - 1) {
            message.append(" ");
          }
        }
        Crystal.fatal(Color.mfc2ansi(Message.effect(message.toString())));
        return true;
      }

      default -> {
        return true;
      }

    }

  }

}

