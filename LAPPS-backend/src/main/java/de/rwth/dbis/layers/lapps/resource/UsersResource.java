package de.rwth.dbis.layers.lapps.resource;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.rwth.dbis.layers.lapps.data.EMF;
import de.rwth.dbis.layers.lapps.entity.UserEntity;

/**
 * Users resource (exposed at "users" path).
 */
@Path("users")
public class UsersResource {

  /**
   * Provides a plain text list of all user emails.
   * 
   * @return String with all user emails.
   */
  @SuppressWarnings("unchecked")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public ArrayList<UserEntity> getAllUsers() {
    final EntityManager em = EMF.getEm();
    em.getTransaction().begin();
    Query query = em.createQuery("select u from UserEntity u");
    ArrayList<UserEntity> entities = (ArrayList<UserEntity>) query.getResultList();
    em.getTransaction().commit();
    em.close();

    return entities;
  }
}
