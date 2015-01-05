package de.rwth.dbis.layers.lapps.resource;

import static javax.ws.rs.client.Entity.entity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import de.rwth.dbis.layers.lapps.entity.App;
import de.rwth.dbis.layers.lapps.entity.Tag;
import de.rwth.dbis.layers.lapps.entity.User;

/**
 * Applications Resource Test Class
 * 
 */
public class ApplicationsResourceTest {

  private HttpServer server;
  private WebTarget target;
  private static final Logger LOGGER = Logger.getLogger(ApplicationsResource.class.getName());
  private Facade entityFacade = new Facade();
  private App app = null;
  private User user = null;
  private Tag tag = null;

  @Before
  public void setUp() throws Exception {
    // start the server
    server = Main.startServer();
    // create the client
    Client c = ClientBuilder.newClient();
    target = c.target(Main.BASE_URI);

    LOGGER.info("Creating a new app...");
    app = new App("TestApp", "iOS", "TestApp");
    app = entityFacade.save(app);
    LOGGER.info("App created: " + app);

    LOGGER.info("Creating a new user as creator of the TestApp...");
    user = entityFacade.save(DataGeneratorUtils.getRandomDeveloperUser());
    app.setCreator(user);
    LOGGER.info("User created: " + user);

    app = entityFacade.save(app);
    LOGGER.info("App updated with creator");

    LOGGER.info("Creating a new tag for the TestApp...");
    tag = new Tag("TestTag");
    tag.setApp(app);
    tag = entityFacade.save(tag);
    LOGGER.info("Tag created: " + tag);
  }

  @After
  public void tearDown() throws Exception {
    LOGGER.info("Deleting old tag data...");
    entityFacade.deleteByParam(Tag.class, "id", tag.getId());
    LOGGER.info("Tag data deleted.");

    LOGGER.info("Deleting old user data...");
    entityFacade.deleteByParam(User.class, "id", user.getId());
    LOGGER.info("User data deleted.");

    LOGGER.info("Deleting old app data...");
    entityFacade.deleteByParam(App.class, "id", app.getId());
    LOGGER.info("App data deleted.");

    server.shutdownNow();
  }

  /**
   * Test to see if a list of apps can be retrieved. Checks, if the previously created app is in the
   * list.
   */
  @Test
  public void testGetAllApps() {
    Response response = target.path("apps").request(MediaType.APPLICATION_JSON).get();
    assertEquals(HttpStatusCode.OK, response.getStatus());
    MediaType responseMediaType = response.getMediaType();
    assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
    String responseContent = response.readEntity(String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode apps;
    try {
      apps = mapper.readTree(responseContent);
      assertFalse(apps.isNull());
      assertTrue(apps.isArray());
      Iterator<JsonNode> appIterator = apps.iterator();
      // check if previously created app can be found
      JsonNode retrievedApp = null;
      while (appIterator.hasNext()) {
        // go through the list until our app is found
        retrievedApp = appIterator.next();
        if (retrievedApp.get("id").toString().equals(app.getId().toString())) {
          break;
        }
      }
      // this is only false if app was not in list
      assertEquals(app.getId().toString(), retrievedApp.get("id").toString());
    } catch (Exception e) {
      e.printStackTrace();
      fail("JSON parsing failed with " + e.getMessage());
    }
  }

  /**
   * Test to get the previously created app.
   */
  @Test
  public void testGetApp() {
    Response response =
        target.path("apps/" + app.getId()).request(MediaType.APPLICATION_JSON).get();
    assertEquals(HttpStatusCode.OK, response.getStatus());
    MediaType responseMediaType = response.getMediaType();
    assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
    String responseContent = response.readEntity(String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode retrievedApp;
    try {
      retrievedApp = mapper.readTree(responseContent);
      assertEquals(app.getId().toString(), retrievedApp.get("id").toString());
      assertEquals("\"" + app.getName() + "\"", retrievedApp.get("name").toString());
    } catch (Exception e) {
      e.printStackTrace();
      fail("JSON parsing failed with " + e.getMessage());
    }
  }

  /**
   * Tries to create an app.
   */
  @Test
  public void testCreateApp() {
    App newApp = null;
    try {
      newApp = new App("NewAppCreateTest", "iOS", "NewApp");
      Response response =
          target.path("apps").request()
              .header("accessToken", OIDCAuthentication.OPEN_ID_TEST_TOKEN)
              .post(entity(newApp, MediaType.APPLICATION_JSON));
      assertEquals(HttpStatusCode.OK, response.getStatus());
      MediaType responseMediaType = response.getMediaType();
      assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
      String responseContent = response.readEntity(String.class);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode retrievedApp;
      retrievedApp = mapper.readTree(responseContent);
      assertTrue(!retrievedApp.isNull());
      assertEquals("\"" + newApp.getName() + "\"", retrievedApp.get("name").toString());
    } catch (Exception e) {
      e.printStackTrace();
      fail("JSON parsing failed with " + e.getMessage());
    } finally {
      LOGGER.info("Deleting created app data...");
      entityFacade.deleteByParam(App.class, "name", "NewAppCreateTest");
      LOGGER.info("App data deleted.");
    }
  }

  /**
   * Tries to delete the previously created app.
   */
  @Test
  public void testDeleteApp() {
    Response response =
        target.path("apps/" + app.getId()).request()
            .header("accessToken", OIDCAuthentication.OPEN_ID_TEST_TOKEN).delete();
    assertEquals(HttpStatusCode.OK, response.getStatus());

    response = target.path("apps/" + app.getId()).request(MediaType.APPLICATION_JSON).get();
    assertEquals(HttpStatusCode.NOT_FOUND, response.getStatus());
  }

  @Test
  public void testUpdateApp() {
    app.setName("UpdatedApp");
    Response response =
        target.path("apps/" + app.getId()).request()
            .header("accessToken", OIDCAuthentication.OPEN_ID_TEST_TOKEN)
            .put(entity(app, MediaType.APPLICATION_JSON));
    assertEquals(HttpStatusCode.OK, response.getStatus());

    response = target.path("apps/" + app.getId()).request(MediaType.APPLICATION_JSON).get();
    assertEquals(HttpStatusCode.OK, response.getStatus());
    MediaType responseMediaType = response.getMediaType();
    assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
    String responseContent = response.readEntity(String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode retrievedApp;
    try {
      retrievedApp = mapper.readTree(responseContent);
      assertEquals(app.getId().toString(), retrievedApp.get("id").toString());
      assertEquals("\"" + app.getName() + "\"", retrievedApp.get("name").toString());
    } catch (Exception e) {
      e.printStackTrace();
      fail("JSON parsing failed with " + e.getMessage());
    }
  }

}
