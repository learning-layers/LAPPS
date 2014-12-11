/**
 * @class searchPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying a list of search
 *              results.
 */
(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'searchPageCtrl',
                  [
                      '$scope',
                      'swaggerApi',
                      '$routeParams',
                      'user',
                      function($scope, swaggerApi, $routeParams, user) {

                        if ($routeParams.query) {
                          $scope.searchQuery = $routeParams.query;
                        } else {
                          $scope.searchQuery = '';
                        }
                        /**
                         * @field
                         * @type app[]
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Stores the app objects retrieved from
                         *              the backend.
                         */
                        $scope.apps = [];
                        /**
                         * @field
                         * @type boolean
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description The state of the advanced search area.
                         */
                        $scope.collapsed = true;

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Searches using the current query
                         *              settings. And loads a list of apps as
                         *              results.
                         */
                        $scope.search = function() {

                          // swaggerApi.users.getAllUsers({'accessToken':user.token}).then(function(data2){

                          swaggerApi.apps
                                  .getAllApps({
                                    search: $scope.searchQuery
                                  })
                                  .then(
                                          function(appList) {
                                            for (var i = 0; i < appList.length; i++) {
                                              swaggerApi.apps
                                                      .getApp(appList[i])
                                                      .then(
                                                              function(app) {
                                                                $scope.apps
                                                                        .push({
                                                                          "id": app.id,
                                                                          "name": app.name,
                                                                          "shortDescription": "Test 1 2 3 4",
                                                                          "description": "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet,",
                                                                          "image": "http://lorempixel.com/500/280/",
                                                                          "thumbnail": "http://lorempixel.com/150/150",
                                                                          "rating": app.rating,
                                                                          "commentsCount": "999",
                                                                          "created": "1564"
                                                                        });
                                                              });
                                            }
                                          });
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Toggles
                         *              {@link lapps.lappsControllers.searchPageCtrl.$scope.collapsed}.
                         */
                        $scope.expandCollapseSearch = function() {
                          if ($scope.collapsed) {
                            $scope.collapsed = false;
                          } else {
                            $scope.collapsed = true;
                          }
                        }
                        $scope.search();
                      }]);
}).call(this);