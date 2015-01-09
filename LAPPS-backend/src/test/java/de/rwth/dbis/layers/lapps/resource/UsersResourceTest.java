package de.rwth.dbis.layers.lapps.resource;

import static javax.ws.rs.client.Entity.entity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.rwth.dbis.layers.lapps.DataGeneratorUtils;
import de.rwth.dbis.layers.lapps.Main;
import de.rwth.dbis.layers.lapps.authenticate.OIDCAuthentication;
import de.rwth.dbis.layers.lapps.domain.Facade;
import de.rwth.dbis.layers.lapps.entity.User;

/**
 * User Resource Test Class
 * 
 */
public class UsersResourceTest {

  private HttpServer server;
  private WebTarget target;
  private static final Logger LOGGER = Logger.getLogger(UsersResource.class.getName());
  private Facade facade = new Facade();
  private User user = null;

  @Before
  public void setUp() throws Exception {
    // start the server
    server = Main.startServer();
    // create the client
    Client c = ClientBuilder.newClient();
    target = c.target(Main.BASE_URI);

    LOGGER.info("Creating a new user...");
    user = facade.save(DataGeneratorUtils.getRandomDeveloperUser());
    LOGGER.info("User created: " + user);
  }

  @After
  public void tearDown() throws Exception {
    LOGGER.info("Deleting old user data...");
    facade.deleteByParam(User.class, "id", user.getId());
    LOGGER.info("User data deleted.");
    server.shutdownNow();
  }

  /**
   * Test to see if a list of user Ids can be retrieved. Checks, if the user previously created is
   * in the list.
   */
  @Test
  public void testGetAllUser() {
    Response response =
        target.path("users").request(MediaType.APPLICATION_JSON)
            .header("accessToken", OIDCAuthentication.OPEN_ID_TEST_TOKEN).get();
    assertEquals(HttpStatusCode.OK, response.getStatus());
    MediaType responseMediaType = response.getMediaType();
    assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
    String responseContent = response.readEntity(String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode retrievedUsers;
    try {
      retrievedUsers = mapper.readTree(responseContent);
      assertTrue(!retrievedUsers.isNull());
      assertTrue(retrievedUsers.isArray());
      Iterator<JsonNode> userIterator = retrievedUsers.iterator();
      // check if previously created user can be found
      JsonNode retrievedUser = null;
      while (userIterator.hasNext()) {
        // go through the list until our user is found
        retrievedUser = userIterator.next();
        if (retrievedUser.get("oidcId").toString().equals(user.getOidcId().toString())) {
          break;
        }
      }
      // this is only false if user was not in list
      assertEquals(user.getOidcId().toString(), retrievedUser.get("oidcId").toString());
    } catch (Exception e) {
      e.printStackTrace();
      fail("JSON parsing failed with " + e.getMessage());
    }
  }

  /**
   * Tries to get the previously created user.
   */
  @Test
  public void testGetUser() {
    Response response =
        target.path("users/" + user.getOidcId()).request(MediaType.APPLICATION_JSON).get();
    assertEquals(HttpStatusCode.OK, response.getStatus());
    MediaType responseMediaType = response.getMediaType();
    assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
    String responseContent = response.readEntity(String.class);
    assertTrue(responseContent.contains("{\"oidcId\":" + user.getOidcId() + ",\"email\":\""
        + user.getEmail() + "\",\"username\":\"" + user.getUsername() + "\",\"role\":"
        + user.getRole() + ","));
  }

  /**
   * Tries to delete the previously created user.
   */
  @Test
  public void testDeleteUser() {
    Response response =
        target.path("users/" + user.getOidcId()).request()
            .header("accessToken", OIDCAuthentication.OPEN_ID_TEST_TOKEN).delete();
    assertEquals(HttpStatusCode.OK, response.getStatus());

    response = target.path("users/" + user.getOidcId()).request((MediaType.APPLICATION_JSON)).get();
    assertEquals(HttpStatusCode.NOT_FOUND, response.getStatus());
  }

  /**
   * Tries to update the previously created user.
   */
  @Test
  public void testUpdateUser() {
    user.setUsername("UpdatedUser");
    Response response =
        target.path("users/" + user.getOidcId()).request()
            .header("accessToken", OIDCAuthentication.OPEN_ID_TEST_TOKEN)
            .put(entity(user, MediaType.APPLICATION_JSON));
    assertEquals(HttpStatusCode.OK, response.getStatus());

    response = target.path("users/" + user.getOidcId()).request(MediaType.APPLICATION_JSON).get();
    assertEquals(HttpStatusCode.OK, response.getStatus());
    MediaType responseMediaType = response.getMediaType();
    assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
    String responseContent = response.readEntity(String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode retrievedUser;
    try {
      retrievedUser = mapper.readTree(responseContent);
      assertEquals("\"" + user.getUsername() + "\"", retrievedUser.get("username").toString());
    } catch (Exception e) {
      e.printStackTrace();
      fail("JSON parsing failed with " + e.getMessage());
    }
  }

  /**
   * Test the authentication functionality (only test token!).
   */
  @Test
  public void testAuthentication() {
    boolean isAdmin = OIDCAuthentication.isAdmin(OIDCAuthentication.OPEN_ID_TEST_TOKEN);
    assertTrue(isAdmin);
  }
}
