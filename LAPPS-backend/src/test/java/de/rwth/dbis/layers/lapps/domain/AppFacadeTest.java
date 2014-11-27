package de.rwth.dbis.layers.lapps.domain;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import de.rwth.dbis.layers.lapps.Utils;
import de.rwth.dbis.layers.lapps.data.EMF;
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
    LOGGER.info("Deleting app data...");
    // Clear data (TODO: beware that this will fail, if the database is already empty)
    final EntityManager em = EMF.getEm();
    em.getTransaction().begin();
    // Cascading delete on foreign keys seems to be doing the trick, so do delete just the 'root'
    // entity.
    em.createQuery("delete AppEntity app where app.name like :value")
        .setParameter("value", "%" + appName + "%").executeUpdate();
    em.getTransaction().commit();
    em.close();
    LOGGER.info("App data deleted.");
    LOGGER.info("Creating dummy app: ");
    app = this.createApp();
    LOGGER.info("Persisting dummy app: ");
    app = this.appFacade.save(app);
    assertTrue(app.getId() > 0 && app.getTags().size() > 0 && app.getDetails().size() > 0
        && app.getArtifacts().size() > 0 && app.getInstances().size() > 0);
    LOGGER.info("App created: " + app);
  }

  public AppEntity createApp() {
    // Load a random app platform:
    AppPlatformEntity platform = getRandomPlatform();
    // Load a random app detail type:
    AppDetailTypeEntity detailType = this.getRandomDetailType();
    // Load a random app artifact type:
    ArtifactTypeEntity artifactType = this.getRandomArtifactType();
    // Save a new app with all its properties.
    AppEntity app = new AppEntity(appName);
    app.addTag(new AppTagEntity("tag_" + appName));
    app.addDetail(new AppDetailEntity(detailType, "detail_" + appName));
    app.addArtifacts(new AppArtifactEntity(artifactType, "url_" + appName));
    app.addInstance(new AppInstanceEntity(platform, "url_" + appName));
    return app;
  }

  @Test
  public void addInstance() {
    LOGGER.info("Creating a random instance to add to " + app);
    // Load a random app platform:
    AppPlatformEntity platform = getRandomPlatform();
    // Create another app instance and add it to this existing app:
    AppInstanceEntity appInstance =
        new AppInstanceEntity(platform, "http://test.com/second_dummy_instance");
    appInstance.setApp(app);
    this.app = appFacade.save(app);
    assertTrue(app.getInstances().size() > 1);
    LOGGER.info("Instance created, application now: " + app);
  }

  @Test
  public void addDetail() {
    LOGGER.info("Creating a random detail to add to " + app);
    // Load a random app detail type:
    AppDetailTypeEntity detailType = this.getRandomDetailType();
    // Create another app detail and add it to this existing app:
    AppDetailEntity appDetail = new AppDetailEntity(detailType, "Test app description");
    app.addDetail(appDetail);
    app = appFacade.save(app);
    assertTrue(app.getDetails().size() > 1);
    LOGGER.info("Detail created, application now: " + app);
  }

  @Test
  public void addTag() {
    LOGGER.info("Creating a random tag to add to " + app);
    AppTagEntity tag = new AppTagEntity("New_tag_" + app.getName());
    app.addTag(tag);
    app = appFacade.save(app);
    assertTrue(app.getTags().size() > 1);
    LOGGER.info("Tag created, application now: " + app);
  }

  @Test
  public void addArtifact() {
    LOGGER.info("Creating a random artifact to add to " + app);
    // Load a random app artifact type:
    ArtifactTypeEntity artifactType = this.getRandomArtifactType();
    // Create another app artifact and add it to this existing app:
    AppArtifactEntity appArtifact =
        new AppArtifactEntity(artifactType, "artifact_" + app.getName());
    app.addArtifacts(appArtifact);
    app = appFacade.save(app);
    assertTrue(app.getArtifacts().size() > 1);
    LOGGER.info("Artifact created, application now: " + app);
  }

  @Test
  public void load() {
    LOGGER.info("Loading a random app...");
    AppEntity app = this.getRandomApp();
    assertTrue(app.getId() > 0);
    LOGGER.info("App loaded: " + app);
  }

  @Test
  public void findByName() {
    String name = app.getName().substring(4); // error-prone!
    LOGGER.info("Searching for \"" + name + "\" with existing \"" + app.getName() + "\"");
    AppEntity found = appFacade.findByName(name).get(0);
    assertTrue(found.getId() == app.getId());
    LOGGER.info("App found.");
  }

  public static AppPlatformEntity getRandomPlatform() {
    List<AppPlatformEntity> platforms = AppPlatformFacade.getFacade().findAll();
    if (platforms.size() < 1) {
      LOGGER.severe("No Platforms available!");
    }
    return platforms.get(Utils.generateRandomInt(0, platforms.size()));
  }

  public AppDetailTypeEntity getRandomDetailType() {
    List<AppDetailTypeEntity> detailTypes = AppDetailTypeFacade.getFacade().findAll();
    if (detailTypes.size() < 1) {
      LOGGER.severe("No Detail types available!");
    }
    return detailTypes.get(Utils.generateRandomInt(0, detailTypes.size()));
  }

  public ArtifactTypeEntity getRandomArtifactType() {
    List<ArtifactTypeEntity> artifactTypes = ArtifactFacade.getFacade().findAll();
    if (artifactTypes.size() < 1) {
      LOGGER.severe("No Artifact types available!");
    }
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
