package de.rwth.dbis.layers.lapps.domain;

import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import org.junit.Test;

import de.rwth.dbis.layers.lapps.entity.AppDetailEntity;
import de.rwth.dbis.layers.lapps.entity.AppDetailTypeEntity;
import de.rwth.dbis.layers.lapps.entity.AppEntity;
import de.rwth.dbis.layers.lapps.entity.AppInstanceEntity;
import de.rwth.dbis.layers.lapps.entity.AppPlatformEntity;
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
    // Load app platform:
    AppPlatformFacade platformFacade = AppPlatformFacade.getFacade();
    AppPlatformEntity platform = platformFacade.find(1);
    // Load app detail type:
    AppDetailTypeFacade detailTypeFacade = AppDetailTypeFacade.getFacade();
    AppDetailTypeEntity detailType = detailTypeFacade.find(1);
    // Save new app with a tag, an instance, a platform and a detail.
    final String appName = "app" + this.generateRandom(5000);
    AppEntity app = new AppEntity(appName);
    app.addTag(new AppTagEntity("tag_" + appName));
    app.addInstance(new AppInstanceEntity(platform, "http://ok.com/turboApp"));
    app.addDetail(new AppDetailEntity(detailType, "Nazdrave!"));
    app = this.appFacade.save(app);
    assertTrue(app.getId() > 0);
    LOGGER.info("Application saved: " + app);
  }

  // @Test
  public void saveInstance() {
    // Load app platform:
    AppPlatformFacade platformFacade = AppPlatformFacade.getFacade();
    AppPlatformEntity platform = platformFacade.find(2);
    // Load app:
    AppEntity app = appFacade.find(27);
    // Create another app instance and add it to this existing app:
    AppInstanceEntity appInstance = new AppInstanceEntity(platform, "http://app.com/app1");
    appInstance.setApp(app);
    app = appFacade.save(app);
    assertTrue(app.getInstances().size() > 0);
    LOGGER.info("Instance created, application now: " + app);
  }

  // @Test
  public void find() {
    AppEntity app = appFacade.find(2);
    LOGGER.info("app loaded: " + app.toString());
  }

  // @Test
  public void addPlatform() {
    final String platformName = "iOS";
    AppPlatformEntity platform = new AppPlatformEntity(platformName);
    AppPlatformFacade platformFacade = AppPlatformFacade.getFacade();
    platform = platformFacade.save(platform);
    LOGGER.info("platform saved: " + platform.toString());
  }

  // @Test
  public void addDetailType() {
    final String detailTypeName = "General description";
    AppDetailTypeEntity detailType = new AppDetailTypeEntity(detailTypeName);
    AppDetailTypeFacade detailTypeFacade = AppDetailTypeFacade.getFacade();
    detailType = detailTypeFacade.save(detailType);
    LOGGER.info("detail type saved: " + detailType.toString());
  }

  private long generateRandom(long max) {
    return Math.round(Math.random() * max);
  }
}
