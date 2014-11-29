package de.rwth.dbis.layers.lapps.resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPRequest.Method;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.openid.connect.sdk.UserInfoErrorResponse;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import com.nimbusds.openid.connect.sdk.UserInfoSuccessResponse;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import de.rwth.dbis.layers.lapps.domain.UserFacade;
import de.rwth.dbis.layers.lapps.entity.UserEntity;
import de.rwth.dbis.layers.lapps.exception.OIDCException;

/**
 * Users resource (exposed at "users" path).
 */
@Path("/users")
@Api(value = "/users", description = "User ressource")
public class UsersResource {

  private static final String OPEN_ID_PROVIDER = "http://api.learning-layers.eu/o/oauth2";
  private static final String OPEN_ID_PROVIDER_CONFIGURATION_URI = OPEN_ID_PROVIDER.trim()
      + "/.well-known/openid-configuration";
  private static final Logger LOGGER = Logger.getLogger(UsersResource.class.getName());

  // only for testing, will always be valid
  public static final String OPEN_ID_TEST_TOKEN = "test_token";
  public static final int OPEN_ID_USER_ID = -1;

  private static UserFacade userFacade = new UserFacade();

  /**
   * Provides a list of user Ids known to this server.
   * 
   * @return Response with all user Ids as a JSON array.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get list of all users")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems"),
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message")})
  public Response getAllUsers(@HeaderParam("access_token") String accessToken) {
    try {
      authenticate(accessToken);
      // TODO: Check for admin rights (not part of the open id authentication process)
    } catch (OIDCException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }

    List<UserEntity> entities = (List<UserEntity>) userFacade.findAll();
    ArrayList<Integer> userIds = new ArrayList<Integer>();
    Iterator<UserEntity> userIt = entities.iterator();
    while (userIt.hasNext()) {
      userIds.add(userIt.next().getId());
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.OK).entity(mapper.writeValueAsBytes(userIds)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * 
   * Gets the user for a given id.
   * 
   * @param id
   * 
   * @return Response with user as a JSON object.
   * 
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get user by ID", response = UserEntity.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found"),
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems"),
      @ApiResponse(code = HttpStatusCode.OK, message = "Default return message")})
  public Response getUser(@PathParam("id") int id) {

    UserEntity user = userFacade.find(id);
    if (user == null) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
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
   * Delete the user with the given id.
   * 
   * @param id
   * 
   * @return Response
   */
  // TODO: Think about success token (instead of only a 200 response)
  @DELETE
  @Path("/{id}")
  @ApiOperation(value = "Delete user by ID")
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response deleteUser(@HeaderParam("access_token") String accessToken,
      @PathParam("id") int id) {
    try {
      // TODO: Check for admin or user himself rights (not part of the open id authentication
      // process)
      authenticate(accessToken);
    } catch (OIDCException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }

    UserEntity user = userFacade.find(id);
    if (user == null) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    }
    // TODO: delete user with help of userFacade
    return Response.status(HttpStatusCode.NOT_IMPLEMENTED).build();
  }

  /**
   * 
   * Update the user with the given id.
   * 
   * @param id
   * @param updatedUser as JSON
   * 
   * @return Response
   */
  // TODO: Think about success token (instead of only a 200 response)
  // TODO: Think if really needed (since we only create users via OIDC authenticate, and also update
  // via this functionality. Would make sense if we had more user attributes.)
  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Update user by ID", response = UserEntity.class)
  @ApiResponses(value = {
      @ApiResponse(code = HttpStatusCode.INTERNAL_SERVER_ERROR,
          message = "Internal server problems"),
      @ApiResponse(code = HttpStatusCode.UNAUTHORIZED, message = "Invalid authentication"),
      @ApiResponse(code = HttpStatusCode.NOT_FOUND, message = "User not found"),
      @ApiResponse(code = HttpStatusCode.NOT_IMPLEMENTED,
          message = "Currently, this method is not implemented")})
  public Response updateUser(@HeaderParam("access_token") String accessToken,
      @PathParam("id") int id,
      @ApiParam(value = "User entity as JSON", required = true) UserEntity updatedUser) {
    try {
      // TODO: Check for admin or user himself rights (not part of the open id authentication
      // process)
      authenticate(accessToken);
    } catch (OIDCException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.UNAUTHORIZED).build();
    }
    UserEntity user = userFacade.find(id);
    if (user == null) {
      return Response.status(HttpStatusCode.NOT_FOUND).build();
    }
    // TODO: update user with help of userFacade
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(HttpStatusCode.NOT_IMPLEMENTED)
          .entity(mapper.writeValueAsBytes(updatedUser)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(HttpStatusCode.INTERNAL_SERVER_ERROR).build();
    }
  }

  /**
   * Tries to authenticate a user for a given OpenIdToken. If the user is not yet registered, it
   * will register him to the LAPPS backend.
   * 
   * @return the (LAPPS) id of the user
   * @throws OIDCException an exception thrown for all Open Id Connect issues
   */
  public static int authenticate(String openIdToken) throws OIDCException {

    // return value
    int userId = OPEN_ID_USER_ID;

    // no token provided
    if (openIdToken == null) {
      throw new OIDCException("No token was provided");
    }

    // default testing token returns default testing id
    if (openIdToken.equals(OPEN_ID_TEST_TOKEN)) {
      return userId;
    }

    // JSON initialization stuff
    ObjectMapper mapper = new ObjectMapper();
    JsonNode serverConfig;

    // get provider configuration
    URL providerConfigurationUri;
    try {
      providerConfigurationUri = new URL(OPEN_ID_PROVIDER_CONFIGURATION_URI);
    } catch (MalformedURLException e) {
      throw new OIDCException("Exception during Open Id Connect authentication occured: "
          + e.getMessage());
    }
    HTTPRequest providerConfigRequest = new HTTPRequest(Method.GET, providerConfigurationUri);

    // parse JSON result from configuration request
    try {
      String configStr = providerConfigRequest.send().getContent();
      serverConfig = mapper.readTree(configStr);
    } catch (Exception e) {
      throw new OIDCException("Exception during Open Id Connect authentication occured: "
          + e.getMessage());
    }

    // send access token, get user info
    HTTPRequest httpRequest;
    HTTPResponse httpResponse;

    try {
      URI userinfoEndpointUri = new URI(serverConfig.get("userinfo_endpoint").textValue());
      httpRequest = new HTTPRequest(Method.GET, userinfoEndpointUri.toURL());
      httpRequest.setAuthorization("Bearer " + openIdToken);
      httpResponse = httpRequest.send();
    } catch (IOException | URISyntaxException e) {
      throw new OIDCException("Exception during Open Id Connect authentication occured: "
          + e.getMessage());
    }

    // ..and process the response
    UserInfoResponse userInfoResponse;
    try {
      userInfoResponse = UserInfoResponse.parse(httpResponse);
    } catch (ParseException e) {
      throw new OIDCException("Exception during Open Id Authentication occured: " + e.getMessage());
    }
    // request failed (unauthorized)
    if (userInfoResponse instanceof UserInfoErrorResponse) {
      UserInfoErrorResponse uier = (UserInfoErrorResponse) userInfoResponse;
      throw new OIDCException("Exception during Open Id Authentication occured: "
          + uier.getClass().toString());
    }
    // successful, now get the user info and start extracting content
    UserInfo userInfo = ((UserInfoSuccessResponse) userInfoResponse).getUserInfo();
    String sub = userInfo.getSubject().toString();
    String mail = userInfo.getEmail().toString();

    // search for existing user
    List<UserEntity> entities = userFacade.findByParameter("oidcId", sub);

    // more than one means something bad happened, one means user is already known..
    if (entities.size() > 1)
      throw new OIDCException("Exception during Open Id Authentication occured.");
    else if (entities.size() == 1) {
      // quick check, if mail of OIDC server account differs (has changed) to our database entry; if
      // so, update our user
      if (!entities.get(0).getEmail().equals(mail)) {
        UserEntity user = entities.get(0);
        userId = user.getId();
        user.setEmail(mail);
        userFacade.save(user);
      }
      return userId;

    }

    // user is unknown, has to be created
    userId = createNewUser(sub, mail);
    return userId;
  }

  /**
   * Creates a new user for a given oidc_id and mail.
   * 
   * @param oidc_id the "subject" identifier of the open id connect authentication
   * @param mail a user email
   * 
   * @return the (LAPPS) id of the user
   */
  private static int createNewUser(String oidc_id, String mail) {
    UserEntity user = new UserEntity(oidc_id, mail);
    user = userFacade.save(user);

    return user.getId();
  }

}
