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
                  $scope.searchQuery = 'af';
                }
                $scope.apps = [];
                $scope.collapsed = true;

                $scope.searchApps = function(query) {
                  $http.get('assets/dummy/appList.json').success(
                          function(data) {
                            $scope.apps = data;
                          });
                }

                $scope.expandCollapseSearch = function() {
                  if ($scope.collapsed) {
                    $scope.collapsed = false;
                  } else {
                    $scope.collapsed = true;
                  }

                }
                $scope.searchApps($scope.searchQuery);
              }]);
}).call(this);