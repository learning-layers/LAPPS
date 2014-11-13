package de.rwth.dbis.layers.lapps.resource;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import de.rwth.dbis.layers.lapps.data.EMF;
import de.rwth.dbis.layers.lapps.entity.UserEntity;

/**
 * User resource (exposed at "users" path).
 */
@Path("users")
public class UserResource {

  /**
   * Provides a plain text list of all user emails.
   * 
   * @return String with all user emails.
   */
  @SuppressWarnings("unchecked")
  @GET
  @Produces("text/plain")
  public String getAllUsers() {
    final EntityManager em = EMF.getEm();
    em.getTransaction().begin();
    Query query = em.createQuery("select u from UserEntity u");
    ArrayList<UserEntity> entities = (ArrayList<UserEntity>) query.getResultList();
    em.getTransaction().commit();
    em.close();
    String str = "";
    for (UserEntity e : entities) {
      str += "user: " + e.toString() + "\r\n";
    }
    return str;
  }
}
