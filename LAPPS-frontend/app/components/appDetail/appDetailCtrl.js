/**
 * @class appDetailCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying the details of a
 *              selected app.
 */
(function() {
  angular.module('lappsControllers').controller(
          'appDetailCtrl',
          [
              '$scope',
              '$routeParams',
              '$http',
              function($scope, $routeParams, $http) {
                /**
                 * @field
                 * @type string
                 * @memberOf lapps.lappsControllers.appDetailCtrl
                 * @description An id is passed as a routing parameter in the
                 *              url. This way the controller can decide, which
                 *              (detail)-page of an app to display.
                 */
                $scope.appId = $routeParams.appId;

                /**
                 * @field
                 * @type appData
                 * @memberOf lapps.lappsControllers.appDetailCtrl
                 * @description AppData is fetched by an API call to the
                 *              backend.
                 */
                $scope.appData = {};
                $http.get('assets/dummy/app-' + $scope.appId + '.json')
                        .success(function(data) {
                          $scope.appData = data;
                        });
              }]);
}).call(this);