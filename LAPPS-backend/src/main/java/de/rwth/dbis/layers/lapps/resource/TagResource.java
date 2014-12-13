package de.rwth.dbis.layers.lapps.resource;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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

import de.rwth.dbis.layers.lapps.domain.AppInstanceFacade;
import de.rwth.dbis.layers.lapps.entity.AppInstanceEntity;
import de.rwth.dbis.layers.lapps.entity.AppTagEntity;

/**
 * Tag resource.
 */
@Api(value = "/tags", description = "Tag resource", hidden = true)
public class TagResource {


  private static final Logger LOGGER = Logger.getLogger(TagResource.class.getName());

  private static AppInstanceFacade appInstanceFacade = AppInstanceFacade.getFacade();


  /**
   * Get all tags for an {@link AppIntanceEntity}.
   * 
   * @param app id
   * @param page number
   * @param pageLength number of tags by page
   * 
   * @return Response with all tags for an {@link AppIntanceEntity} as a JSON array.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all tags for an app", response = AppTagEntity.class,
      responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getAllTags(
      @PathParam("appId") int appId,
      @ApiParam(value = "Page number", required = false) @DefaultValue("1") @QueryParam("page") int page,
      @ApiParam(value = "Number of tags by page", required = false) @DefaultValue("-1") @HeaderParam("pageLength") int pageLength) {
    List<AppInstanceEntity> appEntities =
        (List<AppInstanceEntity>) appInstanceFacade.findByParameter("id", appId);

    if (appEntities.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    }
    AppInstanceEntity appIntance = appEntities.get(0);

    List<AppTagEntity> tagEntities = appIntance.getTags();

    int numberOfPages = 1;
    if (pageLength > 0 && pageLength < appEntities.size()) {
      int fromIndex = page == 1 ? 0 : (page * pageLength) - pageLength;
      int toIndex = page == 1 ? pageLength : page * pageLength;
      numberOfPages = (int) Math.ceil((double) appEntities.size() / pageLength);
      if (appEntities.size() < fromIndex + 1) {
        appEntities.clear();
      } else {
        if (appEntities.size() < toIndex + 1) {
          toIndex = appEntities.size();
        }
        appEntities = appEntities.subList(fromIndex, toIndex);
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
