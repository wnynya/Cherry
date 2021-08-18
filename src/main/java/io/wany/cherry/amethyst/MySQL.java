package io.wany.cherry.amethyst;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySQL {

  public static List<MySQL> databases = new ArrayList<>();

  private final String id;
  public Connection connection;

  public MySQL(String id, DatabaseConfig databaseConfig) {
    this.id = id;
    try {
      this.connection = DriverManager.getConnection("jdbc:mysql://" + databaseConfig.host + ":" + databaseConfig.port + "/" + databaseConfig.database, databaseConfig.username, databaseConfig.password);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    databases.add(this);
  }

  public static void onEnable() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void onDisable() {
    for (MySQL database : databases) {
      try {
        database.connection.close();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public Statement getStatement() throws SQLException {
    return this.connection.createStatement();
  }

  public String getId() {
    return id;
  }

  public static class DatabaseConfig {
    public String host;
    public int port;
    public String username;
    public String password;
    public String database;

    public DatabaseConfig(String host, int port, String username, String password, String database) {
      this.host = host;
      this.port = port;
      this.username = username;
      this.password = password;
      this.database = database;
    }
  }

}
