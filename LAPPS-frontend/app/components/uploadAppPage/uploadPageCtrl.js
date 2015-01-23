/**
 * @class uploadPageCtrl
 * @memberOf lapps.lappsControllers
 * @description This controller is responsible for uploading apps
 */
(function() {
  angular
          .module('lappsControllers')
          .controller(
                  'uploadPageCtrl',
                  [
                      '$scope',
                      '$http',
                      'swaggerApi',
                      'user',
                      'platform',
                      '$modal',
                      '$location',
                      function($scope, $http, swaggerApi, user, platform,
                              $modal, $location) {

                        // dummy userId
                        $scope.userId = 390532463;

                        /**
                         * @field
                         * @type object
                         * @memberOf lapps.lappsControllers.uploadPageCtrl
                         * @description New app object.
                         */

                        $scope.newapp = {};
                        /**
                         * @field
                         * @type object
                         * @memberOf lapps.lappsControllers.uploadPageCtrl
                         * @description Temporary tags object.
                         */

                        $scope.tags = {};

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.uploadPageCtrl
                         * @description Resets valus of newapp object.
                         */
                        $scope.reset = function() {

                          $scope.$broadcast('show-errors-reset');

                          $scope.newapp = {
                            id: 0,
                            name: '',
                            platform: '',
                            minPlatformRequired: '',
                            downloadUrl: '',
                            version: '',
                            size: 0,
                            sourceUrl: '',
                            supportUrl: '',
                            rating: 0,
                            dateCreated: '',
                            dateModified: '',
                            license: '',
                            shortDescription: '',
                            longDescription: '',
                            creator: {
                              oidcId: 0,
                              email: '',
                              username: '',
                              role: 0,
                              dateRegistered: '',
                              description: '',
                              website: ''
                            },
                            artifacts: [],
                            tags: []
                          }

                          $scope.tags.value = '';
                          $scope.video = {};
                          $scope.thumbnail = {};
                          $scope.images = [];
                          $scope.dateModified = '';

                        }

                        $scope.reset();

                        var counter = 0;

                        /**
                         * @field
                         * @type object[]
                         * @memberOf lapps.lappsControllers.uploadPageCtrl
                         * @description Fields to be generated for image in
                         *              images.
                         */

                        $scope.newFields = [{
                          id: 0,
                          buttonName: 'add',
                          buttonTitle: 'Add Image'
                        }];

                        /**
                         * @field
                         * @type object
                         * @memberOf lapps.lappsControllers.uploadPageCtrl
                         * @description Object for video.
                         */
                        $scope.video = {};

                        /**
                         * @field
                         * @type object
                         * @memberOf lapps.lappsControllers.uploadPageCtrl
                         * @description Object for thumbnail.
                         */
                        $scope.thumbnail = {};

                        /**
                         * @field
                         * @type object[]
                         * @memberOf lapps.lappsControllers.uploadPageCtrl
                         * @description array of images for carousel
                         */

                        $scope.images = [];

                        /**
                         * @field
                         * @type object[]
                         * @memberOf lapps.lappsControllers.uploadPageCtrl
                         * @description Gets list of platforms from platform
                         *              object, neded for dropdown.
                         */
                        this.platforms = platform.platforms;

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.uploadPageCtrl
                         * @description If form is valid creates new app object
                         *              inputs in form and sends the object
                         *              through swaggerApi.
                         */
                        $scope.createNewApp = function() {

                          $scope.$broadcast('show-errors-check-validity');
                          // check if form is valid

                          if ($scope.uploadForm.$valid) {
                            $scope.newapp.creator.oidcId = $scope.userId;

                            // split tags and delete white spaces

                            if ($scope.tags.value) {
                              var tempTags = [];
                              tempTags = $scope.tags.value.split(',');

                              for (var i = 0; i < tempTags.length; i++) {
                                $scope.newapp.tags.push({
                                  value: tempTags[i].trim()
                                });
                              }
                            }

                            // create new artifacts from each image url

                            for (var i = 0; i < $scope.images.length; i++) {
                              if ($scope.images[i].url) {
                                $scope.newapp.artifacts.push({
                                  url: $scope.images[i].url,
                                  description: $scope.images[i].description,
                                  type: 'image/png'
                                })
                              }
                            }
                            // create another artifact for video
                            if ($scope.video.url) {
                              $scope.newapp.artifacts.push({
                                url: $scope.video.url,
                                description: $scope.video.description,
                                type: 'video/youtube'
                              })
                            }
                            // create another artifact for thumbnail
                            $scope.newapp.artifacts.push({
                              url: $scope.thumbnail.url,
                              description: '',
                              type: 'thumbnail'
                            })
                            if ($scope.dateModified) {
                              $scope.newapp.dateModified = Date.parse(
                                      $scope.dateModified).toString();
                            }

                            $scope.newapp.size = parseInt($scope.newapp.size);

                            // send app object through swaggerApi
                            swaggerApi.apps
                                    .createApp({
                                      accessToken: 'test_token',
                                      body: $scope.newapp
                                    })
                                    .then(
                                            function(response) {
                                              var modalInstance = $modal
                                                      .open({
                                                        templateUrl: 'components/uploadAppPage/submitConfirmView.html',
                                                        controller: 'submitConfirmCtrl',
                                                        size: 'xs',
                                                      });
                                              modalInstance.result
                                                      .then(
                                                              function(isOk) {
                                                                if (isOk) {
                                                                  $location
                                                                          .path('/apps/'
                                                                                  + response.data.id);
                                                                }
                                                              }, function() {
                                                              });

                                            });
                          }
                        };

                        /**
                         * @function
                         * @memberOf lapps.lappsControllers.uploadPageCtrl
                         * @param {object}
                         *          event
                         * @description For current event checks the name of the
                         *              currentTarget and if it is 'add' then
                         *              adds object to newFields array, else
                         *              calls function to find object at index
                         *              then delete the object.
                         */

                        $scope.addAnotherField = function($event) {
                          if ($event.currentTarget.name == 'add') {
                            counter++;
                            $scope.newFields.push({
                              id: counter,
                              buttonName: 'remove' + counter,
                              buttonTitle: 'Remove'
                            });
                          } else {
                            var currentIndex = $scope
                                    .findElementInArray($event.currentTarget['data-id']);

                            $scope.newFields.splice(currentIndex, 1);

                          }
                          $event.preventDefault();
                        };

                        /**
                         * @function
                         * @type number
                         * @memberOf lapps.lappsControllers.uploadPageCtrl
                         * @param {object}
                         *          index index of array
                         * @description Finds element in array for given index.
                         */

                        $scope.findElementInArray = function(index) {
                          for (var i = 0; i < $scope.newFields.length; i++) {
                            if ($scope.newFields[i].id == index) { return $scope.newFields
                                    .indexOf($scope.newFields[i]); }
                          }
                          return -1;
                        };

                        /*
                         * $scope.test = function() {
                         * 
                         * $scope.newapp = { id: 0, name: 'Test App', platform:
                         * 'Windows', minPlatformRequired: 'XP', downloadUrl:
                         * "http://google.com", version: '1.1', size: 3000,
                         * sourceUrl: 'http://google.com', supportUrl:
                         * 'http://google.com', rating: 3.5, dateCreated:
                         * '1421617299101', dateModified: '1421617299101',
                         * license: 'Copyright 2014', shortDescription:
                         * 'Consequat. Tempor ut cupidatat quis anim amet, irure
                         * ad ea' , longDescription: "##Consectetur ## \n
                         * adasdasdasd", creator: { oidcId: 390532463, email:
                         * '', username: '', role: 0, dateRegistered: '',
                         * description: '', website: '' }, artifacts: [], tags: [] }
                         * 
                         * $scope.tags.value = 'windowsapp,windows'; }
                         * 
                         * $scope.test();
                         */

                      }]);
}).call(this);