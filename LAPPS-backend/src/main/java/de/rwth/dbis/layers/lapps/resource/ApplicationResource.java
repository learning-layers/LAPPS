package de.rwth.dbis.layers.lapps.resource;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
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
import de.rwth.dbis.layers.lapps.domain.AppInstanceFacade;
import de.rwth.dbis.layers.lapps.entity.AppInstanceEntity;
import de.rwth.dbis.layers.lapps.exception.OIDCException;

/**
 * Application resource (exposed at "apps" path). AppInstance refers to AppInstanceRessource.
 */
@Path("/apps")
@Api(value = "/apps", description = "Application resource")
public class ApplicationResource {

  private static final Logger LOGGER = Logger.getLogger(ApplicationResource.class.getName());

  private static AppInstanceFacade appInstanceFacade = AppInstanceFacade.getFacade();

  /**
   * Get all apps.
   * 
   * @param search query parameter
   * @param page number
   * @param pageLength number of apps by page
   * @param sortBy field
   * @param order asc or desc
   * 
   * @return Response with all applications as a JSON array.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all apps", response = AppInstanceEntity.class,
      responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getAllApps(
      @ApiParam(value = "Search query parameter", required = false) @QueryParam("search") String search,
      @ApiParam(value = "Page number", required = false) @DefaultValue("1") @QueryParam("page") int page,
      @ApiParam(value = "Number of apps by page", required = false) @DefaultValue("-1") @HeaderParam("pageLength") int pageLength,
      @ApiParam(value = "Sort by field", required = false, allowableValues = "name") @DefaultValue("name") @QueryParam("sortBy") String sortBy,
      @ApiParam(value = "Order asc or desc", required = false, allowableValues = "asc,desc") @DefaultValue("asc") @QueryParam("order") String order) {
    List<AppInstanceEntity> entities;
    if (search == null) {
      entities = (List<AppInstanceEntity>) appInstanceFacade.findAll();
    } else {
      entities = (List<AppInstanceEntity>) appInstanceFacade.findByName(search);
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
  @ApiOperation(value = "Get app by ID", response = AppInstanceEntity.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getApp(@PathParam("id") int id) {

    AppInstanceEntity app = appInstanceFacade.find(id);
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
   * Create an app.
   * 
   * @param openID connect token
   * @param created app as JSON
   * 
   * @return Response
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Create app", response = AppInstanceEntity.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response createApp(@HeaderParam("accessToken") String accessToken, @ApiParam(
      value = "App entity as JSON", required = true) AppInstanceEntity createdApp) {
    try {
      OIDCAuthentication.authenticate(accessToken);
    } catch (OIDCException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }
    // TODO: create app with help of appFacade
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.NOT_IMPLEMENTED)
          .entity(mapper.writeValueAsBytes(createdApp)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }

  }

  /**
   * 
   * Delete the app with the given id.
   * 
   * @param openID connect token
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

    AppInstanceEntity app = appInstanceFacade.find(id);
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
   * @param openID connect token
   * @param id
   * @param updated app as JSON
   * 
   * @return Response
   */
  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Update app by ID", response = AppInstanceEntity.class)
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
      @ApiParam(value = "App entity as JSON", required = true) AppInstanceEntity updatedApp) {
    try {
      // TODO: Check for admin or user himself rights (not part of the open id authentication
      // process)
      OIDCAuthentication.authenticate(accessToken);
    } catch (OIDCException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }
    AppInstanceEntity app = appInstanceFacade.find(id);
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
