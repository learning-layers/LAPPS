/**
 * @class userPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying the profile of
 *              users.
 */
(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'userPageCtrl',
                  [
                      '$scope',
                      '$routeParams',
                      'user',
                      '$http',
                      'swaggerApi',
                      'md5',
                      function($scope, $routeParams, user, $http, swaggerApi,
                              md5) {
                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.userPageCtrl
                         * @description The id of the displayed user (needed for
                         *              backend fetch).
                         */
                        $scope.userId = $routeParams.userId;
                        /**
                         * @field
                         * @type {user}
                         * @memberOf lapps.lappsControllers.userPageCtrl
                         * @description The user object of the displayed user.
                         */
                        $scope.user = {};
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.userPageCtrl
                         * @description The size in pixels of the avatar to
                         *              load.
                         */
                        $scope.avatarSize = 150;
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.userPageCtrl
                         * @description Fetches the user with the requested id
                         *              from the backend.
                         */
                        $scope.fetchUser = function() {
                          // TODO: sync with actual user
                          $scope.user.role = 'Developer';
                          $scope.user.name = 'John Doe';
                          $scope.user.email = 'john@mail1.foobar';
                          $scope.user.homepage = 'https://www.google.com';
                          $scope.user.memberSince = '13-November-14';
                          $scope.user.description = 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est.';
                          $scope.user.avatar = 'https://s.gravatar.com/avatar/'
                                  + md5.createHash($scope.user.email.trim()
                                          .toLowerCase()
                                          || $scope.user.name) + '?s='
                                  + $scope.avatarSize + '&d=identicon';

                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.userPageCtrl
                         * @description Requests a backend update of the user,
                         *              if the object was edited (i.e.
                         *              properties changed by the user).
                         */
                        $scope.updateUser = function() {
                          // TODO: implement actual update for backend
                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.userPageCtrl
                         * @type boolean
                         * @description True if the visitor is a developer.
                         */
                        $scope.visitorIsDeveloper = function() {
                          return user.isDeveloper();
                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.userPageCtrl
                         * @type boolean
                         * @description True if the visitor is an admin. Note:
                         *              Actual security checks are performed on
                         *              the backend side. Here it is only used
                         *              to show/hide elements.
                         */
                        $scope.visitorIsAdmin = function() {
                          return user.isAdmin();
                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.userPageCtrl
                         * @type boolean
                         * @description True if the profile belongs to a
                         *              developer.
                         */
                        $scope.isDeveloper = function() {
                          return $scope.user.role.toLowerCase().indexOf('dev') == 0;
                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.userPageCtrl
                         * @type boolean
                         * @description True if the profile belongs to an admin.
                         */
                        $scope.isAdmin = function() {
                          return user.data.role.toLowerCase().indexOf('admin') == 0;
                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.userPageCtrl
                         * @type boolean
                         * @description True if the visitor is logged in and an
                         *              admin or profile owner.
                         */
                        $scope.mayEdit = function() {
                          if (!user.signedIn || user.data == null
                                  || user.data.sub == null) { return false; }
                          if ($scope.visitorIsAdmin()) { return true; }
                          return $scope.userId == user.data.sub;
                        }

                        $scope.fetchUser();
                      }]);
}).call(this);