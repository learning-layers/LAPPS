package de.rwth.dbis.layers.lapps.domain;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.rwth.dbis.layers.lapps.data.EMF;
import de.rwth.dbis.layers.lapps.entity.AppInstanceEntity;
import de.rwth.dbis.layers.lapps.entity.AppInstanceOverview;
import de.rwth.dbis.layers.lapps.entity.Entity;

public class AppInstanceFacade extends AbstractFacade<AppInstanceEntity, Integer> implements Entity {
  private static final long serialVersionUID = -6661663546406136419L;
  private static AppInstanceFacade instance = new AppInstanceFacade();
  private static Logger LOGGER = Logger.getLogger(AppInstanceFacade.class.getName());

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
   * Create a new AppInstanceEntity with the given User as creator
   * 
   * @param creator the owner of the AppInstanceEntity
   * @param onPlatform the platform which the AppInstanceEntity runs on
   * @param url the url for the AppInstanceEntity
   * @return persisted AppInstanceEntity
   * 
   *         public AppInstanceEntity createAppInstance(UserEntity creator, AppEntity instanceOf,
   *         AppPlatformEntity onPlatform, String url) { // AppInstanceEntity inst = new
   *         AppInstanceEntity(onPlatform, url); // inst.addRights(new
   *         AppInstanceRightsEntity(AppInstanceRightsEntity.CREATE, creator, inst)); //
   *         inst.setApp(instanceOf); // return this.save(inst); // }
   */

  /**
   * Finds an application by its name or part of the name.
   * 
   * @param name (part of ) The name of the app
   * @return List of matched AppEntities
   */
  public List<AppInstanceEntity> findByName(String name) {
    return super.findByParameter("name", name);
  }

  @SuppressWarnings("unchecked")
  public List<AppInstanceOverview> findAllPreview(int platformId) {
    final EntityManager em = this.getEntityManager();
    List<AppInstanceOverview> entities = null;
    try {
      em.getTransaction().begin();
      Query query =
          em.createQuery(
              "select new "
                  + AppInstanceOverview.class.getName()
                  + "(inst.id, app.name, inst.rating, artifact.url, description.contents, platform.name, management.user.email) from AppInstanceEntity inst inner join inst.artifacts as artifact inner join inst.platform as platform inner join inst.details as description inner join inst.app as app inner join inst.rights as management where platform.id = :platformId and artifact.artifactType = 2 and description.type.id = 1 and bitwiseAnd(management.rights, 1) = 1")
              .setParameter("platformId", platformId);
      LOGGER.info("Searching for all AppInstanceEntities in Preview mode...");
      entities = query.getResultList();
      em.getTransaction().commit();
      LOGGER.info("Found " + entities.size() + " entities!");
    } catch (IllegalArgumentException e) {
      // not really needed...
      em.getTransaction().rollback();
      LOGGER.log(Level.SEVERE, e.getMessage());
      throw e;
    } finally {
      em.close();
    }
    return entities;
  }

}
