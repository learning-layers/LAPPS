package de.rwth.dbis.layers.lapps.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
import de.rwth.dbis.layers.lapps.entity.App.DateCreatedComparator;
import de.rwth.dbis.layers.lapps.entity.App.DateModifiedComparator;
import de.rwth.dbis.layers.lapps.entity.App.PlatformComparator;
import de.rwth.dbis.layers.lapps.entity.App.RatingComparator;
import de.rwth.dbis.layers.lapps.entity.Artifact;
import de.rwth.dbis.layers.lapps.entity.Tag;
import de.rwth.dbis.layers.lapps.entity.User;

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
          allowableValues = "name,platform,rating,dateCreated,dateModified,random") @DefaultValue("name") @QueryParam("sortBy") String sortBy,
      @ApiParam(value = "Order asc or desc", required = false, allowableValues = "asc,desc") @DefaultValue("asc") @QueryParam("order") String order,
      @ApiParam(
          value = "Filter by field : platform, creator, minRating, minDateCreated ,minDateModified. Multiple fields can be used with ; as separator.",
          required = false) @QueryParam("filterBy") String filterBy, @ApiParam(
          value = "Filter value. When using multiple filters, values can be separated by ; ",
          required = false) @QueryParam("filterValue") String filterValue) {
    List<App> entities;
    if (search == null) {
      entities = (List<App>) entitiyFacade.loadAll(App.class);
    } else {
      entities = (List<App>) entitiyFacade.findByParam(App.class, "name", search);
      List<Tag> tagEntities = (List<Tag>) entitiyFacade.findByParam(Tag.class, "value", search);
      for (Tag tag : tagEntities) {
        App app = tag.getApp();
        if (!entities.contains(app)) {
          entities.add(app);
        }
      }
    }

    // filter before sorting
    if (filterBy != null && filterValue != null) {
      String[] filters = filterBy.split(";");
      String[] values = filterValue.split(";");

      int min = Math.min(values.length, filters.length); // we need pairs, so if the user did not
                                                         // provide equal amount of key/value pairs,
                                                         // take the minimum.
      for (int i = 0; i < min; i++) {
        filters[i] = filters[i].toLowerCase();
        boolean isLong = false;
        long longVal = 0;
        try {
          longVal = Long.parseLong(values[i]);
          isLong = true;
        } catch (NumberFormatException e) {
          isLong = false;
        }

        boolean isDouble = false;
        double doubleVal = 0;
        try {
          doubleVal = Double.parseDouble(values[i]);
          isDouble = true;
        } catch (NumberFormatException e) {
          isDouble = false;
        }

        switch (filters[i]) {
          case "platform":
            for (Iterator<App> iterator = entities.iterator(); iterator.hasNext();) {
              App app = iterator.next();
              if (!app.getPlatform().equalsIgnoreCase(values[i])) {// if not specified platform,
                                                                   // remove
                iterator.remove();
              }
            }
            break;
          case "creator":
            for (Iterator<App> iterator = entities.iterator(); iterator.hasNext();) {
              App app = iterator.next();
              if (!isLong
                  && (app.getCreator() == null || !app.getCreator().getUsername()
                      .equalsIgnoreCase(values[i]))) {
                // if user asks for name and name does not match
                iterator.remove();
              } else if (isLong
                  && (app.getCreator() == null || app.getCreator().getOidcId() != longVal)) {
                // if user asks for id
                iterator.remove();
              }
            }
            break;
          case "minrating":
            if (isDouble) { // don't bother if filter value is not a double
              for (Iterator<App> iterator = entities.iterator(); iterator.hasNext();) {
                App app = iterator.next();
                if (app.getRating() < doubleVal) {
                  iterator.remove();
                }
              }
            }

            break;
          case "mindatecreated":
            if (isLong) { // don't bother if filter value is not a long
              for (Iterator<App> iterator = entities.iterator(); iterator.hasNext();) {
                App app = iterator.next();
                if (app.getDateCreated().getTime() < longVal) { // if too old, remove
                  iterator.remove();
                }
              }
            }
            break;

          case "mindatemodified":
            if (isLong) { // don't bother if filter value is not a long
              for (Iterator<App> iterator = entities.iterator(); iterator.hasNext();) {
                App app = iterator.next();
                if (app.getDateModified().getTime() < longVal) { // if too old, remove
                  iterator.remove();
                }
              }
            }
            break;
          default:
            break;
        }
      }
    }

    if (sortBy.equalsIgnoreCase("name")) {
      Collections.sort(entities);
    } else if (sortBy.equalsIgnoreCase("platform")) {
      Collections.sort(entities, new PlatformComparator());
    } else if (sortBy.equalsIgnoreCase("rating")) {
      Collections.sort(entities, new RatingComparator());
    } else if (sortBy.equalsIgnoreCase("dateCreated")) {
      Collections.sort(entities, new DateCreatedComparator());
    } else if (sortBy.equalsIgnoreCase("dateModified")) {
      Collections.sort(entities, new DateModifiedComparator());
    } else if (sortBy.equalsIgnoreCase("random")) { // just shuffle
      Collections.shuffle(entities);
    }
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
          message = "Internal server problems")})
  public Response createApp(@HeaderParam("accessToken") String accessToken, @ApiParam(
      value = "App entity as JSON", required = true) App createdApp) {

    // Check, if the user has developer rights
    if (!OIDCAuthentication.isDeveloper(accessToken)) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }
    createdApp.deleteId();

    // save child elements
    User creator = createdApp.getCreator();
    List<Artifact> artifacts = new ArrayList<Artifact>(createdApp.getArtifacts());
    List<Tag> tags = new ArrayList<Tag>(createdApp.getTags());

    // delete child elements from app
    createdApp.getArtifacts().clear();
    createdApp.getTags().clear();

    // validate the existence of the creator
    List<User> creatorList = entitiyFacade.findByParam(User.class, "oidcId", creator.getOidcId());
    if (creatorList.size() != 1) {
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
    creator = creatorList.get(0);
    if (creator.getRole() != User.DEVELOPER && creator.getRole() != User.ADMIN) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build(); // TODO error description
    }
    createdApp.setCreator(creator);

    createdApp = entitiyFacade.save(createdApp);

    for (Artifact newArtifact : artifacts) {
      newArtifact.setBelongingTo(createdApp);
      entitiyFacade.save(newArtifact);
    }
    for (Tag newTag : tags) {
      newTag.setApp(createdApp);
      entitiyFacade.save(newTag);
    }

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
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found")})
  public Response deleteApp(@HeaderParam("accessToken") String accessToken, @PathParam("id") Long id) {

    List<App> apps = entitiyFacade.findByParam(App.class, "id", id);
    App app = null;
    if (apps.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      app = apps.get(0);
    }
    // Check, if user is creator of the app
    if (!OIDCAuthentication.isCreatorOfApp(app, accessToken)) {
      // If not, check, if the user has admin rights
      if (!OIDCAuthentication.isAdmin(accessToken)) {
        return Response.status(HttpStatusCode.UNAUTHORIZED).build();
      }
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
          message = "Internal server problems")})
  public Response updateApp(@HeaderParam("accessToken") String accessToken,
      @PathParam("id") Long id,
      @ApiParam(value = "App entity as JSON", required = true) App updatedApp) {

    List<App> apps = entitiyFacade.findByParam(App.class, "id", id);
    App app = null;
    if (apps.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      app = apps.get(0);
    }
    // Check, if user is creator of app
    if (!OIDCAuthentication.isCreatorOfApp(app, accessToken)) {
      // If not, check, if the user has admin rights
      if (!OIDCAuthentication.isAdmin(accessToken)) {
        return Response.status(HttpStatusCode.UNAUTHORIZED).build();
      }
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
   * Get all platform versions for an app.
   * 
   * @param appId app id
   * 
   * @return Response with all platform versions as a JSON array.
   * 
   */
  @GET
  @Path("/{appId}/platforms")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all platform versions for an app", response = App.class,
      responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getAllPlatformsForApp(@PathParam("appId") Long appId) {

    // find app
    List<App> apps = entitiyFacade.findByParam(App.class, "id", appId);
    App app = null;
    if (apps.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      app = apps.get(0);
    }

    // find all platform versions for the app
    List<App> entities = entitiyFacade.findByParam(App.class, "name", app.getName());

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
