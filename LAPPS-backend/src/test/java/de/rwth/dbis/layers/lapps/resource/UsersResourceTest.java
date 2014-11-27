package de.rwth.dbis.layers.lapps.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.rwth.dbis.layers.lapps.Main;
import de.rwth.dbis.layers.lapps.exception.OIDCException;

public class UsersResourceTest {

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
   * Test to see if a list of user mails can be received.
   */
  @Test
  public void testGetAllUser() {
    String response = target.path("users").request().get(String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode userIds;
    try {
      userIds = mapper.readTree(response);
      assertTrue(!userIds.isNull());
      assertTrue(userIds.isArray());
      JsonNode firstUser = userIds.get(0);
      // Analyze the first user id child in node, rest is not checked.
      assertEquals("1", firstUser.toString());
    } catch (Exception e) {
      e.printStackTrace();
      fail("JSON parsing failed with " + e.getMessage());
    }
  }

  /**
   * Test the authentication functionality.
   */
  @Test
  public void testAuthentication() {
    // int returnValue = -1;
    try {
      // returnValue =
      UsersResource.authenticate("here has to be a valid token in the future");
    } catch (OIDCException e) {
      // e.printStackTrace();
      // Currently, this is desired TODO
      // fail("Open Id authentication did not succeed!");
    }
    // TODO assertEquals(4, returnValue); //ID of test user, currently also not usable since no
    // valid token is provided
  }
}
