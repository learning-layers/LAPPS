/**
  * @namespace lapps
  */
(function () {
  /**
  * @class lapps.lappsControllers
  * @memberOf lapps
  */
  var lappsControllers = angular.module('lappsControllers', []);
  /**
  * @class lapps.lappsDirectives
  * @memberOf lapps
  */
  var lappsDirectives = angular.module('lappsDirectives', []);
  /**
  * @class lapps.lappsApp
  * @memberOf lapps
  */
  var lappsApp = angular.module('lappsApp', ['ngRoute', 'lappsControllers', 'lappsDirectives']);

  lappsApp.config([
    '$routeProvider', function ($routeProvider) {
      $routeProvider.when('/apps', {
        templateUrl: 'components/appList/appListView.html',
        controller: 'appListCtrl'
      }).when('/apps/:appId', {
        templateUrl: 'components/appDetail/appDetailView.html',
        controller: 'appDetailCtrl'
      }).otherwise({
        redirectTo: '/apps'
      });
    }
  ]);
}).call(this);