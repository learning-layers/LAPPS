package de.rwth.dbis.layers.lapps.authenticate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.inject.Singleton;

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

import de.rwth.dbis.layers.lapps.domain.UserFacade;
import de.rwth.dbis.layers.lapps.entity.UserEntity;
import de.rwth.dbis.layers.lapps.exception.OIDCException;

/**
 * AuthenticationProvider to authenticate an OpenIdToken.
 * 
 */
@Singleton
public class OIDCAuthentication {

  private static final String OPEN_ID_PROVIDER = "https://api.learning-layers.eu/o/oauth2";
  private static final String OPEN_ID_PROVIDER_CONFIGURATION_URI = OPEN_ID_PROVIDER.trim()
      + "/.well-known/openid-configuration";

  // only for testing, will always be valid
  public static final String OPEN_ID_TEST_TOKEN = "test_token";
  public static final int OPEN_ID_USER_ID = -1;

  private static UserFacade userFacade = new UserFacade();

  /**
   * Tries to authenticate a user for a given OpenIdToken. If the user is not yet registered, it
   * will register him to the LAPPS backend.
   * 
   * @param openIdToken
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
    String userName = userInfo.getName();

    // search for existing user
    List<UserEntity> entities = userFacade.findByParameter("oidcId", sub);

    // more than one means something bad happened, one means user is already known..
    if (entities.size() > 1)
      throw new OIDCException("Exception during Open Id Authentication occured.");
    else if (entities.size() == 1) {
      // quick check, if mail or user name of OIDC server account differs (has changed) to our
      // database entry; if so, update our user
      if (!entities.get(0).getEmail().equals(mail)
          && !entities.get(0).getUsername().equals(userName)) {
        UserEntity user = entities.get(0);
        userId = user.getId();
        user.setEmail(mail);
        user.setUsername(userName);
        userFacade.save(user);
      }
      return userId;

    }

    // user is unknown, has to be created
    // TODO: type-check!? (String -> Long)
    userId = createNewUser(Long.parseLong(sub), mail, userName);
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
  private static int createNewUser(Long oidc_id, String mail, String userName) {
    UserEntity user = new UserEntity(oidc_id, mail, userName);
    user = userFacade.save(user);
    return user.getId();
  }

}
