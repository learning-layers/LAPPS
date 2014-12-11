﻿/**
 * @class user
 * @memberOf lapps.lappsServices
 * @description This provider manages users: (Automatic) login of users and
 *              information about the current user
 */
(function() {
  angular
          .module('lappsServices')
          .provider(
                  'user',
                  function() {
                    /**
                     * @field
                     * @type oidcConfig
                     * @memberOf lapps.lappsServices.user
                     * @description Current limit on the amount of tags to
                     *              display.
                     */
                    this.oidcData = {
                      server: null,
                      clientId: null,
                      scope: null
                    };

                    /**
                     * @field
                     * @type {object}
                     * @memberOf lapps.lappsServices.user
                     * @description The configuration details retrieved from the
                     *              oidc provider.
                     */
                    var oidcProviderConfig = null;

                    /**
                     * @function
                     * @memberOf lapps.lappsServices.user
                     * @param {oidcConfig}
                     *          data Configuration data for communication with
                     *          the oidc provider.
                     * @description Initial configuration on startup for
                     *              contacting the oidc provider.
                     */
                    this.configOidc = function(data) {
                      oidcData = data;
                    }
                    this.$get = [
                        '$http',
                        '$location',
                        function($http, $location) {
                          return {

                            /**
                             * @field
                             * @type {object}
                             * @memberOf lapps.lappsServices.user
                             * @description Data about the user retrived from
                             *              the oidc provider.
                             */
                            data: null,

                            /**
                             * @field
                             * @type boolean
                             * @memberOf lapps.lappsServices.user
                             * @description True if the user is signed in.
                             */
                            signedIn: false,

                            /**
                             * @field
                             * @type string
                             * @memberOf lapps.lappsServices.user
                             * @description The oidc token of the current user.
                             *              Needed for some restricted backend
                             *              requests.
                             */
                            token: '',

                            /**
                             * @function
                             * @memberOf lapps.lappsServices.user
                             * @param function
                             *          loginCallback What function to call,
                             *          when login was successful or failed.
                             * @description Retrieves oidc provider information
                             *              and attempts to login the current
                             *              user. Retrieves token and user data
                             *              if successful.
                             */
                            init: function(loginCallback) {
                              var self = this;

                              self.loginCallback = loginCallback;
                              this
                                      .getProviderConfig(
                                              oidcData.server,
                                              function(config) {
                                                oidcProviderConfig = config;

                                                // after successful retrieval of
                                                // server configuration, check
                                                // auth status
                                                if (self.checkAuth()) {
                                                  // use access token and
                                                  // retrieve user info
                                                  self
                                                          .getUserInfo(function(
                                                                  user) {
                                                            if (user['sub']) {
                                                              self.data = user;

                                                              self.signedIn = true;
                                                              self.token = window.localStorage['access_token'];// needed
                                                              // for
                                                              // requests

                                                              self
                                                                      .loginCallback(true);
                                                            } else {
                                                              self.signedIn = false;
                                                              self
                                                                      .loginCallback(false);
                                                            }
                                                          });
                                                } else {
                                                  // render signin button
                                                  self.signedIn = false;
                                                  self.loginCallback(false);
                                                }
                                              },
                                              function(d, s) {
                                                console
                                                        .log('Warning: could not retrieve OpenID Connect server configuration!');
                                                console.log(d);
                                                console.log(s);
                                                console.log('--');
                                              });
                            },
                            /**
                             * @function
                             * @memberOf lapps.lappsServices.user
                             * @description When the user wants to sign in
                             *              (automatic sign in was not
                             *              possible), she is redirected to the
                             *              login page given by the oidc
                             *              provider.
                             */
                            signIn: function() {
                              var pair = window.location.href.split('?');
                              window.localStorage.oldSearch = pair[1] || '';

                              var url = oidcProviderConfig.authorization_endpoint
                                      .replace('http:', 'https:')
                                      + '?response_type=id_token%20token&client_id='
                                      + oidcData.clientId
                                      + '&redirect_uri='
                                      + encodeURIComponent(window.location.href)
                                      + (window.location.href.indexOf('?') > -1
                                              ? '' : '?oidc=1')
                                      + '&scope='
                                      + encodeURIComponent(oidcData.scope);

                              window.location.href = url;
                            },
                            /**
                             * @function
                             * @memberOf lapps.lappsServices.user
                             * @description When the user wants to sign out, the
                             *              oidc information is deleted from the
                             *              local storage (no auto login
                             *              anymore).
                             */
                            signOut: function() {
                              var url = oidcData.server;
                              // window.location.href = url;
                              this.signedIn = false;
                              window.localStorage.removeItem('access_token');
                              window.localStorage.removeItem('id_token');
                              this.token = '';
                            },
                            /**
                             * @function
                             * @type string
                             * @memberOf lapps.lappsServices.user
                             * @description Returns the accessToken of the
                             *              current user stored in the local
                             *              storage.
                             */
                            getAccessToken: function() {
                              return window.localStorage['access_token'];
                            },

                            /**
                             * @function
                             * @memberOf lapps.lappsServices.user
                             * @param {string}
                             *          provider Url of the oidc provider.
                             * @param {function}
                             *          callback Callback for success.
                             * @param {function}
                             *          errorCallback Callback for errors.
                             * @description Fetches the oidc provider
                             *              configuration.
                             */
                            getProviderConfig: function(provider, callback,
                                    errorCallback) {
                              var req = {
                                method: 'GET',
                                url: provider
                                        + '/.well-known/openid-configuration'
                              };

                              $http(req).success(function(data, status) {
                                callback(data)
                              }).error(function(data, status) {
                                errorCallback(data, status)
                              });
                            },
                            /**
                             * @function
                             * @memberOf lapps.lappsServices.user
                             * @param {function}
                             *          callback Callback for success.
                             * @description Fetches the user information from
                             *              the oidc provider.
                             */
                            getUserInfo: function(callback) {
                              var req = {
                                method: 'GET',
                                url: oidcProviderConfig.userinfo_endpoint,
                                headers: {
                                  'Authorization': 'Bearer '
                                          + this.getAccessToken()
                                }
                              };
                              $http(req).success(function(data, status) {
                                callback(data)
                              }).error(function(data, status) {
                                callback(status)
                              });
                            },

                            /**
                             * @function
                             * @memberOf lapps.lappsServices.user
                             * @description Reads the token information
                             *              retrieved from the oidc provider on
                             *              login from the address bar and sets
                             *              the local storage variables
                             *              access_token and id_token.
                             */
                            checkAuth: function() {
                              var fragment = this.parseFragment();

                              if (fragment != {} && fragment.access_token
                                      && fragment.id_token) {
                                window.localStorage['access_token'] = fragment['access_token'];
                                window.localStorage['id_token'] = fragment['id_token'];
                                this.removeTokenFromUrl();
                              }
                              if (window.localStorage['access_token'] != null
                                      && window.localStorage['id_token'] != null) {
                                return true;
                              } else {
                                return false;
                              }
                            },
                            /**
                             * @function
                             * @memberOf lapps.lappsServices.user
                             * @description Reads the token information from the
                             *              address bar.
                             */
                            parseFragment: function() {
                              var params = {}, queryString = location.hash
                                      .substring(1), regex = /([^&=]+)=([^&]*)/g, m;
                              while (m = regex.exec(queryString)) {
                                params[decodeURIComponent(m[1])] = decodeURIComponent(m[2]);
                              }
                              return params;
                            },
                            /**
                             * @function
                             * @memberOf lapps.lappsServices.user
                             * @description Removes token information from
                             *              address bar to avoid clutter and
                             *              accidental sharing.
                             */
                            removeTokenFromUrl: function() {
                              var parseLocation = function(location) {
                                var pairs = location.split("&");
                                var obj = {};
                                var pair;
                                var i;

                                for (i in pairs) {
                                  if (pairs[i] === "") continue;

                                  pair = pairs[i].split("=");
                                  obj[decodeURIComponent(pair[0])] = decodeURIComponent(pair[1]);
                                }

                                return obj;
                              };

                              parseLocation(window.location.search)['a']

                              var queryObj = parseLocation(window.localStorage.oldSearch);

                              var oldSearch = window.localStorage.oldSearch;
                              if ($location.$$search.access_token) {
                                $location.search({});
                                for (key in queryObj) {
                                  if (queryObj.hasOwnProperty(key)) {
                                    $location.search(key, queryObj[key]);
                                  }
                                }
                                $location.$$compose();
                              }
                            }
                          }
                        }];
                  });
}).call(this);