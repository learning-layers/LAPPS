package de.rwth.dbis.layers.lapps.domain;

import javax.persistence.EntityManager;

import de.rwth.dbis.layers.lapps.data.EMF;
import de.rwth.dbis.layers.lapps.entity.AppPlatformEntity;

public class AppPlatformFacade extends AbstractFacade<AppPlatformEntity, Integer> {
  private static AppPlatformFacade instance = null;

  private AppPlatformFacade() {
    super(AppPlatformEntity.class);
  }

  @Override
  protected EntityManager getEntityManager() {
    return EMF.getEm();
  }

  public static AppPlatformFacade getFacade() {
    if (instance == null) {
      instance = new AppPlatformFacade();
    }
    return instance;
  }

}
