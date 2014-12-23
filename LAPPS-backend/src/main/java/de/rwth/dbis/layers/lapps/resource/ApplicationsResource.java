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

import org.dozer.DozerBeanMapper;

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

/**
 * Applications resource (exposed at "apps" path).
 */
@Path("/apps")
@Api(value = "/apps", description = "Applications resource")
public class ApplicationsResource {

  private static final Logger LOGGER = Logger.getLogger(ApplicationsResource.class.getName());

  private Facade entitiyFacade = new Facade();

  /**
   * Get all apps.
   * 
   * @param search query parameter
   * @param page number
   * @param pageLength number of apps by page
   * @param sortBy field
   * @param order asc or desc
   * @param filterBy field
   * @param filterValue
   * 
   * @return Response with all applications as a JSON array.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all apps", response = App.class, responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getAllApps(
      @ApiParam(value = "Search query parameter for name and tag", required = false) @QueryParam("search") String search,
      @ApiParam(value = "Page number", required = false) @DefaultValue("1") @QueryParam("page") int page,
      @ApiParam(value = "Number of apps by page", required = false) @DefaultValue("-1") @HeaderParam("pageLength") int pageLength,
      @ApiParam(value = "Sort by field", required = false,
          allowableValues = "name,platform,dateCreated,dateModified") @DefaultValue("name") @QueryParam("sortBy") String sortBy,
      @ApiParam(value = "Order asc or desc", required = false, allowableValues = "asc,desc") @DefaultValue("asc") @QueryParam("order") String order,
      @ApiParam(value = "Filter by field", required = false, allowableValues = "platform,creator") @QueryParam("filterBy") String filterBy,
      @ApiParam(value = "Filter value", required = false) @QueryParam("filterValue") String filterValue) {
    List<App> entities;
    if (search == null) {
      entities = (List<App>) entitiyFacade.loadAll(App.class);
    } else {
      entities = (List<App>) entitiyFacade.findByParam(App.class, "name", search);
      // TODO: search for tag
    }

    Collections.sort(entities);
    if (order.equalsIgnoreCase("desc")) {
      Collections.reverse(entities);
      // TODO: check and use sort by field
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
   * @param id app id
   * 
   * @return Response with an app as a JSON object.
   * 
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get app by ID", response = App.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getApp(@PathParam("id") Long id) {

    List<App> apps = entitiyFacade.findByParam(App.class, "id", id);
    App app = null;
    if (apps.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      app = apps.get(0);
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
   * @param accessToken openID connect token
   * @param createdApp app as JSON
   * 
   * @return Response
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Create app", response = App.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response createApp(@HeaderParam("accessToken") String accessToken, @ApiParam(
      value = "App entity as JSON", required = true) App createdApp) {

    // Check, if the user has developer rights
    if (!OIDCAuthentication.isDeveloper(accessToken)) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }
    createdApp = entitiyFacade.save(createdApp);
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.OK).entity(mapper.writeValueAsBytes(createdApp))
          .build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }

  }

  /**
   * 
   * Delete the app with the given id.
   * 
   * @param accessToken openID connect token
   * @param id app id
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
  public Response deleteApp(@HeaderParam("accessToken") String accessToken, @PathParam("id") Long id) {

    // TODO: Check, if the user is creator of the app (also ok)

    // If not, check, if the user has admin rights
    if (!OIDCAuthentication.isAdmin(accessToken)) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }
    List<App> apps = entitiyFacade.findByParam(App.class, "id", id);
    App app = null;
    if (apps.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      app = apps.get(0);
    }
    entitiyFacade.deleteByParam(App.class, "id", app.getId());
    return Response.status(HttpStatusCode.OK).build();
  }

  /**
   * 
   * Update the app with the given id.
   * 
   * @param accessToken openID connect token
   * @param id app id
   * @param updatedApp app as JSON
   * 
   * @return Response
   */
  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Update app by ID", response = App.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response updateApp(@HeaderParam("accessToken") String accessToken,
      @PathParam("id") Long id,
      @ApiParam(value = "App entity as JSON", required = true) App updatedApp) {

    // TODO: Check, if the user is creator of the app (also ok)

    // If not, check, if the user has admin rights
    if (!OIDCAuthentication.isAdmin(accessToken)) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }
    List<App> apps = entitiyFacade.findByParam(App.class, "id", id);
    App app = null;
    if (apps.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      app = apps.get(0);
    }
    DozerBeanMapper dozerMapper = new DozerBeanMapper();
    dozerMapper.map(updatedApp, app);
    app = entitiyFacade.save(app);
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return Response.status(HttpStatusCode.OK).entity(objectMapper.writeValueAsBytes(app)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * 
   * Tag subresource. Leads to {@link TagsResource}.
   * 
   * @return TagsResource
   * 
   */
  @Path("/{appId}/tags")
  public TagsResource getTagsResource() {
    return new TagsResource();
  }

}
