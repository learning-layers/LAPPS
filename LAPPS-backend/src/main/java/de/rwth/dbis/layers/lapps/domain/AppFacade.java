package de.rwth.dbis.layers.lapps.domain;

import javax.persistence.EntityManager;

import de.rwth.dbis.layers.lapps.data.EMF;
import de.rwth.dbis.layers.lapps.entity.AppEntity;

public class AppFacade extends AbstractFacade<AppEntity, Integer> {
  private static AppFacade instance = null;

  private AppFacade() {
    super(AppEntity.class);
  }

  @Override
  protected EntityManager getEntityManager() {
    return EMF.getEm();
  }

  public static AppFacade getFacade() {
    if (instance == null) {
      instance = new AppFacade();
    }
    return instance;
  }

}
