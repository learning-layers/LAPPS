package de.rwth.dbis.layers.lapps.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
  private static Connection conn = null;

  public static Connection getConnection() throws SQLException {
    if (conn == null) {
      Properties connectionProps = new Properties();
      connectionProps.put("user", "java_user");
      connectionProps.put("password", "A6XwKeLFVCadZh2t");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lapps", connectionProps);
    }
    return conn;
  }
}
