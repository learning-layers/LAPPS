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

import de.rwth.dbis.layers.lapps.domain.Facade;
import de.rwth.dbis.layers.lapps.entity.User;
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
  public static final Long OPEN_ID_USER_ID = -1L;

  private static Facade facade = new Facade();

  /**
   * Tries to authenticate a user for a given OpenIdToken. If the user is not yet registered, it
   * will register him to the LAPPS backend.
   * 
   * @param openIdToken
   * 
   * @return the (LAPPS) id of the user
   * @throws OIDCException an exception thrown for all Open Id Connect issues
   */
  public static Long authenticate(String openIdToken) throws OIDCException {

    // return value
    Long userId = OPEN_ID_USER_ID;

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
    long sub = Long.parseLong(userInfo.getSubject().toString());
    String mail = userInfo.getEmail().toString();
    String userName = userInfo.getName();

    // search for existing user
    List<User> entities = facade.findByParam(User.class, "oidcId", sub);

    // more than one means something bad happened, one means user is already known..
    if (entities.size() > 1)
      throw new OIDCException("Exception during Open Id Authentication occured.");
    else if (entities.size() == 1) {
      User user = entities.get(0);
      userId = user.getId();
      // quick check, if mail or user name of OIDC server account differs (has changed) to our
      // database entry; if so, update our user
      if (!user.getEmail().equals(mail) || !user.getUsername().equals(userName)) {
        user.setEmail(mail);
        user.setUsername(userName);
        facade.save(user);
      }
      return userId;

    }

    // user is unknown, has to be created
    userId = createNewUser(sub, mail, userName);
    return userId;
  }

  /**
   * Creates a new user for a given oidc_id and mail.
   * 
   * @param oidc_id the "subject" identifier of the open id connect authentication
   * @param mail a user email
   * @param userName the name of the user to be created
   * 
   * @return the (LAPPS) id of the user
   */
  private static Long createNewUser(long oidc_id, String mail, String userName) {
    User user = new User(String.valueOf(oidc_id), userName, mail);
    user = facade.save(user);
    return user.getId();
  }

}
