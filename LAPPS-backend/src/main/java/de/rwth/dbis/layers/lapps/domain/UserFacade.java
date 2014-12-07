package de.rwth.dbis.layers.lapps.domain;

import java.util.List;

import javax.persistence.EntityManager;

import de.rwth.dbis.layers.lapps.data.EMF;
import de.rwth.dbis.layers.lapps.entity.AppCommentEntity;
import de.rwth.dbis.layers.lapps.entity.AppInstanceEntity;
import de.rwth.dbis.layers.lapps.entity.AppInstanceRightsEntity;
import de.rwth.dbis.layers.lapps.entity.UserEntity;

/**
 * A business service implementation for {@link UserEntity} instances. Used to handle specific
 * business/domain methods over these instances.
 *
 */
public class UserFacade extends AbstractFacade<UserEntity, Integer> {

  public UserFacade() {
    super(UserEntity.class);
  }

  @Override
  protected EntityManager getEntityManager() {
    return EMF.getEm();
  }

  /**
   * @deprecated Publishes a comment given author's id and the id of the app the comment refers to.
   *             Note that this method first loads user and app with the given IDs so that they are
   *             attached to the current {@link EntityManager}. If user and app instances are
   *             already available better user
   *             {@link UserFacade#comment(String, UserEntity, AppInstanceEntity)}.
   * 
   * @param comment The comment contents
   * @param userId The id of the author
   * @param appId The id of the app the comment refers to
   * @return The newly created {@link AppCommentEntity}
   */
  public AppCommentEntity comment(String comment, int userId, int appId) {
    final EntityManager em = getEntityManager();
    em.getTransaction().begin();
    UserEntity user = em.find(UserEntity.class, userId);
    AppInstanceEntity appInstance = em.find(AppInstanceEntity.class, appId);
    AppCommentEntity c = new AppCommentEntity(comment, user, appInstance);
    // user.addComment(c);
    // app.addComment(c);
    em.persist(user);
    em.getTransaction().commit();
    em.close();
    return c;
  }

  /**
   * Published a comment given author's instance and the instance of the app the comment refers to.
   * 
   * @param comment The comment contents
   * @param user The author
   * @param appInstance The app the comment refers to
   * @return The newly created {@link AppCommentEntity}
   */
  public AppCommentEntity comment(String comment, UserEntity user, AppInstanceEntity appInstance) {
    new AppCommentEntity(comment, user, appInstance);
    UserEntity u = this.save(user);
    List<AppCommentEntity> comments = u.getComments();
    return comments.get(comments.size() - 1);
  }

  /**
   * Finds users by email (or part of it).
   * 
   * @param email (part of) The email of the user
   * @return List of matched users
   */
  public List<UserEntity> findByEmail(String email) {
    return super.findByParameter("email", email);
  }

  /**
   * Finds users by OIDC ID (or part of it).
   * 
   * @param oidcId
   * @return List of matched users
   */
  public List<UserEntity> findByOidcId(Long oidcId) {
    return super.findByParameter("oidcId", oidcId);
  }

  /**
   * Gives a User certain rights over an AppInstanceEntity.
   * 
   * @param user the user to grant the rights to
   * @param withRights the rights to grant
   * @param forAppInstance the AppInstanceEntity which are the rights given for
   * @return persisted User containing the rights given
   */
  public UserEntity authorize(UserEntity user, int withRights, AppInstanceEntity forAppInstance) {
    new AppInstanceRightsEntity(withRights, user, forAppInstance);
    return super.save(user);
  }

}
