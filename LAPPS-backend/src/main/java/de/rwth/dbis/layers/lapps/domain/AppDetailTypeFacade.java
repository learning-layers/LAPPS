package de.rwth.dbis.layers.lapps.domain;

import javax.persistence.EntityManager;

import de.rwth.dbis.layers.lapps.data.EMF;
import de.rwth.dbis.layers.lapps.entity.AppDetailTypeEntity;

public class AppDetailTypeFacade extends AbstractFacade<AppDetailTypeEntity, Integer> {
  private static AppDetailTypeFacade instance = null;

  private AppDetailTypeFacade() {
    super(AppDetailTypeEntity.class);
  }

  @Override
  protected EntityManager getEntityManager() {
    return EMF.getEm();
  }

  public static AppDetailTypeFacade getFacade() {
    if (instance == null) {
      instance = new AppDetailTypeFacade();
    }
    return instance;
  }

}
