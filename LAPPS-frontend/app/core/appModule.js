/**
 * @namespace lapps
 */
(function() {
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
   * @class lapps.lassFilters
   * @memberOf lapps
   */
  var lappsFilters = angular.module('lappsFilters', []);
  /**
   * @class lapps.lappsApp
   * @memberOf lapps
   */
  var lappsApp = angular.module('lappsApp', ['ngRoute', 'ui.bootstrap',
      'lappsControllers', 'lappsDirectives', 'lappsFilters']);

  lappsApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/apps', {
      templateUrl: 'components/welcomePage/welcomePageView.html',
      controller: 'welcomePageCtrl'
    }).when('/apps/:appId', {
      templateUrl: 'components/appDetail/appDetailView.html',
      controller: 'appDetailCtrl'
    }).when('/search/:query', {
      templateUrl: 'components/searchPage/searchPageView.html',
      controller: 'searchPageCtrl'
    }).when('/search', {
      templateUrl: 'components/searchPage/searchPageView.html',
      controller: 'searchPageCtrl'
    }).otherwise({
      redirectTo: '/apps'
    });
  }]);
}).call(this);