/**
 * @class searchPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying a list of search
 *              results.
 */
(function() {
  angular.module('lappsControllers').controller(
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
                  $http.get('assets/dummy/appList.json').success(
                          function(data) {
                            $scope.apps = data.filter(function(el) {
                              return el.name.toLowerCase().indexOf(
                                      $scope.searchQuery.toLowerCase()) >= 0;
                            });
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