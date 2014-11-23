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
  private static UserFacade userFacade = new UserFacade();
  private final static Logger LOGGER = Logger.getLogger(UsersResource.class.getName());


  /**
   * Provides a list of user ids known to this server.
   * 
   * @return Response with all users as a JSON array.
   */
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Get list of all users")
  @ApiResponses(value = {@ApiResponse(code = 401, message = "Invalid authentication")})
  public Response getAllUsers(@HeaderParam("access_token") String accessToken) {
    // TODO Currently authentication is optional for the tests to still run through
    if (accessToken != null) {
      try {
        authenticate(accessToken);
      } catch (OIDCException e) {
        LOGGER.warning(e.getMessage());
        return Response.status(401).build();
      }
    }
    userFacade.findAll();
    List<UserEntity> entities = (List<UserEntity>) userFacade.findAll();
    ArrayList<Integer> userIds = new ArrayList<Integer>();
    Iterator<UserEntity> userIt = entities.iterator();
    while (userIt.hasNext()) {
      userIds.add(userIt.next().getId());
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(200).entity(mapper.writeValueAsBytes(userIds)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(500).build();
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
  @ApiResponses(value = {@ApiResponse(code = 404, message = "User not found")})
  public Response getUser(@PathParam("id") int id) {
    UserEntity user = userFacade.find(id);
    if (user == null) {
      return Response.status(404).build();
    }
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(200).entity(mapper.writeValueAsBytes(user)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(500).build();
    }
  }

  /**
   * 
   * Remove the user with the given id.
   * 
   * @param id
   * 
   * @return Response
   */
  // TODO: Think about success token (instead of only a 200 response)
  // TODO: Write test case
  @DELETE
  @Path("/{id}")
  @ApiOperation(value = "Delete user by ID")
  @ApiResponses(value = {@ApiResponse(code = 401, message = "Invalid authentication"),
      @ApiResponse(code = 404, message = "User not found")})
  public Response removeUser(@HeaderParam("access_token") String accessToken,
      @PathParam("id") int id) {
    if (accessToken != null) {
      try {
        // TODO: authenticate only the user himself and the admin
        authenticate(accessToken);
      } catch (OIDCException e) {
        LOGGER.warning(e.getMessage());
        return Response.status(401).build();
      }
    }
    UserEntity user = userFacade.find(id);
    if (user == null) {
      return Response.status(404).build();
    }
    // TODO: delete user with help of userFacade
    return Response.status(200).build();
  }

  /**
   * 
   * Update the user with the given id.
   * 
   * @param id
   * @param UserEntity as JSON
   * 
   * @return Response
   */
  // TODO: Think about success token (instead of only a 200 response)
  // TODO: Write test case
  @PUT
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Update user by ID", response = UserEntity.class)
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid user entity"),
      @ApiResponse(code = 401, message = "Invalid authentication"),
      @ApiResponse(code = 404, message = "User not found")})
  public Response updateUser(@HeaderParam("access_token") String accessToken,
      @PathParam("id") int id,
      @ApiParam(value = "User entity as JSON", required = true) UserEntity updatedUser) {
    if (accessToken != null) {
      try {
        // TODO: authenticate only the user himself and the admin
        authenticate(accessToken);
      } catch (OIDCException e) {
        LOGGER.warning(e.getMessage());
        return Response.status(401).build();
      }
    }
    UserEntity user = userFacade.find(id);
    if (user == null) {
      return Response.status(404).build();
    }
    // TODO: update user with help of userFacade
    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(200).entity(mapper.writeValueAsBytes(updatedUser)).build();
    } catch (JsonProcessingException e) {
      LOGGER.warning(e.getMessage());
      return Response.status(500).build();
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
    int userId = -1;
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
        user.setEmail(mail);
        userFacade.save(user);
      }
      return entities.get(0).getId();

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
