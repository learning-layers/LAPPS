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

import de.rwth.dbis.layers.lapps.Main;
import de.rwth.dbis.layers.lapps.authenticate.OIDCAuthentication;
import de.rwth.dbis.layers.lapps.domain.UserFacade;
import de.rwth.dbis.layers.lapps.entity.UserEntity;
import de.rwth.dbis.layers.lapps.exception.OIDCException;

/**
 * User Resource Test Class
 * 
 */
public class UsersResourceTest {

  private HttpServer server;
  private WebTarget target;
  private static final Logger LOGGER = Logger.getLogger(UsersResource.class.getName());
  private UserFacade userFacade = new UserFacade();
  private UserEntity user = null;

  @Before
  public void setUp() throws Exception {
    // start the server
    server = Main.startServer();
    // create the client
    Client c = ClientBuilder.newClient();
    target = c.target(Main.BASE_URI);

    LOGGER.info("Deleting old user data...");
    userFacade.deleteAll("oidcId", 1234567L);
    LOGGER.info("User data deleted.");
    LOGGER.info("Creating a new user...");
    user = new UserEntity(1234567, "test@lapps.com", "testuser");
    user = userFacade.save(user);
    LOGGER.info("User created: " + user);
  }

  @After
  public void tearDown() throws Exception {
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
    JsonNode users;
    try {
      users = mapper.readTree(responseContent);
      assertTrue(!users.isNull());
      assertTrue(users.isArray());
      Iterator<JsonNode> userIterator = users.iterator();
      // check if previously created user can be found
      JsonNode retrievedUser = null;
      while (userIterator.hasNext()) {
        // go through the list until our user is found
        retrievedUser = userIterator.next();
        if (retrievedUser.get("id").toString().equals(user.getId())) {
          break;
        }
      }
      // this is only false if user was not in list
      assertEquals(user.getId().toString(), retrievedUser.get("id").toString());
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
        target.path("users/" + user.getId().toString()).request(MediaType.APPLICATION_JSON).get();
    assertEquals(HttpStatusCode.OK, response.getStatus());
    MediaType responseMediaType = response.getMediaType();
    assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
    String responseContent = response.readEntity(String.class);
    assertEquals(new String("{\"id\":" + user.getId().toString()
        + ",\"oidcId\":1234567,\"email\":\"test@lapps.com\",\"username\":\"testuser\"}"),
        responseContent);
  }

  /**
   * Tries to delete the previously created user. Currently should result in a not implemented
   * return.
   */
  @Test
  public void testDeleteUser() {
    Response response =
        target.path("users/" + user.getId().toString()).request()
            .header("accessToken", OIDCAuthentication.OPEN_ID_TEST_TOKEN).delete();
    assertEquals(HttpStatusCode.NOT_IMPLEMENTED, response.getStatus());
  }

  /**
   * Tries to update the previously created user. Currently should result in a not implemented
   * return.
   */
  @Test
  public void testUpdateUser() {
    UserEntity updatedUser = user;
    updatedUser.setEmail("new@mail.com");
    Response response =
        target.path("users/" + user.getId().toString()).request()
            .header("accessToken", OIDCAuthentication.OPEN_ID_TEST_TOKEN)
            .put(entity(updatedUser, MediaType.APPLICATION_JSON));
    assertEquals(HttpStatusCode.NOT_IMPLEMENTED, response.getStatus());
  }

  /**
   * Test the authentication functionality.
   */
  @Test
  public void testAuthentication() {
    int returnValue = 0;
    try {
      returnValue = OIDCAuthentication.authenticate(OIDCAuthentication.OPEN_ID_TEST_TOKEN);
    } catch (OIDCException e) {
      fail("Open Id authentication did not succeed!");
    }
    assertEquals(OIDCAuthentication.OPEN_ID_USER_ID, returnValue); // ID of test user
  }
}
