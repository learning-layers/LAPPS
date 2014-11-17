package de.rwth.dbis.layers.lapps.resource;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.rwth.dbis.layers.lapps.data.EMF;
import de.rwth.dbis.layers.lapps.entity.UserEntity;

/**
 * User resource (exposed at "users/{id}" path).
 */
@Path("users/{id}")
public class UserResource {

  /**
   * 
   * Gets the user for a given id.
   * 
   * @param id
   * 
   * @return The user as a JSON object.
   * 
   */
  @SuppressWarnings("unchecked")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUser(@PathParam("id") int id) {
    final EntityManager em = EMF.getEm();
    em.getTransaction().begin();
    Query query = em.createQuery("select u from UserEntity u where u.id=" + id);
    ArrayList<UserEntity> entities = (ArrayList<UserEntity>) query.getResultList();
    em.getTransaction().commit();
    em.close();
    if (entities.size() == 0)
      return Response.status(404).build();
    if (entities.size() != 1)
      return Response.status(500).build();
    JsonObject returnObject =
        Json.createObjectBuilder().add("User", entities.get(0).toString()).build();
    return Response.status(200).entity(returnObject).build();
  }

}
