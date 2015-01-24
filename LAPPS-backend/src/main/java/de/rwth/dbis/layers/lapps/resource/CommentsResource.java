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
import de.rwth.dbis.layers.lapps.entity.Comment;
import de.rwth.dbis.layers.lapps.entity.User;

/**
 * Comments resource.
 */
@Api(value = "/comments", description = "Comments resource", hidden = true)
public class CommentsResource {

  private static final Logger LOGGER = Logger.getLogger(CommentsResource.class.getName());

  private Facade entityFacade = new Facade();

  /**
   * 
   * Get all comments for an {@link App}.
   * 
   * @param accessToken
   * @param search query parameter
   * @param page number
   * @param pageLength number of comments by page
   * @param sortBy field
   * @param order asc or desc
   * 
   * @return Response with all comments for an {@link App} as JSON array.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all comments for an app", response = Comment.class,
      responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.BAD_REQUEST, message = "Content data invalid format"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getAllComments(
      @PathParam("appId") long appId,
      @ApiParam(value = "Page number", required = false) @DefaultValue("1") @QueryParam("page") int page,
      @ApiParam(value = "Number of comments by page", required = false) @DefaultValue("-1") @HeaderParam("pageLength") int pageLength,
      @ApiParam(value = "Sort by field", required = false, allowableValues = "date") @DefaultValue("date") @QueryParam("sortBy") String sortBy,
      @ApiParam(value = "Order asc or desc", required = false, allowableValues = "asc,desc") @DefaultValue("asc") @QueryParam("order") String order) {

    List<App> apps = entityFacade.findByParam(App.class, "id", appId);
    if (apps.size() != 1) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      App app = apps.get(0);
      List<Comment> commentEntities = entityFacade.findByParam(Comment.class, "app", app);

      if (sortBy.equalsIgnoreCase("date")) {
        Collections.sort(commentEntities);
      }
      if (order.equalsIgnoreCase("desc")) {
        Collections.reverse(commentEntities);
      }

      // Get the page
      int numberOfPages = 1;
      if (pageLength > 0 && pageLength < commentEntities.size()) {
        int fromIndex = page == 1 ? 0 : (page * pageLength) - pageLength;
        int toIndex = page == 1 ? pageLength : page * pageLength;
        numberOfPages = (int) Math.ceil((double) commentEntities.size() / pageLength);
        if (commentEntities.size() < fromIndex + 1) {
          commentEntities.clear();
        } else {
          if (commentEntities.size() < toIndex + 1) {
            toIndex = commentEntities.size();
          }
          commentEntities = commentEntities.subList(fromIndex, toIndex);
        }
      }

      // Return Result
      try {
        ObjectMapper mapper = new ObjectMapper();
        return Response.status(HttpStatusCode.OK).header("numberOfPages", numberOfPages)
            .entity(mapper.writeValueAsBytes(commentEntities)).build();
      } catch (JsonProcessingException e) {
        LOGGER.warning(e.getMessage());
        return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
      }
    }
  }

  /**
   * 
   * Get comment for an app by commentId.
   * 
   * @param appId app id
   * @param CommentId comment id
   * 
   * @return Response with comment as a JSON object.
   * 
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get comment for an app", response = Comment.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "Comment not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getComment(@PathParam("appId") long appId, @PathParam("id") long commentId) {

    try {
      // Get the comment
      List<Comment> comments = entityFacade.findByParam(Comment.class, "id", commentId);
      Comment comment = null;
      if (comments.isEmpty()) {
        return Response.status(HttpStatusCode.NOT_FOUND).build();
      } else {
        comment = comments.get(0);
        if (comment.getApp().getId() != appId) {
          return Response.status(HttpStatusCode.NOT_FOUND).build();
        }
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
  @ApiOperation(value = "Create comment for an app")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.CREATED, message = "Commment successful created"),
      @ApiResponse(code = HttpStatusCode.BAD_REQUEST, message = "Content doesn't match"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.FORBIDDEN, message = "Not authorized"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App not found"),
      @ApiResponse(code = HttpStatusCode.CONFLICT, message = "Already exists"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response createComment(@HeaderParam("accessToken") String accessToken, @ApiParam(
      value = "Comment entity as JSON", required = true) Comment createdComment,
      @PathParam("appId") long appId) {

    // Check Authentication
    if (!OIDCAuthentication.isUser(accessToken)) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }

    try {
      List<User> users =
          entityFacade.findByParam(User.class, "oidcId", createdComment.getUser().getOidcId());
      User user = null;
      if (users.size() != 1) {
        return Response.status(HttpStatusCode.NOT_FOUND).build();
      } else {
        user = users.get(0);
      }

      // Get the app
      List<App> apps = entityFacade.findByParam(App.class, "id", appId);
      App app = null;
      if (users.size() != 1) {
        return Response.status(HttpStatusCode.NOT_FOUND).build();
      } else {
        app = apps.get(0);
      }

      // Get all comments for the app
      List<Comment> comments = entityFacade.findByParam(Comment.class, "app", app);

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
      double newRating = 0;
      if (counter != 0) {
        newRating = ratingSum / counter;
      }
      app.setRating(newRating);
      app = entityFacade.save(app);

      // Create Comment
      createdComment.setApp(app);
      createdComment.setUser(user);
      long time = System.currentTimeMillis();
      java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
      createdComment.setReleaseDate(timestamp);
      createdComment.setUpdateDate(timestamp);
      createdComment = entityFacade.save(createdComment);

      // Return comment
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.CREATED)
          .entity(mapper.writeValueAsBytes(createdComment)).build();

    } catch (Exception e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }

  }


  /**
   * 
   * Delete the comment with the given AppId.
   * 
   * @param appId app id
   * @param commentId comment id
   * 
   * @return Response
   * 
   */
  @DELETE
  @Path("/{id}")
  @ApiOperation(value = "Delete comment for an app")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.NO_CONTENT, message = "Comment successful deleted"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.FORBIDDEN, message = "Not authorized"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App or comment not found")})
  public Response deleteComment(@HeaderParam("accessToken") String accessToken,
      @PathParam("appId") long appId, @PathParam("id") long commentId) {

    // Check Authentication
    if (!OIDCAuthentication.isUser(accessToken)) {
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
      if (!(OIDCAuthentication.isCommentOwner(accessToken, comment))) {
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
      comments = entityFacade.findByParam(Comment.class, "app", app);
      int ratingSum = 0;
      int counter = 0;
      for (Comment temp : comments) {
        ratingSum += temp.getRating();
        counter++;
      }

      // Update rating of of the app
      ratingSum -= comment.getRating();
      counter -= 1;
      double newRating = 0;
      if (counter != 0) {
        newRating = ratingSum / counter;
      }
      app.setRating(newRating);
      app = entityFacade.save(app);

      // Delete the comment
      entityFacade.deleteByParam(Comment.class, "id", commentId);
      return Response.status(HttpStatusCode.NO_CONTENT).build();

    } catch (Exception e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * 
   * Update the comment with the given AppId.
   * 
   * @param appId app id
   * @param updatedComment as JSON
   * 
   * @return Response with updated Comment
   */
  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Update comment for an app", response = Comment.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Comment successful updated"),
      @ApiResponse(code = HttpStatusCode.BAD_REQUEST, message = "Content data invalid format"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.FORBIDDEN, message = "Not authorized"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "App or comment not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response updateComment(@HeaderParam("accessToken") String accessToken,
      @PathParam("appId") long appId, @PathParam("id") long commentId, @ApiParam(
          value = "Comment entity as JSON", required = true) Comment updatedComment) {

    // Check Authentication
    if (!OIDCAuthentication.isUser(accessToken)) {
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
      if (!(OIDCAuthentication.isCommentOwner(accessToken, comment))) {
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
      comments = entityFacade.findByParam(Comment.class, "app", app);
      int ratingSum = 0;
      int counter = 0;
      for (Comment temp : comments) {
        ratingSum += temp.getRating();
        counter++;
      }

      // Update rating of of the app
      ratingSum -= comment.getRating();
      ratingSum += updatedComment.getRating();
      double newRating = 0;
      if (counter != 0) {
        newRating = ratingSum / counter;
      }
      app.setRating(newRating);
      app = entityFacade.save(app);

      try {
        // Update Comment
        DozerBeanMapper dozerMapper = new DozerBeanMapper();
        updatedComment.setApp(app);
        updatedComment.setUser(comment.getUser());
        // Release date not subject to change
        updatedComment.setReleaseDate(comment.getReleaseDate());
        // Update date is set here
        long time = System.currentTimeMillis();
        java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
        updatedComment.setUpdateDate(timestamp);
        dozerMapper.map(updatedComment, comment);
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

}
