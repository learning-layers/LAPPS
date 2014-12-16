/**
 * @class headerCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for the header (i.e. check if
 *              user should see links, like admin-area, search etc)
 */
(function() {
  angular.module('lappsControllers').controller('headerCtrl',
          ['$scope', '$location', 'user', function($scope, $location, user) {
            var SIGN_IN = 'Sign in';
            var SIGN_OUT = 'Sign out';
            /**
             * @field
             * @type string
             * @memberOf lapps.lappsControllers.headerCtrl
             * @description Text displayed on the sign in button.
             */
            $scope.loginText = SIGN_IN;
            /**
             * @field
             * @type string
             * @memberOf lapps.lappsControllers.headerCtrl
             * @description Current content of the search box.
             */
            $scope.searchQuery = '';

            /**
             * @field
             * @type boolean
             * @memberOf lapps.lappsControllers.headerCtrl
             * @description True if user is signed in. Used to display sign in
             *              button accordingly to the current state.
             */
            $scope.isloggedIn = false;

            /**
             * @function
             * @memberOf lapps.lappsControllers.headerCtrl
             * @description Signs the user manually in (when clicking the sign
             *              in button).
             */
            $scope.login = function() {
              user.signIn();
            }
            /**
             * @function
             * @memberOf lapps.lappsControllers.headerCtrl
             * @description Signs the user out (when clicking the sign out
             *              button).
             */
            $scope.logout = function() {
              user.signOut();
              $scope.loginCallback(false);
            }
            /**
             * @function
             * @memberOf lapps.lappsControllers.headerCtrl
             * @description Redirects to the search page with the given search
             *              parameters.
             */
            $scope.search = function() {
              $location.path('/search/' + $scope.searchQuery);
            }
            /**
             * @function
             * @memberOf lapps.lappsControllers.headerCtrl
             * @param {boolean}
             *          success True, if the login was successful.
             * @description Called after a oidc login attempt.
             */
            $scope.loginCallback = function(success) {
              /*
               * user.signedIn = true; user.data = { sub: 12,
               * preferred_username: 'JohnDoe', email: 'john@mail1.foobar' };
               * user.role = 'Developer'; $scope.isloggedIn = true;
               * $scope.userData = user.data; return;
               */

              $scope.isloggedIn = success;

              if (!success) {
              } else {
                $scope.userData = user.data;
              }
              // console.log(user);

            }

            user.init($scope.loginCallback);

          }]);
}).call(this);