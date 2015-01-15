package de.rwth.dbis.layers.lapps.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.persistence.EntityExistsException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.rwth.dbis.layers.lapps.authenticate.OIDCAuthentication;
import de.rwth.dbis.layers.lapps.domain.Facade;
import de.rwth.dbis.layers.lapps.entity.Comment;
import de.rwth.dbis.layers.lapps.entity.App;
import de.rwth.dbis.layers.lapps.entity.User;
import de.rwth.dbis.layers.lapps.exception.OIDCException;
import de.rwth.dbis.layers.lapps.resource.HttpStatusCode;

/**
 * Comment resource (exposed at "apps/{id}/comments" path).
 */
@Path("/apps/{id}/comments")
@Api(value = "/apps/{id}/comments", description = "Comment resource")
public class CommentsResource {

  private static final Logger LOGGER = Logger.getLogger(CommentsResource.class.getName());

  private Facade entityFacade = new Facade();


  /**
   * Create an Comment.
   * 
   * @param accessToken openID connect token
   * @param createdComment as JSON
   * 
   * @return newComment as JSON
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Create comment")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.BAD_REQUEST, message = "Content doesn't match "),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.FORBIDDEN, message = "not authorized"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found"),
      @ApiResponse(code = HttpStatusCode.CONFLICT, message = "Already exists"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response createComment(@HeaderParam("accessToken") String accessToken, @ApiParam(
      value = "Comment entity as JSON", required = true) Comment createdComment,
      @PathParam("id") long appId) {

    // Check Authentication
    User user;
    try {
      user = OIDCAuthentication.authenticate(accessToken);
    } catch (OIDCException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }

    try {

      // Get the app
      List<App> apps = entityFacade.findByParam(App.class, "id", appId);
      App app = null;
      if (apps.isEmpty()) {
        return Response.status(HttpStatusCode.NOT_FOUND).build();
      } else {
        app = apps.get(0);
      }

      // Get all comments for the app
      List<Comment> comments = entityFacade.findByParam(Comment.class, "app_id", app.getId());

      // Check user already commented
      for (Comment temp : comments) {
        if (temp.getUser().equals(user)) {
          return Response.status(HttpStatusCode.CONFLICT).build();
        }
      }

      // Sum up rating of all comments
      int ratingSum = createdComment.getRating();
      int counter = 1;
      for (Comment temp : comments) {
        ratingSum += temp.getRating();
        counter++;
      }

      // Update rating of of the app
      double newRating = ratingSum / counter;
      app.setRating(newRating);
      app = entityFacade.save(app);

      // Create Comment
      createdComment.setApp(app);
      createdComment.setUser(user);
      createdComment = entityFacade.save(createdComment);

      // Return comment
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.OK).entity(mapper.writeValueAsBytes(createdComment))
          .build();

    } catch (Exception e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }

  }

  /**
   * 
   * Gets the comment for a given commentId.
   * 
   * @param oidcId
   * @param appId
   * 
   * @return Response with comment as a JSON object.
   * 
   */
  @GET
  @Path("/{cid}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get comment by commentId", response = Comment.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "Comment not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getComment(@PathParam("id") long appId, @PathParam("cid") long commentId) {

    try {
      // Get the comment
      List<Comment> comments = entityFacade.findByParam(Comment.class, "id", commentId);
      Comment comment = null;
      if (comments.isEmpty()) {
        return Response.status(HttpStatusCode.NOT_FOUND).build();
      } else {
        comment = comments.get(0);
      }

      // Return the comment
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.OK).entity(mapper.writeValueAsBytes(comment)).build();
    } catch (Exception e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }


