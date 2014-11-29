package de.rwth.dbis.layers.lapps.data;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * A utility class for obtaining a {@link EntityManager} in a container not supporting Injection.
 *
 */
public class EMF {
  private static Logger LOGGER = Logger.getLogger(EMF.class.getName());
  private static EntityManagerFactory emf = null;

  /**
   * Obtain an {@link EntityManager} out of a single static {@link EntityManagerFactory} instance.
   *
   * @return {@link EntityManager}
   */
  public static EntityManager getEm() {
    if (emf == null) {
      LOGGER.info("Entity Manager Factory has just beeen created...");
      emf = Persistence.createEntityManagerFactory("lappsPU");
    }
    return emf.createEntityManager();
  }
}
