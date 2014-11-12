package de.rwth.dbis.layers.lapps.data;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.Test;

import de.rwth.dbis.layers.lapps.entity.UserEntity;

public class EntityManagerTest {
  private EntityManager em = null;

  @Test
  public void loadUserById() {
    final int id = 1;
    em = EMF.getEm();
    em.getTransaction().begin();
    UserEntity user = em.find(UserEntity.class, id);
    System.out.println("User email is " + user.getEmail());
    em.getTransaction().commit();
    em.close();
  }

  @After
  public void releaseResources() {
    if (em != null && em.isOpen()) {
      em.close();
    }
  }
}
