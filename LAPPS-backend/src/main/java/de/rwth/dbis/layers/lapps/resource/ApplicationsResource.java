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
import de.rwth.dbis.layers.lapps.exception.OIDCException;

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
      @ApiParam(
          value = "Search query parameter for name and tag. Multiple keywords use ; as separator.",
          required = false) @QueryParam("search") String search,
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
    if (search == null || search.isEmpty()) {
      entities = (List<App>) entitiyFacade.loadAll(App.class);
    } else {
      entities = new ArrayList<App>();
      String[] searchStrings = search.split(";");
      for (String searchString : searchStrings) {
        searchString = searchString.trim();
        for (App app : (List<App>) entitiyFacade.findByParam(App.class, "name", searchString)) {
          if (!entities.contains(app)) {
            entities.add(app);
          }
        }
        List<Tag> tagEntities =
            (List<Tag>) entitiyFacade.findByParam(Tag.class, "value", searchString);
        for (Tag tag : tagEntities) {
          App app = tag.getApp();
          if (!entities.contains(app)) {
            entities.add(app);
          }
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
                  && (app.getCreator() == null || !app.getCreator().getUsername().toLowerCase()
                      .contains(values[i].toLowerCase()))) {
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
      @ApiResponse(code = HttpStatusCode.CREATED, message = "App successful created"),
      @ApiResponse(code = HttpStatusCode.BAD_REQUEST,
          message = "Missing or incorrect fields in provided app entity"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response createApp(@HeaderParam("accessToken") String accessToken, @ApiParam(
      value = "App entity as JSON", required = true) App createdApp) {

    // Authenticate user and set as the application creator
    User creator;
    try {
      creator = OIDCAuthentication.authenticate(accessToken);
      if (creator.getRole() < User.DEVELOPER) {
        throw new OIDCException();
      }
    } catch (OIDCException e) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }

    // Delete / overwrite not wanted stuff
    createdApp.deleteId();
    createdApp.setCreator(creator);
    // Initial rating of three
    createdApp.setRating(3.0);
    // Set date created to null (set by database)
    createdApp.setDateCreated(null);
    // Update date is set here
    long time = System.currentTimeMillis();
    java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
    createdApp.setDateModified(timestamp);

    // Sanity checks
    if (createdApp.getPlatform() == null || createdApp.getName() == null
        || createdApp.getDownloadUrl() == null || createdApp.getVersion() == null
        || createdApp.getLongDescription() == null || createdApp.getShortDescription() == null) {
      return Response.status(HttpStatusCode.BAD_REQUEST).build();
    }

    // Check for correct platform entry
    boolean correctEntry = false;
    String platform = createdApp.getPlatform();
    for (int i = 0; i < App.PLATFORMS.length; i++) {
      if (platform.equals(App.PLATFORMS[i])) {
        correctEntry = true;
        break;
      }
    }
    if (!correctEntry) {
      return Response.status(HttpStatusCode.BAD_REQUEST).build();
    }

    // Check for existing thumbnail
    correctEntry = false;
    Iterator<Artifact> artifactIterator = createdApp.getArtifacts().iterator();
    while (artifactIterator.hasNext()) {
      if (artifactIterator.next().getType().equals("thumbnail")) {
        correctEntry = true;
        break;
      }
    }
    if (!correctEntry) {
      return Response.status(HttpStatusCode.BAD_REQUEST).build();
    }

    // Interim storage of artifacts and tags
    List<Artifact> artifacts = new ArrayList<Artifact>(createdApp.getArtifacts());
    List<Tag> tags = new ArrayList<Tag>(createdApp.getTags());

    // Delete child elements from application (persistence issues otherwise)
    createdApp.getArtifacts().clear();
    createdApp.getTags().clear();

    // Save the application
    createdApp = entitiyFacade.save(createdApp);

    // Store the artifacts and tags separately
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
      return Response.status(HttpStatusCode.CREATED).entity(mapper.writeValueAsBytes(createdApp))
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
      @ApiResponse(code = HttpStatusCode.NO_CONTENT, message = "App successful deleted"),
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
    return Response.status(HttpStatusCode.NO_CONTENT).build();
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
      @ApiResponse(code = HttpStatusCode.OK, message = "App successful updated"),
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

    // Rating not allowed to change
    updatedApp.setRating(app.getRating());
    // Developer not allowed to change
    updatedApp.setCreator(app.getCreator());
    // Release date not subject to change
    updatedApp.setDateCreated(app.getDateCreated());
    // Update date is set here
    long time = System.currentTimeMillis();
    java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
    updatedApp.setDateModified(timestamp);
    dozerMapper.map(updatedApp, app);

    app.getArtifacts().clear();
    app.getTags().clear();

    app = entitiyFacade.save(app);

    // backup child elements
    List<Artifact> artifacts = new ArrayList<Artifact>(updatedApp.getArtifacts());
    List<Tag> tags = new ArrayList<Tag>(updatedApp.getTags());

    // delete child elements from app
    entitiyFacade.deleteByParam(Artifact.class, "belongingTo", app);
    entitiyFacade.deleteByParam(Tag.class, "app", app);

    // save child elements
    for (Artifact newArtifact : artifacts) {
      newArtifact.setBelongingTo(app);
      entitiyFacade.save(newArtifact);
    }
    for (Tag newTag : tags) {
      newTag.setApp(app);
      entitiyFacade.save(newTag);
    }


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
