package de.rwth.dbis.layers.lapps.data;

import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Test;

public class EntityManagerTest {
  private EntityManager em = null;

  @Test
  public void getEntityManager() {
    em = EMF.getEm();
    assertTrue(em instanceof EntityManager);
    assertTrue(em != null);
  }

  @After
  public void releaseResources() {
    if (em != null && em.isOpen()) {
      em.close();
    }
  }
}
