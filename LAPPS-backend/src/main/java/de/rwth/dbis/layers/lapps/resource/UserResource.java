package de.rwth.dbis.layers.lapps.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.rwth.dbis.layers.lapps.domain.UserFacade;
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
   * @return Response with user as a JSON object.
   * 
   */
  @SuppressWarnings("unchecked")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUser(@PathParam("id") int id) {
    /**
     * After I excuse myself to touch a code who I did not write, I would kindly suggest a similar
     * way of querying as the one shown below. Reason is that it is considered a good practise to
     * abstract from the lower-level operations as data-layer access. Moreover, ideally (but not
     * guaranteed J), we can then use the same 'business' methods with another data provider
     * implementation.
     */

    // final EntityManager em = EMF.getEm();
    // em.getTransaction().begin();
    // Query query = em.createQuery("select u from UserEntity u where u.id=" + id);
    // ArrayList<UserEntity> entities = (ArrayList<UserEntity>) query.getResultList();
    // em.getTransaction().commit();
    // em.close();
    UserFacade userFacade = new UserFacade();
    UserEntity user = userFacade.find(id);
    if (user == null) {
      return Response.status(404).build();
    }
    // if (entities.size() == 0)
    // return Response.status(404).build();
    // if (entities.size() != 1)
    // return Response.status(500).build();
    // UserEntity user = entities.get(0);
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(200).entity(mapper.writeValueAsBytes(user)).build();
    } catch (JsonProcessingException e) {
      // e.printStackTrace(); // TODO have a look at the exception handling
      return Response.status(500).build();
    }
  }
}
