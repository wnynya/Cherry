package io.wany.cherry.supports.vault;

import io.wany.cherry.Cherry;
import io.wany.cherry.Console;
import io.wany.cherry.Message;
import io.wany.cherry.amethyst.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VaultEconomySync {

  public static boolean ENABLED = false;
  public static MySQL database;
  public static Statement statement;
  public static String tableName;
  public static HashMap<OfflinePlayer, Double> lastPlayersBalances = new HashMap<>();
  public static ExecutorService syncExecutorService = Executors.newFixedThreadPool(1);
  public static Timer syncTimer = new Timer();

  public static void onPlayerJoin(PlayerJoinEvent event) {
    if (!ENABLED) {
      return;
    }
    if (statement == null) {
      return;
    }

    Player player = event.getPlayer();
    String uuidString = player.getUniqueId().toString();

    try {
      ResultSet checkSet = statement.executeQuery("SELECT * FROM " + tableName + " WHERE uuid = '" + uuidString + "';");
      int n = 0;
      while (checkSet.next()) {
        n++;
      }
      checkSet.close();
      if (n == 0) {
        double balance = VaultSupport.ECONOMY.getBalance(player);
        statement.executeUpdate("INSERT INTO " + tableName + " (uuid, name, balance) VALUES ('" + uuidString + "', '" + player.getName() + "', " + balance + ");");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void onEnable() {

    if (!Cherry.CONFIG.getBoolean("vault-support.economy.database-sync.enable")) {
      return;
    }
    Console.debug(Message.effect(VaultSupport.PREFIX + "  Load economy database-sync module"));
    ENABLED = true;

    MySQL.DatabaseConfig databaseConfig = new MySQL.DatabaseConfig(Cherry.CONFIG.getString("vault-support.economy.database-sync.hostname"), Cherry.CONFIG.getInt("vault-support.economy.database-sync.port"), Cherry.CONFIG.getString("vault-support.economy.database-sync.username"), Cherry.CONFIG.getString("vault-support.economy.database-sync.password"), Cherry.CONFIG.getString("vault-support.economy.database-sync.database"));
    database = new MySQL("cherry-vault-economy-sync", databaseConfig);

    tableName = Cherry.CONFIG.getString("vault-support.economy.database-sync.table");

    Console.debug(Message.effect(VaultSupport.PREFIX + "    Use database " + databaseConfig.database + "." + tableName));

    try {
      statement = database.getStatement();
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    try {
      int n = statement.executeUpdate("SHOW TABLES LIKE '" + tableName + "';");
      if (n == 0) {
        Console.debug(Message.effect(VaultSupport.PREFIX + "  Table not exist. create new table"));
        statement.executeUpdate("CREATE TABLE " + tableName + " (" + "uuid varchar(64) NOT NULL, " + "name varchar(64) NOT NULL, " + "balance bigint NOT NULL," + "PRIMARY KEY (`uuid`) " + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      return;
    }

    for (Player player : Bukkit.getOnlinePlayers()) {
      String uuidString = player.getUniqueId().toString();
      try {
        ResultSet checkSet = statement.executeQuery("SELECT * FROM " + tableName + " WHERE uuid = '" + uuidString + "';");
        int n = 0;
        while (checkSet.next()) {
          n++;
        }
        if (n == 0) {
          double balance = VaultSupport.ECONOMY.getBalance(player);
          statement.executeUpdate("INSERT INTO " + tableName + " (uuid, name, balance) VALUES ('" + uuidString + "', '" + player.getName() + "', " + balance + ");");
        }
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }

    syncExecutorService.submit(() -> syncTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        HashMap<OfflinePlayer, Double> lbp = (HashMap<OfflinePlayer, Double>) lastPlayersBalances.clone();
        for (OfflinePlayer player : lbp.keySet()) {
          double currentBalance = VaultSupport.ECONOMY.getBalance(player);
          double lastBalance = lbp.get(player);
          double dbBalance;
          try {
            ResultSet resultSet = statement.executeQuery("SELECT balance FROM " + tableName + " WHERE uuid = '" + player.getUniqueId() + "';");
            resultSet.next();
            dbBalance = resultSet.getDouble(1);
          }
          catch (Exception e) {
            e.printStackTrace();
            continue;
          }
          if (lastBalance != dbBalance && lastBalance == currentBalance) {
            double c = lastBalance - dbBalance;
            if (c > 0) {
              VaultSupport.ECONOMY.withdrawPlayer(player, c);
            }
            else {
              VaultSupport.ECONOMY.depositPlayer(player, (c * -1));
            }
            currentBalance = VaultSupport.ECONOMY.getBalance(player);
          }
          if (currentBalance != dbBalance) {
            try {
              statement.executeUpdate("UPDATE " + tableName + " SET balance = " + currentBalance + " WHERE uuid = '" + player.getUniqueId() + "';");
            }
            catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
          lastPlayersBalances.remove(player);
          lastPlayersBalances.put(player, VaultSupport.ECONOMY.getBalance(player));
        }
      }
    }, 0, 1000));

  }

  public static void onDisable() {
    if (!ENABLED) {
      return;
    }

    if (statement != null) {
      try {
        statement.close();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }

    syncTimer.cancel();
    syncExecutorService.shutdown();
  }

}
