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
import de.rwth.dbis.layers.lapps.entity.AppEntity;
import de.rwth.dbis.layers.lapps.entity.UserEntity;

/**
 * A test class suited for tests on set of mockup apps and users and their respective comments and
 * rights.
 *
 */
public class AppsAndUsersTest {
  private final static Logger LOGGER = Logger.getLogger(AppsAndUsersTest.class.getName());
  private AppFacade appFacade = AppFacade.getFacade();
  private UserFacade userFacade = new UserFacade();

  private final static int APP_COUNT = 2;
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
    // Add two comments for the first app:
    userFacade.comment("Test Comment", users.get(0), apps.get(0));
    userFacade.comment("Another test comment", users.get(users.size() - 1), apps.get(0));
    // Grand the first user rights for some apps:
  }

  @Test
  public void dataAvailable() {
    assertTrue(users.size() > 0 && apps.size() > 0);
    for (UserEntity u : users) {
      assertTrue(u.getId() > 0);
    }
    for (AppEntity a : apps) {
      assertTrue(a.getId() > 0);
    }
  }

  @Test
  public void getComments() {
    boolean expectationMet = false;
    for (AppEntity app : apps) {
      if (app.getComments().size() > 1) {
        expectationMet = true;
      }
    }
    assertTrue(expectationMet);
  }

  @Test
  public void getManagedApps() {

  }

  private void deleteExisting() {
    final EntityManager em = EMF.getEm();
    em.getTransaction().begin();
    em.createQuery(Utils.DELETE_APPS_QUERY).executeUpdate();
    em.createQuery(Utils.DELETE_USERS_QUERY).executeUpdate();
    em.getTransaction().commit();
    em.close();
  }

  private void populateWithData() {
    for (int i = 0; i < APP_COUNT; i++) {
      AppEntity app = new AppEntity("Test app " + Utils.generateRandomInt(0, 5000));
      apps.add(appFacade.save(app));
    }
    for (int i = 0; i < USER_COUNT; i++) {
      UserEntity user =
          new UserEntity(Utils.generateRandomInt(0, 5000) + "", "test"
              + Utils.generateRandomInt(0, 5000) + "@lapps.com");
      users.add(userFacade.save(user));
    }
  }
}
