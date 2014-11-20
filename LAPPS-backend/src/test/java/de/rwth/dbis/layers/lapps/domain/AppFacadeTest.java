package de.rwth.dbis.layers.lapps.domain;

import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import org.junit.Test;

import de.rwth.dbis.layers.lapps.entity.AppEntity;
import de.rwth.dbis.layers.lapps.entity.AppInstanceEntity;
import de.rwth.dbis.layers.lapps.entity.AppPlatformEntity;
import de.rwth.dbis.layers.lapps.entity.AppTagEntity;
import de.rwth.dbis.layers.lapps.entity.ArtifactTypeEntity;

/**
 * Test class for App Facade business ojects.
 *
 */
public class AppFacadeTest {
  private AppFacade appFacade = AppFacade.getFacade();
  private final static Logger LOGGER = Logger.getLogger(AppFacadeTest.class.getName());

  // @Test
  public void save() {
    // Load app platform:
    AppPlatformFacade platformFacade = AppPlatformFacade.getFacade();
    AppPlatformEntity platform = platformFacade.find(1);
    // Save new app with a tag, an instance and a platform.
    final String appName = "app" + this.generateRandom(5000);
    AppEntity app = new AppEntity(appName);
    app.addTag(new AppTagEntity("tag_" + appName));
    app.addInstance(new AppInstanceEntity(platform, "http://ok.com/turboApp"));
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
    AppEntity app = appFacade.find(47);
    // Create another app instance and add it to this existing app:
    AppInstanceEntity appInstance = new AppInstanceEntity(platform, "http://turbo.com/app");
    appInstance.setApp(app);
    app = appFacade.save(app);
    assertTrue(app.getInstances().size() > 1);
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

  @Test
  public void addArtifactType() {
    final String artifactType = "jpg";
    ArtifactTypeEntity artifact = new ArtifactTypeEntity(artifactType);
    ArtifactFacade artifactFacade = ArtifactFacade.getFacade();
    artifact = artifactFacade.save(artifact);
    LOGGER.info("artifact type saved: " + artifact.toString());
  }

  private long generateRandom(long max) {
    return Math.round(Math.random() * max);
  }
}
