package io.wany.cherry.commands;

import io.wany.cherry.Cherry;
import io.wany.cherry.Message;
import io.wany.cherry.Updater;
import io.wany.cherry.amethyst.Color;
import io.wany.cherry.amethyst.Crystal;
import io.wany.cherry.amethyst.PluginLoader;
import io.wany.cherry.amethyst.SystemInfo;
import io.wany.cherry.gui.Menu;
import io.wany.cherry.supports.coreprotect.CoreProtectSupport;
import io.wany.cherry.supports.cucumbery.CucumberySupport;
import io.wany.cherry.terminal.Terminal;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        Terminal.STATUS = Terminal.Status.RELOAD;
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

        ExecutorService e = Executors.newFixedThreadPool(1);
        e.submit(() -> {

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
          Updater.Version version = null;
          if (!silent) {
            Message.info(sender, Cherry.PREFIX + "Check latest " + Cherry.COLOR + Updater.defaultUpdater.getChannelName() + "&r version . . . ");
          }
          try {
            version = Updater.defaultUpdater.getLatestVersion();
          }
          catch (Updater.NotFoundException exception) {
            Message.warn(sender, Cherry.PREFIX + "&eError on updater/check: Build Not Found");
          }
          catch (Updater.InternalServerErrorException exception) {
            Message.warn(sender, Cherry.PREFIX + "&eError on updater/check: Internal Server Error");
          }
          catch (SocketTimeoutException exception) {
            Message.warn(sender, Cherry.PREFIX + "&eError on updater/check: Timed Out");
          }
          catch (IOException exception) {
            Message.warn(sender, Cherry.PREFIX + "&eError on updater/check: IO");
          }
          catch (ParseException exception) {
            Message.warn(sender, Cherry.PREFIX + "&eError on updater/check: Data Parse Failed");
          }
          catch (Exception e1) {
            Message.warn(sender, Cherry.PREFIX + "&eError on updater/check: Unknown");
          }

          if (version != null) {

            if (Cherry.PLUGIN.getDescription().getVersion().equals(version.name) && !force) {
              if (!silent) {
                Message.info(sender, Cherry.PREFIX + "Already latest version " + Cherry.COLOR + Cherry.PLUGIN.getDescription().getVersion() + "");
              }
            }
            else {

              if (Cherry.PLUGIN.getDescription().getVersion().equals(version.name)) {
                if (!silent) {
                  Message.info(sender, Cherry.PREFIX + "Already latest version " + Cherry.COLOR + Cherry.PLUGIN.getDescription().getVersion() + "&r, but it forces to update");
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
                version = null;
              }
              catch (FileNotFoundException exception) {
                Message.warn(sender, Cherry.PREFIX + "&eError on updater/download: File Not Found");
                version = null;
              }
              catch (IOException exception) {
                Message.warn(sender, Cherry.PREFIX + "&eError on updater/download: IO");
                version = null;
              }
              catch (Exception e1) {
                Message.warn(sender, Cherry.PREFIX + "&eError on updater/download: Unknown");
                version = null;
              }

              if (version != null) {
                if (!silent) {
                  Message.info(sender, Cherry.PREFIX + "Update plugin . . . ");
                }
                try {
                  version.update();
                }
                catch (IOException exception) {
                  Message.warn(sender, Cherry.PREFIX + "&eError on updater/update: IO");
                  version = null;
                }
                catch (Exception e1) {
                  Message.warn(sender, Cherry.PREFIX + "&eError on updater/update: Unknown");
                  version = null;
                }

                if (version != null) {
                  long e1 = System.currentTimeMillis();

                  if (!silent) {
                    Message.info(sender, Cherry.PREFIX + "Update Success " + Cherry.COLOR + Cherry.PLUGIN.getDescription().getVersion() + "&r => " + Cherry.COLOR + version.name + "&r &7(" + (e1 - s) + "ms)");
                  }
                }

              }

            }

          }

        });

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

            Message.info(sender, Cherry.PREFIX + "Bukkit");
            Message.info(sender, Cherry.PREFIX + "  " + Cherry.COLOR + Bukkit.getServer().getName() + "&r " + Bukkit.getServer().getVersion());
            Message.info(sender, Cherry.PREFIX + "  Players: " + Cherry.COLOR + Bukkit.getOnlinePlayers().size() + "&r / " + Bukkit.getMaxPlayers());
            int worldLoadedChunks = 0;
            int worldForceLoadedChunks = 0;
            for (World world : Bukkit.getWorlds()) {
              worldLoadedChunks += world.getLoadedChunks().length;
              worldForceLoadedChunks += world.getForceLoadedChunks().size();
            }
            Message.info(sender, Cherry.PREFIX + "  Worlds: " + Cherry.COLOR + Bukkit.getWorlds().size() + "&r Chunks: " + worldLoadedChunks + " (" + worldForceLoadedChunks + ")");

            Message.info(sender, Cherry.PREFIX + "Processor");
            com.sun.management.OperatingSystemMXBean osb = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            /*for (final Cpu cpu : SystemInfo.cpus) {
              Message.info(sender, Cherry.PREFIX + "  Name: " + Cherry.COLOR + cpu.name);
            }*/
            Message.info(sender, Cherry.PREFIX + "  System Load: " + Cherry.COLOR + Math.round(osb.getCpuLoad() * 10000) / 100.0 + "&r%");
            Message.info(sender, Cherry.PREFIX + "  Server Load: " + Cherry.COLOR + Math.round(osb.getProcessCpuLoad() * 10000) / 100.0 + "&r%");

            Message.info(sender, Cherry.PREFIX + "Memory");
            Runtime r = Runtime.getRuntime();
            //long freeM = Math.round(r.freeMemory() / 1024.0 / 1024.0);
            long maxM = Math.round(r.maxMemory() / 1024.0 / 1024.0);
            long totalM = Math.round(r.totalMemory() / 1024.0 / 1024.0);
            long usedM = Math.round((r.totalMemory() - r.freeMemory()) / 1024.0 / 1024.0);
            if (maxM <= 16384) {
              Message.info(sender, Cherry.PREFIX + "  Load: " + Cherry.COLOR + usedM + "&rM / " + Cherry.COLOR + totalM + "&rM / " + Cherry.COLOR + maxM + "&rM");
            }
            else {
              //double freeMd = Math.round(freeM / 1024.0 * 100.0) / 100.0;
              double maxMd = Math.round(maxM / 1024.0 * 100.0) / 100.0;
              double totalMd = Math.round(totalM / 1024.0 * 100.0) / 100.0;
              double usedMd = Math.round(usedM / 1024.0 * 100.0) / 100.0;
              Message.info(sender, Cherry.PREFIX + "  Load: " + Cherry.COLOR + usedMd + "&rG / " + Cherry.COLOR + totalMd + "&rG / " + Cherry.COLOR + maxMd + "&rG");
            }

            Message.info(sender, Cherry.PREFIX + "TPS");
            double[] bukkitTPS = Bukkit.getTPS();
            Message.info(sender, Cherry.PREFIX + "  " + Cherry.COLOR + SystemInfo.serverLastTPS + "&r, " + Math.round(bukkitTPS[0] * 10) / 10.0 + "&r, " + Math.round(bukkitTPS[1] * 10) / 10.0 + "&r, " + Math.round(bukkitTPS[2] * 10) / 10.0);

            return true;
          }

          case "java", "j" -> {
            if (!sender.hasPermission("cherry.system.java")) {
              return true;
            }
            Message.info(sender, Cherry.PREFIX + "Java " + Cherry.COLOR + SystemInfo.javaVMVersion);
            Message.info(sender, Cherry.PREFIX + "  Runtime: " + Cherry.COLOR + SystemInfo.javaRuntimeName);
            Message.info(sender, Cherry.PREFIX + "  Vendor: " + Cherry.COLOR + SystemInfo.javaVMVersion);
            Message.info(sender, Cherry.PREFIX + "  Home: " + Cherry.COLOR + SystemInfo.javaHome);
            return true;
          }

          /*
          case "processor", "processors", "cpu", "cpus" -> {
            if (!sender.hasPermission("cherry.system.processor")) {
              return true;
            }

            Message.info(sender, Cherry.PREFIX + "Processor");
            com.sun.management.OperatingSystemMXBean osb = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            if (SystemInfo.cpus.size() == 0) {
              Message.info(sender, Cherry.PREFIX + "  &7No detailed processor information");
            }
            for (final Cpu cpu : SystemInfo.cpus) {
              Message.info(sender, Cherry.PREFIX + "  Name: " + Cherry.COLOR + cpu.name);
              Message.info(sender, Cherry.PREFIX + "  Arch: " + Cherry.COLOR + osb.getArch());
              Message.info(sender, Cherry.PREFIX + "  Threads: " + Cherry.COLOR + osb.getAvailableProcessors());
              List<Temperature> temps = cpu.sensors.temperatures;
              if (temps.size() > 1) {
                Message.info(sender, Cherry.PREFIX + "  Temperatures:");
              }
              for (final Temperature temp : temps) {
                Message.info(sender, Cherry.PREFIX + "    " + temp.name + ": " + Cherry.COLOR + Math.round(temp.value * 10) / 10.0 + "&r °C");
              }
              List<Load> loads = cpu.sensors.loads;
              if (loads.size() > 1) {
                Message.info(sender, Cherry.PREFIX + "  Loads:");
              }
              for (final Load load : loads) {
                Message.info(sender, Cherry.PREFIX + "    " + load.name + ": " + Cherry.COLOR + Math.round(load.value * 10) / 10.0 + "&r %");
              }
              List<Fan> fans = cpu.sensors.fans;
              if (fans.size() > 1) {
                Message.info(sender, Cherry.PREFIX + "  Fans:");
              }
              for (final Fan fan : fans) {
                Message.info(sender, Cherry.PREFIX + "    " + fan.name + ": " + Cherry.COLOR + Math.round(fan.value * 10) / 10.0 + "&r RPM");
              }
            }
            Message.info(sender, Cherry.PREFIX + "  System Load: " + Cherry.COLOR + Math.round(osb.getCpuLoad() * 1000) / 10.0 + "&r%");
            Message.info(sender, Cherry.PREFIX + "  Server Load: " + Cherry.COLOR + Math.round(osb.getProcessCpuLoad() * 1000) / 10.0 + "&r%");

            return true;
          }

          case "graphic", "graphics", "gpu", "gpus" -> {
            if (!sender.hasPermission("cherry.system.graphic")) {
              return true;
            }

            Message.info(sender, Cherry.PREFIX + "Graphic");
            if (SystemInfo.gpus.size() == 0) {
              Message.info(sender, Cherry.PREFIX + "  &7No detailed graphic processor information");
            }
            for (final Gpu gpus : SystemInfo.gpus) {
              Message.info(sender, Cherry.PREFIX + "  Name: " + Cherry.COLOR + gpus.name);
              List<Temperature> temps = gpus.sensors.temperatures;
              if (temps.size() > 1) {
                Message.info(sender, Cherry.PREFIX + "  Temperatures:");
              }
              for (final Temperature temp : temps) {
                Message.info(sender, Cherry.PREFIX + "    " + temp.name + ": " + Cherry.COLOR + Math.round(temp.value * 10) / 10.0 + "&r °C");
              }
              List<Load> loads = gpus.sensors.loads;
              if (loads.size() > 1) {
                Message.info(sender, Cherry.PREFIX + "  Loads:");
              }
              for (final Load load : loads) {
                Message.info(sender, Cherry.PREFIX + "    " + load.name + ": " + Cherry.COLOR + Math.round(load.value * 10) / 10.0 + "&r %");
              }
              List<Fan> fans = gpus.sensors.fans;
              if (fans.size() > 1) {
                Message.info(sender, Cherry.PREFIX + "  Fans:");
              }
              for (final Fan fan : fans) {
                Message.info(sender, Cherry.PREFIX + "    " + fan.name + ": " + Cherry.COLOR + Math.round(fan.value * 10) / 10.0 + "&r RPM");
              }
            }
            try {
              for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
                int width = gd.getDisplayMode().getWidth();
                int height = gd.getDisplayMode().getHeight();
                int bit = gd.getDisplayMode().getBitDepth();
                int refreshRate = gd.getDisplayMode().getRefreshRate();
                String idString = gd.getIDstring();
                Message.info(sender, Cherry.PREFIX + "  Monitor " + Cherry.COLOR + idString);
                Message.info(sender, Cherry.PREFIX + "    " + width + "x" + height + " / " + refreshRate + "Hz " + bit + "Bits");
              }
            }
            catch (Exception ignored) {
            }

            return true;
          }
          */

          case "disk", "disks", "storage", "storages" -> {
            if (!sender.hasPermission("cherry.system.disk")) {
              return true;
            }

            Message.info(sender, Cherry.PREFIX + "Disk");
            ExecutorService e = Executors.newFixedThreadPool(1);
            e.submit(() -> {
              long serverDir = SystemInfo.getFolderByteSize(Cherry.SERVER_DIR);
              long worlds = 0;
              for (World world : Bukkit.getWorlds()) {
                worlds += SystemInfo.getFolderByteSize(world.getWorldFolder());
              }
              long pluginsDir = SystemInfo.getFolderByteSize(Cherry.PLUGINS_DIR);
              long cherryDir = SystemInfo.getFolderByteSize(Cherry.DIR);
              long cucumberyDir = new File(Cherry.PLUGINS_DIR.getAbsolutePath() + "/Cucumbery").length();
              long coreprotectLog = new File(Cherry.PLUGINS_DIR.getAbsolutePath() + "/CoreProtect/database.db").length();
              Message.info(sender, Cherry.PREFIX + "  Server: " + SystemInfo.getFriendlyByteSize(serverDir));
              Message.info(sender, Cherry.PREFIX + "  Worlds: " + SystemInfo.getFriendlyByteSize(worlds));
              Message.info(sender, Cherry.PREFIX + "  Plugins: " + SystemInfo.getFriendlyByteSize(pluginsDir));
              Message.info(sender, Cherry.PREFIX + "  Cherry: " + SystemInfo.getFriendlyByteSize(cherryDir));
              if (CucumberySupport.EXIST) {
                Message.info(sender, Cherry.PREFIX + "  Cucumbery: " + SystemInfo.getFriendlyByteSize(cucumberyDir));
              }
              if (CoreProtectSupport.EXIST) {
                Message.info(sender, Cherry.PREFIX + "  CoreProtect: " + SystemInfo.getFriendlyByteSize(coreprotectLog));
              }
            });

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
        return true;
      }

      case "translate" -> {
        if (!sender.hasPermission("cherry.translate")) {
          return true;
        }
        if (!(sender instanceof Player player)) {
          return true;
        }

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
          sb1.append(args[i]);
          sb2.append(getTranslate(args[i]));
          if (i < args.length - 1) {
            sb1.append(" ");
            sb2.append(" ");
          }
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
          p.sendMessage(Message.parse(player, " => ", sb1.toString(), " => ", sb2.toString()));
        }

        return true;
      }


      // Crystal
      // Boooooooooom!

      case "gc" -> {
        if (!sender.hasPermission("cherry.crystal.gc")) {
          return true;
        }
        Crystal.gc();
        return true;
      }
      case "sleep" -> {
        if (!sender.hasPermission("cherry.crystal.sleep")) {
          return true;
        }
        long l = 0;
        try {
          l = Long.parseLong(args[1]);
        }
        catch (Exception ignored) {}
        Crystal.sleep(l);
        return true;
      }
      case "exit" -> {
        if (!sender.hasPermission("cherry.crystal.exit")) {
          return true;
        }
        int i = 0;
        try {
          i = Integer.parseInt(args[1]);
        }
        catch (Exception ignored) {}
        Crystal.exit(i);
        return true;
      }
      case "halt" -> {
        if (!sender.hasPermission("cherry.crystal.halt")) {
          return true;
        }
        int i = 0;
        try {
          i = Integer.parseInt(args[1]);
        }
        catch (Exception ignored) {}
        Crystal.halt(i);
        return true;
      }
      case "crash" -> {
        if (!sender.hasPermission("cherry.crystal.crash")) {
          return true;
        }
        Crystal.crash();
        return true;
      }

      /*case "exec" -> {
        if (!sender.hasPermission("cherry.crystal.exec")) {
          return true;
        }
        StringBuilder c = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
          c.append(args[i]);
          if (i < args.length - 1) {
            c.append(" ");
          }
        }
        Crystal.exec(c.toString());
        return true;
      }*/

      case "windowsprompt" -> {
        if (!sender.hasPermission("cherry.crystal.windowsprompt")) {
          return true;
        }
        StringBuilder c = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
          c.append(args[i]);
          if (i < args.length - 1) {
            c.append(" ");
          }
        }
        Crystal.windowsPrompt(c.toString(), "sansppap", 16);
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

  @SuppressWarnings("all")
  public static String getTranslate(String text) {

    JSONObject objecta = new JSONObject();
    objecta.put("source", "");
    objecta.put("target", "en");
    objecta.put("text", text);

    try {

      URL url = new URL("https://api.wany.io/terminal/translate");

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("User-Agent", "Cherry");
      connection.setConnectTimeout(2000);
      connection.setReadTimeout(2000);

      OutputStream outputStream = connection.getOutputStream();
      outputStream.write(objecta.toJSONString().getBytes(StandardCharsets.UTF_8));
      outputStream.flush();

      int responseCode = connection.getResponseCode();
      if (responseCode == 200) { // OK
        Reader inputReader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader streamReader = new BufferedReader(inputReader);
        String streamLine;
        StringBuilder content = new StringBuilder();
        while ((streamLine = streamReader.readLine()) != null) {
          content.append(streamLine);
        }
        streamReader.close();
        connection.disconnect();
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(content.toString());
        return object.get("data").toString();
      }
      connection.disconnect();

    } catch (Exception ignored) {}
    return "";
  }

}