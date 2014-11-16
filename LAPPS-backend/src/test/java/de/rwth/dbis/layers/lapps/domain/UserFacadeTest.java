package de.rwth.dbis.layers.lapps.domain;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Test;

import de.rwth.dbis.layers.lapps.data.EntityManagerTest;
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

  @After
  public void releaseResources() {
    EntityManager em = userFacade.getEntityManager();
    if (em != null && em.isOpen()) {
      em.close();
    }
  }
}
