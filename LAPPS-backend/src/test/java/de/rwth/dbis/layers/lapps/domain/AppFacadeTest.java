package de.rwth.dbis.layers.lapps.domain;

import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import org.junit.Test;

import de.rwth.dbis.layers.lapps.entity.AppEntity;
import de.rwth.dbis.layers.lapps.entity.AppTagEntity;

/**
 * Test class for App Facade business ojects.
 *
 */
public class AppFacadeTest {
  private AppFacade appFacade = AppFacade.getFacade();
  private final static Logger LOGGER = Logger.getLogger(AppFacadeTest.class.getName());

  @Test
  public void save() {
    final String appName = "app" + this.generateRandom(5000);
    AppEntity app = new AppEntity(appName);
    app.addTag(new AppTagEntity("tag_" + appName));
    app = this.appFacade.save(app);
    assertTrue(app.getId() > 0);
    LOGGER.info("Application saved: " + app);
  }

  @Test
  public void find() {
    AppEntity app = appFacade.find(2);
    LOGGER.info("app loaded: " + app.toString());
  }

  private long generateRandom(long max) {
    return Math.round(Math.random() * max);
  }
}
