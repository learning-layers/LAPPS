﻿/**
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
   * @class lapps.lappsServices
   * @memberOf lapps
   */
  var lappsServices = angular.module('lappsServices', []);
  /**
   * @class lapps.lappsFilters
   * @memberOf lapps
   */
  var lappsFilters = angular.module('lappsFilters', []);
  /**
   * @class lapps.lappsApp
   * @memberOf lapps
   */
  var lappsFilters = angular.module('lappsAttibuteDirectives', []);
  /**
   * @class lapps.lappsApp
   * @memberOf lapps
   */
  var lappsApp = angular.module('lappsApp', ['ngRoute', 'ui.bootstrap',
      'angular-md5', 'xeditable', 'lappsControllers', 'lappsDirectives',
      'lappsServices', 'swagger-client', 'lappsFilters',
      'lappsAttibuteDirectives']);

  lappsApp.config(['userProvider', function(userProvider) {
    userProvider.configOidc({
      server: 'https://api.learning-layers.eu/o/oauth2',
      clientId: '67da4ca6-c1b3-48cf-a7e5-df61274d55f0',// 'f31522e8-3088-4ef8-9eb3-2881f39a7f78',//
      // new one for buche
      // 67da4ca6-c1b3-48cf-a7e5-df61274d55f0
      scope: 'openid phone email address profile'
    });
  }]);

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
    }).when('/users/:userId', {
      templateUrl: 'components/userPage/userPageView.html',
      controller: 'userPageCtrl'
    }).otherwise({
      redirectTo: '/apps'
    });
  }]);
  lappsApp.run(['editableOptions', function(editableOptions) {
    editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2',
                                    // 'default'
  }]);
}).call(this);