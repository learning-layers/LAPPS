package de.rwth.dbis.layers.lapps.domain;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.rwth.dbis.layers.lapps.Utils;
import de.rwth.dbis.layers.lapps.entity.AppCommentEntity;
import de.rwth.dbis.layers.lapps.entity.AppEntity;
import de.rwth.dbis.layers.lapps.entity.AppInstanceEntity;
import de.rwth.dbis.layers.lapps.entity.AppPlatformEntity;
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
    userFacade.deleteAll("email", "@test.com");
    LOGGER.info("User data deleted.");
    LOGGER.info("Creating a new user...");
    user = this.createUser();
    user = userFacade.save(user);
    assertTrue(user.getId() > 0);
    LOGGER.info("User created: " + user);
  }

  private UserEntity createUser() {
    return new UserEntity(Utils.generateRandomInt(0, 5000) + "", "test"
        + Utils.generateRandomInt(0, 5000) + "@test.com");
  }

  @Test
  public void find() {
    UserEntity foundUser = userFacade.find(user.getId());
    LOGGER.info("user loaded: " + foundUser.toString());
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
    AppInstanceEntity appInstance =
        new AppInstanceEntity(this.getPlatform(), "test_app_instance@gdahfk.c");
    app.addInstance(appInstance);
    app = AppFacade.getFacade().save(app);
    AppCommentEntity comment = userFacade.comment("Test comment", user, appInstance);
    assertTrue(comment != null && comment.getId() > 0);
    // LOGGER.info("user's comment: " + comment.getAuthor().getComments().get(0).toString());
    // LOGGER.info("app's comment: " + app.getComments().get(0).toString());
    LOGGER.info("Dummy app and dummy instance created, comment added: " + comment.toString());
  }

  private AppPlatformEntity getPlatform() {
    List<AppPlatformEntity> platforms = AppPlatformFacade.getFacade().findAll();
    assertTrue(platforms.size() > 0);
    return platforms.get(0);
  }

  // @Test
  public void update() {
    UserFacade userFacade = new UserFacade();
    UserEntity foundUser = userFacade.find(user.getId());
    foundUser.setEmail("new@email.com");
    foundUser = userFacade.save(foundUser);
    LOGGER.info("updated user: " + foundUser);
  }

  @After
  public void releaseResources() {
    AppFacade.getFacade().deleteAll("name", "Test app ");
  }
}
