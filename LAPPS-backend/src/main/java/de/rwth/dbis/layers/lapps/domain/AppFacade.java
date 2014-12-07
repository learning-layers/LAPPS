package de.rwth.dbis.layers.lapps.domain;

import java.util.List;

import javax.persistence.EntityManager;

import de.rwth.dbis.layers.lapps.data.EMF;
import de.rwth.dbis.layers.lapps.entity.AppEntity;
import de.rwth.dbis.layers.lapps.entity.AppInstanceEntity;
import de.rwth.dbis.layers.lapps.entity.AppInstanceRightsEntity;
import de.rwth.dbis.layers.lapps.entity.AppPlatformEntity;
import de.rwth.dbis.layers.lapps.entity.UserEntity;

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

  /**
   * Finds an application by its name or part of the name.
   * 
   * @param name (part of ) The name of the app
   * @return List of matched AppEntities
   */
  public List<AppEntity> findByName(String name) {
    return super.findByParameter("name", name);
  }

  public AppInstanceEntity createAppInstance(UserEntity creator, AppEntity instanceOf,
      AppPlatformEntity onPlatform, String url) {
    AppInstanceEntity inst = new AppInstanceEntity(onPlatform, url);
    inst.addRights(new AppInstanceRightsEntity(AppInstanceRightsEntity.CREATE, creator, inst));
    inst.setApp(instanceOf);
    this.save(instanceOf);
    return inst;
  }
}
