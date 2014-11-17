package de.rwth.dbis.layers.lapps.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.rwth.dbis.layers.lapps.Main;

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
   * 
   * @throws IOException
   * @throws JsonProcessingException
   */
  @Test
  public void testGetAllUser() throws JsonProcessingException, IOException {
    String response = target.path("users").request().get(String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode users = mapper.readTree(response);
    // Analyze the first user child in node, rest does not matter.
    assertTrue(!users.isNull());
    assertTrue(users.isArray());
    JsonNode firstUser = users.get(0);
    assertEquals(new String("{\"id\":1,\"oidcId\":\"1\",\"email\":\"test@lapps.com\"}"),
        firstUser.toString());
  }
}
