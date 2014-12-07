package de.rwth.dbis.layers.lapps.domain;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.rwth.dbis.layers.lapps.data.EMF;
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
  private static Logger LOGGER = Logger.getLogger(ArtifactFacade.class.getName());

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
    T managed = null;
    LOGGER.info("Attempting to save a(n) " + entityClass.getName() + "...");
    try {
      em.getTransaction().begin();
      // em.persist(entity);
      managed = em.merge(entity);
      em.flush();
      em.getTransaction().commit();
      em.clear();
    } catch (Throwable t) {
      LOGGER.log(Level.SEVERE, "Exception while saving: " + t.getMessage());
      em.getTransaction().rollback();
      throw t;
    } finally {
      em.close();
    }
    LOGGER.info("Saved: " + managed);
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
    T entity = null;
    LOGGER.info("Attempting to find a(n) " + entityClass.getName() + "...");
    try {
      em.getTransaction().begin();
      entity = getEntityManager().find(entityClass, id);
      em.getTransaction().commit();
    } catch (Throwable t) {
      LOGGER.log(Level.SEVERE, "Exception while searching for an entity: " + t.getMessage());
      em.getTransaction().rollback();
      throw t;
    } finally {
      em.close();
    }
    LOGGER.info("Found: " + entity);
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
    List<T> entities = null;
    // Session session = em.unwrap(Session.class);
    // Criteria crit = session.createCriteria(entityClass);
    // try {
    // session.getTransaction().begin();
    // entities =
    // crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).add(Restrictions.gt("id", 0))
    // .list();
    // session.getTransaction().commit();
    // } catch (Throwable t) {
    // session.getTransaction().rollback();
    // throw t;
    // } finally {
    // em.close();
    // // session.close();
    // }
    // return entities;

    LOGGER.info("Attempting to find all " + entityClass.getName() + " instances...");
    try {
      em.getTransaction().begin();
      // e parameter is the entity class itself
      Query query = em.createQuery("select e from " + entityClass.getSimpleName() + " e");
      entities = query.getResultList();
      em.getTransaction().commit();
    } catch (Throwable t) {
      LOGGER.log(Level.SEVERE, "Exception while obtaining all entities: " + t.getMessage());
      em.getTransaction().rollback();
      throw t;
    } finally {
      em.close();
    }
    LOGGER.info("Found: " + entities.size() + " " + entityClass.getName() + "entities.");
    return entities;
  }

  @SuppressWarnings("unchecked")
  protected List<T> doFindByParameter(EntityManager em, Query query) {
    List<T> entities = null;
    try {
      em.getTransaction().begin();
      entities = query.getResultList();
      em.getTransaction().commit();
    } catch (Throwable t) {
      LOGGER.log(Level.SEVERE,
          "Exception while searching for an entity by a paramter: " + t.getMessage());
      em.getTransaction().rollback();
      throw t;
    } finally {
      em.close();
    }
    return entities;
  }

  /**
   * Lists all {@link Entity} instances filtered by a certain parameter. For Strings, this will
   * return all instances that match the given substring (case-insensitive). Numerical data types
   * have to match fully.
   * 
   * @param param The field to filter on
   * @param value The value of the field filtering on
   * @return List of Entities
   */
  public final List<T> findByParameter(String param, Object value) {
    final EntityManager em = getEntityManager();
    List<T> entities = null;
    LOGGER.info("Searching for " + entityClass.getName() + " by a parameter...");
    value = value instanceof String ? "%" + value + "%" : value;
    try {
      Query query =
          em.createQuery(
              "select entity from " + entityClass.getSimpleName() + " entity where entity." + param
                  + " like :value").setParameter("value", value);
      entities = this.doFindByParameter(em, query);
    } catch (IllegalArgumentException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
      throw e;
    }
    LOGGER.info("Found: " + entities.size() + " " + entityClass.getName() + "entities.");
    return entities;
  }

  /**
   * Lists all {@link Entity} instances filtered by a certain parameter. For Strings, this will
   * return all instances that match the given substring (case-insensitive). Numerical data types
   * have to match fully.
   * 
   * @param param The field to filter on
   * @param value The value of the field filtering on
   * @param page The number of the page in the result set
   * @param pageLength Page size
   * @param isOrderedAsceding Order ascending or descending
   * @return List of Entities
   */
  public List<T> findByParameter(String param, Object value, int page, int pageLength,
      boolean isOrderedAsceding) {
    if (page < 1) {
      throw new IllegalArgumentException("page parameter must be positive integer!");
    }
    if (pageLength < 1) {
      throw new IllegalArgumentException("pageLength parameter must be positive integer!");
    }
    int limitStart = (page - 1) * pageLength;
    // int limitEnd = limitStart + pageLength;
    final EntityManager em = getEntityManager();
    List<T> entities = null;
    LOGGER.info("Searching for " + entityClass.getName() + " by a parameter...");
    value = value instanceof String ? "%" + value + "%" : value;
    try {
      Query query =
          em.createQuery(
              "select entity from " + entityClass.getSimpleName() + " entity where entity." + param
                  + " like :value order by entity." + param
                  + (isOrderedAsceding ? " asc" : " desc")).setParameter("value", value)
              .setFirstResult(limitStart).setMaxResults(pageLength);
      entities = this.doFindByParameter(em, query);
    } catch (IllegalArgumentException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
      throw e;
    }
    LOGGER.info("Found: " + entities.size() + " " + entityClass.getName() + "entities.");
    return entities;
  }

  private void doDelete(EntityManager em, Query query) {
    try {
      em.getTransaction().begin();
      final int count = query.executeUpdate();
      em.getTransaction().commit();
      LOGGER.info(count + " entities deleted.");
    } catch (Throwable t) {
      LOGGER.log(Level.SEVERE, "Exception while deleting entities: " + t.getMessage());
      em.getTransaction().rollback();
      throw t;
    } finally {
      em.close();
    }
  }

  /**
   * Deletes all {@link Entity} instances of the {@link #entityClass} type, filtered by a certain
   * parameter. For Strings, this will delete all instances containing the given substring
   * (%substring%). Numerical data types have to match fully.
   * 
   * @param param The field that should match
   * @param value The value of the field matching on
   * 
   */
  public void deleteAll(String param, Object value) {
    final EntityManager em = getEntityManager();
    Query query = null;
    if (value instanceof String) {
      value = (String) "%" + value + "%";
    }
    if (param != null && value != null) {
      try {
        query =
            em.createQuery(
                "delete from " + entityClass.getSimpleName() + " entity where entity." + param
                    + " like :value").setParameter("value", value);
        LOGGER
            .info("Deleting " + entityClass.getName() + " with " + param + " == " + value + "...");
        this.doDelete(em, query);
      } catch (IllegalArgumentException e) {
        LOGGER.log(Level.SEVERE, e.getMessage());
        throw e;
      }
    }
  }

  /**
   * Deletes all entities from the {@link #entityClass} type.
   */
  public void deleteAll() {
    final EntityManager em = getEntityManager();
    Query query = null;
    try {
      query = em.createQuery("delete from " + entityClass.getName());
      LOGGER.info("Deleting all " + entityClass.getName() + "...");
      this.doDelete(em, query);
    } catch (IllegalArgumentException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
      throw e;
    }
  }


  /**
   * Finds entities from {@link Entity} subtype filtered by the expression given with
   * <code>queryStr</code>.
   * 
   * @param queryStr The JPQL Query string
   * @return List of Entities
   */
  @SuppressWarnings("unchecked")
  public List<T> findByQuery(String queryStr) {
    List<T> entities = null;
    final EntityManager em = EMF.getEm();
    Query query = em.createQuery(queryStr, entityClass);
    try {
      em.getTransaction().begin();
      entities = query.getResultList();
      em.getTransaction().commit();
    } catch (Throwable t) {
      LOGGER.log(Level.SEVERE, "Exception while fetching: " + t.getMessage());
      em.getTransaction().rollback();
      throw t;
    } finally {
      em.close();
    }
    return entities;
  }
}
