package de.rwth.dbis.layers.lapps.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.rwth.dbis.layers.lapps.entity.Entity;

/**
 * Generic business service for CRUD operations over {@link Entity} instances. Designed to be used
 * by all business service implementations (facades). The term 'business service' is used as to
 * represent the functionality of an EJB, since the environment does not allow for the usage of the
 * latter.
 *
 * @param <T> Entity type
 * @param <I> Identity type
 */
public abstract class AbstractFacade<T extends Entity, I> {

  /**
   * The type (class) of the concrete {@link Entity} extending this abstract business service.
   */
  private final transient Class<T> entityClass;

  public AbstractFacade(final Class<T> entityType) {
    this.entityClass = entityType;
  }

  /**
   * Returns the {@link EntityManager} associated to the current business service (facade).
   * 
   * @return {@link EntityManager}
   */
  protected abstract EntityManager getEntityManager();

  /**
   * Persists an {@link Entity} (possibly) together with child entities. Note that merge returns
   * managed Entity, while the entity used as argument for the merge method stays unmanaged. When
   * using persist, however, the entity stays managed. On the other hand, persist is not possible
   * for cascading insert when the father entity exists and a child must be inserted - e.g. set
   * comment to user and save the user. This is why we use merge and return the duplicated (managed)
   * entity.
   * 
   * @param entity The Entity to save
   * @return Managed copy of the Entity to save
   */
  public T save(final T entity) {
    final EntityManager em = getEntityManager();
    em.getTransaction().begin();
    // em.persist(entity);
    T managed = em.merge(entity);
    em.flush();
    em.getTransaction().commit();
    em.close();
    return managed;
  }

  /**
   * Loads an existing {@link Entity} and (possibly) inner Entities.
   * 
   * @param id Entity identificator. Usually Integer or Long.
   * @return the Entity
   */
  public final T find(final I id) {
    final EntityManager em = getEntityManager();
    em.getTransaction().begin();
    T entity = getEntityManager().find(entityClass, id);
    em.getTransaction().commit();
    em.close();
    return entity;
  }

  /**
   * Lists all {@link Entity} instances specified by the {@link AbstractFacade#entityClass}.
   * 
   * @return List of Entities
   */
  @SuppressWarnings("unchecked")
  public final List<T> findAll() {
    final EntityManager em = getEntityManager();
    em.getTransaction().begin();
    Query query = em.createQuery("select u from " + entityClass.getSimpleName() + " u");
    ArrayList<T> entities = (ArrayList<T>) query.getResultList();
    em.getTransaction().commit();
    em.close();
    return entities;
  }
}
