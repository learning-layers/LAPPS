package de.rwth.dbis.layers.lapps.domain;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import de.rwth.dbis.layers.lapps.Utils;
import de.rwth.dbis.layers.lapps.entity.AppArtifactEntity;
import de.rwth.dbis.layers.lapps.entity.AppDetailEntity;
import de.rwth.dbis.layers.lapps.entity.AppDetailTypeEntity;
import de.rwth.dbis.layers.lapps.entity.AppEntity;
import de.rwth.dbis.layers.lapps.entity.AppInstanceEntity;
import de.rwth.dbis.layers.lapps.entity.AppInstanceRightsEntity;
import de.rwth.dbis.layers.lapps.entity.AppPlatformEntity;
import de.rwth.dbis.layers.lapps.entity.ArtifactTypeEntity;
import de.rwth.dbis.layers.lapps.entity.UserEntity;

/**
 * A class for tests on set of mockup apps and users and their respective attributes.
 */
public class AppsAndUsersTest {
  private final static Logger LOGGER = Logger.getLogger(AppsAndUsersTest.class.getName());
  private AppFacade appFacade = AppFacade.getFacade();
  private UserFacade userFacade = new UserFacade();
  private static AppInstanceFacade appInstanceFacade = AppInstanceFacade.getFacade();
  private static List<AppPlatformEntity> platforms = AppPlatformFacade.getFacade().findAll();

  private final static int APP_COUNT = 30;
  private final static int USER_COUNT = 3;

  private List<AppEntity> apps = new ArrayList<AppEntity>();
  private List<UserEntity> users = new ArrayList<UserEntity>();

  /**
   * Initialize a set of user and app objects.
   */
  @Before
  public void init() {
    this.deleteExisting();
    this.populateWithData();

    this.addComments();
    this.addDescriptions();
    this.addArtifacts();
    this.findByQuery();
    // this.grantRights();

    for (AppEntity app : apps) {
      appFacade.save(app);
    }
  }

  @Test
  public void testResult() {
    this.dataAvailable();
    this.getComments();
    this.report();
  }

  private void dataAvailable() {
    assertTrue("No users or no apps have been created!", users.size() > 0 && apps.size() > 0);
    for (UserEntity u : users) {
      assertTrue("There is a user with an id <= 0!", u.getId() > 0);
    }
    for (AppEntity a : apps) {
      assertTrue("There is an app with an id <= 0!", a.getId() > 0);
    }
  }

  private void getComments() {
    boolean expectationMet = false;
    for (AppEntity app : apps) {
      if (app.getInstances().get(0).getComments().size() > 1) {
        expectationMet = true;
      }
    }
    assertTrue("There is no app instance with comments!", expectationMet);
  }

  private void deleteExisting() {
    LOGGER.info("Deleting data...");
    userFacade.deleteAll();
    appFacade.deleteAll();
  }

  private void populateWithData() {
    LOGGER.info("Creating data...");
    for (int i = 0; i < USER_COUNT; i++) {
      UserEntity user =
          new UserEntity(Utils.generateRandomInt(0, 5000), "test"
              + Utils.generateRandomInt(0, 5000) + "@lapps.com", "test-"
              + Utils.generateRandomInt(0, 5000));
      users.add(userFacade.save(user));
    }
    for (int i = 0; i < APP_COUNT; i++) {
      AppEntity app = new AppEntity(Utils.MockupAppGenerator.getRandomName());
      // AppInstanceEntity appInstance =
      // new AppInstanceEntity(appPlatform, "http://store.apple.com/" + app.getName());
      // app.addInstance(appInstance);
      try {
        app = appFacade.save(app);
      } catch (Throwable t) {
        // TODO: Try to catch SQL error for duplicates!
        LOGGER.log(Level.WARNING, "Possible duplicate! Error: " + t.getMessage());
      }
      app =
          appFacade.createAppInstance(Utils.getRandomEntity(users), app,
              Utils.getRandomEntity(platforms), "http://dummyStore.com/" + app.getName());
      apps.add(app);
      AppInstanceEntity instance = app.getInstances().get(0);
      instance.setSourceUrl("http://dummySourceStore.com/" + app.getName());
      instance.setAvailableOn("Min. Version: " + Utils.generateRandomInt(1, 10));
      instance.setVersion(Utils.generateRandomInt(1, 10) + "." + Utils.generateRandomInt(1, 10));
      instance.setSize(Utils.generateRandomInt(100, 10000));
      // appInstanceFacade.save(instance);
    }
  }

  private void addComments() {
    userFacade.comment("I really love this app!", users.get(0), apps.get(0).getInstances().get(0));
    userFacade.comment("Aaah.. that's a crap...", users.get(users.size() - 1), apps.get(0)
        .getInstances().get(0));
    // new AppCommentEntity("Test Comment", users.get(Utils.generateRandomInt(0, users.size())),
    // apps.get(0));
    // new AppCommentEntity("Another Test Comment",
    // users.get(Utils.generateRandomInt(0, users.size())), apps.get(0));
  }

  private void addDescriptions() {
    // Detail types are not to be deleted => use them with hard-coded ids
    AppDetailTypeEntity detailType = AppDetailTypeFacade.getFacade().find(1);
    assertTrue(detailType != null);
    for (AppEntity app : apps) {
      app.getInstances()
          .get(0)
          .addDetail(
              new AppDetailEntity(detailType, Utils.MockupAppGenerator.getRandomIpsumBlock()));
    }
  }

  private void addArtifacts() {
    // Artifact types are not to be deleted => use them with hard-coded ids
    ArtifactTypeEntity image = ArtifactFacade.getFacade().find(1);
    ArtifactTypeEntity thumbnail = ArtifactFacade.getFacade().find(2);
    assertTrue(image != null && thumbnail != null);
    for (AppEntity app : apps) {
      String[] pair = Utils.MockupAppGenerator.getImagePair();
      app.getInstances().get(0).addArtifacts(new AppArtifactEntity(image, pair[1]));
      app.getInstances().get(0).addArtifacts(new AppArtifactEntity(thumbnail, pair[0]));
    }
  }

  private void report() {
    LOGGER.info("All apps created: ");
    for (AppEntity app : apps) {
      LOGGER.info(app.toString());
    }
  }

  private void findByQuery() {
    String pl = Utils.getRandomEntity(platforms).getName();
    List<AppInstanceEntity> appInstances = appInstanceFacade.findByParameter("platform.name", pl);
    LOGGER.info("Apps on " + pl + ": " + appInstances.size());
  }

  private void grantRights() {
    UserEntity user = users.get(0);
    AppInstanceEntity appInstance = apps.get(0).getInstances().get(0);
    user = userFacade.authorize(user, AppInstanceRightsEntity.CREATE, appInstance);
  }
}
