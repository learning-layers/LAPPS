package de.rwth.dbis.layers.lapps.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
import de.rwth.dbis.layers.lapps.entity.User;
import de.rwth.dbis.layers.lapps.entity.User.DateRegisteredComparator;
import de.rwth.dbis.layers.lapps.exception.OIDCException;

/**
 * Users resource (exposed at "users" path).
 */
@Path("/users")
@Api(value = "/users", description = "Users resource")
public class UsersResource {

  private static final Logger LOGGER = Logger.getLogger(UsersResource.class.getName());

  private Facade entityFacade = new Facade();

  /**
   * 
   * Get all users.
   * 
   * @param accessToken openID connect token
   * @param search query parameter
   * @param page number
   * @param pageLength number of users by page
   * @param sortBy field
   * @param order asc or desc
   * @param filterBy field
   * @param filterValue
   * 
   * @return Response with all users as JSON array.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all users", response = User.class, responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getAllUsers(
      @HeaderParam("accessToken") String accessToken,
      @ApiParam(
          value = "Search query parameter for username, email. Multiple keywords use whitespace as separator. Quotation marks indentify exact search strings.",
          required = false) @QueryParam("search") String search,
      @ApiParam(value = "Page number", required = false) @DefaultValue("1") @QueryParam("page") int page,
      @ApiParam(value = "Number of users by page", required = false) @DefaultValue("-1") @HeaderParam("pageLength") int pageLength,
      @ApiParam(value = "Sort by field", required = false,
          allowableValues = "username,dateRegistered") @DefaultValue("username") @QueryParam("sortBy") String sortBy,
      @ApiParam(value = "Order asc or desc", required = false, allowableValues = "asc,desc") @DefaultValue("asc") @QueryParam("order") String order,
      @ApiParam(value = "Filter by field", required = false, allowableValues = "role") @DefaultValue("role") @QueryParam("filterBy") String filterBy,
      @ApiParam(value = "Filter value", required = false) @QueryParam("filterValue") String filterValue) {
    // Check, if the user has admin rights
    if (!OIDCAuthentication.isAdmin(accessToken)) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }

    List<User> entities;
    if (search == null || search.isEmpty()) {
      entities = (List<User>) entityFacade.loadAll(User.class);
    } else {
      entities = new ArrayList<User>();

      List<String> searchStrings = new ArrayList<String>();
      Matcher matcher = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(search);
      while (matcher.find()) {
        searchStrings.add(matcher.group(1).replace("\"", ""));
      }

      for (String searchString : searchStrings) {
        searchString = searchString.trim();
        for (User user : entityFacade.findByParam(User.class, "username", searchString)) {
          if (!entities.contains(user)) {
            entities.add(user);
          }
        }
        List<User> additionalEntities = entityFacade.findByParam(User.class, "email", searchString);
        for (User user : additionalEntities) {
          if (!entities.contains(user)) {
            entities.add(user);
          }
        }
      }
    }

    // filter before sorting
    if (filterBy != null && filterValue != null) {
      switch (filterBy) {
        case "role":
          for (Iterator<User> iterator = entities.iterator(); iterator.hasNext();) {
            User user = iterator.next();
            if (!user.getRole().toString().equalsIgnoreCase(filterValue)) {
              iterator.remove();
            }
          }
          break;
      }
    }

    if (sortBy.equalsIgnoreCase("username")) {
      Collections.sort(entities);
    } else if (sortBy.equalsIgnoreCase("dateRegistered")) {
      Collections.sort(entities, new DateRegisteredComparator());
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
    // remove deleted users from list
    for (Iterator<User> iterator = entities.iterator(); iterator.hasNext();) {
      User user = iterator.next();
      if (user.getRole() == User.DELETED) {
        iterator.remove();
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
   * Gets the user for a given oidcId.
   * 
   * @param oidcId open ID connect id
   * 
   * @return Response with user as a JSON object.
   * 
   */
  @GET
  @Path("/{oidcId}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get user by oidcId", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getUser(@PathParam("oidcId") Long oidcId) {
    // search for existing user
    List<User> entities = entityFacade.findByParam(User.class, "oidcId", oidcId);
    User user = null;
    if (entities.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      user = entities.get(0);
      if (user.getRole() == User.DELETED) {
        return Response.status(HttpStatusCode.NOT_FOUND).build();
      }
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.OK).entity(mapper.writeValueAsBytes(user)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * 
   * Delete the user with the given oidcId.
   * 
   * @param accessToken openID connect token
   * @param oidcId open ID connect id
   * 
   * @return Response
   * 
   */
  @DELETE
  @Path("/{oidcId}")
  @ApiOperation(value = "Delete user by oidcId")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.NO_CONTENT, message = "User successful deleted"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found")})
  public Response deleteUser(@HeaderParam("accessToken") String accessToken,
      @PathParam("oidcId") Long oidcId) {
    // Check, if the user has admin rights and if not, if he is the same user
    if (!OIDCAuthentication.isAdmin(accessToken)
        && !OIDCAuthentication.isSameUser(oidcId, accessToken)) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }
    // search for existing user
    List<User> entities = entityFacade.findByParam(User.class, "oidcId", oidcId);
    User user = null;
    if (entities.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      user = entities.get(0);
    }
    user.setDescription("deletedUser");
    user.setEmail("deletedUser");
    user.setUsername("deletedUser");
    user.setRole(User.DELETED);
    user.setWebsite("deletedUser");

    entityFacade.save(user);
    return Response.status(HttpStatusCode.NO_CONTENT).build();
  }

  /**
   * 
   * Update the user with the given oidcId.
   * 
   * @param accessToken openID connect token
   * @param oidcId open ID connect id
   * @param updatedUser as JSON
   * 
   * @return Response with updated User
   */
  @PUT
  @Path("/{oidcId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Update user by oidcId", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "User successful updated"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response updateUser(@HeaderParam("accessToken") String accessToken,
      @PathParam("oidcId") Long oidcId,
      @ApiParam(value = "User entity as JSON", required = true) User updatedUser) {

    // Check, if updating user is oneself
    if (!OIDCAuthentication.isSameUser(oidcId, accessToken)) {
      // If not, check, if the user has admin rights
      if (!OIDCAuthentication.isAdmin(accessToken)) {
        return Response.status(HttpStatusCode.UNAUTHORIZED).build();
      }
    }
    // search for existing user
    List<User> entities = entityFacade.findByParam(User.class, "oidcId", oidcId);
    User user = null;
    if (entities.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      user = entities.get(0);
    }
    // Prevent users to give themselves new rights, usernames or mail (latter two are managed by
    // OIDC server)
    updatedUser.setOidcId(user.getOidcId());
    updatedUser.setRole(user.getRole());
    updatedUser.setEmail(user.getEmail());
    updatedUser.setUsername(user.getUsername());

    DozerBeanMapper dozerMapper = new DozerBeanMapper();
    dozerMapper.map(updatedUser, user);
    user = entityFacade.save(user);
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return Response.status(HttpStatusCode.OK).entity(objectMapper.writeValueAsBytes(user))
          .build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * 
   * Get all apps for the user with the given oidcId.
   * 
   * @param oidcId open ID connect id
   * @param page number
   * @param pageLength number of users by page
   * 
   * @return Response with all apps as JSON array.
   * 
   */
  @GET
  @Path("/{oidcId}/apps")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get all apps for an user", response = App.class,
      responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response getAppsForUser(
      @PathParam("oidcId") long oidcId,
      @ApiParam(value = "Page number", required = false) @DefaultValue("1") @QueryParam("page") int page,
      @ApiParam(value = "Number of users by page", required = false) @DefaultValue("-1") @HeaderParam("pageLength") int pageLength) {
    return new ApplicationsResource().getAllApps(null, page, pageLength, null, null, "Creator",
        String.valueOf(oidcId));
  }

  /**
   * 
   * Can be called by an user to grant him "pendingDeveloper" rights.
   * 
   * @param accessToken openID connect token
   * @param oidcId open ID connect id
   * @param applyMessage message that will be send to an administrator via mail
   * 
   * @return Response with updated User
   * 
   */
  @PUT
  @Path("/{oidcId}/apply")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Upgrades a user to 'pending developer' status", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_MODIFIED, message = "User role was not changed"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response apply(@HeaderParam("accessToken") String accessToken,
      @PathParam("oidcId") Long oidcId,
      @ApiParam(value = "Apply Message as JSON", required = true) String applyMessage) {

    // First check, if the user is currently only a "user"
    User user;
    try {
      user = OIDCAuthentication.authenticate(accessToken);
    } catch (OIDCException e) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }
    if (user.getRole() == User.USER) {
      user.setRole(User.PENDING_DEVELOPER);
      user = entityFacade.save(user);
    } else {
      return Response.status(HttpStatusCode.NOT_MODIFIED).build();
    }

//    String to = "PLACEHERE";
//    String from = "PLACEHERE";
//    final String passphrase = "PLACEHERE";
//    final String username = "PLACEHERE";
//
//    Properties props = new Properties();
//    props.put("mail.smtp.host", "smtp.gmail.com");
//    props.put("mail.from", from);
//    props.put("mail.smtp.starttls.enable", "true");
//    props.put("mail.smtp.auth", "true");
//
//    Session session = Session.getInstance(props, new Authenticator() {
//      @Override
//      protected PasswordAuthentication getPasswordAuthentication() {
//        return new PasswordAuthentication(username, passphrase);
//      }
//    });
//    try {
//      MimeMessage msg = new MimeMessage(session);
//      msg.setFrom();
//      msg.setRecipients(Message.RecipientType.TO, to);
//      msg.setSubject("A user has applied for developer rights");
//      msg.setSentDate(new Date());
//      msg.setContent("<h1>User with mail" + user.getEmail()
//          + " has applied for Developer Rights</h1><br><h2>Message:</h2><br><br>" + applyMessage,
//          "text/html");
//      Transport.send(msg);
//    } catch (MessagingException mex) {
//      System.out.println("send failed, exception: " + mex);
//    }

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return Response.status(HttpStatusCode.OK).entity(objectMapper.writeValueAsBytes(user))
          .build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * 
   * Can be called by an administrator to grant a user "developer" rights.
   * 
   * @param accessToken openID connect token
   * @param oidcId open ID connect id of the user that will get developer rights
   * 
   * @return Response with updated User
   * 
   */
  @PUT
  @Path("/{oidcId}/grantDeveloperRights")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Upgrades a user to 'developer' status", response = User.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message"),
      @ApiResponse(code = HttpStatusCode.NOT_MODIFIED, message = "User role was not changed"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems")})
  public Response grantDeveloperRights(@HeaderParam("accessToken") String accessToken,
      @PathParam("oidcId") Long oidcId) {

    // Method is admin-only
    if (!OIDCAuthentication.isAdmin(accessToken)) {
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }

    User user = null;
    List<User> entities = entityFacade.findByParam(User.class, "oidcId", oidcId);
    if (entities.isEmpty()) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    } else {
      user = entities.get(0);
    }

    if (user.getRole() == User.PENDING_DEVELOPER) {
      user.setRole(User.DEVELOPER);
      user = entityFacade.save(user);
    } else {
      return Response.status(HttpStatusCode.NOT_MODIFIED).build();
    }
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return Response.status(HttpStatusCode.OK).entity(objectMapper.writeValueAsBytes(user))
          .build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }
}
