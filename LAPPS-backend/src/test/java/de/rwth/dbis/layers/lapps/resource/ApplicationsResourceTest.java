package de.rwth.dbis.layers.lapps.resource;

import static org.junit.Assert.assertTrue;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.rwth.dbis.layers.lapps.Main;

/**
 * Application Resource Test Class
 * 
 */
public class ApplicationsResourceTest {

  private HttpServer server;

  // private WebTarget target;

  // private static final Logger LOGGER = Logger.getLogger(UsersResource.class.getName());


  @Before
  public void setUp() throws Exception {
    // start the server
    server = Main.startServer();
    // create the client
    // Client c = ClientBuilder.newClient();
    // target = c.target(Main.BASE_URI);
  }

  @After
  public void tearDown() throws Exception {
    server.shutdownNow();
  }


  @Test
  public void testSomething() {
    // TODO:
    assertTrue(true);
  }

}
