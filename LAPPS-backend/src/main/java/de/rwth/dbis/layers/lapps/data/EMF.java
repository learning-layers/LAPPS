package de.rwth.dbis.layers.lapps.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMF {
  private static EntityManagerFactory emf;

  public static EntityManager getEm() {
    if (emf == null) {
      emf = Persistence.createEntityManagerFactory("lappsPU");
    }
    return emf.createEntityManager();
  }
}
