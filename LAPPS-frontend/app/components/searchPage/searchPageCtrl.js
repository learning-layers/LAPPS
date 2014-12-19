/**
 * @class searchPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying a list of search
 *              results.
 */
(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'searchPageCtrl',
                  [
                      '$scope',
                      'swaggerApi',
                      '$routeParams',
                      'user',
                      'platform',
                      function($scope, swaggerApi, $routeParams, user, platform) {
                        if ($routeParams.query) {
                          $scope.searchQuery = $routeParams.query;
                        } else {
                          $scope.searchQuery = '';
                        }
                        /**
                         * @field
                         * @type app[]
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Stores the app objects retrieved from
                         *              the backend.
                         */
                        $scope.apps = [];
                        /**
                         * @field
                         * @type boolean
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description The state of the advanced search area.
                         */
                        $scope.collapsed = true;
                        /**
                         * @function
                         * @type string
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @param {number}
                         *          utc timestamp.
                         * @description Converts timestamp in date string
                         *              day-month-year.
                         */
                        $scope.convertDate = function(utc) {
                          var d = new Date(utc);
                          var m_names = new Array("January", "February",
                                  "March", "April", "May", "June", "July",
                                  "August", "September", "October", "November",
                                  "December");

                          var curr_date = d.getDate();
                          var curr_month = d.getMonth();
                          var curr_year = d.getFullYear();
                          return (curr_date + "-" + m_names[curr_month] + "-" + curr_year);
                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Searches using the current query
                         *              settings. And loads a list of apps as
                         *              results.
                         */
                        $scope.search = function() {
                          // swaggerApi.users.getAllUsers({'accessToken':user.token}).then(function(data2){
                          swaggerApi.apps
                                  .getAllApps({
                                    search: $scope.searchQuery
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

                                              $scope.apps[i].platformObj = platform
                                                      .getPlatformByName($scope.apps[i].platform);

                                              $scope.apps[i].dateCreated = $scope
                                                      .convertDate($scope.apps[i].dateCreated);
                                              $scope.apps[i].dateModified = $scope
                                                      .convertDate($scope.apps[i].dateModified);
                                            }
                                          });
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.searchPageCtrl
                         * @description Toggles
                         *              {@link lapps.lappsControllers.searchPageCtrl.$scope.collapsed}.
                         */
                        $scope.expandCollapseSearch = function() {
                          if ($scope.collapsed) {
                            $scope.collapsed = false;
                          } else {
                            $scope.collapsed = true;
                          }
                        }
                        $scope.search();
                      }]);
}).call(this);