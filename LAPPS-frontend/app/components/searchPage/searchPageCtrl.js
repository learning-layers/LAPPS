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
                      '$routeParams',
                      '$http',
                      function($scope, $routeParams, $http) {
                        if ($routeParams.query) {
                          $scope.searchQuery = $routeParams.query;
                        } else {
                          $scope.searchQuery = '';
                        }
                        $scope.apps = [];
                        $scope.collapsed = true;

                        $scope.search = function() {
                          $http
                                  .get(
                                          'http://buche.informatik.rwth-aachen.de:9080/lapps-0.2-SNAPSHOT/lapps/v1/apps?search='
                                                  + $scope.searchQuery)
                                  .success(

                                          function(data) {/*
                                                           * var appIds = data;
                                                           * for (var i = 0; i <
                                                           * appIds.length; i++) {
                                                           * alert(appIds); }
                                                           */
                                            $scope.apps = [];
                                            for (var i = 0; i < data.length; i++) {
                                              $http
                                                      .get(
                                                              'http://buche.informatik.rwth-aachen.de:9080/lapps-0.2-SNAPSHOT/lapps/v1/apps/'
                                                                      + data[i])
                                                      .success(
                                                              function(appData) {
                                                                $scope.apps
                                                                        .push({
                                                                          "id": appData.id,
                                                                          "name": appData.name,
                                                                          "shortDescription": "Test 1 2 3 4",
                                                                          "description": "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet,",
                                                                          "image": "http://lorempixel.com/500/280/",
                                                                          "thumbnail": "http://lorempixel.com/150/150",
                                                                          "rating": appData.rating,
                                                                          "commentsCount": "999",
                                                                          "created": "1564"
                                                                        });
                                                              });
                                            }

                                            /*
                                             * /$scope.apps =
                                             * data.filter(function(el) { return
                                             * el.name.toLowerCase().indexOf(
                                             * $scope.searchQuery.toLowerCase()) >=
                                             * 0; });
                                             */
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