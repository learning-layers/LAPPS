/**
 * @class editPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is for editing the app
 */
(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'editPageCtrl',
                  [
                      '$scope',
                      '$filter',
                      '$routeParams',
                      'user',
                      '$http',
                      'swaggerApi',
                      'platform',
                      function($scope, $filter, $routeParams, user, $http,
                              swaggerApi, platform) {

                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description gets appid through routeParam and sets
                         *              it to appId
                         */
                        $scope.appId = $routeParams.appId;

                        /**
                         * @field
                         * @type array of strings
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description will store tags temporary
                         */
                        $scope.appTags = [];
                        /**
                         * @field
                         * @type object
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description will store app object
                         */

                        $scope.app = {};

                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description will store size of an app
                         */
                        $scope.size = '';

                        /**
                         * @field
                         * @type array of objects
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description gets the list of platforms from platform
                         *              object
                         */
                        this.platforms = platform.platforms;

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description convert date from milliseconds to
                         *              standard date
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
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description fetch app data for appid
                         */

                        $scope.fetchApp = function() {

                          swaggerApi.apps
                                  .getApp({
                                    id: +$scope.appId,
                                  })
                                  .then(
                                          function(response) {

                                            $scope.app = response.data;
                                            $scope.dateCreated = $scope
                                                    .convertDate($scope.app.dateCreated);
                                            $scope.dateModified = new Date(
                                                    $scope
                                                            .convertDate($scope.app.dateModified));
                                            var tags = [];
                                            for (var i = 0; i < $scope.app.tags.length; i++) {
                                              tags
                                                      .push($scope.app.tags[i].value);
                                            }
                                            $scope.appTags = tags.join();
                                            var thumbnail = {};
                                            var images = [];
                                            var video = {
                                              url: '',
                                              description: '',
                                              type: ''
                                            };
                                            for (var j = 0; j < $scope.app.artifacts.length; j++) {
                                              if ($scope.app.artifacts[j].type
                                                      .indexOf('thumb') >= 0
                                                      || $scope.app.artifacts[j].type
                                                              .indexOf('thumbnail') >= 0) {
                                                thumbnail.url = $scope.app.artifacts[j].url;
                                                thumbnail.description = $scope.app.artifacts[j].description;
                                                thumbnail.type = 'thumbnail';
                                              } else if ($scope.app.artifacts[j].type
                                                      .indexOf('image') >= 0
                                                      || $scope.app.artifacts[j].type
                                                              .indexOf('image/png') >= 0) {
                                                images
                                                        .push({
                                                          url: $scope.app.artifacts[j].url,
                                                          description: $scope.app.artifacts[j].description,
                                                          type: 'image/png'
                                                        });
                                              } else if ($scope.app.artifacts[j].type
                                                      .indexOf('video') >= 0
                                                      || $scope.app.artifacts[j].type
                                                              .indexOf('video/youtube') >= 0) {
                                                video.url = $scope.app.artifacts[j].url;
                                                video.description = $scope.app.artifacts[j].description;
                                                video.type = 'video/youtube';
                                              }
                                              ;
                                            }

                                            $scope.size = $scope.app.size;
                                            $scope.thumbnail = thumbnail;
                                            $scope.images = images;
                                            $scope.video = video;
                                          });
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description shows chosen platform on dropdown
                         */

                        $scope.showPlatform = function() {

                          var selected = $filter('filter')($scope.platforms, {
                            name: $scope.app.platform
                          });
                          $scope.chosenPlatform = selected[0].name;
                          return ($scope.app.platform && selected.length)
                                  ? selected[0].name : 'Not set';

                        };

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description checks whether input in form is empty or
                         *              not
                         */

                        $scope.validateInput = function(data) {
                          if (!data || data == 'empty') { return 'Input required'; }
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @param {object}
                         *          index of the object in array to be removed
                         * @description remove image from array of images
                         */
                        $scope.removeImage = function($index) {

                          $scope.images.splice($index, 1);

                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description add more image into image fields
                         */

                        $scope.addMoreField = function() {
                          $scope.images.push({
                            url: '',
                            description: '',
                            type: ''
                          });
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description sends the edited app data back
                         */

                        $scope.sendAppData = function() {
                          // push images to artifacts
                          $scope.app.artifacts = [];
                          for (var i = 0; i < $scope.images.length; i++) {
                            if ($scope.images[i].url) {
                              $scope.app.artifacts.push($scope.images[i]);
                            }
                          }
                          // check if minPlatformRequired is empty
                          if (!$scope.app.minPlatformRequired) {
                            $scope.app.minPlatformRequired = "not defined";
                          }
                          // check if sourceUrl is empty
                          if (!$scope.app.sourceUrl) {
                            $scope.app.sourceUrl = "not defined";
                          }
                          // check if license is empty
                          if (!scope.app.license) {
                            $scope.app.license = "not defined";
                          }
                          // set platform
                          $scope.app.platform = $scope.chosenPlatform;
                          // push video to artifacts
                          $scope.app.artifacts.push($scope.video);
                          // push thumbnail to artifacts
                          $scope.app.artifacts.push($scope.thumbnail);

                          // split tags, ignore white spaces
                          var tempTags = [];
                          $scope.app.tags = [];
                          tempTags = $scope.appTags
                                  .match(/(?=\S)[^,]+?(?=\s*(,|$))/g);
                          for (var i = 0; i < tempTags.length; i++) {
                            $scope.app.tags.push({
                              value: tempTags[i]
                            });
                          }
                          $scope.app.dateCreated = $scope.app.dateCreated
                                  .toString();
                          $scope.app.dateModified = Date.parse(
                                  $scope.dateModified).toString();
                          ;

                          $scope.app.size = parseInt($scope.size);
                          $scope.app.creator.dateRegistered = $scope.app.creator.dateRegistered
                                  .toString();

                          swaggerApi.apps.updateApp({
                            accessToken: 'test_token',
                            id: +$scope.appId,
                            body: $scope.app
                          }).then(function(response) {
                            alert('Success');
                          });
                        }
                        // calling fetchapp function
                        $scope.fetchApp();
                      }]);

}).call(this);
