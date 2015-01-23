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

                      '$routeParams',
                      'user',
                      '$modal',
                      'swaggerApi',
                      'platform',
                      'convert',
                      '$location',
                      function($scope, $routeParams, user, $modal, swaggerApi,
                              platform, convert, $location) {

                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description Gets appid through routeParam and sets
                         *              it to appId.
                         */
                        $scope.appId = $routeParams.appId;

                        /**
                         * @field
                         * @type string[]
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description Stores tags temporary.
                         */
                        $scope.appTags = [];
                        /**
                         * @field
                         * @type object
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description Stores app object.
                         */

                        $scope.app = {};

                        /**
                         * @field
                         * @type string
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @descriptione Stores size of an app.
                         */
                        $scope.size = '';

                        /**
                         * @field
                         * @type platform[]
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description Gets the list of platforms from platform
                         *              object, needed for select menu.
                         */
                        this.platforms = platform.platforms;

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description Fetch app data for appId.
                         */

                        $scope.fetchApp = function() {

                          swaggerApi.apps
                                  .getApp({
                                    id: +$scope.appId,
                                  })
                                  .then(
                                          function(response) {

                                            $scope.app = response.data;
                                            $scope.dateCreated = convert
                                                    .date($scope.app.dateCreated);
                                            $scope.app.dateModified = ''
                                                    + $scope.app.dateModified;
                                            var tags = [];
                                            for (var i = 0; i < $scope.app.tags.length; i++) {
                                              tags
                                                      .push($scope.app.tags[i].value);
                                            }
                                            $scope.appTags = tags.join(', ');
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
                                            $scope.displayedPlatform = $scope
                                                    .showPlatform();
                                          });
                        }

                        /**
                         * @function
                         * @type string
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description Shows chosen platform on dropdown.
                         */

                        $scope.showPlatform = function() {

                          var platformData = platform
                                  .getPlatformByName($scope.app.platform);
                          var selected = platformData;

                          $scope.chosenPlatform = platformData.name;
                          return ($scope.app.platform && selected !== null)
                                  ? selected.name : 'Not set';

                        };

                        /**
                         * @function
                         * @type string
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description Checks whether input in form is empty or
                         *              not.
                         */

                        $scope.validateInput = function(data) {
                          if (!data || data == 'empty') { return 'Input required'; }
                        }
                        /**
                         * @function
                         * @type string
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description Checks wheter input in form is an
                         *              integer.
                         */

                        $scope.validateNumericalInput = function(data) {
                          if (isNaN(data) || data < 0 || data % 1 !== 0) { return 'Input must be a positive integer.'; }
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description Checks whether the given url is a valid
                         *              youtube video url.
                         */

                        $scope.validateVideoInput = function(data) {
                          if (data.indexOf('//www.youtube.com/embed/') < 0) { return 'Only URLs containing www.youtube.com/embed/ are accepted.'; }
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @param {object}
                         *          $index index of the object in array to be
                         *          removed
                         * @description Remove image from array of images.
                         */
                        $scope.removeImage = function($index) {

                          $scope.images.splice($index, 1);

                        }
                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description Add more images into image fields.
                         */

                        $scope.addMoreField = function() {
                          $scope.images.push({
                            url: '',
                            description: '',
                            type: 'image/png'
                          });
                        }

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.editPageCtrl
                         * @description Sends the edited app data to the
                         *              backend.
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
                            $scope.app.minPlatformRequired = 'not defined';
                          }
                          // check if sourceUrl is empty
                          if (!$scope.app.sourceUrl) {
                            $scope.app.sourceUrl = 'not defined';
                          }
                          // check if license is empty
                          if (!$scope.app.license) {
                            $scope.app.license = 'not defined';
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
                          tempTags = $scope.appTags.split(',');
                          for (var i = 0; i < tempTags.length; i++) {
                            $scope.app.tags.push({
                              value: tempTags[i].trim()
                            });
                          }
                          $scope.app.dateCreated = $scope.app.dateCreated
                                  .toString();

                          $scope.app.size = parseInt($scope.size);
                          $scope.app.creator.dateRegistered = $scope.app.creator.dateRegistered
                                  .toString();

                          swaggerApi.apps
                                  .updateApp({
                                    accessToken: user.token,
                                    id: +$scope.appId,
                                    body: $scope.app
                                  })
                                  .then(
                                          function(response) {
                                            var modalInstance = $modal
                                                    .open({
                                                      templateUrl: 'components/editAppPage/submitConfirmView.html',
                                                      controller: 'submitConfirmCtrl',
                                                      size: 'xs',
                                                    });
                                            modalInstance.result.then(function(
                                                    isOk) {
                                              if (isOk) {
                                                $location.path('/apps/'
                                                        + $scope.app.id);
                                              }
                                            }, function() {
                                            });
                                          });
                        }
                        // calling fetchapp function
                        $scope.fetchApp();
                      }]);

}).call(this);
