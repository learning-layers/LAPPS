/**
 * @class featuredListCtr
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying a list of featured
 *              apps.
 */
(function() {
  angular.module('lappsControllers').controller('featuredListCtr',
          ['$scope', '$http', function($scope, $http) {

            $scope.apps = [];

            $http.get('assets/dummy/appList.json').success(function(data) {
              $scope.apps = data;
            });
          }]);
}).call(this);