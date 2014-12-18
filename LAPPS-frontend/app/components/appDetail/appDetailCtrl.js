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
                      'swaggerApi',
                      function($scope, $routeParams, swaggerApi) {
                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Maximum amount of tags, that can be
                         *              displayed in the app details.
                         */
                        $scope.MAX_TAGS = 999;

                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Amount of tags displayed in the app
                         *              details in collapsed state.
                         */
                        $scope.DEFAULT_TAGS = 5;
                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description An id is passed as a routing parameter
                         *              in the url. This way the controller can
                         *              decide, which app's details to display.
                         */
                        $scope.appId = $routeParams.appId;

                        /**
                         * @field
                         * @type boolean
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description The state of the long description area.
                         *              Important for applying correct classes,
                         *              when state changes.
                         */
                        $scope.collapsed = true;

                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Current limit on the amount of tags to
                         *              display.
                         */
                        $scope.tagLimit = $scope.DEFAULT_TAGS;

                        /**
                         * @field
                         * @type number
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Time in ms to wait until the image in
                         *              the carousel is changed.
                         */
                        $scope.interval = 4000;

                        /**
                         * @field
                         * @type app
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Stores the app object retrieved from the
                         *              backend.
                         */
                        $scope.appData = {};

                        marked.setOptions({
                          renderer: new marked.Renderer(),
                          gfm: true,
                          tables: true,
                          breaks: false,
                          pedantic: false,
                          sanitize: true,
                          smartLists: true,
                          smartypants: false
                        });

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Toggles
                         *              {@link lapps.lappsControllers.appDetailCtrl.$scope.collapsed}.
                         */

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Toggles
                         *              {@link lapps.lappsControllers.appDetailCtrl.$scope.collapsed}.
                         */

                        marked.setOptions({
                          renderer: new marked.Renderer(),
                          gfm: true,
                          tables: true,
                          breaks: false,
                          pedantic: false,
                          sanitize: true,
                          smartLists: true,
                          smartypants: false
                        });
                        $scope.expandCollapseDescription = function() {
                          if ($scope.collapsed) {
                            $scope.collapsed = false;
                          } else {
                            $scope.collapsed = true;
                          }
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @description Toggles
                         *              {@link lapps.lappsControllers.appDetailCtrl.$scope.tagLimit}
                         *              to change the amount of currently
                         *              displayed tags.
                         */
                        $scope.expandCollapseTags = function() {
                          if ($scope.tagLimit == $scope.DEFAULT_TAGS) {
                            $scope.tagLimit = $scope.MAX_TAGS;
                          } else {
                            $scope.tagLimit = $scope.DEFAULT_TAGS;
                          }
                        }

                        /**
                         * @function
                         * @type string
                         * @memberOf lapps.lappsControllers.appDetailCtrl
                         * @param {number|string}
                         *          size size in KB to convert to MB.
                         * @description Converts size in KB to MB with 1 digit
                         *              after the comma. If size is less than
                         *              1024 the value is not changed. The
                         *              string MB or KB is appended to the
                         *              result (12.5 MB).
                         */
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

                        swaggerApi.apps
                                .getApp({
                                  id: +$scope.appId
                                })
                                .then(
                                        function(data) {
                                          $scope.appData = data;
                                          $scope.appData.longDescriptionMarkdown = marked($scope.appData.longDescription);
                                          var thumbnail = '';
                                          var images = [];
                                          for (var j = 0; j < $scope.appData.artifacts.length; j++) {
                                            if ($scope.appData.artifacts[j].type
                                                    .indexOf('thumb') >= 0) {
                                              thumbnail = $scope.appData.artifacts[j].url;
                                            } else if ($scope.appData.artifacts[j].type
                                                    .indexOf('image') >= 0) {
                                              images
                                                      .push({
                                                        url: $scope.appData.artifacts[j].url,
                                                        description: $scope.appData.artifacts[j].description
                                                      });
                                            }
                                          }
                                          $scope.appData.thumbnail = thumbnail;
                                          $scope.appData.images = images;
                                        });
                      }]);
}).call(this);