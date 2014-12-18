package de.rwth.dbis.layers.lapps.resource;

import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.rwth.dbis.layers.lapps.Main;
import de.rwth.dbis.layers.lapps.domain.Facade;
import de.rwth.dbis.layers.lapps.entity.App;

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
  public void testGetAllApps() {
    // TODO:
    assertTrue(true);
  }

  @Test
  public void testGetApp() {
    // TODO:
    assertTrue(true);
  }

  @Test
  public void testDeleteApp() {
    // TODO:
    assertTrue(true);
  }

  @Test
  public void testUpdateApp() {
    // TODO:
    assertTrue(true);
  }

}
