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

import de.rwth.dbis.layers.lapps.authenticate.OIDCAuthentication;
import de.rwth.dbis.layers.lapps.domain.Facade;
import de.rwth.dbis.layers.lapps.entity.App;
import de.rwth.dbis.layers.lapps.entity.User;

/**
 * Users resource (exposed at "users" path).
 */
@Path("/users")
@Api(value = "/users", description = "User resource")
public class UsersResource {

  private static final Logger LOGGER = Logger.getLogger(UsersResource.class.getName());

  private static Facade userFacade = new Facade();

  /**
   * 
   * Get all users.
   * 
   * @param accessToken openID connect token
   * @param search query parameter
   * @param page number
   * @param pageLength number of users by page
   * @param sortBy field
   * @param order asc or desc
   * @param filterBy field
   * @param filterValue
   * 
   * @return Response with all users as JSON array.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all users", response = User.class, responseContainer = "List")
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
      @ApiParam(value = "Order asc or desc", required = false, allowableValues = "asc,desc") @DefaultValue("asc") @QueryParam("order") String order,
      @ApiParam(value = "Filter by field", required = false, allowableValues = "role") @DefaultValue("role") @QueryParam("filterBy") String filterBy,
      @ApiParam(value = "Filter value", required = false) @QueryParam("filterValue") String filterValue) {
    // Check, if the user has admin rights
    if (!OIDCAuthentication.isAdmin(accessToken)) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }

    List<User> entities;
    if (search == null) {
      entities = (List<User>) userFacade.loadAll(User.class);
    } else {
      entities = (List<User>) userFacade.loadAll(User.class);
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
   * Gets the user for a given oidcId.
   * 
   * @param oidcId
   * 
   * @return Response with user as a JSON object.
   * 
   */
  @GET
  @Path("/{oidcId}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get user by oidcId", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getUser(@PathParam("oidcId") Long oidcId) {

    // search for existing user
    List<User> entities = userFacade.findByParam(User.class, "oidcId", oidcId);
    User user = null;
    if (entities.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      user = entities.get(0);
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
   * Delete the user with the given oidcId.
   * 
   * @param accessToken openID connect token
   * @param oidcId
   * 
   * @return Response
   * 
   */
  @DELETE
  @Path("/{oidcId}")
  @ApiOperation(value = "Delete user by oidcId")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response deleteUser(@HeaderParam("accessToken") String accessToken,
      @PathParam("oidcId") Long oidcId) {
    // Check, if the user has admin rights
    if (!OIDCAuthentication.isAdmin(accessToken)) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }
    // search for existing user
    List<User> entities = userFacade.findByParam(User.class, "oidcId", oidcId);
    if (entities.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      // UserEntity user = entities.get(0);
    }
    // TODO: delete user with help of userFacade
    return Response.status(HttpStatusCode.NOT_IMPLEMENTED).build();
  }

  /**
   * 
   * Update the user with the given oidcId.
   * 
   * @param accessToken openID connect token
   * @param oidcId
   * @param updatedUser as JSON
   * 
   * @return Response with updated User
   */
  @PUT
  @Path("/{oidcId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Update user by oidcId", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response updateUser(@HeaderParam("accessToken") String accessToken,
      @PathParam("oidcId") Long oidcId,
      @ApiParam(value = "User entity as JSON", required = true) User updatedUser) {

    // TODO: Check if the user is himself (also ok)

    // If not, check, if the user has admin rights
    if (!OIDCAuthentication.isAdmin(accessToken)) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }
    // search for existing user
    List<User> entities = userFacade.findByParam(User.class, "oidcId", oidcId);
    if (entities.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      // UserEntity user = entities.get(0);
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

  /**
   * 
   * Get all apps for the user with the given oidcId.
   * 
   * @param oidcId
   * @param page number
   * @param pageLength number of users by page
   * 
   * @return Response with all apps as JSON array.
   * 
   */
  @GET
  @Path("/{oidcId}/apps")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all apps for an user", response = App.class,
      responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getAppsForUser(
      @PathParam("oidcId") long oidcId,
      @ApiParam(value = "Page number", required = false) @DefaultValue("1") @QueryParam("page") int page,
      @ApiParam(value = "Number of users by page", required = false) @DefaultValue("-1") @HeaderParam("pageLength") int pageLength) {
    return new ApplicationResource().getAllApps(null, page, pageLength, null, null, "Creator",
        String.valueOf(oidcId));
  }

}
