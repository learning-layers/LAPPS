package de.rwth.dbis.layers.lapps.domain;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
import de.rwth.dbis.layers.lapps.entity.ArtifactTypeEntity;
import de.rwth.dbis.layers.lapps.entity.UserEntity;

/**
 * A class for tests on set of mockup apps and users and their respective attributes.
 */
public class AppsAndUsersTest {
  private final static Logger LOGGER = Logger.getLogger(AppsAndUsersTest.class.getName());
  private AppFacade appFacade = AppFacade.getFacade();
  private UserFacade userFacade = new UserFacade();

  private final static int APP_COUNT = 2;
  private final static int USER_COUNT = 3;
  private List<String> appNames = null;

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
    this.addInstances();
    // TODO: Grand the first user rights for some apps!

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
    assertTrue(users.size() > 0 && apps.size() > 0);
    for (UserEntity u : users) {
      assertTrue(u.getId() > 0);
    }
    for (AppEntity a : apps) {
      assertTrue(a.getId() > 0);
    }
  }

  private void getComments() {
    boolean expectationMet = false;
    for (AppEntity app : apps) {
      if (app.getComments().size() > 1) {
        expectationMet = true;
      }
    }
    assertTrue(expectationMet);
  }

  private void deleteExisting() {
    LOGGER.info("Deleting data...");
    final EntityManager em = EMF.getEm();
    em.getTransaction().begin();
    em.createQuery(Utils.DELETE_APPS_QUERY).executeUpdate();
    em.createQuery(Utils.DELETE_USERS_QUERY).executeUpdate();
    em.getTransaction().commit();
    em.close();
  }

  private void populateWithData() {
    // Init predefined app names:
    appNames = new ArrayList<String>();
    appNames.add("HelpApp");
    appNames.add("ExpertApp");
    appNames.add("TurboApp");
    appNames.add("Yeah");

    LOGGER.info("Creating data...");
    for (int i = 0; i < APP_COUNT; i++) {
      AppEntity app = new AppEntity(appNames.remove(Utils.generateRandomInt(0, appNames.size())));
      apps.add(appFacade.save(app));
    }
    for (int i = 0; i < USER_COUNT; i++) {
      UserEntity user =
          new UserEntity(Utils.generateRandomInt(0, 5000) + "", "test"
              + Utils.generateRandomInt(0, 5000) + "@lapps.com");
      users.add(userFacade.save(user));
    }
  }

  private void addComments() {
    userFacade.comment("I really love this app!", users.get(0), apps.get(0));
    userFacade.comment("Aaah.. that's a crap...", users.get(users.size() - 1), apps.get(0));
    // new AppCommentEntity("Test Comment", users.get(Utils.generateRandomInt(0, users.size())),
    // apps.get(0));
    // new AppCommentEntity("Another Test Comment",
    // users.get(Utils.generateRandomInt(0, users.size())), apps.get(0));
  }

  private void addDescriptions() {
    // Detail types are not to be deleted => use them with hard-coded ids
    AppDetailTypeEntity detailType = AppDetailTypeFacade.getFacade().find(2);
    assertTrue(detailType != null);
    for (AppEntity app : apps) {
      app.addDetail(new AppDetailEntity(detailType, "Lorem ipsum..."));
    }
  }

  private void addArtifacts() {
    // Artifact types are not to be deleted => use them with hard-coded ids
    ArtifactTypeEntity artifactType = ArtifactFacade.getFacade().find(2);
    assertTrue(artifactType != null);
    for (AppEntity app : apps) {
      app.addArtifacts(new AppArtifactEntity(artifactType, "http://lorempixel.com/500/280/cats"));
      app.addArtifacts(new AppArtifactEntity(artifactType, "http://lorempixel.com/150/150/cats"));
    }
  }

  private void report() {
    LOGGER.info("All apps created: ");
    for (AppEntity app : apps) {
      LOGGER.info(app.toString());
    }
  }

  private void addInstances() {
    // Platform are not to be deleted => use them with hard-coded ids
    AppPlatformEntity appPlatform = AppPlatformFacade.getFacade().find(1);
    assertTrue(appPlatform != null);
    for (AppEntity app : apps) {
      app.addInstance(new AppInstanceEntity(appPlatform, "http://store.apple.com/" + app.getName()));
    }
  }
}
