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
                      'convert',
                      '$timeout',
                      function($scope, $routeParams, user, $http, swaggerApi,
                              md5, convert, $timeout) {
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
                         * @field
                         * @type boolean
                         * @memberOf lapps.lappsControllers.userPageCtrl
                         * @description Set to true after a fixed period of
                         *              time. Used to show 'invalid-link'
                         *              notifications.
                         */
                        $scope.requestTimedOut = false;

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.userPageCtrl
                         * @description Fetches the user with the requested id
                         *              from the backend.
                         */

                        $scope.fetchUser = function() {
                          // TODO: sync with actual user

                          swaggerApi.users
                                  .getUser({
                                    oidcId: +$scope.userId,
                                  })
                                  .then(
                                          function(response) {
                                            $scope.user = response.data;
                                            $scope.user.roleName = user
                                                    .roleIdToRoleName($scope.user.role);
                                            $scope.user.memberScince = convert
                                                    .date($scope.user.dateRegistered);
                                            $scope.user.avatar = 'https://s.gravatar.com/avatar/'
                                                    + md5
                                                            .createHash($scope.user.email
                                                                    .trim()
                                                                    .toLowerCase()
                                                                    || $scope.user.username)
                                                    + '?s='
                                                    + $scope.avatarSize
                                                    + '&d=identicon';
                                          });
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
                          return user.isDeveloper($scope.user.role);
                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.userPageCtrl
                         * @type boolean
                         * @description True if the profile belongs to an admin.
                         */
                        $scope.isAdmin = function() {
                          return user.isAdmin($scope.user.role);
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

                        $timeout(function() {
                          $scope.requestTimedOut = true
                        }, 500);
                      }]);
}).call(this);