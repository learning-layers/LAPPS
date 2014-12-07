package de.rwth.dbis.layers.lapps.domain;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import de.rwth.dbis.layers.lapps.Utils;
import de.rwth.dbis.layers.lapps.entity.AppArtifactEntity;
import de.rwth.dbis.layers.lapps.entity.AppDetailEntity;
import de.rwth.dbis.layers.lapps.entity.AppDetailTypeEntity;
import de.rwth.dbis.layers.lapps.entity.AppEntity;
import de.rwth.dbis.layers.lapps.entity.AppInstanceEntity;
import de.rwth.dbis.layers.lapps.entity.AppPlatformEntity;
import de.rwth.dbis.layers.lapps.entity.AppTagEntity;
import de.rwth.dbis.layers.lapps.entity.ArtifactTypeEntity;

/**
 * Test class for App Facade business objects.
 *
 */
public class AppFacadeTest {
  private AppFacade appFacade = AppFacade.getFacade();
  private AppEntity app = null;
  // Recreate This specific app (i.e. do not delete all apps, they are not your concern...).
  private String appName = "Test app";// Utils.generateRandomString();
  private final static Logger LOGGER = Logger.getLogger(AppFacadeTest.class.getName());

  @Before
  public void beforeTest() {
    appFacade.deleteAll("name", appName);
    app = this.createApp();
    app = this.appFacade.save(app);
    assertTrue("App ID or Instance ID lower than 0!", app.getId() > 0
        && app.getInstances().size() > 0);
  }

  public AppEntity createApp() {
    // Load a random app platform:
    AppPlatformEntity platform = getRandomPlatform();
    // Load a random app detail type:
    AppDetailTypeEntity detailType = getRandomDetailType();
    // Load a random app artifact type:
    ArtifactTypeEntity artifactType = getRandomArtifactType();
    // Save a new app with all its properties.
    AppEntity app = new AppEntity(appName);
    AppInstanceEntity appInstance = new AppInstanceEntity(platform, "url_" + appName);
    appInstance.addTag(new AppTagEntity("tag_" + appName));
    appInstance.addDetail(new AppDetailEntity(detailType, "detail_" + appName));
    appInstance.addArtifacts(new AppArtifactEntity(artifactType, "url_" + appName));
    app.addInstance(appInstance);
    return app;
  }

  @Test
  public void addInstance() {
    // Load a random app platform:
    AppPlatformEntity platform = getRandomPlatform();
    // Create another app instance and add it to this existing app:
    AppInstanceEntity appInstance =
        new AppInstanceEntity(platform, "http://test.com/second_dummy_instance");
    appInstance.setApp(app);
    this.app = appFacade.save(app);
    assertTrue(app.getInstances().size() > 1);
  }

  @Test
  public void addDetail() {
    // Load a random app detail type:
    AppDetailTypeEntity detailType = getRandomDetailType();
    // Create another app detail and add it to this existing app:
    AppDetailEntity appDetail = new AppDetailEntity(detailType, "Test app description");
    app.getInstances().get(0).addDetail(appDetail);
    app = appFacade.save(app);
    assertTrue(app.getInstances().get(0).getDetails().size() > 1);
  }

  @Test
  public void addTag() {
    AppTagEntity tag = new AppTagEntity("New_tag_" + app.getName());
    app.getInstances().get(0).addTag(tag);
    app = appFacade.save(app);
    assertTrue(app.getInstances().get(0).getTags().size() > 1);
  }

  @Test
  public void addArtifact() {
    // Load a random app artifact type:
    ArtifactTypeEntity artifactType = getRandomArtifactType();
    // Create another app artifact and add it to this existing app:
    AppArtifactEntity appArtifact =
        new AppArtifactEntity(artifactType, "artifact_" + app.getName());
    app.getInstances().get(0).addArtifacts(appArtifact);
    app = appFacade.save(app);
    assertTrue(app.getInstances().get(0).getArtifacts().size() > 1);
  }

  @Test
  public void load() {
    AppEntity app = this.getRandomApp();
    assertTrue(app.getId() > 0);
  }

  @Test
  public void findByName() {
    String name = app.getName().substring(4); // error-prone!
    List<AppEntity> matched = appFacade.findByName(name);
    boolean found = false;
    for (AppEntity a : matched) {
      if (a.getId() == app.getId()) {
        found = true;
        break;
      }
    }
    assertTrue(found);
  }

  @Test
  public void pagination() {
    String needle = "App";
    List<AppEntity> apps = appFacade.findByParameter("name", needle, 2, 2, true);
    if (apps.size() > 0) {
      for (AppEntity app : apps) {
        assertTrue("The app " + app.getName() + " fetched does not contain '" + needle
            + "' in its name!", app.getName().toLowerCase().contains(needle.toLowerCase()));
      }
    } else {
      LOGGER.info("No apps containing '" + needle + "' in thier names matched!");
    }
  }

  public static AppPlatformEntity getRandomPlatform() {
    List<AppPlatformEntity> platforms = AppPlatformFacade.getFacade().findAll();
    assertTrue("No platforms available! Please create at least one platform and try again!",
        platforms.size() > 0);
    return platforms.get(Utils.generateRandomInt(0, platforms.size()));
  }

  public static AppDetailTypeEntity getRandomDetailType() {
    List<AppDetailTypeEntity> detailTypes = AppDetailTypeFacade.getFacade().findAll();
    assertTrue("No detail types available! Please create at least one detail type and try again!",
        detailTypes.size() > 0);
    return detailTypes.get(Utils.generateRandomInt(0, detailTypes.size()));
  }

  public static ArtifactTypeEntity getRandomArtifactType() {
    List<ArtifactTypeEntity> artifactTypes = ArtifactFacade.getFacade().findAll();
    assertTrue(
        "No artifact types available! Please create at least on artifact type and try again!",
        artifactTypes.size() > 0);
    return artifactTypes.get(Utils.generateRandomInt(0, artifactTypes.size()));
  }

  public AppEntity getRandomApp() {
    List<AppEntity> apps = appFacade.findAll();
    return apps.get(Utils.generateRandomInt(0, apps.size()));
  }

  // One-time tests. TODO: Either remove, or drop and create random in @Before.
  // @Test
  public void addPlatform() {
    final String platformName = "Github";
    AppPlatformEntity platform = new AppPlatformEntity(platformName);
    AppPlatformFacade platformFacade = AppPlatformFacade.getFacade();
    platform = platformFacade.save(platform);
    LOGGER.info("platform saved: " + platform.toString());
  }


  // @Test
  public void addArtifactType() {
    final String artifactType = "png";
    ArtifactTypeEntity artifact = new ArtifactTypeEntity(artifactType);
    ArtifactFacade artifactFacade = ArtifactFacade.getFacade();
    artifact = artifactFacade.save(artifact);
    LOGGER.info("artifact type saved: " + artifact.toString());
  }

  // @Test
  public void addDetailType() {
    final String detailTypeName = "General description";
    AppDetailTypeEntity detailType = new AppDetailTypeEntity(detailTypeName);
    AppDetailTypeFacade detailTypeFacade = AppDetailTypeFacade.getFacade();
    detailType = detailTypeFacade.save(detailType);
    LOGGER.info("detail type saved: " + detailType.toString());
  }
}
