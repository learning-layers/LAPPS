package de.rwth.dbis.layers.lapps.data;

import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Test;

public class EntityManagerTest {
  private final static Logger LOGGER = Logger.getLogger(EntityManagerTest.class.getName());
  private EntityManager em = null;

  @Test
  public void getEntityManager() {
    LOGGER.info("Getting Entity Manager...");
    em = EMF.getEm();
    assertTrue(em instanceof EntityManager);
    assertTrue(em != null);
    LOGGER.info("Got Entity Manager..");
  }

  @After
  public void releaseResources() {
    if (em != null && em.isOpen()) {
      em.close();
    }
  }
}
