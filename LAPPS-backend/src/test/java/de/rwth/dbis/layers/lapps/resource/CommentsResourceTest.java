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
import de.rwth.dbis.layers.lapps.entity.Comment;
import de.rwth.dbis.layers.lapps.entity.App;
import de.rwth.dbis.layers.lapps.entity.User;

/**
 * Comments Resource Test Class
 * 
 */
public class CommentsResourceTest {

  private HttpServer server;
  private WebTarget target;
  private static final Logger LOGGER = Logger.getLogger(CommentsResource.class.getName());
  private Facade entityFacade = new Facade();
  private App app = null;
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
    app = new App("TestApp", "iOS", "TestApp");
    app = entityFacade.save(app);
    LOGGER.info("App created: " + app);

    LOGGER.info("Creating a new user...");
    user = entityFacade.save(DataGeneratorUtils.getRandomDeveloperUser());
    LOGGER.info("User created: " + user);


  }

  @After
  public void tearDown() throws Exception {
 
    LOGGER.info("Deleting old user data...");
    entityFacade.deleteByParam(User.class, "id", user.getId());
    LOGGER.info("User data deleted.");

    LOGGER.info("Deleting old app data...");
    entityFacade.deleteByParam(App.class, "id", app.getId());
    LOGGER.info("App data deleted.");

    server.shutdownNow();
  }

  
  /**
   * Tries to create an comment.
   */
  @Test
  public void testCreateComment() {
    Comment newComment = null;
    try {
      newComment = new Comment("test comment text", user, app);
      Response response =
          target.path("apps/" + app.getId() + "comments").request()
              .header("accessToken", OIDCAuthentication.OPEN_ID_TEST_TOKEN)
              .post(entity(newComment, MediaType.APPLICATION_JSON));
      assertEquals(HttpStatusCode.OK, response.getStatus());
      MediaType responseMediaType = response.getMediaType();
      assertEquals(MediaType.APPLICATION_JSON, responseMediaType.toString());
      String responseContent = response.readEntity(String.class);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode retrievedComment;
      retrievedComment = mapper.readTree(responseContent);
      assertTrue(!retrievedComment.isNull());
    } catch (Exception e) {
      e.printStackTrace();
      fail("JSON parsing failed with " + e.getMessage());
    } finally {
      LOGGER.info("Deleting created comment data...");
      entityFacade.deleteByParam(Comment.class, "id", newComment.getId());
      LOGGER.info("Comment data deleted.");
    }
  }

    
}
