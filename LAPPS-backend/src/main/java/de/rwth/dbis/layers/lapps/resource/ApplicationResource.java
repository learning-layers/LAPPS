package de.rwth.dbis.layers.lapps.resource;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import de.rwth.dbis.layers.lapps.domain.AppFacade;
import de.rwth.dbis.layers.lapps.entity.AppEntity;
import de.rwth.dbis.layers.lapps.exception.OIDCException;

/**
 * Application resource (exposed at "apps" path).
 */
@Path("/apps")
@Api(value = "/apps", description = "Application resource")
public class ApplicationResource {

  private static final Logger LOGGER = Logger.getLogger(ApplicationResource.class.getName());

  private static AppFacade appFacade = AppFacade.getFacade();

  /**
   * Get all apps.
   * 
   * @return Response with all applications as a JSON array.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all apps", response = AppEntity.class, responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getAllApps(
      @ApiParam(value = "Search query parameter", required = false) @QueryParam("search") String search) {
    List<AppEntity> entities;

    if (search == null) {
      entities = (List<AppEntity>) appFacade.findAll();
    } else {
      entities = (List<AppEntity>) appFacade.findByName(search);
    }

    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.OK).entity(mapper.writeValueAsBytes(entities)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * 
   * Gets the app for a given id.
   * 
   * @param id
   * 
   * @return Response with an app as a JSON object.
   * 
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get app by ID", response = AppEntity.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getApp(@PathParam("id") int id) {

    AppEntity app = appFacade.find(id);
    if (app == null) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.OK).entity(mapper.writeValueAsBytes(app)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * 
   * Delete the app with the given id.
   * 
   * @param id
   * 
   * @return Response
   */
  @DELETE
  @Path("/{id}")
  @ApiOperation(value = "Delete app by ID")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response deleteApp(@HeaderParam("accessToken") String accessToken, @PathParam("id") int id) {
    try {
      // TODO: Check for admin or user himself rights (not part of the open id authentication
      // process)
      OIDCAuthentication.authenticate(accessToken);
    } catch (OIDCException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }

    AppEntity app = appFacade.find(id);
    if (app == null) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    }
    // TODO: delete app with help of appFacade
    return Response.status(HttpStatusCode.NOT_IMPLEMENTED).build();
  }

  /**
   * 
   * Update the app with the given id.
   * 
   * @param id
   * @param updatedApp as JSON
   * 
   * @return Response
   */
  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Update app by ID", response = AppEntity.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response updateApp(@HeaderParam("accessToken") String accessToken,
      @PathParam("id") int id,
      @ApiParam(value = "App entity as JSON", required = true) AppEntity updatedApp) {
    try {
      // TODO: Check for admin or user himself rights (not part of the open id authentication
      // process)
      OIDCAuthentication.authenticate(accessToken);
    } catch (OIDCException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }
    AppEntity app = appFacade.find(id);
    if (app == null) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    }
    // TODO: update app with help of appFacade
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.NOT_IMPLEMENTED)
          .entity(mapper.writeValueAsBytes(updatedApp)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }

}
