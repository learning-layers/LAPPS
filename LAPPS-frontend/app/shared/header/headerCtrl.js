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

            $scope.loginText = SIGN_IN;
            $scope.searchQuery = '';

            $scope.isloggedIn = false;
            $scope.userName = '';

            $scope.login = function() {
              user.signIn();
            }

            $scope.logout = function() {
              user.signOut();
              $scope.loginCallback(false);
            }
            $scope.search = function() {
              $location.path('/search/' + $scope.searchQuery);
            }

            $scope.loginCallback = function(success) {
              $scope.isloggedIn = success;
              if (!success) {
              } else {
                $scope.userName = user.data.preferred_username;
              }
              // console.log(user);
            }

            user.init($scope.loginCallback);
          }]);
}).call(this);