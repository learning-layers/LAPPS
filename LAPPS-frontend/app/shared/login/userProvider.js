(function() {
  angular
          .module('lappsServices')
          .provider(
                  'user',
                  function() {
                    this.oidcData = {
                      server: null,
                      clientId: null,
                      scope: null
                    };
                    var oidcProviderConfig = null;

                    this.configOidc = function(data) {
                      oidcData = data;
                    }
                    this.$get = [
                        '$http',
                        '$location',
                        function($http, $location) {
                          return {
                            data: null,
                            signedIn: false,
                            token: '',
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
                            signOut: function() {
                              var url = oidcData.server;
                              // window.location.href = url;
                              this.signedIn = false;
                              window.localStorage.removeItem('access_token');
                              window.localStorage.removeItem('id_token');
                              this.token = '';
                            },
                            getAccessToken: function() {
                              return window.localStorage['access_token'];
                            },
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
                            parseFragment: function() {
                              var params = {}, queryString = location.hash
                                      .substring(1), regex = /([^&=]+)=([^&]*)/g, m;
                              while (m = regex.exec(queryString)) {
                                params[decodeURIComponent(m[1])] = decodeURIComponent(m[2]);
                              }
                              return params;
                            },
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