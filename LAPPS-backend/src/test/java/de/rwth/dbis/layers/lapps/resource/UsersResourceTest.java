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
    int returnValue = -1;
    try {
      returnValue =
          UsersResource
              .authenticate("eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE0MTYzODk5MzcsImF1ZCI6WyJMYXllcnNfQWRhcHRlciJdLCJpc3MiOiJodHRwOlwvXC9hcGkubGVhcm5pbmctbGF5ZXJzLmV1XC9vXC9vYXV0aDJcLyIsImp0aSI6IjZkY2E3NjViLWMxNGUtNDE2OS04OWI3LTQ0ODVjN2JjNjE2MyIsImlhdCI6MTQxNjM4NjMzN30.imGTLRw0Q5C3K3eJ7D1L7D4vyy1jS_NuZUz01DNtX4Sg8BbUi5izrxexMAjiJdRZiePyWCyBCv6BAvDxHppPKj9sycpVcPlmaNkMox7XIR19KoO847WynS_dFlEd97yMMtboXZwQhpkmm1XOXjc2CHdC6cUJ_hoEeHMlY-pAocE");
    } catch (OIDCException e) {
      e.printStackTrace();
      fail("Open Id authentication did not succeed!");
    }
    assertEquals(4, returnValue);
  }
}
