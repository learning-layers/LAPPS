package de.rwth.dbis.layers.lapps.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * A utility class for obtaining a {@link EntityManager} in a container not supporting Injection.
 *
 */
public class EMF {
  private static EntityManagerFactory emf = null;

  /**
   * Obtain an {@link EntityManager} out of a single static {@link EntityManagerFactory} instance.
   *
   * @return {@link EntityManager}
   */
  public static EntityManager getEm() {
    if (emf == null) {
      emf = Persistence.createEntityManagerFactory("lappsPU");
    }
    return emf.createEntityManager();
  }
}
