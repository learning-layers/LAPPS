/**
 * @class appDetailCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for displaying the details of a
 *              selected app.
 */

(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'appDetailCtrl',
                  [
                      '$scope',
                      '$routeParams',
                      '$http',
                      function($scope, $routeParams, $http) {
                        $scope.MAX_TAGS = 999;
                        $scope.DEFAULT_TAGS = 5;
                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description An id is passed as a routing parameter
                         *              in the url. This way the controller can
                         *              decide, which (detail)-page of an app to
                         *              display.
                         */
                        $scope.appId = $routeParams.appId;

                        $scope.currentVersion = "ios";
                        $scope.collapsed = true;
                        $scope.tagLimit = $scope.DEFAULT_TAGS;

                        $scope.interval = 4000;
                        $scope.chosenVersion = function(event) {
                          $scope.currentVersion = event.target.id;
                        };

                        $scope.appData = {};

                        $scope.expandCollapseDescription = function() {
                          if ($scope.collapsed) {
                            $scope.collapsed = false;
                          } else {
                            $scope.collapsed = true;
                          }
                        }

                        $scope.expandCollapseTags = function() {
                          if ($scope.tagLimit == $scope.DEFAULT_TAGS) {
                            $scope.tagLimit = $scope.MAX_TAGS;
                          } else {
                            $scope.tagLimit = $scope.DEFAULT_TAGS;
                          }
                        }
                        $scope.convertSize = function(size) {
                          size = parseInt(size, 10);
                          if (size < 1024) {
                            return size + " KB";
                          } else {
                            var div = Math.floor(size / 1024);
                            var rem = size % 1024;
                            return div + "." + Math.round(rem / 100) + " MB";
                          }

                        }
                        $scope.convertDate = function(utc) {
                          var d = new Date(utc * 1000);
                          var m_names = new Array("January", "February",
                                  "March", "April", "May", "June", "July",
                                  "August", "September", "October", "November",
                                  "December");

                          var curr_date = d.getDate();
                          var curr_month = d.getMonth();
                          var curr_year = d.getFullYear();
                          return (curr_date + "-" + m_names[curr_month] + "-" + curr_year);

                        }

                        $http.get('assets/dummy/app-' + $scope.appId + '.json')
                                .success(function(data) {
                                  $scope.appData = data;
                                });
                      }]);
}).call(this);