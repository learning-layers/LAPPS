package de.rwth.dbis.layers.lapps.resource;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.rwth.dbis.layers.lapps.domain.UserFacade;
import de.rwth.dbis.layers.lapps.entity.UserEntity;

/**
 * User resource (exposed at "/users" path).
 */
@Path("/users")
@Api(value = "/users", description = "User ressource")
public class UserResource {

  private static UserFacade userFacade = new UserFacade();
  private final static Logger LOGGER = Logger.getLogger(UserResource.class.getName());

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
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Find user by ID", response = UserEntity.class)
  @ApiResponses(value = {@ApiResponse(code = 404, message = "User not found")})
  public Response getUser(@PathParam("id") int id) {
    UserEntity user = userFacade.find(id);
    if (user == null)
      return Response.status(404).build();
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(200).entity(mapper.writeValueAsBytes(user)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(500).build();
    }
  }
}
