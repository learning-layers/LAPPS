package de.rwth.dbis.layers.lapps.domain;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Test;

import de.rwth.dbis.layers.lapps.data.EntityManagerTest;
import de.rwth.dbis.layers.lapps.entity.AppCommentEntity;
import de.rwth.dbis.layers.lapps.entity.AppEntity;
import de.rwth.dbis.layers.lapps.entity.UserEntity;

/**
 * Test class for User Facade business objects.
 * 
 */
public class UserFacadeTest {
  private UserFacade userFacade = new UserFacade();
  private final static Logger LOGGER = Logger.getLogger(EntityManagerTest.class.getName());

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
    UserEntity user = userFacade.findAll().get(0);
    AppFacade appFacade = AppFacade.getFacade();
    AppEntity app = appFacade.findAll().get(0);
    AppCommentEntity comment = userFacade.comment("Aaaaand... another one!", user, app);
    assertTrue(comment != null && comment.getId() > 0);
    LOGGER.info(comment.toString());
  }

  @After
  public void releaseResources() {
    EntityManager em = userFacade.getEntityManager();
    if (em != null && em.isOpen()) {
      em.close();
    }
  }
}
