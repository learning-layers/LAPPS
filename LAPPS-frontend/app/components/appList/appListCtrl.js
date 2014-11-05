/**
  * @class appListCtrl
  * @memberOf lapps.lappsControllers
  * @description This controller is responsible for displaying a list of available apps.
  */
(function () {
  angular.module('lappsControllers').controller('appListCtrl', [
    '$scope', '$http', function ($scope, $http) {
      /**
       * @field
       * @type {appListItem[]}
       * @memberOf lapps.lappsControllers.appListCtrl
       * @description This array stores a list of available apps with a short description to display.
       */
      $scope.apps = [];
      $http.get('assets/dummy/appList.json').success(function (data) {
        $scope.apps = data;
      });
    }
  ]);
}).call(this);