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

  private static UserFacade userFacade = new UserFacade();

  /**
   * 
   * Gets the user for a given id.
   * 
   * @param id
   * 
   * @return Response with user as a JSON object.
   * 
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUser(@PathParam("id") int id) {
    UserEntity user = userFacade.find(id);
    if (user == null)
      return Response.status(404).build();
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(200).entity(mapper.writeValueAsBytes(user)).build();
    } catch (JsonProcessingException e) {
      // e.printStackTrace(); // TODO have a look at the exception handling
      return Response.status(500).build();
    }
  }
}
