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
                      function($scope, $routeParams, user, $http, swaggerApi,
                              md5, convert) {
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

                        $scope.user.avatar = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAABmJLR0QAJwBxAIVCQz2jAAAACXBIWXMAAC4jAAAuIwF4pT92AAAAB3RJTUUH3gwJFTshNi7riQAAAg5JREFUOMutkr1rFGEQh595d/dub0MSv4iI1xwIBoTgR87CSo1/gIo2XqukuwiChyjaqbE8TCEIghC1kFiksxUCsoVV/Aia84JfScw3WXN3uzsW7umJBgX9NTMDM8/7e4eBf5QABOWcNPOW2FQE4BUr6wMSSBewAcgAFhADDa9YGQ/KOZPUv4AkKOcGgAvA5+S1CNAEbpL6pFesTAblnHjFirYCTF2sm5Nue1ZhwqAqEBo0FAiBKIkjQTl3JQESlHM/f+HiyPEt83a6q2/p/eUDKzN9dWM8VGxQFSTujGrVdBwtKmwGznnFymhzd3L+0YnTQD+glqqEYuifftHtaGxanKoiVkdUn0rH4YLARoVrwKgBuXPj2MM88ECN1gWVoa273ry1O61QrUxNbTdUkzKoBsbOzjluz6zjti1bqb3el8UlAei9fi/l2PXuWuie3Z0dO5pxVtIg7KzNWwdXp+x5y9V3Tnv8OrUp/pRyV9usYNoiDoyJ7kp+cLgfONO0Ciquveb0ZMd2/DiIb4uP1WhQ7wgmZno+NKJ0XYgfG+CWXyr0AlcBG4S1MNN4WjnyfHZ120KsotPL2Tm/euiVXz38cvxjb7URpYb80qn9ilwSgPzgMH6pQJLvAW4nhyMtFixgAHjilwrfZ2ygdRi/VHgG7MsPDm8H7ieQgl8qTDV7/qjfNa03KH8Labr87/oKw03euhH4vI8AAAAASUVORK5CYII=';
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
                      }]);
}).call(this);
