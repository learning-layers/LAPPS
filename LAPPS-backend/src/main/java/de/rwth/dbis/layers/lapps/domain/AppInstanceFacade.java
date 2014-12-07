package de.rwth.dbis.layers.lapps.domain;

import java.util.List;

import javax.persistence.EntityManager;

import de.rwth.dbis.layers.lapps.data.EMF;
import de.rwth.dbis.layers.lapps.entity.AppInstanceEntity;
import de.rwth.dbis.layers.lapps.entity.Entity;

public class AppInstanceFacade extends AbstractFacade<AppInstanceEntity, Integer> implements Entity {
  private static final long serialVersionUID = -6661663546406136419L;
  private static AppInstanceFacade instance = new AppInstanceFacade();

  private AppInstanceFacade() {
    super(AppInstanceEntity.class);
  }

  public static AppInstanceFacade getFacade() {
    return instance;
  }

  @Override
  protected EntityManager getEntityManager() {
    return EMF.getEm();
  }

  /**
   * Finds an application by its name or part of the name.
   * 
   * @param name (part of ) The name of the app
   * @return List of matched AppEntities
   */
  public List<AppInstanceEntity> findByName(String name) {
    return super.findByParameter("name", name);
  }

}
