package de.rwth.dbis.layers.lapps.resource;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import de.rwth.dbis.layers.lapps.data.EMF;
import de.rwth.dbis.layers.lapps.entity.UserEntity;

/**
 * Users resource (exposed at "users" path).
 */
@Path("users")
@Api(value = "users", description = "Example")
public class UsersResource {

  /**
   * Provides a plain text list of all user emails.
   * 
   * @return Response with all users as a JSON array.
   */
  @SuppressWarnings("unchecked")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation("A test operation")
  public Response getAllUsers() {
    final EntityManager em = EMF.getEm();
    em.getTransaction().begin();
    Query query = em.createQuery("select u from UserEntity u");
    ArrayList<UserEntity> entities = (ArrayList<UserEntity>) query.getResultList();
    em.getTransaction().commit();
    em.close();
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(200).entity(mapper.writeValueAsBytes(entities)).build();
    } catch (JsonProcessingException e) {
      // e.printStackTrace(); // TODO have a look at the exception handling
      return Response.status(500).build();
    }
  }
}
