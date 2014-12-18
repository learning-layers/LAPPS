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
                      function($scope, swaggerApi) {
                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.featuredListCtrl
                         * @description 'The currently active tab (new or top)
                         */
                        $scope.currentTab = '';
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
                          if ((tab == 'top' || tab == 'new')
                                  && $scope.currentTab != tab) {
                            $scope.currentTab = tab;
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
                          swaggerApi.apps
                                  .getAllApps({
                                    page: 1,
                                    pageLength: 30
                                  })
                                  .then(
                                          function(data) {
                                            $scope.apps = data;
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
                                            }

                                          });
                        }

                        $scope.setTab('new');
                      }]);
}).call(this);