package de.rwth.dbis.layers.lapps.resource;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
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
import de.rwth.dbis.layers.lapps.entity.Tag;

/**
 * Tag resource.
 */
@Api(value = "/tags", description = "Tags resource", hidden = true)
public class TagsResource {

  private static final Logger LOGGER = Logger.getLogger(TagsResource.class.getName());

  private Facade entitiyFacade = new Facade();

  /**
   * Get all tags for an {@link App}.
   * 
   * @param appId app id
   * @param page page number number
   * @param pageLength number of tags by page
   * 
   * @return Response with all tags for an {@link App} as a JSON array.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all tags for an app", response = Tag.class, responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getAllTags(
      @PathParam("appId") long appId,
      @ApiParam(value = "Page number", required = false) @DefaultValue("1") @QueryParam("page") int page,
      @ApiParam(value = "Number of tags by page", required = false) @DefaultValue("-1") @HeaderParam("pageLength") int pageLength) {

    List<App> apps = entitiyFacade.findByParam(App.class, "id", appId);
    if (apps.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      App app = apps.get(0);
      List<Tag> tagEntities = app.getTags();

      int numberOfPages = 1;
      if (pageLength > 0 && pageLength < tagEntities.size()) {
        int fromIndex = page == 1 ? 0 : (page * pageLength) - pageLength;
        int toIndex = page == 1 ? pageLength : page * pageLength;
        numberOfPages = (int) Math.ceil((double) tagEntities.size() / pageLength);
        if (tagEntities.size() < fromIndex + 1) {
          tagEntities.clear();
        } else {
          if (tagEntities.size() < toIndex + 1) {
            toIndex = tagEntities.size();
          }
          tagEntities = tagEntities.subList(fromIndex, toIndex);
        }
      }

      ObjectMapper mapper = new ObjectMapper();
      try {
        return Response.status(HttpStatusCode.OK).header("numberOfPages", numberOfPages)
            .entity(mapper.writeValueAsBytes(tagEntities)).build();
      } catch (JsonProcessingException e) {
        LOGGER.warning(e.getMessage());
        return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
      }
    }
  }

  /**
   * Create a tag for an {@link App}
   * 
   * @param appId app id
   * @param createdTag tag to create as JSON
   * 
   * @return Response
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Create tag for an app", response = Tag.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response createTag(@PathParam("appId") long appId, @ApiParam(value = "Tag entity as JSON",
      required = true) Tag createdTag) {

    List<App> apps = entitiyFacade.findByParam(App.class, "id", appId);
    if (apps.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      App app = apps.get(0);
      createdTag.setApp(app);
      entitiyFacade.save(createdTag);

      try {
        ObjectMapper mapper = new ObjectMapper();
        return Response.status(HttpStatusCode.OK).entity(mapper.writeValueAsBytes(createdTag))
            .build();
      } catch (JsonProcessingException e) {
        LOGGER.warning(e.getMessage());
        return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
      }
    }
  }

  /**
   * Delete the tag with the given id for an {@link App}
   * 
   * @param accessToken openID connect token
   * @param appId app id
   * @param id tag id
   * 
   * @return Response
   * 
   */
  @DELETE
  @Path("/{id}")
  @ApiOperation(value = "Delete tag with id from app")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App or tag of app not found")})
  public Response deleteTag(@HeaderParam("accessToken") String accessToken,
      @PathParam("appId") long appId, @PathParam("id") long id) {

    // Check for admin status
    if (!OIDCAuthentication.isAdmin(accessToken)) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }

    List<App> apps = entitiyFacade.findByParam(App.class, "id", appId);
    if (apps.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      App app = apps.get(0);
      List<Tag> tagEntities = app.getTags();
      Tag tag = null;
      for (Tag tagTmp : tagEntities) {
        if (tagTmp.getId() == id) {
          tag = tagTmp;
          break;
        }
      }

      if (tag == null) {
        return Response.status(HttpStatusCode.NOT_FOUND).build();
      } else {
      }
      entitiyFacade.deleteByParam(Tag.class, "id", tag.getId());
      return Response.status(HttpStatusCode.OK).build();
    }
  }
}
