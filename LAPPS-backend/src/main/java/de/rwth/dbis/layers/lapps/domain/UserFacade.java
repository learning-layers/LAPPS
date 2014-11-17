package de.rwth.dbis.layers.lapps.domain;

import javax.persistence.EntityManager;

import de.rwth.dbis.layers.lapps.data.EMF;
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

}
