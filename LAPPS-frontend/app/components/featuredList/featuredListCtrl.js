/**
 * @class featuredListCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying a list of featured
 *              apps.
 */
(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'featuredListCtrl',
                  [
                      '$scope',
                      'swaggerApi',
                      'platform',
                      '$routeParams',
                      '$location',
                      function($scope, swaggerApi, platform, $routeParams,
                              $location) {
                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.featuredListCtrl
                         * @description 'The currently active tab (new or top)
                         */
                        $scope.currentTab = 'new';

                        /**
                         * @field
                         * @type app[]
                         * @memberOf lapps.lappsControllers.featuredListCtrl
                         * @description Stores the app objects retrieved from
                         *              the backend.
                         */
                        $scope.apps = [];
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.featuredListCtrl
                         * @param {string}
                         *          tab The new tab to activate.
                         * @description Changes the active tab. Only allows
                         *              'top' and 'new'. Changing tabs requests
                         *              a new list of apps from the backend.
                         */
                        $scope.setTab = function(tab) {
                          if ((tab == 'top' || tab == 'new')) {
                            $scope.currentTab = tab;
                            $location.search('tab', tab);
                            $scope.getApps(tab);
                          }
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.featuredListCtrl
                         * @param {string}
                         *          tab What kind of apps to fetch.
                         * @description Fetches a list of apps depending on the
                         *              current tab. For 'new' the newest apps
                         *              are fetched. For 'top' the best rated
                         *              apps are fetched.
                         */
                        $scope.getApps = function(tab) {

                          var apiParams = {
                            page: 1,
                            pageLength: 18,
                            order: 'desc',
                            sortBy: tab == 'top' ? 'rating' : 'dateCreated',

                          };
                          if (platform.currentPlatform.isAllPlatforms !== true) {
                            apiParams.filterBy = 'platform';
                            apiParams.filterValue = platform.currentPlatform.name;
                          }
                          swaggerApi.apps
                                  .getAllApps(apiParams)
                                  .then(
                                          function(response) {
                                            $scope.apps = response.data;
                                            for (var i = 0; i < $scope.apps.length; i++) {
                                              var thumbnail = '';
                                              for (var j = 0; j < $scope.apps[i].artifacts.length; j++) {
                                                if ($scope.apps[i].artifacts[j].type
                                                        .indexOf('thumb') >= 0) {
                                                  thumbnail = $scope.apps[i].artifacts[j].url;
                                                  break;
                                                }
                                              }
                                              $scope.apps[i].thumbnail = thumbnail;

                                              $scope.apps[i].platformObj = platform
                                                      .getPlatformByName($scope.apps[i].platform);

                                            }
                                          });
                        }
                        if ($routeParams.tab) {
                          $scope.setTab($routeParams.tab);
                        } else {
                          $scope.setTab('new');
                        }

                      }]);
}).call(this);