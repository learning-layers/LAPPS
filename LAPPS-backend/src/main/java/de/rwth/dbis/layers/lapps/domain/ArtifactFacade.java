package de.rwth.dbis.layers.lapps.domain;

import javax.persistence.EntityManager;

import de.rwth.dbis.layers.lapps.data.EMF;
import de.rwth.dbis.layers.lapps.entity.ArtifactTypeEntity;

public class ArtifactFacade extends AbstractFacade<ArtifactTypeEntity, Integer> {

  private static ArtifactFacade instance = null;

  private ArtifactFacade() {
    super(ArtifactTypeEntity.class);
  }

  @Override
  protected EntityManager getEntityManager() {
    return EMF.getEm();
  }

  public static ArtifactFacade getFacade() {
    if (instance == null) {
      instance = new ArtifactFacade();
    }
    return instance;
  }

}
