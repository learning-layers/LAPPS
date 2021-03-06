package de.rwth.dbis.layers.lapps.domain;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.rwth.dbis.layers.lapps.data.EMF;
import de.rwth.dbis.layers.lapps.entity.Entity;

public class Facade {
  private static Logger LOGGER = Logger.getLogger(Facade.class.getName());
  private EntityManager em = null;

  public Facade() {}

  public <E extends Entity> E save(E entity) {
    em = EMF.getEm();
    E managed = null;
    LOGGER.info("Saving " + entity.getClass().getName() + "...");
    try {
      em.getTransaction().begin();
      managed = em.merge(entity);
      em.getTransaction().commit();
    } catch (Throwable t) {
      LOGGER.log(Level.SEVERE, "Exception while saving: " + t.getMessage());
      if (em.isOpen() && em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw t;
    } finally {
      em.close();
    }
    LOGGER.info("Saved: " + managed);
    return managed;
  }

  public <E extends Entity> int deleteByParam(Class<E> clazz, String paramName, Object paramValue) {
    em = EMF.getEm();
    Query query = null;
    int count = 0;
    LOGGER.info("Deleting all " + clazz.getName() + " with " + paramName + " == " + paramValue
        + "...");
    paramValue = paramValue instanceof String ? "%" + paramValue + "%" : paramValue;
    try {
      query =
          em.createQuery("delete from " + clazz.getName() + " e where e." + paramName + " like :v")
              .setParameter("v", paramValue);
      em.getTransaction().begin();
      count = query.executeUpdate();
      em.getTransaction().commit();
    } catch (Throwable t) {
      LOGGER.log(Level.SEVERE, t.getMessage());
      if (em.isOpen() && em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw t;
    } finally {
      em.close();
    }
    LOGGER.info("Deleted: " + count + " " + clazz.getName() + " entity/ies");
    return count;
  }

  public <E extends Entity> int deleteAll(Class<E> clazz) {
    em = EMF.getEm();
    Query query = null;
    int count = 0;
    LOGGER.info("Deleting all " + clazz.getName() + "...");
    try {
      query = em.createQuery("delete from " + clazz.getName());
      em.getTransaction().begin();
      count = query.executeUpdate();
      em.getTransaction().commit();
    } catch (Throwable t) {
      LOGGER.log(Level.SEVERE, t.getMessage());
      if (em.isOpen() && em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw t;
    } finally {
      em.close();
    }
    LOGGER.info("Deleted: " + count + " " + clazz.getName() + " entity/ies");
    return count;
  }

  /**
   * Returns a list of entities that contains the value paramValue of the field paramName.
   * 
   * @param clazz
   * @param paramName
   * @param paramValue
   * @return the entity list
   */
  @SuppressWarnings("unchecked")
  public <E extends Entity> List<E> findByParam(Class<E> clazz, String paramName, Object paramValue) {
    em = EMF.getEm();
    List<E> entities = null;
    LOGGER.info("Searching for all " + clazz.getName() + " instances with " + paramName + " == "
        + paramValue + "...");
    paramValue = paramValue instanceof String ? "%" + paramValue + "%" : paramValue;
    try {
      em.getTransaction().begin();
      Query query =
          em.createQuery(
              "select e from " + clazz.getSimpleName() + " e where e." + paramName + " like :v")
              .setParameter("v", paramValue);
      entities = query.getResultList();
      em.getTransaction().commit();
    } catch (Throwable t) {
      LOGGER.log(Level.SEVERE, "Exception while obtaining all entities: " + t.getMessage());
      if (em.isOpen() && em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw t;
    } finally {
      em.close();
    }
    LOGGER.info("Found: " + entities.size() + " " + clazz.getName() + "entity/ies");
    return entities;
  }

  @SuppressWarnings("unchecked")
  public <E extends Entity> List<E> loadAll(Class<E> clazz) {
    em = EMF.getEm();
    List<E> entities = null;
    LOGGER.info("Searching for all " + clazz.getName() + " instances...");
    try {
      em.getTransaction().begin();
      Query query = em.createQuery("select e from " + clazz.getSimpleName() + " e");
      entities = query.getResultList();
      em.getTransaction().commit();
    } catch (Throwable t) {
      LOGGER.log(Level.SEVERE, "Exception while obtaining all entities: " + t.getMessage());
      if (em.isOpen() && em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw t;
    } finally {
      em.close();
    }
    LOGGER.info("Found: " + entities.size() + " " + clazz.getName() + "entity/ies");
    return entities;
  }
}
