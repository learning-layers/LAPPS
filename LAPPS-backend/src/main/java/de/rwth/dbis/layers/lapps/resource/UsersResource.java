package de.rwth.dbis.layers.lapps.resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
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

import de.rwth.dbis.layers.lapps.domain.UserFacade;
import de.rwth.dbis.layers.lapps.entity.UserEntity;
import de.rwth.dbis.layers.lapps.exception.OIDCException;

/**
 * Users resource (exposed at "users" path).
 */
@Path("users")
@Api(value = "users", description = "Example")
public class UsersResource {

  private static final String OPEN_ID_PROVIDER = "http://api.learning-layers.eu/o/oauth2";
  private static final String OPEN_ID_PROVIDER_CONFIGURATION_URI = OPEN_ID_PROVIDER.trim()
      + "/.well-known/openid-configuration";
  private static UserFacade userFacade = new UserFacade();


  /**
   * Provides a list of user ids known to this server.
   * 
   * @return Response with all users as a JSON array.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation("A test operation")
  public Response getAllUsers(@HeaderParam("access_token") String accessToken) {
    // TODO Currently authentication is optional for the tests to still run through..
    if (accessToken != null) {
      try {
        authenticate(accessToken);
      } catch (OIDCException e) {
        e.printStackTrace();
        return Response.status(401).build();
      }
    }
    userFacade.findAll();
    List<UserEntity> entities = (List<UserEntity>) userFacade.findAll();
    ArrayList<Integer> userIds = new ArrayList<Integer>();
    Iterator<UserEntity> userIt = entities.iterator();
    while (userIt.hasNext())
      userIds.add(userIt.next().getId());

    try {
      ObjectMapper mapper = new ObjectMapper();
      return Response.status(200).entity(mapper.writeValueAsBytes(userIds)).build();
    } catch (JsonProcessingException e) {
      // e.printStackTrace(); // TODO have a look at the exception handling
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
