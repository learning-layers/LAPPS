(function() {
  angular.module('lappsServices').provider('user', function() {
    this.oidcData = {
      server: '',
      clientId: '',
      scope: ''
    };
    var oidcProviderConfig = null;

    this.configOidc = function(data) {
      oidcData = data;
    }
    this.$get = ['$http', '$location', function($http, $location) {
      return {
        data: {
          email: 'johndoe@gmail.com',
          email_verified: true,
          name: 'John Doe',
          preferred_username: 'JohnDoe',
          sub: '42',
          updated_time: '1'
        },
        signedIn: true,
        token: 'asdasd',
        init: function(loginCallback) {
          loginCallback(true);
        },
        signIn: function() {
          this.signedIn = true;
        },
        signOut: function() {
          this.signedIn = false;
        },
        getAccessToken: function() {
          return window.localStorage['access_token'];
        }
      }
    }];
  });
}).call(this);