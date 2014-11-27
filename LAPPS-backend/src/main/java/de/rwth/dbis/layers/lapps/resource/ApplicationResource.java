package de.rwth.dbis.layers.lapps.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

import de.rwth.dbis.layers.lapps.domain.AppFacade;
import de.rwth.dbis.layers.lapps.entity.AppEntity;

/**
 * Application resource (exposed at "apps" path).
 */
@Path("/apps")
@Api(value = "/apps", description = "Application resource")
public class ApplicationResource {

  private static final Logger LOGGER = Logger.getLogger(ApplicationResource.class.getName());

  private static AppFacade appFacade = AppFacade.getFacade();

  /**
   * Provides a list of application Ids known to this server.
   * 
   * @return Response with all applications as a JSON array.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get list of all applications")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems"),
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message")})
  public Response getAllApps() {
    List<AppEntity> entities = (List<AppEntity>) appFacade.findAll();
    ArrayList<Integer> appIds = new ArrayList<Integer>();
    Iterator<AppEntity> appIt = entities.iterator();
    while (appIt.hasNext()) {
      appIds.add(appIt.next().getId());
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.OK).entity(mapper.writeValueAsBytes(appIds)).build();
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
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems"),
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message")})
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

}
