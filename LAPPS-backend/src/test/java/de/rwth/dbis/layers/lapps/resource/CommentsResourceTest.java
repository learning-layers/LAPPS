package de.rwth.dbis.layers.lapps.resource;

import static javax.ws.rs.client.Entity.entity;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

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
import de.rwth.dbis.layers.lapps.entity.Comment;
import de.rwth.dbis.layers.lapps.entity.User;

/**
 * Comments Resource Test Class
 */
public class CommentsResourceTest {

  private HttpServer server;
  private WebTarget target;
  private static final Logger LOGGER = Logger.getLogger(CommentsResource.class.getName());
  private Facade entityFacade = new Facade();
  private App ucApp = null;
  private App cApp = null;
  private User user = null;
  private Comment comment = null;

  @Before
  public void setUp() throws Exception {
    // start the server
    server = Main.startServer();
    // create the client
    Client c = ClientBuilder.newClient();
    target = c.target(Main.BASE_URI);

    LOGGER.info("Creating a new app...");
    ucApp = new App("TestApp", "iOS", "uncommented TestApp", "1.0", "long descr", "myUrl.com");
    ucApp = entityFacade.save(ucApp);
    LOGGER.info("App created: " + ucApp);

    LOGGER.info("Creating another new app...");
    cApp = new App("TestApp2", "iOS", "commented TestApp", "1.0", "long descr", "myUrl.com");
    cApp = entityFacade.save(cApp);
    LOGGER.info("App created: " + cApp);

    LOGGER.info("Creating a new user...");
    user = entityFacade.save(DataGeneratorUtils.getRandomDeveloperUser());
    LOGGER.info("User created: " + user);

    LOGGER.info("Creating a new comment...");
    comment = new Comment("TestMessage", 3, user, cApp);
    comment = entityFacade.save(comment);
    LOGGER.info("Comment created: " + comment);
  }

  @After
  public void tearDown() throws Exception {

    LOGGER.info("Deleting old comment data...");
    entityFacade.deleteByParam(Comment.class, "id", comment.getId());
    LOGGER.info("Comment data deleted.");

    LOGGER.info("Deleting old app data...");
    entityFacade.deleteByParam(App.class, "id", cApp.getId());
    entityFacade.deleteByParam(App.class, "id", ucApp.getId());
    LOGGER.info("App data deleted.");

    LOGGER.info("Deleting old user data...");
    entityFacade.deleteByParam(User.class, "id", user.getId());
    LOGGER.info("User data deleted.");

    server.shutdownNow();
  }

  /**
   * Test to get all comments for an app
   */
  @Test
  public void testGetAllComments() {
    Response response =
        target.path("apps/" + ucApp.getId() + "/comments").request(MediaType.APPLICATION_JSON)
            .get();
    assertEquals(HttpStatusCode.OK, response.getStatus());

  }

  /**
   * Test to get the created comment.
   */
  @Test
  public void testGetComment() {
    Response response =
        target.path("apps/" + cApp.getId() + "/comments/" + comment.getId())
            .request(MediaType.APPLICATION_JSON).get();
    assertEquals(HttpStatusCode.OK, response.getStatus());
    MediaType responseMediaType = response.getMediaType();
    assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
    String responseContent = response.readEntity(String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode retrievedComment;
    try {
      retrievedComment = mapper.readTree(responseContent);
      assertEquals(comment.getId().toString(), retrievedComment.get("id").toString());
    } catch (Exception e) {
      e.printStackTrace();
      fail("JSON parsing failed with " + e.getMessage());
    }
  }

  /**
   * Tries to create an comment.
   */
  @Test
  public void testCreateComment() {
    Comment newComment = null;
    long commentId = 0;
    try {
      newComment = new Comment("test comment text", 3, user, ucApp);
      Response response =
          target.path("apps/" + ucApp.getId() + "/comments").request()
              .header("accessToken", OIDCAuthentication.OPEN_ID_TEST_TOKEN)
              .post(entity(newComment, MediaType.APPLICATION_JSON));
      assertEquals(HttpStatusCode.CREATED, response.getStatus());
      MediaType responseMediaType = response.getMediaType();
      assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
      String responseContent = response.readEntity(String.class);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode retrievedComment;
      retrievedComment = mapper.readTree(responseContent);
      commentId = Long.valueOf(retrievedComment.get("id").toString());
      assertFalse(retrievedComment.isNull());
    } catch (Exception e) {
      e.printStackTrace();
      fail("JSON parsing failed with " + e.getMessage());
    } finally {
      LOGGER.info("Deleting created comment data...");
      entityFacade.deleteByParam(Comment.class, "id", commentId);
      LOGGER.info("Comment data deleted.");
    }
  }

  /**
   * Tries to delete the previously created comment.
   */
  @Test
  public void testDeleteComment() {
    Response response =
        target.path("apps/" + cApp.getId() + "/comments/" + comment.getId()).request()
            .header("accessToken", OIDCAuthentication.OPEN_ID_TEST_TOKEN).delete();
    assertEquals(HttpStatusCode.NO_CONTENT, response.getStatus());

  }

  /**
   * Tries to update the previously created comment.
   */
  @Test
  public void testUpdateComment() {
    comment.setContent("New comment text");
    comment = entityFacade.save(comment);
    Response response =
        target.path("apps/" + cApp.getId() + "/comments/" + comment.getId()).request()
            .header("accessToken", OIDCAuthentication.OPEN_ID_TEST_TOKEN)
            .put(entity(comment, MediaType.APPLICATION_JSON));
    assertEquals(HttpStatusCode.OK, response.getStatus());
    MediaType responseMediaType = response.getMediaType();
    assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
    String responseContent = response.readEntity(String.class);
    ObjectMapper mapper = new ObjectMapper();
    JsonNode retrievedComment;
    try {
      retrievedComment = mapper.readTree(responseContent);
      assertEquals(comment.getId().toString(), retrievedComment.get("id").toString());
      assertEquals("\"" + comment.getContent() + "\"", retrievedComment.get("content").toString());
    } catch (Exception e) {
      e.printStackTrace();
      fail("JSON parsing failed with " + e.getMessage());
    }
  }

}
