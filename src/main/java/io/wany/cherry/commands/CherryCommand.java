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
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.SocketTimeoutException;
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

      case "version", "v" -> {
        if (!sender.hasPermission("cherry.version")) {
          return true;
        }
        Message.info(sender, Cherry.PREFIX + Cherry.PLUGIN.getDescription().getVersion());
        return true;
      }

      case "reload", "r" -> {
        if (!sender.hasPermission("cherry.reload")) {
          return true;
        }
        long s = System.currentTimeMillis();
        PluginLoader.unload();
        PluginLoader.load(Cherry.FILE);
        long e = System.currentTimeMillis();
        Message.info(sender, Cherry.PREFIX + "Reload complete &7(" + (e - s) + "ms)");
        return true;
      }

      case "update", "u" -> {
        if (!sender.hasPermission("cherry.update")) {
          return true;
        }

        boolean silent = false;
        boolean force = false;
        if (args.length > 1) {
          for (String str : args) {
            if (str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
              silent = true;
            }
            if (str.equalsIgnoreCase("-force") || str.equalsIgnoreCase("-f")) {
              force = true;
            }
          }
        }

        long s = System.currentTimeMillis();
        Updater.Version version;
        if (!silent) {
          Message.info(sender, Cherry.PREFIX + "Check latest " + Cherry.COLOR + Updater.defaultUpdater.getChannel() + "&r version . . . ");
        }
        try {
          version = Updater.defaultUpdater.getLatestVersion();
        }
        catch (Updater.NotFoundException exception) {
          Message.warn(sender, Cherry.PREFIX + "&eError on updater/check: Build Not Found");
          return true;
        }
        catch (Updater.InternalServerErrorException exception) {
          Message.warn(sender, Cherry.PREFIX + "&eError on updater/check: Internal Server Error");
          return true;
        }
        catch (SocketTimeoutException exception) {
          Message.warn(sender, Cherry.PREFIX + "&eError on updater/check: Timed Out");
          return true;
        }
        catch (IOException exception) {
          Message.warn(sender, Cherry.PREFIX + "&eError on updater/check: IO");
          return true;
        }
        catch (ParseException exception) {
          Message.warn(sender, Cherry.PREFIX + "&eError on updater/check: Data Parse Failed");
          return true;
        }
        catch (Exception e) {
          Message.warn(sender, Cherry.PREFIX + "&eError on updater/check: Unknown");
          return true;
        }

        if (Cherry.PLUGIN.getDescription().getVersion().equals(version.name)) {
          if (force) {
            if (!silent) {
              Message.info(sender, Cherry.PREFIX + "Already latest version " + Cherry.COLOR + Cherry.PLUGIN.getDescription().getVersion() + "&r, but it forces to update");
            }
          }
          else {
            if (!silent) {
              Message.info(sender, Cherry.PREFIX + "Already latest version " + Cherry.COLOR + Cherry.PLUGIN.getDescription().getVersion() + "");
            }
            return true;
          }
        }
        else {
          if (!silent) {
            Message.info(sender, Cherry.PREFIX + "Found new latest version " + Cherry.COLOR + version.name);
          }
        }

        if (!silent) {
          Message.info(sender, Cherry.PREFIX + "Downloading file . . . ");
        }
        try {
          version.download();
        }
        catch (SecurityException exception) {
          Message.warn(sender, Cherry.PREFIX + "&eError on updater/download: Denied");
          return true;
        }
        catch (FileNotFoundException exception) {
          Message.warn(sender, Cherry.PREFIX + "&eError on updater/download: File Not Found");
          return true;
        }
        catch (IOException exception) {
          Message.warn(sender, Cherry.PREFIX + "&eError on updater/download: IO");
          return true;
        }
        catch (Exception e) {
          Message.warn(sender, Cherry.PREFIX + "&eError on updater/download: Unknown");
          return true;
        }

        if (!silent) {
          Message.info(sender, Cherry.PREFIX + "Update plugin . . . ");
        }
        try {
          version.update();
        }
        catch (IOException exception) {
          Message.warn(sender, Cherry.PREFIX + "&eError on updater/update: IO");
          return true;
        }
        catch (Exception e) {
          Message.warn(sender, Cherry.PREFIX + "&eError on updater/update: Unknown");
          return true;
        }

        long e = System.currentTimeMillis();

        if (!silent) {
          Message.info(sender, Cherry.PREFIX + "Update Success " + Cherry.COLOR + Cherry.PLUGIN.getDescription().getVersion() + "&r => " + Cherry.COLOR + version.name + "&r &7(" + (e - s) + "ms)");
        }

        return true;
      }

      case "menu", "m" -> {
        if (!sender.hasPermission("cherry.menu")) {
          return true;
        }
        if (!(sender instanceof Player player)) {
          return true;
        }

        Menu.show(player, Menu.Main.inventory(player));
        return true;
      }

      case "system", "s" -> {

        if (args.length == 1) {
          if (!sender.hasPermission("cherry.system")) {
            return true;
          }
          return true;
        }

        switch (args[1].toLowerCase()) {

          case "info", "i" -> {
            if (!sender.hasPermission("cherry.system.info")) {
              return true;
            }
            Runtime r = Runtime.getRuntime();
            com.sun.management.OperatingSystemMXBean osb = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            Message.info(sender, Cherry.PREFIX + "Processor");
            Message.info(sender, Cherry.PREFIX + "  System Load: " + Cherry.COLOR + Math.round(osb.getProcessCpuLoad() * 10000) / 100.0 + "&r%");
            Message.info(sender, Cherry.PREFIX + "  Server Load: " + Cherry.COLOR + Math.round(osb.getCpuLoad() * 10000) / 100.0 + "&r%");
            Message.info(sender, Cherry.PREFIX + "Memory");
            long freeM = Math.round(r.freeMemory() / 1024.0 / 1024.0);
            long maxM = Math.round(r.maxMemory() / 1024.0 / 1024.0);
            long totalM = Math.round(r.totalMemory() / 1024.0 / 1024.0);
            long usedM = Math.round((r.totalMemory() - r.freeMemory()) / 1024.0 / 1024.0);
            Message.info(sender, Cherry.PREFIX + "  Load: " + Cherry.COLOR + usedM + "&rM / " + Cherry.COLOR + totalM + "&rM / " + Cherry.COLOR + maxM + "&rM");
            return true;
          }

          case "java", "j" -> {
            if (!sender.hasPermission("cherry.system.java")) {
              return true;
            }
            Message.info(sender, Cherry.PREFIX + "Java " + Cherry.COLOR + System.getProperty("java.vm.version"));
            Message.info(sender, Cherry.PREFIX + "  Runtime: " + Cherry.COLOR + System.getProperty("java.runtime.name"));
            Message.info(sender, Cherry.PREFIX + "  Vendor: " + Cherry.COLOR + System.getProperty("java.vm.vendor"));
            Message.info(sender, Cherry.PREFIX + "  Home: " + Cherry.COLOR + System.getProperty("java.home"));
            return true;
          }

          case "gc" -> {
            if (!sender.hasPermission("cherry.system.gc")) {
              return true;
            }
            boolean silent = false;
            if (args.length > 2) {
              for (String str : args) {
                if (str.equalsIgnoreCase("-silent") || str.equalsIgnoreCase("-s")) {
                  silent = true;
                  break;
                }
              }
            }
            long s = System.currentTimeMillis();
            System.gc();
            long e = System.currentTimeMillis();
            if (!silent) {
              Message.info(sender, Cherry.PREFIX + "System.gc(); &7(" + (e - s) + "ms)");
            }
            return true;
          }

        }

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



      // Crystal
      // Boooooooooom!
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

