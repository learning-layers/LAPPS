package de.rwth.dbis.layers.lapps.domain;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.rwth.dbis.layers.lapps.Utils;
import de.rwth.dbis.layers.lapps.data.EMF;
import de.rwth.dbis.layers.lapps.entity.AppCommentEntity;
import de.rwth.dbis.layers.lapps.entity.AppEntity;
import de.rwth.dbis.layers.lapps.entity.UserEntity;

/**
 * Test class for User Facade business objects. <b>Do not delete or modify user with id of 1!</b>
 * 
 */
public class UserFacadeTest {
  private UserFacade userFacade = new UserFacade();
  private UserEntity user = null;
  private final static Logger LOGGER = Logger.getLogger(UserFacadeTest.class.getName());

  @Before
  public void init() {
    LOGGER.info("Deleting user data...");
    final EntityManager em = EMF.getEm();
    em.getTransaction().begin();
    em.createQuery(Utils.DELETE_USERS_QUERY).executeUpdate();
    em.getTransaction().commit();
    em.close();
    LOGGER.info("User data deleted.");
    LOGGER.info("Creating a new user...");
    user = this.createUser();
    user = userFacade.save(user);
    assertTrue(user.getId() > 0);
    LOGGER.info("User created: " + user);
  }

  private UserEntity createUser() {
    return new UserEntity(Utils.generateRandomInt(0, 5000) + "", "test"
        + Utils.generateRandomInt(0, 5000) + "@lapps.com");
  }

  @Test
  public void find() {
    UserEntity user = userFacade.find(1);
    LOGGER.info("user loaded: " + user.toString());
  }

  @Test
  public void findAll() {
    ArrayList<UserEntity> users = (ArrayList<UserEntity>) userFacade.findAll();
    LOGGER.info(users.size() + " users loaded");
  }

  @Test
  public void comment() {
    LOGGER.info("Creating a test comment...");
    AppEntity app = new AppEntity("Test app " + Utils.generateRandomInt(0, 5000));
    app = AppFacade.getFacade().save(app);
    AppCommentEntity comment = userFacade.comment("Test comment", user, app);
    assertTrue(comment != null && comment.getId() > 0);
    // LOGGER.info("user's comment: " + comment.getAuthor().getComments().get(0).toString());
    // LOGGER.info("app's comment: " + app.getComments().get(0).toString());
    LOGGER.info("Dummy app created, comment added: " + comment.toString());
  }

  // TODO: Implement
  @Test
  public void grandRights() {

  }

  // @Test
  public void update() {
    UserFacade userFacade = new UserFacade();
    UserEntity user = userFacade.find(1);
    user.setEmail("new@email.com");
    user = userFacade.save(user);
    LOGGER.info("updated user: " + user);
  }

  @After
  public void releaseResources() {
    final EntityManager em = EMF.getEm();
    em.getTransaction().begin();
    em.createQuery("delete from AppEntity").executeUpdate();
    em.getTransaction().commit();
    em.close();
  }
}
