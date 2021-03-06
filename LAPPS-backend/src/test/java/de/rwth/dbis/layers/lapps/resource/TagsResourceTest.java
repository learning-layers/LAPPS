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

public class TagsResourceTest {

  private HttpServer server;
  private WebTarget target;
  private static final Logger LOGGER = Logger.getLogger(TagsResource.class.getName());
  private Facade entityFacade = new Facade();
  private App app = null;
  private Tag tag = null;
  private User user = null;

  @Before
  public void setUp() throws Exception {
    // start the server
    server = Main.startServer();
    // create the client
    Client c = ClientBuilder.newClient();
    target = c.target(Main.BASE_URI);

    LOGGER.info("Creating a new app...");
    app =
        new App("TestApp", "iOS", "TestApp Short Description", "0.2",
            "This is a not so long Description", "www.here-123.itis");
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
   * Test to see if a list of tags can be retrieved. Checks, if the previously created tag is in the
   * list.
   */
  @Test
  public void testGetAllTags() {
    Response response =
        target.path("apps/" + app.getId() + "/tags").request(MediaType.APPLICATION_JSON).get();
    assertEquals(HttpStatusCode.OK, response.getStatus());
    MediaType responseMediaType = response.getMediaType();
    assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
    String responseContent = response.readEntity(String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode tags;
    try {
      tags = mapper.readTree(responseContent);
      assertFalse(tags.isNull());
      assertTrue(tags.isArray());
      Iterator<JsonNode> tagIterator = tags.iterator();
      // check if previously created tag can be found
      JsonNode retrievedTags = null;
      while (tagIterator.hasNext()) {
        // go through the list until our tag is found
        retrievedTags = tagIterator.next();
        if (retrievedTags.get("value").toString().equals("\"" + tag.getValue().toString() + "\"")) {
          break;
        }
      }
      // this is only false if tag was not in list
      assertEquals("\"" + tag.getValue().toString() + "\"", retrievedTags.get("value").toString());
    } catch (Exception e) {
      e.printStackTrace();
      fail("JSON parsing failed with " + e.getMessage());
    }
  }

  /**
   * Tries to create a new tag.
   */
  @Test
  public void testCreateTag() {
    Tag newTag = null;
    try {
      newTag = new Tag("NewTag");
      Response response =
          target.path("apps/" + app.getId() + "/tags").request()
              .header("accessToken", OIDCAuthentication.OPEN_ID_TEST_TOKEN)
              .post(entity(newTag, MediaType.APPLICATION_JSON));
      assertEquals(HttpStatusCode.CREATED, response.getStatus());
      MediaType responseMediaType = response.getMediaType();
      assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
      String responseContent = response.readEntity(String.class);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode retrievedTag;
      retrievedTag = mapper.readTree(responseContent);
      assertTrue(!retrievedTag.isNull());
      assertEquals("\"" + newTag.getValue().toString() + "\"", retrievedTag.get("value").toString());
    } catch (Exception e) {
      e.printStackTrace();
      fail("JSON parsing failed with " + e.getMessage());
    } finally {
      LOGGER.info("Deleting old tag data...");
      entityFacade.deleteByParam(Tag.class, "id", newTag.getId());
      LOGGER.info("Tag data deleted.");
    }
  }

  /**
   * Tries to delete the previously created tag.
   */
  @Test
  public void testDeleteTag() {
    Response response =
        target.path("apps/" + app.getId() + "/tags/" + tag.getId()).request()
            .header("accessToken", OIDCAuthentication.OPEN_ID_TEST_TOKEN).delete();
    assertEquals(HttpStatusCode.NO_CONTENT, response.getStatus());

    response =
        target.path("apps/" + app.getId() + "/tags").request(MediaType.APPLICATION_JSON).get();
    assertEquals(HttpStatusCode.OK, response.getStatus());
    MediaType responseMediaType = response.getMediaType();
    assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
    String responseContent = response.readEntity(String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode retrievedTags;
    try {
      retrievedTags = mapper.readTree(responseContent);
      assertFalse(retrievedTags.isNull());
      assertTrue(retrievedTags.isArray());
      Iterator<JsonNode> tagIterator = retrievedTags.iterator();
      assertFalse(tagIterator.hasNext());
    } catch (Exception e) {
      e.printStackTrace();
      fail("JSON parsing failed with " + e.getMessage());
    }
  }
}
