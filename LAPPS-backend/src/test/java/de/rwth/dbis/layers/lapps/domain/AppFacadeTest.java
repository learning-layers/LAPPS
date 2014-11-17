package de.rwth.dbis.layers.lapps.domain;

import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import org.junit.Test;

import de.rwth.dbis.layers.lapps.entity.AppEntity;

/**
 * Test class for App Facade business ojects.
 *
 */
public class AppFacadeTest {
  private AppFacade appFacade = AppFacade.getFacade();
  private final static Logger LOGGER = Logger.getLogger(AppFacadeTest.class.getName());

  @Test
  public void save() {
    String appName = "app" + this.generateRandom(500);
    AppEntity app = new AppEntity(appName);
    app = this.appFacade.save(app);
    assertTrue(app.getId() > 0);
    LOGGER.info("Application saved: " + app);
  }

  private long generateRandom(long max) {
    return Math.round(Math.random() * max);
  }
}
