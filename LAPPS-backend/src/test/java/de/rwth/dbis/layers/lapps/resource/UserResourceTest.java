package de.rwth.dbis.layers.lapps.resource;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.rwth.dbis.layers.lapps.Main;

public class UserResourceTest {

  private HttpServer server;
  private WebTarget target;

  @Before
  public void setUp() throws Exception {
    // start the server
    server = Main.startServer();
    // create the client
    Client c = ClientBuilder.newClient();

    target = c.target(Main.BASE_URI);
  }

  @After
  public void tearDown() throws Exception {
    server.shutdownNow();
  }

  /**
   * Tries to get the user with the id 1.
   */
  @Test
  public void testGetUser() {
    String responseMsg = target.path("users/1").request().get(String.class);
    assertEquals(new String("{\"id\":1,\"oidcId\":\"1\",\"email\":\"test@lapps.com\"}"),
        responseMsg);
  }
}