  /**
   * 
   * Delete the comment with the given AppId.
   * 
   * @param AppId
   * @param CommentId
   * 
   * @return Response
   * 
   */
  @DELETE
  @Path("/{cid}")
  @ApiOperation(value = "Delete comment by AppId")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.FORBIDDEN, message = "not authorized"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "comment not found"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response deleteUser(@HeaderParam("accessToken") String accessToken,
      @PathParam("id") long appId, @PathParam("cid") long commentId) {

    // Check authentication
    User user;
    try {
      user = OIDCAuthentication.authenticate(accessToken);
    } catch (OIDCException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }

    try {
      // Get the comment
      List<Comment> comments = entityFacade.findByParam(Comment.class, "id", commentId);
      Comment comment = null;
      if (comments.isEmpty()) {
        return Response.status(HttpStatusCode.NOT_FOUND).build();
      } else {
        comment = comments.get(0);
      }

      // Check, if user is creator of the comment
      if (!(comment.getUser() == user)) {
        // If not, check, if the user has admin rights
        if (!OIDCAuthentication.isAdmin(accessToken)) {
          return Response.status(HttpStatusCode.FORBIDDEN).build();
        }
      }

      // Get the app
      List<App> apps = entityFacade.findByParam(App.class, "id", appId);
      App app = null;
      if (apps.isEmpty()) {
        return Response.status(HttpStatusCode.NOT_FOUND).build();
      } else {
        app = apps.get(0);
      }

      // Sum up rating of all comments
      comments = entityFacade.findByParam(Comment.class, "app_id", app.getId());
      int ratingSum = 0;
      int counter = 0;
      for (Comment temp : comments) {
        ratingSum += temp.getRating();
        counter++;
      }

      // Update rating of of the app
      ratingSum -= comment.getRating();
      counter -= 1;
      double newRating = ratingSum / counter;
      app.setRating(newRating);
      app = entityFacade.save(app);

      // Delete the comment
      entityFacade.deleteByParam(Comment.class, "id", commentId);
      return Response.status(HttpStatusCode.OK).build();

    } catch (Exception e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * 
   * Update the comment with the given AppId.
   * 
   * @param AppId
   * @param updatedComment as JSON
   * 
   * @return Response with updated Comment
   */
  @PUT
  @Path("/{cid}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Update comment by AppId", response = Comment.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.BAD_REQUEST, message = "Content data invalid format"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.FORBIDDEN, message = "not authorized"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "Comment not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response updateComment(@HeaderParam("accessToken") String accessToken,
      @PathParam("id") long appId, @PathParam("cid") long commentId, @ApiParam(
          value = "Comment entity as JSON", required = true) Comment updatedComment) {

    // Check authentication
    User user;
    try {
      user = OIDCAuthentication.authenticate(accessToken);
    } catch (OIDCException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }

    try {
      // Get the comment
      List<Comment> comments = entityFacade.findByParam(Comment.class, "id", commentId);
      Comment comment = null;
      if (comments.isEmpty()) {
        return Response.status(HttpStatusCode.NOT_FOUND).build();
      } else {
        comment = comments.get(0);
      }

      // Check, if user is creator of the comment
      if (!(comment.getUser() == user)) {
        return Response.status(HttpStatusCode.FORBIDDEN).build();
      }

      try {
        // Update Comment
        comment.setText(updatedComment.getText());
        comment.setText(updatedComment.getText());
        comment = entityFacade.save(comment);
      } catch (Exception e) {
        LOGGER.warning(e.getMessage());
        return Response.status(HttpStatusCode.BAD_REQUEST).build();
      }


      try {
        // Return updated comment
        ObjectMapper objectMapper = new ObjectMapper();
        return Response.status(HttpStatusCode.OK).entity(objectMapper.writeValueAsBytes(comment))
            .build();
      } catch (JsonProcessingException e) {
        LOGGER.warning(e.getMessage());
        return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
      }

    } catch (Exception e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * 
   * Get all comments.
   * 
   * @param accessToken
   * @param search query parameter
   * @param page number
   * @param pageLength number of comments by page
   * @param sortBy field
   * @param order asc or desc
   * 
   * @return Response with all comments as JSON array.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all comments", response = Comment.class, responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.BAD_REQUEST, message = "Content data invalid format"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response getAllComments(
      @ApiParam(value = "Search query parameter", required = false) @QueryParam("search") String search,
      @ApiParam(value = "Page number", required = false) @DefaultValue("1") @QueryParam("page") int page,
      @ApiParam(value = "Number of comments by page", required = false) @DefaultValue("-1") @HeaderParam("pageLength") int pageLength,
      @ApiParam(value = "Sort by field", required = false, allowableValues = "date") @DefaultValue("date") @QueryParam("sortBy") String sortBy,
      @ApiParam(value = "Order asc or desc", required = false, allowableValues = "asc,desc") @DefaultValue("asc") @QueryParam("order") String order,
      @PathParam("id") long appId) {

    // Get All Comments
    List<Comment> entities;
    entities = (List<Comment>) entityFacade.loadAll(Comment.class);

    // Get the page
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

    // Return Result
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.NOT_IMPLEMENTED).header("numberOfPages", numberOfPages)
          .entity(mapper.writeValueAsBytes(entities)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }


}
