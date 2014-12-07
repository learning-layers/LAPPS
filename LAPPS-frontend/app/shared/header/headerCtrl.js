/**
 * @class headerCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for the header (i.e. check if
 *              user should see links, like admin-area, search etc)
 */
(function() {
  angular.module('lappsControllers').controller('headerCtrl',
          ['$scope', '$location', '$http', function($scope, $location, $http) {
            $scope.searchQuery = "";

            $scope.search = function() {
              $location.path("/search/" + $scope.searchQuery);
            }
          }]);
}).call(this);