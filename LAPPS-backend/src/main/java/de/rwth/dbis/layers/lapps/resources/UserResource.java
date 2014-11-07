package de.rwth.dbis.layers.lapps.resources;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import de.rwth.dbis.layers.lapps.data.ConnectionFactory;

@Path("users")
public class UserResource {

  @GET
  @Produces("text/plain")
  public String getAllUsers() {
    Connection conn;
    try {
      conn = ConnectionFactory.getConnection();
      if (conn != null) {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from user where 1");
        String users = "all the fellow users are:\r\n";
        while (rs.next()) {
          users += rs.getString("email") + "\r\n";
        }
        return users;
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "No connection to the LAPPS database...";
  }
}
