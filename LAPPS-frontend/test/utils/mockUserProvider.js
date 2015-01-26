/**
 * Since in tests we don't want to rely on 3rd party services: just give some
 * mockup values to work with.
 */
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
          updated_time: '1',
          oidcId: '42'
        },
        signedIn: true,
        token: 'asdasd',
        roleIdToRoleName: function(id) {
          if (id == 1) { return 'User'; }
          if (id == 2) { return 'Dev. Applicant'; }
          if (id == 3) { return 'Developer'; }
          if (id == 4) { return 'Admin'; }
          return 'User';
        },
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
        },
        isAdmin: function(id) {
          if (typeof id === 'undefined' || id === null) {
            return this.role == 4;
          } else {
            return id == 4;
          }
        },
        isDeveloper: function(id) {
          if (typeof id === 'undefined' || id === null) {
            return this.role == 3;
          } else {
            return id == 3;
          }
        }
      }
    }];
  });
}).call(this);