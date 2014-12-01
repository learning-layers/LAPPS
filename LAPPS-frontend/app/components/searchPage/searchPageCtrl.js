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
                      '$rootScope',
                      '$routeParams',
                      '$http',
                      function($scope, $rootScope, $routeParams, $http) {
                        if ($routeParams.query) {
                          $scope.searchQuery = $routeParams.query;
                        } else {
                          $scope.searchQuery = '';
                        }
                        $scope.apps = [];
                        $scope.collapsed = true;
                        /*
                         * /$scope.apps = data.filter(function(el) { return
                         * el.name.toLowerCase().indexOf(
                         * $scope.searchQuery.toLowerCase()) >= 0; });
                         */
                        $scope.search = function() {
                          $rootScope.api.apps
                                  .getAllApps({
                                    search: $scope.searchQuery
                                  })
                                  .then(
                                          function(appList) {
                                            for (var i = 0; i < appList.length; i++) {
                                              $rootScope.api.apps
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