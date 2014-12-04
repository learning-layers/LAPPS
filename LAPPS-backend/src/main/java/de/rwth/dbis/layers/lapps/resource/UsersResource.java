package de.rwth.dbis.layers.lapps.resource;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.rwth.dbis.layers.lapps.authenticate.AuthenticationProvider;
import de.rwth.dbis.layers.lapps.domain.UserFacade;
import de.rwth.dbis.layers.lapps.entity.UserEntity;
import de.rwth.dbis.layers.lapps.exception.OIDCException;

/**
 * Users resource (exposed at "users" path).
 */
@Path("/users")
@Api(value = "/users", description = "User ressource")
public class UsersResource {

  private static final Logger LOGGER = Logger.getLogger(UsersResource.class.getName());

  private static UserFacade userFacade = new UserFacade();

  /**
   * 
   * Get all users.
   * 
   * @param accessToken
   * @param search query parameter
   * @param page number
   * @param pageLength number of users by page
   * @param sortBy field
   * @param order asc or desc
   * 
   * @return Response with all users as JSON array.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all users")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getAllUsers(
      @HeaderParam("accessToken") String accessToken,
      @ApiParam(value = "Search query parameter", required = false) @QueryParam("search") String search,
      @ApiParam(value = "Page number", required = false) @DefaultValue("1") @QueryParam("page") int page,
      @ApiParam(value = "Number of users by page", required = false) @DefaultValue("-1") @HeaderParam("pageLength") int pageLength,
      @ApiParam(value = "Sort by field", required = false, allowableValues = "email") @DefaultValue("email") @QueryParam("sortBy") String sortBy,
      @ApiParam(value = "Order asc or desc", required = false, allowableValues = "asc,desc") @DefaultValue("asc") @QueryParam("order") String order) {
    try {
      AuthenticationProvider.authenticate(accessToken);
      // TODO: Check for admin rights (not part of the open id authentication process)
    } catch (OIDCException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }

    List<UserEntity> entities;
    if (search == null) {
      entities = (List<UserEntity>) userFacade.findAll();
    } else {
      entities = (List<UserEntity>) userFacade.findByEmail(search);
    }

    Collections.sort(entities);
    if (order.equalsIgnoreCase("desc")) {
      Collections.reverse(entities);
    }

    int numberOfPages = 1;
    if (pageLength > 0 && pageLength < entities.size()) {
      int fromIndex = page == 1 ? 0 : (page * pageLength) - pageLength;
      int toIndex = page == 1 ? pageLength : page * pageLength;
      numberOfPages = (int) Math.ceil((double) entities.size() / pageLength);
      if (entities.size() < fromIndex + 1) {
        entities.clear();
      } else {
        if (entities.size() < toIndex + 1) {
          toIndex = entities.size();
        }
        entities = entities.subList(fromIndex, toIndex);
      }
    }

    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.OK).header("numberOfPages", numberOfPages)
          .entity(mapper.writeValueAsBytes(entities)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }

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
  @ApiOperation(value = "Get user by ID", response = UserEntity.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getUser(@PathParam("id") int id) {

    UserEntity user = userFacade.find(id);
    if (user == null) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.OK).entity(mapper.writeValueAsBytes(user)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * 
   * Delete the user with the given id.
   * 
   * @param id
   * 
   * @return Response
   */
  @DELETE
  @Path("/{id}")
  @ApiOperation(value = "Delete user by ID")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response deleteUser(@HeaderParam("accessToken") String accessToken, @PathParam("id") int id) {
    try {
      // TODO: Check for admin or user himself rights (not part of the open id authentication
      // process)
      AuthenticationProvider.authenticate(accessToken);
    } catch (OIDCException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }

    UserEntity user = userFacade.find(id);
    if (user == null) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    }
    // TODO: delete user with help of userFacade
    return Response.status(HttpStatusCode.NOT_IMPLEMENTED).build();
  }

  /**
   * 
   * Update the user with the given id.
   * 
   * @param id
   * @param updatedUser as JSON
   * 
   * @return Response
   */
  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Update user by ID", response = UserEntity.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response updateUser(@HeaderParam("accessToken") String accessToken,
      @PathParam("id") int id,
      @ApiParam(value = "User entity as JSON", required = true) UserEntity updatedUser) {
    try {
      // TODO: Check for admin or user himself rights (not part of the open id authentication
      // process)
      AuthenticationProvider.authenticate(accessToken);
    } catch (OIDCException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }
    UserEntity user = userFacade.find(id);
    if (user == null) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    }
    // TODO: update user with help of userFacade
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.NOT_IMPLEMENTED)
          .entity(mapper.writeValueAsBytes(updatedUser)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }
}
